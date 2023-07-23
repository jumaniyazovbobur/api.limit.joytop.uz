package uz.dachatop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import uz.dachatop.config.EntityDetails;
import uz.dachatop.dto.auth.AuthenticationResponse;
import uz.dachatop.dto.profile.*;
import uz.dachatop.dto.response.ApiResponse;
import uz.dachatop.dto.sms.SmsCodeConfirmDTO;
import uz.dachatop.dto.subscription.SubscriptionDTO;
import uz.dachatop.dto.subscription.SubscriptionFilterRequestDTO;
import uz.dachatop.dto.subscription.SubscriptionFilterResult;
import uz.dachatop.entity.ProfileEntity;
import uz.dachatop.enums.GlobalStatus;
import uz.dachatop.enums.ProfileRole;
import uz.dachatop.enums.UserType;
import uz.dachatop.exp.AppBadRequestException;
import uz.dachatop.exp.ItemNotFoundException;
import uz.dachatop.mapper.ProfileCustomMapper;
import uz.dachatop.mapper.ProfileMapper;
import uz.dachatop.mapper.SubscriptionMapper;
import uz.dachatop.repository.ProfileRepository;
import uz.dachatop.repository.filter.ProfileFilterRepository;
import uz.dachatop.util.MD5Util;
import uz.dachatop.util.RandomUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final ProfileFilterRepository filterRepository;
    private final SmsService smsService;
    private final RoleService roleService;

    public ApiResponse<ProfileResponseDTO> getCurrentProfileInfo() {
        ProfileAndRoleResponseDTO profile = EntityDetails.getCurrentProfile();
        ProfileResponseDTO responseDTO = new ProfileResponseDTO();
        responseDTO.setId(profile.getId());
        responseDTO.setFirstName(profile.getFirstName());
        responseDTO.setLastName(profile.getLastName());
        responseDTO.setRoleList(profile.getRoleList()
                .stream().map(ProfileRoleDTO::getRole).toList());
        responseDTO.setPhone(profile.getPhone());
        responseDTO.setStatus(profile.getStatus());
        responseDTO.setCreateDate(profile.getCreateDate());
        return ApiResponse.ok(responseDTO);
        /*if (!(current.getGiverInfo() == null)) {
            PassportInfoEntity passportInfoEntity = current.getGiverInfo().getPassportInfoEntity();
            PaymentInfoEntity paymentInfoEntity = current.getGiverInfo().getPaymentInfoEntity();
            profileDto.setPassportInfoDto(PassportInfoDTO.toDto(passportInfoEntity));
            profileDto.setPaymentInfoDto(PaymentInfoDTO.toDto(paymentInfoEntity));
        }*/
    }

    public ApiResponse<Object> updateCurrentProfile(ProfileDetailUpdateDTO dto) {
        ProfileAndRoleResponseDTO currentProfile = EntityDetails.getCurrentProfile();
        currentProfile.setFirstName(dto.getFirstName());
        currentProfile.setLastName(dto.getLastName());
        ProfileEntity profile = roleService.toDTO(currentProfile);
        profileRepository.save(profile);
        return ApiResponse.ok();
    }

    public ApiResponse<Page<ProfileResponseDTO>> profileFilter(ProfileFilterDTO filter, int page, int size) {
        // filter
        // return (id,name,surname,phone,created_date,role_list) with pagination
        ProfileFilterResult<ProfileMapper> filterResult = filterRepository.getFilter(filter, page * size, size);
        List<ProfileResponseDTO> dtoList = filterResult.getContent().stream().map(customMapper -> toDTO(customMapper)).collect(Collectors.toList());
        Page<ProfileResponseDTO> pageObj = new PageImpl<>(dtoList, PageRequest.of(page, size), filterResult.getTotalCount());
        return ApiResponse.ok(pageObj);
    }


    public ApiResponse<String> profileRegistration(ProfileRequestDTO dto) {
        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.setFirstName(dto.getFirstName());
        profileEntity.setLastName(dto.getLastName());
        profileEntity.setPassword(MD5Util.getMd5(dto.getPassword()));

        Optional<ProfileEntity> byPhoneNumber = profileRepository.findByPhoneAndVisibleTrue(dto.getPhone());
        if (byPhoneNumber.isPresent()) {
            throw new AppBadRequestException("Phone unique");
        } else {
            profileEntity.setPhone(dto.getPhone());
        }
        profileEntity.setStatus(GlobalStatus.NOT_ACTIVE);
        profileRepository.save(profileEntity);

        smsService.sendRegistrationSmsCode(profileEntity.getPhone());
        return new ApiResponse<>(200, false, "Successfully");
    }

    public ApiResponse<ProfileResponseDTO> profileCreate(ProfileRequestDTO dto) {
        ProfileAndRoleResponseDTO currentProfile = EntityDetails.getAdminEntity();
        List<ProfileRoleDTO> profileRoleList = currentProfile.getRoleList();
        Optional<ProfileEntity> byPhoneNumber = profileRepository.findByPhoneAndVisibleTrue(dto.getPhone());
        if (byPhoneNumber.isPresent()) {
            throw new AppBadRequestException("Phone unique");
        }


        check_requested_roles(dto.getRoleList(), profileRoleList);
        ProfileEntity profileEntity = crate_profileEntity_from_dto(dto);
        // check for required  fields exists


        profileRepository.save(profileEntity);
        roleService.creteRole(dto, profileEntity);

        return new ApiResponse<>(200, false, toDTO(profileEntity));
    }


    public ApiResponse<Boolean> deleteUser(String id) {
        int i = profileRepository.deleteStatus(false, id);
        return new ApiResponse<Boolean>(200, false, i > 0);
    }

    public ApiResponse<Boolean> blockUser(ProfileBlockDTO dto) {
        int i;
        if (dto.getStatus().equals(GlobalStatus.ACTIVE)) {
            i = profileRepository.blockStatus(GlobalStatus.ACTIVE, dto.getId());
        } else {
            i = profileRepository.blockStatus(GlobalStatus.NOT_ACTIVE, dto.getId());
        }
        return new ApiResponse<Boolean>(200, false, i > 0);
    }

    public ApiResponse<ProfileResponseDTO> getById(String id) {
        ProfileEntity profileEntity = profileRepository.findByIdAndVisibleTrue(id).orElseThrow(() -> new ItemNotFoundException("Profile by id not found" + id));
        return new ApiResponse<>(200, false, toDTO(profileEntity));
    }

    /**
     * Change password
     */
    public ApiResponse<String> profileChangPassword(ProfileChangPasswordDTO requestDTO) {
        ProfileAndRoleResponseDTO profileEntity = EntityDetails.getCurrentProfile();
        if (!profileEntity.getPassword().equals(MD5Util.getMd5(requestDTO.getOldPassword()))) {
            return ApiResponse.bad("Wrong password");
        }
        String securedNewPassword = MD5Util.getMd5(requestDTO.getNewPassword());
        profileEntity.setPassword(securedNewPassword);
        profileRepository.updatePassword(securedNewPassword, profileEntity.getId());
        return new ApiResponse<>(200, false);
    }

    public ApiResponse<String> setAdminRole(String profileId) {
        ProfileEntity profileEntity = get(profileId);
        AuthenticationResponse dto = new AuthenticationResponse();
        dto.setRole(List.of(ProfileRole.ROLE_ADMIN));
        roleService.creteAuthRole(dto, profileEntity);
        return new ApiResponse<>(200, false);
    }


    /**
     * Reset password
     */
    public ApiResponse<String> resetPasswordRequest() {
        ProfileAndRoleResponseDTO profileEntity = EntityDetails.getCurrentProfile();
        String tempPassword = RandomUtil.getRandomPassword();
        profileEntity.setTempPassword(tempPassword);
        profileRepository.addTempPassword(MD5Util.getMd5(tempPassword), profileEntity.getId());
        smsService.sendResetPasswordSmsCode(profileEntity.getPhone(), tempPassword);
        return new ApiResponse<>(200, false);
    }

    public ApiResponse<Boolean> resetPasswordConfirm(SmsCodeConfirmDTO dto) {
        ProfileAndRoleResponseDTO currentProfile = EntityDetails.getCurrentProfile();
        if (currentProfile.getTempPassword() != null && currentProfile.getTempPassword().equals(MD5Util.getMd5(dto.getCode()))) {
            //smsService.confirmSmsCode(currentProfile.getPhone(), dto.getCode()); // confirm that 2 minute not left
            profileRepository.updatePasswordToTempPassword(currentProfile.getId());
            return ApiResponse.ok();
        }
        return ApiResponse.bad("Wrong credentials");
    }

    /**
     * Change phone
     */
    public ApiResponse<String> changPhoneRequest(ProfileChangePhoneDTO dto) {
        String profileId = EntityDetails.getCurrentProfileId();
        long count = profileRepository.countByPhoneAndIdNotAndVisibleIsTrue(dto.getNewPhone(), profileId);
        if (count != 0) {
            return ApiResponse.ok("Phone is used");
        }
        profileRepository.addNewPhone(dto.getNewPhone(), profileId);
        smsService.sendConfirmSmsCode(dto.getNewPhone());
        return new ApiResponse<>(200, false);
    }

    public ApiResponse<Boolean> changPhoneConfirm(SmsCodeConfirmDTO dto) {
        String profileId = EntityDetails.getCurrentProfileId();
        ProfileEntity profile = get(profileId);
        smsService.confirmSmsCode(profile.getTempPhone(), dto.getCode());  // confirm sms
        profileRepository.updatePhoneToTempPhone(profile.getId()); // update phone
        return ApiResponse.ok();
    }

    public ProfileDTO getProfileShortInfo(String profileId) {
        ProfileEntity entity = get(profileId);
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setId(profileId);
        profileDTO.setFirstName(entity.getFirstName());
        profileDTO.setLastName(entity.getLastName());
        profileDTO.setPhone(entity.getPhone());
        return profileDTO;
    }


    private ProfileResponseDTO toDTO(ProfileEntity profile) {
        ProfileResponseDTO responseDTO = new ProfileResponseDTO();
        responseDTO.setId(profile.getId());
        responseDTO.setFirstName(profile.getFirstName());
        responseDTO.setLastName(profile.getLastName());
//        responseDTO.setRole(profile.getRole());
        responseDTO.setPhone(profile.getPhone());
        responseDTO.setStatus(profile.getStatus());
        responseDTO.setCreateDate(profile.getCreatedDate());
        return responseDTO;
    }

    private ProfileResponseDTO toDTO(ProfileMapper profile) {
        ProfileResponseDTO responseDTO = new ProfileResponseDTO();
        responseDTO.setId(profile.getPro_id());
        responseDTO.setFirstName(profile.getPro_firstName());
        responseDTO.setLastName(profile.getPro_lastName());
        responseDTO.setPhone(profile.getPro_phone());
        responseDTO.setStatus(profile.getPro_status());
        responseDTO.setRoleList(profile.getRoleList());
        return responseDTO;
    }

    public ProfileEntity get(String profileId) {
        return profileRepository.findById(profileId).orElseThrow(() -> new ItemNotFoundException("Profile by id not found"));
    }

    public ProfileEntity getByPhone(String phone) {
        return profileRepository.findTop1ByPhoneAndStatusAndVisibleTrue(phone,GlobalStatus.ACTIVE)
                .orElseThrow(() -> new ItemNotFoundException("Profile not found"));
    }

    public Boolean restartSms(ProfileRestartSmsDTO dto) {

        return null;
    }

    private void check_requested_roles(List<ProfileRole> requestRole, List<ProfileRoleDTO> jwtRole) {

        long count = jwtRole.stream().filter(role -> role.getRole().equals(ProfileRole.ROLE_ADMIN)).count();
        if (count != 0) {
            if (requestRole.size() != 1 || !requestRole.get(0).equals(ProfileRole.ROLE_MODERATOR)) {
                throw new AppBadRequestException("You do not want right permission.");
            }
        }

        count = jwtRole.stream().filter(role -> role.getRole().equals(ProfileRole.ROLE_MODERATOR)).count();
        if (count != 0) {
            count = requestRole.stream().filter(role -> {
                if (!role.equals(ProfileRole.ROLE_USER)) {
                    return true;
                }
                return false;
            }).count();

            if (count != 0) {
                throw new AppBadRequestException("You do not want right permission.");
            }
        }
    }

    private ProfileEntity crate_profileEntity_from_dto(ProfileRequestDTO dto) {
        ProfileEntity entity = new ProfileEntity();
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setPhone(dto.getPhone());
        String pas = MD5Util.getMd5(dto.getPassword());
        entity.setPassword(pas);
        entity.setStatus(GlobalStatus.ACTIVE);
        return entity;
    }


}

