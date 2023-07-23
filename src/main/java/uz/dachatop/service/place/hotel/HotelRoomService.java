package uz.dachatop.service.place.hotel;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import uz.dachatop.dto.place.hotel.HotelRoomDTO;
import uz.dachatop.entity.place.hotel.HotelRoomEntity;
import uz.dachatop.enums.AppLanguage;
import uz.dachatop.repository.place.hotel.HotelRoomRepository;
import uz.dachatop.service.place.PlaceAttachService;
import uz.dachatop.service.place.PlaceConvenienceService;

import java.util.LinkedList;
import java.util.List;

@Service
@AllArgsConstructor
public class HotelRoomService {
    private final HotelRoomRepository hotelRoomRepository;
    private final PlaceConvenienceService placeConvenienceService;
    private final PlaceAttachService placeAttachService;

    public void merge(String hotelId, List<HotelRoomDTO> newRoomList) {
        List<HotelRoomEntity> oldList = hotelRoomRepository.findAllByHotelId(hotelId);
        if (oldList.isEmpty()) {
            if (newRoomList != null) {
                newRoomList.forEach(roomDTO -> create(hotelId, roomDTO));
            }
            return;
        }
        if (newRoomList == null) {
            oldList.forEach(room -> delete(room.getId()));
            return;
        }
        // create or remove
        newRoomList.forEach(roomDTO -> {
            if (roomDTO.getId() == null) {
                create(hotelId, roomDTO);
            } else {
                update(hotelId, roomDTO);
            }
        });
        // delete
        oldList.forEach(roomEntity -> {
            if (newRoomList.stream().noneMatch(roomDTO -> roomDTO.getId() != null && roomDTO.getId().equals(roomEntity.getId()))) {
                delete(roomEntity.getId());
            }
        });
    }

    public HotelRoomEntity create(String hotelId, HotelRoomDTO dto) {
        HotelRoomEntity entity = new HotelRoomEntity();
        entity.setRoomType(dto.getRoomType());
        entity.setTotalArea(dto.getTotalArea());
        entity.setDayPrice(dto.getDayPrice());
        entity.setPriceOnSale(dto.getPriceOnSale());
        entity.setHotelId(hotelId);
        // save
        hotelRoomRepository.save(entity);
        // convenience
        placeConvenienceService.mergeConvenienceList(entity.getId(), dto.getConvenienceList());
        // attach
        placeAttachService.mergePlaceAttach(entity.getId(), dto.getAttachList());
        return entity;
    }

    public HotelRoomEntity update(String hotelId, HotelRoomDTO dto) {
        HotelRoomEntity entity = new HotelRoomEntity();
        entity.setRoomType(dto.getRoomType());
        entity.setTotalArea(dto.getTotalArea());
        entity.setDayPrice(dto.getDayPrice());
        entity.setPriceOnSale(dto.getPriceOnSale());
        entity.setHotelId(hotelId);
        // save
        hotelRoomRepository.save(entity);
        // convenience
        placeConvenienceService.mergeConvenienceList(entity.getId(), dto.getConvenienceList());
        // attach
        placeAttachService.mergePlaceAttach(entity.getId(), dto.getAttachList());
        return entity;
    }

    public void delete(String roomId) {
        // delete room
        hotelRoomRepository.deleteById(roomId);
        // delete convenience
        placeConvenienceService.deleteAllByPlaceId(roomId);
        // delete convenience
        placeAttachService.deleteAllByPlaceId(roomId);
    }

    public List<HotelRoomDTO> getHotelRoomList(String hotelId, AppLanguage appLanguage) {
        List<HotelRoomEntity> roomList = hotelRoomRepository.findAllByHotelId(hotelId);
        List<HotelRoomDTO> hotelRoomDTOList = new LinkedList<>();
        for (HotelRoomEntity entity : roomList) {
            HotelRoomDTO dto = new HotelRoomDTO();
            dto.setId(entity.getId());
            dto.setRoomType(entity.getRoomType());
            dto.setTotalArea(entity.getTotalArea());
            dto.setDayPrice(entity.getDayPrice());
            dto.setPriceOnSale(entity.getPriceOnSale());
            dto.setConvenienceList(placeConvenienceService.getPlaceConvenienceList(entity.getId(), appLanguage));
            dto.setAttachList(placeAttachService.getPlaceAttachList(entity.getId()));
            hotelRoomDTOList.add(dto);
        }
        return hotelRoomDTOList;
    }
}
