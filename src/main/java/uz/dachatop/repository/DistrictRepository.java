package uz.dachatop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uz.dachatop.dto.response.ApiResponse;
import uz.dachatop.entity.DistrictEntity;
import uz.dachatop.entity.RegionEntity;

import java.util.List;
import java.util.Optional;


@Repository
public interface DistrictRepository  extends JpaRepository<DistrictEntity, Long> {


    @Modifying
    @Transactional
    @Query("Update district set visible = :visible where id =:id")
    int deleteStatus(@Param("visible") boolean b, @Param("id") Long id);



    Optional<DistrictEntity> findByIdAndVisibleTrue(Long id);

    Page<DistrictEntity> getAllByVisibleTrue(PageRequest pageable);

    List<DistrictEntity> findByRegionIdAndVisibleTrue(Long regionId);

    Page<DistrictEntity> getAllByVisibleTrueAndRegionId(Long id, PageRequest pageable);
}
