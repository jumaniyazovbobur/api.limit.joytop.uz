package uz.dachatop.repository.place.dacha;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import uz.dachatop.dto.place.PlaceFilterResult;
import uz.dachatop.dto.place.dacha.DachaFilterDTO;
import uz.dachatop.enums.AppLanguage;
import uz.dachatop.enums.GlobalStatus;
import uz.dachatop.enums.PlaceSubType;
import uz.dachatop.enums.PlaceType;
import uz.dachatop.mapper.apartment.ApartmentMapperDTO;
import uz.dachatop.mapper.dacha.DachaMapperDTO;
import uz.dachatop.util.MapperUtil;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class DachaCustomFilter {
    @Autowired
    private EntityManager entityManager;

    public PlaceFilterResult<DachaMapperDTO> filter(DachaFilterDTO filter, AppLanguage lang, int page, int size) {
        StringBuilder builder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        builder.append(" where d.visible is true and d.status = 'ACTIVE' ");
        if (filter.getRegionId() != null) { // branchList
            builder.append(" and d.region_id =:regionId ");
            params.put("regionId", filter.getRegionId());
            if (filter.getDistrictId() != null) { // branchList
                builder.append(" and d.district_id =:districtId ");
                params.put("districtId", filter.getDistrictId());
            }
        }
        if (filter.getName() != null) {
            builder.append(" and lower(d.name) like :name ");
            params.put("name", "%" + filter.getName().toLowerCase().concat("%"));
        }

        if (filter.getPriceFrom() != null && filter.getPriceTo() != null) {
            builder.append(" and d.week_day_price between :priceForm and :priceTo ");
            params.put("priceForm", filter.getPriceFrom());
            params.put("priceTo", filter.getPriceTo());
        } else if (filter.getPriceFrom() != null) {
            builder.append(" and d.week_day_price >= :priceForm ");
            params.put("priceForm", filter.getPriceFrom());
        } else if (filter.getPriceTo() != null) {
            builder.append(" and d.week_day_price <=:priceTo ");
            params.put("priceTo", filter.getPriceTo());
        }
        if (filter.getRoomCount() != null) {
            builder.append(" and d.room_count =:roomCount ");
            params.put("roomCount", filter.getRoomCount());
        }
        if (filter.getBedRoomCount() != null) {
            builder.append(" and  (d.single_bed_room_count=:bedRoomCount or d.double_bed_room_count=:bedRoomCount) ");
            params.put("bedRoomCount", filter.getBedRoomCount());
        }
        if (filter.getConvenienceList() != null && filter.getConvenienceList().size() > 0) {
            builder.append(" and exists (select * from place_convenience where place_id = d.id and convenience_id in (:convenienceList))");
            params.put("convenienceList", filter.getConvenienceList());
        }
        if (filter.getType() != null) {
            builder.append(" and d.type =:apartmentType ");
            params.put("apartmentType", filter.getType().toString());
        }
        if (filter.getNumber() != null) {
            builder.append(" and d.number = :number ");
            params.put("number", filter.getNumber());
        }
        if (filter.getSubType() != null) {
            builder.append(" and d.sub_type = :subType ");
            params.put("subType", filter.getSubType().toString());
        }


        StringBuilder selectQueryBuilder = new StringBuilder();
        selectQueryBuilder.append(" select d.id,");
        selectQueryBuilder.append(" region.id, case :lang when 'uz' then region.name_uz when 'en' then region.name_en else region.name_ru end as rName,");
        selectQueryBuilder.append(" district.id, case :lang when 'uz' then district.name_uz when 'en' then district.name_en else district.name_ru end as dName,");
        selectQueryBuilder.append(" d.latitude, d.longitude, d.week_day_price, d.weekend_price, d.total_area,");
        selectQueryBuilder.append(" d.single_bed_room_count, d.double_bed_room_count, d.type,");
        selectQueryBuilder.append(" (select attach_id from place_attach where place_id = d.id limit 1) as mainAttach, ");
        selectQueryBuilder.append(" d.name, d.created_date, d.number, d.sub_type, d.sale_price, ");
        selectQueryBuilder.append(" (select string_agg(concat(case :lang when 'uz' then tariff.name_uz when 'en' then tariff.name_en else tariff.name_ru end,',',tariff.color) , '; ') ");
        selectQueryBuilder.append(" from subscription s inner join subscription_tariff tariff on s.tariff_id = tariff.id where s.place_id = d.id  ");
        selectQueryBuilder.append(" and s.place_type = 'COTTAGE' and s.status = 'ACTIVE' ) ");
        selectQueryBuilder.append(" from dacha d ");
        selectQueryBuilder.append(" inner join region on region.id = d.region_id ");
        selectQueryBuilder.append(" left join district on district.id = d.district_id ");
        selectQueryBuilder.append(builder);

        StringBuilder premiumQueryBuilder = new StringBuilder();
        premiumQueryBuilder.append(selectQueryBuilder);
        premiumQueryBuilder.append(" and d.id in (select distinct place_id from subscription where place_type = 'COTTAGE' and status = 'ACTIVE' ) ");
        premiumQueryBuilder.append(" order by d.created_date desc ");
        params.put("lang", lang.toString());

        Query premiumCotageQuery = entityManager.createNativeQuery(premiumQueryBuilder.toString());

        for (Map.Entry<String, Object> param : params.entrySet()) {
            premiumCotageQuery.setParameter(param.getKey(), param.getValue());
        }
        List<Object[]> premiumCotageList = premiumCotageQuery.getResultList();

        List<String> premiumIds = premiumCotageList
                .stream()
                .map(objects -> MapperUtil.getStringValue(objects[0]))
                .toList();

        selectQueryBuilder.append(premiumIds.isEmpty() ? " " : " and d.id not in (:premiumIds) ");
        selectQueryBuilder.append(" order by d.created_date desc ");
        selectQueryBuilder.append(" limit ").append(size).append(" offset ").append(page).append(";");

        if (!premiumIds.isEmpty()){
            params.put("premiumIds", premiumIds);
        }
        String countQueryBuilder = "select count(*) from dacha as d " + builder + ";";

        Query selectQuery = entityManager.createNativeQuery(selectQueryBuilder.toString());
        Query countQuery = entityManager.createNativeQuery(countQueryBuilder.toString());
        for (Map.Entry<String, Object> param : params.entrySet()) {
            selectQuery.setParameter(param.getKey(), param.getValue());
            if (!param.getKey().equals("lang")) {
                if (!param.getKey().equals("premiumIds")) {
                    countQuery.setParameter(param.getKey(), param.getValue());
                }
            }
        }
        List<Object[]> apartmentList = selectQuery.getResultList();
        Long totalCount = (Long) countQuery.getSingleResult();

        List<DachaMapperDTO> mapperList = new LinkedList<>();

        if (page == 0) mapperList.addAll(convert(premiumCotageList));

        mapperList.addAll(convert(apartmentList));

        return new PlaceFilterResult(mapperList, totalCount);
    }

    private List<DachaMapperDTO> convert(List<Object[]> apartmentList) {
        List<DachaMapperDTO> mapperList = new LinkedList<>();
        for (Object[] object : apartmentList) {
            DachaMapperDTO placeMapperDTO = new DachaMapperDTO();
            placeMapperDTO.setId(MapperUtil.getStringValue(object[0]));
            placeMapperDTO.setRegionId(MapperUtil.getLongValue(object[1]));
            placeMapperDTO.setRegionName(MapperUtil.getStringValue(object[2]));
            placeMapperDTO.setDistrictId(MapperUtil.getLongValue(object[3]));
            placeMapperDTO.setDistrictName(MapperUtil.getStringValue(object[4]));
            placeMapperDTO.setLatitude(MapperUtil.getDoubleValue(object[5]));
            placeMapperDTO.setLongitude(MapperUtil.getDoubleValue(object[6]));
            placeMapperDTO.setWeekDayPrice(MapperUtil.getLongValue(object[7]));
            placeMapperDTO.setWeekendPrice(MapperUtil.getLongValue(object[8]));
            placeMapperDTO.setTotalArea(MapperUtil.getDoubleValue(object[9]));
            placeMapperDTO.setSingleBedRoomCount(MapperUtil.getIntegerValue(object[10]));
            placeMapperDTO.setDoubleBedRoomCount(MapperUtil.getIntegerValue(object[11]));
            placeMapperDTO.setPlaceType(PlaceType.valueOf(MapperUtil.getStringValue(object[12])));
            placeMapperDTO.setMainAttachId(MapperUtil.getStringValue(object[13]));
            placeMapperDTO.setDachaName(MapperUtil.getStringValue(object[14]));
            placeMapperDTO.setCreatedDate(MapperUtil.getStringValue(object[15]));
            placeMapperDTO.setNumber(MapperUtil.getLongValue(object[16]));
            placeMapperDTO.setSubType(MapperUtil.getStringValue(object[17]) != null ? PlaceSubType.valueOf(MapperUtil.getStringValue(object[17])) : null);
            placeMapperDTO.setSalePrice(MapperUtil.getLongValue(object[18]));
            placeMapperDTO.setTariffResult(MapperUtil.getStringValue(object[19]));
            mapperList.add(placeMapperDTO);
        }
        return mapperList;
    }
    // no need to implement
    /*public PlaceFilterResult<DachaMapperDTO> adminFilter(DachaFilterDTO filter, AppLanguage lang, int page, int size) {
        StringBuilder builder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        builder.append(" where d.visible = true ");
        if (filter.getRegionId() != null) { // branchList
            builder.append(" and d.region_id =:regionId ");
            params.put("regionId", filter.getRegionId());
            if (filter.getDistrictId() != null) { // branchList
                builder.append(" and d.district_id =:districtId ");
                params.put("districtId", filter.getDistrictId());
            }
        }
        if (filter.getPriceFrom() != null && filter.getPriceTo() != null) {
            builder.append(" and d.week_day_price between :priceForm and :priceTo ");
            params.put("priceForm", filter.getPriceFrom());
            params.put("priceTo", filter.getPriceTo());
        } else if (filter.getPriceFrom() != null) {
            builder.append(" and d.week_day_price >= :priceForm ");
            params.put("priceForm", filter.getPriceFrom());
        } else if (filter.getPriceTo() != null) {
            builder.append(" and d.week_day_price <=:priceTo ");
            params.put("priceTo", filter.getRoomCount());
        }
        if (filter.getRoomCount() != null) {
            builder.append(" and d.room_count =:roomCount ");
            params.put("roomCount", filter.getRoomCount());
        }
        if (filter.getBedRoomCount() != null) {
            builder.append(" and  (d.single_bed_room_count=:bedRoomCount or d.double_bed_room_count=:bedRoomCount) ");
            params.put("bedRoomCount", filter.getBedRoomCount());
        }
        if (filter.getConvenienceList() != null && filter.getConvenienceList().size() > 0) {
            builder.append(" and exists (select * from place_convenience where apartment_id = ap.id and convenience_id in (:convenienceList))");
            params.put("convenienceList", filter.getConvenienceList());
        }
        if (filter.getType() != null) {
            builder.append(" and d.type =:apartmentType");
            params.put("apartmentType", filter.getType());
        }

        StringBuilder selectQueryBuilder = new StringBuilder();
        selectQueryBuilder.append(" select d.id,");
        selectQueryBuilder.append(" region.id, case :lang when 'uz' then region.name_uz when 'en' then region.name_en else region.name_ru end as rName,");
        selectQueryBuilder.append(" district.id, case :lang when 'uz' then district.name_uz when 'en' then district.name_en else district.name_ru end as dName,");
        selectQueryBuilder.append(" d.latitude, d.longitude, d.week_day_price, d.weekend_price, d.total_area,");
        selectQueryBuilder.append(" d.single_bed_room_count, d.double_bed_room_count, d.type, ");
        selectQueryBuilder.append(" (select attach_id from apartment_attach where apartment_id = d.id limit 1) as mainAttach,");
        selectQueryBuilder.append(" d.status ");
        selectQueryBuilder.append(" from apardachatment d").append(builder);
        selectQueryBuilder.append(" limit ").append(size).append(" offset ").append(page * size).append(";");
        params.put("lang", lang);
        String countQueryBuilder = "select count(*) from apartment as d " + builder + ";";

        Query selectQuery = entityManager.createNativeQuery(selectQueryBuilder.toString());
        Query countQuery = entityManager.createNativeQuery(countQueryBuilder.toString());
        for (Map.Entry<String, Object> param : params.entrySet()) {
            selectQuery.setParameter(param.getKey(), param.getValue());
            countQuery.setParameter(param.getKey(), param.getValue());
        }
        List<Object[]> apartmentList = selectQuery.getResultList();
        BigInteger totalCount = (BigInteger) countQuery.getSingleResult();
        List<ApartmentMapperDTO> mapperList = new LinkedList<>();
        for (Object[] object : apartmentList) {
            DachaMapperDTO organizationMapperDTO = new DachaMapperDTO();
            organizationMapperDTO.setId(MapperUtil.getStringValue(object[0]));
            organizationMapperDTO.setRegionId(MapperUtil.getLongValue(object[1]));
            organizationMapperDTO.setRegionName(MapperUtil.getStringValue(object[2]));
            organizationMapperDTO.setDistrictId(MapperUtil.getLongValue(object[3]));
            organizationMapperDTO.setDistrictName(MapperUtil.getStringValue(object[4]));
            organizationMapperDTO.setLatitude(MapperUtil.getDoubleValue(object[5]));
            organizationMapperDTO.setLatitude(MapperUtil.getDoubleValue(object[6]));
            organizationMapperDTO.setWeekDayPrice(MapperUtil.getLongValue(object[7]));
            organizationMapperDTO.setWeekendPrice(MapperUtil.getLongValue(object[8]));
            organizationMapperDTO.setTotalArea(MapperUtil.getDoubleValue(object[9]));
            organizationMapperDTO.setSingleBedRoomCount(MapperUtil.getIntegerValue(object[10]));
            organizationMapperDTO.setDoubleBedRoomCount(MapperUtil.getIntegerValue(object[11]));
            organizationMapperDTO.setPlaceType(PlaceType.valueOf(MapperUtil.getStringValue(object[12])));
            organizationMapperDTO.setMainAttachId(MapperUtil.getStringValue(object[13]));
            organizationMapperDTO.setStatus(GlobalStatus.valueOf(MapperUtil.getStringValue(object[14])));
            mapperList.add(organizationMapperDTO);
        }
        return new PlaceFilterResult(mapperList, totalCount.longValue());
    }
*/
}
