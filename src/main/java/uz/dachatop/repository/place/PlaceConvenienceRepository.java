package uz.dachatop.repository.place;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uz.dachatop.entity.place.PlaceConvenienceEntity;
import uz.dachatop.mapper.ConvenientNameMapper;

import java.util.List;


@Repository
public interface PlaceConvenienceRepository extends JpaRepository<PlaceConvenienceEntity, String> {


    @Query("select ace.convenienceId from PlaceConvenienceEntity as ace where ace.placeId=?1")
    List<Long> getConvenienceListByPlaceId(String placeId);

    @Modifying
    @Transactional
    int deleteByPlaceIdAndConvenienceId(String placeId, Long convenienceId);

    @Query(value = "select c.id as convenientId, " +
            " case :lang when 'uz' then c.name_uz when 'en' then c.name_en else c.name_ru end as convenientName" +
            " from place_convenience as pc " +
            " inner join convenience as c on c.id = pc.convenience_id" +
            " where place_id =:placeId",
            nativeQuery = true)
    List<ConvenientNameMapper> getApartmentConvenientList(@Param("placeId") String placeId,
                                                          @Param("lang") String lang);

    @Modifying
    @Transactional
    void deleteByPlaceId(String roomId);
}
