package uz.dachatop;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import uz.dachatop.dto.place.dacha.DachaCreateDTO;
import uz.dachatop.dto.place.dacha.DachaDTO;
import uz.dachatop.dto.place.extreme.ExtremeFilterDTO;
import uz.dachatop.entity.MessageEntity;
import uz.dachatop.entity.place.dacha.DachaEntity;
import uz.dachatop.enums.AppLanguage;
import uz.dachatop.enums.GlobalStatus;
import uz.dachatop.enums.PlaceSubType;
import uz.dachatop.repository.place.extreme.ExtremeCustomFilter;
import uz.dachatop.service.place.PlaceService;
import uz.dachatop.telegrambot.service.TelegramBotService;
import uz.dachatop.util.MD5Util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.Optional;

@SpringBootTest
class ApplicationTests {
    @Autowired
    private ExtremeCustomFilter service;

    @Autowired
    private TelegramBotService telegramBotService;

    @Test
    void contextLoads() {
//        YearMonth yearMonthObject = YearMonth.of(2023, 2);
//        int daysInMonth = yearMonthObject.lengthOfMonth(); //28
//        service.filter(new ExtremeFilterDTO("sdfsdfsdf"), AppLanguage.en,0,10);
//        System.out.println(MD5Util.getMd5("LobiMazgi"));
//        smsService.sendSms("998972322903");
//      smsService.sendSms("998998638201");
//        service.getByApartmentId("sadas").getData().forEach(apartmentCalendarDTO -> {
//            System.out.println(apartmentCalendarDTO);
//        });
//        System.out.println(ApartmentType.getLanguageEnum("Путешествовать"));
//        System.out.println(UUID.randomUUID().toString());
    }

//    @Test
//    void sendTelegramMessage() {
//        MessageEntity entity = new MessageEntity();
//        entity.setText("Test text");
//        entity.setPhone("+998912345678");
//        entity.setFromDay(LocalDate.of(2020, 02, 02));
//        entity.setToDay(LocalDate.of(2020, 02, 03));
//        entity.setPrice(1000L);
//        telegramBotService.sendMessageToGroup(entity);
//    }

//    @Test
//    void sendDachaCreatedMessage() {
//        // TODO check for required fields
//        DachaCreateDTO dacha = new DachaCreateDTO();
//        dacha.setNumber(23l);
//        dacha.setSubType(PlaceSubType.RENT);
//        dacha.setStatus(GlobalStatus.NOT_ACTIVE);
//        dacha.setSalePrice(123l);
//        dacha.setTotalArea(10);
//        dacha.setRoomCount(5);
//        dacha.setSingleBedRoomCount(2);
//        dacha.setDoubleBedRoomCount(3);
//        dacha.setWeekDayPrice(150L);
//        dacha.setPriceOnSale(110L);
//        dacha.setWeekendPrice(180L);
//        dacha.setGageOfDeposit(10L);
//        dacha.setEnterTime(LocalTime.of(17, 0, 0));
//        dacha.setDepartureTime(LocalTime.of(18, 0, 0));
//        dacha.setVideoUrl("https://www.youtube.com/");
//        dacha.setNumber(123L);
//        dacha.setDescription(" Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.");
//
//        telegramBotService.sendMessageForAdminAboutPlaces(dacha, "555");
//    }
}
