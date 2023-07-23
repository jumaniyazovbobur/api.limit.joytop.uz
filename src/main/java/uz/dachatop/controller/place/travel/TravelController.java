package uz.dachatop.controller.place.travel;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.dachatop.dto.place.CustomPageImplResult;
import uz.dachatop.dto.place.camp.TravelReqDTO;
import uz.dachatop.dto.place.travel.TravelCreateDTO;
import uz.dachatop.dto.place.travel.TravelFilterDTO;
import uz.dachatop.dto.place.travel.TravelInfoDTO;
import uz.dachatop.dto.response.ApiResponse;
import uz.dachatop.enums.AppLanguage;
import uz.dachatop.enums.GlobalStatus;
import uz.dachatop.service.place.tral.TravelService;

@Slf4j
@Tag(name = "Travel")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/travel")
public class TravelController {
    private final TravelService travelService;

    /**
     * Public
     */
    @Operation(summary = "Filter travel", description = "Filter travel  with pagination")
    @PostMapping("/public/filter")
    public ResponseEntity<ApiResponse<Page<TravelInfoDTO>>> filer(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                  @RequestParam(value = "size", defaultValue = "10") int size,
                                                                  @RequestHeader(value = "Accept-Language", defaultValue = "uz") String lang,
                                                                  @RequestBody TravelFilterDTO filterDTO) {
        return ResponseEntity.ok(travelService.filterAsClient(filterDTO, page, size, AppLanguage.getLanguage(lang)));
    }

    @Operation(summary = "Get travel by id", description = "Method returns travel by id")
    @GetMapping("/public/{id}")
    public ResponseEntity<ApiResponse<TravelReqDTO>> getById(@PathVariable("id") String id,
                                                             @RequestHeader(value = "Accept-Language", defaultValue = "uz") String lang) {
        return ResponseEntity.ok(travelService.getById(id, AppLanguage.getLanguage(lang)));
    }

    /**
     * Client
     */

    @Operation(summary = "Create travel", description = "Method used for creating travel")
    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponse<Boolean>> create(@RequestBody TravelCreateDTO dto,
                                                       @RequestHeader(value = "Accept-Language", defaultValue = "uz") String lang) {
        return ResponseEntity.ok(travelService.create(dto));
    }

    @Operation(summary = "Update travel", description = "Method used for creating travel")
    @PutMapping("/{travelId}")
//    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponse<String>> update(@PathVariable("travelId") String travelId, @RequestBody TravelCreateDTO dto) {
        return ResponseEntity.ok(travelService.update(travelId, dto));
    }

    @Operation(summary = "Delete travel by id", description = "Delete travel by id. For Client owner")
    @DeleteMapping("/{travelId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable("travelId") String id) {
        return ResponseEntity.ok(travelService.delete(id));
    }

    /**
     * Admin
     */

    @Operation(summary = "Change travel status", description = "Change travel status by id. Admin only")
    @PutMapping("/admin/status/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<?>> changeStatus(@PathVariable("id") String id,
                                                       @RequestParam("status") GlobalStatus status,
                                                       @RequestHeader(value = "Accept-Language", defaultValue = "uz") String lang) {
        return ResponseEntity.ok(travelService.changeStatus(id, status));
    }
}
