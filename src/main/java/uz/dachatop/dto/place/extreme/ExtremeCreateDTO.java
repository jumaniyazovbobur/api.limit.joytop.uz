package uz.dachatop.dto.place.extreme;

import lombok.Getter;
import lombok.Setter;
import uz.dachatop.dto.place.PlaceCreateDTO;
import uz.dachatop.dto.response.place.ExtremeTypeResDTO;
import uz.dachatop.entity.place.extreme.ExtremeTypeEntity;

@Getter
@Setter
public class ExtremeCreateDTO extends PlaceCreateDTO {
    //price
    private Long weekDayPrice; // обычная цена (hafta kunlik narx)
    private Long weekendPrice; // цена по выходным (dam olish kunlari narxi)
    // Extreme type
    private String extremeTypeId;
    private ExtremeTypeResDTO extremeType;
}
