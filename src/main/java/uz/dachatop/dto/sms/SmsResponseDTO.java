package uz.dachatop.dto.sms;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SmsResponseDTO extends SmsRequestDTO {
    private String id;
    private Boolean visible;
    private LocalDateTime createdDate;

    private  Boolean  success;


}
