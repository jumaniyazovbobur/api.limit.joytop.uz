package uz.dachatop.entity;

import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import uz.dachatop.entity.base.BaseLongIdEntity;
//qulayliklar

@Entity
@Table(name="convenience")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConvenienceEntity extends BaseLongIdEntity {

    private String nameUz;
    private String nameEn;
    private String nameRu;
}
