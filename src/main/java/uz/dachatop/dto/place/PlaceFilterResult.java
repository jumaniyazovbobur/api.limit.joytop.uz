package uz.dachatop.dto.place;

import lombok.Getter;
import uz.dachatop.mapper.PlaceMapperDTO;

import java.util.List;

@Getter
public class PlaceFilterResult<T extends PlaceMapperDTO> {
    List<T> content;
    private Long totalCount;

    public PlaceFilterResult(List<T> content, Long totalCount) {
        this.content = content;
        this.totalCount = totalCount;
    }
}
