package uz.dachatop.dto.country;
// PROJECT NAME -> api.dachatop
// TIME -> 16:07
// MONTH -> 08
// DAY -> 06

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CountryRequestDTO {

    private Long Id;
    private String nameRu;
    private String nameEn;
    private String nameUz;
}
