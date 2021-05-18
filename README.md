# Ocean

这是使用Spring Boot搭建的基础框架，包含了Spring Framework和Spring Boot的一些常用特性，以及Spring其他项目的特性。基本上可以把它当成一个Spring全家桶的整合。

## 如何运行

```bash

git clone https://github.com/lw900925/ocean.git

## For Windows
gradlew.bat clean build -x test

## For Linux or MacOS
gradlew clean build -x test

```

构建完成后分别运行每个模块`build/libs`目录下的`jar`文件，运行命令：

```bash
java -jar ocean-{model}-x.x.x.jar
```

也可以导入到IDE中，直接运行`*Application.java`类启动。

## 项目结构

基于Gradle工具构建的项目。

- `ocean-core`：核心模块，包含项目元数据，公共接口以及一些工具类。
- `ocean-oauth2`：Spring Security OAuth2的集成，是一个OAuth2认证服务。
- `ocean-restful`：一组RESTful API，为Web端提供调用。

## 其他说明

### UI

我用Vue和AdminLTE搭建了一个简易的前端应用[vue-AdminLTE](https://github.com/lw900925/vue-AdminLTE)，用来调用RESTful API。

### Database

由于使用Spring Data JPA作为持久层，所以不需要手动创建表结构，但需要创建好数据库，默认数据库名称为`ocean`。

### Lombok

项目中使用了Lombok简化代码，如果你在IntelliJ IDEA中启动的话，可能会无法编译，解决办法为：

1. 在Setting设置中，找到`Build, Extension, Depolyment` - `Compiler` - `Annotation Processors`。
2. 勾选`Enable annotation processing`选项并保存。

### Swagger

`ocean-restful`中已经包含了Swagger UI接口文档，项目启动后访问`http://localhost:8081/swagger-ui.html`查看。

### 测试账户

执行下面脚本插入测试数据：

```sql
INSERT INTO oc_client(`client_id`, `access_token_validity_seconds`, `additional_information`, `client_secret`, `refresh_token_validity_seconds`) VALUES ('94984796', NULL, NULL, '$2a$10$9CW3Kzt4bDDIVmoGM6nRu.tbQKOGs81lzMJ9kx.Moe8qOUkAJjMye', NULL);

INSERT INTO oc_client_grant_types(`client_id`, `authorized_grant_type`) VALUES ('94984796', 'authorization_code');
INSERT INTO oc_client_grant_types(`client_id`, `authorized_grant_type`) VALUES ('94984796', 'password');
INSERT INTO oc_client_grant_types(`client_id`, `authorized_grant_type`) VALUES ('94984796', 'refresh_token');

INSERT INTO oc_client_scopes(`client_id`, `scope`) VALUES ('94984796', 'app');

INSERT INTO oc_role(`authority`, `description`) VALUES ('ADMIN', '管理员');
INSERT INTO oc_role(`authority`, `description`) VALUES ('AUDITOR', '审计人员');
INSERT INTO oc_role(`authority`, `description`) VALUES ('DEVELOPER', '开发者');
INSERT INTO oc_role(`authority`, `description`) VALUES ('USER', '普通用户');

INSERT INTO oc_user(`username`, `account_non_expired`, `account_non_locked`, `avatar_url`, `birthday`, `client_capability`, `credentials_non_expired`, `display_name`, `email`, `enabled`, `password`, `phone_number`) VALUES ('lw900925', b'1', b'1', 'https://avatars0.githubusercontent.com/u/4954519?s=460&v=4', '1990-09-25', NULL, b'1', '鳄鱼先生', 'lw900925@163.com', b'1', '$2a$10$vCUkpagiHSB2UO5Eallx6.YTPye.WnxLam9jsGD7s2fZJFF5mdfWO', '');

INSERT INTO oc_user_role_ref(`username`, `authority`) VALUES ('lw900925', 'ADMIN');
INSERT INTO oc_user_role_ref(`username`, `authority`) VALUES ('lw900925', 'DEVELOPER');
INSERT INTO oc_user_role_ref(`username`, `authority`) VALUES ('lw900925', 'USER');

INSERT INTO ocean.oc_resource (oid, creation_date, creator, modified_date, modifier, version, description, resource_name, uri) VALUES (1392042675661029377, '2021-05-11 17:03:31.433000', 'lw900925', '2021-05-11 17:49:16.135000', 'lw900925', 2, '用户管理', '用户管理', '/users/**');
INSERT INTO ocean.oc_resource (oid, creation_date, creator, modified_date, modifier, version, description, resource_name, uri) VALUES (1392043265887682562, '2021-05-11 17:05:52.172000', 'lw900925', '2021-05-11 17:49:51.051000', 'lw900925', 2, '角色管理', '角色管理', '/roles/**');
INSERT INTO ocean.oc_resource (oid, creation_date, creator, modified_date, modifier, version, description, resource_name, uri) VALUES (1392043741232324610, '2021-05-11 17:07:45.484000', 'lw900925', '2021-05-11 17:50:05.571000', 'lw900925', 2, '资源管理', '资源管理', '/resources/**');

INSERT INTO ocean.oc_resource_role_ref (authority, oid) VALUES ('ADMIN', 1392042675661029377);
INSERT INTO ocean.oc_resource_role_ref (authority, oid) VALUES ('ADMIN', 1392043265887682562);
INSERT INTO ocean.oc_resource_role_ref (authority, oid) VALUES ('ADMIN', 1392043741232324610);
```

用户登陆密码为`123456`。

