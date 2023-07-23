package uz.dachatop.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileAndRolePaginationDTO {
    private Long totalElements;
    private Integer totalPages;
    private List<ProfileAndRoleResponseDTO> content;
}
