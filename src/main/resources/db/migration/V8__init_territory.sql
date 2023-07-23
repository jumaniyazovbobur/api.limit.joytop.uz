
INSERT INTO territory(name_uz,name_en, name_ru, district_id, created_date, visible, county)
values
    ('G’azalkent','Gazalkent','Газалкент',14,now(), true,'G’azalkent'),
    ('Tovoqsoy','Tovoqsay','Товоксой',14,now(), true,'Tovoqsoy'),
    ('Oqtosh','Aqtash','Акташ',14,now(), true,'Oqtosh'),
    ('Qizilsuv','Kizilsuv','Кизилсув',14,now(), true,'Qizilsuv'),
    ('Bochka','Bochka','Бочка',14,now(), true,'Bochka'),
    ('Qoronqul','Korongul','Горонгуль',14,now(), true,'Qoronqul'),
    ('Hojikent','Hodjikent','Гаджикент',14,now(), true,'Hojikent'),
    ('Humson','Humsan','Хамсон',14,now(), true,'Humson'),
    ('Sijjak','Sidjjak','Сиджжак',14,now(), true,'Sijjak'),
    ('Yusufxona','Yusufxana','Юсуфхана',14,now(), true,'Yusufxona'),
    ('Yangiqo’rg’on','Yangikurgan','Янгикурган',14,now(), true,'Yangiqo’rg’on'),
    ('Burchmulla','Burchmulla','Бурчмулла',14,now(), true,'Burchmulla'),
    ('Nanay','Nanay','Нанай',14,now(), true,'Nanay'),
    ('Chimyon','Chimgan','Чимган',14,now(), true,'Chimyon'),
    ('Beldirsoy','Beldirsay','Бельдирсой',14,now(), true,'Beldirsoy'),
    ('Kumushkon','Kumushkan','Кумушкон',14,now(), true,'Kumushkon'),
    ('So’qoq','Sokak','Сокак',14,now(), true,'So’qoq') on conflict  (id) do nothing;
SELECT setval('territory_id_seq', max(id)) FROM territory;


/*
    G’azalkent
Tovoqsoy
Oqtosh
Qizilsuv
Bochka
Qoronqul
Hojikent
Humson
Sijjak
Yusufxona
Yangiqo’rg’on
    Burchmulla
    Nanay
    Chimyon
    Beldirsoy
    Kumushkon
    So’qoq*/