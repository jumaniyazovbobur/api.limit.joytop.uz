package uz.dachatop.entity.place;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import uz.dachatop.entity.base.BaseStringIdEntity;
import uz.dachatop.enums.BookingStatus;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "place_calendar")
public class PlaceCalendarEntity extends BaseStringIdEntity {
    @Column(name = "place_id")
    private String placeId;
    @Column(name = "cal_date")
    private LocalDate calDate;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
}
