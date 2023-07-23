package uz.dachatop.dto.place.travel;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import uz.dachatop.dto.country.CountryResponseDTO;
import uz.dachatop.dto.place.PlaceInfoDTO;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TravelInfoDTO extends PlaceInfoDTO {
    private Long countryId;
    private CountryResponseDTO country;
    // main information
    private String companyName;
    private String companyAddress;

    private Double standardsPrice;

    private Double priceOnSale;
}
