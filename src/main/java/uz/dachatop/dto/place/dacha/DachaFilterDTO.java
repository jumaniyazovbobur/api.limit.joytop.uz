package uz.dachatop.dto.place.dacha;

import lombok.Getter;
import lombok.Setter;
import uz.dachatop.dto.place.PlaceFilterDTO;
import uz.dachatop.enums.PlaceSubType;

import java.util.List;

@Getter
@Setter
public class DachaFilterDTO extends PlaceFilterDTO {
    private Integer roomCount;
    private Integer bedRoomCount;  //количество двухместный спальный или  количество одноместных спальный
    private List<Long> convenienceList; // удобства
    private Long number; // number
    private PlaceSubType subType;
}
