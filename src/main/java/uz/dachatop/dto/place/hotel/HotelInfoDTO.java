package uz.dachatop.dto.place.hotel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import uz.dachatop.dto.place.PlaceInfoDTO;
import uz.dachatop.enums.RoomType;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)

public class HotelInfoDTO extends PlaceInfoDTO {
    private String name;
    private Double starCount;
    private Long minPrice;

    private List<RoomType> roomTypeList;
    @JsonProperty("tariff_result")
    private String tariffResult;
}
