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
INSERT INTO lu_access_record (short_url, user_agent, ip, referer, access_time)
VALUES ('ab1c2d',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36',
        '192.168.1.1', 'https://www.google.com', '2024-03-02 09:15:30'),
       ('ab1c2d',
        'Mozilla/5.0 (iPhone; CPU iPhone OS 14_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.1.1 Mobile/15E148 Safari/604.1',
        '10.0.0.1', 'https://www.facebook.com', '2024-03-03 14:30:45'),
       ('ef3g4h',
        'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.1.1 Safari/605.1.15',
        '172.16.0.1', NULL, '2024-03-04 11:20:15'),
       ('ij5k6l',
        'Mozilla/5.0 (Linux; Android 11; SM-G991B) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.120 Mobile Safari/537.36',
        '192.168.0.100', 'https://www.twitter.com', '2024-03-05 16:45:00'),
       ('ij5k6l', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:89.0) Gecko/20100101 Firefox/89.0', '10.1.1.1', NULL,
        '2024-03-06 10:10:30'),
       ('ij5k6l',
        'Mozilla/5.0 (iPad; CPU OS 14_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/91.0.4472.80 Mobile/15E148 Safari/604.1',
        '192.168.1.50', 'https://www.linkedin.com', '2024-03-07 13:25:45'),
       ('mn7o8p',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36 Edg/91.0.864.59',
        '172.16.0.2', NULL, '2024-03-08 09:30:00'),
       ('qr9s0t',
        'Mozilla/5.0 (iPhone; CPU iPhone OS 14_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.1.1 Mobile/15E148 Safari/604.1',
        '10.0.0.2', 'https://www.instagram.com', '2024-03-09 15:40:20'),
       ('uv1w2x',
        'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.114 Safari/537.36',
        '192.168.1.2', NULL, '2024-03-10 11:55:30'),
       ('uv1w2x', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:89.0) Gecko/20100101 Firefox/89.0', '10.1.1.2',
        'https://www.youtube.com', '2024-03-11 14:15:45'),
       ('yz3a4b',
        'Mozilla/5.0 (Linux; Android 11; Pixel 5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.120 Mobile Safari/537.36',
        '172.16.0.3', NULL, '2024-03-12 10:30:00'),
       ('cd5e6f',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36',
        '192.168.1.3', 'https://www.bing.com', '2024-03-13 16:20:15'),
       ('gh7i8j',
        'Mozilla/5.0 (iPhone; CPU iPhone OS 14_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.1.1 Mobile/15E148 Safari/604.1',
        '10.0.0.3', NULL, '2024-03-14 12:45:30'),
       ('kl9m0n',
        'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.1.1 Safari/605.1.15',
        '172.16.0.4', 'https://www.reddit.com', '2024-03-15 09:10:45'),
       ('op1q2r',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36 Edg/91.0.864.59',
        '192.168.1.4', NULL, '2024-03-16 14:30:00'),
       ('st3u4v',
        'Mozilla/5.0 (Linux; Android 11; SM-G991B) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.120 Mobile Safari/537.36',
        '10.1.1.3', 'https://www.facebook.com', '2024-03-17 11:20:30'),
       ('wx5y6z',
        'Mozilla/5.0 (iPad; CPU OS 14_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/91.0.4472.80 Mobile/15E148 Safari/604.1',
        '192.168.0.101', 'https://www.linkedin.com', '2024-03-18 15:45:15'),
       ('ab7c8d', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:89.0) Gecko/20100101 Firefox/89.0', '172.16.0.5', NULL,
        '2024-03-19 09:55:40'),
       ('ef9g0h',
        'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.114 Safari/537.36',
        '10.0.0.4', 'https://www.twitter.com', '2024-03-20 13:10:20'),
       ('ij1k2l',
        'Mozilla/5.0 (iPhone; CPU iPhone OS 14_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.1.1 Mobile/15E148 Safari/604.1',
        '192.168.1.5', 'https://www.instagram.com', '2024-03-21 16:30:50'),
       ('mn3o4p',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36',
        '10.1.1.4', NULL, '2024-03-22 10:45:15'),
       ('qr5s6t',
        'Mozilla/5.0 (Linux; Android 11; Pixel 5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.120 Mobile Safari/537.36',
        '172.16.0.6', 'https://www.youtube.com', '2024-03-23 14:20:30'),
       ('uv7w8x',
        'Mozilla/5.0 (iPad; CPU OS 14_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/91.0.4472.80 Mobile/15E148 Safari/604.1',
        '192.168.0.102', NULL, '2024-03-24 09:35:45'),
       ('yz9a0b', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:89.0) Gecko/20100101 Firefox/89.0', '10.0.0.5',
        'https://www.bing.com', '2024-03-25 12:50:20'),
       ('cd1e2f',
        'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.1.1 Safari/605.1.15',
        '192.168.1.6', 'https://www.reddit.com', '2024-03-26 16:15:35'),
       ('gh3i4j',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36 Edg/91.0.864.59',
        '10.1.1.5', NULL, '2024-03-27 11:30:50'),
       ('kl5m6n',
        'Mozilla/5.0 (iPhone; CPU iPhone OS 14_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.1.1 Mobile/15E148 Safari/604.1',
        '172.16.0.7', 'https://www.google.com', '2024-03-28 14:45:15'),
       ('op7q8r',
        'Mozilla/5.0 (Linux; Android 11; SM-G991B) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.120 Mobile Safari/537.36',
        '192.168.0.103', 'https://www.facebook.com', '2024-03-29 09:00:30'),
       ('st9u0v',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36',
        '10.0.0.6', NULL, '2024-03-30 12:15:45'),
       ('wx1y2z',
        'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.114 Safari/537.36',
        '192.168.1.7', 'https://www.linkedin.com', '2024-03-31 15:30:20'),
       ('ab3c4d',
        'Mozilla/5.0 (iPad; CPU OS 14_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/91.0.4472.80 Mobile/15E148 Safari/604.1',
        '10.1.1.6', 'https://www.twitter.com', '2024-04-01 10:45:35'),
       ('ef5g6h', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:89.0) Gecko/20100101 Firefox/89.0', '172.16.0.8', NULL,
        '2024-04-02 14:00:50'),
       ('ij7k8l',
        'Mozilla/5.0 (iPhone; CPU iPhone OS 14_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.1.1 Mobile/15E148 Safari/604.1',
        '192.168.0.104', 'https://www.instagram.com', '2024-04-03 09:15:15'),
       ('mn9o0p',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36 Edg/91.0.864.59',
        '10.0.0.7', 'https://www.youtube.com', '2024-04-04 12:30:30'),
       ('qr1s2t',
        'Mozilla/5.0 (Linux; Android 11; Pixel 5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.120 Mobile Safari/537.36',
        '192.168.1.8', NULL, '2024-04-05 15:45:45'),
       ('uv3w4x',
        'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.1.1 Safari/605.1.15',
        '10.1.1.7', 'https://www.bing.com', '2024-04-06 11:00:20'),
       ('yz5a6b',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36',
        '172.16.0.9', 'https://www.reddit.com', '2024-04-07 14:15:35'),
       ('cd7e8f',
        'Mozilla/5.0 (iPhone; CPU iPhone OS 14_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.1.1 Mobile/15E148 Safari/604.1',
        '192.168.0.105', NULL, '2024-04-08 09:30:50'),
       ('gh9i0j',
        'Mozilla/5.0 (iPad; CPU OS 14_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/91.0.4472.80 Mobile/15E148 Safari/604.1',
        '10.0.0.8', 'https://www.google.com', '2024-04-09 12:45:15'),
       ('kl1m2n', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:89.0) Gecko/20100101 Firefox/89.0', '192.168.1.9',
        'https://www.facebook.com', '2024-04-10 16:00:30'),
       ('op3q4r',
        'Mozilla/5.0 (Linux; Android 11; SM-G991B) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.120 Mobile Safari/537.36',
        '10.1.1.8', NULL, '2024-04-11 11:15:45'),
       ('st5u6v',
        'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.114 Safari/537.36',
        '172.16.0.10', 'https://www.linkedin.com', '2024-04-12 14:30:20'),
       ('wx7y8z',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36 Edg/91.0.864.59',
        '192.168.0.106', 'https://www.twitter.com', '2024-04-13 09:45:35'),
       ('ab9c0d',
        'Mozilla/5.0 (iPhone; CPU iPhone OS 14_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.1.1 Mobile/15E148 Safari/604.1',
        '10.0.0.9', NULL, '2024-04-14 13:00:50'),
       ('ef1g2h',
        'Mozilla/5.0 (iPad; CPU OS 14_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/91.0.4472.80 Mobile/15E148 Safari/604.1',
        '192.168.1.10', 'https://www.instagram.com', '2024-04-15 16:15:15'),
       ('ij3k4l',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36',
        '10.1.1.9', 'https://www.youtube.com', '2024-04-16 11:30:30'),
       ('mn5o6p',
        'Mozilla/5.0 (Linux; Android 11; Pixel 5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.120 Mobile Safari/537.36',
        '172.16.0.11', NULL, '2024-04-17 14:45:45'),
       ('qr7s8t',
        'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.1.1 Safari/605.1.15',
        '192.168.0.107', 'https://www.bing.com', '2024-04-18 10:00:20'),
       ('ab1c2d',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36',
        '192.168.1.11', 'https://www.google.com', '2024-04-19 08:30:15'),
       ('ab1c2d',
        'Mozilla/5.0 (iPhone; CPU iPhone OS 15_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.0 Mobile/15E148 Safari/604.1',
        '10.0.0.10', 'https://www.facebook.com', '2024-04-20 13:45:30'),
       ('ef3g4h',
        'Mozilla/5.0 (Macintosh; Intel Mac OS X 11_5_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36',
        '172.16.0.12', NULL, '2024-04-21 10:20:45'),
       ('ef3g4h',
        'Mozilla/5.0 (Linux; Android 12; SM-G998B) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Mobile Safari/537.36',
        '192.168.0.108', 'https://www.twitter.com', '2024-04-22 15:35:00'),
       ('ij5k6l', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:91.0) Gecko/20100101 Firefox/91.0', '10.1.1.10', NULL,
        '2024-04-23 09:50:20'),
       ('mn7o8p',
        'Mozilla/5.0 (iPad; CPU OS 15_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/92.0.4515.90 Mobile/15E148 Safari/604.1',
        '192.168.1.51', 'https://www.linkedin.com', '2024-04-24 14:05:40'),
       ('mn7o8p',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36 Edg/92.0.902.78',
        '172.16.0.13', NULL, '2024-04-25 11:20:55'),
       ('qr9s0t',
        'Mozilla/5.0 (iPhone; CPU iPhone OS 15_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.0 Mobile/15E148 Safari/604.1',
        '10.0.0.11', 'https://www.instagram.com', '2024-04-26 16:40:10'),
       ('uv1w2x',
        'Mozilla/5.0 (Macintosh; Intel Mac OS X 11_5_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36',
        '192.168.1.12', NULL, '2024-04-27 12:55:25'),
       ('yz3a4b', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:91.0) Gecko/20100101 Firefox/91.0', '10.1.1.11',
        'https://www.youtube.com', '2024-04-28 08:10:40'),
       ('yz3a4b',
        'Mozilla/5.0 (Linux; Android 12; Pixel 6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Mobile Safari/537.36',
        '172.16.0.14', NULL, '2024-04-29 13:25:55'),
       ('cd5e6f',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36',
        '192.168.1.13', 'https://www.bing.com', '2024-04-30 09:40:15'),
       ('gh7i8j',
        'Mozilla/5.0 (iPhone; CPU iPhone OS 15_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.0 Mobile/15E148 Safari/604.1',
        '10.0.0.12', NULL, '2024-05-01 14:55:30'),
       ('kl9m0n',
        'Mozilla/5.0 (Macintosh; Intel Mac OS X 11_5_2) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.1.2 Safari/605.1.15',
        '172.16.0.15', 'https://www.reddit.com', '2024-05-02 11:10:45'),
       ('op1q2r',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36 Edg/92.0.902.78',
        '192.168.1.14', NULL, '2024-05-03 16:25:00'),
       ('st3u4v',
        'Mozilla/5.0 (Linux; Android 12; SM-G998B) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Mobile Safari/537.36',
        '10.1.1.12', 'https://www.facebook.com', '2024-05-04 12:40:20'),
       ('wx5y6z',
        'Mozilla/5.0 (iPad; CPU OS 15_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/92.0.4515.90 Mobile/15E148 Safari/604.1',
        '192.168.0.109', 'https://www.linkedin.com', '2024-05-05 08:55:35'),
       ('ab7c8d', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:91.0) Gecko/20100101 Firefox/91.0', '172.16.0.16', NULL,
        '2024-05-06 14:10:50'),
       ('ef9g0h',
        'Mozilla/5.0 (Macintosh; Intel Mac OS X 11_5_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36',
        '10.0.0.13', 'https://www.twitter.com', '2024-05-07 10:25:15'),
       ('ij1k2l',
        'Mozilla/5.0 (iPhone; CPU iPhone OS 15_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.0 Mobile/15E148 Safari/604.1',
        '192.168.1.15', 'https://www.instagram.com', '2024-05-08 15:40:30'),
       ('mn3o4p',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36',
        '10.1.1.13', NULL, '2024-05-09 11:55:45'),
       ('qr5s6t',
        'Mozilla/5.0 (Linux; Android 12; Pixel 6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Mobile Safari/537.36',
        '172.16.0.17', 'https://www.youtube.com', '2024-05-10 08:10:00'),
       ('uv7w8x',
        'Mozilla/5.0 (iPad; CPU OS 15_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/92.0.4515.90 Mobile/15E148 Safari/604.1',
        '192.168.0.110', NULL, '2024-05-11 13:25:20'),
       ('yz9a0b', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:91.0) Gecko/20100101 Firefox/91.0', '10.0.0.14',
        'https://www.bing.com', '2024-05-12 09:40:35'),
       ('cd1e2f',
        'Mozilla/5.0 (Macintosh; Intel Mac OS X 11_5_2) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.1.2 Safari/605.1.15',
        '192.168.1.16', 'https://www.reddit.com', '2024-05-13 14:55:50'),
       ('gh3i4j',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36 Edg/92.0.902.78',
        '10.1.1.14', NULL, '2024-05-14 11:10:15'),
       ('kl5m6n',
        'Mozilla/5.0 (iPhone; CPU iPhone OS 15_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.0 Mobile/15E148 Safari/604.1',
        '172.16.0.18', 'https://www.google.com', '2024-05-15 16:25:30'),
       ('op7q8r',
        'Mozilla/5.0 (Linux; Android 12; SM-G998B) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Mobile Safari/537.36',
        '192.168.0.111', 'https://www.facebook.com', '2024-05-16 12:40:45'),
       ('st9u0v',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36',
        '10.0.0.15', NULL, '2024-05-17 08:55:00'),
       ('wx1y2z',
        'Mozilla/5.0 (Macintosh; Intel Mac OS X 11_5_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36',
        '192.168.1.17', 'https://www.linkedin.com', '2024-05-18 14:10:20'),
       ('ab3c4d',
        'Mozilla/5.0 (iPad; CPU OS 15_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/92.0.4515.90 Mobile/15E148 Safari/604.1',
        '10.1.1.15', 'https://www.twitter.com', '2024-05-19 10:25:35'),
       ('ef5g6h', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:91.0) Gecko/20100101 Firefox/91.0', '172.16.0.19', NULL,
        '2024-05-20 15:40:50'),
       ('ij7k8l',
        'Mozilla/5.0 (iPhone; CPU iPhone OS 15_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.0 Mobile/15E148 Safari/604.1',
        '192.168.0.112', 'https://www.instagram.com', '2024-05-21 11:55:15'),
       ('mn9o0p',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36 Edg/92.0.902.78',
        '10.0.0.16', 'https://www.youtube.com', '2024-05-22 08:10:30'),
       ('qr1s2t',
        'Mozilla/5.0 (Linux; Android 12; Pixel 6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Mobile Safari/537.36',
        '192.168.1.18', NULL, '2024-05-23 13:25:45'),
       ('uv3w4x',
        'Mozilla/5.0 (Macintosh; Intel Mac OS X 11_5_2) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.1.2 Safari/605.1.15',
        '10.1.1.16', 'https://www.bing.com', '2024-05-24 09:40:00'),
       ('yz5a6b',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36',
        '172.16.0.20', 'https://www.reddit.com', '2024-05-25 14:55:20'),
       ('cd7e8f',
        'Mozilla/5.0 (iPhone; CPU iPhone OS 15_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.0 Mobile/15E148 Safari/604.1',
        '192.168.0.113', NULL, '2024-05-26 11:10:35'),
       ('gh9i0j',
        'Mozilla/5.0 (iPad; CPU OS 15_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/92.0.4515.90 Mobile/15E148 Safari/604.1',
        '10.0.0.17', 'https://www.google.com', '2024-05-27 16:25:50'),
       ('kl1m2n', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:91.0) Gecko/20100101 Firefox/91.0', '192.168.1.19',
        'https://www.facebook.com', '2024-05-28 12:40:15'),
       ('op3q4r',
        'Mozilla/5.0 (Linux; Android 12; SM-G998B) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Mobile Safari/537.36',
        '10.1.1.17', NULL, '2024-05-29 08:55:30'),
       ('st5u6v',
        'Mozilla/5.0 (Macintosh; Intel Mac OS X 11_5_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36',
        '172.16.0.21', 'https://www.linkedin.com', '2024-05-30 14:10:45'),
       ('wx7y8z',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36 Edg/92.0.902.78',
        '192.168.0.114', 'https://www.twitter.com', '2024-05-31 10:25:00'),
       ('ab9c0d',
        'Mozilla/5.0 (iPhone; CPU iPhone OS 15_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.0 Mobile/15E148 Safari/604.1',
        '10.0.0.18', NULL, '2024-06-01 15:40:20'),
       ('ef1g2h',
        'Mozilla/5.0 (iPad; CPU OS 15_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/92.0.4515.90 Mobile/15E148 Safari/604.1',
        '192.168.1.20', 'https://www.instagram.com', '2024-06-02 11:55:35'),
       ('ij3k4l',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36',
        '10.1.1.18', 'https://www.youtube.com', '2024-06-03 08:10:50'),
       ('mn5o6p',
        'Mozilla/5.0 (Linux; Android 12; Pixel 6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Mobile Safari/537.36',
        '172.16.0.22', NULL, '2024-06-04 13:25:15'),
       ('qr7s8t',
        'Mozilla/5.0 (Macintosh; Intel Mac OS X 11_5_2) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.1.2 Safari/605.1.15',
        '192.168.0.115', 'https://www.bing.com', '2024-06-05 09:40:30'),
       ('ab1c2d',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36',
        '192.168.1.21', 'https://www.google.com', '2024-06-06 11:30:45'),
       ('ef3g4h',
        'Mozilla/5.0 (iPhone; CPU iPhone OS 15_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.0 Mobile/15E148 Safari/604.1',
        '10.0.0.19', 'https://www.facebook.com', '2024-06-07 14:45:20'),
       ('ij5k6l',
        'Mozilla/5.0 (Macintosh; Intel Mac OS X 11_5_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36',
        '172.16.0.23', NULL, '2024-06-08 09:15:35'),
       ('mn7o8p',
        'Mozilla/5.0 (Linux; Android 12; SM-G998B) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Mobile Safari/537.36',
        '192.168.0.116', 'https://www.twitter.com', '2024-06-09 16:30:10'),
       ('qr9s0t', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:91.0) Gecko/20100101 Firefox/91.0', '10.1.1.19', NULL,
        '2024-06-10 12:45:55'),
       ('uv1w2x',
        'Mozilla/5.0 (iPad; CPU OS 15_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/92.0.4515.90 Mobile/15E148 Safari/604.1',
        '192.168.1.22', 'https://www.linkedin.com', '2024-06-11 08:20:30'),
       ('yz3a4b',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36 Edg/92.0.902.78',
        '172.16.0.24', NULL, '2024-06-12 15:35:15'),
       ('cd5e6f',
        'Mozilla/5.0 (iPhone; CPU iPhone OS 15_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.0 Mobile/15E148 Safari/604.1',
        '10.0.0.20', 'https://www.instagram.com', '2024-06-13 11:50:40'),
       ('gh7i8j',
        'Mozilla/5.0 (Macintosh; Intel Mac OS X 11_5_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36',
        '192.168.0.117', NULL, '2024-06-14 14:05:25'),
       ('kl9m0n',
        'Mozilla/5.0 (Linux; Android 12; Pixel 6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Mobile Safari/537.36',
        '10.1.1.20', 'https://www.youtube.com', '2024-06-15 09:30:50'),
       ('op1q2r', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:91.0) Gecko/20100101 Firefox/91.0', '172.16.0.25', NULL,
        '2024-06-16 16:45:15'),
       ('st3u4v',
        'Mozilla/5.0 (iPad; CPU OS 15_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/92.0.4515.90 Mobile/15E148 Safari/604.1',
        '192.168.1.23', 'https://www.bing.com', '2024-06-17 13:10:30'),
       ('wx5y6z',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36',
        '10.0.0.21', 'https://www.reddit.com', '2024-06-18 08:25:55'),
       ('ab7c8d',
        'Mozilla/5.0 (iPhone; CPU iPhone OS 15_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.0 Mobile/15E148 Safari/604.1',
        '192.168.0.118', NULL, '2024-06-19 15:40:20'),
       ('ef9g0h',
        'Mozilla/5.0 (Macintosh; Intel Mac OS X 11_5_2) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.1.2 Safari/605.1.15',
        '10.1.1.21', 'https://www.facebook.com', '2024-06-20 11:55:45'),
       ('ij1k2l',
        'Mozilla/5.0 (Linux; Android 12; SM-G998B) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Mobile Safari/537.36',
        '172.16.0.26', 'https://www.twitter.com', '2024-06-21 14:20:10'),
       ('mn3o4p',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36 Edg/92.0.902.78',
        '192.168.1.24', NULL, '2024-06-22 09:35:35'),
       ('qr5s6t',
        'Mozilla/5.0 (iPad; CPU OS 15_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/92.0.4515.90 Mobile/15E148 Safari/604.1',
        '10.0.0.22', 'https://www.linkedin.com', '2024-06-23 16:50:50'),
       ('uv7w8x', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:91.0) Gecko/20100101 Firefox/91.0', '192.168.0.119',
        NULL, '2024-06-24 13:15:15'),
       ('yz9a0b',
        'Mozilla/5.0 (iPhone; CPU iPhone OS 15_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.0 Mobile/15E148 Safari/604.1',
        '10.1.1.22', 'https://www.instagram.com', '2024-06-25 08:30:40'),
       ('cd1e2f',
        'Mozilla/5.0 (Macintosh; Intel Mac OS X 11_5_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36',
        '172.16.0.27', 'https://www.youtube.com', '2024-06-26 15:45:05'),
       ('gh3i4j',
        'Mozilla/5.0 (Linux; Android 12; Pixel 6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Mobile Safari/537.36',
        '192.168.1.25', NULL, '2024-06-27 11:10:30'),
       ('kl5m6n',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36',
        '10.0.0.23', 'https://www.bing.com', '2024-06-28 14:25:55'),
       ('op7q8r',
        'Mozilla/5.0 (iPad; CPU OS 15_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/92.0.4515.90 Mobile/15E148 Safari/604.1',
        '192.168.0.120', 'https://www.reddit.com', '2024-06-29 09:50:20'),
       ('st9u0v', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:91.0) Gecko/20100101 Firefox/91.0', '10.1.1.23', NULL,
        '2024-06-30 16:05:45'),
       ('wx1y2z',
        'Mozilla/5.0 (iPhone; CPU iPhone OS 15_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.0 Mobile/15E148 Safari/604.1',
        '172.16.0.28', 'https://www.google.com', '2024-07-01 12:30:10'),
       ('ab3c4d',
        'Mozilla/5.0 (Macintosh; Intel Mac OS X 11_5_2) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.1.2 Safari/605.1.15',
        '192.168.1.26', NULL, '2024-07-02 08:45:35'),
       ('ef5g6h',
        'Mozilla/5.0 (Linux; Android 12; SM-G998B) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Mobile Safari/537.36',
        '10.0.0.24', 'https://www.facebook.com', '2024-07-03 15:10:50'),
       ('ij7k8l',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36 Edg/92.0.902.78',
        '192.168.0.121', 'https://www.twitter.com', '2024-07-04 11:25:15'),
       ('mn9o0p',
        'Mozilla/5.0 (iPad; CPU OS 15_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/92.0.4515.90 Mobile/15E148 Safari/604.1',
        '10.1.1.24', NULL, '2024-07-05 14:40:40'),
       ('qr1s2t', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:91.0) Gecko/20100101 Firefox/91.0', '172.16.0.29',
        'https://www.linkedin.com', '2024-07-06 09:55:05'),
       ('uv3w4x',
        'Mozilla/5.0 (iPhone; CPU iPhone OS 15_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.0 Mobile/15E148 Safari/604.1',
        '192.168.1.27', NULL, '2024-07-07 16:20:30'),
       ('yz5a6b',
        'Mozilla/5.0 (Macintosh; Intel Mac OS X 11_5_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36',
        '10.0.0.25', 'https://www.instagram.com', '2024-07-08 12:35:55'),
       ('cd7e8f',
        'Mozilla/5.0 (Linux; Android 12; Pixel 6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Mobile Safari/537.36',
        '192.168.0.122', 'https://www.youtube.com', '2024-07-09 08:50:20'),
       ('gh9i0j',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36',
        '10.1.1.25', NULL, '2024-07-10 15:15:45'),
       ('kl1m2n',
        'Mozilla/5.0 (iPad; CPU OS 15_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/92.0.4515.90 Mobile/15E148 Safari/604.1',
        '172.16.0.30', 'https://www.bing.com', '2024-07-11 11:30:10'),
       ('op3q4r', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:91.0) Gecko/20100101 Firefox/91.0', '192.168.1.28',
        'https://www.reddit.com', '2024-07-12 14:45:35'),
       ('st5u6v',
        'Mozilla/5.0 (iPhone; CPU iPhone OS 15_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.0 Mobile/15E148 Safari/604.1',
        '10.0.0.26', NULL, '2024-07-13 10:10:50'),
       ('wx7y8z',
        'Mozilla/5.0 (Macintosh; Intel Mac OS X 11_5_2) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.1.2 Safari/605.1.15',
        '192.168.0.123', 'https://www.google.com', '2024-07-14 16:25:15'),
       ('ab9c0d',
        'Mozilla/5.0 (Linux; Android 12; SM-G998B) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Mobile Safari/537.36',
        '10.1.1.26', 'https://www.facebook.com', '2024-07-15 12:40:40'),
       ('ef1g2h',
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36 Edg/92.0.902.78',
        '172.16.0.31', NULL, '2024-07-16 08:55:05'),
       ('ij3k4l',
        'Mozilla/5.0 (iPad; CPU OS 15_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/92.0.4515.90 Mobile/15E148 Safari/604.1',
        '192.168.1.29', 'https://www.twitter.com', '2024-07-17 15:20:30'),
       ('mn5o6p', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:91.0) Gecko/20100101 Firefox/91.0', '10.0.0.27',
        'https://www.linkedin.com', '2024-07-18 11:35:55'),
       ('qr7s8t',
        'Mozilla/5.0 (iPhone; CPU iPhone OS 15_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.0 Mobile/15E148 Safari/604.1',
        '192.168.0.124', NULL, '2024-07-19 14:50:20');

commit;