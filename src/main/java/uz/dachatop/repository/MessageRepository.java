package uz.dachatop.repository;
//PROJECT NAME --> api.dachatop
// YEAR --> 2023
// MONTH --> 02
// DAY --> 28
// TIME --> 13:26

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import uz.dachatop.entity.MessageEntity;
import uz.dachatop.mapper.MessageMapper;

import java.util.Optional;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {


    Optional<MessageEntity> findByPhoneAndVisibleTrue(String phone);

    Optional<MessageEntity> findByIdAndVisibleTrue(Long id);

    @Modifying
    @Transactional
    @Query("Update MessageEntity set visible = :visible where id =:id")
    int deleteStatus(@Param("visible") boolean b, @Param("id") Long id);


    @Query("SELECT  mes.id as id,mes.visible as mes_visible,mes.createdDate as mes_createdDate," +
            " mes.phone as phone,mes.text as text," +
            " mes.price  as price," +
            " mes.toDay as to_day,mes.fromDay as from_day from MessageEntity as mes " +
            " WHERE mes.visible = true ")
    Page<MessageMapper> findAllAndVisibleTrue(PageRequest pageable);

}
