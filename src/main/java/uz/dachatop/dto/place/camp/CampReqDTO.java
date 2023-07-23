package uz.dachatop.dto.place.camp;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import uz.dachatop.dto.convenience.ConvenienceDTO;
import uz.dachatop.dto.place.PlaceDTO;
import uz.dachatop.dto.place.calendar.PlaceCalendarDTO;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CampReqDTO extends PlaceDTO {
    // main information
    private String name;
    private Long price; // обычная
    private Long priceOnSale; // сумма со скидкой
    // convenienceList
    private List<ConvenienceDTO> convenienceList;
    private List<PlaceCalendarDTO> calendarList;
}
