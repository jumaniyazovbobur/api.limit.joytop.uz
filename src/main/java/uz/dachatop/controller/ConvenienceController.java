package uz.dachatop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.dachatop.dto.convenience.ConvenienceCreateDTO;
import uz.dachatop.dto.convenience.ConvenienceDTO;
import uz.dachatop.dto.response.ApiResponse;
import uz.dachatop.enums.AppLanguage;
import uz.dachatop.service.ConvenienceService;

import java.util.List;

@Slf4j
@Tag(name = "Convenience")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/convenience")
public class ConvenienceController {
    private final ConvenienceService service;

    /**
     * PUBLIC
     */
    @Operation(summary = "Find All Convenience List", description = "Method : Find All Convenience List")
    @GetMapping({"/public"})
    public ApiResponse<List<ConvenienceDTO>> findAll(@RequestHeader(value = "Accept-Language", defaultValue = "uz") String lang) {
        return service.findAll(AppLanguage.getLanguage(lang));
    }

    /**
     * ADMIN
     */
    @Operation(summary = "Create Convenience by Admin", description = "Method : Create Convenience by Admin")
    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ApiResponse<ConvenienceDTO> save(@RequestBody ConvenienceCreateDTO dto) {
        return service.addConvenience(dto);
    }


    @Operation(summary = "Get One Convenience", description = "Method : Get One Convenience")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ApiResponse<ConvenienceDTO> getOne(@PathVariable Long id) {
        return service.getOne(id);
    }


    @Operation(summary = "Update Convenience by Admin", description = "Method : Update Convenience by Admin")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ApiResponse<ConvenienceDTO> update(@PathVariable Long id, @RequestBody ConvenienceCreateDTO dto) {
        return service.updateConvenience(id, dto);
    }

    @Operation(summary = "Delete Convenience by Admin", description = "Method : Delete Convenience by Admin")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ApiResponse<Boolean> delete(@PathVariable Long id) {
        return service.delete(id);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ApiResponse<List<ConvenienceDTO>> getAllForAdmin() {
        return service.findAllForAdmin();
    }

}

