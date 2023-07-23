package uz.dachatop.dto.subscription;
//PROJECT NAME --> api.dachatop
// YEAR --> 2023
// MONTH --> 04
// DAY --> 11
// TIME --> 9:27

import lombok.Getter;
import lombok.Setter;
import uz.dachatop.mapper.PlaceMapperDTO;
import uz.dachatop.mapper.SubscriptionMapper;

import java.util.List;
@Getter
@Setter
public class SubscriptionFilterResult<T extends SubscriptionMapper> {
    List<T> content;
    private Long totalCount;

    public SubscriptionFilterResult(List<T> content, Long totalCount) {
        this.content = content;
        this.totalCount = totalCount;
    }
}
