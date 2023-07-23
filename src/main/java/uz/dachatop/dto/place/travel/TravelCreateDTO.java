package uz.dachatop.dto.place.travel;

import lombok.Getter;
import lombok.Setter;
import uz.dachatop.dto.place.PlaceCreateDTO;
import uz.dachatop.entity.CountryEntity;

@Getter
@Setter
public class TravelCreateDTO extends PlaceCreateDTO {
    // Address
    private Long countryId;
    private CountryEntity country;
    // main information
    private String companyName;
    private Double standardsPrice;
    private Double priceOnSale;
    private String companyAddress;
}
