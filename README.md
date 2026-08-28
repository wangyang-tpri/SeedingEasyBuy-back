# 苗木易购微信小程序后端

基于 **Spring Boot 2.7** 的苗木/园艺电商微信小程序后端服务，提供用户登录、商品管理、购物车、订单、收藏、轮播图、优惠券、评价等完整电商能力，同时支持**普通用户**与**系统管理员**两类角色。

## 技术栈

| 分类 | 技术 |
| ---- | ---- |
| 开发语言 | Java 8 |
| 基础框架 | Spring Boot 2.7.18 |
| 持久层 | MyBatis-Plus 3.5.5 |
| 数据库 | MySQL 8.x |
| 对象存储 | MinIO 8.5.7 |
| 认证授权 | JWT（jjwt 0.9.1） |
| 工具库 | Hutool 5.8.25 |
| API 文档 | springdoc-openapi（Swagger UI） |
| 构建工具 | Maven |
| 其他 | Lombok、spring-boot-starter-validation |

## 功能模块

- **用户认证**：微信 `code` 登录（MVP 阶段直接以 `code` 作为 openid，接入真实微信登录仅需替换实现）、手机号登录、用户信息查询与修改。
- **商品管理**：商品分页查询、详情、推荐、新品、搜索；卖家可发布 / 上下架 / 编辑自己的商品；支持图片、视频、SKU 规格。
- **分类管理**：商品分类列表（公开接口）。
- **轮播图管理**：前台列表展示，后台（管理员）新增 / 编辑 / 上下架 / 删除。
- **购物车**：加入 / 修改数量 / 勾选 / 删除 / 列表查询。
- **订单**：创建订单、订单列表（按状态过滤）、详情、取消、支付、确认收货。
- **收货地址**：地址增删改查、设置默认地址。
- **收藏**：收藏 / 取消收藏 / 列表查询 / 是否已收藏。
- **优惠券 / 评价**：优惠券及用户领券、商品评价（已定义实体与状态常量，接口可按需扩展）。
- **角色权限**：`role` 表区分普通用户与系统管理员，配合登录拦截器实现接口鉴权。
- **文件上传**：MinIO 对象存储，支持图片 / 视频，提供访问下载接口。

## 项目结构

```
back-end
├── pom.xml
├── src/main/java/com/nursery/
│   ├── NurseryApplication.java      # 启动类
│   ├── common/                      # 通用：Result 封装、异常处理、JWT、常量、分页、雪花 ID 等
│   ├── config/                      # 配置：MyBatis-Plus、MinIO、Jackson、WebMvc、OpenAPI
│   ├── interceptor/                 # 登录拦截器 + Token 上下文
│   └── module/                      # 按业务模块分包
│       ├── user/        # 用户、角色（登录 / 注册 / 个人信息）
│       ├── product/     # 商品、SKU、图片
│       ├── category/    # 分类
│       ├── banner/      # 轮播图
│       ├── cart/        # 购物车
│       ├── order/       # 订单、订单项
│       ├── address/     # 收货地址
│       ├── collection/  # 收藏
│       ├── coupon/      # 优惠券、用户优惠券
│       ├── review/      # 评价
│       └── upload/      # 文件上传
└── uploads/                        # 本地文件回退存储目录
```

每个模块内采用 `controller / service / mapper / entity / dto` 的标准分层。

## 快速开始

### 环境要求

- JDK 1.8+
- Maven 3.6+
- MySQL 8.x（本机默认 `localhost:3306`，库名 `nursery_app`）
- MinIO（本机默认 `localhost:9000`）

### 1. 初始化数据库

创建数据库 `nursery_app`，并导入表结构（`user`、`role`、`product`、`category`、`banner`、`cart`、`order`、`order_item`、`address`、`favorites`、`coupon`、`user_coupon`、`review` 等）。建议初始 `role` 表中预置普通用户与管理员角色，并在 `user` 表中通过 `role_id` 关联。

### 2. 配置 MinIO

```bash
# 启动 MinIO（默认账号密码均为 minioadmin）
minio server ./data
```

在 MinIO 控制台创建存储桶 `nurseryapp`（桶名可在配置中修改）。

### 3. 修改配置

编辑 `src/main/resources/application.yml`，按实际环境修改：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/nursery_app?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai&allowMultiQueries=true
    username: root
    password: your_password

jwt:
  secret: your-jwt-secret   # 生产环境务必更换

minio:
  endpoint: http://localhost:9000
  access-key: minioadmin
  secret-key: minioadmin
  bucket-name: nurseryapp
```

### 4. 启动服务

```bash
mvn spring-boot:run
```

或先打包再运行：

```bash
mvn clean package -DskipTests
java -jar target/nursery-app-1.0.0.jar
```

启动后服务默认地址：`http://localhost:8080/api`

## API 文档

启动服务后访问 Swagger UI 在线文档：

- Swagger UI：`http://localhost:8080/api/swagger-ui.html`
- OpenAPI JSON：`http://localhost:8080/api/v3/api-docs`

### 主要接口一览

| 模块 | 方法 | 路径 | 说明 |
| ---- | ---- | ---- | ---- |
| 认证 | POST | `/api/auth/login` | 微信 code 登录（MVP：code 即 openid） |
| 认证 | POST | `/api/auth/login_by_phone` | 手机号登录 |
| 用户 | GET/PUT | `/api/user/info` | 查询 / 修改个人信息 |
| 商品 | GET | `/api/product/page` | 分页查询（支持分类、关键字、排序、价格区间） |
| 商品 | GET | `/api/product/detail/{id}` | 商品详情 |
| 商品 | GET | `/api/product/recommend` | 推荐商品 |
| 商品 | GET | `/api/product/new` | 最新上架 |
| 商品 | GET | `/api/product/search` | 关键字搜索 |
| 商品 | POST | `/api/product/add` | 发布商品 |
| 商品 | GET | `/api/product/my` | 我的商品 |
| 商品 | PUT/POST | `/api/product/update/{id}`、`/api/product/status/{id}` | 编辑 / 上下架 |
| 分类 | GET | `/api/category/list` | 分类列表（公开） |
| 轮播图 | GET | `/api/banner/list` | 轮播图列表（公开） |
| 购物车 | GET/POST/PUT/DELETE | `/api/cart/list`、`/add`、`/update`、`/delete/{id}` | 购物车操作 |
| 订单 | POST | `/api/order/create` | 创建订单 |
| 订单 | GET | `/api/order/list`、`/detail/{id}` | 订单列表 / 详情 |
| 订单 | POST | `/api/order/cancel/{id}`、`/pay/{id}`、`/confirm_receive/{id}` | 取消 / 支付 / 确认收货 |
| 地址 | GET/POST/PUT/DELETE | `/api/address/list`、`/add`、`/update`、`/delete/{id}`、`/default/{id}` | 收货地址管理 |
| 收藏 | GET/POST | `/api/collection/list`、`/add`、`/check/{productId}` | 收藏管理 |
| 上传 | POST | `/api/file/upload` | 文件上传（MinIO） |
| 上传 | GET | `/api/file/{objectName}` | 文件访问 |

## 认证机制

- 登录成功返回 JWT Token，后续请求在请求头携带 `Authorization: Bearer <token>`。
- 使用 `LoginInterceptor` 统一拦截，除登录、公开查询、文件访问、Swagger 外均需登录。
- 公开接口白名单（无需登录）：
  - `/auth/login`、`/auth/login_by_phone`
  - `/file/**`
  - `/banner/list`、`/category/list`
  - `/product/page`、`/product/detail/*`、`/product/recommend`、`/product/new`、`/product/search`
  - `/swagger-ui/**`、`/swagger-ui.html`、`/v3/api-docs/**`

> 管理端接口（轮播图新增 / 编辑 / 删除等）依赖 `role` 表校验管理员角色，可在此基础上继续扩展商品审核等管理能力。

## 统一响应格式

所有接口返回统一的 `Result` 结构：

```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

- `code = 200` 表示成功；业务错误码按模块分段定义在 `Constants.java`（1xxx 用户、2xxx 商品、3xxx 订单、4xxx 购物车、5xxx 地址、6xxx 收藏、7xxx 优惠券、8xxx 评价、9xxx 通用）。
- 全局异常由 `GlobalExceptionHandler` 统一处理。

## 说明与注意事项

- **微信登录**：当前 `login` 接口在 MVP 阶段直接用 `code` 充当 openid，接生产时需调用微信 `code2Session` 接口换取真实 openid。
- **密钥安全**：`application.yml` 中的数据库密码、JWT 密钥、MinIO 凭证均为默认/示例值，部署前务必修改。
- **上传**：文件默认存储于 MinIO；`upload.path` 为本地回退目录（`./uploads/`）。
- **ID 策略**：`id-type: auto`（数据库自增），另提供 `SnowflakeIdGenerator` 备选方案。

## 后续规划

- [ ] 接入微信支付与真实微信登录
- [ ] 完善优惠券、评价模块接口
- [ ] 管理端商品审核流程
- [ ] 订单售后流程
