package uz.dachatop.service.place.hotel;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import uz.dachatop.config.EntityDetails;
import uz.dachatop.dto.place.PlaceFilterResult;
import uz.dachatop.dto.place.hotel.HotelCreateDTO;
import uz.dachatop.dto.place.hotel.HotelDTO;
import uz.dachatop.dto.place.hotel.HotelFilterDTO;
import uz.dachatop.dto.place.hotel.HotelInfoDTO;
import uz.dachatop.dto.profile.ProfileRoleDTO;
import uz.dachatop.dto.response.ApiResponse;
import uz.dachatop.entity.place.hotel.HotelEntity;
import uz.dachatop.enums.AppLanguage;
import uz.dachatop.enums.GlobalStatus;
import uz.dachatop.enums.ProfileRole;
import uz.dachatop.enums.RoomType;
import uz.dachatop.exp.AppBadRequestException;
import uz.dachatop.exp.ItemNotFoundException;
import uz.dachatop.mapper.hotel.HotelMapperDTO;
import uz.dachatop.repository.place.hotel.HotelCustomFilter;
import uz.dachatop.repository.place.hotel.HotelRepository;
import uz.dachatop.service.place.PlaceAttachService;
import uz.dachatop.service.place.PlaceConvenienceService;
import uz.dachatop.service.place.PlaceService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class HotelService {
    private final HotelRepository hotelRepository;
    private final PlaceAttachService placeAttachService;
    private final HotelRoomService hotelRoomService;
    private final PlaceService placeService;
    private HotelCustomFilter customFilter;
    private final PlaceConvenienceService placeConvenienceService;

    public ApiResponse<String> create(HotelCreateDTO dto) {
        // TODO check for required fields
        HotelEntity hotel = PlaceService.toEntity(new HotelEntity(), dto);
        toEntity(hotel, dto);
        hotel.setProfileId(EntityDetails.getCurrentProfileId());
        hotel.setMinPrice(dto.getMinPrice());
        hotel.setStarCount(dto.getStarCount());
        // save
        hotelRepository.save(hotel);
        // add image
        placeAttachService.mergePlaceAttach(hotel.getId(), dto.getAttachList());
        // merge hotel room
        hotelRoomService.merge(hotel.getId(), dto.getRoomList());
        // add convenience
        placeConvenienceService.mergeConvenienceList(hotel.getId(), dto.getConvenienceList());
        return ApiResponse.ok(hotel.getId());
    }

    public ApiResponse<String> update(String hotelId, HotelCreateDTO dto) {
        // check  for requirement fields
        HotelEntity hotel = get(hotelId);
        if (!EntityDetails.getCurrentProfileId()
                .equals(hotel.getProfileId()) &&
                !Objects.requireNonNull(EntityDetails
                                .getCurrentProfile())
                        .getRoleList()
                        .stream()
                        .anyMatch(value -> value.getRole()
                                .equals(ProfileRole.ROLE_ADMIN))) {

            throw new AppBadRequestException("You are not owner");
        }
        PlaceService.toEntityForUpdate(hotel, dto);
        toEntity(hotel, dto);
        // update
        hotelRepository.save(hotel);
        // add image
        placeAttachService.mergePlaceAttach(hotel.getId(), dto.getAttachList());
        // merge hotel room
        hotelRoomService.merge(hotel.getId(), dto.getRoomList());
        // add convenience
        placeConvenienceService.mergeConvenienceList(hotel.getId(), dto.getConvenienceList());
        return ApiResponse.ok();
    }

    public ApiResponse<Page<HotelInfoDTO>> filter_asClient(HotelFilterDTO filter, int page, int size, AppLanguage lang) {
        PlaceFilterResult<HotelMapperDTO> filterResult = customFilter.filter(filter, lang, page * size, size);
        List<HotelInfoDTO> dtoList = filterResult.getContent()
                .stream()
                .map(this::toInfoDTO)
                .collect(Collectors.toList());
        Page<HotelInfoDTO> pageObj = new PageImpl<>(dtoList, PageRequest.of(page, size), filterResult.getTotalCount());
        return ApiResponse.ok(pageObj);
    }

    public ApiResponse<HotelDTO> getDachaById(String dachaId, AppLanguage lang) {
        Optional<HotelEntity> optional = hotelRepository.findByIdAndVisibleTrue(dachaId);
        if (optional.isEmpty()) {
            return ApiResponse.bad("Item not found");
        }
        HotelDTO dto = toDetailDTO(optional.get(), lang);
        return ApiResponse.ok(dto);
    }

    public ApiResponse<?> deleteHotel(String id, AppLanguage lang) {
        //TODO only admin or owner.
        HotelEntity entity = get(id);
        String profileId = EntityDetails.getCurrentProfileId();
        List<ProfileRole> profileRoles = Objects.requireNonNull(EntityDetails.getAdminEntity()).getRoleList()
                .stream().map(ProfileRoleDTO::getRole).toList();
        if (!entity.getProfileId().equals(profileId)
                && !profileRoles.contains(ProfileRole.ROLE_ADMIN)) {
            return ApiResponse.bad("You are not owner. Mazgi.");
        }
        hotelRepository.deleteHotel(id, profileId, LocalDateTime.now());
        return ApiResponse.ok();
    }

    public ApiResponse<?> changeStatus(String dachaId, GlobalStatus status) {
        //TODO only admin or owner.
        HotelEntity entity = get(dachaId);
        String profileId = EntityDetails.getCurrentProfileId();
        if (!entity.getProfileId().equals(profileId)) {
            return ApiResponse.bad("You are not owner. Mazgi.");
        }
        hotelRepository.changeStatus(dachaId, profileId, status, LocalDateTime.now());
        return ApiResponse.ok();
    }

    private void toEntity(HotelEntity entity, HotelCreateDTO dto) {
        // main information
        entity.setName(dto.getName());
        entity.setMinPrice(dto.getMinPrice());
        entity.setStarCount(dto.getStarCount());
    }

    public HotelEntity get(String id) {
        return hotelRepository.findById(id).orElseThrow(() -> {
            throw new ItemNotFoundException("Hotel Not Found");
        });
    }

    private HotelInfoDTO toInfoDTO(HotelMapperDTO mapper) {
        HotelInfoDTO infoDTO = placeService.toInfoDTO(new HotelInfoDTO(), mapper);
        infoDTO.setName(mapper.getName());
        infoDTO.setStarCount(mapper.getStarCount());
        infoDTO.setMinPrice(mapper.getMinPrice());
        infoDTO.setLatitude(mapper.getLatitude());
        infoDTO.setLongitude(mapper.getLongitude());
        if (mapper.getRoomTypeStr() != null) {
            String[] str = mapper.getRoomTypeStr().split(",");
            List<RoomType> roomTypeList = new LinkedList<>();
            for (String s : str) {
                roomTypeList.add(RoomType.valueOf(s));
            }
            infoDTO.setRoomTypeList(roomTypeList);
        }
        infoDTO.setTariffResult(mapper.getTariffResult());
        return infoDTO;
    }

    private HotelDTO toDetailDTO(HotelEntity entity, AppLanguage language) {
        HotelDTO dto = placeService.toDetailDTO(new HotelDTO(), entity, language);
        // main information
        dto.setName(entity.getName());
        dto.setMinPrice(entity.getMinPrice());
        dto.setStarCount(entity.getStarCount());
        // room
        dto.setRoomList(hotelRoomService.getHotelRoomList(entity.getId(), language));
        dto.setConvenienceList(placeConvenienceService.getPlaceConvenienceList(entity.getId(), language));
        return dto;
    }

}
