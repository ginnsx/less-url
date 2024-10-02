begin;

insert into lu_role(id, code, name, description, enabled)
values (1, 'USER', '普通用户', '普通用户', true),
       (2, 'ADMIN', '管理员', '管理员', true);

insert into lu_user (id, level, username, password, email, api_key, subscription_term_id, subscription_start_time)
values (1, 2, 'test', '$2a$10$/og3a88F2MTD1vNJ/3h3OewCUjwjZDPfKSOgT1E62HBXdiHIBgn8S',
        'test@email.com', '1234567890', 1, '2024-01-01 00:00:00'),
       (2, 0, 'admin', '$2a$10$/og3a88F2MTD1vNJ/3h3OewCUjwjZDPfKSOgT1E62HBXdiHIBgn8S',
        null, '1234567890', 1, '2024-01-01 00:00:00');

insert into lu_user_role(user_id, role_id)
values (1, 1),
       (2, 2);

commit;