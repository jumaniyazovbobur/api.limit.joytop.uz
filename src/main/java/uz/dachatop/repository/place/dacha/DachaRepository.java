package uz.dachatop.repository.place.dacha;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uz.dachatop.entity.place.dacha.DachaEntity;
import uz.dachatop.enums.GlobalStatus;

import java.time.LocalDateTime;
import java.util.Optional;


@Repository
public interface DachaRepository extends JpaRepository<DachaEntity, String> {

    Optional<DachaEntity> findByIdAndVisibleTrue(String dachaId);

    Optional<DachaEntity> findByNumberAndVisibleIsTrue(Long number);

    @Modifying
    @Transactional
    @Query("update DachaEntity set visible = false, deletedId =:deletedId, deletedDate =:deletedDate  where id = :apartmentId")
    void deleteApartment(@Param("apartmentId") String apartmentId,
                         @Param("deletedId") String deletedId,
                         @Param("deletedDate") LocalDateTime deletedDate);

    @Modifying
    @Transactional
    @Query("update DachaEntity set status = :status, deletedId =:profileId," +
            " updatedDate =:deletedDate,adminChecked = :adminCheck  where id =:dachaId")
    void changeStatus(@Param("dachaId") String dachaId,
                      @Param("profileId") String profileId,
                      @Param("status") GlobalStatus status,
                      @Param("deletedDate") LocalDateTime deletedDate,
                      @Param("adminCheck") Boolean adminCheck);

    @Modifying
    @Transactional
    @Query("update DachaEntity set status =:status,adminChecked = true where id =:dachaId")
    void changeToPublish(@Param("status") GlobalStatus status,
            @Param("dachaId") String dachaId);

    @Modifying
    @Transactional
    @Query("update DachaEntity set profileId = :profileId where id =:apartmentId")
    void transfer(@Param("apartmentId") String apartmentId,
                  @Param("profileId") String profileId);
}
