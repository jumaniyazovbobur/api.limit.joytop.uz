package uz.dachatop.dto.sms;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SmsDTO {
    private String key;
    private String message;
    private String phone;

}
