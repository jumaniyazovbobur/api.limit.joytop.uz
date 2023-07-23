package uz.dachatop.dto.profile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class ProfileChangPasswordDTO {
    @NotBlank(message = "oldPassword required")
    @Size(min = 6, message = "min size 6")
    private String oldPassword;

    @NotBlank(message = "Password required")
    @Size(min = 6, message = "min size 6")
    private String newPassword;
}
