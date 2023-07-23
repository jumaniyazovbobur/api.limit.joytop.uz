package uz.dachatop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import uz.dachatop.entity.place.PlaceCalendarEntity;
import uz.dachatop.enums.BookingStatus;

import java.time.LocalDate;
import java.util.List;

public interface PlaceCalendarRepository extends JpaRepository<PlaceCalendarEntity, String> {
    PlaceCalendarEntity findByPlaceIdAndCalDateAndVisibleTrue(String id, LocalDate calDate);

    @Modifying
    @Transactional
    @Query("Update PlaceCalendarEntity set status=:status where placeId =:placeId and calDate=:date")
    int update(@Param("placeId") String placeId,
               @Param("date") LocalDate date,
               @Param("status") BookingStatus status);

    @Modifying
    @Transactional
    @Query("Update PlaceCalendarEntity set status=:status where placeId =:placeId and calDate not in (:dates)")
    int update(@Param("placeId") String placeId,
               @Param("dates") List<LocalDate> date,
               @Param("status") BookingStatus status);


    @Query("select apa FROM PlaceCalendarEntity as apa where apa.placeId =?1 and apa.visible =true and date_part('year',apa.calDate) = ?2 and date_part('month',apa.calDate) = ?3" )
    List<PlaceCalendarEntity> findByApartmentIdAndVisibleTrue(String id, Integer year, Integer month);
}
