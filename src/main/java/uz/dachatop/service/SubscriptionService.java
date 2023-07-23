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
import uz.dachatop.config.EntityDetails;
import uz.dachatop.dto.response.ApiResponse;
import uz.dachatop.dto.subscription.*;
import uz.dachatop.entity.subscriotion.SubscriptionEntity;
import uz.dachatop.entity.subscriotion.SubscriptionTariffEntity;
import uz.dachatop.enums.SubscriptionStatus;
import uz.dachatop.exp.ItemNotFoundException;
import uz.dachatop.mapper.SubscriptionMapper;
import uz.dachatop.repository.SubscriptionRepository;
import uz.dachatop.repository.filter.SubscriptionFilterRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionFilterRepository filterRepository;
    private final SubscriptionTariffService subscriptionTariffService;

    private final SubscriptionTransactionService subscriptionTransactionService;

    public ApiResponse<SubscriptionDTO> create(SubscriptionCreateDTO dto) {
        String profileId = EntityDetails.getCurrentProfileId();
        SubscriptionEntity entity = null;

        int purchaseTimes = dto.getPurchaseTimes() == null ? 1 : dto.getPurchaseTimes();
        purchaseTimes = dto.getPurchaseTimes() == 0 ? 1 : dto.getPurchaseTimes();

        SubscriptionTariffEntity tariff = subscriptionTariffService.get(dto.getTariffId());
        int purchaseDays = purchaseTimes * tariff.getDays();

        LocalDateTime endDate = LocalDateTime.now().plusDays(purchaseDays);

        Optional<SubscriptionEntity> oldSubscriptionOptional = subscriptionRepository
                .findByPlaceIdAndTariffIdAndStatus(dto.getPlaceId(), dto.getTariffId(), SubscriptionStatus.ACTIVE);

        if (oldSubscriptionOptional.isPresent()) {  // old subscription with tariff and place already exists so needs to add days on this.
            entity = oldSubscriptionOptional.get();
            entity.setPrtId(profileId); // last prtId
            entity.setDays(entity.getDays() + purchaseDays);
            entity.setEndDate(endDate);

        } else { // no subscription so crate new one.
            entity = new SubscriptionEntity();
            entity.setPlaceId(dto.getPlaceId());
            entity.setPlaceType(dto.getPlaceType());
            entity.setTariffId(dto.getTariffId());
            entity.setPrtId(profileId); // prtId
            entity.setStartDate(LocalDateTime.now());
            entity.setStatus(SubscriptionStatus.ACTIVE);
            entity.setDays(purchaseDays);
            entity.setEndDate(endDate);
        }
        subscriptionRepository.save(entity); // save subscription
        for (int i = 0; i < purchaseTimes; i++) { // save subscription Transaction each
            subscriptionTransactionService.saveTransaction(tariff.getId(),
                    tariff.getDays(), tariff.getPrice(), dto.getPlaceId(), dto.getPlaceType());
        }
        return new ApiResponse<>(200, false, toDTO(entity));
    }


    public ApiResponse<String> changeStatus(SubscriptionChangeStatusDTO dto) {

        int i = subscriptionRepository.changeStatus(dto.getStatus(), dto.getId());
        return new ApiResponse<String>(200, false, "Subscription successfully " + dto.getStatus());
    }

    public ApiResponse<Page<SubscriptionDTO>> filter(SubscriptionFilterRequestDTO filter, int page, int size) {
        SubscriptionFilterResult<SubscriptionMapper> filterResult = filterRepository.filter(filter, page * size, size);
        List<SubscriptionDTO> dtoList = filterResult.getContent().stream().map(apartmentMapper -> toDTO(apartmentMapper)).collect(Collectors.toList());
        Page<SubscriptionDTO> pageObj = new PageImpl<SubscriptionDTO>(dtoList, PageRequest.of(page, size), filterResult.getTotalCount());
        return ApiResponse.ok(pageObj);
    }


    public SubscriptionEntity get(String id) {
        return subscriptionRepository.findById(id).orElseThrow(() -> {
            throw new ItemNotFoundException("Subscription not found by this id " + id);
        });
    }


    private SubscriptionDTO toDTO(SubscriptionEntity entity) {
        SubscriptionDTO dto = new SubscriptionDTO();
        dto.setId(entity.getId());
        dto.setStartDate(String.valueOf(entity.getStartDate()));
        dto.setEndDate(String.valueOf(entity.getEndDate()));
        dto.setPlaceType(entity.getPlaceType());
        dto.setPlaceId(entity.getPlaceId());
        dto.setPrtId(entity.getPrtId());
        dto.setTariffId(entity.getTariffId());
        dto.setPrice(entity.getPrice());
        dto.setDays(entity.getDays());
        return dto;
    }

    private SubscriptionDTO toDTO(SubscriptionMapper entity) {
        SubscriptionDTO dto = new SubscriptionDTO();
        dto.setId(entity.getSub_id());
        dto.setStartDate(entity.getCreated_date());
        dto.setEndDate(entity.getEnd_date());
        dto.setPlaceType(entity.getPlace_type());
        dto.setPlaceId(entity.getPlace_id());
        dto.setPrtId(entity.getProfile_id());
        dto.setTariffId(entity.getTariff_id());
        dto.setPrice(entity.getSub_price());
        dto.setDays(entity.getSub_days());
        dto.setStatus(entity.getSub_status());
        return dto;
    }

    public void changeSubscriptionStatus() {
        subscriptionRepository.updateSubscriptionStatus();
    }
}
