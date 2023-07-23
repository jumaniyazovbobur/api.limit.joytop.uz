package uz.dachatop.controller.place;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.dachatop.dto.place.PlaceFilterDTO;
import uz.dachatop.dto.place.PlaceInfoDTO;
import uz.dachatop.dto.place.PlacePaginationDTO;
import uz.dachatop.dto.place.PlaceTransferReqDTO;
import uz.dachatop.dto.profile.ProfilePaginationDTO;
import uz.dachatop.dto.response.ApiResponse;
import uz.dachatop.enums.AppLanguage;
import uz.dachatop.service.place.PlaceService;

import javax.validation.Valid;

/**
 * @author 'Mukhtarov Sarvarbek' on 28.02.2023
 * @project api.dachatop
 * @contact @sarvargo
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/place")
@Tag(name = "Place")
public class PlaceController {

    private final PlaceService placeService;

    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Get all places for requesting user", description = "Get all places for requesting user")
    public ResponseEntity<PageImpl<PlaceInfoDTO>> findPlacesByUser(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                   @RequestParam(value = "size", defaultValue = "10") int size,
                                                                   @RequestHeader(value = "Accept-Language", defaultValue = "uz") String lang) {
        log.info("Get all places for requesting user");
        return ResponseEntity.ok(placeService.findAllPlaces(page, size, AppLanguage.getLanguage(lang)));
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Get all places only for admin", description = "Get all places only for admin")
    public ResponseEntity<PageImpl<PlaceInfoDTO>> findAllPlacesByFilter(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                        @RequestParam(value = "size", defaultValue = "10") int size,
                                                                        @RequestHeader(value = "Accept-Language", defaultValue = "uz") String lang,
                                                                        @RequestBody PlaceFilterDTO filterDTO) {
        log.info("Get all places only for admin");
        return ResponseEntity.ok(placeService.findAllPlacesByFilter(filterDTO, page, size, AppLanguage.getLanguage(lang)));
    }

    @PutMapping("/transfer")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Get all places only for admin", description = "Get all places only for admin")
    public ResponseEntity<ApiResponse<?>> transferPlace(@RequestBody @Valid PlaceTransferReqDTO dto) {
        log.info("APi for transfer place");
        return ResponseEntity.ok(placeService.transferPlace(dto));
    }
}
