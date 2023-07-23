package uz.dachatop.mapper;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import uz.dachatop.enums.PlaceType;
import uz.dachatop.enums.GlobalStatus;

@Getter
@Setter
public class PlaceMapperDTO {
    private String id;
    private Long regionId;
    private String regionName;
    private Long districtId;
    private String districtName;
    private Double latitude;
    private Double longitude;
    private String mainAttachId;
    private PlaceType placeType;
    private GlobalStatus status;
    private String createdDate;
    private Long salePrice;
    @JsonProperty("tariff_result")
    private String tariffResult;
}
