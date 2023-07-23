package uz.dachatop.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uz.dachatop.entity.RegionEntity;

import java.beans.Transient;
import java.util.List;
import java.util.Optional;


public interface RegionRepository extends JpaRepository<RegionEntity, Long> {

    @Query(value = "Select n from RegionEntity n where n.nameUz like  %:name%")
    List<RegionEntity> findByNameUz(@Param("name") String name);

    @Query(value = "Select n from RegionEntity n where n.nameEn like  %:name%")
    List<RegionEntity> findByNameEn(@Param("name") String name);

    @Query(value = "Select n from RegionEntity n where n.nameRu like  %:name%")
    List<RegionEntity> findByNameRu(@Param("name") String name);


    @Modifying
    @Transactional
    @Query("Update RegionEntity set visible = :visible where id =:id")
    int deleteStatus(@Param("visible") Boolean b, @Param("id") Long id);

    Optional<RegionEntity> findByIdAndVisibleTrue(Long id);

    List<RegionEntity> findAllByVisibleTrue(Sort sort);


    List<RegionEntity> findByCountryIdAndVisibleTrue(Long countryId);
}
