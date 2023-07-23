package uz.dachatop.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uz.dachatop.dto.profile.ProfileAndRoleResponseDTO;
import uz.dachatop.dto.profile.ProfileRoleDTO;
import uz.dachatop.entity.ProfileEntity;

import java.util.*;
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomUserDetails implements UserDetails {
    private ProfileEntity profile;
    private ProfileAndRoleResponseDTO profileEntity;


//    public CustomUserDetails(ProfileAndRoleResponseDTO profileEntity) {
//        this.profileEntity = profileEntity;
//    }


//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        List<GrantedAuthority> list = new LinkedList<>();
//        list.add(new SimpleGrantedAuthority(profileEntity.getRole().name()));
//        return list;
//    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (profileEntity == null) {
            return new LinkedList<>(Collections.singletonList(new SimpleGrantedAuthority(profileEntity.getRoleList().toString())));
        }
        List<SimpleGrantedAuthority> role = new ArrayList<>();
        for (ProfileRoleDTO roleDTO : profileEntity.getRoleList()) {
            role.add(new SimpleGrantedAuthority(roleDTO.getRole().name()));
        }
        return role;
//        return new LinkedList<>(Collections.singletonList(new SimpleGrantedAuthority(employee.getRole().name())));
    }

    @Override
    public String getPassword() {
        return profileEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return profileEntity.getPhone();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public ProfileAndRoleResponseDTO getProfile() {
        return profileEntity;
    }
}
