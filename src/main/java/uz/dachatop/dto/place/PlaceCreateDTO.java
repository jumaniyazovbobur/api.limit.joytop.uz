package uz.dachatop.dto.place;

import lombok.Getter;
import lombok.Setter;
import uz.dachatop.dto.attach.AttachDTO;
import uz.dachatop.enums.PlaceType;
import uz.dachatop.enums.GlobalStatus;

import java.util.List;

@Getter
@Setter
public class PlaceCreateDTO {
    // attach
    private List<AttachDTO> attachList;
    //
    private String videoUrl;
    // Address
    private Long regionId;
    private Long districtId;
    private Long territoryId;
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
    // description
    private String description;
    // contact
    private String contact;
    // general
    private GlobalStatus status;
}
