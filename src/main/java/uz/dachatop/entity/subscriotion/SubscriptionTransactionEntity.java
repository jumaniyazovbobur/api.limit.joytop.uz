package uz.dachatop.entity.subscriotion;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import uz.dachatop.entity.base.BaseStringIdEntity;
import uz.dachatop.enums.PlaceType;
import uz.dachatop.enums.SubscriptionTransactionStatus;

@Getter
@Setter
@Entity
@Table(name = "subscription_transaction")
public class SubscriptionTransactionEntity extends BaseStringIdEntity {
    @Column(name = "price")
    private Long price; // price in uzs
    @Column(name = "days")
    private Integer days; // active days
    @Column(name = "place_id")
    private String placeId;
    @Column(name = "tariff_id")
    private String tariffId;
    @Enumerated(EnumType.STRING)
    @Column(name = "place_type")
    private PlaceType placeType;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private SubscriptionTransactionStatus status = SubscriptionTransactionStatus.SUCCESS;
}
