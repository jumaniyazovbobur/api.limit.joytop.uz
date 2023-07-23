package uz.dachatop.repository;
//PROJECT NAME --> api.dachatop
// YEAR --> 2023
// MONTH --> 02
// DAY --> 26
// TIME --> 13:55

import org.springframework.data.jpa.repository.JpaRepository;
import uz.dachatop.entity.ProfileRoleEntity;
import uz.dachatop.enums.ProfileRole;

import java.util.List;

public interface ProfileRoleRepository extends JpaRepository<ProfileRoleEntity,String> {

    List<ProfileRoleEntity> findByProfileIdAndVisibleTrue(String id);

}
