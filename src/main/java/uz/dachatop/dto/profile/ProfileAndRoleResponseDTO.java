package uz.dachatop.dto.profile;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.codec.language.bm.Lang;

import uz.dachatop.enums.GlobalStatus;
import uz.dachatop.enums.ProfileRole;
import uz.dachatop.enums.UserType;


import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ProfileAndRoleResponseDTO {

    private List<ProfileRoleDTO> roleList;

    private String firstName;
    private String lastName;
    private String phone;
    private String password;
    private String id;
    private GlobalStatus status;
    private LocalDateTime createDate;

    private UserType userType; // remove

    private String tempPassword;

//    private GiverInfoDTO giverInfo; // remove

}
