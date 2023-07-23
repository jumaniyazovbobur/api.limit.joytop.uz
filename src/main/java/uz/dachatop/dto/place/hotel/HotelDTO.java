package uz.dachatop.dto.place.hotel;

import lombok.Getter;
import lombok.Setter;
import uz.dachatop.dto.convenience.ConvenienceDTO;
import uz.dachatop.dto.place.PlaceDTO;

import java.util.List;

@Getter
@Setter
public class HotelDTO extends PlaceDTO {
    private String name;
    private Long minPrice;
    private Double starCount;
    private List<HotelRoomDTO> roomList;
    private List<ConvenienceDTO> convenienceList;
}
