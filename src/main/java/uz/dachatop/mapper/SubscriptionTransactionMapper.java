package uz.dachatop.mapper;

import jakarta.ws.rs.GET;
import lombok.Getter;
import lombok.Setter;
import uz.dachatop.enums.PlaceType;
import uz.dachatop.enums.SubscriptionTransactionStatus;
@Getter
@Setter
public class SubscriptionTransactionMapper {

    private String id;

    private String place_id;
    private PlaceType place_type;

    private String tariff_id;
    private Long tariff_plan;
    private Long tariff_plan_price;

    private Long price; // price by tariff
    private Integer days; // active days by tariff

    private SubscriptionTransactionStatus status;


}
