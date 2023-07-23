package uz.dachatop.mapper;

import lombok.Getter;
import lombok.Setter;
import uz.dachatop.dto.country.CountryResponseDTO;

@Getter
@Setter
public class TravelMapperDTO extends PlaceMapperDTO {
    private Long countryId;
    private CountryResponseDTO country;
    // main information
    private String companyName;
    private String companyAddress;

    private Double standardsPrice;

    private Double priceOnSale;
}
