package uz.dachatop.service;
//PROJECT NAME --> api.dachatop
// YEAR --> 2023
// MONTH --> 03
// DAY --> 28
// TIME --> 10:03

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import uz.dachatop.dto.district.DistrictPaginationDTO;
import uz.dachatop.dto.territory.TerritoryPaginationDTO;
import uz.dachatop.dto.district.DistrictDTO;
import uz.dachatop.dto.response.ApiResponse;
import uz.dachatop.dto.territory.TerritoryDTO;
import uz.dachatop.entity.DistrictEntity;
import uz.dachatop.entity.TerritoryEntity;
import uz.dachatop.enums.AppLanguage;
import uz.dachatop.exp.GlobalException;
import uz.dachatop.exp.ItemNotFoundException;
import uz.dachatop.repository.TerritoryRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TerritoryService {
    private final TerritoryRepository repository;


    public ApiResponse<TerritoryDTO> add(TerritoryDTO form) {
        TerritoryEntity territoryEntity = new TerritoryEntity();
        if (form.getNameUz().isEmpty()) {
            throw new GlobalException("Uzbek name is Empty !!!");
        }
        if (form.getNameRu().isEmpty()) {
            throw new GlobalException("Russian name is Empty !!!");
        }
        if (form.getNameEn().isEmpty()) {
            throw new GlobalException("English name is Empty !!!");
        }
        territoryEntity.setNameUz(form.getNameUz());
        territoryEntity.setNameRu(form.getNameRu());
        territoryEntity.setNameEn(form.getNameEn());
        territoryEntity.setVisible(true);
        territoryEntity.setDistrictId(form.getDistrictId());
        repository.save(territoryEntity);
        return new ApiResponse<>(200, false, TerritoryDTO.toDto(territoryEntity));


    }

    public ApiResponse<TerritoryDTO> update(Long id, TerritoryDTO form) {
        TerritoryEntity territoryEntity = repository.findById(id).orElseThrow(() ->
                new ItemNotFoundException("Territory Id not found" + id));
        territoryEntity.setNameUz(form.getNameUz());
        territoryEntity.setNameRu(form.getNameRu());
        territoryEntity.setNameEn(form.getNameEn());
        repository.save(territoryEntity);
        return new ApiResponse<>(200, false, TerritoryDTO.toDto(territoryEntity));
    }

    public ApiResponse<Boolean> delete(Long id) {
        int i = repository.deleteStatus(false, id);
        return new ApiResponse<Boolean>(200, false, i > 0);
    }

    public ApiResponse<TerritoryDTO> getOne(Long id) {
        return new ApiResponse<>(200, false, TerritoryDTO.toDto(repository.findById(id).orElseThrow(() ->
                new ItemNotFoundException("Territory id is not found" + id))));
    }


    public ApiResponse<List<TerritoryDTO>> getTerritoryByDistrictId(Long districtId, AppLanguage language) {
        List<TerritoryDTO> responseDTOList = new ArrayList<>();

        repository.findByDistrictIdAndVisibleTrue(districtId).forEach(district -> {
            responseDTOList.add((TerritoryDTO.toInfoDto(district.getId(), district.getNameUz(), district.getNameEn(), district.getNameRu(), language)));
        });
        return new ApiResponse<>(200, false, responseDTOList);
    }

    public TerritoryPaginationDTO getAllForAdmin(Long id, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.Direction.ASC, "id");

        Page<TerritoryEntity> all = repository.getAllByVisibleTrueAndDistrictId(id, pageable);
        long totalElements = all.getTotalElements();
        int totalPages = all.getTotalPages();

        List<TerritoryDTO> dtoList = all.stream().map(this::toDTO).toList();
        return new TerritoryPaginationDTO(totalElements, totalPages, dtoList);
    }


    private TerritoryDTO toDTO(TerritoryEntity entity) {
        TerritoryDTO dto = new TerritoryDTO();
        dto.setId(entity.getId());
        dto.setNameUz(entity.getNameUz());
        dto.setNameEn(entity.getNameEn());
        dto.setNameRu(entity.getNameRu());
        dto.setDistrictId(entity.getDistrictId());
        dto.setVisible(entity.getVisible());
        dto.setCounty(entity.getCounty());
        return dto;
    }
}
