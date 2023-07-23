package uz.dachatop.repository;
//PROJECT NAME --> api.dachatop
// YEAR --> 2023
// MONTH --> 04
// DAY --> 02
// TIME --> 22:35

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import uz.dachatop.entity.subscriotion.SubscriptionEntity;
import uz.dachatop.enums.PlaceType;
import uz.dachatop.enums.SubscriptionStatus;
import uz.dachatop.mapper.subscription.PlaceSubscriptionMapper;

import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, String> {

    @Modifying
    @Transactional
    @Query("Update SubscriptionEntity set status = :status where id =:id")
    int changeStatus(@Param("status") SubscriptionStatus s, @Param("id") String id);


    @Modifying
    @Transactional
    @Query(value = "update subscription set status = 'FINISH' " +
            " where id in (select id from subscription " +
            " where now() > end_date and status = 'ACTIVE')",
            nativeQuery = true)
    void updateSubscriptionStatus();

    @Query(value = "select string_agg(concat(case ?3 " +
            "                             when 'uz' then tariff.name_uz " +
            "                             when 'en' then tariff.name_en " +
            "                             else tariff.name_ru end, ',', tariff.color), '; ') " +
            " as tariffResult " +
            "from subscription s " +
            "         inner join subscription_tariff tariff on s.tariff_id = tariff.id " +
            "where s.place_id = ?1 and s.place_type = ?2 and s.status = 'ACTIVE' ",
            nativeQuery = true)
    Optional<PlaceSubscriptionMapper> findByPlaceSub(String placeId, PlaceType type, String lang);


    Optional<SubscriptionEntity> findByPlaceIdAndTariffIdAndStatus(String placeId, String tariffId, SubscriptionStatus status);

}
