package uz.dachatop.controller;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.dachatop.dto.response.ApiResponse;
import uz.dachatop.dto.subscription.SubscriptionChangeStatusDTO;
import uz.dachatop.dto.subscription.SubscriptionCreateDTO;
import uz.dachatop.dto.subscription.SubscriptionDTO;
import uz.dachatop.dto.subscription.SubscriptionFilterRequestDTO;
import uz.dachatop.dto.transaction.SubscriptionTransactionDTO;
import uz.dachatop.dto.transaction.SubscriptionTransactionFilterRequestDTO;
import uz.dachatop.service.SubscriptionService;
import uz.dachatop.service.SubscriptionTransactionService;

//PROJECT NAME --> api.dachatop
// YEAR --> 2023
// MONTH --> 04
// DAY --> 05
// TIME --> 13:21
@Slf4j
@Api(tags = "Subscription Transaction")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/transaction")
public class SubscriptionTransactionController {

    private final SubscriptionTransactionService subscriptionTransactionService;


    @Operation(description = "Filter profile. Only for the admin")
    @PostMapping("/filter")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Page<SubscriptionTransactionDTO>>> filter(@RequestBody SubscriptionTransactionFilterRequestDTO filterDTO,
                                                                                @RequestParam(value = "page", defaultValue = "0") int page,
                                                                                @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(subscriptionTransactionService.filter(filterDTO,page, size));
    }




}
