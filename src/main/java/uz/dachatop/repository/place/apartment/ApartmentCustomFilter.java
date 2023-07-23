package uz.dachatop.repository.place.apartment;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import uz.dachatop.dto.place.PlaceFilterResult;
import uz.dachatop.dto.place.apartment.ApartmentFilterDTO;
import uz.dachatop.enums.AppLanguage;
import uz.dachatop.enums.PlaceSubType;
import uz.dachatop.enums.PlaceType;
import uz.dachatop.mapper.apartment.ApartmentMapperDTO;
import uz.dachatop.util.MapperUtil;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Repository
public class ApartmentCustomFilter {
    @Autowired
    private EntityManager entityManager;

    public PlaceFilterResult<ApartmentMapperDTO> filter(ApartmentFilterDTO filter, AppLanguage lang, int page, int size) {
        StringBuilder builder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        builder.append(" where ap.visible = true and ap.status = 'ACTIVE' ");
        if (filter.getRegionId() != null) { // branchList
            builder.append(" and ap.region_id =:regionId ");
            params.put("regionId", filter.getRegionId());
            if (filter.getDistrictId() != null) { // branchList
                builder.append(" and ap.district_id =:districtId ");
                params.put("districtId", filter.getDistrictId());
            }
        }
        if (filter.getPriceFrom() != null && filter.getPriceTo() != null) {
            builder.append(" and ap.month_price between :priceForm and :priceTo ");
            params.put("priceForm", filter.getPriceFrom());
            params.put("priceTo", filter.getPriceTo());
        } else if (filter.getPriceFrom() != null) {
            builder.append(" and ap.month_price >= :priceForm ");
            params.put("priceForm", filter.getPriceFrom());
        } else if (filter.getPriceTo() != null) {
            builder.append(" and ap.month_price <=:priceTo ");
            params.put("priceTo", filter.getPriceTo());
        }
        if (filter.getRoomCount() != null) {
            builder.append(" and ap.room_count =:roomCount ");
            params.put("roomCount", filter.getRoomCount());
        }
        if (filter.getTotalArea() != null) {
            builder.append(" and ap.total_area =:totalArea ");
            params.put("totalArea", filter.getTotalArea());
        }
        if (filter.getSingleBedRoomCount() != null) {
            builder.append(" and ap.single_bed_room_count=:singleBedRoomCount ");
            params.put("singleBedRoomCount", filter.getSingleBedRoomCount());
        }
        if (filter.getSingleBedRoomCount() != null) {
            builder.append(" and ap.double_bed_room_count=:doubleBedRoomCount ");
            params.put("doubleBedRoomCount", filter.getDoubleBedRoomCount());
        }
        if (filter.getConvenienceList() != null && filter.getConvenienceList().size() > 0) {
            builder.append(" and exists (select * from place_convenience where place_id = ap.id and convenience_id in (:convenienceList))");
            params.put("convenienceList", filter.getConvenienceList());
        }
        if (filter.getType() != null) {
            builder.append(" and ap.type =:placeType ");
            params.put("placeType", filter.getType().toString());
        }
        if (filter.getSubType() != null) {
            builder.append(" and ap.sub_type =:subType ");
            params.put("subType", filter.getSubType().toString());
        }

        StringBuilder selectQueryBuilder = new StringBuilder();
        selectQueryBuilder.append(" select ap.id,");
        selectQueryBuilder.append(" region.id, case :lang when 'uz' then region.name_uz when 'en' then region.name_en else region.name_ru end as rName,");
        selectQueryBuilder.append(" district.id, case :lang when 'uz' then district.name_uz when 'en' then district.name_en else district.name_ru end as dName,");
        selectQueryBuilder.append(" ap.latitude, ap.longitude, ap.day_price, ap.month_price, ap.total_area,");
        selectQueryBuilder.append(" ap.single_bed_room_count, ap.double_bed_room_count, ap.type,");
        selectQueryBuilder.append(" (select attach_id from place_attach where place_id = ap.id limit 1) as mainAttach,");
        selectQueryBuilder.append(" ap.sub_type, ap.created_date, ap.sale_price, ap.room_count, ");
        selectQueryBuilder.append(" (select string_agg(concat(case :lang when 'uz' then tariff.name_uz when 'en' then tariff.name_en else tariff.name_ru end,',',tariff.color) , '; ') ");
        selectQueryBuilder.append(" from subscription s inner join subscription_tariff tariff on s.tariff_id = tariff.id where s.place_id = ap.id ");
        selectQueryBuilder.append(" and s.place_type = 'APARTMENT' and s.status = 'ACTIVE' )");
        selectQueryBuilder.append(" from apartment ap ");
        selectQueryBuilder.append(" inner join region on region.id = ap.region_id ");
        selectQueryBuilder.append(" inner join district on district.id = ap.district_id ");
        selectQueryBuilder.append(builder);

        //
        StringBuilder premiumQueryBuilder = new StringBuilder();
        premiumQueryBuilder.append(selectQueryBuilder);
        premiumQueryBuilder.append(" and ap.id in (select distinct place_id from subscription where place_type = 'APARTMENT' and status = 'ACTIVE' ) ");
        premiumQueryBuilder.append(" order by ap.created_date desc ");
        //set lang
        params.put("lang", lang.toString());

        Query premiumCotageQuery = entityManager.createNativeQuery(premiumQueryBuilder.toString());

        for (Map.Entry<String, Object> param : params.entrySet()) {
            premiumCotageQuery.setParameter(param.getKey(), param.getValue());
        }
        List<Object[]> premiumApartmentList = premiumCotageQuery.getResultList();

        List<String> premiumIds = premiumApartmentList
                .stream()
                .map(objects -> MapperUtil.getStringValue(objects[0]))
                .toList();

        selectQueryBuilder.append(premiumIds.isEmpty() ? " " : " and ap.id not in (:premiumIds) ");
        selectQueryBuilder.append(" order by ap.created_date desc ");
        selectQueryBuilder.append(" limit ").append(size).append(" offset ").append(page).append(";");
        // set premium ids
        if (!premiumIds.isEmpty()) {
            params.put("premiumIds", premiumIds);
        }


        String countQueryBuilder = "select count(*) from apartment as ap " + builder + ";";

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
        List<ApartmentMapperDTO> mapperList = new LinkedList<>();
        //
        if (page == 0) mapperList.addAll(convert(premiumApartmentList));

        mapperList.addAll(convert(apartmentList));

        return new PlaceFilterResult<>(mapperList, totalCount);
    }

    private List<ApartmentMapperDTO> convert(List<Object[]> apartmentList) {
        List<ApartmentMapperDTO> mapperList = new LinkedList<>();
        for (Object[] object : apartmentList) {
            ApartmentMapperDTO mapperDTO = new ApartmentMapperDTO();
            mapperDTO.setId(MapperUtil.getStringValue(object[0]));
            mapperDTO.setRegionId(MapperUtil.getLongValue(object[1]));
            mapperDTO.setRegionName(MapperUtil.getStringValue(object[2]));
            mapperDTO.setDistrictId(MapperUtil.getLongValue(object[3]));
            mapperDTO.setDistrictName(MapperUtil.getStringValue(object[4]));
            mapperDTO.setLatitude(MapperUtil.getDoubleValue(object[5]));
            mapperDTO.setLongitude(MapperUtil.getDoubleValue(object[6]));
            mapperDTO.setDayPrice(MapperUtil.getLongValue(object[7]));
            mapperDTO.setMonthPrice(MapperUtil.getLongValue(object[8]));
            mapperDTO.setTotalArea(MapperUtil.getDoubleValue(object[9]));
            mapperDTO.setSingleBedRoomCount(MapperUtil.getIntegerValue(object[10]));
            mapperDTO.setDoubleBedRoomCount(MapperUtil.getIntegerValue(object[11]));
            mapperDTO.setPlaceType(PlaceType.valueOf(MapperUtil.getStringValue(object[12])));
            mapperDTO.setMainAttachId(MapperUtil.getStringValue(object[13]));
            mapperDTO.setSubType(MapperUtil.getStringValue(object[14]) == null ? null : PlaceSubType.valueOf(MapperUtil.getStringValue(object[14])));
            mapperDTO.setCreatedDate(MapperUtil.getStringValue(object[15]));
            mapperDTO.setSalePrice(MapperUtil.getLongValue(object[16]));
            mapperDTO.setRoomCount(MapperUtil.getIntegerValue(object[17]));
            mapperDTO.setTariffResult(MapperUtil.getStringValue(object[18]));
            mapperList.add(mapperDTO);
        }
        return mapperList;
    }
}
