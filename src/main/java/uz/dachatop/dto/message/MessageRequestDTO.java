package uz.dachatop.dto.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

//PROJECT NAME --> api.dachatop
// YEAR --> 2023
// MONTH --> 03
// DAY --> 01
// TIME --> 14:03
@Getter
@Setter
@Valid
public class MessageRequestDTO {
    private Long id;
    @NotNull(message = "Kirish vaqti majburiy")
    private LocalDate fromDay;
    @NotNull(message = "Ketish vaqti majburiy")
    private LocalDate toDay;
    @NotNull(message = "Pul majburiy")
    private Long price;
    private String text;
    private String phone;

}
