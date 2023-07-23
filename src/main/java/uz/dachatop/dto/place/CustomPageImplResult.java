package uz.dachatop.dto.place;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * @author 'Mukhtarov Sarvarbek' on 28.02.2023
 * @project api.dachatop
 * @contact @sarvargo
 */
@Getter
public class CustomPageImplResult<T> {
    private List<T> list;
    private Long count;

    public CustomPageImplResult(List<T> list, Long count) {
        this.list = list;
        this.count = count;
    }
}
