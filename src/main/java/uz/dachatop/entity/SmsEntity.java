package uz.dachatop.entity;

import lombok.Getter;
import lombok.Setter;
import uz.dachatop.enums.SmsStatus;

import jakarta.persistence.*;
import uz.dachatop.entity.base.BaseStringIdEntity;

@Getter
@Setter
@Entity
@Table(name = "sms")
public class SmsEntity extends BaseStringIdEntity {


    @Column(name = "phone")
    private String phone;


    @Column(name = "content")
    private String content; // code

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private SmsStatus status;
    @Column
    private  Boolean SmsStatus = Boolean.FALSE;




}
