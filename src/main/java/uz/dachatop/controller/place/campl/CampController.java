package uz.dachatop.controller.place.campl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.dachatop.dto.place.CustomPageImplResult;
import uz.dachatop.dto.place.camp.CampCreateDTO;
import uz.dachatop.dto.place.camp.CampFilterDTO;
import uz.dachatop.dto.place.camp.CampInfoDTO;
import uz.dachatop.dto.place.camp.CampReqDTO;
import uz.dachatop.dto.response.ApiResponse;
import uz.dachatop.enums.AppLanguage;
import uz.dachatop.enums.GlobalStatus;
import uz.dachatop.service.place.camp.CampService;

@Slf4j
@Tag(name = "Camp")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/camp")
public class CampController {
    private final CampService campService;

    /**
     * Public
     */
    @Operation(summary = "Filter camp", description = "Filter camp  with pagination")
    @PostMapping("/public/filter")
    public ResponseEntity<ApiResponse<Page<CampInfoDTO>>> filer(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                @RequestParam(value = "size", defaultValue = "10") int size,
                                                                @RequestHeader(value = "Accept-Language", defaultValue = "uz") String lang,
                                                                @RequestBody CampFilterDTO filterDTO) {
        return ResponseEntity.ok(campService.filterAsClient(filterDTO, page, size, AppLanguage.getLanguage(lang)));
    }

    @Operation(summary = "Get camp by id", description = "Method returns camp by id")
    @GetMapping("/public/{id}")
    public ResponseEntity<ApiResponse<CampReqDTO>> getById(@PathVariable("id") String id,
                                                           @RequestHeader(value = "Accept-Language", defaultValue = "uz") String lang) {
        return ResponseEntity.ok(campService.getCampById(id, AppLanguage.getLanguage(lang)));
    }

    /**
     * Client
     */

    @Operation(summary = "Create camp", description = "Method used for creating camp")
    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponse<Boolean>> create(@RequestBody CampCreateDTO dto,
                                                       @RequestHeader(value = "Accept-Language", defaultValue = "uz") String lang) {
        return ResponseEntity.ok(campService.create(dto));
    }

    @Operation(summary = "Update camp", description = "Method used for creating camp")
    @PutMapping("/{campId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponse<String>> update(@PathVariable("campId") String campId, @RequestBody CampCreateDTO dto) {
        return ResponseEntity.ok(campService.update(campId, dto));
    }

    @Operation(summary = "Delete camp by id", description = "Delete camp by id. For Client owner")
    @DeleteMapping("/{campId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable("campId") String id) {
        return ResponseEntity.ok(campService.delete(id));
    }

    /**
     * Admin
     */

    @Operation(summary = "Change camp status", description = "Change camp status by id. Admin only")
    @PutMapping("/admin/status/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<?>> changeStatus(@PathVariable("id") String id,
                                                       @RequestParam("status") GlobalStatus status,
                                                       @RequestHeader(value = "Accept-Language", defaultValue = "uz") String lang) {
        return ResponseEntity.ok(campService.changeStatus(id, status));
    }
}
