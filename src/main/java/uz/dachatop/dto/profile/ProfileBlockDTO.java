package uz.dachatop.dto.profile;

import lombok.Getter;
import lombok.Setter;
import uz.dachatop.enums.GlobalStatus;
@Getter
@Setter
public class ProfileBlockDTO {
    private String id;
    private GlobalStatus status;
}
