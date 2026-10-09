# 尚品甄选 · SPZX

基于 RuoYi 扩展的电商微服务项目，使用 Java 17、Spring Boot 3、Spring Cloud Alibaba 和 Vue 3，覆盖商品、用户、购物车、订单及支付宝支付接入。

> 当前仓库发布项目文档，业务源码尚未上传。以下说明依据本地 `spzx-parent` 源码整理，启动步骤适用于取得完整源码与配套配置后。数据库初始化脚本和 Nacos 配置导出暂未提供，尚未验证完整部署。

## 项目能力

| 领域 | 源码中的功能 |
| --- | --- |
| 商品 | 品牌、分类、分类品牌关联、规格、单位、商品与 SKU 管理 |
| 商城接口 | 首页、分类、品牌、商品列表与商品详情聚合 |
| 用户 | 用户信息、短信接口、地区与收货地址 |
| 购物车 | 购物项管理，以及结算时查询选中商品 |
| 订单 | 结算信息、订单与明细、交易号防重复提交、消息队列协作 |
| 支付 | 支付信息与支付宝表单支付接入 |
| 管理平台 | 用户、角色、菜单、部门、岗位、字典、参数、公告和日志 |
| 开发运维 | 文件服务、代码生成、定时任务、服务监控 |

适合学习微服务模块划分、OpenFeign 服务调用和电商业务实现。上述功能表示已有对应实现，并不代表已完成生产环境验证。

## 技术栈

版本来自本地父级 `pom.xml` 和前端 `package.json`。

| 层次 | 技术 |
| --- | --- |
| 后端 | Java 17、Spring Boot 3.0.5、Spring Cloud 2022.0.2 |
| 微服务 | Spring Cloud Alibaba 2022.0.0.0-RC2、Nacos、Gateway、OpenFeign |
| 数据访问 | MyBatis-Plus 3.5.3.1、PageHelper、Druid、MySQL |
| 缓存与消息 | Redis、RabbitMQ |
| 接口与监控 | Knife4j 4.1.0、Spring Boot Admin 3.0.4 |
| 文件与支付 | 本地文件 / FastDFS / MinIO 适配、支付宝 SDK |
| 前端 | Vue 3.3.9、Vite 5.0.4、Element Plus 2.4.3、Pinia、Vue Router |

## 模块结构

以下为完整源码结构；当前远程文档仓库尚未包含这些模块。

```text
spzx-parent/
├── spzx-ui/                 # Vue 3 管理端
├── spzx-gateway/            # API 网关
├── spzx-auth/               # 认证服务
├── spzx-api/                # 跨服务接口与共享业务模型
├── spzx-common/             # 核心、安全、日志、缓存、消息等通用模块
├── spzx-modules/
│   ├── spzx-system/         # 系统管理
│   ├── spzx-gen/            # 代码生成
│   ├── spzx-job/            # 定时任务
│   ├── spzx-file/           # 文件服务
│   ├── spzx-product/        # 商品服务
│   ├── spzx-user/           # 用户服务
│   ├── spzx-order/          # 订单服务
│   ├── spzx-channel/        # 商城接口聚合
│   ├── spzx-cart/           # 购物车服务
│   └── spzx-payment/        # 支付服务
├── spzx-visual/
│   └── spzx-monitor/       # 服务监控
├── docker/                 # 继承的容器配置，需按当前模块调整
└── pom.xml                 # Maven 多模块依赖管理
```

## 本地开发

### 1. 准备环境

- JDK 17、Maven，以及支持 Vite 5 的 Node.js 和 npm。
- MySQL、Redis、Nacos；使用订单消息流程时需要 RabbitMQ。
- 完整源码、数据库表结构与初始化数据、各服务的 Nacos 配置。
- 按需准备文件存储及支付宝沙箱配置。

前端项目未声明 Node.js `engines`。中间件版本需结合实际配置确认，当前未提供经过验证的统一版本组合。

### 2. 配置服务

各服务通过 `src/main/resources/bootstrap.yml` 设置注册中心与配置中心，默认启用 `dev`。将 Nacos discovery 和 config 两处 `server-addr` 改为自己的地址；监控服务的默认地址与其他服务不同，也需要检查。

共享配置命名为 `application-dev.yml`，服务配置按应用名与环境准备，例如 `spzx-product-dev.yml`。配置中心的 namespace、group 应与应用保持一致。

配套配置需要覆盖数据库、Redis、网关路由及所用服务的外部依赖，按需补齐 RabbitMQ、文件存储和支付配置。当前源码目录没有数据库 SQL 初始化脚本，也没有完整 Nacos 配置导出，仅运行构建命令不能完成部署。

### 3. 构建与启动后端

在完整源码根目录执行：

```bash
mvn clean package -DskipTests
```

此命令用于打包并跳过测试，不代表功能验证。通过 IDE 运行所需模块的 `*Application` 启动类；先准备中间件，再启动认证、系统管理及业务服务，最后启动网关和前端。各模块按实际调用关系一起启动。

| 服务 | 本地 bootstrap 默认端口 |
| --- | --- |
| 网关 | 8080 |
| 认证 / 系统管理 | 9200 / 9201 |
| 代码生成 / 定时任务 | 9202 / 9203 |
| 商品 / 用户 / 订单 | 9205 / 9206 / 9207 |
| 商城接口 / 购物车 / 支付 | 9208 / 9209 / 9210 |
| 文件 / 监控 | 9300 / 9100 |

Nacos 中的配置可能覆盖本地设置，请以实际运行配置为准。

### 4. 启动管理端

```bash
cd spzx-ui
npm install
npm run dev
```

开发服务器配置端口为 `81`，访问 `http://localhost:81`；端口被占用时以 Vite 输出为准。`/dev-api` 请求代理到 `http://localhost:8080`，更换网关地址时修改 `vite.config.js`。当前目录为管理端，未包含独立商城 H5 前端。

生产构建：

```bash
npm run build:prod
```

### Docker 配置现状

`docker/docker-compose.yml` 保留若依模板配置：部分 build context 指向 `./spzx/...`，实际 Dockerfile 位于 `docker/ruoyi/...`，电商业务服务尚未完整纳入 Compose。使用前需要修正路径、准备 JAR 与前端产物、补齐挂载文件及服务配置，目前不能视为一键部署方案。

## 后续完善

- 上传整理后的项目源码。
- 补充数据库初始化脚本及脱敏 Nacos 配置示例。
- 补充业务流程图、真实页面截图和接口使用示例。
- 对齐 Docker 构建目录与业务模块。
- 补充订单、支付流程测试，记录经过验证的运行环境。

## 参与贡献

问题反馈与建议请通过 [Issues](https://github.com/GooChen55/spzx-parent/issues) 提交。贡献约定见 [CONTRIBUTING.md](CONTRIBUTING.md)。

## 开源来源与许可

本项目基于 RuoYi 微服务工程扩展，保留原项目 MIT 许可和版权声明。感谢 [RuoYi-Cloud](https://gitee.com/y_project/RuoYi-Cloud) 及相关开源项目。

许可全文见 [LICENSE](LICENSE)。
