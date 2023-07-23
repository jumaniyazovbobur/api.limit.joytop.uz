package uz.dachatop.dto.region;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegionCreateDTO {
    private String nameUz;
    private String nameRu;
    private String nameEn;

    private Long countryId;
}
