package uz.dachatop.dto.transaction;

import lombok.Getter;
import lombok.Setter;
import uz.dachatop.mapper.SubscriptionMapper;
import uz.dachatop.mapper.SubscriptionTransactionMapper;

import java.util.List;
@Getter
@Setter
public class SubscriptionTransactionFilterResult<T extends SubscriptionTransactionMapper> {
    List<T> content;
    private Long totalCount;

    public SubscriptionTransactionFilterResult(List<T> content, Long totalCount) {
        this.content = content;
        this.totalCount = totalCount;
    }
}
