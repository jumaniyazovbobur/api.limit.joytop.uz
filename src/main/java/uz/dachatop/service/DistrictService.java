package uz.dachatop.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import uz.dachatop.dto.district.DistrictDTO;
import uz.dachatop.dto.district.DistrictPaginationDTO;
import uz.dachatop.dto.response.ApiResponse;
import uz.dachatop.entity.DistrictEntity;
import uz.dachatop.enums.AppLanguage;
import uz.dachatop.exp.ItemNotFoundException;
import uz.dachatop.repository.DistrictRepository;
import uz.dachatop.repository.RegionRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DistrictService {

    private final DistrictRepository repository;
    private final EntityManager manager;
    public final RegionRepository region;

    public ApiResponse<DistrictDTO> add(DistrictDTO form) {
        DistrictEntity districtEntity = new DistrictEntity();
        districtEntity.setNameUz(form.getNameUz());
        districtEntity.setNameRu(form.getNameRu());
        districtEntity.setNameEn(form.getNameEn());
        districtEntity.setVisible(true);
        districtEntity.setRegionId(form.getRegionId());
        repository.save(districtEntity);
        return new ApiResponse<>(200, false, DistrictDTO.toDto(districtEntity));
    }

    public ApiResponse<DistrictDTO> update(Long id, DistrictDTO form) {
        DistrictEntity districtEntity = repository.findById(id).orElseThrow(() ->
                new ItemNotFoundException("District Id not found" + id));
        districtEntity.setNameUz(form.getNameUz());
        districtEntity.setNameRu(form.getNameRu());
        districtEntity.setNameEn(form.getNameEn());
        repository.save(districtEntity);
        return new ApiResponse<>(200, false, DistrictDTO.toDto(districtEntity));
    }

    public ApiResponse<DistrictDTO> getOne(Long id) {
        return new ApiResponse<>(200, false, DistrictDTO.toDto(repository.findById(id).orElseThrow(() ->
                new ItemNotFoundException("District id is not found" + id))));
    }

    public ApiResponse<Boolean> delete(Long id) {
        int i = repository.deleteStatus(false, id);
        return new ApiResponse<Boolean>(200, false, i > 0);
        // TODO do an ours delete method
    }

    public DistrictPaginationDTO getAllForAdmin(Long id, int page, int size) {

        PageRequest pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdDate");

        Page<DistrictEntity> all = repository.getAllByVisibleTrueAndRegionId(id, pageable);

        long totalElements = all.getTotalElements();
        int totalPages = all.getTotalPages();

        List<DistrictDTO> dtoList = all.stream().map(this::toDTO).toList();
        return new DistrictPaginationDTO(totalElements, totalPages, dtoList);
    }

    public ApiResponse<List<DistrictDTO>> getDistrictByRegionId(Long regionId, AppLanguage language) {
        List<DistrictDTO> responseDTOList = new ArrayList<>();

        repository.findByRegionIdAndVisibleTrue(regionId).forEach(district -> {
            responseDTOList.add((DistrictDTO.toInfoDto(district.getId(), district.getNameUz(), district.getNameEn(), district.getNameRu(), language)));
        });
        return new ApiResponse<>(200, false, responseDTOList);
    }

    private DistrictDTO toDTO(DistrictEntity entity) {
        DistrictDTO dto = new DistrictDTO();
        dto.setId(entity.getId());
        dto.setNameUz(entity.getNameUz());
        dto.setNameEn(entity.getNameEn());
        dto.setNameRu(entity.getNameRu());
        dto.setRegionId(entity.getRegionId());
        dto.setVisible(entity.getVisible());
        dto.setCounty(entity.getCounty());
        return dto;
    }

    public DistrictDTO getById(Long id, AppLanguage language) {
        DistrictEntity entity = null;
        if (id != null) {
            entity = repository.findById(id).orElse(null);
        }
        if (entity != null) {
            DistrictDTO dto = new DistrictDTO();
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
}
