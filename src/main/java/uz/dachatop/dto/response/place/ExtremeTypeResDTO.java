package uz.dachatop.dto.response.place;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * @author 'Mukhtarov Sarvarbek' on 24.02.2023
 * @project api.dachatop
 * @contact @sarvargo
 */
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExtremeTypeResDTO {
    String id;
    String name;
    String nameUz;
    String nameRu;
    String nameEn;

    public ExtremeTypeResDTO(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public ExtremeTypeResDTO(String id, String nameUz, String nameRu, String nameEn) {
        this.id = id;
        this.nameUz = nameUz;
        this.nameRu = nameRu;
        this.nameEn = nameEn;
    }
}
