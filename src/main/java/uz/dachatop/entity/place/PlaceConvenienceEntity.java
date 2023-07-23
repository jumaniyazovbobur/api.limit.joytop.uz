package uz.dachatop.entity.place;

import jakarta.persistence.*;
import lombok.*;
import uz.dachatop.entity.ConvenienceEntity;
import uz.dachatop.entity.base.BaseStringIdEntity;

@Entity
@Getter
@Setter
@Table(name = "place_convenience")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlaceConvenienceEntity extends BaseStringIdEntity {
    @Column(name = "place_id")
    private String placeId;

    @Column(name = "convenience_id")
    private Long convenienceId;
    @ManyToOne
    @JoinColumn(name = "convenience_id", insertable = false, updatable = false)
    private ConvenienceEntity convenience;
}
