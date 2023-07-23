package uz.dachatop.dto.place.dacha;

import lombok.Getter;
import lombok.Setter;
import uz.dachatop.dto.convenience.ConvenienceDTO;
import uz.dachatop.dto.place.PlaceDTO;
import uz.dachatop.dto.place.calendar.PlaceCalendarDTO;
import uz.dachatop.enums.PlaceSubType;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class DachaDTO extends PlaceDTO {
    private String name;
    private PlaceSubType subType;
    private Long salePrice;

    // main information
    private double totalArea; // общее помощение
    private int roomCount; // количество комнат
    private int singleBedRoomCount; // количество одноместных спальный
    private int doubleBedRoomCount; // количество двухместный с
    //price
    private Long weekDayPrice; // обычная цена за сутки
    private Long priceOnSale; // сумма со скидкой
    private Long weekendPrice; // цена по выходным за сутки
    private Long gageOfDeposit; // процент залога
    //time
    private LocalTime enterTime;
    private LocalTime departureTime;
    //Book type
    private int maximumDayBooking;
    private int minimumDayBooking;
    // card
    private String profileCardPan;
    // convenienceList
    private List<ConvenienceDTO> convenienceList;
    // calendar list
    private List<PlaceCalendarDTO> calendarList;
    private Long number; //number
}
