package uz.dachatop.dto.place.camp;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import uz.dachatop.dto.place.PlaceFilterDTO;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CampFilterDTO  extends PlaceFilterDTO {
    // main information
    private String name;
    // convenienceList
    private List<Long> convenienceList;
}
