package uz.dachatop.dto.place.hotel;

import lombok.Getter;
import lombok.Setter;
import uz.dachatop.dto.convenience.ConvenienceDTO;
import uz.dachatop.dto.place.PlaceCreateDTO;

import java.util.List;

@Getter
@Setter
public class HotelCreateDTO extends PlaceCreateDTO {
    private String name;
    private Double starCount;
    private Long minPrice;
    private List<HotelRoomDTO> roomList;
    private List<ConvenienceDTO> convenienceList;
}
