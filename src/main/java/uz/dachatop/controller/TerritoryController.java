package uz.dachatop.controller;
//PROJECT NAME --> api.dachatop
// YEAR --> 2023
// MONTH --> 03
// DAY --> 28
// TIME --> 10:07

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.dachatop.dto.district.DistrictPaginationDTO;
import uz.dachatop.dto.territory.TerritoryDTO;
import uz.dachatop.dto.response.ApiResponse;
import uz.dachatop.dto.territory.TerritoryPaginationDTO;
import uz.dachatop.enums.AppLanguage;
import uz.dachatop.service.TerritoryService;

import java.util.List;

@Slf4j
@Tag(name = "Territory")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/territory")
public class TerritoryController {

    private final TerritoryService service;

    /**
     * PUBLIC
     */
    @Operation(summary = "Get Territory List By DistrictId ", description = "Method : Get Territory List By DistrictId")
    @GetMapping("/public/districtId/{id}")
    public ApiResponse<List<TerritoryDTO>> getTerritoryListByDistrictId(@PathVariable Long id,
                                                                        @RequestHeader(value = "Accept-Language", defaultValue = "uz") String lang) {
        return service.getTerritoryByDistrictId(id, AppLanguage.getLanguage(lang));
    }

    @Operation(summary = "Get One Territory", description = "Method :Get One Territory")
    @GetMapping("/public/{id}")
    public ApiResponse<TerritoryDTO> getOne(@PathVariable Long id) {
        return service.getOne(id);
    }


    /**
     * ADMIN
     */
    @Operation(summary = "Update Territory for Admin", description = "Method :Update Territory for Admin")
    @PutMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ApiResponse<TerritoryDTO> update(@PathVariable Long id, @RequestBody TerritoryDTO form) {
        return service.update(id, form);
    }

    @Operation(summary = "Create Territory for Admin", description = "Method :Update Territory for Admin")
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ApiResponse<TerritoryDTO> add(@RequestBody TerritoryDTO form) {
        return service.add(form);
    }

    @Operation(summary = "Delete Territory for Admin", description = "Method :Delete Territory for Admin")
    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ApiResponse<Boolean> delete(@PathVariable Long id) {
        return service.delete(id);
    }


    @Operation(summary = "Pagination Territory for Admin", description = "Method : Territory Pagination for Admin")
    @GetMapping("/all/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<TerritoryPaginationDTO> getAllForAdmin(@PathVariable("id") long id,
                                                                 @RequestParam(value = "page", defaultValue = "0") int page,
                                                                 @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(service.getAllForAdmin(id, page, size));
    }

}
