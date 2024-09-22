begin;

insert into lu_role(id, role_name)
values (1, 'ROLE_USER'),
       (2, 'ROLE_ADMIN');

insert into lu_user (id, level, username, password, email, api_key)
values (1, 2, 'test', '$2a$10$/og3a88F2MTD1vNJ/3h3OewCUjwjZDPfKSOgT1E62HBXdiHIBgn8S', 'test@email.com', '1234567890'),
       (2, 0, 'admin', '$2a$10$/og3a88F2MTD1vNJ/3h3OewCUjwjZDPfKSOgT1E62HBXdiHIBgn8S', null, '1234567890');

insert into lu_user_role(user_id, role_id)
values (1, 1),
       (2, 2);

commit;