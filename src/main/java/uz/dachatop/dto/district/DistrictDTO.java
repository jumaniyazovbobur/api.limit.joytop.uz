package uz.dachatop.dto.district;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import uz.dachatop.entity.DistrictEntity;
import uz.dachatop.enums.AppLanguage;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DistrictDTO {
    private Long id;
    private Long regionId;
    private String nameUz;
    private String nameRu;
    private String nameEn;
    private Boolean visible;
    private String county;
    private String name;


    public DistrictDTO() {
    }

    public DistrictDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static DistrictDTO toDto(DistrictEntity districtEntity) {
        DistrictDTO dto = new DistrictDTO();
//        dto.setRegionId(districtEntity.getRegionEntityId().getId());
        dto.setNameUz(districtEntity.getNameUz());
        dto.setNameRu(districtEntity.getNameRu());
        dto.setNameEn(districtEntity.getNameEn());
        return dto;
    }

    public static DistrictDTO toInfoDto(Long districtId, String districtNameUz, String districtNameEn, String districtNameRu, AppLanguage lang) {
        DistrictDTO dto = new DistrictDTO();
        dto.setId(districtId);
        switch (lang) {
            case en -> dto.setName(districtNameEn);
            case ru -> dto.setName(districtNameRu);
            default -> dto.setName(districtNameUz);
        }
        return dto;
    }

    public static DistrictDTO toDTO(DistrictEntity entity, AppLanguage lang) {
        DistrictDTO dto = new DistrictDTO();
        dto.setId(entity.getId());
        switch (lang) {
            case en -> dto.setName(entity.getNameEn());
            case ru -> dto.setName(entity.getNameRu());
            default -> dto.setName(entity.getNameUz());
        }
        return dto;
    }

}
