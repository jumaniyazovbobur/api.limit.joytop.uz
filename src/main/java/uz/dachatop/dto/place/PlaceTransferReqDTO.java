package uz.dachatop.dto.place;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.dachatop.enums.PlaceType;

/**
 * @author 'Mukhtarov Sarvarbek' on 18.05.2023
 * @project api.dachatop
 * @contact @sarvargo
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlaceTransferReqDTO {
    @NotBlank(message = "Choose place!")
    private String placeId;

    @NotBlank(message = "Enter profile phone!")
    private String toProfile; // phone number
    @NotNull(message = "Select place type!")
    private PlaceType type;
}
