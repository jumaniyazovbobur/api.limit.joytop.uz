package uz.dachatop.repository.place.extreme;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import uz.dachatop.entity.place.extreme.ExtremeTypeEntity;

import java.util.List;
import java.util.Optional;

public interface ExtremeTypeRepository extends JpaRepository<ExtremeTypeEntity, String> {

    Optional<ExtremeTypeEntity> findByIdAndVisibleIsTrue(String id);

    Page<ExtremeTypeEntity> findAllByVisibleIsTrue(Pageable pageable);
    List<ExtremeTypeEntity> findAllByVisibleIsTrue();

    @Query(value = "update ExtremeTypeEntity set visible = ?1 where id = ?2 ")
    @Modifying
    @Transactional
    void updateVisible(Boolean visible, String id);
}
