package uz.dachatop.dto.place.apartment;

import lombok.Getter;
import lombok.Setter;
import uz.dachatop.dto.convenience.ConvenienceDTO;
import uz.dachatop.dto.place.PlaceFilterDTO;
import uz.dachatop.enums.PlaceSubType;

import java.util.List;

@Getter
@Setter
public class ApartmentFilterDTO extends PlaceFilterDTO {
    private Integer roomCount; // количество комнат
    private Integer singleBedRoomCount; // количество одноместных спальный
    private Integer doubleBedRoomCount; // количество двухместный спальный
    // convenienceList
    private List<Long> convenienceList;
    private PlaceSubType subType;
}
