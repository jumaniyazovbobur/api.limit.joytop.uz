package uz.dachatop.dto.place;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import uz.dachatop.dto.district.DistrictDTO;
import uz.dachatop.dto.profile.ProfileDTO;
import uz.dachatop.dto.region.RegionDTO;
import uz.dachatop.dto.attach.AttachDTO;
import uz.dachatop.dto.territory.TerritoryDTO;
import uz.dachatop.enums.PlaceType;
import uz.dachatop.enums.GlobalStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaceDTO {
    private String id;
    // attach
    private List<AttachDTO> attachList;
    //
    private String videoUrl;
    // Address
    private Long regionId;
    private RegionDTO region;
    private Long territoryId;
    private TerritoryDTO territory;
    private DistrictDTO district;
    private Long districtId;
    // location
    private Double latitude;
    private Double longitude;
    // main information
    private PlaceType type;
    //Rules
    private boolean smoking;
    private boolean alcohol;
    private boolean pets;
    private boolean availableOnlyFamily;
    private boolean loudlyMusic;
    private boolean party;
    // owner
    private String profileId;
    private ProfileDTO profile;
    // description
    private String description;
    // contact
    private String contact;
    // general
    private GlobalStatus status;
    private LocalDateTime createdDate;
    @JsonProperty("tariff_result")
    private String tariffResult;


    // card
//    private String profileCardPan;
//    // image
//    private List<String> attachList;
//    // convenience - Qulayliklar
//    private List<Long> convenienceList;
//    // calendar list
//    private List<ApartmentCalendarDTO> calendarList;
}
