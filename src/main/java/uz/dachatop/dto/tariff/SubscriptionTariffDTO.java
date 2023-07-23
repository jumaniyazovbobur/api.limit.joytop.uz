package uz.dachatop.dto.tariff;

import jakarta.persistence.Column;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

//PROJECT NAME --> api.dachatop
// YEAR --> 2023
// MONTH --> 04
// DAY --> 02
// TIME --> 22:38
@Data
public class SubscriptionTariffDTO {
    private String id;
    private String nameUz;
    private String nameRu;
    private String nameEn;
    private String color;
    private Integer orderNumber;
    private Long price; // price in uzs
    private Integer days; // active days

}
