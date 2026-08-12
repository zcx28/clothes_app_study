# SYDRA Android App 第一阶段：项目分析

状态：已由项目拥有者确认（2026-08-12）
分析依据：`示意图/` 中 38 张 PNG 设计图  
当前工程：Android 原型工程已创建；商品、购物车、订单、地址、物流、售后与客服仍使用本地替身，生产后端尚未创建

> 2026-08-12 生产范围补充：首发为中国大陆公司主体的 SYDRA 自营成衣电商，约 10 单/日。首发只销售具有尺码 SKU 的普通成衣，Configurator 延后且正式包隐藏入口。后端采用 Kotlin/Spring Boot 模块化单体，部署在阿里云杭州地域。完整决定以 `backend-production-spec.md` 为准。

## 1. 产品理解

### 1.1 一句话产品定义

SYDRA 是一个以黑白先锋视觉为核心的服装电商 App。用户既可以浏览并直接购买成衣，也可以进入 Configurator，按类别、连接规格和组件兼容规则组合服装，然后加入购物车；购买后可管理订单、地址、物流和退货售后。

### 1.2 核心功能

1. 品牌启动、登录和会话恢复。
2. 品牌活动首页与四个主入口：首页、选购、购物车、我的。
3. 两种选购方式：
   - Products List：按分类浏览标准商品。
   - Configurator：逐层选择兼容服装组件，生成配置成品。
4. 商品详情、尺码选择、加入购物车、立即购买。
5. 购物车空态、商品选择、数量/规格编辑和结算。
6. 订单状态管理：待付款、待发货、已发货/待收货、已完成、售后。
7. 订单详情、物流轨迹、退货原因与证据提交、售后进度。
8. 收货地址列表、新增、编辑、删除和默认地址。
9. 会员信息入口、使用说明和客服入口；目前缺少其目标页面设计。

### 1.3 用户目标

- 快速找到符合风格和尺码的标准商品。
- 通过可视化组件组合得到可生产、可购买的服装配置。
- 安全完成下单和支付。
- 随时查看订单、配送和售后进度。
- 维护默认收货地址和账号信息。

### 1.4 主操作流程

#### 首次启动与登录

用户打开 App

↓

品牌启动页

↓

检查本地登录会话

↓ 无有效会话

登录与协议确认

↓ 登录成功

品牌首页

#### 标准商品购买

品牌首页

↓

点击“选购”

↓

选择 Products List Mode

↓

选择分类/连接规格

↓

商品列表

↓

商品详情与尺码选择

↓

加入购物车或立即购买

↓

使用默认地址确认订单（设计缺口，待确认交互）

↓

创建待付款订单

↓

调用支付能力

↓

进入订单状态跟踪

#### 服装组件选配

品牌首页

↓

点击“选购”

↓

选择 Configurator Mode

↓

选择 Wearable / Artefacts 与品类

↓

选择尺码和连接规格

↓

逐层选择兼容组件

↓

实时更新预览、步骤和价格

↓

完成配置

↓

加入购物车

↓

进入统一结算流程

#### 订单与物流

我的

↓

点击订单状态或全部订单

↓

订单列表（同一页面按状态筛选）

↓

查看订单详情

↓

查看物流

↓

读取物流时间线

#### 退货售后

已发货或已完成订单

↓

选择退货/售后

↓

选择退货原因

↓

按原因填写说明、订单号或上传图片/视频

↓

提交售后申请

↓

售后列表/售后详情查看处理状态

#### 地址管理

我的

↓

收货地址

↓

地址列表

↓

新增或编辑地址

↓

设置默认地址

↓

保存并返回

### 1.5 页面关系与设计图映射

文件名多数沿用了设计工具中的旧名称，“登录”并不代表这些都是登录页。以下按实际内容归类。

| 页面/状态 | 主要设计图 | 说明 |
|---|---|---|
| 启动页 | `登录 – 5.png` | SYDRA Logo 启动状态 |
| 登录页 | `登录 – 13.png` | 当前是微信手机号快捷登录文案 |
| 品牌首页 | `登录 – 6.png`、`登录 – 23.png` | 两图内容接近，是主导航首页 |
| 选购模式入口 | `登录 – 7.png`、`改1.png` | 两文件完全相同；选择 Configurator 或 Products List |
| 品类入口 | `首页.png`、`登录 – 15.png`、`登录 – 22.png` | 是不同设计迭代，需要确定最终版本 |
| 商品列表 | `登录 – 8.png` | 左侧分类＋右侧商品网格 |
| 商品详情 | `登录 – 9.png` | 尺码、价格、加入购物车、立即购买 |
| Configurator | `登录 – 16.png` 至 `登录 – 21.png` | 多个步骤/选择/完成/禁用状态，不是六个独立页面 |
| 购物车 | `登录 – 10.png` 至 `登录 – 12.png` | 空态、未选、已选三种状态 |
| 我的 | `登录 – 14.png`、`登录 – 24.png` | 同一页面的设计迭代/近似状态 |
| 订单列表 | `购买订单.png`、`购买订单 – 1.png`、`购买订单 – 4.png`、`购买订单 – 6.png` | 同一页面的多订单状态 |
| 订单详情 | `购买订单 – 3.png` | 商品与付款信息 |
| 售后详情 | `购买订单 – 5.png` | 设计信息较少，需与接口状态一起补全 |
| 售后列表 | `售后.png` | 官方处理中状态 |
| 退货申请 | `退款.png`、`退款 – 2.png`、`退款 – 3.png` | 原因选择后的动态字段状态 |
| 物流轨迹 | `物流轨迹.png` | 物流时间线与收件信息 |
| 地址列表 | `修改地址.png` | 默认、编辑、删除、添加 |
| 地址表单 | `修改地址 – 1.png` | 新增地址；编辑可复用同一页面 |
| 图标/切图 | `画板 – 1.png`、`资源 7@2x.png` | 底栏图标参考和 AVANT 标题资源 |
| 全局概览 | `iPhone 12 Pro Max – 1.png` | 早期画板概览，包含若干旧版页面方向 |

### 1.6 建议的导航图

一个 Activity，一个根 NavHost；主页面使用四个顶级目的地，业务流程使用嵌套导航图。

```text
App
├─ Splash
├─ Auth
│  └─ Login
└─ Main
   ├─ Home
   ├─ Shop
   │  ├─ ModeSelection
   │  ├─ CategorySelection
   │  ├─ ProductList
   │  ├─ ProductDetail(productId)
   │  └─ Configurator(categoryId, size)
   ├─ Cart
   └─ Profile
      ├─ OrderList(status)
      ├─ OrderDetail(orderId)
      ├─ Logistics(orderId)
      ├─ AfterSaleList
      ├─ AfterSaleDetail(requestId)
      ├─ ReturnRequest(orderId, itemId)
      ├─ AddressList
      └─ AddressForm(addressId?)
```

这里的 `AddressForm(addressId?)` 是同一页面：没有 `addressId` 时新增，有值时编辑，不额外创造编辑页。

### 1.7 设计稿中发现的产品决策与缺口

以下问题不妨碍做架构规划，但进入对应模块前必须确认。

#### P0：原生 Android 与微信小程序外壳冲突

设计图包含 iOS 状态栏、iPhone Home Indicator 和微信小程序右上角胶囊。原生 Android 不应把这些画进业务 UI。建议：

- 保留 SYDRA 内容、品牌字体、黑白布局、搜索和返回等业务动作。
- 状态栏、手势导航区交给 Android 系统处理并做 edge-to-edge 适配。
- 移除小程序胶囊；需要的更多操作改为原生 AppBar action。

#### P0：登录能力不一致（已确认）

“微信手机号快捷登录”是小程序语境。项目拥有者已于 2026-08-08 确认：原生 Android 使用短信验证码登录，不把微信授权作为登录方式。后续账号体系、后端接口、隐私政策和页面交互均以短信验证码方案设计。

#### P0：结算确认和支付设计缺失（支付渠道已确认）

购物车有“结算”，订单有“付款”，但原设计没有订单确认、地址选择、运费和支付结果页面。

项目拥有者已确认：新增正式订单确认页，展示商品、尺码、数量、地址、运费和服务端重算金额；首发全场包邮，运费明确显示 `¥0.00`。支付渠道为微信支付和支付宝。支付 SDK 的返回值不直接改变订单状态，只有服务端验签后的支付回调或主动查单结果可以确认付款成功。

#### P0：订单状态设计互相冲突（领域状态已确认）

“我的”中有待付款、待发货、待收货、已完成、售后五个入口；订单列表截图有多个不同的四标签版本。项目拥有者已于 2026-08-08 确认领域模型保留五个真实状态，同一订单列表页面按状态过滤；最终标签布局仍待确认。

#### P1：两套品类命名并存

设计中出现 `ROUTINE / ACT / FORM / EVENT / AVANT`，另有 `WEARABLE / ARTEFACTS` 和 `CORE`。需要产品给出最终分类树、排序和中英文/日文文案。

#### P1：Configurator 首发范围（已确认）

Configurator 不进入首发生产范围，正式包隐藏其入口；后端首版不建立配置组件、兼容规则或配置订单能力。未来恢复该功能时必须重新完成产品、履约、库存、退货和服务端可生产性校验评审，不能直接把原型中的“全部兼容”带入生产。

#### P1：搜索和会员卡片缺少目标页

搜索图标、会员信息、使用说明、客户服务有入口但无目标页面。遵守“不额外创造页面”原则，在设计或外链策略确认前不实现虚构页面。

## 2. 技术路线

### 2.1 技术栈

| 层面 | 选择 | 原因 |
|---|---|---|
| 语言 | Kotlin | Android 官方主流语言；空安全、协程和类型系统适合生产项目 |
| UI | Jetpack Compose | 声明式 UI，适合高复用的商品卡片、状态页面和 Configurator |
| 设计基础 | Material 3 + 自定义 SYDRA Design System | 获取可访问交互、系统适配和组件语义，同时保留品牌视觉 |
| 页面导航 | Navigation Compose 类型安全路由 | 避免字符串路由，清晰表达 `productId`、`orderId` 等参数 |
| 架构 | MVVM + 单向数据流 + Repository | 新手可理解，也能支撑生产中的状态、测试和接口替换 |
| 异步/状态 | Kotlin Coroutines、Flow、StateFlow | 统一处理网络、本地缓存和页面状态 |
| 依赖注入 | Hilt | 管理 API、Repository、数据库和 ViewModel 生命周期 |
| 网络 | Retrofit、OkHttp、kotlinx.serialization | 接口声明清楚、生态成熟、便于日志和契约测试 |
| 图片 | Coil Compose | 网络商品图加载、缓存和 Compose 集成 |
| 偏好设置 | DataStore | 保存引导状态、非敏感偏好和轻量设置 |
| 结构化缓存 | Room | 缓存分类、商品、购物车草稿、地址或待同步任务；按实际离线需求启用 |
| 密钥保护 | Android Keystore 支撑的安全存储封装 | Token 不以明文放入普通偏好或日志 |
| 后台任务 | WorkManager（按需） | 售后图片上传重试、延迟同步等必须可靠完成的任务 |
| 测试 | JUnit、协程测试、Compose UI Test、MockWebServer | 覆盖 ViewModel、Repository、导航和核心购买/售后流程 |

不在此阶段锁死依赖版本。项目初始化时以已安装 Android Studio、AGP/Kotlin 兼容矩阵和官方稳定版为准，记录到 Version Catalog。

### 2.2 项目结构

先使用一个 `app` Gradle module，内部按功能组织。这样避免在第一天引入大量 Gradle 模块成本，同时保留未来拆分边界。

```text
app/src/main/java/<package>/
├─ app/                 # Application、根导航、顶级状态
├─ core/
│  ├─ designsystem/     # 颜色、字体、间距、形状、通用组件
│  ├─ model/            # 跨功能领域模型
│  ├─ network/          # API client、DTO、错误映射
│  ├─ database/         # Room entity/DAO
│  └─ common/           # 通用结果、日志、调度器
├─ data/                # Repository 实现和数据源协调
└─ feature/
   ├─ auth/
   ├─ home/
   ├─ catalog/
   ├─ configurator/
   ├─ cart/
   ├─ order/
   ├─ aftersale/
   ├─ address/
   └─ profile/
```

拆为多 Gradle module 的触发条件：多人并行维护、功能需独立编译/发布、依赖边界频繁被破坏，或构建时间测量证明收益明显。

### 2.3 页面管理方式

- `Splash/Auth/Main` 为根级图。
- Main 内维护四个顶级目的地：首页、选购、购物车、我的。
- Catalog、Configurator、Order、AfterSale、Address 使用嵌套图。
- 路由使用可序列化类型，不使用 `"detail/42"` 字符串拼接。
- Composable 只接收数据和事件回调；导航动作由图的上层协调，不把 NavController 放进 ViewModel。

### 2.4 数据管理方式

每个页面遵循：

```text
用户点击
↓
Screen 发送 Action
↓
ViewModel 执行业务意图
↓
Repository 读取/提交数据
↓
ViewModel 更新 UiState
↓
Compose 根据状态重绘
```

业务状态以不可变 `UiState` 表示，通常包含 Loading、Content、Empty、Error 和必要的表单状态。短暂展开/折叠等局部 UI 状态留在 Compose；订单、购物车、登录等跨旋转/跨页面状态由 ViewModel/Repository 持有。

### 2.5 网络与本地存储策略

- 服务端是商品、库存、价格、订单、支付和售后的最终事实来源。
- 商品分类和图片允许缓存；价格与库存必须在下单前由服务端重新校验。
- 购物车可本地即时展示，但登录用户需要服务端同步和版本冲突处理。
- 地址可缓存用于展示，提交订单时由服务端验证。
- Token 不写入普通 DataStore，不出现在日志、截图或错误上报中。
- 没有后端时，先实现正式的 Repository/API 契约和可保留的 Fake Data Source；UI 不依赖假数据结构。

## 3. 初步领域模型

| 模型 | 关键字段 | 作用 |
|---|---|---|
| User | id、displayName、avatarUrl、memberLevel | 我的页面和账号权限 |
| Category | id、code、localizedName、mode、parentId、sortOrder | 标准商品/Configurator 分类树 |
| Product | id、name、description、images、basePrice、sizes、status | 标准商品展示 |
| Sku | id、productId、size、price、stock | 首发真正可购买的尺码库存单位 |
| Connector | id、code、label、constraints | D60/D90/D120 等连接规格 |
| Component | id、categoryId、layer、connectorIds、price、images | Configurator 可选组件 |
| CompatibilityRule | sourceId、targetId、ruleType、reason | 必选、互斥、兼容和层级规则 |
| Configuration | id/draftId、size、selectedComponents、preview、totalPrice、validation | 用户的选配结果 |
| Cart | id、items、selectedIds、total | 购物车整体状态 |
| CartItem | id、productSku、quantity、selected | 首发购物车只承载标准商品 SKU |
| Address | id、recipient、phone、region、detail、tag、isDefault | 收货信息 |
| Order | id、displayNo、status、items、amounts、addressSnapshot、timestamps | 订单状态和金额快照 |
| Payment | id、orderId、provider、status、amount | 支付流程，不把支付状态混入 UI 猜测 |
| Shipment | carrier、trackingNo、status、events | 物流轨迹 |
| AfterSaleRequest | id、orderItemId、type、reason、description、evidence、status | 退货/售后申请 |

## 4. 接口协议初案

统一约定建议：

- 基础路径：`/api/v1`
- JSON 字段使用稳定英文名，展示文案可由后端返回本地化内容或前端资源映射。
- 金额使用最小货币单位整数，例如 `amountMinor: 88000`、`currency: "CNY"`，避免浮点误差。
- 时间使用 ISO 8601，例如 `2026-08-08T09:41:00+08:00`。
- 成功响应直接返回资源或统一 `data`；错误至少包含稳定 `code`、用户可读 `message` 和可追踪 `requestId`。
- 订单创建、支付发起、售后提交等写操作使用幂等键，防止用户重复点击造成重复订单。

### 4.1 主要端点

| 方法与 URL | 请求重点 | 返回重点 | 常见错误 |
|---|---|---|---|
| `POST /api/v1/auth/login` | provider、授权凭证/验证码、协议版本 | accessToken、refreshToken、user | AUTH_FAILED、CONSENT_REQUIRED |
| `POST /api/v1/auth/refresh` | refreshToken | 新 Token | SESSION_EXPIRED |
| `GET /api/v1/home` | locale、appVersion | Banner、活动内容 | CONTENT_UNAVAILABLE |
| `GET /api/v1/categories` | mode、parentId | 分类树 | CATEGORY_NOT_FOUND |
| `GET /api/v1/products` | categoryId、cursor、filters | 商品分页 | INVALID_FILTER |
| `GET /api/v1/products/{id}` | productId | 商品、SKU、尺码、库存摘要 | PRODUCT_NOT_FOUND |
| Configurator 接口 | 首发不提供 | 正式包隐藏入口；未来重新评审 | NOT_IN_V1_SCOPE |
| `GET /api/v1/cart` | 当前用户 | 购物车 | UNAUTHORIZED |
| `POST /api/v1/cart/items` | skuId、quantity | 更新后的购物车 | OUT_OF_STOCK、PRICE_CHANGED |
| `PATCH /api/v1/cart/items/{id}` | quantity、selected | 更新后的购物车 | CART_ITEM_NOT_FOUND |
| `DELETE /api/v1/cart/items/{id}` | itemId | 204 | CART_ITEM_NOT_FOUND |
| `POST /api/v1/orders` | cartItemIds、addressId、priceVersion | 待付款订单 | ADDRESS_INVALID、PRICE_CHANGED、OUT_OF_STOCK |
| `GET /api/v1/orders` | status、cursor | 订单分页 | INVALID_STATUS |
| `GET /api/v1/orders/{id}` | orderId | 订单详情 | ORDER_NOT_FOUND |
| `POST /api/v1/orders/{id}/payments` | provider、returnScheme | 支付参数 | ORDER_NOT_PAYABLE |
| `GET /api/v1/orders/{id}/shipment` | orderId | 物流与时间线 | SHIPMENT_NOT_READY |
| `POST /api/v1/after-sales` | orderItemId、reason、description、evidenceIds | 售后申请 | RETURN_WINDOW_EXPIRED |
| `GET /api/v1/after-sales` | status、cursor | 售后列表 | INVALID_STATUS |
| `GET /api/v1/addresses` | 当前用户 | 地址列表 | UNAUTHORIZED |
| `POST /api/v1/addresses` | 联系人、电话、地区、详情、默认值 | 新地址 | ADDRESS_INVALID |
| `PATCH /api/v1/addresses/{id}` | 可修改字段 | 更新地址 | ADDRESS_NOT_FOUND |
| `DELETE /api/v1/addresses/{id}` | addressId | 204 | ADDRESS_IN_USE |

### 4.2 代表性请求与响应

#### 创建订单

```http
POST /api/v1/orders
Idempotency-Key: 663b9a08-...
Authorization: Bearer <token>
Content-Type: application/json
```

```json
{
  "cartItemIds": ["cart_item_1", "cart_item_2"],
  "addressId": "address_8",
  "priceVersion": "price_v31"
}
```

```json
{
  "id": "order_1001",
  "displayNo": "202608081001",
  "status": "PENDING_PAYMENT",
  "amountMinor": 760000,
  "currency": "CNY",
  "expiresAt": "2026-08-08T10:11:00+08:00"
}
```

Configurator 首发不提供生产请求或响应示例，避免原型契约被误接入正式交易链路。未来恢复该功能时单独完成接口评审。

真实公司中，前端与后端会先共同确认请求、响应和错误码，再分别开发。前端可用相同 JSON 契约驱动本地替身，后端完成后只替换数据源，不重写页面。

## 5. 完整开发路线图概览

| 阶段 | 目标 | 主要交付物 | 模块级验收 |
|---|---|---|---|
| 0. 产品与设计对齐 | 消除平台、登录、支付、状态和分类冲突 | 本分析、最终页面清单、产品决策 | 核心流程无未说明的分叉 |
| 1. 工程初始化 | 建立可持续开发基础 | Android 工程、Git 基线、构建配置、包结构 | Debug App 在模拟器/真机启动 |
| 2. 导航与页面骨架 | 所有已确认页面可到达 | 类型安全路由、顶级导航、空状态骨架 | 按验收路径往返页面且返回栈正确 |
| 3. 设计系统与基础组件 | 把视觉规则变成可复用代码 | 颜色、字体、间距、图标、AppBar、按钮、卡片 | 关键组件与设计稿对比通过 |
| 4. 商品与 Configurator UI | 完成核心选购体验 | 首页、分类、列表、详情、选配状态机 | 标准商品与选配流程可完整走通 |
| 5. 数据层与接口替身 | 页面不再依赖硬编码 | Model、Repository、Fake/fixture、缓存策略 | 切换数据状态能正确显示加载/空/错/成功 |
| 6. 购物车、订单与地址 | 建立交易主链路 | Cart、Order、Address、接口契约 | 创建待付款订单前的校验流程通过 |
| 7. 物流与售后 | 完成购买后的服务闭环 | Logistics、Return、AfterSale、证据上传 | 提交申请并显示处理状态 |
| 8. 后端联调与支付 | 接入真实服务 | API、鉴权、刷新 Token、支付回调/状态轮询 | 测试环境端到端成功且失败可恢复 |
| 9. 质量与发布 | 达到可交付标准 | 测试、性能、无障碍、隐私、签名、发布包 | Release 构建、回归清单和交付文档通过 |

详细学习内容和每阶段任务见 `learning-roadmap.md`。

## 6. 验证与 Worktree 策略

### 验证节奏

- 一个页面骨架或单个基础组件完成后只做必要预览。
- 一个完整模块完成后统一编译、Lint、单元测试和人工验收。
- 核心导航、订单金额、配置兼容性、重复提交和 Token 刷新属于必须自动化验证的高风险点。
- UI 精准还原使用关键页面截图对比：启动、首页、选购入口、商品详情、Configurator、购物车、我的、订单、退货。

### Worktree 判断

当前不使用 Worktree，因为仓库还没有工程和主线代码，分析文档不会破坏任何实现。未来在以下场景使用：

- Configurator 渲染方案需要同时比较 Canvas、图片叠层或服务端预览。
- 导航/模块化重构会大范围移动稳定代码。
- 支付 SDK 或架构升级可能让主分支暂时无法构建。

简单页面、局部 UI 和单一 Bug 修复不使用 Worktree。

## 7. 本阶段完成标准

- [x] 盘点现有 38 张设计图和重复文件。
- [x] 推断核心功能、主要用户流程和页面关系。
- [x] 给出 Android 技术路线、项目结构、导航、数据和存储方案。
- [x] 建立初步领域模型和 API 协议方向。
- [x] 记录设计冲突、缺失页面和高风险产品决策。
- [x] 项目拥有者于 2026-08-12 确认本分析及首发生产范围。

后端交付按 `backend-production-spec.md` 的阶段门禁推进；每个小任务经人工验收并回复 `ok` 后才进入下一任务。

## 8. 阶段 1 环境基线

检查日期：2026-08-08  
检查结论：本机具备创建和运行 Android 工程的工具链；当前工作区仍未创建 Android 项目。

| 项目 | 检查结果 |
|---|---|
| 操作系统 | Windows 11 家庭版中文版，64 位，Build 26200 |
| Android Studio | 2026.1.2 AI，安装目录 `D:\\Android\\Sdk`，可执行文件 `D:\\Android\\Sdk\\bin\\studio64.exe` |
| Java/JDK | Android Studio JBR，OpenJDK 21.0.10，路径 `D:\\Android\\Sdk\\jbr` |
| Android SDK | `D:\\Android\\AndroidSdk` |
| Android Platform | `android-36`、`android-36.1` |
| Build Tools | `35.0.0`、`36.0.0` |
| Platform Tools | `37.0.0`，包含 ADB |
| Emulator | `36.6.11` |
| 可用 AVD | `Pixel_8_API_36`，Android 16 / API 36 / Google Play / x86_64 |
| Gradle 缓存 | 已存在 8.13、9.3.0、9.4.1、9.5.0 发行包缓存 |
| Android Gradle Plugin 缓存 | 已存在 8.7.3、8.12.2、9.3.1 缓存 |
| Kotlin Gradle Plugin 缓存 | 已存在 2.0.21、2.2.10 缓存 |
| Git | 2.55.0.windows.3 |

已通过 `tools/setup-android-env.ps1` 完成当前 Windows 用户级配置：写入 JDK、SDK、AVD 和 Gradle 变量，并将必要工具路径去重后追加到用户 PATH；没有修改系统级 PATH。JDK 21、ADB 37.0.0 和 `Pixel_8_API_36` 已由脚本外部验证通过。由于已打开的终端和 Android Studio 不会自动继承新环境，人工验收前需要关闭并重新打开它们。
