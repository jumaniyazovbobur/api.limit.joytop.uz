package uz.dachatop.dto.profile;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.dachatop.enums.GlobalStatus;
import uz.dachatop.enums.ProfileRole;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileResponseDTO extends ProfileRequestDTO {

    private String id;
    private GlobalStatus status;
    private LocalDateTime createDate;

    public ProfileResponseDTO(String firstName, String lastName, String phone,
                              List<ProfileRole> roleList, String password) {
        super(firstName, lastName, phone, roleList, password);
//        this.id = id;
//        this.status = status;
//        this.createDate = createDate;
    }


//    public ProfileResponseDTO(String firstName, String lastName, String phone,
//                              String password, String id, GlobalStatus status,
//                              LocalDateTime createDate) {
//        super(firstName, lastName, phone, password);
//        this.id = id;
//        this.status = status;
//        this.createDate = createDate;
//    }
}
