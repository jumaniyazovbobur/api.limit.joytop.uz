package uz.dachatop.dto.auth;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {
    @NotNull(message = "Phone required")
    @Size(min = 12, max = 12, message = "min 12 max 12 size")
    private String phone;
    @NotNull(message = "Password required")
    @Size(min = 6, message = "min size 6")
    private String password;

}
