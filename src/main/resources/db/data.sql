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

-- Insert records into lu_link table
INSERT INTO lu_link (short_url, original_url, owner_id, status, is_custom, expires_at, created_at, updated_at)
VALUES ('ab1c2d', 'https://www.example.com/blog/top-10-productivity-tips-2024', 'u_1', 1, false, '2024-12-31 23:59:59',
        '2024-03-01 10:15:30', '2024-03-01 10:15:30'),
       ('ef3g4h', 'https://www.example.com/products/bestsellers', 'u_1', 1, true, '2025-06-30 23:59:59',
        '2024-03-02 14:22:45', '2024-03-02 14:22:45'),
       ('ij5k6l', 'https://www.example.com/events/annual-conference-2024', 'u_1', 1, false, '2024-09-15 23:59:59',
        '2024-03-03 09:30:15', '2024-03-03 09:30:15'),
       ('mn7o8p', 'https://www.example.com/support/faq', 'u_1', 1, false, '2025-03-31 23:59:59', '2024-03-04 16:45:00',
        '2024-03-04 16:45:00'),
       ('qr9s0t', 'https://www.example.com/user/profile/settings', 'u_1', 1, true, NULL, '2024-03-05 11:10:20',
        '2024-03-05 11:10:20'),
       ('uv1w2x', 'https://www.example.com/blog/artificial-intelligence-trends', 'u_1', 1, false, '2024-11-30 23:59:59',
        '2024-03-06 13:25:40', '2024-03-06 13:25:40'),
       ('yz3a4b', 'https://www.example.com/products/summer-sale-2024', 'u_1', 1, true, '2024-08-31 23:59:59',
        '2024-03-07 08:50:30', '2024-03-07 08:50:30'),
       ('cd5e6f', 'https://www.example.com/careers/job-openings', 'u_1', 1, false, '2025-01-15 23:59:59',
        '2024-03-08 15:35:10', '2024-03-08 15:35:10'),
       ('gh7i8j', 'https://www.example.com/news/latest-updates', 'u_1', 1, false, '2024-12-31 23:59:59',
        '2024-03-09 10:05:55', '2024-03-09 10:05:55'),
       ('kl9m0n', 'https://www.example.com/resources/whitepapers', 'u_1', 1, true, '2025-05-31 23:59:59',
        '2024-03-10 12:40:25', '2024-03-10 12:40:25'),
       ('op1q2r', 'https://www.example.com/blog/cybersecurity-best-practices', 'u_1', 1, false, '2024-10-31 23:59:59',
        '2024-03-11 09:15:40', '2024-03-11 09:15:40'),
       ('st3u4v', 'https://www.example.com/products/clearance', 'u_1', 1, true, '2024-07-31 23:59:59',
        '2024-03-12 14:30:00', '2024-03-12 14:30:00'),
       ('wx5y6z', 'https://www.example.com/events/webinar-series-2024', 'u_1', 1, false, '2025-02-28 23:59:59',
        '2024-03-13 11:20:15', '2024-03-13 11:20:15'),
       ('ab7c8d', 'https://www.example.com/support/live-chat', 'u_1', 1, false, NULL, '2024-03-14 16:55:30',
        '2024-03-14 16:55:30'),
       ('ef9g0h', 'https://www.example.com/blog/remote-work-tips', 'u_1', 1, true, '2024-11-15 23:59:59',
        '2024-03-15 10:10:45', '2024-03-15 10:10:45'),
       ('ij1k2l', 'https://www.example.com/products/new-arrivals', 'u_1', 1, false, '2025-04-30 23:59:59',
        '2024-03-16 13:45:20', '2024-03-16 13:45:20'),
       ('mn3o4p', 'https://www.example.com/about-us/company-history', 'u_1', 1, false, '2024-12-31 23:59:59',
        '2024-03-17 09:30:00', '2024-03-17 09:30:00'),
       ('qr5s6t', 'https://www.example.com/resources/case-studies', 'u_1', 1, true, '2025-03-15 23:59:59',
        '2024-03-18 15:15:35', '2024-03-18 15:15:35'),
       ('uv7w8x', 'https://www.example.com/blog/digital-marketing-strategies', 'u_1', 1, false, '2024-09-30 23:59:59',
        '2024-03-19 11:40:10', '2024-03-19 11:40:10'),
       ('yz9a0b', 'https://www.example.com/products/holiday-specials', 'u_1', 1, true, '2024-12-25 23:59:59',
        '2024-03-20 14:25:50', '2024-03-20 14:25:50'),
       ('cd1e2f', 'https://www.example.com/events/customer-appreciation-day', 'u_1', 1, false, '2024-11-30 23:59:59',
        '2024-03-21 10:50:30', '2024-03-21 10:50:30'),
       ('gh3i4j', 'https://www.example.com/support/knowledge-base', 'u_1', 1, false, NULL, '2024-03-22 16:35:15',
        '2024-03-22 16:35:15'),
       ('kl5m6n', 'https://www.example.com/blog/sustainability-initiatives', 'u_1', 1, true, '2025-06-30 23:59:59',
        '2024-03-23 12:20:40', '2024-03-23 12:20:40'),
       ('op7q8r', 'https://www.example.com/products/bundle-deals', 'u_1', 1, false, '2024-08-31 23:59:59',
        '2024-03-24 09:45:25', '2024-03-24 09:45:25'),
       ('st9u0v', 'https://www.example.com/careers/internship-program', 'u_1', 1, false, '2025-05-31 23:59:59',
        '2024-03-25 14:10:50', '2024-03-25 14:10:50'),
       ('wx1y2z', 'https://www.example.com/resources/ebooks', 'u_1', 1, true, '2024-12-31 23:59:59',
        '2024-03-26 11:30:15', '2024-03-26 11:30:15'),
       ('ab3c4d', 'https://www.example.com/blog/industry-trends-2024', 'u_1', 1, false, '2024-10-31 23:59:59',
        '2024-03-27 15:55:30', '2024-03-27 15:55:30'),
       ('ef5g6h', 'https://www.example.com/products/gift-cards', 'u_1', 1, true, NULL, '2024-03-28 10:40:45',
        '2024-03-28 10:40:45'),
       ('ij7k8l', 'https://www.example.com/events/product-launch-2024', 'u_1', 1, false, '2024-09-15 23:59:59',
        '2024-03-29 13:25:20', '2024-03-29 13:25:20'),
       ('mn9o0p', 'https://www.example.com/support/contact-us', 'u_1', 1, false, '2025-03-31 23:59:59',
        '2024-03-30 09:50:00', '2024-03-30 09:50:00'),
       ('qr1s2t', 'https://www.example.com/blog/customer-success-stories', 'u_1', 1, true, '2024-11-30 23:59:59',
        '2024-03-31 14:15:30', '2024-03-31 14:15:30'),
       ('uv3w4x', 'https://www.example.com/products/flash-sale', 'u_1', 1, false, '2024-07-15 23:59:59',
        '2024-04-01 11:30:45', '2024-04-01 11:30:45'),
       ('yz5a6b', 'https://www.example.com/about-us/leadership-team', 'u_1', 1, false, '2025-01-31 23:59:59',
        '2024-04-02 16:20:10', '2024-04-02 16:20:10'),
       ('cd7e8f', 'https://www.example.com/resources/webinars', 'u_1', 1, true, '2024-12-31 23:59:59',
        '2024-04-03 09:45:30', '2024-04-03 09:45:30'),
       ('gh9i0j', 'https://www.example.com/blog/tech-innovations', 'u_1', 1, false, '2025-05-15 23:59:59',
        '2024-04-04 13:10:50', '2024-04-04 13:10:50'),
       ('kl1m2n', 'https://www.example.com/products/seasonal-collection', 'u_1', 1, true, '2024-08-31 23:59:59',
        '2024-04-05 10:35:15', '2024-04-05 10:35:15'),
       ('op3q4r', 'https://www.example.com/events/annual-sale', 'u_1', 1, false, '2024-11-30 23:59:59',
        '2024-04-06 15:50:40', '2024-04-06 15:50:40'),
       ('st5u6v', 'https://www.example.com/support/faq', 'u_1', 1, false, NULL, '2024-04-07 12:25:00',
        '2024-04-07 12:25:00'),
       ('wx7y8z', 'https://www.example.com/blog/company-updates', 'u_1', 1, true, '2025-03-31 23:59:59',
        '2024-04-08 09:40:30', '2024-04-08 09:40:30'),
       ('ab9c0d', 'https://www.example.com/products/limited-edition', 'u_1', 1, false, '2024-09-30 23:59:59',
        '2024-04-09 14:15:45', '2024-04-09 14:15:45'),
       ('ef1g2h', 'https://www.example.com/careers/open-positions', 'u_1', 1, false, '2025-02-28 23:59:59',
        '2024-04-10 11:30:20', '2024-04-10 11:30:20'),
       ('ij3k4l', 'https://www.example.com/resources/podcasts', 'u_1', 1, true, '2024-12-31 23:59:59',
        '2024-04-11 16:55:10', '2024-04-11 16:55:10'),
       ('mn5o6p', 'https://www.example.com/blog/industry-insights', 'u_1', 1, false, '2025-06-30 23:59:59',
        '2024-04-12 10:20:35', '2024-04-12 10:20:35'),
       ('qr7s8t', 'https://www.example.com/products/back-to-school', 'u_1', 1, true, '2024-08-15 23:59:59',
        '2024-04-13 13:45:50', '2024-04-13 13:45:50'),
       ('uv9w0x', 'https://www.example.com/events/tech-conference-2024', 'u_1', 1, false, '2024-10-31 23:59:59',
        '2024-04-14 09:10:15', '2024-04-14 09:10:15'),
       ('yz1a2b', 'https://www.example.com/support/tutorials', 'u_1', 1, false, NULL, '2024-04-15 14:30:40',
        '2024-04-15 14:30:40'),
       ('cd3e4f', 'https://www.example.com/blog/employee-spotlights', 'u_1', 1, true, '2025-04-30 23:59:59',
        '2024-04-16 11:55:00', '2024-04-16 11:55:00'),
       ('gh5i6j', 'https://www.example.com/products/eco-friendly-line', 'u_1', 1, false, '2024-12-31 23:59:59',
        '2024-04-17 16:20:25', '2024-04-17 16:20:25'),
       ('kl7m8n', 'https://www.example.com/about-us/press-releases', 'u_1', 1, false, '2025-03-15 23:59:59',
        '2024-04-18 10:45:50', '2024-04-18 10:45:50');

-- Insert access records for each link (random number of records per link)
INSERT INTO lu_access_record (short_url, user_agent, ip, referer, language, access_time)
VALUES ('ab1c2d',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36',
        '114.114.114.114', 'https://www.google.com', 'zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7', '2024-03-02 09:15:30'),
       ('ab1c2d',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36',
        '23.45.67.89', 'https://www.google.com', 'en-US,en;q=0.9', '2024-03-15 14:22:31'),
       ('ab1c2d',
        'Mozilla/5.0 (iPhone; CPU iPhone OS 17_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.2 Mobile/15E148 Safari/604.1',
        '34.56.78.90', 'https://www.bing.com', 'zh-CN,zh;q=0.9,en;q=0.8', '2024-03-10 09:15:42'),
       ('ef3g4h',
        'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36',
        '45.67.89.101', NULL, 'en-GB,en;q=0.9', '2024-03-05 18:33:15'),
       ('ij5k6l',
        'Mozilla/5.0 (Linux; Android 14) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.6261.90 Mobile Safari/537.36',
        '56.78.90.112', 'https://t.co/share', 'es-ES,es;q=0.9', '2024-03-20 11:45:23'),
       ('mn7o8p', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Edge/122.0.2365.66',
        '67.89.101.123', 'https://www.facebook.com', 'fr-FR,fr;q=0.9', '2024-03-18 16:27:54'),
       ('qr9s0t',
        'Mozilla/5.0 (iPad; CPU OS 17_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.2 Mobile/15E148 Safari/604.1',
        '78.90.112.134', 'https://www.baidu.com', 'ja-JP,ja;q=0.9', '2024-03-25 08:12:37'),
       ('uv1w2x',
        'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36',
        '89.101.123.145', NULL, 'ko-KR,ko;q=0.9', '2024-03-22 21:09:48'),
       ('yz3a4b', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:123.0) Gecko/20100101 Firefox/123.0', '101.123.145.167',
        'https://www.zhihu.com', 'zh-TW,zh;q=0.9', '2024-03-12 13:54:29'),
       ('cd5e6f', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:123.0) Gecko/20100101 Firefox/123.0',
        '112.134.156.178', 'https://www.linkedin.com', 'de-DE,de;q=0.9', '2024-03-28 15:41:16'),
       ('gh7i8j',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36 Edg/122.0.2365.66',
        '123.145.167.189', 'https://www.twitter.com', 'it-IT,it;q=0.9', '2024-03-17 19:33:52'),
       ('kl9m0n',
        'Mozilla/5.0 (iPhone; CPU iPhone OS 17_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/122.0.6261.89 Mobile/15E148 Safari/604.1',
        '134.156.178.190', NULL, 'ru-RU,ru;q=0.9', '2024-03-24 10:28:45'),
       ('op1q2r',
        'Mozilla/5.0 (Linux; Android 14; SM-S918B) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36',
        '145.167.189.201', 'https://www.yahoo.com', 'pt-BR,pt;q=0.9', '2024-03-19 14:15:33'),
       ('st3u4v',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36',
        '156.178.190.212', 'https://www.weibo.com', 'nl-NL,nl;q=0.9', '2024-03-21 17:22:41'),
       ('wx5y6z',
        'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.2 Safari/605.1.15',
        '167.189.201.223', NULL, 'sv-SE,sv;q=0.9', '2024-03-26 12:47:19'),
       ('ab7c8d',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36',
        '178.190.212.234', 'https://www.yandex.ru', 'pl-PL,pl;q=0.9', '2024-03-23 20:39:27'),
       ('ef9g0h',
        'Mozilla/5.0 (Linux; Android 14; Pixel 8) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36',
        '189.201.223.245', 'https://www.instagram.com', 'tr-TR,tr;q=0.9', '2024-03-16 08:56:14'),
       ('ij1k2l', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:123.0) Gecko/20100101 Firefox/123.0', '190.212.234.251',
        NULL, 'ar-SA,ar;q=0.9', '2024-03-27 16:11:38'),
       ('mn3o4p',
        'Mozilla/5.0 (iPad; CPU OS 17_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.2 Mobile/15E148 Safari/604.1',
        '201.223.245.12', 'https://www.reddit.com', 'hi-IN,hi;q=0.9', '2024-03-14 11:28:53'),
       ('qr5s6t',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36 OPR/108.0.0.0',
        '212.234.251.23', 'https://www.duckduckgo.com', 'th-TH,th;q=0.9', '2024-03-29 19:44:25'),
       ('uv7w8x', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:123.0) Gecko/20100101 Firefox/123.0',
        '223.245.12.34', NULL, 'vi-VN,vi;q=0.9', '2024-03-13 13:37:42'),
       ('yz9a0b',
        'Mozilla/5.0 (Linux; Android 14; SM-G998B) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36',
        '234.251.23.45', 'https://www.sogou.com', 'id-ID,id;q=0.9', '2024-03-30 15:59:16'),
       ('cd1e2f',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36',
        '245.12.34.56', 'https://www.qwant.com', 'ms-MY,ms;q=0.9', '2024-03-11 21:26:31'),
       ('gh3i4j',
        'Mozilla/5.0 (iPhone; CPU iPhone OS 17_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.2 Mobile/15E148 Safari/604.1',
        '251.23.45.67', NULL, 'fi-FI,fi;q=0.9', '2024-03-31 09:18:47'),
       ('kl5m6n', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Edge/122.0.2365.66',
        '12.34.56.78', 'https://www.ecosia.org', 'da-DK,da;q=0.9', '2024-03-08 17:33:59'),
       ('op7q8r',
        'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36',
        '23.45.67.89', 'https://www.naver.com', 'hu-HU,hu;q=0.9', '2024-03-25 14:52:23'),
       ('st9u0v',
        'Mozilla/5.0 (Linux; Android 14; OnePlus 9 Pro) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36',
        '34.56.78.90', NULL, 'cs-CZ,cs;q=0.9', '2024-03-07 12:41:15'),
       ('wx1y2z', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:123.0) Gecko/20100101 Firefox/123.0', '45.67.89.101',
        'https://www.startpage.com', 'sk-SK,sk;q=0.9', '2024-03-28 18:29:34'),
       ('ab3c4d',
        'Mozilla/5.0 (iPad; CPU OS 17_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.2 Mobile/15E148 Safari/604.1',
        '56.78.90.112', 'https://www.google.co.jp', 'el-GR,el;q=0.9', '2024-03-09 20:15:48'),
       ('ef5g6h',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0Safari/537.36', '67.89.101.123',
        NULL, 'ro-RO,ro;q=0.9', '2024-03-29 11:37:26'),
       ('ij7k8l',
        'Mozilla/5.0 (Linux; Android 14; Pixel 7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36',
        '78.90.112.134', 'https://www.google.fr', 'fr-FR,fr;q=0.9,en;q=0.8', '2024-03-30 15:42:19'),
       ('mn9o0p',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36 Edg/122.0.2365.66',
        '89.101.123.145', 'https://www.bing.com', 'de-DE,de;q=0.9,en;q=0.8', '2024-03-31 09:23:45'),
       ('qr1s2t',
        'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.2 Safari/605.1.15',
        '101.123.145.167', NULL, 'it-IT,it;q=0.9,en;q=0.8', '2024-04-01 12:56:33'),
       ('uv3w4x',
        'Mozilla/5.0 (iPhone; CPU iPhone OS 17_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.2 Mobile/15E148 Safari/604.1',
        '112.134.156.178', 'https://www.baidu.com', 'zh-CN,zh;q=0.9,en;q=0.8', '2024-04-02 16:29:11'),
       ('yz5a6b',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36',
        '123.145.167.189', 'https://www.yahoo.co.jp', 'ja-JP,ja;q=0.9,en;q=0.8', '2024-04-03 20:14:57'),
       ('cd7e8f',
        'Mozilla/5.0 (Linux; Android 14; Samsung S23) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36',
        '134.156.178.190', NULL, 'ko-KR,ko;q=0.9,en;q=0.8', '2024-04-04 08:47:22'),
       ('gh9i0j', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:123.0) Gecko/20100101 Firefox/123.0', '145.167.189.201',
        'https://www.naver.com', 'es-ES,es;q=0.9,en;q=0.8', '2024-04-05 13:35:48'),
       ('kl1m2n',
        'Mozilla/5.0 (iPad; CPU OS 17_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.2 Mobile/15E148 Safari/604.1',
        '156.178.190.212', 'https://www.yandex.ru', 'ru-RU,ru;q=0.9,en;q=0.8', '2024-04-06 17:22:39'),
       ('op3q4r',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36 OPR/108.0.0.0',
        '167.189.201.223', NULL, 'pt-BR,pt;q=0.9,en;q=0.8', '2024-04-07 21:09:15'),
       ('st5u6v', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:123.0) Gecko/20100101 Firefox/123.0',
        '178.190.212.234', 'https://www.google.de', 'nl-NL,nl;q=0.9,en;q=0.8', '2024-04-08 11:45:33'),
       ('wx7y8z',
        'Mozilla/5.0 (Linux; Android 14; Huawei P40) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36',
        '189.201.223.245', 'https://www.qwant.com', 'sv-SE,sv;q=0.9,en;q=0.8', '2024-04-09 15:28:47'),
       ('ab9c0d',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36',
        '190.212.234.251', NULL, 'pl-PL,pl;q=0.9,en;q=0.8', '2024-04-10 19:17:26'),
       ('ef1g2h',
        'Mozilla/5.0 (iPhone; CPU iPhone OS 17_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/122.0.6261.89 Mobile/15E148 Safari/604.1',
        '201.223.245.12', 'https://www.ecosia.org', 'tr-TR,tr;q=0.9,en;q=0.8', '2024-04-11 09:52:41'),
       ('ij3k4l', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Edge/122.0.2365.66',
        '212.234.251.23', 'https://www.google.co.uk', 'ar-SA,ar;q=0.9,en;q=0.8', '2024-04-12 14:39:18'),
       ('mn5o6p',
        'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36',
        '223.245.12.34', NULL, 'hi-IN,hi;q=0.9,en;q=0.8', '2024-04-13 18:24:55'),
       ('qr7s8t',
        'Mozilla/5.0 (Linux; Android 14; OnePlus 10) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36',
        '234.251.23.45', 'https://www.startpage.com', 'th-TH,th;q=0.9,en;q=0.8', '2024-04-14 22:11:37'),
       ('uv9w0x', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:123.0) Gecko/20100101 Firefox/123.0', '245.12.34.56',
        'https://www.google.it', 'vi-VN,vi;q=0.9,en;q=0.8', '2024-04-15 10:48:22'),
       ('yz1a2b',
        'Mozilla/5.0 (iPad; CPU OS 17_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.2 Mobile/15E148 Safari/604.1',
        '251.23.45.67', NULL, 'id-ID,id;q=0.9,en;q=0.8', '2024-04-16 15:33:49'),
       ('cd3e4f',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36',
        '12.34.56.78', 'https://www.duckduckgo.com', 'ms-MY,ms;q=0.9,en;q=0.8', '2024-04-17 19:19:14'),
       ('gh5i6j',
        'Mozilla/5.0 (Linux; Android 14; Xiaomi 13) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36',
        '23.45.67.89', 'https://www.google.es', 'fi-FI,fi;q=0.9,en;q=0.8', '2024-04-18 23:05:38'),
       ('kl7m8n',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36',
        '34.56.78.90', 'https://www.google.com.br', 'pt-BR,pt;q=0.9,en;q=0.8', '2024-04-19 08:15:27'),
       ('op3q4r',
        'Mozilla/5.0 (iPhone; CPU iPhone OS 17_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.2 Mobile/15E148 Safari/604.1',
        '45.67.89.101', NULL, 'en-AU,en;q=0.9', '2024-04-19 12:42:15'),
       ('st5u6v',
        'Mozilla/5.0 (Linux; Android 14; SM-S908B) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36',
        '56.78.90.112', 'https://www.facebook.com', 'es-MX,es;q=0.9,en;q=0.8', '2024-04-19 16:33:48'),
       ('wx7y8z',
        'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36',
        '67.89.101.123', 'https://www.linkedin.com', 'fr-CA,fr;q=0.9,en;q=0.8', '2024-04-19 20:11:33'),
       ('ab9c0d', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Edge/122.0.2365.66',
        '78.90.112.134', 'https://t.me', 'uk-UA,uk;q=0.9,en;q=0.8', '2024-04-20 07:25:19'),
       ('ef1g2h',
        'Mozilla/5.0 (iPad; CPU OS 17_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.2 Mobile/15E148 Safari/604.1',
        '89.101.123.145', NULL, 'zh-TW,zh;q=0.9,en;q=0.8', '2024-04-20 11:48:52'),
       ('ij3k4l',
        'Mozilla/5.0 (Linux; Android 14; Pixel 8 Pro) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36',
        '101.123.145.167', 'https://www.instagram.com', 'ko-KR,ko;q=0.9,en;q=0.8', '2024-04-20 15:37:24'),
       ('mn5o6p', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:123.0) Gecko/20100101 Firefox/123.0', '112.134.156.178',
        'https://www.google.co.in', 'hi-IN,hi;q=0.9,en;q=0.8', '2024-04-20 19:22:41'),
       ('qr7s8t', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:123.0) Gecko/20100101 Firefox/123.0',
        '123.145.167.189', 'https://www.reddit.com', 'sv-SE,sv;q=0.9,en;q=0.8', '2024-04-21 08:55:13'),
       ('uv9w0x',
        'Mozilla/5.0 (iPhone; CPU iPhone OS 17_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/122.0.6261.89 Mobile/15E148 Safari/604.1',
        '134.156.178.190', NULL, 'da-DK,da;q=0.9,en;q=0.8', '2024-04-21 12:41:37'),
       ('yz1a2b',
        'Mozilla/5.0 (Linux; Android 14; OnePlus 11) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36',
        '145.167.189.201', 'https://www.twitter.com', 'nl-NL,nl;q=0.9,en;q=0.8', '2024-04-21 16:28:59'),
       ('cd3e4f',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36',
        '156.178.190.212', 'https://www.google.ru', 'ru-RU,ru;q=0.9,en;q=0.8', '2024-04-21 20:15:22'),
       ('gh5i6j',
        'Mozilla/5.0 (iPad; CPU OS 17_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.2 Mobile/15E148 Safari/604.1',
        '167.189.201.223', 'https://www.weibo.com', 'zh-CN,zh;q=0.9,en;q=0.8', '2024-04-22 09:33:45'),
       ('kl7m8n',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36 OPR/108.0.0.0',
        '178.190.212.234', NULL, 'pl-PL,pl;q=0.9,en;q=0.8', '2024-04-22 13:27:18'),
       ('op3q4r',
        'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36',
        '189.201.223.245', 'https://www.google.co.jp', 'ja-JP,ja;q=0.9,en;q=0.8', '2024-04-22 17:12:54'),
       ('st5u6v',
        'Mozilla/5.0 (Linux; Android 14; Xiaomi 14) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36',
        '190.212.234.251', 'https://www.bing.com', 'tr-TR,tr;q=0.9,en;q=0.8', '2024-04-22 21:05:37'),
       ('wx7y8z', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Edge/122.0.2365.66',
        '201.223.245.12', 'https://www.google.de', 'de-DE,de;q=0.9,en;q=0.8', '2024-04-23 08:45:22'),
       ('ab9c0d',
        'Mozilla/5.0 (iPhone; CPU iPhone OS 17_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.2 Mobile/15E148 Safari/604.1',
        '212.234.251.23', NULL, 'it-IT,it;q=0.9,en;q=0.8', '2024-04-23 12:33:48'),
       ('ef1g2h',
        'Mozilla/5.0 (Linux; Android 14; SM-A546B) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36',
        '223.245.12.34', 'https://www.google.fr', 'fr-FR,fr;q=0.9,en;q=0.8', '2024-04-23 16:21:15'),
       ('ij3k4l', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:123.0) Gecko/20100101 Firefox/123.0', '234.251.23.45',
        'https://www.youtube.com', 'es-ES,es;q=0.9,en;q=0.8', '2024-04-23 20:08:39'),
       ('mn5o6p', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:123.0) Gecko/20100101 Firefox/123.0',
        '245.12.34.56', 'https://www.google.com.mx', 'es-MX,es;q=0.9,en;q=0.8', '2024-04-24 09:55:27'),
       ('qr7s8t',
        'Mozilla/5.0 (iPad; CPU OS 17_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.2 Mobile/15E148 Safari/604.1',
        '251.23.45.67', NULL, 'pt-PT,pt;q=0.9,en;q=0.8', '2024-04-24 13:42:51'),
       ('uv9w0x',
        'Mozilla/5.0 (Linux; Android 14; Huawei P60) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36',
        '12.34.56.78', 'https://www.baidu.com', 'zh-CN,zh;q=0.9,en;q=0.8', '2024-04-24 17:29:14'),
       ('yz1a2b',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36',
        '23.45.67.89', 'https://www.google.co.kr', 'ko-KR,ko;q=0.9,en;q=0.8', '2024-04-24 21:15:38'),
       ('cd3e4f',
        'Mozilla/5.0 (iPhone; CPU iPhone OS 17_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/122.0.6261.89 Mobile/15E148 Safari/604.1',
        '34.56.78.90', 'https://www.naver.com', 'ko-KR,ko;q=0.9,en;q=0.8', '2024-04-25 08:22:45'),
       ('gh5i6j',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36 OPR/108.0.0.0',
        '45.67.89.101', NULL, 'cs-CZ,cs;q=0.9,en;q=0.8', '2024-04-25 12:11:19'),
       ('kl7m8n',
        'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36',
        '56.78.90.112', 'https://www.google.com.au', 'en-AU,en;q=0.9', '2024-04-25 15:58:42'),
       ('op3q4r',
        'Mozilla/5.0 (Linux; Android 14; Nothing Phone 2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36',
        '67.89.101.123', 'https://www.google.co.uk', 'en-GB,en;q=0.9', '2024-04-25 19:45:15'),
       ('st5u6v', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Edge/122.0.2365.66',
        '78.90.112.134', 'https://www.google.it', 'it-IT,it;q=0.9,en;q=0.8', '2024-04-26 09:32:38'),
       ('wx7y8z',
        'Mozilla/5.0 (iPad; CPU OS 17_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.2 Mobile/15E148 Safari/604.1',
        '89.101.123.145', NULL, 'ar-SA,ar;q=0.9,en;q=0.8', '2024-04-26 13:19:51'),
       ('ab9c0d',
        'Mozilla/5.0 (Linux; Android 14; Vivo X100) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36',
        '101.123.145.167', 'https://www.google.com.sg', 'zh-SG,zh;q=0.9,en;q=0.8', '2024-04-26 17:07:24'),
       ('ef1g2h', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:123.0) Gecko/20100101 Firefox/123.0', '112.134.156.178',
        'https://www.google.com.tr', 'tr-TR,tr;q=0.9,en;q=0.8', '2024-04-26 20:54:47'),
       ('ij3k4l', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:123.0) Gecko/20100101 Firefox/123.0',
        '123.145.167.189', 'https://www.yandex.ru', 'ru-RU,ru;q=0.9,en;q=0.8', '2024-04-27 10:42:10'),
       ('mn5o6p',
        'Mozilla/5.0 (iPhone; CPU iPhone OS 17_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.2 Mobile/15E148 Safari/604.1',
        '134.156.178.190', NULL, 'he-IL,he;q=0.9,en;q=0.8', '2024-04-27 14:29:33'),
       ('qr7s8t',
        'Mozilla/5.0 (Linux; Android 14; OPPO Find X7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36',
        '145.167.189.201', 'https://www.google.nl', 'nl-NL,nl;q=0.9,en;q=0.8', '2024-04-27 18:16:56'),
       ('uv9w0x',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36',
        '156.178.190.212', 'https://www.google.pl', 'pl-PL,pl;q=0.9,en;q=0.8', '2024-04-27 22:04:19'),
       ('yz1a2b',
        'Mozilla/5.0 (Linux; Android 14; Redmi Note 13) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36',
        '167.189.201.223', NULL, 'vi-VN,vi;q=0.9,en;q=0.8', '2024-04-28 07:51:42'),
       ('cd3e4f',
        'Mozilla/5.0 (iPhone; CPU iPhone OS 17_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.2 Mobile/15E148 Safari/604.1',
        '178.190.212.234', 'https://www.google.com.vn', 'vi-VN,vi;q=0.9,en;q=0.8', '2024-04-28 11:39:05'),
       ('gh5i6j',
        'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36',
        '189.201.223.245', 'https://www.google.se', 'sv-SE,sv;q=0.9,en;q=0.8', '2024-04-28 15:26:28'),
       ('kl7m8n', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Edge/122.0.2365.66',
        '190.212.234.251', 'https://www.google.dk', 'da-DK,da;q=0.9,en;q=0.8', '2024-04-28 19:13:51'),
       ('op3q4r',
        'Mozilla/5.0 (Linux; Android 14; Motorola Edge 40) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36',
        '201.223.245.12', NULL, 'th-TH,th;q=0.9,en;q=0.8', '2024-04-29 09:01:14'),
       ('st5u6v',
        'Mozilla/5.0 (iPad; CPU OS 17_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.2 Mobile/15E148 Safari/604.1',
        '212.234.251.23', 'https://www.google.co.th', 'th-TH,th;q=0.9,en;q=0.8', '2024-04-29 12:48:37'),
       ('wx7y8z', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:123.0) Gecko/20100101 Firefox/123.0', '223.245.12.34',
        'https://www.google.fi', 'fi-FI,fi;q=0.9,en;q=0.8', '2024-04-29 16:36:00'),
       ('ab9c0d', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:123.0) Gecko/20100101 Firefox/123.0',
        '234.251.23.45', 'https://www.google.gr', 'el-GR,el;q=0.9,en;q=0.8', '2024-04-29 20:23:23'),
       ('ef1g2h',
        'Mozilla/5.0 (Linux; Android 14; Sony Xperia 1 V) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36',
        '245.12.34.56', NULL, 'hu-HU,hu;q=0.9,en;q=0.8', '2024-04-30 10:10:46'),
       ('ij3k4l',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36',
        '251.23.45.67', 'https://www.google.hu', 'hu-HU,hu;q=0.9,en;q=0.8', '2024-04-30 13:58:09'),
       ('mn5o6p',
        'Mozilla/5.0 (iPhone; CPU iPhone OS 17_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/122.0.6261.89 Mobile/15E148 Safari/604.1',
        '12.34.56.78', 'https://www.google.cz', 'cs-CZ,cs;q=0.9,en;q=0.8', '2024-04-30 17:45:32'),
       ('qr7s8t',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36 OPR/108.0.0.0',
        '23.45.67.89', 'https://www.google.ro', 'ro-RO,ro;q=0.9,en;q=0.8', '2024-04-30 21:32:55'),
       ('uv9w0x',
        'Mozilla/5.0 (Linux; Android 14; Asus Zenfone 10) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36',
        '34.56.78.90', NULL, 'sk-SK,sk;q=0.9,en;q=0.8', '2024-05-01 11:20:18'),
       ('yz1a2b',
        'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36',
        '45.67.89.101', 'https://www.google.sk', 'sk-SK,sk;q=0.9,en;q=0.8', '2024-05-01 15:07:41'),
       ('cd3e4f',
        'Mozilla/5.0 (iPad; CPU OS 17_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.2 Mobile/15E148 Safari/604.1',
        '56.78.90.112', 'https://www.google.hr', 'hr-HR,hr;q=0.9,en;q=0.8', '2024-05-01 18:55:04'),
       ('gh5i6j', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Edge/122.0.2365.66',
        '67.89.101.123', 'https://www.google.rs', 'sr-RS,sr;q=0.9,en;q=0.8', '2024-05-01 22:42:27'),
       ('kl7m8n',
        'Mozilla/5.0 (Linux; Android 14; Google Pixel Fold) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36',
        '78.90.112.134', NULL, 'sl-SI,sl;q=0.9,en;q=0.8', '2024-05-02 08:29:50'),
       ('op3q4r', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:123.0) Gecko/20100101 Firefox/123.0', '89.101.123.145',
        'https://www.google.si', 'sl-SI,sl;q=0.9,en;q=0.8', '2024-05-02 12:17:13'),
       ('st5u6v', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:123.0) Gecko/20100101 Firefox/123.0',
        '101.123.145.167', 'https://www.google.bg', 'bg-BG,bg;q=0.9,en;q=0.8', '2024-05-02 16:04:36'),
       ('wx7y8z',
        'Mozilla/5.0 (iPhone; CPU iPhone OS 17_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.2 Mobile/15E148 Safari/604.1',
        '112.134.156.178', 'https://www.google.ee', 'et-EE,et;q=0.9,en;q=0.8', '2024-05-02 19:51:59'),
       ('ab9c0d',
        'Mozilla/5.0 (Linux; Android 14; Samsung Galaxy Z Flip5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36',
        '123.145.167.189', NULL, 'lv-LV,lv;q=0.9,en;q=0.8', '2024-05-03 09:39:22'),
       ('ef1g2h',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36',
        '134.156.178.190', 'https://www.google.lv', 'lv-LV,lv;q=0.9,en;q=0.8', '2024-05-03 13:26:45'),
       ('ij3k4l',
        'Mozilla/5.0 (iPad; CPU OS 17_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.2 Mobile/15E148 Safari/604.1',
        '145.167.189.201', 'https://www.google.lt', 'lt-LT,lt;q=0.9,en;q=0.8', '2024-05-03 17:14:08'),
       ('mn5o6p',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36 OPR/108.0.0.0',
        '156.178.190.212', 'https://www.google.is', 'is-IS,is;q=0.9,en;q=0.8', '2024-05-03 21:01:31'),
       ('qr7s8t',
        'Mozilla/5.0 (Linux; Android 14; OnePlus Open) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36',
        '167.189.201.223', NULL, 'mt-MT,mt;q=0.9,en;q=0.8', '2024-05-04 10:48:54'),
       ('uv9w0x',
        'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36',
        '178.190.212.234', 'https://www.google.com.cy', 'el-CY,el;q=0.9,en;q=0.8', '2024-05-04 14:36:17'),
       ('yz1a2b', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Edge/122.0.2365.66',
        '189.201.223.245', 'https://www.google.com.mt', 'mt-MT,mt;q=0.9,en;q=0.8', '2024-05-04 18:23:40'),
       ('cd3e4f',
        'Mozilla/5.0 (Linux; Android 14; Xiaomi Mix Fold 3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36',
        '190.212.234.251', 'https://www.google.lu', 'fr-LU,fr;q=0.9,en;q=0.8',
        '2024-05-04 22:11:03'),
       ('gh5i6j',
        'Mozilla/5.0 (iPhone; CPU iPhone OS 17_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.2 Mobile/15E148 Safari/604.1',
        '201.223.245.12', NULL, 'lb-LU,lb;q=0.9,en;q=0.8', '2024-05-05 08:58:26'),
       ('kl7m8n', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:123.0) Gecko/20100101 Firefox/123.0', '212.234.251.23',
        'https://www.google.com.my', 'ms-MY,ms;q=0.9,en;q=0.8', '2024-05-05 12:45:49'),
       ('op3q4r', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:123.0) Gecko/20100101 Firefox/123.0',
        '223.245.12.34', 'https://www.google.co.id', 'id-ID,id;q=0.9,en;q=0.8', '2024-05-05 16:33:12'),
       ('st5u6v',
        'Mozilla/5.0 (Linux; Android 14; Huawei Mate X5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36',
        '234.251.23.45', 'https://www.google.co.nz', 'en-NZ,en;q=0.9', '2024-05-05 20:20:35'),
       ('wx7y8z',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36',
        '245.12.34.56', NULL, 'en-PH,en;q=0.9', '2024-05-06 10:07:58'),
       ('ab9c0d',
        'Mozilla/5.0 (iPad; CPU OS 17_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.2 Mobile/15E148 Safari/604.1',
        '251.23.45.67', 'https://www.google.com.ph', 'fil-PH,fil;q=0.9,en;q=0.8', '2024-05-06 13:55:21'),
       ('ef1g2h',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36 OPR/108.0.0.0',
        '12.34.56.78', 'https://www.google.com.pk', 'ur-PK,ur;q=0.9,en;q=0.8', '2024-05-06 17:42:44'),
       ('ij3k4l',
        'Mozilla/5.0 (Linux; Android 14; Vivo X Fold 3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36',
        '23.45.67.89', 'https://www.google.com.bd', 'bn-BD,bn;q=0.9,en;q=0.8', '2024-05-06 21:30:07'),
       ('mn5o6p',
        'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36',
        '34.56.78.90', NULL, 'si-LK,si;q=0.9,en;q=0.8', '2024-05-07 11:17:30'),
       ('qr7s8t',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Edge/122.0.2365.66',
        '45.67.89.101', 'https://www.google.lk', 'ta-LK,ta;q=0.9,en;q=0.8', '2024-05-07 15:04:53'),
       ('uv9w0x',
        'Mozilla/5.0 (Linux; Android 14; HONOR Magic V2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36',
        '56.78.90.112', 'https://www.google.com.np', 'ne-NP,ne;q=0.9,en;q=0.8', '2024-05-07 18:52:16'),
       ('yz1a2b',
        'Mozilla/5.0 (iPhone; CPU iPhone OS 17_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.2 Mobile/15E148 Safari/604.1',
        '67.89.101.123', NULL, 'km-KH,km;q=0.9,en;q=0.8', '2024-05-07 22:39:39'),
       ('cd3e4f',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36',
        '78.90.112.134', 'https://www.google.com.kh', 'km-KH,km;q=0.9,en;q=0.8', '2024-05-08 08:27:02'),
       ('gh5i6j', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:123.0) Gecko/20100101 Firefox/123.0',
        '89.101.123.145', 'https://www.google.com.mm', 'my-MM,my;q=0.9,en;q=0.8', '2024-05-08 12:14:25'),
       ('kl7m8n',
        'Mozilla/5.0 (Linux; Android 14; Tecno Phantom V2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36',
        '101.123.145.167', 'https://www.google.co.za', 'zu-ZA,zu;q=0.9,en;q=0.8', '2024-05-08 16:01:48'),
       ('op3q4r',
        'Mozilla/5.0 (iPad; CPU OS 17_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.2 Mobile/15E148 Safari/604.1',
        '112.134.156.178', NULL, 'af-ZA,af;q=0.9,en;q=0.8', '2024-05-08 19:49:11'),
       ('st5u6v',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36 OPR/108.0.0.0',
        '123.145.167.189', 'https://www.google.co.ke', 'sw-KE,sw;q=0.9,en;q=0.8', '2024-05-08 23:36:34'),
       ('wx7y8z',
        'Mozilla/5.0 (Linux; Android 14; Infinix Zero 30) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36',
        '134.156.178.190', 'https://www.google.com.ng', 'ha-NG,ha;q=0.9,en;q=0.8', '2024-05-09 09:23:57'),
       ('ab9c0d', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Edge/122.0.2365.66',
        '145.167.189.201', 'https://www.google.com.gh', 'ak-GH,ak;q=0.9,en;q=0.8', '2024-05-09 13:11:20'),
       ('ef1g2h',
        'Mozilla/5.0 (iPhone; CPU iPhone OS 17_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/122.0.6261.89 Mobile/15E148 Safari/604.1',
        '156.178.190.212', NULL, 'am-ET,am;q=0.9,en;q=0.8', '2024-05-09 16:58:43'),
       ('ij3k4l',
        'Mozilla/5.0 (Linux; Android 14; Realme GT5 Pro) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36',
        '167.189.201.223', 'https://www.google.com.et', 'am-ET,am;q=0.9,en;q=0.8', '2024-05-09 20:46:06'),
       ('mn5o6p',
        'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36',
        '178.190.212.234', 'https://www.google.co.ma', 'ar-MA,ar;q=0.9,fr;q=0.8', '2024-05-10 10:33:29'),
       ('qr7s8t', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:123.0) Gecko/20100101 Firefox/123.0', '189.201.223.245',
        'https://www.google.dz', 'ar-DZ,ar;q=0.9,fr;q=0.8', '2024-05-10 14:20:52'),
       ('uv9w0x',
        'Mozilla/5.0 (Linux; Android 14; iQOO 12) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36',
        '190.212.234.251', NULL, 'ar-TN,ar;q=0.9,fr;q=0.8', '2024-05-10 18:08:15'),
       ('yz1a2b',
        'Mozilla/5.0 (iPad; CPU OS 17_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.2 Mobile/15E148 Safari/604.1',
        '201.223.245.12', 'https://www.google.tn', 'ar-TN,ar;q=0.9,fr;q=0.8', '2024-05-10 21:55:38'),
       ('cd3e4f',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36',
        '212.234.251.23', 'https://www.google.com.lb', 'ar-LB,ar;q=0.9,en;q=0.8', '2024-05-11 11:43:01'),
       ('gh5i6j',
        'Mozilla/5.0 (Linux; Android 14; ZTE Axon 50) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36',
        '223.245.12.34', 'https://www.google.jo', 'ar-JO,ar;q=0.9,en;q=0.8', '2024-05-11 15:30:24'),
       ('kl7m8n',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36 OPR/108.0.0.0',
        '234.251.23.45', NULL, 'ar-KW,ar;q=0.9,en;q=0.8', '2024-05-11 19:17:47'),
       ('op3q4r',
        'Mozilla/5.0 (iPhone; CPU iPhone OS 17_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.2 Mobile/15E148 Safari/604.1',
        '245.12.34.56', 'https://www.google.com.kw', 'ar-KW,ar;q=0.9,en;q=0.8', '2024-05-11 23:05:10'),
       ('st5u6v',
        'Mozilla/5.0 (Linux; Android 14; Meizu 21 Pro) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36',
        '251.23.45.67', 'https://www.google.com.om', 'ar-OM,ar;q=0.9,en;q=0.8', '2024-05-12 08:52:33'),
       ('wx7y8z',
        'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36',
        '12.34.56.78', 'https://www.google.com.qa', 'ar-QA,ar;q=0.9,en;q=0.8', '2024-05-12 12:39:56'),
       ('ab9c0d', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Edge/122.0.2365.66',
        '23.45.67.89', NULL, 'fa-IR,fa;q=0.9,en;q=0.8', '2024-05-12 16:27:19'),
       ('ef1g2h',
        'Mozilla/5.0 (Linux; Android 14; Nubia Z50S Pro) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36',
        '34.56.78.90', 'https://www.google.az', 'az-AZ,az;q=0.9,en;q=0.8', '2024-05-12 20:14:42'),
       ('ij3k4l',
        'Mozilla/5.0 (iPad; CPU OS 17_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.2 Mobile/15E148 Safari/604.1',
        '45.67.89.101', 'https://www.google.com.ge', 'ka-GE,ka;q=0.9,en;q=0.8', '2024-05-13 10:02:05'),
       ('mn5o6p', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:123.0) Gecko/20100101 Firefox/123.0', '56.78.90.112',
        'https://www.google.am', 'hy-AM,hy;q=0.9,en;q=0.8', '2024-05-13 13:49:28'),
       ('qr7s8t',
        'Mozilla/5.0 (Linux; Android 14; Black Shark 5 Pro) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36',
        '67.89.101.123', NULL, 'uz-UZ,uz;q=0.9,en;q=0.8', '2024-05-13 17:36:51'),
       ('uv9w0x',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36',
        '78.90.112.134', 'https://www.google.co.uz', 'uz-UZ,uz;q=0.9,en;q=0.8', '2024-05-13 21:24:14'),
       ('yz1a2b',
        'Mozilla/5.0 (iPhone; CPU iPhone OS 17_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/122.0.6261.89 Mobile/15E148 Safari/604.1',
        '89.101.123.145', 'https://www.google.kg', 'ky-KG,ky;q=0.9,en;q=0.8', '2024-05-14 11:11:37'),
       ('cd3e4f',
        'Mozilla/5.0 (Linux; Android 14; Red Magic 9 Pro) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36',
        '101.123.145.167', 'https://www.google.mn', 'mn-MN,mn;q=0.9,en;q=0.8', '2024-05-14 14:59:00'),
       ('gh5i6j',
        'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36',
        '112.134.156.178', NULL, 'tg-TJ,tg;q=0.9,en;q=0.8', '2024-05-14 18:46:23'),
       ('kl7m8n',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36 OPR/108.0.0.0',
        '123.145.167.189', 'https://www.google.com.tj', 'tg-TJ,tg;q=0.9,en;q=0.8', '2024-05-14 22:33:46'),
       ('op3q4r',
        'Mozilla/5.0 (Linux; Android 14; Legion Y90) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36',
        '134.156.178.190', 'https://www.google.tm', 'tk-TM,tk;q=0.9,en;q=0.8', '2024-05-15 08:21:09'),
       ('st5u6v',
        'Mozilla/5.0 (iPad; CPU OS 17_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.2 Mobile/15E148 Safari/604.1',
        '145.167.189.201', 'https://www.google.com.af', 'ps-AF,ps;q=0.9,en;q=0.8', '2024-05-15 12:08:32'),
       ('wx7y8z', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Edge/122.0.2365.66',
        '156.178.190.212', NULL, 'ps-AF,ps;q=0.9,en;q=0.8', '2024-05-15 15:55:55'),
       ('ab9c0d',
        'Mozilla/5.0 (Linux; Android 14; Sharp Aquos R8) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36',
        '167.189.201.223', 'https://www.google.com.ua', 'uk-UA,uk;q=0.9,en;q=0.8', '2024-05-15 19:43:18'),
       ('ef1g2h', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:123.0) Gecko/20100101 Firefox/123.0', '178.190.212.234',
        'https://www.google.by', 'be-BY,be;q=0.9,en;q=0.8', '2024-05-15 23:30:41'),
       ('ij3k4l',
        'Mozilla/5.0 (iPhone; CPU iPhone OS 17_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.2 Mobile/15E148 Safari/604.1',
        '189.201.223.245', 'https://www.google.md', 'ro-MD,ro;q=0.9,en;q=0.8', '2024-05-16 09:18:04'),
       ('mn5o6p',
        'Mozilla/5.0 (Linux; Android 14; Fairphone 5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36',
        '190.212.234.251', NULL, 'sq-AL,sq;q=0.9,en;q=0.8', '2024-05-16 13:05:27'),
       ('qr7s8t',
        'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36',
        '201.223.245.12', 'https://www.google.al', 'sq-AL,sq;q=0.9,en;q=0.8', '2024-05-16 16:52:50'),
       ('uv9w0x',
        'Mozilla/5.0 (Linux; Android 14; Nothing Phone 3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36',
        '212.234.251.23', 'https://www.google.me', 'sr-ME,sr;q=0.9,en;q=0.8', '2024-05-16 20:40:13'),
       ('yz1a2b',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36',
        '223.245.12.34', NULL, 'bs-BA,bs;q=0.9,en;q=0.8', '2024-05-17 10:27:36'),
       ('cd3e4f',
        'Mozilla/5.0 (iPhone; CPU iPhone OS 17_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.2 Mobile/15E148 Safari/604.1',
        '234.251.23.45', 'https://www.google.ba', 'bs-BA,bs;q=0.9,en;q=0.8', '2024-05-17 14:14:59'),
       ('gh5i6j',
        'Mozilla/5.0 (Linux; Android 14; POCO F5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36',
        '245.12.34.56', 'https://www.google.mk', 'mk-MK,mk;q=0.9,en;q=0.8', '2024-05-17 18:02:22'),
       ('kl7m8n', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Edge/122.0.2365.66',
        '251.23.45.67', 'https://www.google.com.cy', 'el-CY,el;q=0.9,en;q=0.8', '2024-05-17 21:49:45'),
       ('op3q4r',
        'Mozilla/5.0 (Linux; Android 14; Redmi K70) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36',
        '12.34.56.78', NULL, 'mt-MT,mt;q=0.9,en;q=0.8', '2024-05-18 11:37:08'),
       ('st5u6v',
        'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36',
        '23.45.67.89', 'https://www.google.is', 'is-IS,is;q=0.9,en;q=0.8', '2024-05-18 15:24:31'),
       ('wx7y8z',
        'Mozilla/5.0 (Linux; Android 14; Oppo Find N3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36',
        '34.56.78.90', 'https://www.google.fo', 'fo-FO,fo;q=0.9,en;q=0.8', '2024-05-18 19:11:54'),
       ('ab9c0d',
        'Mozilla/5.0 (iPad; CPU OS 17_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.2 Mobile/15E148 Safari/604.1',
        '45.67.89.101', 'https://www.google.gl', 'kl-GL,kl;q=0.9,en;q=0.8', '2024-05-18 22:59:17'),
       ('ef1g2h',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36 OPR/108.0.0.0',
        '56.78.90.112', NULL, 'iu-CA,iu;q=0.9,en;q=0.8', '2024-05-19 08:46:40'),
       ('ij3k4l',
        'Mozilla/5.0 (Linux; Android 14; Motorola Razr 40) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36',
        '67.89.101.123', 'https://www.google.gp', 'fr-GP,fr;q=0.9,en;q=0.8', '2024-05-19 12:34:03'),
       ('mn5o6p', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:123.0) Gecko/20100101 Firefox/123.0', '78.90.112.134',
        'https://www.google.mq', 'fr-MQ,fr;q=0.9,en;q=0.8', '2024-05-19 16:21:26'),
       ('qr7s8t',
        'Mozilla/5.0 (Linux; Android 14; OnePlus Nord 3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36',
        '89.101.123.145', 'https://www.google.re', 'fr-RE,fr;q=0.9,en;q=0.8', '2024-05-19 20:08:49'),
       ('uv9w0x',
        'Mozilla/5.0 (iPhone; CPU iPhone OS 17_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/122.0.6261.89 Mobile/15E148 Safari/604.1',
        '101.123.145.167', NULL, 'fr-YT,fr;q=0.9,en;q=0.8', '2024-05-20 09:56:12'),
       ('yz1a2b',
        'Mozilla/5.0 (Linux; Android 14; Realme GT Neo 5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36',
        '112.134.156.178', 'https://www.google.nc', 'fr-NC,fr;q=0.9,en;q=0.8', '2024-05-20 13:43:35'),
       ('cd3e4f',
        'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36',
        '123.145.167.189', 'https://www.google.pf', 'fr-PF,fr;q=0.9,en;q=0.8', '2024-05-20 17:30:58'),
       ('gh5i6j',
        'Mozilla/5.0 (Linux; Android 14; Vivo V29) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36',
        '134.156.178.190', 'https://www.google.vu', 'bi-VU,bi;q=0.9,en;q=0.8', '2024-05-20 21:18:21'),
       ('kl7m8n', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Edge/122.0.2365.66',
        '145.167.189.201', NULL, 'sm-WS,sm;q=0.9,en;q=0.8', '2024-05-21 11:05:44'),
       ('op3q4r',
        'Mozilla/5.0 (Linux; Android 14; iQOO Neo 9) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36',
        '156.178.190.212', 'https://www.google.ws', 'sm-WS,sm;q=0.9,en;q=0.8', '2024-05-21 14:53:07'),
       ('st5u6v',
        'Mozilla/5.0 (iPad; CPU OS 17_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.2 Mobile/15E148 Safari/604.1',
        '167.189.201.223', 'https://www.google.to', 'to-TO,to;q=0.9,en;q=0.8', '2024-05-21 18:40:30'),
       ('wx7y8z',
        'Mozilla/5.0 (Linux; Android 14; Huawei Nova 12) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36',
        '178.190.212.234', 'https://www.google.co.ck', 'mi-CK,mi;q=0.9,en;q=0.8', '2024-05-21 22:27:53'),
       ('ab9c0d',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36',
        '189.201.223.245', NULL, 'haw-US,haw;q=0.9,en;q=0.8', '2024-05-22 08:15:16'),
       ('ef1g2h',
        'Mozilla/5.0 (Linux; Android 14; Xiaomi 14 Pro) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36',
        '190.212.234.251', 'https://www.google.co.nz', 'mi-NZ,mi;q=0.9,en;q=0.8', '2024-05-22 12:02:39'),
       ('ij3k4l',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36 OPR/108.0.0.0',
        '201.223.245.12', 'https://www.google.as', 'sm-AS,sm;q=0.9,en;q=0.8', '2024-05-22 15:50:02'),
       ('mn5o6p',
        'Mozilla/5.0 (Linux; Android 14; HONOR 90) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36',
        '212.234.251.23', 'https://www.google.co.vi', 'en-VI,en;q=0.9', '2024-05-22 19:37:25'),
       ('qr7s8t',
        'Mozilla/5.0 (iPhone; CPU iPhone OS 17_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.2 Mobile/15E148 Safari/604.1',
        '223.245.12.34', NULL, 'pap-CW,pap;q=0.9,en;q=0.8', '2024-05-23 09:24:48'),
       ('uv9w0x', 'Mozilla/5.0 (Linux; Android 14; TCL 40 Pro) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36', '234.251.23.45', 'https://www.google.com.fj', 'fj-FJ,fj;q=0.9,en;q=0.8', '2024-05-23 13:12:11'),
       ('yz1a2b', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36', '245.12.34.56', 'https://www.google.com.pg', 'ho-PG,ho;q=0.9,en;q=0.8', '2024-05-23 16:59:34'),
       ('cd3e4f', 'Mozilla/5.0 (Linux; Android 14; Infinix Note 30) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36', '251.23.45.67', NULL, 'rw-RW,rw;q=0.9,en;q=0.8', '2024-05-23 20:46:57'),
       ('gh5i6j', 'Mozilla/5.0 (iPhone; CPU iPhone OS 17_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.2 Mobile/15E148 Safari/604.1', '12.34.56.78', 'https://www.google.rw', 'rw-RW,rw;q=0.9,en;q=0.8', '2024-05-24 10:34:20'),
       ('kl7m8n', 'Mozilla/5.0 (Linux; Android 14; Tecno Camon 20) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36', '23.45.67.89', 'https://www.google.bi', 'rn-BI,rn;q=0.9,en;q=0.8', '2024-05-24 14:21:43'),
       ('op3q4r', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Edge/122.0.2365.66', '34.56.78.90', NULL, 'mg-MG,mg;q=0.9,fr;q=0.8', '2024-05-24 18:09:06'),
       ('st5u6v', 'Mozilla/5.0 (Linux; Android 14; ZTE Blade V40) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36', '45.67.89.101', 'https://www.google.mg', 'mg-MG,mg;q=0.9,fr;q=0.8', '2024-05-24 21:56:29'),
       ('wx7y8z', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36', '56.78.90.112', 'https://www.google.sc', 'fr-SC,fr;q=0.9,en;q=0.8', '2024-05-25 11:43:52'),
       ('ab9c0d', 'Mozilla/5.0 (Linux; Android 14; Realme 11 Pro) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36', '67.89.101.123', 'https://www.google.mu', 'mfe-MU,mfe;q=0.9,en;q=0.8', '2024-05-25 15:31:15'),
       ('ef1g2h', 'Mozilla/5.0 (iPad; CPU OS 17_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.2 Mobile/15E148 Safari/604.1', '78.90.112.134', NULL, 'bho-IN,bho;q=0.9,hi;q=0.8', '2024-05-25 19:18:38'),
       ('ij3k4l', 'Mozilla/5.0 (Linux; Android 14; Vivo Y100) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36', '89.101.123.145', 'https://www.google.co.in', 'mai-IN,mai;q=0.9,hi;q=0.8', '2024-05-25 23:06:01'),
       ('mn5o6p', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36', '101.123.145.167', 'https://www.google.co.in', 'sat-IN,sat;q=0.9,hi;q=0.8', '2024-05-26 08:53:24'),
       ('qr7s8t', 'Mozilla/5.0 (Linux; Android 14; POCO X6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36', '112.134.156.178', 'https://www.google.lk', 'si-LK,si;q=0.9,en;q=0.8', '2024-05-26 12:40:47'),
       ('uv9w0x', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36 OPR/108.0.0.0', '123.145.167.189', NULL, 'dv-MV,dv;q=0.9,en;q=0.8', '2024-05-26 16:28:10'),
       ('yz1a2b', 'Mozilla/5.0 (Linux; Android 14; Oppo A79) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36', '134.156.178.190', 'https://www.google.mv', 'dv-MV,dv;q=0.9,en;q=0.8', '2024-05-26 20:15:33'),
       ('cd3e4f', 'Mozilla/5.0 (iPhone; CPU iPhone OS 17_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.2 Mobile/15E148 Safari/604.1', '145.167.189.201', 'https://www.google.bt', 'dz-BT,dz;q=0.9,en;q=0.8', '2024-05-27 10:02:56'),
       ('gh5i6j', 'Mozilla/5.0 (Linux; Android 14; Huawei Nova 11) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36', '156.178.190.212', 'https://www.google.com.np', 'ne-NP,ne;q=0.9,en;q=0.8', '2024-05-27 13:50:19'),
       ('kl7m8n', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:123.0) Gecko/20100101 Firefox/123.0', '167.189.201.223', NULL, 'kok-IN,kok;q=0.9,en;q=0.8', '2024-05-27 17:37:42'),
       ('op3q4r', 'Mozilla/5.0 (Linux; Android 14; OnePlus Ace 3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36', '178.190.212.234', 'https://www.google.co.in', 'gom-IN,gom;q=0.9,en;q=0.8', '2024-05-27 21:25:05'),
       ('st5u6v', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36', '189.201.223.245', 'https://www.google.co.in', 'ks-IN,ks;q=0.9,en;q=0.8', '2024-05-28 11:12:28'),
       ('wx7y8z', 'Mozilla/5.0 (Linux; Android 14; iQOO Z9) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36', '190.212.234.251', 'https://www.google.co.in', 'doi-IN,doi;q=0.9,hi;q=0.8', '2024-05-28 14:59:51'),
       ('ab9c0d', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Edge/122.0.2365.66', '201.223.245.12', NULL, 'mni-IN,mni;q=0.9,en;q=0.8', '2024-05-28 18:47:14'),
       ('ef1g2h', 'Mozilla/5.0 (Linux; Android 14; Redmi Note 13 Pro) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36', '212.234.251.23', 'https://www.google.co.in', 'sa-IN,sa;q=0.9,hi;q=0.8', '2024-05-28 22:34:37'),
       ('ij3k4l', 'Mozilla/5.0 (iPad; CPU OS 17_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.2 Mobile/15E148 Safari/604.1', '223.245.12.34', 'https://www.google.co.in', 'sd-IN,sd;q=0.9,hi;q=0.8', '2024-05-29 08:22:00'),
       ('mn5o6p', 'Mozilla/5.0 (Linux; Android 14; Lava Agni 2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36', '234.251.23.45', 'https://www.google.co.in', 'ml-IN,ml;q=0.9,en;q=0.8', '2024-05-29 12:09:23'),
       ('qr7s8t', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36', '245.12.34.56', NULL, 'te-IN,te;q=0.9,en;q=0.8', '2024-05-29 15:56:46'),
       ('uv9w0x', 'Mozilla/5.0 (Linux; Android 14; Micromax IN 2C) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36', '251.23.45.67', 'https://www.google.co.in', 'kn-IN,kn;q=0.9,en;q=0.8', '2024-05-29 19:44:09'),
       ('yz1a2b', 'Mozilla/5.0 (iPhone; CPU iPhone OS 17_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.2 Mobile/15E148 Safari/604.1', '12.34.56.78', 'https://www.google.co.in', 'or-IN,or;q=0.9,en;q=0.8', '2024-05-29 23:31:32'),
       ('cd3e4f', 'Mozilla/5.0 (Linux; Android 14; Karbonn X40) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36', '23.45.67.89', 'https://www.google.co.in', 'as-IN,as;q=0.9,en;q=0.8', '2024-05-30 09:18:55'),
       ('gh5i6j', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Edge/122.0.2365.66', '34.56.78.90', NULL, 'brx-IN,brx;q=0.9,en;q=0.8', '2024-05-30 13:06:18'),
       ('kl7m8n', 'Mozilla/5.0 (Linux; Android 14; Intex Cloud) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36', '45.67.89.101', 'https://www.google.co.in', 'pa-IN,pa;q=0.9,en;q=0.8', '2024-05-30 16:53:41'),
       ('op3q4r', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36', '56.78.90.112', 'https://www.google.co.in', 'gu-IN,gu;q=0.9,en;q=0.8', '2024-05-30 20:41:04'),
       ('st5u6v', 'Mozilla/5.0 (Linux; Android 14; Xolo Era) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36', '67.89.101.123', 'https://www.google.co.in', 'mr-IN,mr;q=0.9,en;q=0.8', '2024-05-31 10:28:27'),
       ('wx7y8z', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36 OPR/108.0.0.0', '78.90.112.134', NULL, 'bo-CN,bo;q=0.9,zh;q=0.8', '2024-05-31 14:15:50'),
       ('ab9c0d', 'Mozilla/5.0 (Linux; Android 14; Meizu 21) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36', '89.101.123.145', 'https://www.google.cn', 'ug-CN,ug;q=0.9,zh;q=0.8', '2024-05-31 18:03:13'),
       ('ef1g2h', 'Mozilla/5.0 (iPad; CPU OS 17_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.2 Mobile/15E148 Safari/604.1', '101.123.145.167', 'https://www.google.com.hk', 'yue-HK,yue;q=0.9,zh;q=0.8', '2024-05-31 21:50:36'),
       ('ij3k4l', 'Mozilla/5.0 (Linux; Android 14; Smartisan Nut R2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36', '112.134.156.178', 'https://www.google.com.tw', 'nan-TW,nan;q=0.9,zh;q=0.8', '2024-06-01 11:37:59'),
       ('mn5o6p', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:123.0) Gecko/20100101 Firefox/123.0', '123.145.167.189', NULL, 'hak-TW,hak;q=0.9,zh;q=0.8', '2024-06-01 15:25:22'),
       ('qr7s8t', 'Mozilla/5.0 (Linux; Android 14; Gionee M12) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36', '134.156.178.190', 'https://www.google.com.sg', 'wuu-CN,wuu;q=0.9,zh;q=0.8', '2024-06-01 19:12:45'),
       ('uv9w0x', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36', '145.167.189.201', 'https://www.google.co.jp', 'ain-JP,ain;q=0.9,ja;q=0.8', '2024-06-02 09:00:08'),
       ('yz1a2b', 'Mozilla/5.0 (Linux; Android 14; Sharp Aquos R7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36', '156.178.190.212', NULL, 'ryu-JP,ryu;q=0.9,ja;q=0.8', '2024-06-02 12:47:31'),
       ('cd3e4f', 'Mozilla/5.0 (iPhone; CPU iPhone OS 17_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.2 Mobile/15E148 Safari/604.1', '167.189.201.223', 'https://www.google.co.kr', 'jje-KR,jje;q=0.9,ko;q=0.8', '2024-06-02 16:34:54'),
       ('gh5i6j', 'Mozilla/5.0 (Linux; Android 14; Fujitsu Arrows) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36', '178.190.212.234', 'https://www.google.co.kr', 'ko-KP,ko;q=0.9,en;q=0.8', '2024-06-02 20:22:17'),
       ('kl7m8n', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Edge/122.0.2365.66', '189.201.223.245', 'https://www.google.mn', 'bua-RU,bua;q=0.9,ru;q=0.8', '2024-06-03 10:09:40'),
       ('op3q4r', 'Mozilla/5.0 (Linux; Android 14; Kyocera Urbano) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36', '190.212.234.251', NULL, 'sah-RU,sah;q=0.9,ru;q=0.8', '2024-06-03 13:57:03'),
       ('st5u6v', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36', '201.223.245.12', 'https://www.google.ru', 'tyv-RU,tyv;q=0.9,ru;q=0.8', '2024-06-03 17:44:26'),
       ('wx7y8z', 'Mozilla/5.0 (Linux; Android 14; Freetel Priori) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36', '212.234.251.23', 'https://www.google.ru', 'xal-RU,xal;q=0.9,ru;q=0.8', '2024-06-03 21:31:49');

commit;