package uz.dachatop.dto.country;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class CountryResponseDTO extends CountryRequestDTO {

    private Long id;
    private String key;
    private Boolean visible;
    private LocalDateTime createdDate;
    private String name;

    public CountryResponseDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
