package uz.dachatop.repository.place.hotel;

import org.springframework.data.repository.CrudRepository;
import uz.dachatop.entity.place.hotel.HotelRoomEntity;

import java.util.List;

public interface HotelRoomRepository extends CrudRepository<HotelRoomEntity, String> {
    List<HotelRoomEntity> findAllByHotelId(String hotelId);
}
