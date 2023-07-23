SELECT ap.id                    as                                        apartmentId,
       region.id                as                                        regionId,
       region.name_uz           as                                        regionNameUz,
       region.name_ru           as                                        regionNameRu,
       region.name_en           as                                        regionNameEn,
       district.id              as                                        districtId,
       district.name_uz         as                                        districtNameUz,
       district.name_ru         as                                        districtNameRu,
       district.name_en         as                                        districtNameEn,
       ap.latitude              as                                        latitude,
       ap.longitude             as                                        longitude,
       ap.week_day_price        as                                        weekDayPrice,
       ap.weekend_price         as                                        weekendPrice,
       ap.total_area            as                                        totalArea,
       ap.single_bed_room_count as                                        singleBedRoomCount,
       ap.double_bed_room_count as                                        doubleBedRoomCount,
       ap.type                  as                                        apartmentType,
       (select attach_id from apartment_attach where apartment_id = ap.id limit 1) as mianAttach
from apartment as ap
    inner join region
on region.id = ap.region_id
    inner join district on district.id = ap.district_id
where ap.profile_id =:profileId
  and ap.visible = true
    limit : size
offset :offset;


SELECT count(ap.id)
from inner
         join region
              on region.id = ap.region_id " +
            " inner join district
on district.id = ap.district_id " +
            " and ap.visible = true



select ap.id,
       region.id,
       case ? when 'uz' then region.name_uz when 'en' then region.name_en else region.name_ru end       as rName,
       district.id,
       case ? when 'uz' then district.name_uz when 'en' then district.name_en else district.name_ru end as dName,
       ap.latitude,
       ap.longitude,
       ap.week_day_price,
       ap.weekend_price,
       ap.total_area,
       ap.single_bed_room_count,
       ap.double_bed_room_count,
       ap.type,
       (select attach_id from apartment_attach where apartment_id = ap.id limit 1) as mainAttach
from apartment ap inner join region
on region.id = ap.region_id inner join district on district.id = ap.district_id
where ap.visible = true
  and ap.status = 'ACTIVE'
  and ap.region_id =?
  and ap.district_id =?
  and ap.week_day_price between ?
  and ?
  and ap.room_count =?
  and ap.single_bed_room_count=?
  and ap.double_bed_room_count=?
  and exists (select * from place_convenience where apartment_id = ap.id
  and convenience_id in (?
    , ?)) limit 10
offset 0