insert into attach(id,created_date,visible,extension,original_name,path)
values('aaaaaaaaaaaaaa1',now(),true,'jpg','no_image','c://'),
      ('aaaaaaaaaaaaaa2',now(),true,'jpg','no_image','c://'),
      ('aaaaaaaaaaaaaa3',now(),true,'jpg','no_image','c://') on conflict  (id) do nothing;