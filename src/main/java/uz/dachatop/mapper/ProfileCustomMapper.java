package uz.dachatop.mapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.dachatop.enums.AppLanguage;
import uz.dachatop.enums.GlobalStatus;
import uz.dachatop.enums.ProfileRole;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileCustomMapper {
    private String rol_id;
    private ProfileRole rol_role;
    private Boolean rol_visible;
    private String rol_workId;

    private String pro_id;
    private String pro_firstName;
    private String pro_lastName;
    private String pro_password;
    private String pro_phone;
    private LocalDateTime pro_createdDate;
    private Boolean pro_visible;
    private GlobalStatus pro_status;


}
