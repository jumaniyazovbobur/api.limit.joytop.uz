//package uz.dachatop.service;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.domain.Page;
//import org.springframework.stereotype.Service;
//import uz.dachatop.config.EntityDetails;
//import uz.dachatop.dto.profile.*;
//import uz.dachatop.dto.response.ApiResponse;
//import uz.dachatop.dto.sms.SmsCodeConfirmDTO;
//import uz.dachatop.entity.ProfileEntity;
//import uz.dachatop.enums.GlobalStatus;
//import uz.dachatop.enums.ProfileRole;
//import uz.dachatop.exp.AppBadRequestException;
//import uz.dachatop.exp.ItemNotFoundException;
//import uz.dachatop.repository.ProfileRepository;
//import uz.dachatop.repository.filter.ProfileFilterRepository;
//import uz.dachatop.util.MD5Util;
//import uz.dachatop.util.RandomUtil;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class ProfileService {
//    private final ProfileRepository profileRepository;
//    private final ProfileFilterRepository filterRepository;
//    private final SmsService smsService;
//
//    public ApiResponse<ProfileResponseDTO> getCurrentProfileInfo() {
//        ProfileEntity profile = EntityDetails.getCurrentProfile();
//        ProfileResponseDTO responseDTO = new ProfileResponseDTO();
//        responseDTO.setId(profile.getId());
//        responseDTO.setFirstName(profile.getFirstName());
//        responseDTO.setLastName(profile.getLastName());
//        responseDTO.setRole(profile.getRole());
//        responseDTO.setPhone(profile.getPhone());
//        responseDTO.setStatus(profile.getStatus());
//        responseDTO.setCreateDate(profile.getCreatedDate());
//        return ApiResponse.ok(responseDTO);
//        /*if (!(current.getGiverInfo() == null)) {
//            PassportInfoEntity passportInfoEntity = current.getGiverInfo().getPassportInfoEntity();
//            PaymentInfoEntity paymentInfoEntity = current.getGiverInfo().getPaymentInfoEntity();
//            profileDto.setPassportInfoDto(PassportInfoDTO.toDto(passportInfoEntity));
//            profileDto.setPaymentInfoDto(PaymentInfoDTO.toDto(paymentInfoEntity));
//        }*/
//    }
//
//    public ApiResponse<Object> updateCurrentProfile(ProfileDetailUpdateDTO dto) {
//        ProfileEntity currentProfile = EntityDetails.getCurrentProfile();
//        currentProfile.setFirstName(dto.getFirstName());
//        currentProfile.setLastName(dto.getLastName());
//        profileRepository.save(currentProfile);
//        return ApiResponse.ok();
//    }
//
//    public ApiResponse<ProfilePaginationDTO> profileFilter(ProfileFilterDTO filter, int page, int size) {
//        // filter
//        // return (id,name,surname,phone,created_date,role_list) with pagination
//        ProfileFilterResponseDTO filterResponseDTO = filterRepository.getFilter(filter, size, page);
//        List<ProfileResponseDTO> responseDTOList = new ArrayList<>();
//        filterResponseDTO.getEntityList().forEach(profileEntity -> {
//            responseDTOList.add(toDTO(profileEntity));
//        });
//        return new ApiResponse<>(200, false,new  ProfilePaginationDTO(filterResponseDTO.getTotalCount(), responseDTOList));
//    }
//
//    public ApiResponse<String> profileRegistration(ProfileRequestDTO dto) {
//        ProfileEntity profileEntity = new ProfileEntity();
//        profileEntity.setFirstName(dto.getFirstName());
//        profileEntity.setLastName(dto.getLastName());
//        profileEntity.setPassword(MD5Util.getMd5(dto.getPassword()));
//
//        Optional<ProfileEntity> byPhoneNumber = profileRepository.findByPhoneAndVisibleTrue(dto.getPhone());
//        if (byPhoneNumber.isPresent()) {
//            throw new AppBadRequestException("Phone unique");
//        } else {
//            profileEntity.setPhone(dto.getPhone());
//        }
//        profileEntity.setRole(ProfileRole.ROLE_USER);
//        profileEntity.setStatus(GlobalStatus.NOT_ACTIVE);
//        profileRepository.save(profileEntity);
//
//        smsService.sendRegistrationSmsCode(profileEntity.getPhone());
//        return new ApiResponse<>(200, false, "Successfully");
//    }
//
//
//    public ApiResponse<Boolean> deleteUser(String id) {
//        int i = profileRepository.deleteStatus(false, id);
//        return new ApiResponse<Boolean>(200, false, i > 0);
//    }
//
//    public ApiResponse<ProfileResponseDTO> getById(String id) {
//        ProfileEntity profileEntity = profileRepository.findByIdAndVisibleTrue(id).orElseThrow(() -> new ItemNotFoundException("Profile by id not found" + id));
//        return new ApiResponse<>(200, false, toDTO(profileEntity));
//    }
//
//    /**
//     * Change password
//     */
//    public ApiResponse<String> profileChangPassword(ProfileChangPasswordDTO requestDTO) {
//        ProfileEntity profileEntity = EntityDetails.getCurrentProfile();
//        if (!profileEntity.getPassword().equals(MD5Util.getMd5(requestDTO.getOldPassword()))) {
//            return ApiResponse.bad("Wrong password");
//        }
//        String securedNewPassword = MD5Util.getMd5(requestDTO.getNewPassword());
//        profileEntity.setPassword(securedNewPassword);
//        profileRepository.updatePassword(securedNewPassword, profileEntity.getId());
//        return new ApiResponse<>(200, false);
//    }
//
//
//    /**
//     * Reset password
//     */
//    public ApiResponse<String> resetPasswordRequest() {
//        ProfileEntity profileEntity = EntityDetails.getCurrentProfile();
//        String tempPassword = RandomUtil.getRandomPassword();
//        profileEntity.setTempPassword(tempPassword);
//        profileRepository.addTempPassword(MD5Util.getMd5(tempPassword), profileEntity.getId());
//        smsService.sendResetPasswordSmsCode(profileEntity.getPhone(), tempPassword);
//        return new ApiResponse<>(200, false);
//    }
//
//    public ApiResponse<Boolean> resetPasswordConfirm(SmsCodeConfirmDTO dto) {
//        ProfileEntity currentProfile = EntityDetails.getCurrentProfile();
//        if (currentProfile.getTempPassword() != null && currentProfile.getTempPassword().equals(MD5Util.getMd5(dto.getCode()))) {
//            //smsService.confirmSmsCode(currentProfile.getPhone(), dto.getCode()); // confirm that 2 minute not left
//            profileRepository.updatePasswordToTempPassword(currentProfile.getId());
//            return ApiResponse.ok();
//        }
//        return ApiResponse.bad("Wrong credentials");
//    }
//
//    /**
//     * Change phone
//     */
//    public ApiResponse<String> changPhoneRequest(ProfileChangePhoneDTO dto) {
//        String profileId = EntityDetails.getCurrentProfileId();
//        long count = profileRepository.countByPhoneAndIdNotAndVisibleIsTrue(dto.getNewPhone(), profileId);
//        if (count != 0) {
//            return ApiResponse.ok("Phone is used");
//        }
//        profileRepository.addNewPhone(dto.getNewPhone(), profileId);
//        smsService.sendConfirmSmsCode(dto.getNewPhone());
//        return new ApiResponse<>(200, false);
//    }
//
//    public ApiResponse<Boolean> changPhoneConfirm(SmsCodeConfirmDTO dto) {
//        String profileId = EntityDetails.getCurrentProfileId();
//        ProfileEntity profile = get(profileId);
//        smsService.confirmSmsCode(profile.getTempPhone(), dto.getCode());  // confirm sms
//        profileRepository.updatePhoneToTempPhone(profile.getId()); // update phone
//        return ApiResponse.ok();
//    }
//
//    public ProfileDTO getProfileShortInfo(String profileId) {
//        ProfileEntity entity = get(profileId);
//        ProfileDTO profileDTO = new ProfileDTO();
//        profileDTO.setId(profileId);
//        profileDTO.setFirstName(entity.getFirstName());
//        profileDTO.setLastName(entity.getLastName());
//        profileDTO.setPhone(entity.getPhone());
//        return profileDTO;
//    }
//
//
//    private ProfileResponseDTO toDTO(ProfileEntity profile) {
//        ProfileResponseDTO responseDTO = new ProfileResponseDTO();
//        responseDTO.setId(profile.getId());
//        responseDTO.setFirstName(profile.getFirstName());
//        responseDTO.setLastName(profile.getLastName());
//        responseDTO.setRole(profile.getRole());
//        responseDTO.setPhone(profile.getPhone());
//        responseDTO.setStatus(profile.getStatus());
//        responseDTO.setCreateDate(profile.getCreatedDate());
//        return responseDTO;
//    }
//
//    private ProfileEntity get(String profileId) {
//        return profileRepository.findById(profileId).orElseThrow(() -> new ItemNotFoundException("Profile by id not found"));
//    }
//}
