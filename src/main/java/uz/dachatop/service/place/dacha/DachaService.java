package uz.dachatop.service.place.dacha;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import uz.dachatop.config.EntityDetails;
import uz.dachatop.dto.place.PlaceFilterResult;
import uz.dachatop.dto.place.dacha.DachaCreateDTO;
import uz.dachatop.dto.place.dacha.DachaDTO;
import uz.dachatop.dto.place.dacha.DachaFilterDTO;
import uz.dachatop.dto.place.dacha.DachaInfoDTO;
import uz.dachatop.dto.profile.ProfileAndRoleResponseDTO;
import uz.dachatop.dto.profile.ProfileRoleDTO;
import uz.dachatop.dto.response.ApiResponse;
import uz.dachatop.entity.place.dacha.DachaEntity;
import uz.dachatop.enums.AppLanguage;
import uz.dachatop.enums.GlobalStatus;
import uz.dachatop.enums.ProfileRole;
import uz.dachatop.exp.AppBadRequestException;
import uz.dachatop.exp.ItemNotFoundException;
import uz.dachatop.mapper.dacha.DachaMapperDTO;
import uz.dachatop.repository.place.dacha.DachaCustomFilter;
import uz.dachatop.repository.place.dacha.DachaRepository;
import uz.dachatop.service.DachaNumberService;
import uz.dachatop.service.TerritoryService;
import uz.dachatop.service.place.PlaceAttachService;
import uz.dachatop.service.place.PlaceCalendarService;
import uz.dachatop.service.place.PlaceConvenienceService;
import uz.dachatop.service.place.PlaceService;
import uz.dachatop.telegrambot.service.TelegramBotService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DachaService {
    private final DachaRepository dachaRepository;
    private final PlaceConvenienceService placeConvenienceService;
    private final PlaceCalendarService placeCalendarService;
    private final PlaceAttachService placeAttachService;
    private final DachaCustomFilter dachaCustomFilter;
    private final PlaceService placeService;
    private final DachaNumberService dachaNumberService;
    private final TerritoryService territoryService;
    private final TelegramBotService telegramBotService;

    public ApiResponse<String> create(DachaCreateDTO dto) {
        // TODO check for required fields
        DachaEntity dacha = PlaceService.toEntity(new DachaEntity(), dto);
        toEntity(dacha, dto);
        dacha.setStatus(GlobalStatus.NOT_ACTIVE);
        if (Optional.ofNullable(dto.getTerritoryId()).isPresent()) {
            territoryService.getOne(dto.getTerritoryId());
            dacha.setTerritoryId(dto.getTerritoryId());
        }

        dacha.setProfileId(EntityDetails.getCurrentProfileId());
        dacha.setNumber(dachaNumberService.save(null));

        // save
        dachaRepository.save(dacha);
        // add image
        placeAttachService.mergePlaceAttach(dacha.getId(), dto.getAttachList());
        // send message to admin from telegram bot
        new Thread(() -> {
            telegramBotService.sendMessageForAdminAboutPlaces(dto, dacha.getId());
        }).start();
        // add convenience
        placeConvenienceService.mergeConvenienceList(dacha.getId(), dto.getConvenienceList());
        // add calendar
        placeCalendarService.merge(dacha.getId(), dto.getCalendarList());
        return ApiResponse.ok();
    }

    public ApiResponse<String> update(String dachaId, DachaCreateDTO dto) {
        // check  for requirement fields
        DachaEntity dacha = get(dachaId);

        List<ProfileRole> profileRoles = Objects.requireNonNull(EntityDetails.getAdminEntity()).getRoleList()
                .stream().map(ProfileRoleDTO::getRole).toList();

        if (!EntityDetails.getCurrentProfileId()
                .equals(dacha.getProfileId()) && !profileRoles.contains(ProfileRole.ROLE_ADMIN)) {
            throw new AppBadRequestException("You are not owner");
        }

        if (Optional.ofNullable(dto.getTerritoryId()).isPresent()) {
            territoryService.getOne(dto.getTerritoryId());
            dacha.setTerritoryId(dto.getTerritoryId());
        }

        if (profileRoles.contains(ProfileRole.ROLE_ADMIN) &&
                dto.getNumber() != null && dacha.getNumber() != null &&
                !dacha.getNumber().equals(dto.getNumber())) {
            long oldNumber = dacha.getNumber();
            dacha.setNumber(dachaNumberService.save(dto.getNumber()));
            dachaNumberService.delete(oldNumber);
        }

        PlaceService.toEntityForUpdate(dacha, dto);
        toEntity(dacha, dto);
        // update
        dachaRepository.save(dacha);
        // add image
        placeAttachService.mergePlaceAttach(dacha.getId(), dto.getAttachList());
        // add convenience
        placeConvenienceService.mergeConvenienceList(dacha.getId(), dto.getConvenienceList());
        // add calendar
        placeCalendarService.merge(dacha.getId(), dto.getCalendarList());
        return ApiResponse.ok();
    }

    private Boolean checkRequirementFields(DachaCreateDTO dto) {
        return true;
    }

    public ApiResponse<Page<DachaInfoDTO>> filter_asClient(DachaFilterDTO filter, int page, int size, AppLanguage lang) {
        PlaceFilterResult<DachaMapperDTO> filterResult = dachaCustomFilter
                .filter(filter, lang, page * size, size);

        List<DachaInfoDTO> dtoList = filterResult.getContent().stream()
                .map(this::toInfoDTO).toList();

        Page<DachaInfoDTO> pageObj = new PageImpl<>(dtoList, PageRequest.of(page, size), filterResult.getTotalCount());

        return ApiResponse.ok(pageObj);
    }

    public ApiResponse<DachaDTO> getDachaById(String dachaId, AppLanguage lang) {
        Optional<DachaEntity> optional = dachaRepository.findByIdAndVisibleTrue(dachaId);
        if (optional.isEmpty()) {
            return ApiResponse.bad("Item not found");
        }
        DachaDTO dto = toDetailDTO(optional.get(), lang);
        return ApiResponse.ok(dto);
    }

    public ApiResponse<DachaDTO> getDachaByNumber(Long number, AppLanguage lang) {
        Optional<DachaEntity> optional = dachaRepository.findByNumberAndVisibleIsTrue(number);
        if (optional.isEmpty()) {
            return ApiResponse.bad("Item not found");
        }
        DachaDTO dto = toDetailDTO(optional.get(), lang);
        return ApiResponse.ok(dto);
    }

    public ApiResponse<?> deleteApartment(String id, AppLanguage lang) {
        //TODO only admin or owner.
        DachaEntity entity = get(id);
        String profileId = EntityDetails.getCurrentProfileId();
        List<ProfileRole> profileRoles = Objects.requireNonNull(EntityDetails.getAdminEntity()).getRoleList()
                .stream().map(ProfileRoleDTO::getRole).toList();
        if (!entity.getProfileId().equals(profileId)
                && !profileRoles.contains(ProfileRole.ROLE_ADMIN)) {
            return ApiResponse.bad("You are not owner. Mazgi.");
        }
        dachaRepository.deleteApartment(id, profileId, LocalDateTime.now());
        return ApiResponse.ok();
    }

    public ApiResponse<?> changeStatus(String dachaId, GlobalStatus status) {
        DachaEntity entity = get(dachaId);
        List<ProfileRole> profileRoles = Objects.requireNonNull(EntityDetails.getAdminEntity()).getRoleList()
                .stream().map(ProfileRoleDTO::getRole).toList();
        if ((entity.getAdminChecked() != null && !entity.getAdminChecked()) && !profileRoles.contains(ProfileRole.ROLE_ADMIN)) {
            return ApiResponse.bad("Dacha not Active");
        }
        String profileId = EntityDetails.getCurrentProfileId();
        dachaRepository.changeStatus(dachaId, profileId, status, LocalDateTime.now(),profileRoles.contains(ProfileRole.ROLE_ADMIN));
        return ApiResponse.ok();
    }

    public DachaEntity get(String id) {
        return dachaRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Dacha Not Found"));
    }

    // user dacha list
    // admin filter

    private void toEntity(DachaEntity entity, DachaCreateDTO dto) {
        // main information
        entity.setName(dto.getName());
        entity.setSalePrice(dto.getSalePrice());
        entity.setSubType(dto.getSubType());
        entity.setRoomCount(dto.getRoomCount());
        entity.setTotalArea(dto.getTotalArea()); // общее помощение
        entity.setSingleBedRoomCount(dto.getSingleBedRoomCount()); // количество одноместных спальный
        entity.setDoubleBedRoomCount(dto.getDoubleBedRoomCount());  // количество двухместный спальный
        //price
        entity.setWeekDayPrice(dto.getWeekDayPrice()); // обычная цена за сутки
        entity.setPriceOnSale(dto.getPriceOnSale()); // сумма со скидкой
        entity.setWeekendPrice(dto.getWeekendPrice()); // цена по выходным за сутки
        entity.setGageOfDeposit(dto.getGageOfDeposit()); // процент залога
        //time
        entity.setEnterTime(dto.getEnterTime());
        entity.setDepartureTime(dto.getDepartureTime());
        //Book type
        entity.setMaximumDayBooking(dto.getMaximumDayBooking());
        entity.setMinimumDayBooking(dto.getMinimumDayBooking());
        // card
        entity.setProfileCardPan(dto.getProfileCardPan());
    }

    private DachaDTO toDetailDTO(DachaEntity entity, AppLanguage language) {
        DachaDTO dto = placeService.toDetailDTO(new DachaDTO(), entity, language);
        // main information
        dto.setName(entity.getName());
        dto.setSalePrice(entity.getSalePrice());
        dto.setSubType(entity.getSubType());
        dto.setRoomCount(entity.getRoomCount());
        dto.setTotalArea(entity.getTotalArea()); // общее помощение
        dto.setSingleBedRoomCount(entity.getSingleBedRoomCount()); // количество одноместных спальный
        dto.setDoubleBedRoomCount(entity.getDoubleBedRoomCount());  // количество двухместный спальный
        //price
        dto.setWeekDayPrice(entity.getWeekDayPrice()); // обычная цена за сутки
        dto.setPriceOnSale(entity.getPriceOnSale()); // сумма со скидкой
        dto.setWeekendPrice(entity.getWeekendPrice()); // цена по выходным за сутки
        dto.setGageOfDeposit(entity.getGageOfDeposit()); // процент залога
        //time
        dto.setEnterTime(entity.getEnterTime());
        dto.setDepartureTime(entity.getDepartureTime());
        //Book type
        dto.setMaximumDayBooking(entity.getMaximumDayBooking());
        dto.setMinimumDayBooking(entity.getMinimumDayBooking());
        //Rules
        dto.setSmoking(entity.isSmoking());
        dto.setAlcohol(entity.isAlcohol());
        dto.setPets(entity.isPets());
        dto.setAvailableOnlyFamily(entity.isAvailableOnlyFamily());
        dto.setLoudlyMusic(entity.isLoudlyMusic());
        dto.setParty(entity.isParty());
        // card
        dto.setProfileCardPan(entity.getProfileCardPan());
        // convenienceList
        dto.setConvenienceList(placeConvenienceService.getPlaceConvenienceList(entity.getId(), language));
        // calendar
        dto.setCalendarList(placeCalendarService.getCalendarListByPlaceId(entity.getId()));
        // additional info
        dto.setDescription(entity.getDescription());
        dto.setContact(entity.getContact());
        dto.setNumber(entity.getNumber());
        return dto;
    }

    private DachaInfoDTO toInfoDTO(DachaMapperDTO mapper) {
        DachaInfoDTO infoDTO = placeService.toInfoDTO(new DachaInfoDTO(), mapper);
        infoDTO.setWeekDayPrice(mapper.getWeekDayPrice());
        infoDTO.setWeekendPrice(mapper.getWeekendPrice());
        infoDTO.setTotalArea(mapper.getTotalArea());
        infoDTO.setSingleBedRoomCount(mapper.getSingleBedRoomCount());
        infoDTO.setDoubleBedRoomCount(mapper.getDoubleBedRoomCount());
        infoDTO.setName(mapper.getDachaName());
        infoDTO.setCreatedDate(mapper.getCreatedDate());
        infoDTO.setNumber(mapper.getNumber());
        infoDTO.setSubType(mapper.getSubType());
        infoDTO.setSalePrice(mapper.getSalePrice());
        infoDTO.setLatitude(mapper.getLatitude());
        infoDTO.setLongitude(mapper.getLongitude());
        infoDTO.setTariffResult(mapper.getTariffResult());
        return infoDTO;
    }
}
