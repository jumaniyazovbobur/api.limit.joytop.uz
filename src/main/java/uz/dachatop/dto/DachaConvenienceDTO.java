package uz.dachatop.dto;
// PROJECT NAME -> api.dachatop
// TIME -> 14:13
// DATE -> 07/02/23

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.dachatop.dto.convenience.ConvenienceDTO;
import uz.dachatop.dto.place.dacha.DachaDTO;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DachaConvenienceDTO {
    private Long dachaId;
    private Long convenienceId;
    private DachaDTO dacha;
    private ConvenienceDTO convenience;

}
