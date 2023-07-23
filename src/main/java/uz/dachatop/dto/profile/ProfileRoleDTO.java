package uz.dachatop.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.dachatop.enums.ProfileRole;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileRoleDTO {
    private String id;
    private String workId;
    private ProfileRole role;
//    private List<ProfileRole> roleList;
    private Boolean visible;


//    public ProfileRoleDTO(String id, String workId, List<ProfileRole> roleList, Boolean visible) {
//        this.id = id;
//        this.workId = workId;
//        this.roleList = roleList;
//        this.visible = visible;
//    }
}
