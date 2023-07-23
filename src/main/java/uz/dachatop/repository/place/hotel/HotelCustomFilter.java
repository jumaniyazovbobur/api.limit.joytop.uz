package uz.dachatop.repository.place.hotel;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import uz.dachatop.dto.place.PlaceFilterResult;
import uz.dachatop.dto.place.dacha.DachaFilterDTO;
import uz.dachatop.dto.place.hotel.HotelFilterDTO;
import uz.dachatop.enums.AppLanguage;
import uz.dachatop.enums.PlaceType;
import uz.dachatop.mapper.dacha.DachaMapperDTO;
import uz.dachatop.mapper.hotel.HotelMapperDTO;
import uz.dachatop.util.MapperUtil;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Repository
public class HotelCustomFilter {
    @Autowired
    private EntityManager entityManager;

    public PlaceFilterResult<HotelMapperDTO> filter(HotelFilterDTO filter, AppLanguage lang, int page, int size) {
        StringBuilder builder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        builder.append(" where h.visible = true and h.status = 'ACTIVE' ");
        if (filter.getRegionId() != null) { // branchList
            builder.append(" and h.region_id =:regionId ");
            params.put("regionId", filter.getRegionId());
            if (filter.getDistrictId() != null) { // branchList
                builder.append(" and h.district_id =:districtId ");
                params.put("districtId", filter.getDistrictId());
            }
        }
        if (filter.getPriceFrom() != null && filter.getPriceTo() != null) {
//            builder.append(" and exists (select * from hotel_room where hotel_id = h.id and day_price between :priceFrom and :priceTo ) ");
            builder.append(" and h.min_price between :priceFrom and :priceTo ");
            params.put("priceFrom", filter.getPriceFrom());
            params.put("priceTo", filter.getPriceTo());
        } else if (filter.getPriceFrom() != null) {
            builder.append(" and h.min_price >= :priceFrom ");
            params.put("priceFrom", filter.getPriceFrom());
        } else if (filter.getPriceTo() != null) {
            builder.append(" and h.min_price <= :priceTo ");
            params.put("priceTo", filter.getPriceTo());
        }
        if (filter.getName() != null) {
            builder.append(" and lower(h.name) like :name ");
            params.put("name", "%" + filter.getName().toLowerCase().concat("%"));
        }
        if (filter.getStarCount() != null) {
            builder.append(" and h.star_count =:starCount ");
            params.put("starCount", filter.getStarCount());
        }
        if (filter.getRoomType() != null) {
            builder.append(" and exists (select * from hotel_room where hotel_id =h.id and room_type=:roomType )");
            params.put("roomType", filter.getRoomType().name());
        }
        if (filter.getConvenienceList() != null && filter.getConvenienceList().size() > 0) {
            builder.append(" and exists ( select * from hotel_room inner join place_convenience pc on pc.place_id = hotel_room.id " +
                    " where hotel_id = h.id and pc.convenience_id in (:convenienceList))");
            params.put("convenienceList", filter.getConvenienceList());
        }
        if (filter.getType() != null) {
            builder.append(" and h.type =:placeType ");
            params.put("placeType", filter.getType().toString());
        }
        // get hotel roomType list
        StringBuilder selectQueryBuilder = new StringBuilder();
        selectQueryBuilder.append(" select h.id,");
        selectQueryBuilder.append(" region.id, case :lang when 'uz' then region.name_uz when 'en' then region.name_en else region.name_ru end as rName,");
        selectQueryBuilder.append(" district.id, case :lang when 'uz' then district.name_uz when 'en' then district.name_en else district.name_ru end as dName,");
        selectQueryBuilder.append(" h.latitude, h.longitude, h.type, h.name, ");
        selectQueryBuilder.append(" (select attach_id from place_attach where place_id = h.id limit 1) as mainAttach, ");
        selectQueryBuilder.append(" (select string_agg(distinct(room_type),',') from hotel_room  where hotel_id = h.id) as roomTypeStr, ");
        selectQueryBuilder.append(" h.star_count, h.min_price, h.created_date, ");
        selectQueryBuilder.append(" (select string_agg(concat(case :lang when 'uz' then tariff.name_uz when 'en' then tariff.name_en else tariff.name_ru end,',',tariff.color) , '; ') ");
        selectQueryBuilder.append(" from subscription s inner join subscription_tariff tariff on s.tariff_id = tariff.id where s.place_id = h.id ");
        selectQueryBuilder.append(" and s.place_type = 'HOTEL' and s.status = 'ACTIVE' ) ");
        selectQueryBuilder.append(" from hotel h");
        selectQueryBuilder.append(" left join region on region.id = h.region_id ");
        selectQueryBuilder.append(" left join district on district.id = h.district_id ");
        selectQueryBuilder.append(builder);


        StringBuilder premiumQueryBuilder = new StringBuilder();
        premiumQueryBuilder.append(selectQueryBuilder);
        premiumQueryBuilder.append(" and h.id in (select distinct place_id from subscription where place_type = 'HOTEL' and status = 'ACTIVE' ) ");
        premiumQueryBuilder.append(" order by h.created_date desc ");
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

        selectQueryBuilder.append(premiumIds.isEmpty()? " ": " and h.id not in (:premiumIds) ");
        selectQueryBuilder.append(" order by h.created_date desc ");
        selectQueryBuilder.append(" limit ").append(size).append(" offset ").append(page).append(";");
        if (!premiumIds.isEmpty()){
            params.put("premiumIds", premiumIds);
        }

        String countQueryBuilder = "select count(*) from hotel as h " + builder + ";";

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
        List<HotelMapperDTO> mapperList = new LinkedList<>();
        if (page == 0) mapperList.addAll(convert(premiumApartmentList));
        mapperList.addAll(convert(apartmentList));
        return new PlaceFilterResult<>(mapperList, totalCount);
    }

    private List<HotelMapperDTO> convert(List<Object[]> apartmentList) {
        List<HotelMapperDTO> mapperList = new LinkedList<>();

        for (Object[] object : apartmentList) {
            HotelMapperDTO placeMapperDTO = new HotelMapperDTO();
            placeMapperDTO.setId(MapperUtil.getStringValue(object[0]));
            placeMapperDTO.setRegionId(MapperUtil.getLongValue(object[1]));
            placeMapperDTO.setRegionName(MapperUtil.getStringValue(object[2]));
            placeMapperDTO.setDistrictId(MapperUtil.getLongValue(object[3]));
            placeMapperDTO.setDistrictName(MapperUtil.getStringValue(object[4]));
            placeMapperDTO.setLatitude(MapperUtil.getDoubleValue(object[5]));
            placeMapperDTO.setLongitude(MapperUtil.getDoubleValue(object[6]));
            placeMapperDTO.setPlaceType(PlaceType.valueOf(MapperUtil.getStringValue(object[7])));
            placeMapperDTO.setName(MapperUtil.getStringValue(object[8]));
            placeMapperDTO.setMainAttachId(MapperUtil.getStringValue(object[9]));
            placeMapperDTO.setRoomTypeStr(MapperUtil.getStringValue(object[10]));
            placeMapperDTO.setStarCount(MapperUtil.getDoubleValue(object[11]));
            placeMapperDTO.setMinPrice(MapperUtil.getLongValue(object[12]));
            placeMapperDTO.setCreatedDate(MapperUtil.getStringValue(object[13]));
            placeMapperDTO.setTariffResult(MapperUtil.getStringValue(object[14]));
            mapperList.add(placeMapperDTO);
        }
        return mapperList;
    }
}
