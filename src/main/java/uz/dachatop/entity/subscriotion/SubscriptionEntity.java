package uz.dachatop.entity.subscriotion;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import uz.dachatop.enums.PlaceType;
import uz.dachatop.enums.SubscriptionStatus;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "subscription")
public class SubscriptionEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    @Column(name = "place_id")
    private String placeId;
    @Enumerated(EnumType.STRING)
    @Column(name = "place_type")
    private PlaceType placeType;

    @Column(name = "tariff_id")
    private String tariffId;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "tariff_id", insertable = false, updatable = false)
    private SubscriptionTariffEntity tariff;

    @Column(name = "price")
    private Long price; // price by tariff
    @Column(name = "days")
    private Integer days; // active days by tariff

    @Column(name = "created_date")
    private LocalDateTime startDate;
    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private SubscriptionStatus status;
    @Column(name = "prt_id")
    private String prtId;  // profile id who created this subscription.
//    private String transactionId

    @Column
    private Boolean visible = true;
}
