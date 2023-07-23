package uz.dachatop.service;
//PROJECT NAME --> api.dachatop
// YEAR --> 2023
// MONTH --> 04
// DAY --> 02
// TIME --> 22:34

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import uz.dachatop.dto.response.ApiResponse;
import uz.dachatop.dto.subscription.SubscriptionDTO;
import uz.dachatop.dto.subscription.SubscriptionFilterRequestDTO;
import uz.dachatop.dto.subscription.SubscriptionFilterResult;
import uz.dachatop.dto.tariff.SubscriptionTariffCreateDTO;
import uz.dachatop.dto.tariff.SubscriptionTariffDTO;
import uz.dachatop.dto.transaction.SubscriptionTransactionDTO;
import uz.dachatop.dto.transaction.SubscriptionTransactionFilterRequestDTO;
import uz.dachatop.dto.transaction.SubscriptionTransactionFilterResult;
import uz.dachatop.entity.subscriotion.SubscriptionTariffEntity;
import uz.dachatop.entity.subscriotion.SubscriptionTransactionEntity;
import uz.dachatop.enums.PlaceType;
import uz.dachatop.enums.SubscriptionTransactionStatus;
import uz.dachatop.exp.ItemNotFoundException;
import uz.dachatop.mapper.SubscriptionMapper;
import uz.dachatop.mapper.SubscriptionTransactionMapper;
import uz.dachatop.repository.SubscriptionTariffRepository;
import uz.dachatop.repository.SubscriptionTransactionRepository;
import uz.dachatop.repository.filter.SubscriptionFilterRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionTransactionService {
    private final SubscriptionTransactionRepository subscriptionTariffRepository;
    private final SubscriptionFilterRepository filterRepository;


    public void saveTransaction(String tariffId, Integer tariffDays, Long tariffPrice, String placeId, PlaceType placeType) {
        SubscriptionTransactionEntity entity = new SubscriptionTransactionEntity();
        entity.setPrice(tariffPrice);
        entity.setDays(tariffDays);
        entity.setPlaceId(placeId);
        entity.setTariffId(tariffId);
        entity.setPlaceType(placeType);
        entity.setStatus(SubscriptionTransactionStatus.SUCCESS);
        subscriptionTariffRepository.save(entity);
    }


    public ApiResponse<Page<SubscriptionTransactionDTO>> filter(SubscriptionTransactionFilterRequestDTO filter, int page, int size) {
        SubscriptionTransactionFilterResult<SubscriptionTransactionMapper> filterResult = filterRepository.transactionFilter(filter, page * size, size);
        List<SubscriptionTransactionDTO> dtoList = filterResult.getContent().stream().map(subscriptionMapper -> toDTO(subscriptionMapper)).collect(Collectors.toList());
        Page<SubscriptionTransactionDTO> pageObj = new PageImpl<>(dtoList, PageRequest.of(page, size), filterResult.getTotalCount());
        return ApiResponse.ok(pageObj);
    }

    private SubscriptionTransactionDTO toDTO(SubscriptionTransactionMapper entity) {
        SubscriptionTransactionDTO dto = new SubscriptionTransactionDTO();
        dto.setId(entity.getId());
        dto.setPlaceType(entity.getPlace_type());
        dto.setPlaceId(entity.getPlace_id());
        dto.setTariffId(entity.getTariff_id());
        dto.setTariffPlan(entity.getTariff_plan());
        dto.setTariffPlanPrice(entity.getTariff_plan_price());
        dto.setPrice(entity.getPrice());
        dto.setDays(entity.getDays());
        dto.setStatus(entity.getStatus());
        return dto;
    }

}
