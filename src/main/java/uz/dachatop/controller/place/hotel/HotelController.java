package uz.dachatop.controller.place.hotel;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.websocket.server.PathParam;
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
import uz.dachatop.dto.place.hotel.HotelCreateDTO;
import uz.dachatop.dto.place.hotel.HotelDTO;
import uz.dachatop.dto.place.hotel.HotelFilterDTO;
import uz.dachatop.dto.place.hotel.HotelInfoDTO;
import uz.dachatop.dto.response.ApiResponse;
import uz.dachatop.enums.AppLanguage;
import uz.dachatop.enums.GlobalStatus;
import uz.dachatop.service.place.dacha.DachaService;
import uz.dachatop.service.place.hotel.HotelService;

@Slf4j
@Tag(name = "Hotel")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/hotel")
public class HotelController {
    private final HotelService hotelService;

    /**
     * Public
     */
    @Operation(summary = "Filter hotel", description = "Filter hotel with pagination")
    @PostMapping("/public/filter")
    public ResponseEntity<ApiResponse<Page<HotelInfoDTO>>> filer(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                 @RequestParam(value = "size", defaultValue = "10") int size,
                                                                 @RequestHeader(value = "Accept-Language", defaultValue = "uz") String lang,
                                                                 @RequestBody HotelFilterDTO filterDTO) {
        return ResponseEntity.ok(hotelService.filter_asClient(filterDTO, page, size, AppLanguage.getLanguage(lang)));
    }

    @Operation(summary = "Get hotel by id", description = "Method returns hotel by id")
    @GetMapping("/public/{id}")
    public ResponseEntity<ApiResponse<HotelDTO>> apartmentById(@PathVariable("id") String id, @RequestHeader(value = "Accept-Language", defaultValue = "uz") String lang) {
        return ResponseEntity.ok(hotelService.getDachaById(id, AppLanguage.getLanguage(lang)));
    }

    /**
     * Client
     */

    @Operation(summary = "Create hotel", description = "Method used for creating hotel")
    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponse<String>> create(@RequestBody HotelCreateDTO dto, @RequestHeader(value = "Accept-Language", defaultValue = "uz") String lang) {
        return ResponseEntity.ok(hotelService.create(dto));
    }

    @Operation(summary = "Update hotel", description = "Method used for creating hotel")
    @PutMapping("/{dachaId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponse<String>> update(@PathVariable("dachaId") String dachaId, @RequestBody HotelCreateDTO dto) {
        return ResponseEntity.ok(hotelService.update(dachaId, dto));
    }

    @Operation(summary = "Delete hotel by id", description = "Delete hotel by id. For Client owner")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable("id") String id, @RequestHeader(value = "Accept-Language", defaultValue = "uz") String lang) {
        return ResponseEntity.ok(hotelService.deleteHotel(id, AppLanguage.getLanguage(lang)));
    }


    /**
     * Admin
     */
    @Operation(summary = "Change hotel status", description = "Change hotel status by id. Admin only")
    @PutMapping("/admin/status/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<?>> changeStatus(@PathVariable("id") String id,
                                                       @RequestParam("status") GlobalStatus status,
                                                       @RequestHeader(value = "Accept-Language", defaultValue = "uz") String lang) {
        return ResponseEntity.ok(hotelService.changeStatus(id, status));
    }

}
