package uz.dachatop.service.place;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import uz.dachatop.dto.convenience.ConvenienceDTO;
import uz.dachatop.entity.place.PlaceConvenienceEntity;
import uz.dachatop.enums.AppLanguage;
import uz.dachatop.mapper.ConvenientNameMapper;
import uz.dachatop.repository.place.PlaceConvenienceRepository;

import java.util.LinkedList;
import java.util.List;

@Service
@AllArgsConstructor
public class PlaceConvenienceService {

    private final PlaceConvenienceRepository placeConvenienceRepository;

    public void mergeConvenienceList(String apartmentId, List<ConvenienceDTO> newList) {
        List<Long> oldList = placeConvenienceRepository.getConvenienceListByPlaceId(apartmentId);
        if (newList == null) {
            oldList.forEach(convenienceId -> {
                placeConvenienceRepository.deleteByPlaceIdAndConvenienceId(apartmentId, convenienceId);
            });
            return;
        }
        newList.forEach(convenienceDTO -> {
            if (!oldList.contains(convenienceDTO.getId())) {
                cratePlaceConvenience(apartmentId, convenienceDTO.getId());
            }
        });
        oldList.forEach(convenienceId -> {
            if (newList.stream().filter(convenienceDTO -> convenienceDTO.getId().equals(convenienceId)).findAny().isEmpty()) {
                placeConvenienceRepository.deleteByPlaceIdAndConvenienceId(apartmentId, convenienceId);
            }
        });
    }

    public void cratePlaceConvenience(String placeId, Long convenienceId) {
        PlaceConvenienceEntity entity = new PlaceConvenienceEntity();
        entity.setPlaceId(placeId);
        entity.setConvenienceId(convenienceId);
        placeConvenienceRepository.save(entity);
    }

    public List<ConvenienceDTO> getPlaceConvenienceList(String placeId, AppLanguage language) {
        List<ConvenientNameMapper> convenienceList = placeConvenienceRepository.getApartmentConvenientList(placeId, language.name());
        List<ConvenienceDTO> convenienceDTOList = new LinkedList<>();
        convenienceList.forEach(mapper -> {
            convenienceDTOList.add(new ConvenienceDTO(mapper.getConvenientId(), mapper.getConvenientName()));
        });
        return convenienceDTOList;
    }

    public void deleteAllByPlaceId(String placeId) {
        placeConvenienceRepository.deleteByPlaceId(placeId);
    }
}
