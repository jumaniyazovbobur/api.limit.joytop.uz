package uz.dachatop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.dachatop.enums.ProfileRole;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JwtDTO {

    private String phone;

    private ProfileRole role;

    public JwtDTO(String phone) {
        this.phone = phone;
    }
}
