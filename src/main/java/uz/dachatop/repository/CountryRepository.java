package uz.dachatop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import uz.dachatop.entity.CountryEntity;
import uz.dachatop.mapper.CountryMapper;

import java.util.Optional;

public interface CountryRepository extends JpaRepository<CountryEntity,Long> {

    Iterable<CountryEntity> findAllByVisibleOrderByNameUzAsc(boolean b);
Iterable<CountryEntity> findAllByVisibleOrderByNameRuAsc(boolean b);
Iterable<CountryEntity> findAllByVisibleOrderByNameEnAsc(boolean b);

    Optional<CountryEntity> findByIdAndVisibleTrue(Long id);

    @Query("SELECT  cou.id as cou_id,cou.visible as cou_visible,cou.createdDate as cou_createdDate," +
            " cou.nameEn as nameEn,cou.nameUz as nameUz," +
            " cou.nameRu as nameRu from CountryEntity as cou " +
            " WHERE cou.id=:id and cou.visible = true ")
    Optional<CountryMapper> getCountryByKey(@Param("id") Long  id);


    @Modifying
    @Transactional
    @Query("Update CountryEntity set visible = :visible where id =:id")
    int deleteStatus(@Param("visible") boolean b, @Param("id") Integer id);

    Page<CountryEntity> getAllByVisibleTrue(PageRequest page);


    Optional<Object> getCountryByIdAndVisibleTrue(Long id);
}
