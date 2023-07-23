package uz.dachatop.entity.place.apartment;

import jakarta.persistence.*;
import lombok.*;
import uz.dachatop.entity.place.PlaceEntity;
import uz.dachatop.enums.PlaceSubType;


@Getter
@Setter
@Entity
@Table(name = "apartment")
public class ApartmentEntity extends PlaceEntity {
    //price
    @Column(name = "day_price")
    private Long dayPrice; // дневная цена
    @Column(name = "month_price")
    private Long monthPrice; // месячная цена
    @Column(name = "gage_of_deposit")
    private Long gageOfDeposit; // процент залога
    // main information
    @Column(name = "total_area")
    private Double totalArea; // общее помощение
    @Column(name = "room_count")
    private Integer roomCount; // количество комнат
    @Column(name = "single_bed_room_count")
    private Integer singleBedRoomCount; // количество одноместных спальный
    @Column(name = "double_bed_room_count")
    private Integer doubleBedRoomCount; // количество двухместный спальный
    // convenience
    @Column(name = "sub_type")
    @Enumerated(EnumType.STRING)
    private PlaceSubType subType;

    @Column(name = "sale_price")
    private Long salePrice;
}
