package uz.dachatop.controller.place.dacha;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.dachatop.dto.place.dacha.DachaCreateDTO;
import uz.dachatop.dto.place.dacha.DachaDTO;
import uz.dachatop.dto.place.dacha.DachaFilterDTO;
import uz.dachatop.dto.place.dacha.DachaInfoDTO;
import uz.dachatop.dto.response.ApiResponse;
import uz.dachatop.enums.AppLanguage;
import uz.dachatop.enums.GlobalStatus;
import uz.dachatop.service.place.dacha.DachaService;

@Slf4j
@Tag(name = "Dacha")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/dacha")
public class DachaController {
    private final DachaService dachaService;

    /**
     * Public
     */
    @Operation(summary = "Filter dacha", description = "Filter dacha  with pagination")
    @PostMapping("/public/filter")
    public ResponseEntity<ApiResponse<Page<DachaInfoDTO>>> filer(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                 @RequestParam(value = "size", defaultValue = "10") int size,
                                                                 @RequestHeader(value = "Accept-Language", defaultValue = "uz") String lang,
                                                                 @RequestBody DachaFilterDTO filterDTO) {
        return ResponseEntity.ok(dachaService.filter_asClient(filterDTO, page, size, AppLanguage.getLanguage(lang)));
    }

    @Operation(summary = "Get dacha by id", description = "Method returns dacha by id")
    @GetMapping("/public/{id}")
    public ResponseEntity<ApiResponse<DachaDTO>> dachaGetById(@PathVariable("id") String id, @RequestHeader(value = "Accept-Language", defaultValue = "uz") String lang) {
        return ResponseEntity.ok(dachaService.getDachaById(id, AppLanguage.getLanguage(lang)));
    }

    @Operation(summary = "Get dacha by number", description = "Method returns dacha by number")
    @GetMapping("/public/number/{number}")
    public ResponseEntity<ApiResponse<DachaDTO>> dachaGetById(@PathVariable("number") Long number,
                                                              @RequestHeader(value = "Accept-Language",
                                                                      defaultValue = "uz") String lang) {
        return ResponseEntity.ok(dachaService.getDachaByNumber(number, AppLanguage.getLanguage(lang)));
    }

    /**
     * Client
     */

    @Operation(summary = "Create dacha", description = "Method used for creating dacha")
    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponse<String>> create(@RequestBody DachaCreateDTO dto,
                                                      @RequestHeader(value = "Accept-Language",
                                                              defaultValue = "uz") String lang) {
        return ResponseEntity.ok(dachaService.create(dto));
    }

    @Operation(summary = "Update dacha", description = "Method used for creating dacha")
    @PutMapping("/{dachaId}")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<String>> update(@PathVariable("dachaId") String dachaId, @RequestBody DachaCreateDTO dto) {
        return ResponseEntity.ok(dachaService.update(dachaId, dto));
    }

    @Operation(summary = "Delete dacha by id", description = "Delete dacha by id. For Client owner")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable("id") String id, @RequestHeader(value = "Accept-Language", defaultValue = "uz") String lang) {
        return ResponseEntity.ok(dachaService.deleteApartment(id, AppLanguage.getLanguage(lang)));
    }

    /**
     * Admin
     */
    @Operation(summary = "Change dacha status", description = "Change dacha status by id. Admin only")
    @PutMapping("/admin/status/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<?>> changeStatus(@PathVariable("id") String id,
                                                       @RequestParam("status") GlobalStatus status,
                                                       @RequestHeader(value = "Accept-Language", defaultValue = "uz") String lang) {
        return ResponseEntity.ok(dachaService.changeStatus(id, status));
    }
}
