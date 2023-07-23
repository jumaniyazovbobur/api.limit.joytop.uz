package uz.dachatop.dto.subscription;
//PROJECT NAME --> api.dachatop
// YEAR --> 2023
// MONTH --> 04
// DAY --> 02
// TIME --> 22:36

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import uz.dachatop.dto.tariff.SubscriptionTariffDTO;
import uz.dachatop.enums.PlaceType;
import uz.dachatop.enums.SubscriptionStatus;

import java.sql.Timestamp;
import java.time.LocalDateTime;
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubscriptionDTO {
    private String id;
    private String placeId;
    private PlaceType placeType;

    private String tariffId;
//    private SubscriptionTariffDTO tariff;

    private Long price; // price by tariff
    private Integer days; // active days by tariff

    private String startDate;
    private String endDate;
    private SubscriptionStatus status;
    private String prtId;  // profile id who created this subscription.
//
}
