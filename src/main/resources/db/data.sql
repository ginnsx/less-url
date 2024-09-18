begin;

insert into lu_user (level, username, password, email, api_key)
values (2, 'test', '$2a$10$/og3a88F2MTD1vNJ/3h3OewCUjwjZDPfKSOgT1E62HBXdiHIBgn8S', 'test@email.com', '1234567890'),
       (0, 'Anonymous', '$2a$10$/og3a88F2MTD1vNJ/3h3OewCUjwjZDPfKSOgT1E62HBXdiHIBgn8S', null, '1234567890');

commit;