package uz.dachatop.mapper.apartment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import uz.dachatop.enums.PlaceSubType;
import uz.dachatop.enums.PlaceType;
import uz.dachatop.enums.GlobalStatus;
import uz.dachatop.mapper.PlaceMapperDTO;

@Getter
@Setter
public class ApartmentMapperDTO extends PlaceMapperDTO {
    private Long dayPrice; // дневная цена
    private Long monthPrice; // месячная цена
    private Double totalArea; // общее помощение
    private Integer singleBedRoomCount; // количество одноместных спальный
    private Integer doubleBedRoomCount; // количество двухместный спальный
    private Integer roomCount; // количество двухместный спальный
    private PlaceSubType subType;
    private Long salePrice;
    @JsonProperty("tariff_result")
    private String tariffResult;
}
