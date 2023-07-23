package uz.dachatop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import uz.dachatop.entity.SmsEntity;
import uz.dachatop.enums.SmsStatus;

import java.util.Optional;

public interface SmsRepository extends JpaRepository<SmsEntity, String> {
    Optional<SmsEntity> findTopByPhoneAndStatusOrderByCreatedDateDesc(String phone, SmsStatus notUsed);

    @Modifying
    @Transactional
    @Query("Update SmsEntity set status =:status where id =:id")
    void updateSmsStatus(@Param("status") SmsStatus status, @Param("id") String id);

    @Query(value = "select * from sms where phone = :phone and created_date > now() - INTERVAL '1 MINUTE' ", nativeQuery = true)
    Long getSmsCount(@Param("phone") String phone);

    SmsEntity findByPhone(String myEskizUzEmail);

    Page<SmsEntity> findByVisibleTrue(Pageable pageable);

    Optional<SmsEntity> findTopByIdAndStatusOrderByCreatedDateDesc(String secretKey, SmsStatus notUsed);
}
