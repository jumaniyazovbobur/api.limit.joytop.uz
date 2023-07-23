package uz.dachatop.dto.district;
// PROJECT NAME -> api.dachatop
// TIME -> 21:43
// DATE -> 08/02/23
import lombok.Getter;
import lombok.Setter;
import uz.dachatop.dto.district.DistrictDTO;

import java.util.List;

@Getter
@Setter
public class DistrictPaginationDTO {
    private long totalElements;
    private int  totalPages;
    private List<DistrictDTO> content;

    public DistrictPaginationDTO(long totalElements, int totalPages, List<DistrictDTO> content) {
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.content = content;
    }


}
