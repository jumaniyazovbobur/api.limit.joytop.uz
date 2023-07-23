package uz.dachatop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import uz.dachatop.entity.base.BaseLongIdEntity;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "country")
public class CountryEntity extends BaseLongIdEntity {

    private String nameUz;
    private String nameRu;
    private String nameEn;

}