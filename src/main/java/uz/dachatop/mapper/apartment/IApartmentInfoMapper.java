package uz.dachatop.mapper.apartment;

import uz.dachatop.enums.PlaceType;

public interface IApartmentInfoMapper {
    String getApartmentId();

    Long getRegionId();

    String getRegionName();

    Long getDistrictId();

    String getDistrictName();

    Double getLatitude();

    Double getLongitude();

    Long getWeekDayPrice();

    Long getWeekendPrice();

    Double getTotalArea();

    Integer getSingleBedRoomCount();

    Integer getDoubleBedRoomCount();

    PlaceType getApartmentType();
}
