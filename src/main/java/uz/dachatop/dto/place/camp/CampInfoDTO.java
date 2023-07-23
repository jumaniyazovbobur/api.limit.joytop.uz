package uz.dachatop.dto.place.camp;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import uz.dachatop.dto.place.PlaceInfoDTO;
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CampInfoDTO extends PlaceInfoDTO {
    private String name;
    private Long price; // обычная
    private Long priceOnSale; // сумма со скидкой
}
