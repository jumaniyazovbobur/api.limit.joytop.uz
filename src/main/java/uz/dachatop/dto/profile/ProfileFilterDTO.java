package uz.dachatop.dto.profile;

import lombok.Getter;
import lombok.Setter;
import uz.dachatop.enums.ProfileRole;

@Getter
@Setter
public class ProfileFilterDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String phone;
    private ProfileRole role;
}
