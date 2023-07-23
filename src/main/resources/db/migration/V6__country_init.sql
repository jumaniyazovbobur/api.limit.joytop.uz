INSERT INTO country(id, name_uz, name_en, name_ru, created_date, visible)
values (2, 'Turkmaniston', 'Turkmenistan', 'Туркменистан', now(), true),
       (3, 'Qozogʻiston', 'Kazakhstan', 'Казахстан', now(), true),
       (4, 'Tojikiston', 'Tajikistan', 'Таджикистан', now(), true),
       (5, 'Qirgʻiziston', 'Kyrgyzstan', 'Кыргызстан', now(), true),
       (6, 'Avgʻoniston', 'Afghanistan', 'Афганистан', now(), true)
      ON CONFLICT (id) DO NOTHING;

SELECT setval('country_id_seq', max(id))
FROM country;




