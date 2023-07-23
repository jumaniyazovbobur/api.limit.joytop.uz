package uz.dachatop.dto.sms;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SmsRequestDTO {
    private String key;
    private String phone;
    private String content;

    public SmsRequestDTO(String phone, String content) {
        this.phone = phone;
        this.content = content;
    }
}
