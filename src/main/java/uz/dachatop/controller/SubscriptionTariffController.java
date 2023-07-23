package uz.dachatop.controller;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.dachatop.dto.region.RegionCreateDTO;
import uz.dachatop.dto.region.RegionDTO;
import uz.dachatop.dto.response.ApiResponse;
import uz.dachatop.dto.tariff.SubscriptionTariffCreateDTO;
import uz.dachatop.dto.tariff.SubscriptionTariffDTO;
import uz.dachatop.service.SubscriptionTariffService;

import java.util.List;

//PROJECT NAME --> api.dachatop
// YEAR --> 2023
// MONTH --> 04
// DAY --> 03
// TIME --> 17:15
@Slf4j
@Api(tags = "SubscriptionTariff")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/tariff")
public class SubscriptionTariffController {
    private final SubscriptionTariffService subscriptionTariffService;

    @Operation(summary = "Add Subscription Tariff ", description = "Method: Add Subscription Tariff")
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ApiResponse<SubscriptionTariffDTO> add(@RequestBody SubscriptionTariffCreateDTO form) {
        return subscriptionTariffService.create(form);
    }

    @Operation(summary = "Get One Subscription Tariff by id ", description = "Method: Get One Subscription Tariff by id")
    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER','ROLE_MODERATOR')")
    public ApiResponse<SubscriptionTariffDTO> getOne(@PathVariable String id) {
        return subscriptionTariffService.getOne(id);
    }


    @Operation(summary = "Update Subscription Tariff ", description = "Method: Update Subscription Tariff")
    @PutMapping("{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ApiResponse<SubscriptionTariffDTO> update(@PathVariable String id, @RequestBody SubscriptionTariffCreateDTO form) {
        return subscriptionTariffService.update(id, form);
    }

    @Operation(summary = "Delete Subscription Tariff ", description = "Method: Delete Subscription Tariff")
    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ApiResponse<Boolean> delete(@PathVariable String id) {
        return subscriptionTariffService.delete(id);
    }


    @Operation(summary = "Get All Subscription Tariffs ", description = "Method: Get All Subscription Tariffs")
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER','ROLE_MODERATOR')")
    public ApiResponse<List<SubscriptionTariffDTO>> getAll() {
        return subscriptionTariffService.getAll();
    }








}
