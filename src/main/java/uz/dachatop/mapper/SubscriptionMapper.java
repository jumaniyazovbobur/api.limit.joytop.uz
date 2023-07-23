package uz.dachatop.mapper;
//PROJECT NAME --> api.dachatop
// YEAR --> 2023
// MONTH --> 04
// DAY --> 10
// TIME --> 16:28

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.dachatop.enums.PlaceType;
import uz.dachatop.enums.SubscriptionStatus;

import java.sql.Timestamp;
import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionMapper {
    private String sub_id;
    private String profile_id;
    private String place_id;
    private PlaceType place_type;
    private Long sub_price;
    private String tariff_id;
    private Integer sub_days;
    private String created_date;
    private String end_date;
    private SubscriptionStatus sub_status;
}
