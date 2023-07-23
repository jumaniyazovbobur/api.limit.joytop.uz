package uz.dachatop.dto.profile;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileRestartSmsDTO {
    @NotNull(message = "Phone required")
    @Size(min = 12, max = 12, message = "min 12 max 12 size")
    private String phone;
}
