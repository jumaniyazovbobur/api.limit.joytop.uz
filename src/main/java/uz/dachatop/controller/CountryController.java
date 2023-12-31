package uz.dachatop.controller;
// PROJECT NAME -> api.dachatop
// TIME -> 22:13
// MONTH -> 07
// DAY -> 30

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.dachatop.dto.country.*;
import uz.dachatop.dto.response.ApiResponse;
import uz.dachatop.enums.AppLanguage;
import uz.dachatop.service.CountryService;

import java.util.List;

@Slf4j
@Api(tags = "Country")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/country")
public class CountryController {
    private final CountryService countryService;

    /**
     * USER
     */
    @ApiOperation(value = "Get Country List", notes = "Get Country List with Language")
    @GetMapping("/public/user")
    public ResponseEntity<ApiResponse<List<CountryDTO>>> getCountryListByLanguage(@RequestHeader(value = "Accept-Language", defaultValue = "LATIN") AppLanguage language) {
        log.info("get country");
        return ResponseEntity.ok().body(countryService.getList(language));
    }

    /**
     * ADMIN
     */

    @ApiOperation(value = "Country Create", notes = "Country Create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("")
    public ResponseEntity<ApiResponse<CountryResponseDTO>> create(@RequestBody CountryRequestDTO countryDTO) {
        log.info("Request for Country Create {}", countryDTO);
        return ResponseEntity.ok().body(countryService.countryCreate(countryDTO));
    }

    @ApiOperation(value = "Update Country ", notes = "Method : Update Country")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CountryResponseDTO>> update(@PathVariable("id") Long id, @RequestBody CountryUpdateDTO dto) {
        log.info("Request for Country Update {}", dto);
        return ResponseEntity.ok().body(countryService.update(id, dto));
    }

    @ApiOperation(value = "Delete Country", notes = "Method : Country Delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Boolean>> delete(@PathVariable("id") Integer id) {
        return ResponseEntity.ok().body(countryService.delete(id));
    }

    @ApiOperation(value = "Country Pagination", notes = "Method : Country Pagination for admin", response = CountryPaginationDTO.class)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/pagination")
    public ResponseEntity<CountryPaginationDTO> pagination(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok().body(countryService.pagination(page, size));
    }
}
