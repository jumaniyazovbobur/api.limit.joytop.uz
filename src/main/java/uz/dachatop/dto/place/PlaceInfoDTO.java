package uz.dachatop.dto.place;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import uz.dachatop.dto.district.DistrictDTO;
import uz.dachatop.dto.region.RegionDTO;
import uz.dachatop.dto.attach.AttachDTO;
import uz.dachatop.dto.territory.TerritoryDTO;
import uz.dachatop.enums.GlobalStatus;
import uz.dachatop.enums.PlaceType;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaceInfoDTO {
    private String id;
    private RegionDTO region;
    private DistrictDTO district;
    private TerritoryDTO territory;
    private Double latitude;
    private Double longitude;
    private AttachDTO mainAttach;
    private PlaceType placeType;
    private GlobalStatus status;
    private String createdDate;
    private Long number;
    @JsonProperty("tariff_result")
    private String tariffResult;
}
