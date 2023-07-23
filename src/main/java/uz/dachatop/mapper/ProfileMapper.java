package uz.dachatop.mapper;

import lombok.Getter;
import lombok.Setter;
import uz.dachatop.enums.ProfileRole;

import java.util.List;
@Getter
@Setter
public class ProfileMapper extends ProfileCustomMapper{
    private List<ProfileRole> roleList;

}
