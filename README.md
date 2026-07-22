# structure-job

分布式任务调度中心

## 项目简介

这是一个基于 XXL-JOB 3.4.2 的分布式任务调度中心，采用 Spring Cloud 微服务架构，集成了 Nacos 服务注册与配置管理。项目提供了 REST API 和可视化管理界面，支持任务的动态配置、调度和监控，并通过 Spring Boot Starter 简化执行器的接入。

## 项目模块

| 模块 | 说明 |
|------|------|
| `structure-job-server` | 调度中心服务端，基于 XXL-JOB Admin 增强，提供 REST API 和 Web 管理界面 |
| `structure-job-starter` | Spring Boot Starter，执行器自动配置，已发布至 Maven Central |
| `structure-job-example` | 示例执行器应用，演示如何使用 Starter 接入调度中心 |
| `xxl-job` | 内置 XXL-JOB 3.4.2 框架（含 AI 任务执行器示例） |

## 功能特性

- 任务管理：支持任务的创建、修改、删除、暂停和恢复
- 调度管理：支持 CRON 表达式配置任务执行时间
- 执行器管理：统一管理任务执行器，支持自动注册
- 日志监控：提供任务执行日志查看和分析
- 失败重试：支持任务失败自动重试机制
- 分片广播：支持任务分片执行，提高执行效率
- 权限管理：基于 XXL-SSO 的用户权限控制和访问安全
- REST API：提供完整的任务管理 REST API，支持程序化操作
- AI 任务：支持 Ollama、OpenAI、Dify 等 AI 模型驱动的任务处理
- 数据库迁移：基于 Flyway 的自动化数据库版本管理

## 技术架构

- 核心框架：XXL-JOB 3.4.2
- 后端框架：Spring Boot + Spring Cloud 2025.1.0 + Spring Cloud Alibaba 2025.1.0.0
- 服务注册/配置：Nacos
- ORM：MyBatis
- 数据库：MySQL
- 数据库迁移：Flyway
- API 文档：SpringDoc OpenAPI
- 前端界面：Freemarker + AdminLTE (Bootstrap 3) + jQuery
- 连接池：HikariCP
- 日志：Logback + structure-log-starter
- AI 集成：Spring AI 2.0.0（Ollama / OpenAI / Dify）

## 环境要求

- JDK 17+
- MySQL 8.0+
- Maven 3.8+

## 快速开始

### 1. 数据库初始化

创建 MySQL 数据库，执行初始化脚本：

```sql
CREATE DATABASE xxl_job DEFAULT CHARACTER SET utf8mb4;
```

调度中心启动后会自动通过 Flyway 执行数据库迁移，创建所需的表结构。

### 2. 修改配置文件

编辑 `structure-job-server/src/main/resources/job-server-dev.yaml`，修改数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/xxl_job?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&serverTimezone=Asia/Shanghai
    username: root
    password: your_password
```

如需启用 Nacos 配置中心，设置环境变量或在 `application.yaml` 中开启：

```yaml
NACOS_CONFIG_ENABLE: true
NACOS_SERVER: your-nacos-server:8848
```

### 3. 启动项目

```bash
# 先安装 xxl-job 到本地 Maven 仓库
cd xxl-job && mvn clean install -DskipTests

# 启动调度中心
cd ../structure-job-server
mvn spring-boot:run
```

启动后访问 `http://localhost:8080` 进入管理界面，默认账号 `admin`，密码 `123456`。

### 4. 接入执行器

在你的执行器项目中中添加依赖：

```xml
<dependency>
    <groupId>cn.structured</groupId>
    <artifactId>structure-job-starter</artifactId>
    <version>最新版本</version>
</dependency>
```

配置 `application.yaml`：

```yaml
structure:
  job:
    enable: true
    admin-addresses: http://localhost:8080
    access-token: your_token
    executor:
      appname: your-executor-name
      port: 9999
```

参考 `structure-job-example` 模块查看完整示例。

## 使用指南

### 添加新任务

1. 登录调度中心管理界面
2. 进入"任务管理"页面
3. 点击"新增"按钮
4. 配置任务信息：
   - 任务描述：填写任务名称和描述
   - 调度时间：配置 CRON 表达式
   - 任务模式：选择 BEAN 模式 或 GLUE 模式
   - 任务参数：填写任务执行参数
5. 保存并启动任务

### 通过 REST API 管理任务

调度中心提供 `/api/*` 系列 REST API，支持程序化管理任务（新增、更新、删除、启动、停止、触发），需在请求头中携带 Access Token 进行认证。

### 任务监控

- 实时调度日志查看
- 任务执行成功率统计
- 执行器在线状态监控
- 任务失败告警通知

## 配置说明

### 执行器配置

执行器是实际执行任务的组件，通过 `structure-job-starter` 自动配置：

| 配置项 | 说明 | 默认值 |
|--------|------|--------|
| `structure.job.enable` | 是否启用 | false |
| `structure.job.admin-addresses` | 调度中心地址 | - |
| `structure.job.access-token` | 通讯 Token | - |
| `structure.job.executor.appname` | 执行器名称 | - |
| `structure.job.executor.port` | 执行器端口 | - |
| `structure.job.executor.logpath` | 日志路径 | - |
| `structure.job.executor.logretentiondays` | 日志保留天数 | - |

### 负载均衡策略

支持多种负载均衡策略，多个执行器实例可自动分担负载：

- FIRST：第一个
- LAST：最后一个
- ROUND：轮询
- RANDOM：随机
- CONSISTENT_HASH：一致性 HASH
- LEAST_FREQUENTLY_USED：最不经常使用
- LEAST_RECENTLY_USED：最近最久未使用
- FAILOVER：故障转移
- BUSYOVER：忙碌转移
- SHARDING_BROADCAST：分片广播

## 部署

项目支持 Docker 容器化部署，镜像基于 `eclipse-temurin:17-jdk-alpine`：

```bash
# 构建镜像
cd structure-job-server
docker build -t structure-job-server:2.0.0 .

# 使用 Docker Compose 部署
cd depoly
docker-compose up -d
```

## 常见问题

### 1. 任务没有执行

- 检查执行器是否在线（执行器管理页面查看注册节点）
- 检查 CRON 表达式是否正确
- 查看任务日志排查错误

### 2. 调度中心无法访问

- 检查端口 8080 是否被占用
- 检查数据库连接是否正常
- 检查 Nacos 服务是否正常（如已启用）
- 检查防火墙设置

### 3. 执行器无法注册

- 检查执行器 `appname` 是否与调度中心配置一致
- 检查网络连通性（执行器需能访问调度中心地址）
- 检查 Access Token 是否一致

## 贡献指南

欢迎提交 Issue 和 Pull Request 来改进项目。

## 许可证

[Apache License 2.0](LICENSE)
