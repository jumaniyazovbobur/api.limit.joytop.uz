package uz.dachatop.dto.place.calendar;

import lombok.Getter;
import lombok.Setter;
import lombok.*;
import uz.dachatop.enums.BookingStatus;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PlaceCalendarDTO {
    private LocalDate date;
    private BookingStatus status;
}
