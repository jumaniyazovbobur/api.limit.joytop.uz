package uz.dachatop.mapper.dacha;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import uz.dachatop.enums.PlaceSubType;
import uz.dachatop.mapper.PlaceMapperDTO;

@Getter
@Setter
public class DachaMapperDTO extends PlaceMapperDTO {
    private Long weekDayPrice; // обычная цена за сутки
    private Long weekendPrice; // цена по выходным за сутки
    private Double totalArea; // общее помощение
    private Integer singleBedRoomCount; // количество одноместных спальный
    private Integer doubleBedRoomCount; // количество двухместный спальный
    private String dachaName;
    private String createdDate;
    private Long number;
    private PlaceSubType subType;

    @JsonProperty("tariff_result")
    private String tariffResult;
}
