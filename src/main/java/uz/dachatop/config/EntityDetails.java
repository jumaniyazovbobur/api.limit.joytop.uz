package uz.dachatop.config;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import uz.dachatop.dto.profile.ProfileAndRoleResponseDTO;
import uz.dachatop.entity.ProfileEntity;

import java.util.Objects;

public class EntityDetails {
    public static ProfileAndRoleResponseDTO getCurrentProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal().equals("anonymousUser")) {
            return null;
        }
        CustomUserDetails details = (CustomUserDetails) authentication.getPrincipal();
        return details.getProfile();
    }

    public static String getCurrentProfileId() {
        return Objects.requireNonNull(getCurrentProfile()).getId();
    }

    public static ProfileAndRoleResponseDTO getAdminEntity() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal().equals("anonymousUser")) {
            return null;
        }
        CustomUserDetails details = (CustomUserDetails) authentication.getPrincipal();
        return details.getProfile();
    }
}
