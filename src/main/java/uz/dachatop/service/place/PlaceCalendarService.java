package uz.dachatop.service.place;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.dachatop.dto.place.calendar.PlaceCalendarDTO;
import uz.dachatop.entity.place.PlaceCalendarEntity;
import uz.dachatop.enums.BookingStatus;
import uz.dachatop.repository.PlaceCalendarRepository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceCalendarService {

    private final PlaceCalendarRepository calendarRepository;

    public void merge(String apartmentId, List<PlaceCalendarDTO> calendarList) {
        if (calendarList == null || calendarList.isEmpty()) {
            return;
        }
        // [8,10,11]
        //
        for (PlaceCalendarDTO dto : calendarList) {
            PlaceCalendarEntity entity = calendarRepository
                    .findByPlaceIdAndCalDateAndVisibleTrue(apartmentId, dto.getDate());
            if (entity == null) {
                create(apartmentId, dto);
            } else {
                update(apartmentId, dto);
            }
        }

        List<LocalDate> list = calendarList.stream().map(PlaceCalendarDTO::getDate).toList();
        update(apartmentId, list, BookingStatus.AVAILABLE); // set status available when dates doesn't like in db
    }

    private void create(String apartmentId, PlaceCalendarDTO dto) {
        PlaceCalendarEntity entity = new PlaceCalendarEntity();
        entity.setPlaceId(apartmentId);
        entity.setCalDate(dto.getDate());
        entity.setStatus(dto.getStatus());
        calendarRepository.save(entity);
    }

    public List<PlaceCalendarDTO> getCalendarListByPlaceId(String apartmentId) {
        LocalDate date = LocalDate.now();
        List<PlaceCalendarEntity> entityList = calendarRepository
                .findByApartmentIdAndVisibleTrue(apartmentId, date.getYear(), date.getMonthValue());
        Map<Integer, PlaceCalendarEntity> entityMap = getMap(entityList);
        YearMonth yearMonthObject = YearMonth.of(date.getYear(), date.getMonthValue());
        int daysInMonth = yearMonthObject.lengthOfMonth();
        List<PlaceCalendarDTO> list = new ArrayList<>();
        for (int i = 1; i <= daysInMonth; i++) {
            if (entityMap.containsKey(i)) {
                PlaceCalendarEntity entity = entityMap.get(i);
                list.add(new PlaceCalendarDTO(entity.getCalDate(), entity.getStatus()));
            } else {
                list.add(new PlaceCalendarDTO(LocalDate.of(date.getYear(), date.getMonthValue(), i), BookingStatus.AVAILABLE));
            }
        }
        // bu mrtod hozirgi oydagi apartmentni calendarlarini jo'natadi.
        // kun bo'yicha order qilib
        // Agar oyning qaysidir kuni uchun calendar berilmagan bo'lsa,
        // uni o'zimiz yaratmiz (db da emas) va listga joylaymiz. Statusi  AVAILABLE bo'lsin.

        // Xullas method shu oy bo'yicha qaysi kunlar ochiq qaysi kunlar bant ekanligini jo'natadi.
        // Agar qaysidir kun belgilanmagan bo'lsa  Statusi  AVAILABLE qilib jo'natadi
        return list;
    }

    private Map<Integer, PlaceCalendarEntity> getMap(List<PlaceCalendarEntity> list) {
        Map<Integer, PlaceCalendarEntity> entityMap = new HashMap<>();
        list.forEach(entity -> {
            entityMap.put(entity.getCalDate().getDayOfMonth(), entity);
        });
        return entityMap;
    }

    private void update(String apartmentId, PlaceCalendarDTO dto) {
        calendarRepository.update(apartmentId, dto.getDate(), dto.getStatus());
    }

    private void update(String apartmentId, List<LocalDate> dates, BookingStatus status) {
        calendarRepository.update(apartmentId, dates, status);
    }
}
