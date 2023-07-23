package uz.dachatop.dto.place.extreme;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.dachatop.dto.place.PlaceFilterDTO;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExtremeFilterDTO extends PlaceFilterDTO {

    private String extremeTypeId;
}
