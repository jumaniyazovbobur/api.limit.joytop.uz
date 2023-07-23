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
public class ProfilePaginationDTO {

    private Long TotalElements;

    private Integer totalPages;

    private List<ProfileResponseDTO> content;

    public ProfilePaginationDTO(Long totalElements, List<ProfileResponseDTO> content) {
        TotalElements = totalElements;
        this.content = content;
    }
}
