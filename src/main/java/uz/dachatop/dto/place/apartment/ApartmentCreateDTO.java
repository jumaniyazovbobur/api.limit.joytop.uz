package uz.dachatop.dto.place.apartment;


import lombok.Getter;
import lombok.Setter;
import uz.dachatop.dto.convenience.ConvenienceDTO;
import uz.dachatop.dto.place.PlaceCreateDTO;
import uz.dachatop.enums.PlaceSubType;

import java.util.List;

@Getter
@Setter
public class ApartmentCreateDTO extends PlaceCreateDTO {
    //price
    private Long dayPrice; // дневная цена
    private Long monthPrice; // месячная цена
    private Long gageOfDeposit; // процент залога
    // main information
    private double totalArea; // общее помощение
    private int roomCount; // количество комнат
    private int singleBedRoomCount; // количество одноместных спальный
    private int doubleBedRoomCount; // количество двухместный спальный
    // convenienceList
    private List<ConvenienceDTO> convenienceList;
    private PlaceSubType subType;
    private Long salePrice;
}
