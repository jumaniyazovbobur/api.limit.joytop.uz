package uz.dachatop.repository.filter;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import uz.dachatop.dto.profile.ProfileFilterDTO;
import uz.dachatop.dto.profile.ProfileFilterResponseDTO;
import uz.dachatop.dto.profile.ProfileFilterResult;
import uz.dachatop.dto.profile.ProfilePaginationDTO;
import uz.dachatop.dto.subscription.SubscriptionFilterResult;
import uz.dachatop.entity.ProfileEntity;
import uz.dachatop.enums.GlobalStatus;
import uz.dachatop.enums.PlaceType;
import uz.dachatop.enums.ProfileRole;
import uz.dachatop.enums.SubscriptionStatus;
import uz.dachatop.mapper.ProfileCustomMapper;
import uz.dachatop.mapper.ProfileMapper;
import uz.dachatop.mapper.SubscriptionMapper;
import uz.dachatop.util.MapperUtil;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class ProfileFilterRepository {
    private final EntityManager entityManager;

    public ProfileFilterResult<ProfileMapper> getFilter(ProfileFilterDTO dto, int size, int page) {
        StringBuilder builder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        builder.append(" where p.status = 'ACTIVE' and p.visible = true ");

        if (dto.getPhone() != null) {
            builder.append(" and p.phone =:phone ");
            params.put("phone", dto.getPhone());
        }
        if (dto.getFirstName() != null) {
            builder.append(" and p.first_name = :firstName ");
            params.put("firstName", dto.getFirstName());
        }
        if (dto.getLastName() != null) {
            builder.append(" and p.last_name = :lastName ");
            params.put("lastName", dto.getLastName());
        }

        StringBuilder selectQueryBuilder = new StringBuilder();
        selectQueryBuilder.append("""
                select p.id, p.first_name, p.last_name,
                 p.phone, p.status,
                 pr.role
                 from profile p
                 inner join profile_role pr on pr.profile_id=p.id
                 """);

        selectQueryBuilder.append(builder);
        selectQueryBuilder.append("""
                order by p.created_date desc""");
        selectQueryBuilder.append(" limit ").append(page).append(" offset ").append(size).append(";");

        String countQueryBuilder = "select count(*) from profile as p " + builder + ";";

        Query selectQuery = entityManager.createNativeQuery(selectQueryBuilder.toString());
        Query countQuery = entityManager.createNativeQuery(countQueryBuilder.toString());

        for (Map.Entry<String, Object> param : params.entrySet()) {
            selectQuery.setParameter(param.getKey(), param.getValue());
            countQuery.setParameter(param.getKey(), param.getValue());
        }

        List<Object[]> apartmentList = selectQuery.getResultList();
        Long totalCount = (Long) countQuery.getSingleResult();

        List<ProfileMapper> mapperList = new LinkedList<>();
        Map<String, ProfileMapper> prMap = new HashMap<>();
        for (Object[] object : apartmentList) {
            ProfileMapper mapperDTO = new ProfileMapper();
            mapperDTO.setPro_id(MapperUtil.getStringValue(object[0]));
            mapperDTO.setPro_firstName(MapperUtil.getStringValue(object[1]));
            mapperDTO.setPro_lastName(MapperUtil.getStringValue(object[2]));
            mapperDTO.setPro_phone(MapperUtil.getStringValue(object[3]));
            mapperDTO.setPro_status(GlobalStatus.valueOf(MapperUtil.getStringValue(object[4])));

            if (prMap.containsKey(mapperDTO.getPro_id())) {
                List<ProfileRole> rolRole = prMap.get(mapperDTO.getPro_id()).getRoleList();
                rolRole.add(ProfileRole.valueOf(MapperUtil.getStringValue(object[5])));
            } else {
                List<ProfileRole> roleList = new ArrayList<>();
                roleList.add(ProfileRole.valueOf(MapperUtil.getStringValue(object[5])));
                mapperDTO.setRoleList(roleList);

                prMap.put(mapperDTO.getPro_id(), mapperDTO);
            }
        }
        prMap.forEach((s, customMapper) -> {
            mapperList.add(customMapper);
        });
        return new ProfileFilterResult(mapperList, totalCount);
    }
}
