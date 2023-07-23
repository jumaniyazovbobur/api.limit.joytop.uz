package uz.dachatop.dto.subscription;
//PROJECT NAME --> api.dachatop
// YEAR --> 2023
// MONTH --> 04
// DAY --> 02
// TIME --> 22:40

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import uz.dachatop.enums.PlaceType;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubscriptionCreateDTO {
    @NotBlank(message = "PlaceId required")
    private String placeId;
    @NonNull
    private PlaceType placeType;
    @NotBlank(message = "TariffId required")
    private String tariffId;
    @NonNull()
    private Integer purchaseTimes; // tariffPlan; // times number
}
