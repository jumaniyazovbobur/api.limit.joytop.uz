package uz.dachatop.entity.place.hotel;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import uz.dachatop.entity.ProfileEntity;
import uz.dachatop.entity.base.BaseStringIdEntity;
import uz.dachatop.enums.RoomType;

@Getter
@Setter
@Entity
@Table(name = "hotel_room")
public class HotelRoomEntity extends BaseStringIdEntity {
    @Enumerated(EnumType.STRING)
    @Column(name = "room_type")
    private RoomType roomType;
    @Column(name = "total_area")
    private double totalArea; // общее помощение
    @Column(name = "day_price")
    private Long dayPrice; // дневная цена
    @Column(name = "price_on_sale")
    private Long priceOnSale; // сумма со скидкой
    // hotel
    @Column(name = "hotel_id")
    private String hotelId;
    @ManyToOne
    @JoinColumn(name = "hotel_id", insertable = false, updatable = false)
    private HotelEntity hotel;
    // Convenience
}
