package uz.dachatop.dto.place;

import lombok.Getter;
import lombok.Setter;
import uz.dachatop.enums.PlaceType;


@Getter
@Setter
public class PlaceFilterDTO {
    private String id;
    private Long regionId;
    private Long territoryId;
    private Long districtId;
    private PlaceType type;
    private Long priceFrom;
    private Long priceTo;
    private String profileId;
    private String name;
    private Long number;
    private Double totalArea;
}
