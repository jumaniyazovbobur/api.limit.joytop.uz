package uz.dachatop.dto.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

//PROJECT NAME --> api.dachatop
// YEAR --> 2023
// MONTH --> 03
// DAY --> 01
// TIME --> 22:14
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessagePaginationDTO {
    private long totalElements;
    private int  totalPages;
    private List<MessageResponseDTO> content;

    public MessagePaginationDTO(long totalElements, int totalPages, List<MessageResponseDTO> content) {
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.content = content;
    }
}
