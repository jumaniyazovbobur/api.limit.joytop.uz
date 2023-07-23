package uz.dachatop.service.place.extreme;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import uz.dachatop.config.EntityDetails;
import uz.dachatop.dto.place.CustomPageImplResult;
import uz.dachatop.dto.place.PlaceFilterResult;
import uz.dachatop.dto.place.apartment.ApartmentInfoDTO;
import uz.dachatop.dto.place.extreme.ExtremeCreateDTO;
import uz.dachatop.dto.place.extreme.ExtremeFilterDTO;
import uz.dachatop.dto.place.extreme.ExtremeInfoDTO;
import uz.dachatop.dto.place.extreme.ExtremeReqDTO;
import uz.dachatop.dto.profile.ProfileRoleDTO;
import uz.dachatop.dto.response.ApiResponse;
import uz.dachatop.entity.place.apartment.ApartmentEntity;
import uz.dachatop.entity.place.extreme.ExtremeEntity;
import uz.dachatop.enums.AppLanguage;
import uz.dachatop.enums.GlobalStatus;
import uz.dachatop.enums.ProfileRole;
import uz.dachatop.exp.ItemNotFoundException;
import uz.dachatop.mapper.extreme.ExtremeMapperDTO;
import uz.dachatop.repository.place.extreme.ExtremeCustomFilter;
import uz.dachatop.repository.place.extreme.ExtremeRepository;
import uz.dachatop.service.place.ExtremeTypeService;
import uz.dachatop.service.place.PlaceAttachService;
import uz.dachatop.service.place.PlaceService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExtremeService {
    private final ExtremeRepository extremeRepository;

    private final ExtremeTypeService typeService;
    private final PlaceAttachService placeAttachService;
    private final PlaceService placeService;
    private final ExtremeCustomFilter extremeCustomFilter;


    public ApiResponse<String> create(ExtremeCreateDTO dto) {
        ExtremeEntity extremeEntity = PlaceService.toEntity(new ExtremeEntity(), dto);
        toEntity(dto, extremeEntity);
        extremeEntity.setProfileId(EntityDetails.getCurrentProfileId());
        extremeRepository.save(extremeEntity);
        // add image
        placeAttachService.mergePlaceAttach(extremeEntity.getId(), dto.getAttachList());
        return ApiResponse.ok();
    }


    private void toEntity(ExtremeCreateDTO dto, ExtremeEntity extreme) {
        // main information
        extreme.setWeekendPrice(dto.getWeekendPrice());
        extreme.setWeekDayPrice(dto.getWeekDayPrice());
        typeService.findById(dto.getExtremeTypeId());
        extreme.setExtremeTypeId(dto.getExtremeTypeId());

    }

    public ExtremeEntity get(String id) {
        return extremeRepository.findByIdAndVisibleIsTrue(id).orElseThrow(() -> {
            throw new ItemNotFoundException("Apartment Not Found");
        });
    }

    public ApiResponse<String> update(String extremeId, ExtremeCreateDTO dto) {
        ExtremeEntity entity = get(extremeId);
        if (!entity.getProfileId().equals(EntityDetails.getCurrentProfileId())) {
            return ApiResponse.bad("You are not owner. Mazgi.");
        }
        PlaceService.toEntity(entity, dto);
        toEntity(dto, entity);
        extremeRepository.save(entity);
        // add image
        placeAttachService.mergePlaceAttach(entity.getId(), dto.getAttachList());
        return ApiResponse.ok();
    }

    public ApiResponse<String> deleteExtreme(String id) {
        ExtremeEntity entity = get(id);
        String profileId = EntityDetails.getCurrentProfileId();
        List<ProfileRole> profileRoles = Objects.requireNonNull(EntityDetails.getAdminEntity()).getRoleList()
                .stream().map(ProfileRoleDTO::getRole).toList();
        if (!entity.getProfileId().equals(profileId)
                && !profileRoles.contains(ProfileRole.ROLE_ADMIN)) {
            return ApiResponse.bad("You are not owner. Mazgi.");
        }
        extremeRepository.deleteExtreme(id, profileId, LocalDateTime.now());
        return null;
    }

    public ApiResponse<?> changeStatus(String extremeId, GlobalStatus status) {
        ExtremeEntity entity = get(extremeId);
        String profileId = EntityDetails.getCurrentProfileId();
        extremeRepository.changeStatus(extremeId, profileId, status, LocalDateTime.now());
        return ApiResponse.ok();
    }

    public ApiResponse<ExtremeReqDTO> getExtremeById(String extremeId, AppLanguage language) {
        Optional<ExtremeEntity> optional = extremeRepository.findByIdAndVisibleTrue(extremeId);
        if (optional.isEmpty()) {
            return ApiResponse.bad("Item not found");
        }
        ExtremeReqDTO dto = toDetailDTO(optional.get(), language);
        return ApiResponse.ok(dto);
    }

    private ExtremeReqDTO toDetailDTO(ExtremeEntity entity, AppLanguage language) {
        ExtremeReqDTO extremeReqDTO = placeService.toDetailDTO(new ExtremeReqDTO(), entity, language);
        extremeReqDTO.setExtremeType(typeService.findById(entity.getExtremeTypeId(), language));
        extremeReqDTO.setWeekendPrice(entity.getWeekendPrice());
        extremeReqDTO.setWeekDayPrice(entity.getWeekDayPrice());
        return extremeReqDTO;
    }

    public ApiResponse<Page<ExtremeInfoDTO>> filterAsClient(ExtremeFilterDTO filter, int page, int size, AppLanguage lang) {
        PlaceFilterResult<ExtremeMapperDTO> filterResult = extremeCustomFilter.filter(filter, lang, page * size, size);
        List<ExtremeInfoDTO> dtoList = filterResult
                .getContent()
                .stream()
                .map(this::toInfoDTO)
                .collect(Collectors.toList());
        Page<ExtremeInfoDTO> pageObj = new PageImpl<>(dtoList, PageRequest.of(page, size), filterResult.getTotalCount());
        return ApiResponse.ok(pageObj);
    }

    private ExtremeInfoDTO toInfoDTO(ExtremeMapperDTO mapper) {
        ExtremeInfoDTO extremeInfoDTO = placeService.toInfoDTO(new ExtremeInfoDTO(), mapper);
        extremeInfoDTO.setExtremeType(mapper.getExtremeType());
        extremeInfoDTO.setWeekendPrice(mapper.getWeekendPrice());
        extremeInfoDTO.setWeekDayPrice(mapper.getWeekDayPrice());
        extremeInfoDTO.setCreatedDate(mapper.getCreatedDate());
        extremeInfoDTO.setLatitude(mapper.getLatitude());
        extremeInfoDTO.setLongitude(mapper.getLongitude());
        extremeInfoDTO.setTariffResult(mapper.getTariffResult());

        return extremeInfoDTO;
    }
}
