begin;

insert into lu_role(id, code, name, description, enabled)
values (1, 'USER', '普通用户', '普通用户', true),
       (2, 'ADMIN', '管理员', '管理员', true);

insert into lu_user (id, level, username, password, email, api_key, subscription_term_id, subscription_start_time)
values (1, 2, 'test', '$2a$10$/og3a88F2MTD1vNJ/3h3OewCUjwjZDPfKSOgT1E62HBXdiHIBgn8S',
        'test@lu.com', '1234567890', 1, '2024-01-01 00:00:00'),
       (2, 0, 'admin', '$2a$10$/og3a88F2MTD1vNJ/3h3OewCUjwjZDPfKSOgT1E62HBXdiHIBgn8S',
        'admin@lu.com', '1234567890', 1, '2024-01-01 00:00:00');

insert into lu_user_role(user_id, role_id)
values (1, 1),
       (2, 2);

insert into lu_email_template (name, subject, content)
values ('register-verification', '您的【Less Url】注册验证码', '<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>注册验证码</title>
    <style>
        body {
            font-family: ''Microsoft YaHei'', Arial, sans-serif;
            line-height: 1.6;
            color: #333;
            max-width: 600px;
            margin: 0 auto;
            padding: 20px;
        }
        header {
            text-align: center;
            margin-bottom: 20px;
        }
        header img {
            max-width: 150px;
        }
        h1 {
            color: #2c3e50;
            font-size: 24px;
            margin-bottom: 20px;
        }
        .verification-code {
            background-color: #f2f2f2;
            font-size: 28px;
            font-weight: bold;
            text-align: center;
            padding: 10px;
            margin: 20px 0;
            letter-spacing: 5px;
        }
        .warning {
            color: #e74c3c;
            font-weight: bold;
        }
        footer {
            margin-top: 40px;
            text-align: center;
            font-size: 12px;
            color: #7f8c8d;
        }
    </style>
</head>
<body>
    <header>
        <img src="https://example.com/logo.png" alt="公司标志">
    </header>

    <main>
        <h1>欢迎注册 - 验证您的邮箱</h1>
        <p>尊敬的新用户：</p>
        <p>感谢您注册我们的服务。为了完成注册流程并验证您的邮箱地址，请使用以下验证码：</p>
        <div class="verification-code">{{ code }}</div>
        <p>此验证码将在 {{ expireTime }} 分钟内有效。请在注册页面输入此验证码以完成注册。</p>
        <p class="warning">请勿将验证码透露给他人，以保护您的账户安全。</p>
        <p>如果您没有尝试注册，请忽略此邮件。</p>
        <p>注册完成后，您将可以享受我们提供的所有服务。如有任何疑问，欢迎联系我们的客户支持团队。</p>
    </main>

    <footer>
        <p>此邮件由系统自动发送，请勿直接回复。</p>
        <p>© 2024 Less Url。保留所有权利。</p>
    </footer>
</body>
</html>
'),
    ('account-creation', '欢迎使用【Less Url】', '<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>欢迎加入我们的服务</title>
    <style>
        body {
            font-family: ''Microsoft YaHei'', Arial, sans-serif;
            line-height: 1.6;
            color: #333;
            max-width: 600px;
            margin: 0 auto;
            padding: 20px;
        }
        header {
            text-align: center;
            margin-bottom: 20px;
        }
        header img {
            max-width: 150px;
        }
        h1 {
            color: #2c3e50;
            font-size: 24px;
            margin-bottom: 20px;
        }
        .cta-button {
            display: inline-block;
            background-color: #3498db;
            color: white;
            padding: 10px 20px;
            text-decoration: none;
            border-radius: 5px;
            margin-top: 20px;
        }
        footer {
            margin-top: 40px;
            text-align: center;
            font-size: 12px;
            color: #7f8c8d;
        }
        .social-link {
            color: #3498db;
            text-decoration: none;
            margin: 0 5px;
        }
    </style>
</head>
<body>
    <header>
        <img src="https://example.com/logo.png" alt="公司标志">
    </header>

    <main>
        <h1>欢迎加入我们的服务！</h1>
        <p>尊敬的 {{ username }}，</p>
        <p>感谢您的注册。您的账户已成功创建，我们很高兴能够为您服务！</p>
        <p>以下是一些您可以采取的后续步骤：</p>
        <ul>
            <li>完善您的个人资料</li>
            <li>探索我们的功能</li>
            <li>与其他用户建立联系</li>
        </ul>
        <p>如果您有任何疑问，请随时联系我们的客户支持团队。</p>
        <a href="https://example.com/login" class="cta-button">立即开始</a>
    </main>

    <footer>
        <p>关注我们的社交媒体：</p>
        <a href="https://weibo.com/example" class="social-link">微博</a>
        <a href="https://wx.example.com" class="social-link">微信</a>
        <a href="https://douyin.com/example" class="social-link">抖音</a>
        <p>© 2024 Less Url。保留所有权利。</p>
    </footer>
</body>
</html>
'),
    ('login-verification', '您的【Less Url】登录验证码', '<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>登录验证码</title>
    <style>
        body {
            font-family: ''Microsoft YaHei'', Arial, sans-serif;
            line-height: 1.6;
            color: #333;
            max-width: 600px;
            margin: 0 auto;
            padding: 20px;
        }
        header {
            text-align: center;
            margin-bottom: 20px;
        }
        header img {
            max-width: 150px;
        }
        h1 {
            color: #2c3e50;
            font-size: 24px;
            margin-bottom: 20px;
        }
        .verification-code {
            background-color: #f2f2f2;
            font-size: 28px;
            font-weight: bold;
            text-align: center;
            padding: 10px;
            margin: 20px 0;
            letter-spacing: 5px;
        }
        .warning {
            color: #e74c3c;
            font-weight: bold;
        }
        footer {
            margin-top: 40px;
            text-align: center;
            font-size: 12px;
            color: #7f8c8d;
        }
    </style>
</head>
<body>
    <header>
        <img src="https://example.com/logo.png" alt="公司标志">
    </header>

    <main>
        <h1>登录验证码</h1>
        <p>尊敬的用户：</p>
        <p>您正在尝试登录您的账户。以下是您的验证码：</p>
        <div class="verification-code">{{ code }}</div>
        <p>此验证码将在 {{ expireTime }} 分钟内有效。如果您没有尝试登录，请忽略此邮件。</p>
        <p class="warning">请勿将验证码透露给他人，以保护您的账户安全。</p>
        <p>如果您有任何疑问，请联系我们的客户支持团队。</p>
    </main>

    <footer>
        <p>此邮件由系统自动发送，请勿直接回复。</p>
        <p>© 2024 Less Url。保留所有权利。</p>
    </footer>
</body>
</html>
');

commit;