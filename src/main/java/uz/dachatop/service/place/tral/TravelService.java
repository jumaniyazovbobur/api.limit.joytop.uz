package uz.dachatop.service.place.tral;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import uz.dachatop.config.EntityDetails;
import uz.dachatop.dto.place.CustomPageImplResult;
import uz.dachatop.dto.place.PlaceFilterResult;
import uz.dachatop.dto.place.camp.TravelReqDTO;
import uz.dachatop.dto.place.dacha.DachaInfoDTO;
import uz.dachatop.dto.place.travel.TravelCreateDTO;
import uz.dachatop.dto.place.travel.TravelFilterDTO;
import uz.dachatop.dto.place.travel.TravelInfoDTO;
import uz.dachatop.dto.profile.ProfileRoleDTO;
import uz.dachatop.dto.response.ApiResponse;
import uz.dachatop.entity.place.apartment.ApartmentEntity;
import uz.dachatop.entity.place.travel.TravelEntity;
import uz.dachatop.enums.AppLanguage;
import uz.dachatop.enums.GlobalStatus;
import uz.dachatop.enums.ProfileRole;
import uz.dachatop.exp.ItemNotFoundException;
import uz.dachatop.mapper.TravelMapperDTO;
import uz.dachatop.repository.place.travel.TravelCustomFilter;
import uz.dachatop.repository.place.travel.TravelRepository;
import uz.dachatop.service.CountryService;
import uz.dachatop.service.place.PlaceAttachService;
import uz.dachatop.service.place.PlaceService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TravelService {


    private final TravelRepository travelRepository;
    private final CountryService countryService;


    private final PlaceAttachService placeAttachService;
    private final PlaceService placeService;
    private final TravelCustomFilter travelCustomFilter;

    public ApiResponse<Boolean> create(TravelCreateDTO dto) {
        TravelEntity camp = PlaceService.toEntity(new TravelEntity(), dto);
        toEntity(camp, dto);
        travelRepository.save(camp);
        placeAttachService.mergePlaceAttach(camp.getId(), dto.getAttachList());
        return ApiResponse.ok(true);
    }

    private void toEntity(TravelEntity entity, TravelCreateDTO dto) {
        countryService.get(dto.getCountryId());
        entity.setCountryId(dto.getCountryId());
        entity.setStatus(GlobalStatus.ACTIVE);
        entity.setStandardsPrice(dto.getStandardsPrice());
        entity.setPriceOnSale(dto.getPriceOnSale());
        entity.setCompanyName(dto.getCompanyName());
        entity.setCompanyAddress(dto.getCompanyAddress());
    }


    public TravelEntity get(String id) {
        return travelRepository.findByIdAndVisibleIsTrue(id)
                .orElseThrow(() -> new ItemNotFoundException("Apartment Not Found"));
    }

    public ApiResponse<String> update(String campId, TravelCreateDTO dto) {
        TravelEntity entity = get(campId);
        if (!entity.getProfileId().equals(EntityDetails.getCurrentProfileId())) {
            return ApiResponse.bad("You are not owner. Mazgi.");
        }
        PlaceService.toEntity(entity, dto);
        toEntity(entity, dto);
        travelRepository.save(entity);
        // add image
        placeAttachService.mergePlaceAttach(entity.getId(), dto.getAttachList());
        return ApiResponse.ok();
    }

    public ApiResponse<String> delete(String id) {
        TravelEntity entity = get(id);
        String profileId = EntityDetails.getCurrentProfileId();
        List<ProfileRole> profileRoles = Objects.requireNonNull(EntityDetails.getAdminEntity()).getRoleList()
                .stream().map(ProfileRoleDTO::getRole).toList();
        if (!entity.getProfileId().equals(profileId)
                && !profileRoles.contains(ProfileRole.ROLE_ADMIN)) {
            return ApiResponse.bad("You are not owner. Mazgi.");
        }
        travelRepository.deleteExtreme(id, profileId, LocalDateTime.now());
        return null;
    }

    public ApiResponse<?> changeStatus(String dachaId, GlobalStatus status) {
        TravelEntity entity = get(dachaId);
        String profileId = EntityDetails.getCurrentProfileId();
        travelRepository.changeStatus(dachaId, profileId, status, LocalDateTime.now());
        return ApiResponse.ok();
    }

    public ApiResponse<TravelReqDTO> getById(String travelId, AppLanguage language) {
        Optional<TravelEntity> optional = travelRepository.findByIdAndVisibleTrue(travelId);
        if (optional.isEmpty()) {
            return ApiResponse.bad("Item not found");
        }
        TravelReqDTO dto = toDetailDTO(optional.get(), language);
        return ApiResponse.ok(dto);
    }

    private TravelReqDTO toDetailDTO(TravelEntity entity, AppLanguage language) {
        TravelReqDTO travelReqDTO = placeService.toDetailDTO(new TravelReqDTO(), entity, language);
        travelReqDTO.setCompanyAddress(entity.getCompanyAddress());
        travelReqDTO.setCompanyName(entity.getCompanyName());
        travelReqDTO.setPriceOnSale(entity.getPriceOnSale());
        travelReqDTO.setStandardsPrice(entity.getStandardsPrice());
        travelReqDTO.setCountry(countryService.getById(entity.getCountryId(), language).getData());
        return travelReqDTO;
    }

    public ApiResponse<Page<TravelInfoDTO>> filterAsClient(TravelFilterDTO filter, int page, int size, AppLanguage lang) {
        PlaceFilterResult<TravelMapperDTO> filterResult = travelCustomFilter.filter(filter, lang, page * size, size);
        List<TravelInfoDTO> dtoList = filterResult.getContent().stream()
                .map(this::toInfoDTO)
                .collect(Collectors.toList());
//        return ApiResponse.ok(new CustomPageImplResult<>(dtoList, filterResult.getTotalCount()));
        Page<TravelInfoDTO> pageObj = new PageImpl<>(dtoList, PageRequest.of(page, size), filterResult.getTotalCount());
        return ApiResponse.ok(pageObj);
    }

    private TravelInfoDTO toInfoDTO(TravelMapperDTO mapper) {
        TravelInfoDTO travelInfoDTO = placeService.toInfoDTO(new TravelInfoDTO(), mapper);
        travelInfoDTO.setCountry(mapper.getCountry());
        travelInfoDTO.setCompanyAddress(mapper.getCompanyAddress());
        travelInfoDTO.setCompanyName(mapper.getCompanyName());
        travelInfoDTO.setStandardsPrice(mapper.getStandardsPrice());
        travelInfoDTO.setPriceOnSale(mapper.getPriceOnSale());
        travelInfoDTO.setLatitude(mapper.getLatitude());
        travelInfoDTO.setLongitude(mapper.getLongitude());
        travelInfoDTO.setTariffResult(mapper.getTariffResult());
        return travelInfoDTO;
    }

}
