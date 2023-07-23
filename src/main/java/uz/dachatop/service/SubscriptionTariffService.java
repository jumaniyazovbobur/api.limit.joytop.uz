package uz.dachatop.service;
//PROJECT NAME --> api.dachatop
// YEAR --> 2023
// MONTH --> 04
// DAY --> 02
// TIME --> 22:34

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import uz.dachatop.dto.region.RegionDTO;
import uz.dachatop.dto.response.ApiResponse;
import uz.dachatop.dto.subscription.SubscriptionCreateDTO;
import uz.dachatop.dto.tariff.SubscriptionTariffCreateDTO;
import uz.dachatop.dto.tariff.SubscriptionTariffDTO;
import uz.dachatop.entity.RegionEntity;
import uz.dachatop.entity.subscriotion.SubscriptionTariffEntity;
import uz.dachatop.enums.PlaceType;
import uz.dachatop.exp.ItemNotFoundException;
import uz.dachatop.mapper.subscription.PlaceSubscriptionMapper;
import uz.dachatop.repository.SubscriptionRepository;
import uz.dachatop.repository.SubscriptionTariffRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionTariffService {
    private final SubscriptionTariffRepository subscriptionTariffRepository;
    private final SubscriptionRepository subscriptionRepository;

    public ApiResponse<SubscriptionTariffDTO> create(SubscriptionTariffCreateDTO dto) {
        SubscriptionTariffEntity entity = new SubscriptionTariffEntity();
        entity.setNameUz(dto.getNameUz());
        entity.setNameRu(dto.getNameRu());
        entity.setColor(dto.getColor());
        entity.setNameEn(dto.getNameEn());
        entity.setPrice(dto.getPrice());
        entity.setOrderNumber(dto.getOrderNumber());
        entity.setDays(dto.getDays());

        subscriptionTariffRepository.save(entity);

        return new ApiResponse<>(200, false, toDTO(entity));
    }

    public ApiResponse<SubscriptionTariffDTO> getOne(String id) {
        return new ApiResponse<>(200, false, toDTO(subscriptionTariffRepository.findById(id).orElseThrow(
                () -> new ItemNotFoundException("Subscription Tariff not found: " + id))));
    }


    public ApiResponse<SubscriptionTariffDTO> update(String id, SubscriptionTariffCreateDTO dto) {
        SubscriptionTariffEntity entity = subscriptionTariffRepository.findByIdAndVisibleTrue(id).orElseThrow(() ->
                new ItemNotFoundException("Subscription Tariff not found: " + id));

        entity.setNameUz(dto.getNameUz());
        entity.setNameRu(dto.getNameRu());
        entity.setNameEn(dto.getNameEn());
        entity.setPrice(dto.getPrice());
        entity.setColor(dto.getColor());
        entity.setOrderNumber(dto.getOrderNumber());
        entity.setDays(dto.getDays());

        subscriptionTariffRepository.save(entity);

        return new ApiResponse<>(200, false, toDTO(entity));
    }

    public ApiResponse<Boolean> delete(String id) {
        int i = subscriptionTariffRepository.deleteStatus(false, id);
        return new ApiResponse<Boolean>(200, false, i > 0);
    }

    public ApiResponse<List<SubscriptionTariffDTO>> getAll() {
        List<SubscriptionTariffDTO> dtoList = new ArrayList<>();

        subscriptionTariffRepository.findAllByVisibleTrue().forEach(entity -> {
            dtoList.add(toDTO(entity));
        });
        return new ApiResponse<>(200, false, dtoList);
    }


    public SubscriptionTariffEntity get(String id) {
        return subscriptionTariffRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Subscription Tariff not found by this id " + id));
    }
    public String getTariffResult(String id, PlaceType type, String lang) {
        PlaceSubscriptionMapper mapper = subscriptionRepository
                .findByPlaceSub(id, type,lang)
                .orElse(null);
        return mapper == null ? null : mapper.getTariffResult();
    }

    private SubscriptionTariffDTO toDTO(SubscriptionTariffEntity entity) {
        SubscriptionTariffDTO dto = new SubscriptionTariffDTO();
        dto.setId(entity.getId());
        dto.setColor(entity.getColor());
        dto.setNameUz(entity.getNameUz());
        dto.setNameRu(entity.getNameRu());
        dto.setNameEn(entity.getNameEn());
        dto.setPrice(entity.getPrice());
        dto.setOrderNumber(entity.getOrderNumber());
        dto.setDays(entity.getDays());
        return dto;
    }


}
