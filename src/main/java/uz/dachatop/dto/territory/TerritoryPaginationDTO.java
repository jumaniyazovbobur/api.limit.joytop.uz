package uz.dachatop.dto.territory;
//PROJECT NAME --> api.dachatop
// YEAR --> 2023
// MONTH --> 03
// DAY --> 28
// TIME --> 10:48

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TerritoryPaginationDTO {
    private long totalElements;
    private int  totalPages;
    private List<TerritoryDTO> content;

    public TerritoryPaginationDTO(long totalElements, int totalPages, List<TerritoryDTO> content) {
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.content = content;
    }
}
