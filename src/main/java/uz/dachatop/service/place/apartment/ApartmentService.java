package uz.dachatop.service.place.apartment;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import uz.dachatop.config.EntityDetails;
import uz.dachatop.dto.place.PlaceDTO;
import uz.dachatop.dto.place.PlaceFilterResult;
import uz.dachatop.dto.place.PlacePaginationDTO;
import uz.dachatop.dto.place.apartment.ApartmentCreateDTO;
import uz.dachatop.dto.place.apartment.ApartmentDTO;
import uz.dachatop.dto.place.apartment.ApartmentFilterDTO;
import uz.dachatop.dto.place.apartment.ApartmentInfoDTO;
import uz.dachatop.dto.profile.*;
import uz.dachatop.dto.response.ApiResponse;
import uz.dachatop.entity.ProfileEntity;
import uz.dachatop.entity.place.apartment.ApartmentEntity;
import uz.dachatop.entity.place.dacha.DachaEntity;
import uz.dachatop.entity.subscriotion.SubscriptionTariffEntity;
import uz.dachatop.enums.AppLanguage;
import uz.dachatop.enums.GlobalStatus;
import uz.dachatop.enums.ProfileRole;
import uz.dachatop.exp.ItemNotFoundException;
import uz.dachatop.mapper.apartment.ApartmentMapperDTO;
import uz.dachatop.repository.place.apartment.ApartmentCustomFilter;
import uz.dachatop.repository.place.apartment.ApartmentRepository;
import uz.dachatop.service.AttachServer;
import uz.dachatop.service.DistrictService;
import uz.dachatop.service.ProfileService;
import uz.dachatop.service.RegionService;
import uz.dachatop.service.place.PlaceAttachService;
import uz.dachatop.service.place.PlaceCalendarService;
import uz.dachatop.service.place.PlaceConvenienceService;
import uz.dachatop.service.place.PlaceService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ApartmentService {
    private final ApartmentRepository apartmentRepository;
    private final PlaceAttachService placeAttachService;
    private final PlaceConvenienceService placeConvenienceService;
    private final ApartmentCustomFilter apartmentCustomFilter;
    private final PlaceService placeService;

    public ApiResponse<String> create(ApartmentCreateDTO dto) {
        // check required fields
        ApartmentEntity apartmentEntity = PlaceService.toEntity(new ApartmentEntity(), dto);
        toEntity(dto, apartmentEntity);
        apartmentEntity.setProfileId(EntityDetails.getCurrentProfileId());
        apartmentRepository.save(apartmentEntity); // create apartment
        // add image
        placeAttachService.mergePlaceAttach(apartmentEntity.getId(), dto.getAttachList());
        // add convenience
        placeConvenienceService.mergeConvenienceList(apartmentEntity.getId(), dto.getConvenienceList());
        return ApiResponse.ok();
    }

    public ApiResponse<String> update(String apartmentId, ApartmentCreateDTO dto) {
        // check required fields
        ApartmentEntity entity = get(apartmentId);
        if (!entity.getProfileId().equals(EntityDetails.getCurrentProfileId())) {
            return ApiResponse.bad("You are not owner. Mazgi.");
        }
        PlaceService.toEntity(entity, dto);
        toEntity(dto, entity);
        apartmentRepository.save(entity); // update apartment
        // add image
        placeAttachService.mergePlaceAttach(entity.getId(), dto.getAttachList());
        // add convenience
        placeConvenienceService.mergeConvenienceList(entity.getId(), dto.getConvenienceList());
        return new ApiResponse<>(200, false);
    }

    public ApiResponse<Page<ApartmentInfoDTO>> apartmentFilter_asClient(ApartmentFilterDTO filter, int page, int size, AppLanguage lang) {
        PlaceFilterResult<ApartmentMapperDTO> filterResult = apartmentCustomFilter.filter(filter, lang, page * size, size);
        List<ApartmentInfoDTO> dtoList = filterResult
                .getContent()
                .stream()
                .map(this::toInfoDTO)
                .collect(Collectors.toList());
        Page<ApartmentInfoDTO> pageObj = new PageImpl<ApartmentInfoDTO>(dtoList, PageRequest.of(page, size), filterResult.getTotalCount());
        return ApiResponse.ok(pageObj);
    }

    public ApiResponse<ApartmentDTO> getApartmentById(String apartmentId, AppLanguage lang) {
        Optional<ApartmentEntity> optional = apartmentRepository.findByIdAndVisibleTrue(apartmentId);
        if (optional.isEmpty()) {
            return ApiResponse.bad("Item not found");
        }
        ApartmentDTO dto = toDetailDTO(optional.get(), lang);
        return ApiResponse.ok(dto);
    }

    public ApiResponse<?> deleteApartment(String id, AppLanguage lang) {
        ApartmentEntity entity = get(id);
        String profileId = EntityDetails.getCurrentProfileId();
        List<ProfileRole> profileRoles = Objects.requireNonNull(EntityDetails.getAdminEntity()).getRoleList()
                .stream().map(ProfileRoleDTO::getRole).toList();
        if (!entity.getProfileId().equals(profileId)
                && !profileRoles.contains(ProfileRole.ROLE_ADMIN)) {
            return ApiResponse.bad("You are not owner. Mazgi.");
        }
        apartmentRepository.deleteApartment(id, profileId, LocalDateTime.now());
        return ApiResponse.ok();
    }

    private ApartmentInfoDTO toInfoDTO(ApartmentMapperDTO mapper) {
        ApartmentInfoDTO apartmentInfoDTO = placeService.toInfoDTO(new ApartmentInfoDTO(), mapper);
        apartmentInfoDTO.setDayPrice(mapper.getDayPrice());
        apartmentInfoDTO.setRoomCount(mapper.getRoomCount());
        apartmentInfoDTO.setSubType(mapper.getSubType());
        apartmentInfoDTO.setMonthPrice(mapper.getMonthPrice());
        apartmentInfoDTO.setTotalArea(mapper.getTotalArea());
        apartmentInfoDTO.setSingleBedRoomCount(mapper.getSingleBedRoomCount());
        apartmentInfoDTO.setDoubleBedRoomCount(mapper.getDoubleBedRoomCount());
        apartmentInfoDTO.setCreatedDate(mapper.getCreatedDate());
        apartmentInfoDTO.setSalePrice(mapper.getSalePrice());
        apartmentInfoDTO.setLatitude(mapper.getLatitude());
        apartmentInfoDTO.setLongitude(mapper.getLongitude());
        apartmentInfoDTO.setTariffResult(mapper.getTariffResult());
        return apartmentInfoDTO;
    }

    private void toEntity(ApartmentCreateDTO dto, ApartmentEntity apartment) {
        // main information
        apartment.setSubType(dto.getSubType());
        apartment.setRoomCount(dto.getRoomCount());
        apartment.setTotalArea(dto.getTotalArea()); // общее помощение
        apartment.setSingleBedRoomCount(dto.getSingleBedRoomCount()); // количество одноместных спальный
        apartment.setDoubleBedRoomCount(dto.getDoubleBedRoomCount());  // количество двухместный спальный
        //price
        apartment.setDayPrice(dto.getDayPrice()); // обычная цена за сутки
        apartment.setMonthPrice(dto.getMonthPrice()); // сумма со скидкой
        apartment.setGageOfDeposit(dto.getGageOfDeposit()); // процент залога
        apartment.setSalePrice(dto.getSalePrice());
    }

    private ApartmentDTO toDetailDTO(ApartmentEntity entity, AppLanguage language) {
        ApartmentDTO dto = placeService.toDetailDTO(new ApartmentDTO(), entity, language);
        // media
        dto.setSubType(entity.getSubType());
        dto.setRoomCount(entity.getRoomCount());
        dto.setTotalArea(entity.getTotalArea()); // общее помощение
        dto.setSingleBedRoomCount(entity.getSingleBedRoomCount()); // количество одноместных спальный
        dto.setDoubleBedRoomCount(entity.getDoubleBedRoomCount());  // количество двухместный спальный
        //price
        dto.setDayPrice(entity.getDayPrice());
        dto.setMonthPrice(entity.getMonthPrice());
        dto.setGageOfDeposit(entity.getGageOfDeposit()); // процент залога
        // convenienceList
        dto.setConvenienceList(placeConvenienceService.getPlaceConvenienceList(entity.getId(), language));
        dto.setSalePrice(entity.getSalePrice());
        return dto;
    }


    public ApartmentEntity get(String id) {
        return apartmentRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Apartment Not Found"));
    }

    public ApiResponse<?> changeStatus(String dachaId, GlobalStatus status) {
        ApartmentEntity entity = get(dachaId);
        String profileId = EntityDetails.getCurrentProfileId();
        apartmentRepository.changeStatus(dachaId, profileId, status, LocalDateTime.now());
        return ApiResponse.ok();
    }


    private ApartmentDTO toDTO(ApartmentEntity entity) {
        ApartmentDTO dto = new ApartmentDTO();
        // media
        dto.setSubType(entity.getSubType());
        dto.setRoomCount(entity.getRoomCount());
        dto.setTotalArea(entity.getTotalArea()); // общее помощение
        dto.setSingleBedRoomCount(entity.getSingleBedRoomCount()); // количество одноместных спальный
        dto.setDoubleBedRoomCount(entity.getDoubleBedRoomCount());  // количество двухместный спальный
        //price
        dto.setDayPrice(entity.getDayPrice());
        dto.setMonthPrice(entity.getMonthPrice());
        dto.setGageOfDeposit(entity.getGageOfDeposit()); // процент залога
        // convenienceList
        dto.setSalePrice(entity.getSalePrice());
        return dto;
    }


       /* public ApiResponse<Page<ApartmentInfoDTO>> profileApartmentPagination(int page, int size, AppLanguage lang) {
        String profileId = EntityDetails.getCurrentProfileId();
        List<IApartmentInfoMapper> mapperList = apartmentRepository.getProfileApartmentList(profileId, lang.name(), page * size, size);
        List<ApartmentInfoDTO> dtoList = mapperList.stream().map(iApartmentInfoMapper -> toInfoDTO(iApartmentInfoMapper)).collect(Collectors.toList());
        Long totalCount = apartmentRepository.getProfileApartmentCount(profileId);
        Page<ApartmentInfoDTO> pageObj = new PageImpl<ApartmentInfoDTO>(dtoList, PageRequest.of(page, size), totalCount);
        return ApiResponse.ok(pageObj);
    }*/

}
