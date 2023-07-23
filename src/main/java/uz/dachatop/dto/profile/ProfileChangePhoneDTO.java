package uz.dachatop.dto.profile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileChangePhoneDTO {
    @NotBlank(message = "Phone required")
    @Size(min = 12, max = 12, message = "min 12 max 12 size")
    private String newPhone;
}
