package uz.dachatop.repository.place.travel;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import uz.dachatop.entity.place.travel.TravelEntity;
import uz.dachatop.enums.GlobalStatus;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TravelRepository extends CrudRepository<TravelEntity, String>  {
    Optional<TravelEntity> findByIdAndVisibleIsTrue(String id);

    @Modifying
    @Transactional
    @Query("update TravelEntity set visible = false, deletedId =:deletedId, deletedDate =:deletedDate  where id =:travelId")
    void deleteExtreme(@Param("travelId") String travelId,
                       @Param("deletedId") String deletedId,
                       @Param("deletedDate") LocalDateTime deletedDate);

    @Modifying
    @Transactional
    @Query("update TravelEntity set status = :status, deletedId =:profileId, updatedDate =:deletedDate  where id =:travelId")
    void changeStatus(@Param("travelId") String travelId,
                      @Param("profileId") String profileId,
                      @Param("status") GlobalStatus status,
                      @Param("deletedDate") LocalDateTime deletedDate);

    Optional<TravelEntity> findByIdAndVisibleTrue(String travelId);

    @Modifying
    @Transactional
    @Query("update TravelEntity set profileId = :profileId where id =:apartmentId")
    void transfer(@Param("apartmentId") String apartmentId,
                  @Param("profileId") String profileId);
}
