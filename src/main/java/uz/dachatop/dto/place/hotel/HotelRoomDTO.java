package uz.dachatop.dto.place.hotel;

import lombok.Getter;
import lombok.Setter;
import uz.dachatop.dto.convenience.ConvenienceDTO;
import uz.dachatop.dto.attach.AttachDTO;
import uz.dachatop.enums.RoomType;

import java.util.List;

@Getter
@Setter
public class HotelRoomDTO {
    private String id;
    private RoomType roomType;
    private double totalArea; // общее помощение
    private Long dayPrice; // дневная цена
    private Long priceOnSale; // сумма со скидкой
    // convenienceList
    private List<ConvenienceDTO> convenienceList;
    // attach
    private List<AttachDTO> attachList;
}
