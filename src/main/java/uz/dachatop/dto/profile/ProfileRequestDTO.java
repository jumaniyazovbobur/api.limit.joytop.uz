package uz.dachatop.dto.profile;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import uz.dachatop.enums.ProfileRole;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileRequestDTO {

    private String firstName;

    private String lastName;

    private String phone;
    private List<ProfileRole> roleList;
    private String password;


}
