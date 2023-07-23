package uz.dachatop.dto.region;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import uz.dachatop.entity.RegionEntity;
import uz.dachatop.enums.AppLanguage;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegionDTO {
    private Long id;
    private String nameUz;
    private String nameRu;
    private String nameEn;

    private String name;

    public RegionDTO() {
    }

    public RegionDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static RegionDTO toDto(RegionEntity regionEntity) {
        RegionDTO dto = new RegionDTO();
        dto.setId(regionEntity.getId());
        dto.setNameUz(regionEntity.getNameUz());
        dto.setNameRu(regionEntity.getNameRu());
        dto.setNameEn(regionEntity.getNameEn());
        return dto;
    }

    public static RegionDTO toInfoDto(Long id, String nameUz, String nameEn, String nameRu, AppLanguage language) {
        RegionDTO dto = new RegionDTO();
        dto.setId(id);
        switch (language) {
            case en -> dto.setName(nameEn);
            case ru -> dto.setName(nameRu);
            default -> dto.setName(nameUz);
        }
        return dto;
    }

    public static RegionDTO toDto(RegionEntity regionEntity, AppLanguage language) {
        RegionDTO dto = new RegionDTO();
        dto.setId(regionEntity.getId());
        switch (language) {
            case en -> dto.setName(regionEntity.getNameEn());
            case ru -> dto.setName(regionEntity.getNameRu());
            default -> dto.setName(regionEntity.getNameUz());
        }
        return dto;
    }
}
