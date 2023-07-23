package uz.dachatop.entity;
// PROJECT NAME -> api.you.go
// TIME -> 11:03
// MONTH -> 10
// DAY -> 31
// USER -> abdulazizkhurramov

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "sms_token")
public class SmsTokenEntity {
    @Id
    private String email;
    @Column(columnDefinition = "TEXT")
    private String token;
    @Column()
    private LocalDate createdDate;
}