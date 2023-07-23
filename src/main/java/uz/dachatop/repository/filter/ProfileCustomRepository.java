package uz.dachatop.repository.filter;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import uz.dachatop.dto.profile.ProfileAndRolePaginationDTO;
import uz.dachatop.dto.profile.ProfileAndRoleResponseDTO;
import uz.dachatop.dto.profile.ProfileCustomRequestDTO;
import uz.dachatop.dto.profile.ProfileRoleDTO;
import uz.dachatop.mapper.ProfileCustomMapper;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProfileCustomRepository {
    private final EntityManager entityManager;

    public ProfileAndRolePaginationDTO getProfile(ProfileCustomRequestDTO requestDTO, Integer page, Integer size) {

        Query query = entityManager.createQuery(getFilter(requestDTO));
        List<ProfileCustomMapper> profileCustomMapperList = query.getResultList();
        return toDTO(profileCustomMapperList, page, size);
    }

    private String getFilter(ProfileCustomRequestDTO requestDTO) {
        StringBuilder sql = getSQL();
        if (requestDTO.getProfileId() != null) {
            sql.append(" and pro.id = '" + requestDTO.getProfileId() + "'");
        }
        if (requestDTO.getPhone() != null) {
            sql.append(" and pro.phone ='" + requestDTO.getPhone() + "'");
        }
        if (requestDTO.getStatus() != null) {
            sql.append(" and pro.status ='" + requestDTO.getStatus().name() + "'");
        }
        sql.append(" ORDER BY pro.id ASC");
        return sql.toString();
    }

    private StringBuilder getSQL() {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT new uz.dachatop.mapper.ProfileCustomMapper(");
        sql.append(" rol.id as  rol_id,");
        sql.append(" rol.role as rol_role,");
        sql.append(" rol.visible as rol_visible,");
        sql.append(" pro.id as pro_id,");
        sql.append(" pro.firstName as pro_firstName,");
        sql.append(" pro.lastName as pro_lastName,");
        sql.append(" pro.password as pro_password,");
        sql.append(" pro.phone as pro_phone,");
        sql.append(" pro.createdDate as pro_createdDate,");
        sql.append(" pro.visible as pro_visible,");
        sql.append(" pro.status as pro_status");
        sql.append(" ) FROM ProfileRoleEntity as rol ");
        sql.append(" inner join rol.profile as pro");
        sql.append(" where rol.visible = true ");
        return sql;
    }

    private ProfileAndRolePaginationDTO toDTO(List<ProfileCustomMapper> roleMappers, Integer page, Integer size) {
        int counts[] = new int[roleMappers.size()];
        int i = 0;
        int count = 0;
        ProfileCustomMapper role = null;
        for (ProfileCustomMapper roleMapper : roleMappers) {
            if (role == null) {
                role = roleMapper;
            }
            if (role.getPro_id().equals(roleMapper.getPro_id())) {
                count++;
            }
            if (!role.getPro_id().equals(roleMapper.getPro_id())) {
                i++;
                count = 1;
                role = roleMapper;
            }
            counts[i] = count;
        }
        List<ProfileAndRoleResponseDTO> employeeAndRoleList = new ArrayList<>();

        List<ProfileRoleDTO> roleList = new ArrayList<>();
        i = 0;
        count = 0;
        for (ProfileCustomMapper roleMapper : roleMappers) {
            count++;

            if (counts[i] == count) {
                roleList.add(new ProfileRoleDTO(roleMapper.getRol_id(), roleMapper.getRol_workId(), roleMapper.getRol_role(), roleMapper.getRol_visible()));


                i++;
                count = 0;

                employeeAndRoleList.add(toDTO(roleMapper, roleList));
                roleList = new ArrayList<>();
            } else {
                roleList.add(new ProfileRoleDTO(roleMapper.getRol_id(), roleMapper.getRol_workId(), roleMapper.getRol_role(), roleMapper.getRol_visible()));

            }

        }

        return getPageBle(employeeAndRoleList, page, size);
    }

    private ProfileAndRolePaginationDTO getPageBle(List<ProfileAndRoleResponseDTO> employeeAndRoleList, Integer page, Integer size) {
        if (page == null || size == null) {
            return new ProfileAndRolePaginationDTO(null, null, employeeAndRoleList);
        }
        List<ProfileAndRoleResponseDTO> responseList = new ArrayList<>();
        if (employeeAndRoleList.size() - page * size < 0) {
            return new ProfileAndRolePaginationDTO(null, null, responseList);
        }

        int count = 1;
        for (int i = page * size; i < employeeAndRoleList.size(); i++) {
            responseList.add(employeeAndRoleList.get(i));
            if (count == size) {
                break;
            }
            count++;
        }
        Long totalElements = (long) employeeAndRoleList.size();
        int totalPages = employeeAndRoleList.size() % size;


        if (totalPages > 0) {
            totalPages = (employeeAndRoleList.size()) / size + 1;
        } else {
            totalPages = (employeeAndRoleList.size()) / size;
        }


        return new ProfileAndRolePaginationDTO(totalElements, totalPages, responseList);
    }

    private ProfileAndRoleResponseDTO toDTO(ProfileCustomMapper roleMapper, List<ProfileRoleDTO> roleList) {
        ProfileAndRoleResponseDTO roleResponseDTO = new ProfileAndRoleResponseDTO();
        roleResponseDTO.setId(roleMapper.getPro_id());
        roleResponseDTO.setFirstName(roleMapper.getPro_firstName());
        roleResponseDTO.setLastName(roleMapper.getPro_lastName());
        roleResponseDTO.setPassword(roleMapper.getPro_password());
        roleResponseDTO.setPhone(roleMapper.getPro_phone());
        roleResponseDTO.setStatus(roleMapper.getPro_status());
        roleResponseDTO.setCreateDate(roleMapper.getPro_createdDate());
        roleResponseDTO.setRoleList(roleList);
        return roleResponseDTO;
    }
}
