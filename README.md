# Less URL

短链接服务 - 一个简单而强大的URL缩短服务

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green)
![License](https://img.shields.io/badge/License-MIT-blue)
![OpenAPI](https://img.shields.io/badge/OpenAPI%203-green)

## 功能特点

- 创建短链接：将长 URL 转换为短链接
- 自定义短链接：支持用户自定义短链接别名和过期时间
- 链接管理：查询、获取和删除短链接
- 访问统计：记录和查看短链接的访问次数
- 访问分析：提供链接访问记录统计和分布分析
- 用户认证：支持用户注册、登录和 JWT 令牌刷新
- 游客模式：支持未登录用户创建短链接
- 权限控制：基于角色的访问控制系统
- 接口文档：提供 OpenAPI 文档，支持在线测试
- 监控和统计：支持 Prometheus 和 Grafana 监控和统计

## 相关项目

- [Less URL Front](https://github.com/xioshe/less-url-front) - 配套前端项目，提供用户界面和交互，基于 Vue3 和 Naive UI 框架

## 安装说明

1. 确保您的系统已安装 Java 17 和 Maven。

2. 提供 Redis 和 MySQL 服务。可以使用 Docker Compose 快速启动。项目 enviroments 目录下提供了 [docker-compose.yml](environments/docker-compose.yml) 文件。

   ```bash
   docker compose up -d
   ```

3. 克隆仓库：

   ```bash
   git clone https://github.com/yourusername/less-url.git
   cd less-url
   ```

4. 在项目根目录创建 `.env` 文件，并填写以下配置：

    ```dotenv
    REDIS_HOST=
    REDIS_PORT=
    MAIL_HOST=
    MAIL_USERNAME=
    MAIL_PASSWORD=
    JWT_SECRET=
    MYSQL_HOST=
    MYSQL_PORT=
    MYSQL_USERNAME=
    MYSQL_PASSWORD=
    ```

5. 编译项目：

   ```bash
   mvn clean package
   ```

6. 运行应用：

   ```bash
   java -jar target/less-url-0.0.1-SNAPSHOT.jar
   ```

## 使用方法

### 创建短链接

```http
POST http://localhost:8080/links
Content-Type: application/json
Authorization: Bearer {{lu_token}}
Guest-Id: {{guest_id}} # 优先级不如 Authorization 高

{
"originalUrl": "https://example.com/very-long-url",
"customAlias": "custom",
"expiresAt": 1729594656895
}
```

游客模式使用 `Guest-Id` 头，正式用户使用 JWT 认证（`Authorization`）。

### 查询短链接

```http
GET http://localhost:8080/links?page=1&size=20&sort_by=short_url,-updated_at
Content-Type: application/json
Authorization: Bearer {{lu_token}}
Guest-Id: {{guest_id}} # 优先级不如 Authorization 高
```

支持丰富的查询条件，包括分页和排序。

游客模式使用 `Guest-Id` 头，正式用户使用 JWT 认证（`Authorization`）。

### 访问短链接

```http
GET http://localhost:8080/s/custom
```

如果使用浏览器之外的客户端，需要支持 302 重定向。

更多 API 使用示例，请参考 [Apis.http](Apis.http) 文件，或访问 [OpenAPI 文档](http://localhost:8080/swagger-ui/index.html)。

## 配置说明

主要配置选项在 [application.yml](src/main/resources/application.yml) 文件中。

### IP 归属地查询

访问记录分析支持地理位置维度，地理信息从 IP 地址获取。默认的 IP 归属地查询使用 [MAXMIND GeoIP2](https://dev.maxmind.com/geoip/geolite2-free-geolocation-data/)
提供的数据库。数据库为本地文件，默认路径为 `src/main/resources/geo/GeoLite2-City.mmdb`。GeoIP2 会自动识别并加载数据库文件。

**地理数据库文件有时效性，可能不一定准确，如有需要请下载最新文件。** 或者使用其他 IP 归属地查询服务。

使用其他 IP 归属地查询时，实现 `IpGeoDetctor` 接口，然后在 `application.yml` 中将配置项 `lu.ip-geo.provider` 的值改为非 `geoip2` 即可。

## 贡献指南

我们欢迎任何形式的贡献！请查看 [CONTRIBUTING.md](CONTRIBUTING.md) 文件了解更多详情。

## 许可证

本项目采用 MIT 许可证。详情请见 [LICENSE](LICENSE) 文件。
