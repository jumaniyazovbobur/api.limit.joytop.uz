package uz.dachatop.service.place.camp;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import uz.dachatop.config.EntityDetails;
import uz.dachatop.dto.place.CustomPageImplResult;
import uz.dachatop.dto.place.PlaceFilterResult;
import uz.dachatop.dto.place.camp.CampCreateDTO;
import uz.dachatop.dto.place.camp.CampFilterDTO;
import uz.dachatop.dto.place.camp.CampInfoDTO;
import uz.dachatop.dto.place.camp.CampReqDTO;
import uz.dachatop.dto.place.extreme.ExtremeInfoDTO;
import uz.dachatop.dto.profile.ProfileRoleDTO;
import uz.dachatop.dto.response.ApiResponse;
import uz.dachatop.entity.place.apartment.ApartmentEntity;
import uz.dachatop.entity.place.camp.CampEntity;
import uz.dachatop.enums.AppLanguage;
import uz.dachatop.enums.GlobalStatus;
import uz.dachatop.enums.ProfileRole;
import uz.dachatop.exp.ItemNotFoundException;
import uz.dachatop.mapper.CampMapperDTO;
import uz.dachatop.repository.place.camp.CampCustomFilter;
import uz.dachatop.repository.place.camp.CampRepository;
import uz.dachatop.service.place.PlaceAttachService;
import uz.dachatop.service.place.PlaceCalendarService;
import uz.dachatop.service.place.PlaceConvenienceService;
import uz.dachatop.service.place.PlaceService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CampService {

    private final CampRepository campRepository;


    private final PlaceAttachService placeAttachService;
    private final PlaceService placeService;
    private final CampCustomFilter campCustomFilter;
    private final PlaceConvenienceService placeConvenienceService;
    private final PlaceCalendarService placeCalendarService;

    public ApiResponse<Boolean> create(CampCreateDTO dto) {
        CampEntity camp = PlaceService.toEntity(new CampEntity(), dto);
        toEntity(camp, dto);
        campRepository.save(camp);
        // add image
        placeAttachService.mergePlaceAttach(camp.getId(), dto.getAttachList());
        // add convenience
        placeConvenienceService.mergeConvenienceList(camp.getId(), dto.getConvenienceList());
        return ApiResponse.ok(true);
    }

    private void toEntity(CampEntity entity, CampCreateDTO dto) {
        // main information
        entity.setName(dto.getName());
        //price
        entity.setPrice(dto.getPrice());
        entity.setPriceOnSale(dto.getPriceOnSale()); // сумма со скидкой
    }


    public CampEntity get(String id) {
        return campRepository.findByIdAndVisibleIsTrue(id).orElseThrow(() -> {
            throw new ItemNotFoundException("Apartment Not Found");
        });
    }

    public ApiResponse<String> update(String campId, CampCreateDTO dto) {
        CampEntity entity = get(campId);
        if (!entity.getProfileId().equals(EntityDetails.getCurrentProfileId())) {
            return ApiResponse.bad("You are not owner. Mazgi.");
        }
         PlaceService.toEntity(entity, dto);;
        toEntity(entity, dto);
        campRepository.save(entity);
        // add image
        placeAttachService.mergePlaceAttach(entity.getId(), dto.getAttachList());
        // add convenience
        placeConvenienceService.mergeConvenienceList(entity.getId(), dto.getConvenienceList());
        return ApiResponse.ok();
    }

    public ApiResponse<String> delete(String id) {
        CampEntity entity = get(id);
        String profileId = EntityDetails.getCurrentProfileId();
        List<ProfileRole> profileRoles = Objects.requireNonNull(EntityDetails.getAdminEntity()).getRoleList()
                .stream().map(ProfileRoleDTO::getRole).toList();
        if (!entity.getProfileId().equals(profileId)
                && !profileRoles.contains(ProfileRole.ROLE_ADMIN)) {
            return ApiResponse.bad("You are not owner. Mazgi.");
        }
        campRepository.deleteExtreme(id, profileId, LocalDateTime.now());
        return null;
    }

    public ApiResponse<?> changeStatus(String dachaId, GlobalStatus status) {
        CampEntity entity = get(dachaId);
        String profileId = EntityDetails.getCurrentProfileId();
        campRepository.changeStatus(dachaId, profileId, status, LocalDateTime.now());
        return ApiResponse.ok();
    }

    public ApiResponse<CampReqDTO> getCampById(String campId, AppLanguage language) {
        Optional<CampEntity> optional = campRepository.findByIdAndVisibleTrue(campId);
        if (optional.isEmpty()) {
            return ApiResponse.bad("Item not found");
        }
        CampReqDTO dto = toDetailDTO(optional.get(), language);
        return ApiResponse.ok(dto);
    }

    private CampReqDTO toDetailDTO(CampEntity entity, AppLanguage language) {
        CampReqDTO campReqDTO = placeService.toDetailDTO(new CampReqDTO(), entity, language);
        campReqDTO.setName(entity.getName());
        campReqDTO.setPrice(entity.getPrice());
        campReqDTO.setPriceOnSale(entity.getPriceOnSale());
        // convenienceList
        campReqDTO.setConvenienceList(placeConvenienceService.getPlaceConvenienceList(entity.getId(), language));
      // calendarList
        campReqDTO.setCalendarList(placeCalendarService.getCalendarListByPlaceId(entity.getId()));
        return campReqDTO;
    }

    public ApiResponse<Page<CampInfoDTO>> filterAsClient(CampFilterDTO filter, int page, int size, AppLanguage lang) {
        PlaceFilterResult<CampMapperDTO> filterResult = campCustomFilter.filter(filter, lang, page * size, size);
        List<CampInfoDTO> dtoList = filterResult.getContent().stream().map(this::toInfoDTO).collect(Collectors.toList());
        Page<CampInfoDTO> pageObj = new PageImpl<CampInfoDTO>(dtoList, PageRequest.of(page, size), filterResult.getTotalCount());
        return ApiResponse.ok(pageObj);
    }

    private CampInfoDTO toInfoDTO(CampMapperDTO mapper) {
        CampInfoDTO campInfoDTO = placeService.toInfoDTO(new CampInfoDTO(), mapper);
        campInfoDTO.setName(mapper.getName());
        campInfoDTO.setPrice(mapper.getPrice());
        campInfoDTO.setPriceOnSale(mapper.getPriceOnSale());
        campInfoDTO.setCreatedDate(mapper.getCreatedDate());
        campInfoDTO.setLatitude(mapper.getLatitude());
        campInfoDTO.setLongitude(mapper.getLongitude());
        campInfoDTO.setTariffResult(mapper.getTariffResult());

        return campInfoDTO;
    }

}
