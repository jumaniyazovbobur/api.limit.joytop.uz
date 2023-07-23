package uz.dachatop.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import javax.validation.constraints.NotNull;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "First name required")
    private String firstname;

    @NotBlank(message = "Last name required")
    private String lastname;

    @NotBlank(message = "Phone required")
    @Size(min = 12, max = 12, message = "min 12 max 12 size")
    private String phone;

    @NotBlank(message = "Password required")
    @Size(min = 6, message = "min size 6")
    private String password;
}
