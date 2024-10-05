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

insert into lu_email_template (name, subject, content)
values ('register-verify-code', '您的【Less Url】注册验证码', '<!DOCTYPE html><html lang="zh"><head><meta charset="UTF-8" /><meta name="viewport" content="width=device-width, initial-scale=1.0" /><title>注册验证码</title><style>      body {        font-family: "Arial", sans-serif;        color: #333;        background-color: #f4f4f4;        margin: 0;        padding: 20px;      }      .email-container {        width: 600px;        margin: 0 auto;        background-color: #fff;        padding: 20px;        box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);      }      .email-header {        text-align: center;        border-bottom: 2px solid #eee;        padding-bottom: 10px;      }      .email-content {        padding: 20px 0;      }      .verification-code {        font-size: 24px;        background-color: #ddd6f3;        padding: 10px;        border: 1px dotted #826fa9;        text-align: center;        display: block;        margin: 20px auto;        width: 200px;        letter-spacing: 3px;        font-weight: bold;        color: #6a8a;      }      .email-footer {        text-align: center;        margin-top: 20px;        font-size: 12px;        color: #666;      }</style></head><body><div class="email-container"><div class="email-header"><h2>欢迎加入我们的服务</h2></div><div class="email-content"><p>尊敬的用户：</p><p>感谢您注册我们的服务。请输入以下验证码以完成注册流程：</p><span class="verification-code">{{ code }}</span><p>该验证码在{{ expireTime }}分钟内有效。请勿向任何人泄露此验证码。</p></div><div class="email-footer"><p>如果您没有申请这个验证码，请忽略这封邮件。</p><p>&copy; Less Url | 2024</p></div></div></body></html>');

commit;