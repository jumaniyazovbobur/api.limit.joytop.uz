package uz.dachatop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.dachatop.dto.profile.*;
import uz.dachatop.dto.response.ApiResponse;
import uz.dachatop.dto.sms.SmsCodeConfirmDTO;
import uz.dachatop.dto.territory.TerritoryPaginationDTO;
import uz.dachatop.service.ProfileService;

@Slf4j
@Tag(name = "Profile")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {
    private final ProfileService service;

    @Operation(description = "Get Current profile Detail")
    @GetMapping("/current")
    public ResponseEntity<ApiResponse<ProfileResponseDTO>> getCurrentUserInfo() {
        return ResponseEntity.ok(service.getCurrentProfileInfo());
    }

    @Operation(description = "Update profile detail")
    @PutMapping("/current")
    public ResponseEntity<ApiResponse<Object>> updateCurrentProfile(@RequestBody @Valid ProfileDetailUpdateDTO dto) {
        return ResponseEntity.ok(service.updateCurrentProfile(dto));
    }

    /**
     * Change password
     */
    @Operation(description = "Profile chang password.")
    @PutMapping("/password/change")
    public ResponseEntity<ApiResponse<String>> changePassword(@RequestBody @Valid ProfileChangPasswordDTO passwordDTO) {
        return ResponseEntity.ok(service.profileChangPassword(passwordDTO));
    }

    /**
     * Reset password
     */

    @Operation(description = "Profile reset password request")
    @PutMapping("/password/reset/request")
    public ResponseEntity<ApiResponse<String>> resetPasswordRequest() {
        return ResponseEntity.ok(service.resetPasswordRequest());
    }

    @Operation(description = "Confirm reset password  password")
    @PutMapping("/password/reset/confirm")
    public ResponseEntity<ApiResponse<Boolean>> resetPasswordConfirm(@RequestBody @Valid SmsCodeConfirmDTO passwordDTO) {
        return ResponseEntity.ok(service.resetPasswordConfirm(passwordDTO));
    }

    /**
     * Update Phone
     */

    @Operation(description = "Make request for profile chang phone.")
    @PutMapping("/phone/change/request")
    public ResponseEntity<ApiResponse<String>> changePhoneRequest(@RequestBody @Valid ProfileChangePhoneDTO passwordDTO) {
        return ResponseEntity.ok(service.changPhoneRequest(passwordDTO));
    }

    @Operation(description = "Confirm change phone.")
    @PutMapping("/phone/change/confirm")
    public ResponseEntity<ApiResponse<Boolean>> changePhoneConfirm(@RequestBody @Valid SmsCodeConfirmDTO passwordDTO) {
        return ResponseEntity.ok(service.changPhoneConfirm(passwordDTO));
    }


    /**
     * ADMIN
     */
    @Operation(description = "Get profile by id. Only for the admin")
    @GetMapping("/admin/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ApiResponse<ProfileResponseDTO> getById(@PathVariable String id) {
        return service.getById(id);
    }

    @Operation(description = "Filter profile. Only for the admin")
    @PostMapping("/admin/filter")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Page<ProfileResponseDTO>>> profileFilter(@RequestBody ProfileFilterDTO filterDTO,
                                                                               @RequestParam(value = "page", defaultValue = "0") int page,
                                                                               @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(service.profileFilter(filterDTO, page, size));
    }

    @Operation(description = "Delete profile. Only for the admin")
    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ApiResponse<Boolean> delete(@PathVariable String id) {
        return service.deleteUser(id);
    }

    @Operation(description = "Block User. Only for admin")
    @PutMapping("/admin/block")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ApiResponse<Boolean> delete(@RequestBody ProfileBlockDTO dto) {
        return service.blockUser(dto);
    }


    @Operation(description = "Create only for the admin")
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public ApiResponse<ProfileResponseDTO> create(@RequestBody ProfileRequestDTO dto) {
        log.info("Create profile");
        return service.profileCreate(dto);
    }

    @Operation(description = "Set admin role")
    @PutMapping("/set-role/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ApiResponse<String> setAdminRole(@PathVariable("id") String profileId) {
        log.info("Admin role");
        return service.setAdminRole(profileId);
    }
}
