package uz.dachatop.dto.message;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

//PROJECT NAME --> api.dachatop
// YEAR --> 2023
// MONTH --> 03
// DAY --> 01
// TIME --> 14:03
@Getter
@Setter
public class MessageUpdateDTO {

    private LocalDateTime fromDay;
    private LocalDateTime toDay;
    private Long fromPrice;
    private Long toPrice;
    private String text;
    private String phone;
    private String sendButton;
}
