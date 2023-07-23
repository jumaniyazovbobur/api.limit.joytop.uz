package uz.dachatop.repository.place.extreme;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import uz.dachatop.entity.place.extreme.ExtremeEntity;
import uz.dachatop.enums.GlobalStatus;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ExtremeRepository extends CrudRepository<ExtremeEntity, String> {
    Optional<ExtremeEntity> findByIdAndVisibleIsTrue(String id);

    @Modifying
    @Transactional
    @Query("update ExtremeEntity set visible = false, deletedId =:deletedId, deletedDate =:deletedDate  where id =:extremeId")
    void deleteExtreme(@Param("extremeId") String extremeId,
                         @Param("deletedId") String deletedId,
                         @Param("deletedDate") LocalDateTime deletedDate);

    @Modifying
    @Transactional
    @Query("update ExtremeEntity set status = :status, deletedId =:profileId, updatedDate =:deletedDate  where id =:extremeId")
    void changeStatus(@Param("extremeId") String extremeId,
                      @Param("profileId") String profileId,
                      @Param("status") GlobalStatus status,
                      @Param("deletedDate") LocalDateTime deletedDate);

    Optional<ExtremeEntity> findByIdAndVisibleTrue(String extremeId);

    @Modifying
    @Transactional
    @Query("update ExtremeEntity set profileId = :profileId where id =:apartmentId")
    void transfer(@Param("apartmentId") String apartmentId,
                  @Param("profileId") String profileId);
}
