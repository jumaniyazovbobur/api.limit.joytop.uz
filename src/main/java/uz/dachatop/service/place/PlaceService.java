package uz.dachatop.service.place;


import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import uz.dachatop.config.EntityDetails;
import uz.dachatop.dto.district.DistrictDTO;
import uz.dachatop.dto.place.*;
import uz.dachatop.dto.region.RegionDTO;
import uz.dachatop.dto.response.ApiResponse;
import uz.dachatop.entity.ProfileEntity;
import uz.dachatop.entity.place.PlaceEntity;
import uz.dachatop.enums.AppLanguage;
import uz.dachatop.mapper.PlaceMapperDTO;
import uz.dachatop.repository.filter.SubscriptionFilterRepository;
import uz.dachatop.repository.place.PlaceCustomFilter;
import uz.dachatop.repository.place.apartment.ApartmentRepository;
import uz.dachatop.repository.place.camp.CampRepository;
import uz.dachatop.repository.place.dacha.DachaRepository;
import uz.dachatop.repository.place.extreme.ExtremeRepository;
import uz.dachatop.repository.place.hotel.HotelRepository;
import uz.dachatop.repository.place.travel.TravelRepository;
import uz.dachatop.service.*;

@Service
@AllArgsConstructor
public class PlaceService {

    private final RegionService regionService;
    private final DistrictService districtService;
    private final ProfileService profileService;
    private final PlaceAttachService placeAttachService;
    private final AttachServer attachServer;
    private final PlaceCustomFilter placeCustomFilter;
    private final SubscriptionFilterRepository subscriptionFilterRepository;
    private final ApartmentRepository apartmentRepository;
    private final DachaRepository dachaRepository;
    private final HotelRepository hotelRepository;
    private final ExtremeRepository extremeRepository;
    private final CampRepository campRepository;
    private final TravelRepository travelRepository;

    public static <T extends PlaceEntity> T toEntity(T entity, PlaceCreateDTO dto) {
        // media
        entity.setVideoUrl(dto.getVideoUrl());
        // address
        entity.setRegionId(dto.getRegionId());
        entity.setDistrictId(dto.getDistrictId());
        entity.setTerritoryId(dto.getTerritoryId());
        // location
        entity.setLatitude(dto.getLatitude());
        entity.setLongitude(dto.getLongitude());
        // main information
        entity.setType(dto.getType());
        //Rules
        entity.setSmoking(dto.isSmoking());
        entity.setAlcohol(dto.isAlcohol());
        entity.setPets(dto.isPets());
        entity.setAvailableOnlyFamily(dto.isAvailableOnlyFamily());
        entity.setLoudlyMusic(dto.isLoudlyMusic());
        entity.setParty(dto.isParty());
        // owner
        entity.setProfileId(EntityDetails.getCurrentProfileId());
        // other
        entity.setDescription(dto.getDescription());
        entity.setContact(dto.getContact());
        return entity;
    }

    public static <T extends PlaceEntity> T toEntityForUpdate(T entity, PlaceCreateDTO dto) {
        // media
        entity.setVideoUrl(dto.getVideoUrl());
        // address
        entity.setRegionId(dto.getRegionId());
        entity.setDistrictId(dto.getDistrictId());
        entity.setTerritoryId(dto.getTerritoryId());
        // location
        entity.setLatitude(dto.getLatitude());
        entity.setLongitude(dto.getLongitude());
        // main information
        entity.setType(dto.getType());
        //Rules
        entity.setSmoking(dto.isSmoking());
        entity.setAlcohol(dto.isAlcohol());
        entity.setPets(dto.isPets());
        entity.setAvailableOnlyFamily(dto.isAvailableOnlyFamily());
        entity.setLoudlyMusic(dto.isLoudlyMusic());
        entity.setParty(dto.isParty());
        // other
        entity.setDescription(dto.getDescription());
        entity.setContact(dto.getContact());
        return entity;
    }


    public <T extends PlaceDTO> T toDetailDTO(T dto, PlaceEntity entity, AppLanguage language) {
        //
        dto.setId(entity.getId());
        // media
        dto.setVideoUrl(entity.getVideoUrl());
        // address
        dto.setRegion(regionService.getById(entity.getRegionId(), language));
        dto.setDistrict(districtService.getById(entity.getDistrictId(), language));
        // location
        dto.setLatitude(entity.getLatitude());
        dto.setLongitude(entity.getLongitude());
        // main information
        dto.setType(entity.getType());
        //Rules
        dto.setSmoking(entity.isSmoking());
        dto.setAlcohol(entity.isAlcohol());
        dto.setPets(entity.isPets());
        dto.setAvailableOnlyFamily(entity.isAvailableOnlyFamily());
        dto.setLoudlyMusic(entity.isLoudlyMusic());
        dto.setParty(entity.isParty());
        //contact
        dto.setContact(entity.getContact());
        dto.setStatus(entity.getStatus());
        // owner
        dto.setProfile(profileService.getProfileShortInfo(entity.getProfileId()));
        //photo
        dto.setAttachList(placeAttachService.getPlaceAttachList(entity.getId()));
        //other
        dto.setCreatedDate(entity.getCreatedDate());
        // other
        dto.setDescription(entity.getDescription());
        dto.setContact(entity.getContact());

        dto.setTariffResult(subscriptionFilterRepository.getSubResul(entity.getId(), entity.getType().name(), language.name()));
        return dto;
    }

    public <T extends PlaceInfoDTO> T toInfoDTO(T infoDTO, PlaceMapperDTO dto) {
        infoDTO.setId(dto.getId());
        // address
        infoDTO.setRegion(new RegionDTO(dto.getRegionId(), dto.getRegionName()));
        infoDTO.setDistrict(new DistrictDTO(dto.getDistrictId(), dto.getDistrictName()));
        // location
        infoDTO.setLatitude(dto.getLatitude());
        infoDTO.setLongitude(dto.getLongitude());
        // main information
        infoDTO.setMainAttach(attachServer.webContentLink(dto.getMainAttachId()));
        //
        infoDTO.setPlaceType(dto.getPlaceType());
        infoDTO.setStatus(dto.getStatus());
        infoDTO.setCreatedDate(dto.getCreatedDate());
        return infoDTO;
    }

    public PageImpl<PlaceInfoDTO> findAllPlaces(int page, int size, AppLanguage language) {
        CustomPageImplResult<PlaceInfoDTO> filter = placeCustomFilter
                .filter(language, EntityDetails.getCurrentProfileId(), page, size);
        return new PageImpl<>(filter.getList(), PageRequest.of(page, size), filter.getCount());
    }

    public PageImpl<PlaceInfoDTO> findAllPlacesByFilter(PlaceFilterDTO filterDTO, int page, int size, AppLanguage language) {
        CustomPageImplResult<PlaceInfoDTO> filter = placeCustomFilter
                .filterForAdmin(filterDTO, language,
                        EntityDetails.getCurrentProfileId(),
                        page, size);
        return new PageImpl<>(filter.getList(), PageRequest.of(page, size), filter.getCount());
    }

    public ApiResponse<?> transferPlace(PlaceTransferReqDTO dto) {
        ProfileEntity profile = profileService.getByPhone(dto.getToProfile());
        switch (dto.getType()) {
            case APARTMENT -> apartmentRepository.transfer(dto.getPlaceId(), profile.getId());
            case HOTEL -> hotelRepository.transfer(dto.getPlaceId(), profile.getId());
            case TRAVEL -> travelRepository.transfer(dto.getPlaceId(), profile.getId());
            case CAMPING -> campRepository.transfer(dto.getPlaceId(), profile.getId());
            case COTTAGE -> dachaRepository.transfer(dto.getPlaceId(), profile.getId());
            case EXTREME -> extremeRepository.transfer(dto.getPlaceId(), profile.getId());
        }
        return ApiResponse.ok();
    }
}
