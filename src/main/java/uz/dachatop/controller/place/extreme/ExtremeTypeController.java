package uz.dachatop.controller.place.extreme;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.dachatop.dto.request.ExtremeTypeReqDTO;
import uz.dachatop.dto.response.ApiResponse;
import uz.dachatop.dto.response.place.ExtremeTypeResDTO;
import uz.dachatop.enums.AppLanguage;
import uz.dachatop.service.place.ExtremeTypeService;

import javax.validation.Valid;
import java.util.List;

/**
 * @author 'Mukhtarov Sarvarbek' on 24.02.2023
 * @project api.dachatop
 * @contact @sarvargo
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/extreme-type")
@Tag(name = "Extreme Type")
public class ExtremeTypeController {

    private final ExtremeTypeService extremeTypeService;

    /**
     * PUBLIC
     */
    @GetMapping("/public")
    public ResponseEntity<List<ExtremeTypeResDTO>> getAll(@RequestHeader(value = "Accept-Language", defaultValue = "uz") String lang) {
        log.info("Get Extreme types ");
        return ResponseEntity.ok(extremeTypeService.findAll(AppLanguage.getLanguage(lang)));
    }

    /**
     * ADMIN
     */
    @PostMapping("")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<ExtremeTypeResDTO>> save(
            @RequestBody @Valid ExtremeTypeReqDTO payload,
            @RequestHeader(value = "Accept-Language", defaultValue = "uz") String lang) {

        log.info("Save Extreme type payload = {}", payload);
        return ResponseEntity.ok(extremeTypeService.save(payload, AppLanguage.getLanguage(lang)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<ExtremeTypeResDTO>> update(@PathVariable("id") String id,
                                                                 @RequestBody @Valid ExtremeTypeReqDTO payload,
                                                                 @RequestHeader(value = "Accept-Language", defaultValue = "uz") String lang) {
        log.info("Update Extreme type payload = {}", payload);
        return ResponseEntity.ok(extremeTypeService.update(id, payload, AppLanguage.getLanguage(lang)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable("id") String id) {
        log.info("Delete Extreme type id = {}", id);
        return ResponseEntity.ok(extremeTypeService.delete(id));
    }

    @GetMapping("")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<PageImpl<ExtremeTypeResDTO>> findAllByPagination(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                           @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("Find all Extreme type");
        return ResponseEntity.ok(extremeTypeService.findAllByPagination(page, size));
    }

}
