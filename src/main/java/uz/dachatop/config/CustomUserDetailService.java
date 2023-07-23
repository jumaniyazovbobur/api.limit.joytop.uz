package uz.dachatop.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import uz.dachatop.dto.profile.ProfileAndRolePaginationDTO;
import uz.dachatop.dto.profile.ProfileCustomRequestDTO;
import uz.dachatop.entity.ProfileEntity;
import uz.dachatop.enums.GlobalStatus;
import uz.dachatop.repository.ProfileRepository;
import uz.dachatop.repository.filter.ProfileCustomRepository;
import uz.dachatop.service.RoleService;

import java.util.Optional;

@Component
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ProfileCustomRepository customRepository;

//    @Override
//    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
//        Optional<ProfileEntity> optional = profileRepository.findByPhoneAndVisibleTrue(phone);
//        if (optional.isEmpty()) {
//            throw new UsernameNotFoundException("Bad Credential");
//        }
//
//        ProfileEntity profile = optional.get();
////        ProfileAndRoleResponseDTO profile = optional.get();
//        ProfileAndRoleResponseDTO profileDTO = roleService.toDTO(profile);
//
//        return new CustomUserDetails(profileDTO);
//    }




    @Override
    public CustomUserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
//        Optional<ProfileEntity> entity = profileRepository.findByPhoneAndVisibleTrue(username);
        ProfileCustomRequestDTO requestDTO = new ProfileCustomRequestDTO();
        requestDTO.setPhone(phone);
        requestDTO.setStatus(GlobalStatus.ACTIVE);
        ProfileAndRolePaginationDTO optional = customRepository.getProfile(requestDTO, null, null);
        if (optional.getContent().size() == 0) {
            throw new UsernameNotFoundException("User Not Found");
        }

        return new CustomUserDetails(null, optional.getContent().get(0));
    }
}
