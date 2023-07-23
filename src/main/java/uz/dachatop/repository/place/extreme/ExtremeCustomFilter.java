package uz.dachatop.repository.place.extreme;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import uz.dachatop.dto.place.PlaceFilterResult;
import uz.dachatop.dto.place.apartment.ApartmentFilterDTO;
import uz.dachatop.dto.place.extreme.ExtremeFilterDTO;
import uz.dachatop.dto.response.place.ExtremeTypeResDTO;
import uz.dachatop.enums.AppLanguage;
import uz.dachatop.enums.PlaceType;
import uz.dachatop.mapper.apartment.ApartmentMapperDTO;
import uz.dachatop.mapper.extreme.ExtremeMapperDTO;
import uz.dachatop.util.MapperUtil;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ExtremeCustomFilter {

    private final EntityManager entityManager;

    public PlaceFilterResult<ExtremeMapperDTO> filter(ExtremeFilterDTO filter, AppLanguage lang, int page, int size) {
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
            builder.append(" and ap.week_day_price between :priceForm and :priceTo ");
            builder.append(" and ap.weekend_price between :priceForm and :priceTo ");
            params.put("priceForm", filter.getPriceFrom());
            params.put("priceTo", filter.getPriceTo());
        } else if (filter.getPriceFrom() != null) {
            builder.append(" and ap.weekend_price >= :priceForm ");
            builder.append(" and ap.week_day_price >= :priceForm ");
            params.put("priceForm", filter.getPriceFrom());
        } else if (filter.getPriceTo() != null) {
            builder.append(" and ap.weekend_price <=:priceTo ");
            builder.append(" and ap.week_day_price <=:priceTo ");
            params.put("priceTo", filter.getPriceTo());
        }
        if (filter.getExtremeTypeId() != null) {
            builder.append(" and ap.extreme_type_id =:extremeTypeId ");
            params.put("extremeTypeId", filter.getExtremeTypeId());
        }
        if (filter.getType() != null) {
            builder.append(" and ap.type =:placeType ");
            params.put("placeType", filter.getType().toString());
        }

        StringBuilder selectQueryBuilder = new StringBuilder();
        selectQueryBuilder.append(" select ap.id,");
        selectQueryBuilder.append(" region.id, case :lang when 'uz' then region.name_uz when 'en' then region.name_en else region.name_ru end as rName,");
        selectQueryBuilder.append(" district.id, case :lang when 'uz' then district.name_uz when 'en' then district.name_en else district.name_ru end as dName,");
        selectQueryBuilder.append(" ap.latitude, ap.longitude, exty.id,case :lang when 'uz' then exty.name_uz when 'en' then exty.name_en else exty.name_ru end as eName, ap.weekend_price,");
        selectQueryBuilder.append(" ap.week_day_price, ap.type,");
        selectQueryBuilder.append(" (select attach_id from place_attach where place_id = ap.id limit 1) as mainAttach,ap.created_date,");
        selectQueryBuilder.append(" (select string_agg(concat(case :lang when 'uz' then tariff.name_uz when 'en' then tariff.name_en else tariff.name_ru end,',',tariff.color) , '; ') ");
        selectQueryBuilder.append(" from subscription s inner join subscription_tariff tariff on s.tariff_id = tariff.id where s.place_id = ap.id ");
        selectQueryBuilder.append(" and s.place_type = 'EXTREME' and s.status = 'ACTIVE' )");
        selectQueryBuilder.append(" from extreme ap");
        selectQueryBuilder.append(" inner join region on region.id = ap.region_id ");
        selectQueryBuilder.append(" inner join extreme_type as exty on exty.id = ap.extreme_type_id ");
        selectQueryBuilder.append(" left join district on district.id = ap.district_id ");
        selectQueryBuilder.append(builder);

        StringBuilder premiumQueryBuilder = new StringBuilder();
        premiumQueryBuilder.append(selectQueryBuilder);
        premiumQueryBuilder.append(" and ap.id in (select distinct place_id from subscription where place_type = 'EXTREME' and status = 'ACTIVE' ) ");
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

        selectQueryBuilder.append(premiumIds.isEmpty() ? " ":" and ap.id not in (:premiumIds) ");
        selectQueryBuilder.append(" order by ap.created_date desc ");
        selectQueryBuilder.append(" limit ").append(size).append(" offset ").append(page).append(";");
        if (!premiumIds.isEmpty()){
            params.put("premiumIds", premiumIds);
        }

        String countQueryBuilder = "select count(*) from extreme as ap " + builder + ";";

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
        List<ExtremeMapperDTO> mapperList = new LinkedList<>();
        if (page==0) mapperList.addAll(convert(premiumApartmentList));
        mapperList.addAll(convert(apartmentList));
        return new PlaceFilterResult<>(mapperList, totalCount);
    }

    public List<ExtremeMapperDTO> convert(List<Object[]> apartmentList){
        List<ExtremeMapperDTO> mapperList = new LinkedList<>();
        for (Object[] object : apartmentList) {
            ExtremeMapperDTO mapperDTO = new ExtremeMapperDTO();
            mapperDTO.setId(MapperUtil.getStringValue(object[0]));
            mapperDTO.setRegionId(MapperUtil.getLongValue(object[1]));
            mapperDTO.setRegionName(MapperUtil.getStringValue(object[2]));
            mapperDTO.setDistrictId(MapperUtil.getLongValue(object[3]));
            mapperDTO.setDistrictName(MapperUtil.getStringValue(object[4]));
            mapperDTO.setLatitude(MapperUtil.getDoubleValue(object[5]));
            mapperDTO.setLongitude(MapperUtil.getDoubleValue(object[6]));
            mapperDTO.setExtremeType(new ExtremeTypeResDTO(MapperUtil.getStringValue(object[7]),MapperUtil.getStringValue(object[8])));
            mapperDTO.setWeekendPrice(MapperUtil.getLongValue(object[9]));
            mapperDTO.setWeekDayPrice(MapperUtil.getLongValue(object[10]));

            mapperDTO.setPlaceType(PlaceType.valueOf(MapperUtil.getStringValue(object[11])));
            mapperDTO.setMainAttachId(MapperUtil.getStringValue(object[12]));
            mapperDTO.setCreatedDate(MapperUtil.getStringValue(object[13]));
            mapperDTO.setTariffResult(MapperUtil.getStringValue(object[14]));
            mapperList.add(mapperDTO);
        }
        return mapperList;
    }
}
