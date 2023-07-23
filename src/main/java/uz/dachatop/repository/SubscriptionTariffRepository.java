package uz.dachatop.repository;
//PROJECT NAME --> api.dachatop
// YEAR --> 2023
// MONTH --> 04
// DAY --> 02
// TIME --> 22:35

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import uz.dachatop.entity.subscriotion.SubscriptionEntity;
import uz.dachatop.entity.subscriotion.SubscriptionTariffEntity;

import java.util.Optional;

public interface SubscriptionTariffRepository extends JpaRepository<SubscriptionTariffEntity,String> {


    Optional<SubscriptionTariffEntity> findByIdAndVisibleTrue(String id);

    @Modifying
    @Transactional
    @Query("Update SubscriptionTariffEntity set visible = :visible where id =:id")
    int deleteStatus(@Param("visible") Boolean b, @Param("id") String id);


    Iterable<SubscriptionTariffEntity> findAllByVisibleTrue();

}
