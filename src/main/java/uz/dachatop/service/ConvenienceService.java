package uz.dachatop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.dachatop.dto.convenience.ConvenienceCreateDTO;
import uz.dachatop.dto.convenience.ConvenienceDTO;
import uz.dachatop.dto.response.ApiResponse;
import uz.dachatop.entity.ConvenienceEntity;
import uz.dachatop.enums.AppLanguage;
import uz.dachatop.exp.ItemNotFoundException;
import uz.dachatop.repository.ConvenienceRepository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConvenienceService {

    private final ConvenienceRepository convenienceRepository;

    public ApiResponse<ConvenienceDTO> addConvenience(ConvenienceCreateDTO dto) {
        ConvenienceEntity con = new ConvenienceEntity();
        con.setNameEn(dto.getNameEn());
        con.setNameUz(dto.getNameUz());
        con.setNameRu(dto.getNameRu());
        convenienceRepository.save(con);
        return new ApiResponse<>(200, false, toDTO(con));
    }

    public ApiResponse<ConvenienceDTO> updateConvenience(Long id, ConvenienceCreateDTO dto) {
        ConvenienceEntity fromDb = convenienceRepository.findByIdAndVisibleTrue(id).orElseThrow(() ->
                new ItemNotFoundException("Convenience not found: " + id));
        fromDb.setNameRu(dto.getNameRu());
        fromDb.setNameRu(dto.getNameEn());
        fromDb.setNameUz(dto.getNameUz());
        convenienceRepository.save(fromDb);
        return new ApiResponse<>(200, false, toDTO(fromDb));
    }

    public ApiResponse<Boolean> delete(Long id) {
        int i = convenienceRepository.deleteStatus(false, id);
        return new ApiResponse<Boolean>(200, false, i > 0);

    }

    public ApiResponse<ConvenienceDTO> getOne(Long id) {
        return new ApiResponse<>(200, false, toDTOLanguage(convenienceRepository.findByIdAndVisibleTrue(id).orElseThrow(
                () -> new ItemNotFoundException("Convenience not found: " + id))));
    }

    public ApiResponse<List<ConvenienceDTO>> findAll(AppLanguage lang) {
        List<ConvenienceDTO> result = new ArrayList<>();
        Iterable<ConvenienceEntity> all = convenienceRepository.findAllByVisibleTrue();
        all.forEach(convenience -> {
            result.add(ConvenienceDTO.getByLanguage(convenience, lang));
        });
        return new ApiResponse<>(200, false, result);
    }

    public ApiResponse<List<ConvenienceDTO>> findAllForAdmin() {
        List<ConvenienceDTO> result = new ArrayList<>();
        Iterable<ConvenienceEntity> all = convenienceRepository.findAllByVisibleTrue();
        all.forEach(convenience -> {
            ConvenienceDTO dto = new ConvenienceDTO();
            dto.setId(convenience.getId());
            dto.setNameEn(convenience.getNameEn());
            dto.setNameRu(convenience.getNameRu());
            dto.setNameUz(convenience.getNameUz());
            result.add(dto);
        });
        return new ApiResponse<>(200, false, result);
    }

    public ConvenienceDTO toDTO(ConvenienceEntity convenience) {
        ConvenienceDTO dto = new ConvenienceDTO();
        dto.setId(convenience.getId());
        dto.setName(convenience.getNameUz());

        return dto;
    }

    public ConvenienceDTO toDTOLanguage(ConvenienceEntity convenience) {
        ConvenienceDTO dto = new ConvenienceDTO();
        dto.setId(convenience.getId());
        dto.setNameUz(convenience.getNameUz());
        dto.setNameRu(convenience.getNameRu());
        dto.setNameEn(convenience.getNameEn());

        return dto;
    }

}

