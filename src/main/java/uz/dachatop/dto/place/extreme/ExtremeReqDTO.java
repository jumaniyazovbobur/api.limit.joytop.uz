package uz.dachatop.dto.place.extreme;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import uz.dachatop.dto.place.PlaceDTO;
import uz.dachatop.dto.response.place.ExtremeTypeResDTO;
import uz.dachatop.entity.place.extreme.ExtremeTypeEntity;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExtremeReqDTO extends PlaceDTO {
    private Long weekDayPrice; // обычная цена (hafta kunlik narx)
    private Long weekendPrice; // цена по выходным (dam olish kunlari narxi)
    // Extreme type
    private String extremeTypeId;
    private ExtremeTypeResDTO extremeType;
}
