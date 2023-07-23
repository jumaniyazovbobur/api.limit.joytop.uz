package uz.dachatop.service.place;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import uz.dachatop.dto.attach.AttachDTO;
import uz.dachatop.dto.place.dacha.DachaCreateDTO;
import uz.dachatop.entity.AttachEntity;
import uz.dachatop.entity.place.PlaceAttachEntity;
import uz.dachatop.repository.PlaceAttachRepository;
import uz.dachatop.service.AttachServer;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
@AllArgsConstructor
public class PlaceAttachService {
    private PlaceAttachRepository placeAttachRepository;
    private AttachServer attachServer;

//    @Value("${attach.upload.folder}")
//    private String attachFolder;


    public void mergePlaceAttach(String apartmentId, List<AttachDTO> newList) {
        List<String> oldList = placeAttachRepository.getAttachListByPlace(apartmentId);
        if (newList == null) {
            oldList.forEach(attachId -> placeAttachRepository.deleteByPlaceIdAndAttachId(apartmentId, attachId));
            return;
        }
        newList.forEach(attachDTO -> {
            if (!oldList.contains(attachDTO.getId())) {
                addPlaceToApartment(apartmentId, attachDTO.getId());
            }
        });
        oldList.forEach(attachId -> {
            if (newList.stream().filter(attachDTO -> attachDTO.getId().equals(attachId)).findAny().isEmpty()) {
                placeAttachRepository.deleteByPlaceIdAndAttachId(apartmentId, attachId);
            }
        });
    }

    public void addPlaceToApartment(String placeId, String attachId) {
        PlaceAttachEntity entity = new PlaceAttachEntity();
        entity.setAttachId(attachId);
        entity.setPlaceId(placeId);
        placeAttachRepository.save(entity);
    }

    public List<AttachDTO> getPlaceAttachList(String placeId) {
        List<AttachDTO> dtoList = new LinkedList<>();
        placeAttachRepository.getAttachListByPlace(placeId).forEach(attachId -> {
            dtoList.add(attachServer.webContentLink(attachId));
        });
        return dtoList;
    }

    public void deleteAllByPlaceId(String placeId) {
        placeAttachRepository.deleteByPlaceId(placeId);
    }

    public List<InputMedia> getPlaceAttachListId(String placeId , DachaCreateDTO dto) {
        List<InputMedia> media = new ArrayList<>();
        List<AttachEntity> list = placeAttachRepository.getAttachListByPlaceId(placeId);
        for (AttachEntity s : list) {
            InputMedia media1 = new InputMediaPhoto();

            String pathFolder = s.getPath() + "/" + s.getId() + "." + s.getExtension();

            Path path = Paths.get("uploads/"+ pathFolder);
            File file = new File(path.toUri());

            media1.setMedia(file,s.getId());
            media.add(media1);
        }
        return media;
    }
}
