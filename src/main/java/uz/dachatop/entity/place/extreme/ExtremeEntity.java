package uz.dachatop.entity.place.extreme;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import uz.dachatop.entity.place.PlaceEntity;


@Getter
@Setter
@Entity
@Table(name = "extreme")
public class ExtremeEntity extends PlaceEntity {
    //price
    @Column(name = "week_day_price")
    private Long weekDayPrice; // обычная цена (hafta kunlik narx)
    @Column(name = "weekend_price")
    private Long weekendPrice; // цена по выходным (dam olish kunlari narxi)
    // Extreme type
    @Column(name = "extreme_type_id")
    private String extremeTypeId;
    @ManyToOne
    @JoinColumn(name = "extreme_type_id", insertable = false, updatable = false)
    private ExtremeTypeEntity extremeType;

}
