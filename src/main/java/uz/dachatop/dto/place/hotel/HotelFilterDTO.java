package uz.dachatop.dto.place.hotel;

import lombok.Getter;
import lombok.Setter;
import uz.dachatop.dto.place.PlaceFilterDTO;
import uz.dachatop.enums.RoomType;

import java.util.List;

@Getter
@Setter
public class HotelFilterDTO extends PlaceFilterDTO {
    private String name;
    private Long minPrice;
    private Double starCount;
    // filter by room
    private RoomType roomType;
    private List<Long> convenienceList; // удобства
}
