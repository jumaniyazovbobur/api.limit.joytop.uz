package uz.dachatop.dto.place.dacha;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import uz.dachatop.dto.place.PlaceInfoDTO;
import uz.dachatop.enums.PlaceSubType;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DachaInfoDTO extends PlaceInfoDTO {
    private Long weekDayPrice; // обычная цена за сутки
    private Long weekendPrice; // цена по выходным за сутки
    private Double totalArea; // общее помощение
    private Integer singleBedRoomCount; // количество одноместных спальный
    private Integer doubleBedRoomCount; // количество двухместный спальный
    private String name;
    private String createdDate;
    private Long number;
    private PlaceSubType subType;
    private Long salePrice;

    @JsonProperty("tariff_result")
    private String tariffResult;

}
