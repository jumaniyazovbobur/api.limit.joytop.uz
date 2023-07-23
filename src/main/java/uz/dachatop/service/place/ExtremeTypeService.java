package uz.dachatop.service.place;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import uz.dachatop.dto.request.ExtremeTypeReqDTO;
import uz.dachatop.dto.response.ApiResponse;
import uz.dachatop.dto.response.place.ExtremeTypeResDTO;
import uz.dachatop.entity.place.extreme.ExtremeTypeEntity;
import uz.dachatop.enums.AppLanguage;
import uz.dachatop.repository.place.extreme.ExtremeTypeRepository;

import java.util.List;

/**
 * @author 'Mukhtarov Sarvarbek' on 24.02.2023
 * @project api.dachatop
 * @contact @sarvargo
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ExtremeTypeService {

    private final ExtremeTypeRepository extremeTypeRepository;

    public ApiResponse<ExtremeTypeResDTO> save(final ExtremeTypeReqDTO payload, AppLanguage language) {

        ExtremeTypeEntity entity = extremeTypeRepository
                .save(new ExtremeTypeEntity(payload.getNameUz(), payload.getNameEn(), payload.getNameRu()));


        return ApiResponse.ok(new ExtremeTypeResDTO(entity.getId(), getNameByLanguage(entity, language)));
    }

    public ApiResponse<ExtremeTypeResDTO> update(final String id,
                                                 final ExtremeTypeReqDTO payload,
                                                 AppLanguage language) {

        ExtremeTypeEntity extremeType = findById(id);

        if (extremeType == null) {
            log.warn("Extreme Type not found! name = {}", payload.getNameEn());
            return ApiResponse.bad("Extreme Type not found!");
        }

        extremeType.setNameUz(payload.getNameUz());
        extremeType.setNameRu(payload.getNameRu());
        extremeType.setNameEn(payload.getNameEn());

        ExtremeTypeEntity entity = extremeTypeRepository
                .save(extremeType);

        return ApiResponse.ok(new ExtremeTypeResDTO(entity.getId(), getNameByLanguage(entity, language)));
    }

    public ApiResponse<?> delete(final String id) {

        ExtremeTypeEntity name = findById(id);

        if (name == null) {
            log.warn("Extreme Type not found! id = {}", id);
            return ApiResponse.bad("Extreme Type not found!");
        }

        extremeTypeRepository.updateVisible(Boolean.FALSE, id);

        return ApiResponse.ok("Success");
    }

    public PageImpl<ExtremeTypeResDTO> findAllByPagination(int page, int size) {
        Pageable pageable = PageRequest
                .of(page, size, Sort
                        .by(Sort.Direction.ASC,
                                "createdDate"));

        Page<ExtremeTypeEntity> entityPage = extremeTypeRepository
                .findAllByVisibleIsTrue(pageable);


        return new PageImpl<>(entityPage
                .stream()
                .map(entity -> new ExtremeTypeResDTO(entity.getId(), entity.getNameUz(), entity.getNameRu(), entity.getNameEn()))
                .toList(), pageable, entityPage.getTotalElements());
    }

    public List<ExtremeTypeResDTO> findAll(AppLanguage language) {

        List<ExtremeTypeEntity> entityPage = extremeTypeRepository.findAllByVisibleIsTrue();

        return entityPage
                .stream()
                .map(entity ->
                        new ExtremeTypeResDTO(entity.getId(), getNameByLanguage(entity, language)))
                .toList();
    }

    private String getNameByLanguage(ExtremeTypeEntity entity, AppLanguage language) {
        return switch (language) {
            case ru -> entity.getNameRu();
            case en -> entity.getNameEn();
            default -> entity.getNameUz();
        };
    }



    public ExtremeTypeEntity findById(final String id) {
        return extremeTypeRepository
                .findByIdAndVisibleIsTrue(id)
                .orElse(null);
    }
    public ExtremeTypeResDTO findById(final String id,AppLanguage lang) {
        ExtremeTypeEntity extremeTypeEntity = extremeTypeRepository
                .findByIdAndVisibleIsTrue(id)
                .orElse(null);
        ExtremeTypeResDTO res = new ExtremeTypeResDTO();
        res.setId(extremeTypeEntity.getId());
        res.setName(getNameByLanguage(extremeTypeEntity,lang));
        return res;
    }
}
