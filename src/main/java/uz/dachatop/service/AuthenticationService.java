package uz.dachatop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.dachatop.dto.auth.AuthenticationRequest;
import uz.dachatop.dto.auth.AuthenticationResponse;
import uz.dachatop.dto.auth.RegisterRequest;
import uz.dachatop.dto.profile.ProfileAndRoleResponseDTO;
import uz.dachatop.dto.profile.ProfileConfirmDTO;
import uz.dachatop.dto.profile.ProfileResponseDTO;
import uz.dachatop.dto.profile.ProfileRestartSmsDTO;
import uz.dachatop.dto.response.ApiResponse;
import uz.dachatop.entity.ProfileEntity;
import uz.dachatop.enums.GlobalStatus;
import uz.dachatop.enums.ProfileRole;
import uz.dachatop.exp.AppBadRequestException;
import uz.dachatop.repository.ProfileRepository;
import uz.dachatop.util.JwtUtil;
import uz.dachatop.util.MD5Util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final ProfileRepository repository;
    private final ProfileService profileService;
    private final ProfileRepository profileRepository;
    private final SmsService smsService;
    private final RoleService roleService;

    public ApiResponse<String> register(RegisterRequest request) {
        List<ProfileRole> roleList = new ArrayList<>();
        roleList.add(ProfileRole.ROLE_USER);

        String stringApiResponse = profileService.profileRegistration(new ProfileResponseDTO(request.getFirstname(), request.getLastname(), request.getPhone(), roleList, request.getPassword())).getData();
        return new ApiResponse<>(200, false, stringApiResponse);
    }

    public ApiResponse<AuthenticationResponse> authenticate(AuthenticationRequest request) {
        ProfileEntity user = repository.findByPhoneAndVisibleTrue(request.getPhone()).orElseThrow(() -> new AppBadRequestException("User not found"));
        if (!user.getPassword().equals(MD5Util.getMd5(request.getPassword()))) {
            throw new AppBadRequestException("The password is not correct");
        }
        if (user.getStatus().equals(GlobalStatus.NOT_ACTIVE)) {
            throw new AppBadRequestException("User not active");
        }

        List<ProfileRole> roles = roleService.getRoles(user.getId());

        var jwtToken = JwtUtil.encode(user.getPhone(), roles);
        return new ApiResponse<>(200, false, AuthenticationResponse.builder().token(jwtToken).name(user.getFirstName()).phone(user.getPhone()).role(roles).build());
    }

    public ApiResponse<AuthenticationResponse> registrationConfirm(ProfileConfirmDTO dto) {
        smsService.confirmSmsCode(dto.getPhone(), dto.getCode()); // confirm sms code
        profileRepository.updateActivation(GlobalStatus.ACTIVE, dto.getPhone()); // update profile status
        ProfileEntity profileEntity = repository.findByPhoneAndVisibleTrue(dto.getPhone()).orElseThrow(() -> {
            throw new AppBadRequestException("User not found");
        });
        AuthenticationResponse responseDTO = new AuthenticationResponse();
        responseDTO.setPhone(profileEntity.getPhone());
        responseDTO.setId(profileEntity.getId());
        responseDTO.setName(profileEntity.getFirstName());

        List<ProfileRole> roleList = new LinkedList<>();
        roleList.add(ProfileRole.ROLE_USER);
        responseDTO.setRole(roleList);

        roleService.creteAuthRole(responseDTO, profileEntity);
        List<ProfileRole> roles = roleService.getRoles(profileEntity.getId());

        String jwt = JwtUtil.encode(profileEntity.getPhone(), roles);
        responseDTO.setToken(jwt);
        return ApiResponse.ok(responseDTO);
    }

    public ApiResponse<Boolean> restartSms(ProfileRestartSmsDTO dto) {
        Boolean sms = profileService.restartSms(dto);
        return new ApiResponse<>(200, false, sms);
    }



}
