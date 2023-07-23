 select ap.id,
         region.id, case :lang when 'uz' then region.name_uz when 'en' then region.name_en else region.name_ru end as rName,
         district.id, case :lang when 'uz' then district.name_uz when 'en' then district.name_en else district.name_ru end as dName,
         ap.latitude, ap.longitude, exty.id,case :lang when 'uz' then exty.name_uz when 'en' then exty.name_en else exty.name_ru end as eName, ap.weekend_price,
         ap.week_day_price, ap.type,
         (select attach_id from place_attach where place_id = ap.id limit 1) as mainAttach,ap.created_date,
         (select string_agg(concat(case :lang when 'uz' then tariff.name_uz when 'en' then tariff.name_en else tariff.name_ru end,',',tariff.color) , '; ')
         from subscription s inner join subscription_tariff tariff on s.tariff_id = tariff.id where s.place_id = ap.id
         and s.place_type = 'EXTREME' and s.status = 'ACTIVE' )
         from extreme ap
         inner join region on region.id = ap.region_id
         inner join extreme_type as exty on exty.id = ap.extreme_type_id
         inner join district on district.id = ap.district_id