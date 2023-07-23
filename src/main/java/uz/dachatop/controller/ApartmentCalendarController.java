/*
package uz.dachatop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.dachatop.dto.apartment.ApartmentCalendarDateDTO;
import uz.dachatop.dto.apartment.ApartmentCalendarRequestDTO;
import uz.dachatop.dto.apartment.ApartmentCalendarResponseDTO;
import uz.dachatop.dto.response.ApiResponse;
import uz.dachatop.service.place.ApartmentCalendarService;

import java.util.List;


@Slf4j
@Tag(name = "Apartment Calendar")
@RestController
@RequestMapping("/api/v1/apartment-calendar")
@RequiredArgsConstructor
public class ApartmentCalendarController {

    private final ApartmentCalendarService calendarService;

    */
/**
     * PUBLIC
     *//*

    @Operation(summary = "Get by apartmentId")
    @PutMapping("/apartment/{Id}")
    public ResponseEntity<ApiResponse<List<ApartmentCalendarResponseDTO>>> getByApartmentId(@PathVariable String id,
                                                                                            @RequestBody ApartmentCalendarDateDTO dto) {
        return ResponseEntity.ok(calendarService.getByApartmentId(id, dto));
    }


    @Operation(summary = "Create", description = "Create jwt role moderator")
    @PreAuthorize("hasAnyRole('ROLE_MODERATOR')")
    @PostMapping()
    public ResponseEntity<ApiResponse<ApartmentCalendarResponseDTO>> create(@RequestBody @Valid ApartmentCalendarRequestDTO requestDTO) {
        return ResponseEntity.ok(calendarService.create(requestDTO));
    }

    @Operation(summary = "Update", description = "Update jwt role moderator")
    @PreAuthorize("hasAnyRole('ROLE_MODERATOR')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Boolean>> update(@PathVariable String id,
                                                       @RequestBody @Valid ApartmentCalendarRequestDTO requestDTO) {
        return ResponseEntity.ok(calendarService.update(id, requestDTO));
    }

    @Operation(summary = "Delete", description = "Delete jwt role moderator")
    @PreAuthorize("hasAnyRole('ROLE_MODERATOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Boolean>> delete(@PathVariable String id) {
        return ResponseEntity.ok(calendarService.delete(id));
    }
}
*/
