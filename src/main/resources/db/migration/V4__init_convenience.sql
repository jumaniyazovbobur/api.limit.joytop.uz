INSERT INTO convenience(id, name_uz, name_en, name_ru, created_date, visible)
values (1, 'Wi-Fi internet', 'Wi-Fi internet', 'Wi-Fi интернет', now(), true),
       (2, 'Playstation 4', 'Playstation 4', 'Плейстейшен 4', now(), true),
       (3, 'Konditsioner', 'Air conditioner', 'Кондитционер', now(), true),
       (4, 'Isitgich(pechka)', 'Heating', 'Обогрев', now(), true),
       (5, 'Basseyn', 'Pool', 'Басейн', now(), true),
       (6, 'Sauna', 'Sauna', 'Сауна', now(), true),
       (7, 'Bilyard', 'Billiards', 'Бильярд', now(), true),
       (8, 'Stol Tennis', 'Table tennis', 'Настольный теннис', now(), true),
       (9, 'Karaoke', 'Karaoke', 'Караоке', now(), true),
       (10, 'Playstation 5', 'Playstation 5', 'Плейстейшен 5', now(), true)
    ON CONFLICT (id) DO NOTHING;
SELECT setval('convenience_id_seq', max(id)) FROM convenience;
