package uz.dachatop.repository.place.camp;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import uz.dachatop.entity.place.camp.CampEntity;
import uz.dachatop.entity.place.extreme.ExtremeEntity;
import uz.dachatop.enums.GlobalStatus;

import java.time.LocalDateTime;
import java.util.Optional;

public interface CampRepository extends CrudRepository<CampEntity, String> {
    Optional<CampEntity> findByIdAndVisibleIsTrue(String id);

    @Modifying
    @Transactional
    @Query("update CampEntity set visible = false, deletedId =:deletedId, deletedDate =:deletedDate  where id =:campId")
    void deleteExtreme(@Param("campId") String campId,
                       @Param("deletedId") String deletedId,
                       @Param("deletedDate") LocalDateTime deletedDate);

    @Modifying
    @Transactional
    @Query("update CampEntity set status = :status, deletedId =:profileId, updatedDate =:deletedDate  where id =:campId")
    void changeStatus(@Param("campId") String campId,
                      @Param("profileId") String profileId,
                      @Param("status") GlobalStatus status,
                      @Param("deletedDate") LocalDateTime deletedDate);

    Optional<CampEntity> findByIdAndVisibleTrue(String campId);

    @Modifying
    @Transactional
    @Query("update CampEntity set profileId = :profileId where id =:apartmentId")
    void transfer(@Param("apartmentId") String apartmentId,
                  @Param("profileId") String profileId);
}
