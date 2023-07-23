package uz.dachatop.repository.filter;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import uz.dachatop.dto.place.PlaceFilterResult;
import uz.dachatop.dto.place.camp.CampFilterDTO;
import uz.dachatop.dto.subscription.SubscriptionDTO;
import uz.dachatop.dto.subscription.SubscriptionFilterRequestDTO;
import uz.dachatop.dto.subscription.SubscriptionFilterResult;
import uz.dachatop.dto.transaction.SubscriptionTransactionFilterRequestDTO;
import uz.dachatop.dto.transaction.SubscriptionTransactionFilterResult;
import uz.dachatop.entity.subscriotion.SubscriptionEntity;
import uz.dachatop.enums.AppLanguage;
import uz.dachatop.enums.PlaceType;
import uz.dachatop.enums.SubscriptionStatus;
import uz.dachatop.enums.SubscriptionTransactionStatus;
import uz.dachatop.mapper.CampMapperDTO;
import uz.dachatop.mapper.SubscriptionMapper;
import uz.dachatop.mapper.SubscriptionTransactionMapper;
import uz.dachatop.util.MapperUtil;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

//PROJECT NAME --> api.dachatop
// YEAR --> 2023
// MONTH --> 04
// DAY --> 06
// TIME --> 14:05
@Repository
@RequiredArgsConstructor
public class SubscriptionFilterRepository {


    private final EntityManager entityManager;


    public SubscriptionFilterResult<SubscriptionMapper> filter(SubscriptionFilterRequestDTO filter, int page, int size) {
        StringBuilder builder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        builder.append(" where visible = true ");
//sub.status='ACTIVE'
        if (filter.getPlaceId() != null) {
            builder.append(" and sub.place_id =:placeId  ");
            params.put("placeId", filter.getPlaceId());
        }
        if (filter.getPlaceType() != null) {
            builder.append(" and sub.place_type = :placeType ");
            params.put("placeType", filter.getPlaceType().toString());
        }
        if (filter.getTariffId() != null) {
            builder.append(" and sub.tariff_id = :tariffId ");
            params.put("tariffId", filter.getTariffId());
        }
        if (filter.getStartDate() != null && filter.getEndDate() != null) {
            builder.append(" and sub.created_date between :startDate and :endDate ");
            params.put("startDate", filter.getStartDate());
            params.put("endDate", filter.getEndDate());

            LocalDate startDate = filter.getStartDate();
            LocalDateTime startOfDay = startDate.atTime(LocalTime.MAX);
            LocalDateTime endOfDay = startDate.atTime(LocalTime.MIN);

        } else if (filter.getStartDate() != null) {
            builder.append(" and and sub.created_date >= :startDate ");
            params.put("startDate", filter.getStartDate());
        } else if (filter.getEndDate() != null) {
            builder.append(" and sub.created_date <=:endDate ");
            params.put("endDate", filter.getEndDate());
        }
        if (filter.getStatus() != null) {
            builder.append(" and sub.status =:status ");
            params.put("status", filter.getStatus().toString());
        }

        StringBuilder selectQueryBuilder = new StringBuilder();
        selectQueryBuilder.append("""
                select sub.id, sub.place_id, sub.tariff_id , sub.prt_id,
                 sub.place_type, sub.price, sub.days ,
                 sub.created_date, sub.end_date, sub.status
                 from subscription sub
                 """);

        selectQueryBuilder.append(builder);
        selectQueryBuilder.append("""
                order by sub.created_date desc""");
        selectQueryBuilder.append(" limit ").append(size).append(" offset ").append(page).append(";");

        String countQueryBuilder = "select count(*) from subscription as sub " + builder + ";";

        Query selectQuery = entityManager.createNativeQuery(selectQueryBuilder.toString());
        Query countQuery = entityManager.createNativeQuery(countQueryBuilder.toString());

        for (Map.Entry<String, Object> param : params.entrySet()) {
            selectQuery.setParameter(param.getKey(), param.getValue());
            countQuery.setParameter(param.getKey(), param.getValue());
        }

        List<Object[]> apartmentList = selectQuery.getResultList();
        Long totalCount = (Long) countQuery.getSingleResult();

        List<SubscriptionMapper> mapperList = new LinkedList<>();

        for (Object[] object : apartmentList) {
            SubscriptionMapper mapperDTO = new SubscriptionMapper();
            mapperDTO.setSub_id(MapperUtil.getStringValue(object[0]));
            mapperDTO.setPlace_id(MapperUtil.getStringValue(object[1]));
            mapperDTO.setTariff_id(MapperUtil.getStringValue(object[2]));
            mapperDTO.setProfile_id(MapperUtil.getStringValue(object[3]));
            mapperDTO.setPlace_type(PlaceType.valueOf(MapperUtil.getStringValue(object[4])));
            mapperDTO.setSub_price(MapperUtil.getLongValue(object[5]));
            mapperDTO.setSub_days(MapperUtil.getIntegerValue(object[6]));

            mapperDTO.setCreated_date(MapperUtil.getStringValue(object[7]));
            mapperDTO.setEnd_date(MapperUtil.getStringValue(object[8]));
            mapperDTO.setSub_status(SubscriptionStatus.valueOf(MapperUtil.getStringValue(object[9])));

            mapperList.add(mapperDTO);


        }
        return new SubscriptionFilterResult(mapperList, totalCount);
    }

    public SubscriptionTransactionFilterResult<SubscriptionTransactionMapper> transactionFilter(SubscriptionTransactionFilterRequestDTO filter, int page, int size) {
        StringBuilder builder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        builder.append(" where visible = true ");

        if (filter.getPlaceId() != null) {
            builder.append(" and sub.place_id =:placeId  ");
            params.put("placeId", filter.getPlaceId());
        }
        if (filter.getPlaceType() != null) {
            builder.append(" and sub.place_type = :placeType ");
            params.put("placeType", filter.getPlaceType().toString());
        }
        if (filter.getTariffId() != null) {
            builder.append(" and sub.tariff_id = :tariffId ");
            params.put("tariffId", filter.getTariffId());
        }
        if (filter.getTariffPlan() != null) {
            builder.append(" and sub.tariff_plan = :tariffPlan ");
            params.put("tariffPlan", filter.getTariffPlan());
        }
        if (filter.getStartDate() != null && filter.getEndDate() != null) {
            builder.append(" and sub.created_date between :startDate and :endDate ");
            params.put("startDate", filter.getStartDate());
            params.put("endDate", filter.getEndDate());
        }
        if (filter.getStatus() != null) {
            builder.append(" and sub.status =:status ");
            params.put("status", filter.getStatus().toString());
        }

        StringBuilder selectQueryBuilder = new StringBuilder();
        selectQueryBuilder.append("""
                select sub.id, sub.place_id, sub.tariff_id ,
                 sub.place_type, sub.price, sub.days ,
                 sub.tariff_plan , sub.status, sub.tariff_plan_price
                 from subscription_transaction sub
                 """);

        selectQueryBuilder.append(builder);
        selectQueryBuilder.append("""
                order by sub.created_date desc""");
        selectQueryBuilder.append(" limit ").append(size).append(" offset ").append(page).append(";");

        String countQueryBuilder = "select count(*) from subscription_transaction as sub " + builder + ";";

        Query selectQuery = entityManager.createNativeQuery(selectQueryBuilder.toString());
        Query countQuery = entityManager.createNativeQuery(countQueryBuilder.toString());

        for (Map.Entry<String, Object> param : params.entrySet()) {
            selectQuery.setParameter(param.getKey(), param.getValue());
            countQuery.setParameter(param.getKey(), param.getValue());
        }

        List<Object[]> apartmentList = selectQuery.getResultList();
        Long totalCount = (Long) countQuery.getSingleResult();

        List<SubscriptionTransactionMapper> mapperList = new LinkedList<>();

        for (Object[] object : apartmentList) {
            SubscriptionTransactionMapper mapperDTO = new SubscriptionTransactionMapper();
            mapperDTO.setId(MapperUtil.getStringValue(object[0]));
            mapperDTO.setPlace_id(MapperUtil.getStringValue(object[1]));
            mapperDTO.setTariff_id(MapperUtil.getStringValue(object[2]));
            mapperDTO.setPlace_type(PlaceType.valueOf(MapperUtil.getStringValue(object[3])));
            mapperDTO.setPrice(MapperUtil.getLongValue(object[4]));
            mapperDTO.setDays(MapperUtil.getIntegerValue(object[5]));
            mapperDTO.setTariff_plan(MapperUtil.getLongValue(object[6]));
            mapperDTO.setStatus(SubscriptionTransactionStatus.valueOf((MapperUtil.getStringValue(object[7]))));
            mapperDTO.setTariff_plan_price(MapperUtil.getLongValue(object[8]));

            mapperList.add(mapperDTO);
        }
        return new SubscriptionTransactionFilterResult(mapperList, totalCount);

    }

    public String getSubResul(String placeId, String placeType, String lang) {
        Map<String, Object> params = new HashMap<>();

        String selectQueryBuilder = """
                select string_agg(
                concat(case :lang when 'uz' then tariff.name_uz
                when 'en' then tariff.name_en
                else tariff.name_ru end, ',', tariff.color), '; ')
                as tariffResult
                from subscription s
                inner join subscription_tariff tariff on s.tariff_id = tariff.id
                where s.place_id = :placeId
                and s.place_type = :placeType
                and s.status = 'ACTIVE'
                 """;
        params.put("placeId",placeId);
        params.put("placeType",placeType);
        params.put("lang",lang);

        Query selectQuery = entityManager.createNativeQuery(selectQueryBuilder);

        for (Map.Entry<String, Object> param : params.entrySet()) {
            selectQuery.setParameter(param.getKey(), param.getValue());
        }

        List<String> apartmentList = selectQuery.getResultList();

        return apartmentList.get(0);

    }


}
