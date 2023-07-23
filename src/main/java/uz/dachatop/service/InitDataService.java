package uz.dachatop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;
import uz.dachatop.entity.ProfileEntity;
import uz.dachatop.entity.RegionEntity;
import uz.dachatop.enums.GlobalStatus;
import uz.dachatop.enums.ProfileRole;
import uz.dachatop.enums.UserType;
import uz.dachatop.repository.*;
import uz.dachatop.repository.place.PlaceConvenienceRepository;
import uz.dachatop.repository.place.apartment.ApartmentRepository;
import uz.dachatop.util.MD5Util;

import java.time.LocalDateTime;

@RestController
@Slf4j
@RequiredArgsConstructor
public class InitDataService {
    private final RegionRepository regionRepository;

    @Value("${attach.upload.folder}")
    private String attachFolder;
    private final AttachRepository attachRepository;
    private final ProfileRepository userRepository;

    public String init_admin() {
        log.info("Init Admin");

        ProfileEntity profile = ProfileEntity
                .builder()
                .userType(UserType.GIVER)  // employee
                .firstName("Adminjon")
                .lastName("Adminjonov")
                .phone("998915721213")
                .password(MD5Util.getMd5("123456"))
//                .role(ProfileRole.ROLE_ADMIN)
                .status(GlobalStatus.ACTIVE)
                .build();

        profile.setVisible(true);
        profile.setCreatedDate(LocalDateTime.now());
        userRepository.save(profile);

        return profile.getId();
    }

    public String init_profile() {
        log.info("Init User");

        ProfileEntity profile = ProfileEntity
                .builder()
                .userType(UserType.GIVER)  // employee
                .firstName("Userjon")
                .lastName("Userjonov")
                .phone("998915723421")
                .password(MD5Util.getMd5("123456"))
//                .role(ProfileRole.ROLE_USER)
                .status(GlobalStatus.ACTIVE)
                .build();

        profile.setVisible(true);
        profile.setCreatedDate(LocalDateTime.now());
        userRepository.save(profile);

        return profile.getId();
    }

    public Long init_region() {
        RegionEntity region = RegionEntity
                .builder()
                .nameRu("Город Ташкент")
                .nameEn("Tashkent Region")
                .nameUz("Toshkent shahar")
                .build();

        region.setVisible(true);
        region.setCreatedDate(LocalDateTime.now());

        regionRepository.save(region);

        return region.getId();
    }

}

//    public Long init_apartment(List<String> images, Long regionId, Long districtId, String userId) {
//   /* public Long init_apartment(List<String> images, Long regionId, Long districtId, String  userId) {
//
//        ApartmentEntity apartment = ApartmentEntity
//                .builder()
//                .images(images)
//                .videoUrl("")
//                .regionId(regionId)
//                .districtId(districtId)
//                .latitude(37.560555) // 37.560555
//                .longitude(66.600281)
//                .countOfRooms(4)
//                .allArea(234)
//                .singleBad(5)
//                .AreaSingleBad(3222)
//                .doubleBad(3)
//                .countOfGuests(2)
//                .simplePriceForDay(3232L)
//                .priceOnSale(232L)
//                .priceForWeekendForaDay(2323L)
//                .gageOfDeposit(565784L)
//                .enterTime("")
//                .until("")
//                .maximumDayBooking(7)
//                .minimumDayBooking(1)
//                .smoking(Boolean.FALSE)
//                .alcohol(Boolean.FALSE)
//                .pets(Boolean.FALSE)
//                .availableOnlyFamily(Boolean.TRUE)
//                .loudlyMusic(Boolean.TRUE)
//                .party(Boolean.TRUE)
//                .ownerId(userId)
//                .recommended(Boolean.FALSE)
//                .superPrice(Boolean.FALSE)
//                .superComfort(Boolean.FALSE)
//                .isBusy(Boolean.FALSE)
//                .build();
//
//        apartmentRepository.save(apartment);
//
//        return apartment.getId();
//    }*/
//
////    public Long init_district(Long regionId) {
////        DistrictEntity district = DistrictEntity.builder()
////                .regionId(regionId)
////                .nameRu("Чиланзар")
////                .nameEn("Chilanzar")
////                .nameUz("Chilonzor")
////                .build();
////
////        district.setVisible(true);
////        district.setCreatedDate(LocalDateTime.now());
////        districtRepository.save(district);
////
////        return district.getId();
////    }
//
//    public Long init_dacha(List<String> images, Long district, Long region, String user) {
//        DachaEntity dacha = DachaEntity.builder()
//                .dachaType(PointType.DACHA)
//                .animal(Boolean.FALSE)
//                .alcohol(Boolean.FALSE)
//                .arrivalTime("")
//                .apartment(4)
//                .gage(2)
//                .home(2L)
//                .countRoom(5)
//                .discountPrice(500)
//                .images(images)
//                .isBusy(Boolean.FALSE)
//                .districtId(district)
//                .doubleBad(2)
//                .hostCount(4)
//                .latitude(37.560555) // 37.560555
//                .longitude(66.600281) // 66.600281
//                .leftTime("")
//                .livingArea(52343411)
//                .loudlyMusic(Boolean.TRUE)
//                .maxDayBook(15)
//                .maxDayBookHours(3)
//                .onlyFamily(Boolean.TRUE)
//                .ownerId(user)
//                .party(Boolean.TRUE)
//                .price(500000)
//                .recommended(Boolean.FALSE)
//                .regionId(region)
//                .singleBad(1)
//                .smoking(Boolean.TRUE)
//                .specialPrice(5200)
//                .street("Chortoq ko'chasi")
//                .superComfort(Boolean.FALSE)
//                .superPrice(Boolean.FALSE)
//                .title("Dacha bor Arzon narxda")
//                .totalArea(23456789)
//                .videoUrl("")
//                .build();
//
//        dacha.setVisible(true);
//        dacha.setCreatedDate(LocalDateTime.now());
//
//        dachaRepository.save(dacha);
//
//        return dacha.getId();
//    }
//
//    public Long init_convenience() {
//        ConvenienceEntity convenience = ConvenienceEntity.builder()
//                .nameEn("pool (summer, winter) 8*4")
//                .nameRu("бассейн (летний, зимний) 8*4")
//                .nameUz("basseyn (yozgi, qishgi) 8*4")
//                .build();
//
//        convenience.setVisible(true);
//        convenience.setCreatedDate(LocalDateTime.now());
//
//        convenienceRepository.save(convenience);
//
//        return convenience.getId();
//    }
//
//    public Long init_dacha_convenience(Long convenienceId,Long dachaId){
//        DachaConvenienceEntity dachaConvenience = DachaConvenienceEntity
//                .builder()
//                .convenienceId(convenienceId)
//                .dachaId(dachaId)
//                .build();
//
//        dachaConvenience.setVisible(Boolean.TRUE);
//        dachaConvenience.setCreatedDate(LocalDateTime.now());
//
//        dachaConvenienceRepository.save(dachaConvenience);
//
//        return dachaConvenience.getId();
//    }
//
//    public Long init_apartment_convenience(Long convenienceId,Long apartment){
//        ApartmentConvenienceEntity apartmentConvenience = ApartmentConvenienceEntity
//                .builder()
//                .convenienceId(convenienceId)
//                .apartmentId(apartment)
//                .build();
//
//        apartmentConvenience.setVisible(Boolean.TRUE);
//        apartmentConvenience.setCreatedDate(LocalDateTime.now());
//
//        apartmentConvenienceRepository.save(apartmentConvenience);
//
//        return apartmentConvenience.getId();
//    }
//
//    public Long init_hotel_convenience(Long convenienceId,Long hotelId){
//        HotelConvenienceEntity hotelConvenience = HotelConvenienceEntity
//                .builder()
//                .convenienceId(convenienceId)
//                .hotelId(hotelId)
//                .build();
//
//        hotelConvenience.setVisible(Boolean.TRUE);
//        hotelConvenience.setCreatedDate(LocalDateTime.now());
//
//        hotelConvenienceRepository.save(hotelConvenience);
//
//        return hotelConvenience.getId();
//    }
//
//    public String init_attach() {
//
//        log.info("Init attach");
//
//        AttachEntity entity = new AttachEntity();
//
//        String pathFolder = "2022/09/22";
//
//        entity.setPath(pathFolder);
//        entity.setOriginalName("islam.jpg");
//        entity.setExtension("jpg");
//        entity.setSize(82L);
//        entity.setVisible(true);
//
//        attachRepository.save(entity);
//
//
//        File folder = new File(attachFolder + pathFolder);
//
//        if (!folder.exists()) {
//            boolean create = folder.mkdirs();
//        }
//
//        try {
//            byte[] bytes = Files.readAllBytes(Paths.get("src/main/resources/images/islam.jpg"));
//
//            Path path = Paths.get(attachFolder + pathFolder + "/" + entity.getId() + ".jpg");
//
//            Files.write(path, bytes);
//
//        } catch (IOException e) {
//            log.warn("Upload Attach Exception = {}", e.getMessage());
//
//        }
//
//        return entity.getId();
//    }
//
//    public List<String> init_attach_list() {
//
//        List<String> attachIdList = new LinkedList<>();
//
//        log.info("Init attach");
//
//        AttachEntity entity = new AttachEntity();
//
//        String pathFolder = "2022/09/22";
//
//        entity.setPath(pathFolder);
//        entity.setOriginalName("islam.jpg");
//        entity.setExtension("jpg");
//        entity.setSize(82L);
//        entity.setVisible(true);
//
//        attachRepository.save(entity);
//
//        attachIdList.add(entity.getId());
//
//        File folder = new File(attachFolder + pathFolder);
//
//        if (!folder.exists()) {
//            boolean create = folder.mkdirs();
//        }
//
//        try {
//            byte[] bytes = Files.readAllBytes(Paths.get("src/main/resources/images/islam.jpg"));
//
//            Path path = Paths.get(attachFolder + pathFolder + "/" + entity.getId() + ".jpg");
//
//            Files.write(path, bytes);
//
//        } catch (IOException e) {
//            log.warn("Upload Attach Exception = {}", e.getMessage());
//
//        }
//
//        return attachIdList;
//    }
//
//
//}
