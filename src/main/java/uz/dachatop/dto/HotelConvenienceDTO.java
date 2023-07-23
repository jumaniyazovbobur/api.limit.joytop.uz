package uz.dachatop.dto;
// PROJECT NAME -> api.dachatop
// TIME -> 14:13
// DATE -> 07/02/23

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.dachatop.dto.convenience.ConvenienceDTO;
import uz.dachatop.dto.place.hotel.HotelDTO;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HotelConvenienceDTO {
    private Long hotelId;
    private Long convenienceId;
    private HotelDTO hotel;
    private ConvenienceDTO convenience;

}
