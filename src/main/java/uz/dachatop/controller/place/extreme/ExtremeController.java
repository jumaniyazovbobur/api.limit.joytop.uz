package uz.dachatop.controller.place.extreme;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.dachatop.dto.place.CustomPageImplResult;
import uz.dachatop.dto.place.extreme.ExtremeCreateDTO;
import uz.dachatop.dto.place.extreme.ExtremeFilterDTO;
import uz.dachatop.dto.place.extreme.ExtremeInfoDTO;
import uz.dachatop.dto.place.extreme.ExtremeReqDTO;
import uz.dachatop.dto.response.ApiResponse;
import uz.dachatop.enums.AppLanguage;
import uz.dachatop.enums.GlobalStatus;
import uz.dachatop.service.place.extreme.ExtremeService;

@Slf4j
@Tag(name = "Extreme")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/extreme")
public class ExtremeController {
    private final ExtremeService extremeService;

    /**
     * Public
     */
    @Operation(summary = "Filter extreme", description = "Filter extreme  with pagination")
    @PostMapping("/public/filter")
    public ResponseEntity<ApiResponse<Page<ExtremeInfoDTO>>> filer(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                   @RequestParam(value = "size", defaultValue = "10") int size,
                                                                   @RequestHeader(value = "Accept-Language", defaultValue = "uz") String lang,
                                                                   @RequestBody ExtremeFilterDTO filterDTO) {
        return ResponseEntity.ok(extremeService.filterAsClient(filterDTO, page, size, AppLanguage.getLanguage(lang)));
    }

    @Operation(summary = "Get extreme by id", description = "Method returns extreme by id")
    @GetMapping("/public/{id}")
    public ResponseEntity<ApiResponse<ExtremeReqDTO>> extremeById(@PathVariable("id") String id,
                                                                  @RequestHeader(value = "Accept-Language", defaultValue = "uz") String lang) {
        return ResponseEntity.ok(extremeService.getExtremeById(id, AppLanguage.getLanguage(lang)));
    }

    /**
     * Client
     */

    @Operation(summary = "Create extreme", description = "Method used for creating extreme")
    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponse<String>> create(@RequestBody ExtremeCreateDTO dto,
                                                      @RequestHeader(value = "Accept-Language", defaultValue = "uz") String lang) {
        return ResponseEntity.ok(extremeService.create(dto));
    }

    @Operation(summary = "Update extreme", description = "Method used for creating extreme")
    @PutMapping("/{extremeId}")
//    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponse<String>> update(@PathVariable("extremeId") String extremeId, @RequestBody ExtremeCreateDTO dto) {
        return ResponseEntity.ok(extremeService.update(extremeId, dto));
    }

    @Operation(summary = "Delete extreme by id", description = "Delete extreme by id. For Client owner")
    @DeleteMapping("/{extremeId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponse<?>> deleteExtreme(@PathVariable("extremeId") String id) {
        return ResponseEntity.ok(extremeService.deleteExtreme(id));
    }

    /**
     * Admin
     */

    @Operation(summary = "Change extreme status", description = "Change extreme status by id. Admin only")
    @PutMapping("/admin/status/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<?>> changeStatus(@PathVariable("id") String id,
                                                       @RequestParam("status") GlobalStatus status,
                                                       @RequestHeader(value = "Accept-Language", defaultValue = "uz") String lang) {
        return ResponseEntity.ok(extremeService.changeStatus(id, status));
    }
}
