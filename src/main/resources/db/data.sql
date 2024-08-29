begin;

insert into lu_user (level, username, password, email, api_key)
values (2, 'test', 'password', 'test@email.com', '1234567890'),
       (0, 'Anonymous', 'password', null, '1234567890');

commit;