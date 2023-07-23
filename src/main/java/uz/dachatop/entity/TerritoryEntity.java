package uz.dachatop.entity;
//PROJECT NAME --> api.dachatop
// YEAR --> 2023
// MONTH --> 03
// DAY --> 28
// TIME --> 10:04

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import uz.dachatop.entity.base.BaseLongIdEntity;


@EqualsAndHashCode(callSuper = true)
@Entity(name = "territory")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TerritoryEntity extends BaseLongIdEntity {
    @Column(name = "district_id")
    private Long districtId;
    @ManyToOne
    @JoinColumn(name = "district_id", insertable = false, updatable = false)
    private DistrictEntity district;
    @Column
    private String nameUz;
    @Column
    private String nameRu;
    @Column
    private String nameEn;
    @Column
    private String county;
}
