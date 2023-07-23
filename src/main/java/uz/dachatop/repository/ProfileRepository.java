package uz.dachatop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import uz.dachatop.entity.ProfileEntity;
import uz.dachatop.enums.GlobalStatus;

import java.util.Optional;


public interface ProfileRepository extends JpaRepository<ProfileEntity, String> {
    Optional<ProfileEntity> findByPhone(String phone);

    Optional<ProfileEntity> findByPhoneAndStatusAndVisibleTrue(String phone, GlobalStatus status);
    Optional<ProfileEntity> findTop1ByPhoneAndStatusAndVisibleTrue(String phone, GlobalStatus status);

    Optional<ProfileEntity> findByPhoneAndVisibleTrue(String phone);

    @Modifying
    @Transactional
    @Query("Update ProfileEntity set visible = :visible where id =:id")
    int deleteStatus(@Param("visible") boolean b, @Param("id") String id);

    @Modifying
    @Transactional
    @Query("Update ProfileEntity set status = :status where id =:id")
    int blockStatus(@Param("status") GlobalStatus status, @Param("id") String id);


    @Modifying
    @Transactional
    @Query("Update ProfileEntity set status = :status where phone =:phone")
    void updateActivation(@Param("status") GlobalStatus status, @Param("phone") String phone);

    Page<ProfileEntity> findByVisibleTrue(Pageable pageable);

    Optional<ProfileEntity> findByIdAndVisibleTrue(String id);

    @Modifying
    @Transactional
    @Query("Update ProfileEntity set tempPassword = ?1 where id =?2")
    void addTempPassword(String md5, String id);

    @Modifying
    @Transactional
    @Query("Update ProfileEntity set password = tempPassword where id =?1")
    void updatePasswordToTempPassword(String id);

    Long countByPhoneAndIdNotAndVisibleIsTrue(String phone, String id);

    @Modifying
    @Transactional
    @Query("Update ProfileEntity set tempPhone = ?1 where id =?2")
    void addNewPhone(String newPhone, String id);

    @Modifying
    @Transactional
    @Query("Update ProfileEntity set phone = tempPhone where id =?1")
    void updatePhoneToTempPhone(String id);


    @Modifying
    @Transactional
    @Query("Update ProfileEntity set password = ?1 where id =?2")
    void updatePassword(String newPassword, String id);
}
