package uz.dachatop.controller.place.apartment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.dachatop.dto.place.apartment.ApartmentDTO;
import uz.dachatop.dto.place.apartment.ApartmentCreateDTO;
import uz.dachatop.dto.place.apartment.ApartmentFilterDTO;
import uz.dachatop.dto.place.apartment.ApartmentInfoDTO;
import uz.dachatop.dto.response.ApiResponse;
import uz.dachatop.enums.AppLanguage;
import uz.dachatop.enums.GlobalStatus;
import uz.dachatop.service.place.apartment.ApartmentService;

@Slf4j
@Tag(name = "Apartment")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/apartment")
public class ApartmentController {
    private final ApartmentService apartmentService;

    /**
     * Public
     */
    @Operation(summary = "Filter apartment", description = "Filter apartment  with pagination")
    @PostMapping("/public/filter")
    public ResponseEntity<ApiResponse<Page<ApartmentInfoDTO>>> filer(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                     @RequestParam(value = "size", defaultValue = "10") int size,
                                                                     @RequestHeader(value = "Accept-Language", defaultValue = "uz") String lang,
                                                                     @RequestBody ApartmentFilterDTO filterDTO) {
        return ResponseEntity.ok(apartmentService.apartmentFilter_asClient(filterDTO, page, size, AppLanguage.getLanguage(lang)));
    }

    @Operation(summary = "Get apartment by id", description = "Method returns apartment by id")
    @GetMapping("/public/{id}")
    public ResponseEntity<ApiResponse<ApartmentDTO>> apartmentById(@PathVariable("id") String id,
                                                                   @RequestHeader(value = "Accept-Language", defaultValue = "uz") String lang) {
        return ResponseEntity.ok(apartmentService.getApartmentById(id, AppLanguage.getLanguage(lang)));
    }

    /**
     * Client
     */

    @Operation(summary = "Create Apartment", description = "Method used for creating apartment")
    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponse<String>> create(@RequestBody ApartmentCreateDTO dto,
                                                      @RequestHeader(value = "Accept-Language",
                                                              defaultValue = "uz") String lang) {
        return ResponseEntity.ok(apartmentService.create(dto));
    }

    @Operation(summary = "Update Apartment", description = "Method used for creating apartment")
    @PutMapping("/{apartmentId}")
//    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponse<String>> update(@PathVariable("apartmentId") String apartmentId, @RequestBody ApartmentCreateDTO dto) {
        return ResponseEntity.ok(apartmentService.update(apartmentId, dto));
    }

    @Operation(summary = "Delete apartment by id", description = "Delete apartment by id. For Client owner")
    @DeleteMapping("/{apartmentId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponse<?>> deleteApartment(@PathVariable("apartmentId") String id,
                                                          @RequestHeader(value = "Accept-Language", defaultValue = "uz") String lang) {
        return ResponseEntity.ok(apartmentService.deleteApartment(id, AppLanguage.getLanguage(lang)));
    }

    /**
     * Admin
     */

    @Operation(summary = "Change apartment status", description = "Change apartment status by id. Admin only")
    @PutMapping("/admin/status/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<?>> changeStatus(@PathVariable("id") String id,
                                                       @RequestParam("status") GlobalStatus status,
                                                       @RequestHeader(value = "Accept-Language", defaultValue = "uz") String lang) {
        return ResponseEntity.ok(apartmentService.changeStatus(id, status));
    }
}
