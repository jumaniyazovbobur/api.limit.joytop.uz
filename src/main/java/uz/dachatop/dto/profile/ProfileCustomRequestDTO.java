package uz.dachatop.dto.profile;

import lombok.Getter;
import lombok.Setter;
import uz.dachatop.enums.GlobalStatus;

@Getter
@Setter
public class ProfileCustomRequestDTO {
    private String profileId;
    private String phone;
    private GlobalStatus status;
}
