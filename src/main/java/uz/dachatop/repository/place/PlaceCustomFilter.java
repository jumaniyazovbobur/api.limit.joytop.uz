package uz.dachatop.repository.place;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import uz.dachatop.dto.district.DistrictDTO;
import uz.dachatop.dto.place.CustomPageImplResult;
import uz.dachatop.dto.place.PlaceFilterDTO;
import uz.dachatop.dto.place.PlaceFilterResult;
import uz.dachatop.dto.place.PlaceInfoDTO;
import uz.dachatop.dto.place.dacha.DachaFilterDTO;
import uz.dachatop.dto.region.RegionDTO;
import uz.dachatop.dto.territory.TerritoryDTO;
import uz.dachatop.enums.AppLanguage;
import uz.dachatop.enums.GlobalStatus;
import uz.dachatop.enums.PlaceType;
import uz.dachatop.mapper.dacha.DachaMapperDTO;
import uz.dachatop.service.AttachServer;
import uz.dachatop.util.MapperUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class PlaceCustomFilter {
    private final EntityManager entityManager;
    private final AttachServer attachService;

    public CustomPageImplResult<PlaceInfoDTO> filter(AppLanguage lang, String profileId,
                                                     int page, int size) {
        Map<String, Object> params = new HashMap<>();

        String selectQueryBuilder = """
                                select dates.id,
                       dates.latitude,
                       dates.longitude,
                       dates.type,
                       dates.status,
                       dates.regionId,
                       dates.rName,
                       dates.mainAttach,
                       dates.dName,
                       dates.districtId,
                       dates.createdDate
                from (select dacha.id   as id,
                                dacha.latitude         as latitude,
                                dacha.longitude        as longitude,
                                dacha.type             as type,
                                dacha.status           as status,
                                dacha.region_id        as regionId,
                                case :lang
                                when 'uz' then r.name_uz
                                when 'en' then r.name_en
                                else r.name_ru end as rName,
                                (select attach_id
                                from place_attach
                                where place_id = dacha.id limit 1) mainAttach,
                                case :lang
                                when 'uz' then d.name_uz
                                when 'en' then d.name_en
                                else d.name_ru end as dName,
                                dacha.district_id      as districtId,
                                dacha.created_date as createdDate
                                from dacha as dacha
                                inner join region r on r.id = dacha.region_id
                                left join district d on d.id = dacha.district_id
                                where dacha.visible is true and dacha.profile_id = :profileId
                                union
                                
                                select ap.id as id, ap.latitude as latitude,
                                       ap.longitude as longitude,
                                       ap.type as type, ap.status as status,
                                       ap.region_id as regionId,
                                       case :lang
                                           when 'uz' then r.name_uz
                                           when 'en' then r.name_en
                                           else r.name_ru end as rName,
                                       (select attach_id
                                        from place_attach
                                        where place_id = ap.id
                                        limit 1)                 mainAttach,
                                       case :lang
                                           when 'uz' then d.name_uz
                                           when 'en' then d.name_en
                                           else d.name_ru end as dName,
                                       ap.district_id         as distrctId,
                                       ap.created_date as createdDate
                                from apartment ap
                                         inner join region r on r.id = ap.region_id
                                         inner join district d on d.id = ap.district_id
                                where ap.visible is true and ap.profile_id = :profileId
                                union
                                
                                select camp.id                as id,
                                       camp.latitude          as latitude,
                                       camp.longitude         as longitude,
                                       camp.type              as type,
                                       camp.status            as status,
                                       camp.region_id         as regionId,
                                       case :lang
                                           when 'uz' then r.name_uz
                                           when 'en' then r.name_en
                                           else r.name_ru end as rName,
                                       (select attach_id
                                        from place_attach
                                        where place_id = camp.id
                                        limit 1)                 mainAttach,
                                       case :lang
                                           when 'uz' then d.name_uz
                                           when 'en' then d.name_en
                                           else d.name_ru end as dName,
                                       camp.district_id       as districtId,
                                       camp.created_date as createdDate
                                from camp
                                         inner join region r on r.id = camp.region_id
                                         inner join district d on d.id = camp.district_id
                                where camp.visible is true and camp.profile_id = :profileId
                                union
                                
                                select extreme.id             as id,
                                       extreme.latitude       as latitude,
                                       extreme.longitude      as longitude,
                                       extreme.type           as type,
                                       extreme.status         as status,
                                       extreme.region_id      as regionId,
                                       case :lang
                                           when 'uz' then r.name_uz
                                           when 'en' then r.name_en
                                           else r.name_ru end as rName,
                                       (select attach_id
                                        from place_attach
                                        where place_id = extreme.id
                                        limit 1)                 mainAttach,
                                       case :lang
                                           when 'uz' then d.name_uz
                                           when 'en' then d.name_en
                                           else d.name_ru end as dName,
                                       extreme.district_id    as districtId,
                                       extreme.created_date as createdDate
                                from extreme
                                         inner join region r on r.id = extreme.region_id
                                         left join district d on d.id = extreme.district_id
                                where extreme.visible is true and extreme.profile_id = :profileId
                                union
                                
                                select hotel.id               as id,
                                       hotel.latitude         as latitude,
                                       hotel.longitude        as longitude,
                                       hotel.type             as type,
                                       hotel.status           as status,
                                       hotel.region_id        as regionId,
                                       case :lang
                                           when 'uz' then r.name_uz
                                           when 'en' then r.name_en
                                           else r.name_ru end as rName,
                                       (select attach_id
                                        from place_attach
                                        where place_id = hotel.id
                                        limit 1)                 mainAttach,
                                       case :lang
                                           when 'uz' then d.name_uz
                                           when 'en' then d.name_en
                                           else d.name_ru end as dName,
                                       hotel.district_id      as districtId,
                                       hotel.created_date as createdDate
                                from hotel
                                         left join region r on r.id = hotel.region_id
                                         left join district d on d.id = hotel.district_id
                                where hotel.visible is true and hotel.profile_id = :profileId
                                union
                                select travel.id              as id,
                                       travel.latitude        as latitude,
                                       travel.longitude       as longitude,
                                       travel.type            as type,
                                       travel.status          as status,
                                       travel.region_id       as regionId,
                                       case :lang
                                           when 'uz' then r.name_uz
                                           when 'en' then r.name_en
                                           else r.name_ru end as rName,
                                       (select attach_id
                                        from place_attach
                                        where place_id = travel.id
                                        limit 1)                 mainAttach,
                                       case :lang
                                           when 'uz' then d.name_uz
                                           when 'en' then d.name_en
                                           else d.name_ru end as dName,
                                       travel.district_id     as districId,
                                       travel.created_date as createdDate
                                from travel
                                         left join region r on r.id = travel.region_id
                                         left join district d on d.id = travel.district_id
                                where travel.visible is true and travel.profile_id = :profileId ) dates 
                                order by dates.createdDate desc 
                                 """ + " limit " + size + " offset " + page * size + ";";

        params.put("lang", lang.toString());
        params.put("profileId", profileId);

        String countQueryBuilder = """
                select sum(x.count) from (select count(*) count
                from dacha as dacha
                inner join region r on r.id = dacha.region_id
                inner join district d on d.id = dacha.district_id
                where dacha.visible is true and dacha.profile_id = :profileId
                union
                select count(*) count
                from apartment ap
                         inner join region r on r.id = ap.region_id
                         inner join district d on d.id = ap.district_id
                where ap.visible is true and ap.profile_id = :profileId
                union
                select count(*) count
                from camp
                         inner join region r on r.id = camp.region_id
                         inner join district d on d.id = camp.district_id
                where camp.visible is true and camp.profile_id = :profileId
                union
                select count(*) count
                from extreme
                         inner join region r on r.id = extreme.region_id
                         left join district d on d.id = extreme.district_id
                where extreme.visible is true and extreme.profile_id = :profileId
                union
                select count(*) count
                from hotel
                         left join region r on r.id = hotel.region_id
                         left join district d on d.id = hotel.district_id
                where hotel.visible is true and hotel.profile_id = :profileId
                union
                select count(*) count
                from travel
                         left join region r on r.id = travel.region_id
                         left join district d on d.id = travel.district_id
                where travel.visible is true and travel.profile_id = :profileId) x;
                """;

        Query selectQuery = entityManager.createNativeQuery(selectQueryBuilder);

        Query countQuery = entityManager.createNativeQuery(countQueryBuilder);

        for (Map.Entry<String, Object> param : params.entrySet()) {
            selectQuery.setParameter(param.getKey(), param.getValue());
            if (!param.getKey().equals("lang")) {
                countQuery.setParameter(param.getKey(), param.getValue());
            }
        }

        List<Object[]> placeList = selectQuery.getResultList();
        BigDecimal totalCount = (BigDecimal) countQuery.getSingleResult();

        List<PlaceInfoDTO> mapperList = new LinkedList<>();

        for (Object[] object : placeList) {
            PlaceInfoDTO placeInfoDTO = new PlaceInfoDTO();
            placeInfoDTO.setId(MapperUtil.getStringValue(object[0]));
            placeInfoDTO.setLatitude(MapperUtil.getDoubleValue(object[1]));
            placeInfoDTO.setLongitude(MapperUtil.getDoubleValue(object[2]));
            placeInfoDTO.setPlaceType(PlaceType.valueOf(MapperUtil.getStringValue(object[3])));
            placeInfoDTO.setStatus(GlobalStatus.valueOf(MapperUtil.getStringValue(object[4])));
            placeInfoDTO.setRegion(new RegionDTO(MapperUtil.getLongValue(object[5]), MapperUtil.getStringValue(object[6])));
            placeInfoDTO.setMainAttach(attachService.webContentLink(MapperUtil.getStringValue(object[7])));
            placeInfoDTO.setDistrict(new DistrictDTO(MapperUtil.getLongValue(object[9]), MapperUtil.getStringValue(object[8])));
            placeInfoDTO.setCreatedDate(MapperUtil.getStringValue(object[10]));
            mapperList.add(placeInfoDTO);
        }
        return new CustomPageImplResult<>(mapperList, totalCount.longValue());
    }

    public CustomPageImplResult<PlaceInfoDTO> filterForAdmin(PlaceFilterDTO filterDTO, AppLanguage language, String currentProfileId, int page, int size) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder dachaSQL = new StringBuilder("""
                select dacha.id   as id,
                dacha.latitude         as latitude,
                dacha.longitude        as longitude,
                dacha.type             as type,
                dacha.status           as status,
                dacha.region_id        as regionId,
                case :lang
                when 'uz' then r.name_uz
                when 'en' then r.name_en
                else r.name_ru end as rName,
                (select attach_id
                from place_attach
                where place_id = dacha.id limit 1) mainAttach,
                case :lang
                when 'uz' then d.name_uz
                when 'en' then d.name_en
                else d.name_ru end as dName,
                dacha.district_id as districtId,
                dacha.territory_id as territoryId,
                case :lang
                 when 'uz' then t.name_uz
                 when 'en' then t.name_en
                 else t.name_ru end as tName,
                 dacha.created_date as createdDate,
                 dacha.number
                from dacha as dacha
                inner join region r on r.id = dacha.region_id
                inner join district d on d.id = dacha.district_id
                left join territory t on t.id =  dacha.territory_id
                where dacha.visible is true
                """);

        StringBuilder apartmentSQL = new StringBuilder("""
                 select ap.id as id, ap.latitude as latitude,
                       ap.longitude as longitude,
                       ap.type as type, ap.status as status,
                       ap.region_id as regionId,
                       case :lang
                       when 'uz' then r.name_uz
                       when 'en' then r.name_en
                       else r.name_ru end as rName,
                       (select attach_id
                        from place_attach
                        where place_id = ap.id
                        limit 1)                 mainAttach,
                       case 'uz'
                       when 'uz' then d.name_uz
                       when 'en' then d.name_en
                       else d.name_ru end as dName,
                       ap.district_id as districtId,
                       ap.territory_id as territoryId,
                                       case :lang
                                        when 'uz' then t.name_uz
                                        when 'en' then t.name_en
                                        else t.name_ru end as tName,
                                       ap.created_date as createdDate,
                                       (NULL)
                 from apartment ap
                         inner join region r on r.id = ap.region_id
                         inner join district d on d.id = ap.district_id
                         left join territory t on t.id = ap.territory_id
                where ap.visible is true
                """);
        StringBuilder campSQL = new StringBuilder("""
                                       select camp.id as id,
                                       camp.latitude          as latitude,
                                       camp.longitude         as longitude,
                                       camp.type              as type,
                                       camp.status            as status,
                                       camp.region_id         as regionId,
                                       case :lang
                                           when 'uz' then r.name_uz
                                           when 'en' then r.name_en
                                           else r.name_ru end as rName,
                                       (select attach_id
                                        from place_attach
                                        where place_id = camp.id
                                        limit 1)                 mainAttach,
                                       case :lang
                                           when 'uz' then d.name_uz
                                           when 'en' then d.name_en
                                           else d.name_ru end as dName,
                                       camp.district_id       as districtId,
                                       camp.territory_id         as territoryId,
                                       case :lang
                                        when 'uz' then t.name_uz
                                        when 'en' then t.name_en
                                        else t.name_ru end as tName,
                                       camp.created_date as createdDate,
                                        (NULL)
                                from camp
                                         inner join region r on r.id = camp.region_id
                                         inner join district d on d.id = camp.district_id
                                         left join territory t on t.id = camp.territory_id
                                where camp.visible is true
                """);
        StringBuilder extremeSQL = new StringBuilder("""
                                       select extreme.id             as id,
                                       extreme.latitude       as latitude,
                                       extreme.longitude      as longitude,
                                       extreme.type           as type,
                                       extreme.status         as status,
                                       extreme.region_id      as regionId,
                                       case :lang
                                           when 'uz' then r.name_uz
                                           when 'en' then r.name_en
                                           else r.name_ru end as rName,
                                       (select attach_id
                                        from place_attach
                                        where place_id = extreme.id
                                        limit 1)                 mainAttach,
                                       case :lang
                                           when 'uz' then d.name_uz
                                           when 'en' then d.name_en
                                           else d.name_ru end as dName,
                                       extreme.district_id    as districtId,
                                       extreme.territory_id      as territoryId,
                                       case :lang
                                        when 'uz' then t.name_uz
                                        when 'en' then t.name_en
                                        else t.name_ru end as tName,
                                       extreme.created_date as createdDate,
                                        (NULL)
                                from extreme
                                         inner join region r on r.id = extreme.region_id
                                         left join district d on d.id = extreme.district_id
                                         left join territory t on t.id = extreme.territory_id
                                where extreme.visible is true
                """);
        StringBuilder hotelSQL = new StringBuilder("""
                                       select hotel.id               as id,
                                       hotel.latitude         as latitude,
                                       hotel.longitude        as longitude,
                                       hotel.type             as type,
                                       hotel.status           as status,
                                       hotel.region_id        as regionId,
                                       case :lang
                                           when 'uz' then r.name_uz
                                           when 'en' then r.name_en
                                           else r.name_ru end as rName,
                                       (select attach_id
                                        from place_attach
                                        where place_id = hotel.id
                                        limit 1)                 mainAttach,
                                       case 'uz'
                                           when 'uz' then d.name_uz
                                           when 'en' then d.name_en
                                           else d.name_ru end as dName,
                                       hotel.district_id      as districtId,
                                       hotel.territory_id        as territoryId,
                                       case :lang
                                           when 'uz' then t.name_uz
                                           when 'en' then t.name_en
                                           else t.name_ru end as tName,
                                       hotel.created_date as createdDate,
                                         (NULL)
                                from hotel
                                         left join region r on r.id = hotel.region_id
                                         left join district d on d.id = hotel.district_id
                                         left join territory t on t.id = hotel.territory_id
                                where hotel.visible is true
                """);
        StringBuilder travelSQL = new StringBuilder("""
                                       select travel.id              as id,
                                       travel.latitude        as latitude,
                                       travel.longitude       as longitude,
                                       travel.type            as type,
                                       travel.status          as status,
                                       travel.region_id       as regionId,
                                       case :lang
                                           when 'uz' then r.name_uz
                                           when 'en' then r.name_en
                                           else r.name_ru end as rName,
                                       (select attach_id
                                        from place_attach
                                        where place_id = travel.id
                                        limit 1)                 mainAttach,
                                       case :lang
                                           when 'uz' then d.name_uz
                                           when 'en' then d.name_en
                                           else d.name_ru end as dName,
                                       travel.district_id     as districtId,
                                       travel.territory_id       as territoryId,
                                       case :lang
                                           when 'uz' then t.name_uz
                                           when 'en' then t.name_en
                                           else t.name_ru end as tName,
                                       travel.created_date as createdDate,
                                          (NULL)
                                from travel
                                         left join region r on r.id = travel.region_id
                                         left join district d on d.id = travel.district_id
                                         left join territory t on t.id = travel.territory_id
                                where travel.visible is true
                """);


        String selectQueryBuilder = """
                                select dates.id,
                       dates.latitude,
                       dates.longitude,
                       dates.type,
                       dates.status,
                       dates.regionId,
                       dates.rName,
                       dates.mainAttach,
                       dates.dName,
                       dates.districtId,
                       dates.territoryId,
                       dates.tName,
                       dates.createdDate,
                       dates.number
                from ( %s
                       union
                       %s
                       union
                       %s
                       union
                       %s
                       union
                       %s        
                       union
                       %s ) dates  
                       order by dates.createddate desc """ + " limit " + size + " offset " + page + ";";

        params.put("lang", language.toString());

        StringBuilder dachaCountSQL = new StringBuilder("""
                select count(*) count
                from dacha as dacha
                inner join region r on r.id = dacha.region_id
                left join territory t on t.id = dacha.territory_id
                inner join district d on d.id = dacha.district_id
                where dacha.visible is true
                """);

        StringBuilder apartmentCountSQL = new StringBuilder("""
                select count(*) count
                from apartment ap
                         inner join region r on r.id = ap.region_id
                         left join territory t on t.id = ap.territory_id
                         inner join district d on d.id = ap.district_id
                where ap.visible is true
                """);

        StringBuilder campCountSQL = new StringBuilder("""
                select count(*) count
                from camp
                         inner join region r on r.id = camp.region_id
                         left join territory t on t.id = camp.territory_id
                         inner join district d on d.id = camp.district_id
                where camp.visible is true
                """);

        StringBuilder extremeCountSQL = new StringBuilder("""
                select count(*) count
                from extreme
                         inner join region r on r.id = extreme.region_id
                         left join territory t on t.id = extreme.territory_id
                         left join district d on d.id = extreme.district_id
                where extreme.visible is true
                """);

        StringBuilder hotelCountSQL = new StringBuilder("""
                select count(*) count
                from hotel
                         left join region r on r.id = hotel.region_id
                         left join territory t on t.id = hotel.territory_id
                         left join district d on d.id = hotel.district_id
                where hotel.visible is true
                """);

        StringBuilder travelCountSQL = new StringBuilder("""
                select count(*) count
                from travel as travel
                         left join region r on r.id = travel.region_id
                         left join territory t on t.id = travel.territory_id
                         left join district d on d.id = travel.district_id
                where travel.visible is true
                """);

        String countQueryBuilder = """
                select sum(x.count) from ( %s
                union
                %s
                union
                %s
                union
                %s
                union
                %s
                union
                %s ) x;
                """;

        if (Optional.ofNullable(filterDTO).isPresent()) {
            if (Optional.ofNullable(filterDTO.getType()).isPresent()) {
                // main sql
                dachaSQL.append(" and dacha.type = :type ");
                apartmentSQL.append(" and ap.type = :type ");
                campSQL.append(" and camp.type = :type ");
                extremeSQL.append(" and extreme.type = :type ");
                hotelSQL.append(" and hotel.type = :type ");
                travelSQL.append(" and travel.type = :type ");

                // count sql
                dachaCountSQL.append(" and dacha.type = :type ");
                apartmentCountSQL.append(" and ap.type = :type ");
                campCountSQL.append(" and camp.type = :type ");
                extremeCountSQL.append(" and extreme.type = :type ");
                hotelCountSQL.append(" and hotel.type = :type ");
                travelCountSQL.append(" and travel.type = :type ");

                params.put("type", filterDTO.getType().name());
            }
            if (Optional.ofNullable(filterDTO.getProfileId()).isPresent()) {
                // main sql
                dachaSQL.append(" and dacha.profile_id = :profileId ");
                apartmentSQL.append(" and ap.profile_id = :profileId ");
                campSQL.append(" and camp.profile_id = :profileId ");
                extremeSQL.append(" and extreme.profile_id = :profileId ");
                hotelSQL.append(" and hotel.profile_id = :profileId ");
                travelSQL.append(" and travel.profile_id = :profileId ");

                // count sql
                dachaCountSQL.append(" and dacha.profile_id = :profileId ");
                apartmentCountSQL.append(" and ap.profile_id = :profileId ");
                campCountSQL.append(" and camp.profile_id = :profileId ");
                extremeCountSQL.append(" and extreme.profile_id = :profileId ");
                hotelCountSQL.append(" and hotel.profile_id = :profileId ");
                travelCountSQL.append(" and travel.profile_id = :profileId ");

                params.put("profileId", filterDTO.getProfileId());
            }
            if (Optional.ofNullable(filterDTO.getRegionId()).isPresent()) {
                // main sql
                dachaSQL.append(" and dacha.region_id = :regionId ");
                apartmentSQL.append(" and ap.region_id = :regionId ");
                campSQL.append(" and camp.region_id = :regionId ");
                extremeSQL.append(" and extreme.region_id = :regionId ");
                hotelSQL.append(" and hotel.region_id = :regionId ");
                travelSQL.append(" and travel.region_id = :regionId ");

                // count sql
                dachaCountSQL.append(" and dacha.region_id = :regionId ");
                apartmentCountSQL.append(" and ap.region_id = :regionId ");
                campCountSQL.append(" and camp.region_id = :regionId ");
                extremeCountSQL.append(" and extreme.region_id = :regionId ");
                hotelCountSQL.append(" and hotel.region_id = :regionId ");
                travelCountSQL.append(" and travel.region_id = :regionId ");

                params.put("regionId", filterDTO.getRegionId());
            }
            if (Optional.ofNullable(filterDTO.getTerritoryId()).isPresent()) {
                // main sql
                dachaSQL.append(" and dacha.territory_id = :territoryId ");
                apartmentSQL.append(" and ap.territory_id = :territoryId ");
                campSQL.append(" and camp.territory_id = :territoryId ");
                extremeSQL.append(" and extreme.territory_id = :territoryId ");
                hotelSQL.append(" and hotel.territory_id = :territoryId ");
                travelSQL.append(" and travel.territory_id = :territoryId ");

                // count sql
                dachaCountSQL.append(" and dacha.territory_id = :territoryId ");
                apartmentCountSQL.append(" and ap.territory_id = :territoryId ");
                campCountSQL.append(" and camp.territory_id = :territoryId ");
                extremeCountSQL.append(" and extreme.territory_id = :territoryId ");
                hotelCountSQL.append(" and hotel.territory_id = :territoryId ");
                travelCountSQL.append(" and travel.territory_id = :territoryId ");

                params.put("territoryId", filterDTO.getRegionId());
            }
            if (Optional.ofNullable(filterDTO.getDistrictId()).isPresent()) {
                // main sql
                dachaSQL.append(" and dacha.district_id = :districtId ");
                apartmentSQL.append(" and ap.district_id = :districtId ");
                campSQL.append(" and camp.district_id = :districtId ");
                extremeSQL.append(" and extreme.district_id = :districtId ");
                hotelSQL.append(" and hotel.district_id = :districtId ");
                travelSQL.append(" and travel.district_id = :districtId ");

                // count sql
                dachaCountSQL.append(" and dacha.district_id = :districtId ");
                apartmentCountSQL.append(" and ap.district_id = :districtId ");
                campCountSQL.append(" and camp.district_id = :districtId ");
                extremeCountSQL.append(" and extreme.district_id = :districtId ");
                hotelCountSQL.append(" and hotel.district_id = :districtId ");
                travelCountSQL.append(" and travel.district_id = :districtId ");

                params.put("districtId", filterDTO.getDistrictId());
            }
            if (Optional.ofNullable(filterDTO.getName()).isPresent()) {
                // main sql
                //lower(d.name) like lower(:name)
                dachaSQL.append(" and lower(dacha.name) like :name ");
                campSQL.append(" and lower(camp.name) like :name ");
                hotelSQL.append(" and lower(hotel.name) like :name ");

                // count sql
                dachaCountSQL.append(" and lower(dacha.name) like :name ");
                campCountSQL.append(" and lower(camp.name) like :name ");
                hotelCountSQL.append(" and lower(hotel.name) like :name ");

                params.put("name", "%" + filterDTO.getName().toLowerCase().concat("%"));
            }
            if (Optional.ofNullable(filterDTO.getPriceFrom()).isPresent()) {
                // main sql
                dachaSQL.append(" and dacha.week_day_price >= :priceFrom ");
                apartmentSQL.append(" and ap.day_price >= :priceFrom ");
                campSQL.append(" and camp.price >= :priceFrom ");
                extremeSQL.append(" and extreme.week_day_price >= :priceFrom ");
                hotelSQL.append(" and hotel.min_price >= :priceFrom ");
                travelSQL.append(" and travel.standards_price >= :priceFrom ");

                // count sql
                dachaCountSQL.append(" and dacha.week_day_price >= :priceFrom ");
                apartmentCountSQL.append(" and ap.day_price >= :priceFrom ");
                campCountSQL.append(" and camp.price >= :priceFrom ");
                extremeCountSQL.append(" and extreme.week_day_price >= :priceFrom ");
                hotelCountSQL.append(" and hotel.min_price >= :priceFrom ");
                travelCountSQL.append(" and travel.standards_price = :priceFrom ");

                params.put("priceFrom", filterDTO.getPriceFrom());
            }
            if (Optional.ofNullable(filterDTO.getPriceTo()).isPresent()) {
                // main sql
                dachaSQL.append(" and dacha.week_day_price <=:priceTo ");
                apartmentSQL.append(" and ap.day_price <= :priceTo ");
                campSQL.append(" and camp.price <= :priceTo ");
                extremeSQL.append(" and extreme.week_day_price <= :priceTo ");
                hotelSQL.append(" and hotel.min_price <= :priceTo ");
                travelSQL.append(" and travel.standards_price <= :priceTo ");

                // count sql
                dachaCountSQL.append(" and dacha.week_day_price <=:priceTo ");
                apartmentCountSQL.append(" and ap.day_price <= :priceTo ");
                campCountSQL.append(" and camp.price <= :priceTo ");
                extremeCountSQL.append(" and extreme.week_day_price <= :priceTo ");
                hotelCountSQL.append(" and hotel.min_price <= :priceTo ");
                travelCountSQL.append(" and travel.standards_price <= :priceTo ");

                params.put("priceTo", filterDTO.getPriceTo());
            }
            if (Optional.ofNullable(filterDTO.getProfileId()).isPresent()) {
                // main sql
                dachaSQL.append(" and dacha.profile_id = :profileId ");
                apartmentSQL.append(" and ap.profile_id = :profileId ");
                campSQL.append(" and camp.profile_id = :profileId ");
                extremeSQL.append(" and extreme.profile_id = :profileId ");
                hotelSQL.append(" and hotel.profile_id = :profileId ");
                travelSQL.append(" and travel.profile_id = :profileId ");

                // count sql
                dachaCountSQL.append(" and dacha.profile_id = :profileId ");
                apartmentCountSQL.append(" and ap.profile_id = :profileId ");
                campCountSQL.append(" and camp.profile_id = :profileId ");
                extremeCountSQL.append(" and extreme.profile_id = :profileId ");
                hotelCountSQL.append(" and hotel.profile_id = :profileId ");
                travelCountSQL.append(" and travel.profile_id = :profileId ");

                params.put("profileId", filterDTO.getProfileId());
            }
            if (Optional.ofNullable(filterDTO.getNumber()).isPresent()) {
                // main sql
                dachaSQL.append(" and dacha.number = :number ");

                // count sql
                dachaCountSQL.append(" and dacha.number = :number ");

                params.put("number", filterDTO.getNumber());
            }
        }

        String sql = String.format(selectQueryBuilder, dachaSQL, apartmentSQL, campSQL,
                extremeSQL, hotelSQL, travelSQL);

        Query selectQuery = entityManager.createNativeQuery(sql);

        String countSQL = String.format(countQueryBuilder, dachaCountSQL, apartmentCountSQL,
                campCountSQL, extremeCountSQL, hotelCountSQL, travelCountSQL);

        Query countQuery = entityManager.createNativeQuery(countSQL);

        for (Map.Entry<String, Object> param : params.entrySet()) {
            selectQuery.setParameter(param.getKey(), param.getValue());
            if (!param.getKey().equals("lang")) {
                countQuery.setParameter(param.getKey(), param.getValue());
            }
        }

        List<Object[]> placeList = selectQuery.getResultList();
        BigDecimal totalCount = (BigDecimal) countQuery.getSingleResult();

        List<PlaceInfoDTO> mapperList = new LinkedList<>();

        for (Object[] object : placeList) {
            PlaceInfoDTO placeInfoDTO = new PlaceInfoDTO();
            placeInfoDTO.setId(MapperUtil.getStringValue(object[0]));
            placeInfoDTO.setLatitude(MapperUtil.getDoubleValue(object[1]));
            placeInfoDTO.setLongitude(MapperUtil.getDoubleValue(object[2]));
            placeInfoDTO.setPlaceType(PlaceType.valueOf(MapperUtil.getStringValue(object[3])));
            placeInfoDTO.setStatus(GlobalStatus.valueOf(MapperUtil.getStringValue(object[4])));
            placeInfoDTO.setRegion(new RegionDTO(MapperUtil.getLongValue(object[5]), MapperUtil.getStringValue(object[6])));
            placeInfoDTO.setMainAttach(attachService.webContentLink(MapperUtil.getStringValue(object[7])));
            placeInfoDTO.setDistrict(new DistrictDTO(MapperUtil.getLongValue(object[9]), MapperUtil.getStringValue(object[8])));
            placeInfoDTO.setTerritory(new TerritoryDTO(MapperUtil.getLongValue(object[10]), MapperUtil.getStringValue(object[11])));
            placeInfoDTO.setCreatedDate(MapperUtil.getStringValue(object[12]));
            placeInfoDTO.setNumber(MapperUtil.getLongValue(object[13]));
            mapperList.add(placeInfoDTO);
        }
        return new CustomPageImplResult<>(mapperList, totalCount.longValue());
    }
}
