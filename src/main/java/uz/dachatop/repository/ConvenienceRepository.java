package uz.dachatop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uz.dachatop.entity.ConvenienceEntity;

import java.util.Optional;


@Repository
public interface ConvenienceRepository extends JpaRepository<ConvenienceEntity, Long> {
    @Modifying
    @Transactional
    @Query("Update ConvenienceEntity set visible = :visible where id =:id")
    int deleteStatus(@Param("visible") boolean b, @Param("id") Long id);

    Optional<ConvenienceEntity> findByIdAndVisibleTrue(Long Id);

    Iterable<ConvenienceEntity> findAllByVisibleTrue();

}
