package uz.dachatop.entity.place.camp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import uz.dachatop.entity.place.PlaceEntity;

import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "camp")
public class CampEntity extends PlaceEntity { // oromgoh
    // main information
    @Column(name = "name", columnDefinition = "TEXT")
    private String name;
    // price
    @Column(name = "price")
    private Long price; // обычная
    @Column(name = "price_on_sale")
    private Long priceOnSale; // сумма со скидкой
    //street,house,apartmentNum - x
}
