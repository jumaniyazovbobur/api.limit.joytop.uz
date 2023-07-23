package uz.dachatop.dto.request;

import jakarta.validation.constraints.NotBlank;
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
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExtremeTypeReqDTO {

    @NotBlank(message = "Name not be empty!")
    String nameUz;

    @NotBlank(message = "Name not be empty!")
    String nameEn;

    @NotBlank(message = "Name not be empty!")
    String nameRu;
}

