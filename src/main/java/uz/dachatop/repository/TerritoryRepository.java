package uz.dachatop.repository;
//PROJECT NAME --> api.dachatop
// YEAR --> 2023
// MONTH --> 03
// DAY --> 28
// TIME --> 10:04

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import uz.dachatop.entity.DistrictEntity;
import uz.dachatop.entity.TerritoryEntity;

import java.util.List;

public interface TerritoryRepository extends JpaRepository<TerritoryEntity,Long> {
    @Modifying
    @Transactional
    @Query("Update territory set visible = :visible where id =:id")
    int deleteStatus(@Param("visible") boolean b, @Param("id") Long id);

    List<TerritoryEntity> findByDistrictIdAndVisibleTrue(Long regionId);

    Page<TerritoryEntity> getAllByVisibleTrueAndDistrictId(Long id, PageRequest pageable);

}
