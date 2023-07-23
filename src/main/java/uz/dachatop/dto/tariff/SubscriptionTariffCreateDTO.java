package uz.dachatop.dto.tariff;

import lombok.Data;

//PROJECT NAME --> api.dachatop
// YEAR --> 2023
// MONTH --> 04
// DAY --> 03
// TIME --> 15:17
@Data
public class SubscriptionTariffCreateDTO {
    private String nameUz;
    private String nameRu;
    private String nameEn;
    private String color;
    private Integer orderNumber;
    private Long price; // price in uzs
    private Integer days; // active days
}
