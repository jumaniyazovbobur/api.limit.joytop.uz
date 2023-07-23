package uz.dachatop.entity.subscriotion;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import uz.dachatop.entity.base.BaseStringIdEntity;

@Getter
@Setter
@Entity
@Table(name = "subscription_tariff")
public class SubscriptionTariffEntity extends BaseStringIdEntity {
    @Column(name = "name_uz")
    private String nameUz;
    @Column(name = "name_ru")
    private String nameRu;
    @Column(name = "name_en")
    private String nameEn;

    @Column
    private String color;

    @Column(name = "order_number")
    private Integer orderNumber;
    @Column(name = "price")
    private Long price; // price in uzs
    @Column(name = "days")
    private Integer days; // active days
}
