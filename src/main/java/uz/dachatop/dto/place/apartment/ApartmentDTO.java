package uz.dachatop.dto.place.apartment;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.dachatop.dto.convenience.ConvenienceDTO;
import uz.dachatop.dto.attach.AttachDTO;
import uz.dachatop.dto.place.PlaceDTO;
import uz.dachatop.dto.place.calendar.PlaceCalendarDTO;
import uz.dachatop.enums.GlobalStatus;
import uz.dachatop.enums.PlaceSubType;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApartmentDTO extends PlaceDTO {
    private int roomCount; // количество комнат
    private double totalArea; // общее помощение
    private int singleBedRoomCount; // количество одноместных спальный
    private int doubleBedRoomCount; // количество двухместный спальный
    // price
    private Long dayPrice; // дневная цена
    private Long monthPrice; // месячная цена
    private Long gageOfDeposit; // процент залога
    // card
    private String profileCardPan;
    private GlobalStatus status;
    // attachList
    private List<AttachDTO> attach;
    // convenienceList
    private List<ConvenienceDTO> convenienceList;
    // calendar list
    private List<PlaceCalendarDTO> calendarList;

    private PlaceSubType subType;
    private Long salePrice;
}
