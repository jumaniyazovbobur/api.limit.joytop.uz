package uz.dachatop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.dachatop.dto.district.DistrictDTO;
import uz.dachatop.dto.district.DistrictPaginationDTO;
import uz.dachatop.dto.response.ApiResponse;
import uz.dachatop.enums.AppLanguage;
import uz.dachatop.service.DistrictService;

import java.util.List;

@Slf4j
@Tag(name = "District")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/district")
public class DistrictController {
    private final DistrictService service;

    /**
     * PUBLIC
     */
    @Operation(summary = "Get District List By RegionId ", description = "Method : Get District List By RegionId")
    @GetMapping("/public/regionId/{id}")
    public ApiResponse<List<DistrictDTO>> getDistrictListByRegionId(@PathVariable Long id,
                                                                    @RequestHeader(value = "Accept-Language", defaultValue = "uz") String lang) {
        return service.getDistrictByRegionId(id, AppLanguage.getLanguage(lang));
    }

    /**
     * ADMIN
     */
    @Operation(summary = "Update District for Admin", description = "Method :Update District for Admin")
    @PutMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ApiResponse<DistrictDTO> update(@PathVariable Long id, @RequestBody DistrictDTO form) {
        return service.update(id, form);
    }

    @Operation(summary = "Create District for Admin", description = "Method :Update District for Admin")
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ApiResponse<DistrictDTO> add(@RequestBody DistrictDTO form) {
        return service.add(form);
    }

    @Operation(summary = "Delete District for Admin", description = "Method :Delete District for Admin")
    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ApiResponse<Boolean> delete(@PathVariable Long id) {
        return service.delete(id);
    }

    @Operation(summary = "Get One District", description = "Method :Get One District")
    @GetMapping("{id}")
    public ApiResponse<DistrictDTO> getOne(@PathVariable Long id) {
        return service.getOne(id);
    }

    @Operation(summary = "Pagination District for Admin", description = "Method : District Pagination for Admin")
    @GetMapping("/all/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<DistrictPaginationDTO> getAllForAdmin(@PathVariable Long id,
                                                                @RequestParam(value = "page", defaultValue = "0") int page,
                                                                @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok().body(service.getAllForAdmin(id,page, size));
    }
}
