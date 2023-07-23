package uz.dachatop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import uz.dachatop.entity.base.BaseLongIdEntity;
import uz.dachatop.entity.CountryEntity;

@Data
@Entity
@Table(name = "region")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegionEntity extends BaseLongIdEntity {

    private String nameUz;
    private String nameRu;
    private String nameEn;

    @Column(name = "country_id")
    private Long countryId;
    @ManyToOne
    @JoinColumn(name = "country_id", insertable = false, updatable = false)
    private CountryEntity country;

    public RegionEntity(String nameUz, String nameRu, String nameEn, Long countryId) {
        this.nameUz = nameUz;
        this.nameRu = nameRu;
        this.nameEn = nameEn;
        this.countryId = countryId;
    }
}

