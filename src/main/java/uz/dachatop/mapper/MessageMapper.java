package uz.dachatop.mapper;
//PROJECT NAME --> api.dachatop
// YEAR --> 2023
// MONTH --> 03
// DAY --> 01
// TIME --> 22:18

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface MessageMapper {

    Long getId();
    String getPhone();
    String getText();
    Long getPrice();
    LocalDate getFrom_day();
    LocalDate getTo_day();
    Boolean getVisible();


}
