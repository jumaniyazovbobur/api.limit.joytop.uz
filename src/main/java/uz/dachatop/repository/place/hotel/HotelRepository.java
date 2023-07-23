package uz.dachatop.repository.place.hotel;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import uz.dachatop.entity.place.dacha.DachaEntity;
import uz.dachatop.entity.place.hotel.HotelEntity;
import uz.dachatop.enums.GlobalStatus;

import java.time.LocalDateTime;
import java.util.Optional;

public interface HotelRepository extends CrudRepository<HotelEntity, String> {

    @Modifying
    @Transactional
    @Query("update HotelEntity set visible = false, deletedId =:deletedId, deletedDate =:deletedDate  where id =:hotelId")
    void deleteHotel(@Param("hotelId") String hotelId,
                     @Param("deletedId") String deletedId,
                     @Param("deletedDate") LocalDateTime deletedDate);

    @Modifying
    @Transactional
    @Query("update HotelEntity set status = :status, deletedId =:profileId, updatedDate =:deletedDate  where id =:hotelId")
    void changeStatus(@Param("hotelId") String dachaId,
                      @Param("profileId") String profileId,
                      @Param("status") GlobalStatus status,
                      @Param("deletedDate") LocalDateTime deletedDate);

    Optional<HotelEntity> findByIdAndVisibleTrue(String hotelId);

    @Modifying
    @Transactional
    @Query("update HotelEntity set profileId = :profileId where id =:apartmentId")
    void transfer(@Param("apartmentId") String apartmentId,
                  @Param("profileId") String profileId);
}
