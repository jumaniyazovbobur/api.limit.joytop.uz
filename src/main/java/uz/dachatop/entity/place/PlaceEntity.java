package uz.dachatop.entity.place;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import uz.dachatop.entity.DistrictEntity;
import uz.dachatop.entity.ProfileEntity;
import uz.dachatop.entity.RegionEntity;
import uz.dachatop.entity.TerritoryEntity;
import uz.dachatop.entity.base.BaseStringIdEntity;
import uz.dachatop.enums.PlaceType;
import uz.dachatop.enums.GlobalStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public class PlaceEntity extends BaseStringIdEntity {
    // video link
    @Column(name = "video_url")
    private String videoUrl;
    // Address
    @Column(name = "region_id")
    private Long regionId;
    @ManyToOne
    @JoinColumn(name = "region_id", insertable = false, updatable = false)
    private RegionEntity region;
    @Column(name = "territory_id")
    private Long territoryId;
    @ManyToOne
    @JoinColumn(name = "territory_id", insertable = false, updatable = false)
    private TerritoryEntity territory;
    @Column(name = "district_id")
    private Long districtId;
    @ManyToOne
    @JoinColumn(name = "district_id", insertable = false, updatable = false)
    private DistrictEntity district;
    // location
    @Column(name = "latitude ", nullable = true)
    private Double latitude;
    @Column(name = "longitude", nullable = true)
    private Double longitude;
    // main information
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private PlaceType type;
    //Rules
    @Column(name = "smoking")
    private boolean smoking;
    @Column(name = "alcohol")
    private boolean alcohol;
    @Column(name = "pets")
    private boolean pets;
    @Column(name = "available_only_family")
    private boolean availableOnlyFamily;
    @Column(name = "loudly_music")
    private boolean loudlyMusic;
    @Column(name = "party")
    private boolean party;
    // owner
    @Column(name = "profile_id")
    private String profileId;
    @ManyToOne
    @JoinColumn(name = "profile_id", insertable = false, updatable = false)
    private ProfileEntity profile;
    // description
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    // contact
    @Column(name = "contact")
    private String contact;
    // general
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private GlobalStatus status = GlobalStatus.ACTIVE;
    @Column(name = "deleted_id")
    private String deletedId; // deleted profile id;
    @Column(name = "deleted_date")
    private LocalDateTime deletedDate;
    // images

    /* @Column(name = "street")
     private String street; // улица
     @Column(name = "house")
     private String house; // дом
     @Column(name = "apartment_num")
     private String apartmentNum; // квт*/
}
