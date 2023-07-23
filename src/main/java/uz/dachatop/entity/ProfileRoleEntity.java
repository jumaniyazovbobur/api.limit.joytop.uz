package uz.dachatop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import uz.dachatop.entity.base.BaseStringIdEntity;
import uz.dachatop.enums.ProfileRole;

@Getter
@Setter
@Entity
@Table(name = "profile_role")
public class ProfileRoleEntity extends BaseStringIdEntity {
     @Column(name = "role")
     @Enumerated(EnumType.STRING)
     private ProfileRole role;



     @Column(name = "profile_id")
     private String profileId;
     @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
     @JoinColumn(name = "profile_id", insertable = false, updatable = false)
     private ProfileEntity profile;
}
