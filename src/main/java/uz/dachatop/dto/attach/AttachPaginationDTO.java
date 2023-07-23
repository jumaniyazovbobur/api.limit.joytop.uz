package uz.dachatop.dto.attach;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AttachPaginationDTO {
    private long totalElements;
    private int totalPages;
    private List<AttachDTO> content;

    public AttachPaginationDTO(long totalElements, int totalPages, List<AttachDTO> content) {
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.content = content;
    }
}