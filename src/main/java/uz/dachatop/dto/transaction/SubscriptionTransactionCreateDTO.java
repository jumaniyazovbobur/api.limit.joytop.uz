package uz.dachatop.dto.transaction;
//PROJECT NAME --> api.dachatop
// YEAR --> 2023
// MONTH --> 04
// DAY --> 02
// TIME --> 22:36

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import uz.dachatop.enums.PlaceType;
import uz.dachatop.enums.SubscriptionStatus;
import uz.dachatop.enums.SubscriptionTransactionStatus;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubscriptionTransactionCreateDTO {
    private String id;

    private String placeId;
    private PlaceType placeType;

    private String tariffId;
    private Long tariffPlan;
    private Long tariffPlanPrice;


    private Long price; // price by tariff
    private Integer days; // active days by tariff

    private SubscriptionTransactionStatus status;
}
