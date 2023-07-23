package uz.dachatop.mapper.hotel;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import uz.dachatop.enums.RoomType;
import uz.dachatop.mapper.PlaceMapperDTO;

import java.util.List;

@Getter
@Setter
public class HotelMapperDTO extends PlaceMapperDTO {
    private String name;
    private Long minPrice;
    private Double starCount;
    private String roomTypeStr; // SINGLE,DOUBLE,TWIN,....
    private String createdDate;
    @JsonProperty("tariff_result")
    private String tariffResult;
}
