package uz.dachatop.dto.subscription;
//PROJECT NAME --> api.dachatop
// YEAR --> 2023
// MONTH --> 04
// DAY --> 02
// TIME --> 22:40

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import uz.dachatop.dto.tariff.SubscriptionTariffDTO;
import uz.dachatop.enums.PlaceType;
import uz.dachatop.enums.SubscriptionStatus;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubscriptionChangeStatusDTO {
    private String id;
    private SubscriptionStatus status;



}
