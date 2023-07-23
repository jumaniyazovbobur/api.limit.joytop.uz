package uz.dachatop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.dachatop.entity.*;
import uz.dachatop.entity.place.apartment.ApartmentEntity;
import uz.dachatop.enums.PlaceType;
import uz.dachatop.enums.GlobalStatus;
import uz.dachatop.enums.ProfileRole;
import uz.dachatop.repository.place.apartment.ApartmentRepository;
import uz.dachatop.repository.AttachRepository;
import uz.dachatop.repository.ProfileRepository;
import uz.dachatop.util.MD5Util;

import java.time.LocalDateTime;
import java.time.LocalTime;

// PROJECT NAME -> api.dachatop
// TIME -> 21:10
// DATE -> 06/02/23
@Slf4j
@Deprecated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/init")
public class InitDataController {
    private final ApartmentRepository apartmentRepository;

    private final AttachRepository attachRepository;
    private final ProfileRepository profileRepository;


    @GetMapping("/start")
    public String initAll() {
        //  initAttach();
        initProfile();
        //  initApartment();
        return "Done";
    }

    private void initAttach() {
        String[][] attachList = {{"logo-c8d85e53-acde-4ca7-be39-5e794b4257e0", "logo_country", "2023/2/5"},
                {"1-c8d85e53-acde-4ca7-be39-5e794b4257e0", "banner", "2023/2/5"}, {"2-c8d85e53-acde-4ca7-be39-5e794b4257e0", "banner", "2023/2/5"}, {"3-c8d85e53-acde-4ca7-be39-5e794b4257e0", "banner", "2023/2/5"}, {"4-c8d85e53-acde-4ca7-be39-5e794b4257e0", "banner", "2023/2/5"}, {"11-c8d85e53-acde-4ca7-be39-5e794b4257e0", "banner", "2023/2/5"}, {"22-c8d85e53-acde-4ca7-be39-5e794b4257e0", "banner", "2023/2/5"}, {"33-c8d85e53-acde-4ca7-be39-5e794b4257e0", "banner", "2023/2/5"}, {"44-c8d85e53-acde-4ca7-be39-5e794b4257e0", "banner", "2023/2/5"}, {"default_profile_photo-4ca7-be39-5e794b4257e0", "banner", "2023/2/5"}};

        for (String[] arr : attachList) {
            AttachEntity attach1 = new AttachEntity();
            attach1.setId(arr[0]);
            attach1.setCreatedDate(LocalDateTime.now());
            attach1.setVisible(true);
            attach1.setExtension("jpg");
            attach1.setOriginalName(arr[1]);
            attach1.setPath(arr[2]);
            attach1.setSize(0L);
            attachRepository.save(attach1);
        }
    }

    private void initProfile() {
        ProfileEntity profile1 = new ProfileEntity();
        profile1.setId("2af00f41-a0d5-4856-90c0-1eb6e6d61103");
        profile1.setFirstName("Admin");
        profile1.setLastName("Adminjon");
        //  profile1.setRole(ProfileRole.ROLE_ADMIN);
        profile1.setPassword(MD5Util.getMd5("123456"));
        profile1.setPhone("998915721213");
        profile1.setStatus(GlobalStatus.ACTIVE);
        profileRepository.save(profile1);

        ProfileEntity profile2 = new ProfileEntity();
        profile2.setId("3c1eee2d-0ae9-409e-a027-950b27154570");
        profile2.setFirstName("User");
        profile2.setLastName("Userjon");
        // profile2.setRole(ProfileRole.ROLE_USER);
        profile2.setPassword(MD5Util.getMd5("123456"));
        profile2.setPhone("998915721415");
        profile2.setStatus(GlobalStatus.ACTIVE);
        profileRepository.save(profile2);


        ProfileEntity profile3 = new ProfileEntity();
        profile3.setId("848764d5-d030-4b6d-ade0-981cb6e15a89");
        profile3.setFirstName("Moderator");
        profile3.setLastName("Moderatorjon");
        //  profile3.setRole(ProfileRole.ROLE_MODERATOR);
        profile3.setPassword(MD5Util.getMd5("123456"));
        profile3.setPhone("998915721617");
        profile3.setStatus(GlobalStatus.ACTIVE);
        profileRepository.save(profile3);
    }

    private void initApartment() {
    /*    ApartmentEntity apartment1 = new ApartmentEntity();
        apartment1.setVideoUrl("");
        apartment1.setRegionId(1L);
        apartment1.setDistrictId(1L);
        apartment1.setStreet("Chimyon Tog' bag'ri");
        apartment1.setHouse("Chimyon");
        apartment1.setApartmentNum("34");
        apartment1.setLatitude(31.23456);
        apartment1.setLongitude(61.23453);
        apartment1.setType(PlaceType.CAMPING);
        apartment1.setName("Chimyon tog'i");
        apartment1.setRoomCount(3);
        apartment1.setTotalArea(123456);
        apartment1.setSingleBedRoomCount(8);
        apartment1.setDoubleBedRoomCount(4);
        apartment1.setWeekDayPrice(10L);
        apartment1.setPriceOnSale(10L);
        apartment1.setWeekendPrice(10L);
        apartment1.setGageOfDeposit(10L);
        apartment1.setEnterTime(LocalTime.now());
        apartment1.setDepartureTime(LocalTime.now());
        apartment1.setMaximumDayBooking(30);
        apartment1.setMinimumDayBooking(10);
        apartment1.setSmoking(Boolean.TRUE);
        apartment1.setAlcohol(Boolean.TRUE);
        apartment1.setPets(Boolean.TRUE);
        apartment1.setAvailableOnlyFamily(Boolean.TRUE);
        apartment1.setLoudlyMusic(Boolean.TRUE);
        apartment1.setParty(Boolean.TRUE);
        apartment1.setProfileId("2af00f41-a0d5-4856-90c0-1eb6e6d61103");
        apartment1.setProfileCardPan("8600 5678 9430 2345");
        apartment1.setStatus(GlobalStatus.ACTIVE);
        apartment1.setDeletedId("");
        apartment1.setDeletedDate(LocalDateTime.now());

        apartmentRepository.save(apartment1);

        ApartmentEntity apartment2 = new ApartmentEntity();
        apartment2.setVideoUrl("");
        apartment2.setRegionId(1L);
        apartment2.setDistrictId(1L);
        apartment2.setStreet("Samarkand ");
        apartment2.setHouse("Samarkand Non Bozor");
        apartment2.setApartmentNum("4");
        apartment2.setLatitude(31.23456);
        apartment2.setLongitude(61.23453);
        apartment2.setType(PlaceType.HOTEL);
        apartment2.setName("Samarkand shaxri");
        apartment2.setRoomCount(3);
        apartment2.setTotalArea(123456);
        apartment2.setSingleBedRoomCount(8);
        apartment2.setDoubleBedRoomCount(4);
        apartment2.setWeekDayPrice(10L);
        apartment2.setPriceOnSale(10L);
        apartment2.setWeekendPrice(10L);
        apartment2.setGageOfDeposit(10L);
        apartment2.setEnterTime(LocalTime.now());
        apartment2.setDepartureTime(LocalTime.now());
        apartment2.setMaximumDayBooking(30);
        apartment2.setMinimumDayBooking(10);
        apartment2.setSmoking(Boolean.TRUE);
        apartment2.setAlcohol(Boolean.TRUE);
        apartment2.setPets(Boolean.TRUE);
        apartment2.setAvailableOnlyFamily(Boolean.TRUE);
        apartment2.setLoudlyMusic(Boolean.TRUE);
        apartment2.setParty(Boolean.TRUE);
        apartment2.setProfileId("3c1eee2d-0ae9-409e-a027-950b27154570");
        apartment2.setProfileCardPan("8600 1254 8765 8954");
        apartment2.setStatus(GlobalStatus.ACTIVE);
        apartment2.setDeletedId("");
        apartment2.setDeletedDate(LocalDateTime.now());

        apartmentRepository.save(apartment2);

        ApartmentEntity apartment3 = new ApartmentEntity();
        apartment3.setVideoUrl("");
        apartment3.setRegionId(1L);
        apartment3.setDistrictId(1L);
        apartment3.setStreet("Tashkent city ");
        apartment3.setHouse("Tashkent");
        apartment3.setApartmentNum("34");
        apartment3.setLatitude(31.23456);
        apartment3.setLongitude(61.23453);
        apartment3.setType(PlaceType.CAMPING);
        apartment3.setName("Tashkent");
        apartment3.setRoomCount(3);
        apartment3.setTotalArea(123456);
        apartment3.setSingleBedRoomCount(8);
        apartment3.setDoubleBedRoomCount(4);
        apartment3.setWeekDayPrice(10L);
        apartment3.setPriceOnSale(10L);
        apartment3.setWeekendPrice(10L);
        apartment3.setGageOfDeposit(10L);
        apartment3.setEnterTime(LocalTime.now());
        apartment3.setDepartureTime(LocalTime.now());
        apartment3.setMaximumDayBooking(30);
        apartment3.setMinimumDayBooking(10);
        apartment3.setSmoking(Boolean.TRUE);
        apartment3.setAlcohol(Boolean.TRUE);
        apartment3.setPets(Boolean.TRUE);
        apartment3.setAvailableOnlyFamily(Boolean.TRUE);
        apartment3.setLoudlyMusic(Boolean.TRUE);
        apartment3.setParty(Boolean.TRUE);
        apartment3.setProfileId("848764d5-d030-4b6d-ade0-981cb6e15a89");
        apartment3.setProfileCardPan("8600 5678 0101 2345");
        apartment3.setStatus(GlobalStatus.ACTIVE);
        apartment3.setDeletedId("");
        apartment3.setDeletedDate(LocalDateTime.now());

        apartmentRepository.save(apartment3);*/
    }
}
