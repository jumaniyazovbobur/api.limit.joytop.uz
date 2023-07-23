package uz.dachatop.dto.place.travel;

import lombok.Getter;
import lombok.Setter;
import uz.dachatop.dto.place.PlaceFilterDTO;

@Getter
@Setter
public class TravelFilterDTO extends PlaceFilterDTO {
    private Long countryId;

    private String companyName;

    private String companyAddress;
}
