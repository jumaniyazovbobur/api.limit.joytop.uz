package uz.dachatop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import lombok.*;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import uz.dachatop.entity.base.BaseLongIdEntity;

@EqualsAndHashCode(callSuper = true)
@Entity(name = "district")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DistrictEntity extends BaseLongIdEntity {
    @Column(name = "region_id")
    private Long regionId;
    @ManyToOne
    @JoinColumn(name = "region_id", insertable = false, updatable = false)
    private RegionEntity region;
    @Column
    private String nameUz;
    @Column
    private String nameRu;
    @Column
    private String nameEn;
    @Column
    private String county;
}
