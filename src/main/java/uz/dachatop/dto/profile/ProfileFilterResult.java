package uz.dachatop.dto.profile;

import lombok.Getter;
import lombok.Setter;
import uz.dachatop.mapper.ProfileCustomMapper;
import uz.dachatop.mapper.SubscriptionMapper;

import java.util.List;
@Getter
@Setter
public class ProfileFilterResult<T extends ProfileCustomMapper> {

        List<T> content;
        private Long totalCount;

        public ProfileFilterResult(List<T> content, Long totalCount) {
            this.content = content;
            this.totalCount = totalCount;
        }
}
