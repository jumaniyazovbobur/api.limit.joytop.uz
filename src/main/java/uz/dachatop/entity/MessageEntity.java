package uz.dachatop.entity;
//PROJECT NAME --> api.dachatop
// YEAR --> 2023
// MONTH --> 02
// DAY --> 28
// TIME --> 11:42

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import uz.dachatop.entity.base.BaseLongIdEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "message")
public class MessageEntity extends BaseLongIdEntity {

    @Column
    private LocalDate fromDay;
    @Column
    private LocalDate toDay;
    @Column
    private Long price;
    @Column(columnDefinition = "text")
    private String text;
    @Column
    private String phone;
}
