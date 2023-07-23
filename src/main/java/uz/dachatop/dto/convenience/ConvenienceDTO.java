package uz.dachatop.dto.convenience;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import uz.dachatop.entity.ConvenienceEntity;
import uz.dachatop.enums.AppLanguage;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConvenienceDTO {
    private Long id;
    private String name;
    private String nameUz;
    private String nameRu;
    private String nameEn;

    public ConvenienceDTO() {

    }

    public ConvenienceDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static ConvenienceDTO toDto(ConvenienceEntity convenienceEntity) {
        ConvenienceDTO dto = new ConvenienceDTO();
        dto.setId(convenienceEntity.getId());
        dto.setNameEn(convenienceEntity.getNameEn());
        dto.setNameRu(convenienceEntity.getNameRu());
        dto.setNameUz(convenienceEntity.getNameUz());
        return dto;
    }

    public static ConvenienceDTO getByLanguage(ConvenienceEntity convenienceEntity, AppLanguage lang) {
        ConvenienceDTO con = new ConvenienceDTO();
        con.setId(convenienceEntity.getId());
        switch (lang) {
            case en -> {
                con.setName(convenienceEntity.getNameEn());
            }
            case ru -> {
                con.setName(convenienceEntity.getNameRu());
            }
            default -> {
                con.setName(convenienceEntity.getNameUz());
            }
        }
        return con;
    }


}
