package uz.dachatop.repository.place.travel;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import uz.dachatop.dto.country.CountryResponseDTO;
import uz.dachatop.dto.place.PlaceFilterResult;
import uz.dachatop.dto.place.travel.TravelFilterDTO;
import uz.dachatop.enums.AppLanguage;
import uz.dachatop.enums.PlaceType;
import uz.dachatop.mapper.TravelMapperDTO;
import uz.dachatop.util.MapperUtil;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class TravelCustomFilter {

    private final EntityManager entityManager;

    public PlaceFilterResult<TravelMapperDTO> filter(TravelFilterDTO filter, AppLanguage lang, int page, int size) {
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
            builder.append(" and ap.standards_price between :priceFrom and :priceTo ");
            params.put("priceFrom", filter.getPriceFrom());
            params.put("priceTo", filter.getPriceTo());
        }
        if (filter.getCountryId() != null) {
            builder.append(" and ap.country_id =:country_id ");
            params.put("country_id", filter.getCountryId());
        }
        if (filter.getCompanyName() != null) {
            builder.append(" and lower(ap.company_name) like :companyName ");
            params.put("companyName", "%" + filter.getCompanyName().toLowerCase().concat("%"));
        }
        if (filter.getCompanyAddress() != null) {
            builder.append(" and ap.company_address =:company_address ");
            params.put("company_address", filter.getCompanyAddress());
        }
        if (filter.getType() != null) {
            builder.append(" and ap.type = :placeType ");
            params.put("placeType", filter.getType().toString());
        }

        StringBuilder selectQueryBuilder = new StringBuilder();
        selectQueryBuilder.append(" select ap.id,");
        selectQueryBuilder.append(" region.id, case :lang when 'uz' then region.name_uz when 'en' then region.name_en else region.name_ru end as rName,");
        selectQueryBuilder.append(" district.id, case :lang when 'uz' then district.name_uz when 'en' then district.name_en else district.name_ru end as dName,");
        selectQueryBuilder.append(" ap.latitude, ap.longitude, cou.id, case :lang when 'uz' then cou.name_uz when 'en' then cou.name_en else cou.name_ru end as cName, ap.company_name,");
        selectQueryBuilder.append(" ap.company_address, ap.type,ap.standards_price,ap.price_on_sale,");
        selectQueryBuilder.append(" (select attach_id from place_attach where place_id = ap.id limit 1) as mainAttach, ");
        selectQueryBuilder.append(" ap.created_date, ");
        selectQueryBuilder.append(" (select string_agg(concat(case :lang when 'uz' then tariff.name_uz when 'en' then tariff.name_en else tariff.name_ru end,',',tariff.color) , '; ') ");
        selectQueryBuilder.append(" from subscription s inner join subscription_tariff tariff on s.tariff_id = tariff.id where s.place_id = ap.id ");
        selectQueryBuilder.append(" and s.place_type = 'TRAVEL' and s.status = 'ACTIVE' ) ");
        selectQueryBuilder.append(" from travel ap ");
        selectQueryBuilder.append(" left join region on region.id = ap.region_id ");
        selectQueryBuilder.append(" left join country as cou on cou.id = ap.country_id ");
        selectQueryBuilder.append(" left join district on district.id = ap.district_id ");
        selectQueryBuilder.append(builder);

        StringBuilder premiumQueryBuilder = new StringBuilder();
        premiumQueryBuilder.append(selectQueryBuilder);
        premiumQueryBuilder.append(" and ap.id in (select distinct place_id from subscription where place_type = 'TRAVEL' and status = 'ACTIVE' ) ");
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
        if (!premiumIds.isEmpty()) {
            params.put("premiumIds", premiumIds);
        }

        String countQueryBuilder = "select count(*) from travel as ap " + builder + ";";

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
        List<TravelMapperDTO> mapperList = new LinkedList<>();
        if (page == 0) mapperList.addAll(convert(premiumApartmentList));
        mapperList.addAll(convert(apartmentList));
        return new PlaceFilterResult<>(mapperList, totalCount);
    }

    private List<TravelMapperDTO> convert(List<Object[]> apartmentList) {
        List<TravelMapperDTO> mapperList = new LinkedList<>();

        for (Object[] object : apartmentList) {
            TravelMapperDTO mapperDTO = new TravelMapperDTO();
            mapperDTO.setId(MapperUtil.getStringValue(object[0]));
            mapperDTO.setRegionId(MapperUtil.getLongValue(object[1]));
            mapperDTO.setRegionName(MapperUtil.getStringValue(object[2]));
            mapperDTO.setDistrictId(MapperUtil.getLongValue(object[3]));
            mapperDTO.setDistrictName(MapperUtil.getStringValue(object[4]));
            mapperDTO.setLatitude(MapperUtil.getDoubleValue(object[5]));
            mapperDTO.setLongitude(MapperUtil.getDoubleValue(object[6]));
            mapperDTO.setCountry(new CountryResponseDTO(MapperUtil.getLongValue(object[7]), MapperUtil.getStringValue(object[8])));
            mapperDTO.setCompanyName(MapperUtil.getStringValue(object[9]));
            mapperDTO.setCompanyAddress(MapperUtil.getStringValue(object[10]));
            mapperDTO.setPlaceType(PlaceType.valueOf(MapperUtil.getStringValue(object[11])));
            mapperDTO.setStandardsPrice(MapperUtil.getDoubleValue(object[12]));
            mapperDTO.setPriceOnSale(MapperUtil.getDoubleValue(object[13]));
            mapperDTO.setMainAttachId(MapperUtil.getStringValue(object[14]));
            mapperDTO.setCreatedDate(MapperUtil.getStringValue(object[15]));
            mapperDTO.setTariffResult(MapperUtil.getStringValue(object[16]));
            mapperList.add(mapperDTO);
        }
        return mapperList;
    }
}
