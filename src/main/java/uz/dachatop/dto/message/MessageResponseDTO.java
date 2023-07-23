package uz.dachatop.dto.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

//PROJECT NAME --> api.dachatop
// YEAR --> 2023
// MONTH --> 03
// DAY --> 01
// TIME --> 14:02
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageResponseDTO extends MessageRequestDTO {

    private Boolean visible;
    private LocalDateTime createdDate;
}
