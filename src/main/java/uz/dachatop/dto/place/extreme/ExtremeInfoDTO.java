package uz.dachatop.dto.place.extreme;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import uz.dachatop.dto.place.PlaceInfoDTO;
import uz.dachatop.dto.response.place.ExtremeTypeResDTO;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExtremeInfoDTO extends PlaceInfoDTO {
    private Long weekDayPrice; // обычная цена (hafta kunlik narx)
    private Long weekendPrice; // цена по выходным (dam olish kunlari narxi)
    // Extreme type
    private String extremeTypeId;
    private ExtremeTypeResDTO extremeType;
    @JsonProperty("tariff_result")
    private String tariffResult;
}
