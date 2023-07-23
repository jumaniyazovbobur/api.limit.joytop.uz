package uz.dachatop.dto.transaction;

import lombok.Getter;
import lombok.Setter;
import uz.dachatop.enums.PlaceType;
import uz.dachatop.enums.SubscriptionStatus;

import java.time.LocalDate;

@Getter
@Setter
public class SubscriptionTransactionFilterRequestDTO {
    private Long tariffPlan;
    private Long tariffPlanPrice;
    private String tariffId;
    private String placeId;
    private PlaceType placeType;
    private LocalDate startDate;
    private LocalDate endDate;
    private SubscriptionStatus status;
}
