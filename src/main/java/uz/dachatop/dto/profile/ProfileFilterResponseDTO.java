package uz.dachatop.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.dachatop.entity.ProfileEntity;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileFilterResponseDTO {
    private List<ProfileEntity> entityList;
    private Long totalCount;
}
