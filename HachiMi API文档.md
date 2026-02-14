版本V1.014514
## 一、介绍

该API文档适用于HachiMi项目移动端和后端，定义了移动端与后端交互的接口规范，用于实现档案、动态、个人中心等核心功能。

**注：所有请求体均为JSON格式！**

### 1.1 请求头规范

所有接口请求头需遵循以下格式：

```json

{
"Content-Type": "application/json",
"Authorization": "Bearer {token}" //如果有Token的话
}

```

### 1.2 统一响应格式

所有接口返回数据均遵循以下结构，字段含义明确：

```json

{

"status": "状态响应码", 

"info": "状态响应描述", 

"data": "返回的实际数据"

}

```

### 1.3 权限枚举值

接口所需的用户权限通过以下枚举值定义：

| 枚举值（整型） | 含义   | 说明          |
| :------ | :--- | :---------- |
| 1       | 管理员  | 仅管理员可访问/操作  |
| 0       | 普通用户 | 仅已登录普通用户可操作 |
| -1      | 游客   | 无需登录即可访问    |

### 1.4响应状态码

响应状态码由以下枚举值定义：

| 枚举值（字符串型） | 含义        | 说明                     |
| :-------- | :-------- | :--------------------- |
| 200       | 访问成功      | 棍木                     |
| 400       | 业务逻辑错误    | 业务逻辑错误，详见Info字段        |
| 401       | Token验证失败 | Token过期了或者有嗨客，登录状态全部清空 |
| 500       | 后端出错      | 后端出现了一些奇异的内部错误......   |
注：本文档所用状态码均为200

### 1.5其他

时间参数需遵循`yyyy-MM-ddTHH:mm:ss.SSS+08:00`格式（东八区）

## 二、接口列表

### 2.1 档案模块

#### 2.1.1 获取档案

**接口说明**：随机返回10条档案数据至主页

**接口地址**：GET /home/homepage

**是否需要JWT认证**：否（游客权限，枚举值-1）

**响应参数说明**：

| 参数名    | 类型  | 说明                                                                             |
| :----- | :-- | :----------------------------------------------------------------------------- |
| status | 字符串 | 接口响应状态码，成功为 "200"                                                              |
| info   | 字符串 | 接口响应描述，成功为 "success"                                                           |
| data   | 集合  | - title：字符串，档案标题<br><br>- desc：字符串，档案描述<br><br>- image：字符串，档案图片 base64 编码（可为空） |

**响应示例**：

```json

{

"status": "200",

"info": "success",

"data": [

{"title":"测试2","desc":"这是一张图片","image":""},

{"title":"测试0","desc":"这是一张图片","image":"测试图片的base64编码，，，"},

{"title":"测试8","desc":"这是一张图片","image":"测试图片的base64编码，，，"},

{"title":"测试7","desc":"这是一张图片","image":"测试图片的base64编码，，，"},

{"title":"测试5","desc":"这是一张图片","image":"测试图片的base64编码，，，"},

{"title":"测试9","desc":"这是一张图片","image":"测试图片的base64编码，，，"},

{"title":"测试11","desc":"这是一张图片","image":"测试图片的base64编码，，，"},

{"title":"测试13","desc":"这是一张图片","image":"测试图片的base64编码，，，"},

{"title":"测试16","desc":"这是一张图片","image":"测试图片的base64编码，，，"},

{"title":"测试12","desc":"这是一张图片","image":"测试图片的base64编码，，，"}

]

}

```

#### 2.1.2 搜索档案

**接口说明**：根据关键字搜索档案数据

**接口地址**：GET /home/search/:keyWords

**是否需要JWT认证**：否（游客权限，枚举值-1）

**路径参数说明**：`keyWords`为搜索关键字

**响应参数说明**：

| 参数名           | 类型  | 说明                         |
| :------------ | :-- | :------------------------- |
| status        | 字符串 | 接口响应状态码，成功为 "200"          |
| info          | 字符串 | 接口响应描述，成功为 "success"       |
| data<br>(非必要) | 集合  | 搜索返回的数据列表，每个元素的字段与“获取档案“一致 |

**响应示例**：

```json

{

"status":"200",

"info":"success",

"data":[

{"title":"测试1","desc":"这是一张图片","image":"测试图片的base64编码，，，"},

{"title":"测试10","desc":"这是一张图片","image":"测试图片的base64编码，，，"},

{"title":"测试11","desc":"这是一张图片","image":"测试图片的base64编码，，，"},

{"title":"测试12","desc":"这是一张图片","image":"测试图片的base64编码，，，"},

{"title":"测试13","desc":"这是一张图片","image":"测试图片的base64编码，，，"},

{"title":"测试14","desc":"这是一张图片","image":"测试图片的base64编码，，，"},

{"title":"测试15","desc":"这是一张图片","image":"测试图片的base64编码，，，"},

{"title":"测试16","desc":"这是一张图片","image":"测试图片的base64编码，，，"},

{"title":"测试17","desc":"这是一张图片","image":"测试图片的base64编码，，，"},

{"title":"测试18","desc":"这是一张图片","image":"测试图片的base64编码，，，"}

]

}

```

#### 2.1.3 删除档案

**接口说明**：删除指定档案数据

**接口地址**：POST /admin/deleteHome

**是否需要JWT认证**：是（管理员权限，枚举值1）

**请求参数说明**：

| 参数名   | 类型  | 必要性 | 说明           |
| :---- | :-- | :-- | :----------- |
| desc  | 字符串 | 是   | 档案描述信息       |
| image | 字符串 | 是   | 档案图片base64编码 |
| title | 字符串 | 是   | 档案标题         |

**响应参数说明**：

| 参数名    | 类型   | 说明                    |
| :----- | :--- | :-------------------- |
| status | 字符串  | 接口响应状态码，成功为 "200"     |
| info   | 字符串  | 接口响应描述，成功为 "success!" |
| data   | null | 删除档案能返回啥数据？           |

**请求示例**：

```json

{

"desc":"这是一张图片",

"image":"测试图片的base64编码，，，",

"title":"测试16"

}

```

**响应示例**：

```json

{

"status":"200",

"info":"success!",

"data":null

}

```

#### 2.1.4 上传档案

**接口说明**：新增/上传档案数据

**接口地址**：POST /admin/uploadHome

**是否需要JWT认证**：是（普通用户权限，枚举值0）

**请求参数**：

| 参数名   | 类型  | 必要性 | 说明           |
| :---- | :-- | :-- | :----------- |
| desc  | 字符串 | 是   | 档案描述信息       |
| image | 字符串 | 是   | 档案图片base64编码 |
| title | 字符串 | 是   | 档案标题         |

**响应参数说明**：

| 参数名    | 类型   | 说明                   |
| :----- | :--- | :------------------- |
| status | 字符串  | 接口响应状态码，成功为 "200"    |
| info   | 字符串  | 接口响应描述，成功为 "success" |
| data   | null | 上传档案能返回啥数据？          |

**请求示例**：

```json

{

"desc":"喵喵喵喵喵喵喵喵喵喵喵",

"image":"某只哈基米的照片",

"title":"喵喵喵喵喵喵"

}

```

**响应示例**：

```json

{

"status":"200",

"info":"success",

"data":null

}

```

### 2.2 动态模块

#### 2.2.1 获取动态

**接口说明**：随机返回10条动态数据

**接口地址**：GET /chat/mainpage

**是否需要JWT认证**：否（游客权限，枚举值-1）

**响应参数说明**：

| 参数名    | 类型  | 说明                                                                                                                                             |
| :----- | :-- | :--------------------------------------------------------------------------------------------------------------------------------------------- |
| status | 字符串 | 接口响应状态码，成功为 "200"                                                                                                                              |
| info   | 字符串 | 接口响应描述，成功为 "success"                                                                                                                           |
| data   | 集合  | - title：字符串，动态标题<br><br>- profile_image：字符串，发布者头像的base64编码<br><br>- cover_image：字符串，动态封面图片<br><br>- account：字符串，发布者账号<br><br>- time：字符串，动态发布时间 |

**响应示例**：

```json

{

"status":"200",

"info":"success",

"data":[

{

"title":"测试15",

"desc":"这是一张图片",

"author_name":"测试15",

"profile_image":"测试图片的base64编码，，，",

"cover_image":"测试图片的base64编码，，，",

"account":"11451415",

"time":"2026-02-14T14:32:32.111+08:00"

},

{

"title":"测试6",

"desc":"这是一张图片",

"author_name":"测试6",

"profile_image":"测试图片的base64编码，，，",

"cover_image":"测试图片的base64编码，，，",

"account":"1145146",

"time":"2026-02-14T14:32:32.054+08:00"

},

{

"title":"测试12",

"desc":"这是一张图片",

"author_name":"测试12",

"profile_image":"测试图片的base64编码，，，",

"cover_image":"测试图片的base64编码，，，",

"account":"11451412",

"time":"2026-02-14T14:32:32.089+08:00"

},

{

"title":"测试17",

"desc":"这是一张图片",

"author_name":"测试17",

"profile_image":"测试图片的base64编码，，，",

"cover_image":"测试图片的base64编码，，，",

"account":"11451417",

"time":"2026-02-14T14:32:32.123+08:00"

}

]

}

```

#### 2.2.2 搜索动态

**接口说明**：根据关键字搜索动态数据

**接口地址**：GET /chat/search/:keyWords

**是否需要JWT认证**：否（游客权限，枚举值-1）

**路径参数说明**：`keyWords`为搜索关键字

**响应参数说明**：

| 参数名    | 类型  | 说明                            |
| :----- | :-- | :---------------------------- |
| status | 字符串 | 接口响应状态码，成功为 "200"             |
| info   | 字符串 | 接口响应描述，成功为 "success"          |
| data   | 数组  | 符合条件的动态列表，每个元素字段与 “获取动态” 接口一致 |

**响应示例**：

```json

{

"status":"200",

"info":"success",

"data":[

{

"title":"测试11",

"desc":"这是一张图片",

"author_name":"测试11",

"profile_image":"测试图片的base64编码，，，",

"cover_image":"测试图片的base64编码，，，",

"account":"11451411",

"time":"2026-02-14T14:32:32.084+08:00"

}

]

}

```

#### 2.2.3 删除动态

**接口说明**：删除指定动态数据

**接口地址**：POST /admin/deleteChat

**是否需要JWT认证**：是（管理员权限，枚举值1）

**请求参数**：

| 参数名         | 类型 | 必要性 | 说明                     |
| :------------- | :--- | :----- | :----------------------- |
| account        | 字符串 | 是     | 发布者账号               |
| author_name    | 字符串 | 是     | 发布者名称               |
| cover_image    | 字符串 | 是     | 动态封面图片base64编码   |
| desc           | 字符串 | 是     | 动态描述信息             |
| profile_image  | 字符串 | 是     | 发布者头像base64编码     |
| title          | 字符串 | 是     | 动态标题                 |
| time           | 字符串 | 是     | 动态发布时间（格式示例：2026-02-14T14:32:32.001+08:00） |

**响应参数说明**：

| 参数名    | 类型   | 说明                    |
| :----- | :--- | :-------------------- |
| status | 字符串  | 接口响应状态码，成功为 "200"     |
| info   | 字符串  | 接口响应描述，成功为 "success!" |
| data   | null | 删除动态需要啥数据？            |

**请求示例**：

```json

{

"account":"1145140",

"author_name":"测试0",

"cover_image":"测试图片的base64编码，，，",

"desc":"这是一张图片",

"profile_image":"测试图片的base64编码，，，",

"time":"2026-02-14T14:32:32.001+08:00",

"title":"测试0"

}
```

**响应示例**：

```json

{

"status":"200",

"info":"success!",

"data":null

}

```

#### 2.2.4 上传动态

**接口说明**：新增/上传动态数据（修正原文档接口地址错误）

**接口地址**：POST /admin/uploadChat

**是否需要JWT认证**：是（普通用户权限，枚举值0）

**请求参数**：

| 参数名            | 类型  | 必要性 | 说明       |
| :------------- | :-- | :-- | :------- |
| author_account | 字符串 | 是   | 发布者账号    |
| title          | 字符串 | 是   | 动态标题     |
| desc           | 字符串 | 是   | 动态描述信息   |
| cover_image    | 字符串 | 是   | 动态封面图片内容 |
|                |     |     |          |

**响应参数说明**：

| 参数名    | 类型   | 说明                    |
| :----- | :--- | :-------------------- |
| status | 字符串  | 接口响应状态码，成功为 "200"     |
| info   | 字符串  | 接口响应描述，成功为 "success!" |
| data   | null | 上传动态需要啥数据？            |

**请求示例**：

```json

{

"author_account":"11451419",

"title":"114514",

"desc":"1919810",

"cover_image":"一张元素周期表"

}

```

**响应示例**：

```json

{

"status":"200",

"info":"success",

"data":null

}

```

### 2.3 个人模块

#### 2.3.1 登录

**接口说明**：用户登录并获取认证令牌

**接口地址**：POST /reverso/login

**是否需要JWT认证**：否（游客权限，枚举值-1）

**请求参数**：

| 参数名      | 类型  | 必要性 | 说明   |
| :------- | :-- | :-- | :--- |
| account  | 字符串 | 是   | 用户账号 |
| password | 字符串 | 是   | 用户密码 |
**响应参数说明**：

| 参数名    | 类型  | 说明                                                                                                  |
| :----- | :-- | :-------------------------------------------------------------------------------------------------- |
| status | 字符串 | 接口响应状态码，成功为 "200"                                                                                   |
| info   | 字符串 | 接口响应描述，成功为 "success"                                                                                |
| data   | 对象  | - user_role：整型，用户权限枚举<br><br>- access_token：字符串，AccessToken<br><br>- refresh_token：字符串，RefreshToken |
**请求示例**：

```json

{

"account":"11451419",

"password":"123456789"

}

```

**响应示例**：

```json

{

"status":"200",

"info":"success",

"data":{

"user_name":"测试18",

"profile_image":"测试图片的base64编码，，，",

"expiration_time":"2026-02-21T17:10:49.2640751+08:00",

"user_role":0,

"access_token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoiMTE0NTE0MTgiLCJyb2xlIjowLCJ0eXBlIjoiYWNjZXNzIiwiaXNzIjoi5pu-5aeQ5aeQ5om55Y-RIiwic3ViIjoiMTE0NTE0MTgiLCJhdWQiOlsidXNlciJdLCJleHAiOjE3NzEwNjAzMDksIm5iZiI6MTc3MTA2MDI0NCwiaWF0IjoxNzcxMDYwMjQ5fQ.fcfp1bMlNeYdEmIzlllfhV0Eo5ICWyIoIugNW-A6QKI",

"refresh_token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoiMTE0NTE0MTgiLCJyb2xlIjowLCJ0eXBlIjoicmVmcmVzaCIsImlzcyI6IuabvuWnkOWnkOaJueWPkSIsInN1YiI6IjExNDUxNDE4IiwiYXVkIjpbInVzZXIiXSwiZXhwIjoxNzcxNjY1MDQ5LCJuYmYiOjE3NzEwNjAyNDQsImlhdCI6MTc3MTA2MDI0OX0.0gs43zTUiFqEKx_CQG8FFUl4jargu3_hgnBU7mQsF3Q"

}

}

```

#### 2.3.2 注册

**接口说明**：用户注册新账号

**接口地址**：POST /reverso/register

**是否需要JWT认证**：否（游客权限，枚举值-1）

**请求参数**：

| 参数名       | 类型  | 必要性 | 说明   |
| :-------- | :-- | :-- | :--- |
| account   | 字符串 | 是   | 用户账号 |
| password  | 字符串 | 是   | 用户密码 |
| user_name | 字符串 | 是   | 用户名  |

**响应参数说明**：

| 参数名    | 类型   | 说明                   |
| :----- | :--- | :------------------- |
| status | 字符串  | 接口响应状态码，成功为 "200"    |
| info   | 字符串  | 接口响应描述，成功为 "success" |
| data   | null | 注册完了去登录呗，能返回啥数据      |

**请求示例**：

```json

{

"account":"11451420",

"user_name":"scdomm",

"password":"123456789"

}

```

**响应示例**：

```json

{

"status":"200",

"info":"success",

"data":null

}

```

#### 2.3.3 注销

**接口说明**：用户注销账号

**接口地址**：POST /reverso/deregister

**是否需要JWT认证**：是（普通用户权限，枚举值0）

**请求参数**：

| 参数名       | 类型  | 必要性 | 说明   |
| :-------- | :-- | :-- | :--- |
| account   | 字符串 | 是   | 用户账号 |
| password  | 字符串 | 是   | 用户密码 |

**响应参数说明**：

| 参数名    | 类型   | 说明                   |
| :----- | :--- | :------------------- |
| status | 字符串  | 接口响应状态码，成功为 "200"    |
| info   | 字符串  | 接口响应描述，成功为 "success" |
| data   | null | 注销成功了就成功了呗，能返回啥数据    |

**请求示例**：

```json

{
"account":"11451420",

"password":"123456789"

}

```

**响应示例**：

```json

{

"status":"200",

"info":"success",

"data":null

}

```

#### 2.3.4 修改个人资料

**接口说明**：修改用户个人信息

**接口地址**：POST /reverso/change（补充请求方式为POST）

**是否需要JWT认证**：是（普通用户权限，枚举值0）

**请求参数**：

| 参数名           | 类型  | 必要性 | 说明          |
| :------------ | :-- | :-- | :---------- |
| account       | 字符串 | 是   | 用户账号        |
| user_name     | 字符串 | 是   | 新的用户名       |
| profile_image | 字符串 | 是   | 新头像base64编码 |

**响应参数说明**：

| 参数名    | 类型   | 说明                   |
| :----- | :--- | :------------------- |
| status | 字符串  | 接口响应状态码，成功为 "200"    |
| info   | 字符串  | 接口响应描述，成功为 "success" |
| data   | null | 修改成功了就成功了呗，能返回啥数据    |


**请求示例**：

```json

{

"account":"11451418",

"user_name":"scdom",

"profile_image":"测试图片的base64编码，，，"

}

```

**响应示例**：

```json

{

"status":"200",

"info":"success",

"data":null

}

```

#### 2.3.5 Token过期检测

**接口说明**：检测Token是否过期，过期则清除登录状态（修正原文档接口地址错误）

**接口地址**：POST /reverso/checkToken（补充请求方式为POST）

**是否需要JWT认证**：是（普通用户权限，枚举值0）

**请求参数（请求头传递）**：

| 请求头字段      | 类型 | 必要性 | 说明                     |
| :-------------- | :--- | :----- | :----------------------- |
| Authorization   | 字符串 | 是     | Bearer + accessToken     |
| Cookie          | 字符串 | 是     | refresh_token={refreshToken} |

**响应参数说明**：

| 参数名    | 类型   | 说明                   |
| :----- | :--- | :------------------- |
| status | 字符串  | 接口响应状态码，成功为 "200"    |
| info   | 字符串  | 接口响应描述，成功为 "success" |
| data   | null | 成功了就成功了呗，能返回啥数据      |


**响应示例**：

```json

{

"status":"200",

"info":"success",

"data":null

}

```

### 2.4JWT中间件

**范围说明**：在需要认证的接口中（即权限>=0）会自动调用，若AccessToken有问题，RefreshToken无问题，则自动生成新的AccessToken；若RefreshToken有问题，则会清除一切登录状态。

## 三、总结说明

### 3.1档案模块

| 接口名  | 请求方式 | 接口路径                   | 接口权限(JWT) |
| :--- | :--- | :--------------------- | :-------- |
| 获取档案 | GET  | /home/homepage         | 无需认证(-1)  |
| 搜索档案 | GET  | /home/search/:keyWords | 无需认证(-1)  |
| 删除档案 | POST | /admin/deleteHome      | 需要认证(1)   |
| 上传档案 | POST | /admin/uploadHome      | 需要认证(1)   |

### 3.2动态模块

| 接口名  | 接口种类 | 接口路径                   | 接口权限(JWT) |
| :--- | :--- | :--------------------- | :-------- |
| 获取动态 | GET  | /chat/mainpage         | 无需认证(-1)  |
| 搜索动态 | GET  | /chat/search/:keyWords | 无需认证(-1)  |
| 删除动态 | POST | /admin/deleteChat      | 需要认证(1)   |
| 上传动态 | POST | /admin/uploadChat      | 需要认证(0)   |

### 3.3个人模块

| 接口名     | 接口种类 | 接口路径                | 接口权限(JWT) |
| :------ | :--- | :------------------ | :-------- |
| 登录      | POST | /reverso/login      | 无需认证(-1)  |
| 注册      | POST | /reverso/register   | 无需认证(-1)  |
| 注销      | POST | /reverso/deregister | 需要认证(0)   |
| 修改      | POST | /reverso/change     | 需要认证(0)   |
| Token测试 | POST | /reverso/checkToken | 需要认证(0)   |
