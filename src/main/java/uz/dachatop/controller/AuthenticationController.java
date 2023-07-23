package uz.dachatop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.dachatop.dto.auth.AuthenticationRequest;
import uz.dachatop.dto.auth.AuthenticationResponse;
import uz.dachatop.dto.auth.RegisterRequest;
import uz.dachatop.dto.profile.ProfileConfirmDTO;
import uz.dachatop.dto.profile.ProfileRestartSmsDTO;
import uz.dachatop.dto.response.ApiResponse;
import uz.dachatop.service.AuthenticationService;


@Tag(name = "Auth")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @Operation(description = "Registration")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@RequestBody @Valid RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    @Operation(description = "Confirm registration sms code")
    @PostMapping("/verification-sms")
    public ApiResponse<AuthenticationResponse> registrationConfirm(@RequestBody ProfileConfirmDTO dto) {
        return service.registrationConfirm(dto);
    }

    @Operation(description = "Resend registration sms code")
    @PostMapping("/register/resend")
    public ResponseEntity<ApiResponse<Boolean>> resendRegistrationSmsCode(@RequestBody @Valid ProfileRestartSmsDTO dto) {
        return ResponseEntity.ok(service.restartSms(dto));
    }

    @Operation(description = "Authorization")
    @PostMapping("/authenticate")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> authenticate(@RequestBody @Valid AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }
}
