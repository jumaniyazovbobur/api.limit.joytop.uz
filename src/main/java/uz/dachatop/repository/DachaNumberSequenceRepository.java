package uz.dachatop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import uz.dachatop.entity.sequence.DachaNumberSequenceEntity;

public interface DachaNumberSequenceRepository
        extends JpaRepository<DachaNumberSequenceEntity, String> {

    @Modifying
    @Transactional
    @Query(value = "insert into dacha_number_sequence (id, seq_value)  " +
            " select ?1, (select last_value from dacha_number_seq) " +
            " where not exists " +
            " (select 1 from dacha_number_sequence " +
            " where seq_value = (SELECT " +
            " CASE WHEN ?2 THEN " +
            " (select  nextval('dacha_number_seq'))" +
            " ELSE last_value END FROM dacha_number_seq))",
            nativeQuery = true)
    int create(String id,Boolean isNext);
    @Modifying
    @Transactional
    @Query(value = "insert into dacha_number_sequence (id, seq_value)  " +
            " select ?1, ?2 " +
            " where not exists " +
            " (select 1 from dacha_number_sequence " +
            " where seq_value = ?2) ",
            nativeQuery = true)
    int create(String id,Long seqValue);

    @Transactional
    @Modifying
    void deleteBySeqValue(Long seqValue);
}