package uz.dachatop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.dachatop.entity.base.BaseStringIdEntity;
import uz.dachatop.enums.GlobalStatus;
import uz.dachatop.enums.ProfileRole;
import uz.dachatop.enums.UserType;

import javax.validation.constraints.NotNull;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "profile")
public class ProfileEntity extends BaseStringIdEntity {

    @Enumerated(EnumType.STRING)
    private UserType userType; // remove
    @NotNull
    @Column(name = ("first_name"))
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    @Column()
    private String phone;
    @Column(name = "temp_phone")
    private String tempPhone;
    @Column(name = "password")
    private String password;
    @Column(name = "temp_password")
    private String tempPassword;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private GlobalStatus status;

//    @Column(name = "role")
//    @Enumerated(EnumType.STRING)
//    private ProfileRole role;
}
