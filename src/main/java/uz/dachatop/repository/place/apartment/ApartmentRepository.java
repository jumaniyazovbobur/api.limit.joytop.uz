package uz.dachatop.repository.place.apartment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import uz.dachatop.entity.place.apartment.ApartmentEntity;
import uz.dachatop.enums.GlobalStatus;
import uz.dachatop.mapper.apartment.IApartmentInfoMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ApartmentRepository extends JpaRepository<ApartmentEntity, String> {


    @Query(value = "SELECT count(ap.id) from apartment as ap " +
            "inner join region on region.id = ap.region_id " +
            "inner join district on district.id = ap.district_id " +
            "and ap.visible = true",
            nativeQuery = true)
    Long getProfileApartmentCount(@Param("profileId") String profileId);

    @Query(value = "SELECT ap.id             as                                        apartmentId,\n" +
            "       region.id                as                                        regionId,\n" +
            "       case :lang when 'uz' then region.name_uz when 'en' then region.name_en else region.name_ru end as regionName, \n" +
            "       district.id              as                                        districtId,\n" +
            "       case :lang when 'uz' then district.name_uz when 'en' then district.name_en else district.name_ru end as districtName,\n" +
            "       ap.latitude              as                                        latitude,\n" +
            "       ap.longitude             as                                        longitude,\n" +
            "       ap.week_day_price        as                                        weekDayPrice,\n" +
            "       ap.weekend_price         as                                        weekendPrice,\n" +
            "       ap.total_area            as                                        totalArea,\n" +
            "       ap.single_bed_room_count as                                        singleBedRoomCount,\n" +
            "       ap.double_bed_room_count as                                        doubleBedRoomCount,\n" +
            "       ap.type                  as                                        apartmentType,\n" +
            "       (select attach_id from apartment_attach where apartment_id = ap.id limit 1) as mianAttach\n" +
            "from apartment as ap\n" +
            "inner join region on region.id = ap.region_id\n" +
            "inner join district on district.id = ap.district_id\n" +
            "where ap.profile_id =:profileId\n" +
            "and ap.visible = true\n" +
            "    limit :size offset :offset",
            nativeQuery = true)
    List<IApartmentInfoMapper> getProfileApartmentList(@Param("profileId") String profileId,
                                                       @Param("lang") String lang,
                                                       @Param("offset") int offset,
                                                       @Param("size") int size);

    @Query("from ApartmentEntity where id =:apartmentId and visible = true")
    Optional<ApartmentEntity> findByIdAndVisibleTrue(@Param("apartmentId") String apartmentId);

    @Modifying
    @Transactional
    @Query("update ApartmentEntity set visible = false, deletedId =:deletedId, deletedDate =:deletedDate  where id =:apartmentId")
    void deleteApartment(@Param("apartmentId") String apartmentId,
                                                     @Param("deletedId") String deletedId,
                                                     @Param("deletedDate") LocalDateTime deletedDate);

    @Modifying
    @Transactional
    @Query("update ApartmentEntity set status = :status, deletedId =:profileId, updatedDate =:deletedDate  where id =:dachaId")
    void changeStatus(@Param("dachaId") String dachaId,
                      @Param("profileId") String profileId,
                      @Param("status") GlobalStatus status,
                      @Param("deletedDate") LocalDateTime deletedDate);

    @Modifying
    @Transactional
    @Query("update ApartmentEntity set profileId = :profileId where id =:apartmentId")
    void transfer(@Param("apartmentId") String apartmentId,
                         @Param("profileId") String profileId);

}
