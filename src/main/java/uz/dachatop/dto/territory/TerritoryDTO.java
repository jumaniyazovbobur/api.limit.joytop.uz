package uz.dachatop.dto.territory;
//PROJECT NAME --> api.dachatop
// YEAR --> 2023
// MONTH --> 03
// DAY --> 28
// TIME --> 10:13

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import uz.dachatop.dto.district.DistrictDTO;
import uz.dachatop.entity.DistrictEntity;
import uz.dachatop.entity.TerritoryEntity;
import uz.dachatop.enums.AppLanguage;
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TerritoryDTO {

    private Long id;
    private Long districtId;
    private String nameUz;
    private String nameRu;
    private String nameEn;
    private Boolean visible;
    private String county;
    private String name;


    public TerritoryDTO() {
    }

    public TerritoryDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static TerritoryDTO toDto(TerritoryEntity districtEntity) {
        TerritoryDTO dto = new TerritoryDTO();
//        dto.setRegionId(districtEntity.getRegionEntityId().getId());
        dto.setNameUz(districtEntity.getNameUz());
        dto.setNameRu(districtEntity.getNameRu());
        dto.setNameEn(districtEntity.getNameEn());
        return dto;
    }

    public static TerritoryDTO toInfoDto(Long territoryId, String territoryNameUz, String territoryNameEn, String territoryNameRu, AppLanguage lang) {
        TerritoryDTO dto = new TerritoryDTO();
        dto.setId(territoryId);
        switch (lang) {
            case en -> dto.setName(territoryNameEn);
            case ru -> dto.setName(territoryNameRu);
            default -> dto.setName(territoryNameUz);
        }
        return dto;
    }

    public static TerritoryDTO toDTO(TerritoryEntity entity, AppLanguage lang) {
        TerritoryDTO dto = new TerritoryDTO();
        dto.setId(entity.getId());
        switch (lang) {
            case en -> dto.setName(entity.getNameEn());
            case ru -> dto.setName(entity.getNameRu());
            default -> dto.setName(entity.getNameUz());
        }
        return dto;
    }

}
