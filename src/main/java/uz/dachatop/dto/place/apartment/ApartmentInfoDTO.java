package uz.dachatop.dto.place.apartment;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import uz.dachatop.dto.place.PlaceInfoDTO;
import uz.dachatop.enums.PlaceSubType;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApartmentInfoDTO extends PlaceInfoDTO {
    private Long dayPrice; // дневная цена
    private Long monthPrice; // месячная цена
    private Double totalArea; // общее помощение
    private Integer singleBedRoomCount; // количество одноместных спальный
    private Integer doubleBedRoomCount; // количество двухместный спальный
    private Integer roomCount; // количество двухместный спальный
    private PlaceSubType subType;
    private Long salePrice;
}
