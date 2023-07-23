package uz.dachatop.dto.place;

import uz.dachatop.dto.profile.ProfileResponseDTO;

import java.util.List;

public class PlacePaginationDTO {
    private Long TotalElements;

    private Integer totalPages;

    private List<PlaceDTO> content;

    public PlacePaginationDTO(Long totalElements, List<PlaceDTO> content) {
        TotalElements = totalElements;
        this.content = content;
    }
}
