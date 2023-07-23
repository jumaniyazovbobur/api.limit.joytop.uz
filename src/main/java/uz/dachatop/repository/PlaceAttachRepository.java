package uz.dachatop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import uz.dachatop.entity.AttachEntity;
import uz.dachatop.entity.place.PlaceAttachEntity;

import java.util.List;

public interface PlaceAttachRepository extends JpaRepository<PlaceAttachEntity, String> {

    @Query("select aa.attachId from PlaceAttachEntity as aa where aa.placeId=?1")
    List<String> getAttachListByPlace(String v);

    @Modifying
    @Transactional
    @Query("delete from PlaceAttachEntity where placeId=?1 and attachId=?2")
    int deleteByPlaceIdAndAttachId(String placeId, String attachId);

    @Modifying
    @Transactional
    @Query("delete from PlaceAttachEntity where placeId=?1")
    void deleteByPlaceId(String placeId);

    @Query("select aa.attach from PlaceAttachEntity as aa where aa.placeId=?1")
    List<AttachEntity> getAttachListByPlaceId(String v);


}
