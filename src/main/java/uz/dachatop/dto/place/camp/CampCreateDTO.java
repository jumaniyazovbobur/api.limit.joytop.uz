package uz.dachatop.dto.place.camp;

import lombok.Getter;
import lombok.Setter;
import uz.dachatop.dto.convenience.ConvenienceDTO;
import uz.dachatop.dto.place.PlaceCreateDTO;

import java.util.List;

@Getter
@Setter
public class CampCreateDTO extends PlaceCreateDTO {
    // main information
    private String name;
    private Long price; // обычная
    private Long priceOnSale; // сумма со скидкой
    // convenienceList
    private List<ConvenienceDTO> convenienceList;
}
