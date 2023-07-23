package uz.dachatop.entity.place.hotel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import uz.dachatop.entity.place.PlaceEntity;

@Getter
@Setter
@Entity
@Table(name = "hotel")
public class HotelEntity extends PlaceEntity {
    @Column(name = "name", columnDefinition = "TEXT")
    private String name;

    @Column(name = "star_count")
    private Double starCount;
    @Column(name = "min_price")
    private Long minPrice;
    // HotelRoom

    // transport - x
    // улица, дом, квт - x
    // Convenience -?
}
