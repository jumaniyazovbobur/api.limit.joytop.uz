package uz.dachatop.dto.subscription;
//PROJECT NAME --> api.dachatop
// YEAR --> 2023
// MONTH --> 04
// DAY --> 11
// TIME --> 9:24

import lombok.Getter;
import lombok.Setter;
import uz.dachatop.enums.PlaceType;
import uz.dachatop.enums.SubscriptionStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class SubscriptionFilterRequestDTO {
    private String tariffId;
    private String placeId;
    private PlaceType placeType;
    private LocalDate startDate;
    private LocalDate endDate;
    private SubscriptionStatus status;
}
