package uz.dachatop.mapper.extreme;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import uz.dachatop.dto.response.place.ExtremeTypeResDTO;
import uz.dachatop.mapper.PlaceMapperDTO;

@Getter
@Setter
public class ExtremeMapperDTO extends PlaceMapperDTO {
    private Long weekDayPrice; // обычная цена (hafta kunlik narx)
    private Long weekendPrice; // цена по выходным (dam olish kunlari narxi)
    // Extreme type
    private String extremeTypeId;
    private ExtremeTypeResDTO extremeType;
    @JsonProperty("tariff_result")
    private String tariffResult;
}
