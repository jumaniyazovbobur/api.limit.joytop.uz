package uz.dachatop.dto.sms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SmsPaginationDTO {
    private long totalElements;
    private int totalPages;
    private List<SmsResponseDTO> content;
}
