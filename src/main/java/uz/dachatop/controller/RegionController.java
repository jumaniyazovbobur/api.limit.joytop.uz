package uz.dachatop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.dachatop.dto.region.RegionCreateDTO;
import uz.dachatop.dto.region.RegionDTO;
import uz.dachatop.dto.response.ApiResponse;
import uz.dachatop.enums.AppLanguage;
import uz.dachatop.service.RegionService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "Region")
@RequestMapping("/api/v1/region")
public class RegionController {
    private final RegionService service;

    /**
     * PUBLIC
     */
    @Operation(summary = "Find All Regions ", description = "Method: Find All Regions")
    @GetMapping({"/public"})
    public ApiResponse<List<RegionDTO>> findAll(@RequestHeader(value = "Accept-Language", defaultValue = "uz") String language) {
        return service.findAll(AppLanguage.getLanguage(language));
    }
    @Operation(summary = "Find All Regions ", description = "Method: Find All Regions")
    @GetMapping({"/public/{countryId}"})
    public ApiResponse<List<RegionDTO>> findByCountryId(@PathVariable("countryId") Long countryId,
                                                        @RequestHeader(value = "Accept-Language", defaultValue = "uz") String language) {
        return service.findByCountryId(countryId,AppLanguage.getLanguage(language));
    }

    /**
     * ADMIN
     */

    @Operation(summary = "Add Region ", description = "Method: Add Region")
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ApiResponse<RegionDTO> add(@RequestBody RegionCreateDTO form) {
        return service.add(form);
    }

    @Operation(summary = "Update Region ", description = "Method: Update Region")
    @PutMapping("{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ApiResponse<RegionDTO> update(@PathVariable Long id, @RequestBody RegionCreateDTO form) {
        return service.update(id, form);
    }

    @Operation(summary = "Get One Regions ", description = "Method: Get One Regions")
    @GetMapping("{id}")
    public ApiResponse<RegionDTO> getOne(@PathVariable Long id) {
        return service.getOne(id);
    }

    @Operation(summary = "Delete Region ", description = "Method: Delete Region")
    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ApiResponse<Boolean> delete(@PathVariable Long id) {
        return service.delete(id);
    }

    @Operation(summary = "Get All Regions ", description = "Method: Get All Regions")
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ApiResponse<List<RegionDTO>> getAll() {
        return service.getAll();
    }
}
