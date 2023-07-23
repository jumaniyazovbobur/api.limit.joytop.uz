package uz.dachatop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import uz.dachatop.dto.region.RegionCreateDTO;
import uz.dachatop.dto.region.RegionDTO;
import uz.dachatop.dto.response.ApiResponse;
import uz.dachatop.entity.RegionEntity;
import uz.dachatop.enums.AppLanguage;
import uz.dachatop.exp.ItemNotFoundException;
import uz.dachatop.repository.RegionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegionService {

    private final RegionRepository repository;


    public ApiResponse<RegionDTO> add(RegionCreateDTO form) {
        RegionEntity regionEntity = new RegionEntity(form.getNameUz(), form.getNameRu(), form.getNameEn(), form.getCountryId());
        return new ApiResponse<>(200, false, toDTO(repository.save(regionEntity)));
    }

    public ApiResponse<RegionDTO> update(Long id, RegionCreateDTO form) {
        RegionEntity regionEntity = repository.findByIdAndVisibleTrue(id).orElseThrow(() ->
                new ItemNotFoundException("Region not found: " + id));

        regionEntity.setNameUz(form.getNameUz());
        regionEntity.setNameEn(form.getNameEn());
        regionEntity.setNameRu(form.getNameRu());
        repository.save(regionEntity);
        return new ApiResponse<>(200, false, toDTO(regionEntity));
    }

    public ApiResponse<RegionDTO> getOne(Long id) {
        return new ApiResponse<>(200, false, toDTO(repository.findById(id).orElseThrow(
                () -> new ItemNotFoundException("Region not found: " + id))));
    }

    public ApiResponse<Boolean> delete(Long id) {
        int i = repository.deleteStatus(false, id);
        return new ApiResponse<Boolean>(200, false, i > 0);
    }

    public ApiResponse<List<RegionDTO>> findAll(AppLanguage lang) {
        return new ApiResponse<>(200, false, repository.findAll(Sort.by(Sort.Direction.ASC, "id"))
                .stream()
                .map(regionEntity -> RegionDTO.toDto(regionEntity, lang))
                .collect(Collectors.toList()));

    }

    public ApiResponse<List<RegionDTO>> getAll() {
        List<RegionDTO> dtoList = new ArrayList<>();

        repository.findAllByVisibleTrue(Sort.by("id")).forEach(region -> {
            dtoList.add(toDTO(region));
        });

        return new ApiResponse<>(200, false, dtoList);
    }

    public ApiResponse<RegionEntity> update(Long id, RegionEntity regionEntity) {
        Optional<RegionEntity> byId = repository.findById(id);
        if (byId.isPresent()) {
            RegionEntity regionEntity1 = byId.get();
            regionEntity1.setNameRu(regionEntity.getNameRu());
            regionEntity1.setNameEn(regionEntity.getNameEn());
            regionEntity1.setNameUz(regionEntity.getNameUz());
            return new ApiResponse<>(200, false, repository.save(regionEntity1));
        } else return null;
    }

    private RegionDTO toDTO(RegionEntity entity) {
        RegionDTO dto = new RegionDTO();
        dto.setId(entity.getId());
        dto.setNameEn(entity.getNameEn());
        dto.setNameUz(entity.getNameUz());
        dto.setNameRu(entity.getNameRu());
        return dto;
    }

    public RegionDTO getById(Long id, AppLanguage language) {
        RegionEntity entity = repository.findById(id).orElse(null);
        if (entity != null) {
            RegionDTO dto = new RegionDTO();
            dto.setId(entity.getId());
            switch (language) {
                case en -> dto.setNameEn(entity.getNameEn());
                case ru -> dto.setNameRu(entity.getNameRu());
                default -> dto.setNameUz(entity.getNameUz());
            }
            return dto;
        }
        return null;
    }


    public ApiResponse<List<RegionDTO>> findByCountryId(Long countryId, AppLanguage language) {
        List<RegionDTO> dtoList = new ArrayList<>();
        repository.findByCountryIdAndVisibleTrue(countryId).forEach(regionEntity -> {
            dtoList.add(toDTO(regionEntity));
        });
        return new ApiResponse<>(200, false, dtoList);
    }
}
