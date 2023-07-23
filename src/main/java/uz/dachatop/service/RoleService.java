package uz.dachatop.service;
//PROJECT NAME --> api.dachatop
// YEAR --> 2023
// MONTH --> 02
// DAY --> 26
// TIME --> 13:54


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.dachatop.dto.auth.AuthenticationResponse;
import uz.dachatop.dto.profile.ProfileAndRoleResponseDTO;
import uz.dachatop.dto.profile.ProfileRequestDTO;
import uz.dachatop.entity.ProfileEntity;
import uz.dachatop.entity.ProfileRoleEntity;
import uz.dachatop.enums.ProfileRole;
import uz.dachatop.repository.ProfileRoleRepository;

import java.util.LinkedList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoleService {

    private final ProfileRoleRepository profileRoleRepository;


    public void creteRole(ProfileRequestDTO dto, ProfileEntity profileEntity) {
        List<ProfileRoleEntity> profileRoleEntityList = new LinkedList<>();
        for (ProfileRole role : dto.getRoleList()) {
            ProfileRoleEntity profileRoleEntity = new ProfileRoleEntity();
            profileRoleEntity.setProfileId(profileEntity.getId());
            profileRoleEntity.setRole(role);
            profileRoleEntityList.add(profileRoleEntity);
        }
        profileRoleRepository.saveAll(profileRoleEntityList);
    }


    public void creteAuthRole(AuthenticationResponse dto, ProfileEntity profileEntity) {
        List<ProfileRoleEntity> profileRoleEntityList = new LinkedList<>();
        for (ProfileRole role : dto.getRole()) {
            ProfileRoleEntity profileRoleEntity = new ProfileRoleEntity();
            profileRoleEntity.setProfileId(profileEntity.getId());
            profileRoleEntity.setRole(role);
            profileRoleEntityList.add(profileRoleEntity);
        }
        profileRoleRepository.saveAll(profileRoleEntityList);
    }

    public ProfileAndRoleResponseDTO toDTO(ProfileEntity entity) {
        ProfileAndRoleResponseDTO dto = new ProfileAndRoleResponseDTO();
        dto.setId(entity.getId());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setPassword(entity.getPassword());
        dto.setPhone(entity.getPhone());
        dto.setStatus(entity.getStatus());
//       dto.setRoleList();


        return dto;
    }

    public ProfileEntity toDTO(ProfileAndRoleResponseDTO dto) {
        ProfileEntity entity = new ProfileEntity();
        entity.setId(dto.getId());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setPassword(dto.getPassword());
        entity.setPhone(dto.getPhone());
        entity.setStatus(dto.getStatus());
//       dto.setRoleList();

        return entity;
    }

    public List<ProfileRole> getRoles(String id) {
        List<ProfileRole> roleList = new LinkedList<>();

        List<ProfileRoleEntity> byProfileId = profileRoleRepository.findByProfileIdAndVisibleTrue(id);

        byProfileId.forEach(profileRoleEntity -> {
            ProfileRoleEntity role = new ProfileRoleEntity();
            role.setRole(profileRoleEntity.getRole());
            roleList.add(role.getRole());
        });

        return roleList;
    }


}
