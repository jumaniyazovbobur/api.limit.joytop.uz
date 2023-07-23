package uz.dachatop.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.dachatop.dto.profile.ProfileFilterDTO;
import uz.dachatop.dto.profile.ProfilePaginationDTO;
import uz.dachatop.dto.response.ApiResponse;
import uz.dachatop.dto.subscription.*;
import uz.dachatop.dto.tariff.SubscriptionTariffDTO;
import uz.dachatop.enums.SubscriptionStatus;
import uz.dachatop.service.SubscriptionService;

//PROJECT NAME --> api.dachatop
// YEAR --> 2023
// MONTH --> 04
// DAY --> 05
// TIME --> 13:21
@Slf4j
@Api(tags = "Subscription")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/subscription")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @Operation(summary = "Add Subscription Tariff ", description = "Method: Add Subscription Tariff")
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ApiResponse<SubscriptionDTO> add(@Valid @RequestBody SubscriptionCreateDTO form) {
        return subscriptionService.create(form);
    }


    @Operation(description = "Subscription change status.")
    @PutMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<String>> changeStatus(@RequestBody SubscriptionChangeStatusDTO dto) {
        return ResponseEntity.ok(subscriptionService.changeStatus(dto));
    }


    @Operation(description = "Filter profile. Only for the admin")
    @PostMapping("/filter")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Page<SubscriptionDTO>>> filter(@RequestBody SubscriptionFilterRequestDTO filterDTO,
                                                                           @RequestParam(value = "page", defaultValue = "0") int page,
                                                                           @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(subscriptionService.filter(filterDTO,page, size));
    }




}
