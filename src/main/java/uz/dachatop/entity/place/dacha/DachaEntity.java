package uz.dachatop.entity.place.dacha;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import uz.dachatop.entity.TerritoryEntity;
import uz.dachatop.entity.place.PlaceEntity;
import uz.dachatop.enums.PlaceSubType;

import java.time.LocalTime;


@Getter
@Setter
@Entity
@Table(name = "dacha")
public class DachaEntity extends PlaceEntity {
    @Column(name = "name", columnDefinition = "TEXT")
    private String name;
    // main information
    @Column(name = "total_area")
    private double totalArea; // общее помощение
    @Column(name = "room_count")
    private int roomCount; // количество комнат
    @Column(name = "single_bed_room_count")
    private int singleBedRoomCount; // количество одноместных спальный
    @Column(name = "double_bed_room_count")
    private int doubleBedRoomCount; // количество двухместный с
    //price
    @Column(name = "week_day_price")
    private Long weekDayPrice; // обычная цена за сутки
    @Column(name = "price_on_sale")
    private Long priceOnSale; // сумма со скидкой
    @Column(name = "weekend_price")
    private Long weekendPrice; // цена по выходным за сутки
    @Column(name = "gage_of_deposit")
    private Long gageOfDeposit; // процент залога
    //time
    @Column(name = "enter_time")
    private LocalTime enterTime;
    @Column(name = "departure_time")
    private LocalTime departureTime;
    //Book type
    @Column(name = "maximum_day_booking")
    private int maximumDayBooking;
    @Column(name = "minimum_day_booking")
    private int minimumDayBooking;
    // card
    @Column(name = "profile_card_pan")
    private String profileCardPan;
    // Convenience

    // улица, дом, квт - x
    @Column(name = "number")
    private Long number;

    @Column(name = "sale_price")
    private Long salePrice;

    @Column(name = "admin_checked")
    private Boolean adminChecked = false;

    @Column(name = "sub_type")
    @Enumerated(EnumType.STRING)
    private PlaceSubType subType;

    @Column(name = "territory_id")
    private Long territoryId;

    @ManyToOne
    @JoinColumn(name = "territory_id", insertable = false, updatable = false)
    private TerritoryEntity territory;

}
