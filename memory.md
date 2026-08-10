# SYDRA 学习记忆

本文件只保留“项目拥有者已经掌握、后续不必重复讲解”的内容，以及对后续开发有用的产品边界。

## 已掌握：工程基础

- Android Studio、Android SDK、Platform Tools、Build Tools 和 Emulator 的关系；能按“构建成功 → 安装启动 → 核对界面 → 检查崩溃”验收 Android 工程。
- Gradle Wrapper、`:app`、`assembleDebug`、Application ID、Manifest 和 `MAIN + LAUNCHER` 的职责。
- 当前启动链：`Manifest → MainActivity → onCreate → setContent → SydraApp → SydraNavHost`。
- Debug/Release、`BuildConfig.DEBUG`、`debugImplementation` 和安全日志边界；禁止记录验证码、Token、手机号、地址和支付凭证。
- Git 工作区、Commit、Branch、Remote、Push、Pull 和 `.gitignore` 的基本区别。

## 已掌握：产品与项目结构

- SYDRA 顶级入口：首页、选购、购物车、我的；选购包含 Products List 和 Configurator 两条路线。
- Android 平台外壳与业务 UI 的边界。
- 当前采用单 `:app` Module 和 feature-first：`feature` 放业务，`data` 放数据，`core` 放真正共享的模型/能力，导航与全局入口由 app 层协调。
- 同一个订单领域模型会影响订单列表、详情、物流和售后，不能为每个页面重复造一套订单数据。
- 已确认的产品决策：短信验证码登录；微信支付和支付宝；五种订单状态；Configurator 初期暂时视为组件兼容。

## 已掌握：Compose、状态与主题

- `State`、`mutableStateOf`、`remember`、`rememberSaveable` 与重组：状态变化会通知 Compose 重绘相关界面，不会重启 Activity。
- 单向数据流：`UI → Action → ViewModel → UiState → UI`；ViewModel 不是数据库，长期数据仍要由 Repository、数据库或服务器保存。
- `Scaffold` 的主内容和底部栏区域；Material 3 Theme、颜色语义、字体和 Design Token 的基本关系。

## 已掌握：依赖与导航

- Version Catalog、插件、依赖、Compose BOM 和 app 模块依赖声明的职责；BOM 只协调版本，不提供业务功能。
- 类型安全路由：无参数使用可序列化对象，有参数使用可序列化数据类；已能识别 `productId`、`orderId`、`addressId?` 等参数。
- `NavHost`、`startDestination`、`NavController.navigate()`、`NavDestination`、底部入口和 `MainScaffold` 的关系。
- `ShopGraph` 嵌套导航图及其 `ShopModeSelection` 起点。
- Lambda、作用域、函数值和 Compose 回调链：外层提供行为，内层点击后上报回调。
- 函数调用栈与 Navigation Back Stack 的区别；`popUpTo`、`launchSingleTop`、`saveState/restoreState` 的顶级入口整理作用。

## 已掌握：当前业务行动主线

### 商品详情点击“立即购买”

能说明下面的完整链路：

```text
Button.onClick
  → onBuyNow
  → CartViewModel.addProduct
  → StateFlow
  → MainScaffold.collectAsState
  → CartScreen
```

已理解：

- `ProductDetailScreen` 只负责显示详情并报告购买动作。
- `CartViewModel` 负责修改共享购物车。
- `CartScreen` 只根据新状态显示结果。
- 商品按钮文字、购物车合并规则、购物车布局属于不同层，应该修改不同文件。

## 当前项目真实边界

- `CatalogRepository` 仍是本地数据替身，尚未接生产 API。
- 购物车目前只存在内存，进程重启会清空。
- “去结算”还没有地址、订单和支付接口；不能描述成真实支付完成。
- Audi Type 字体尚未真正打包进项目。

## 教学约定

- 详细教学偏好、代码解释原则和可直接使用的提示词见 [教学风格.md](教学风格.md)。
- 本文件只记已掌握知识、项目边界和必要的不重复规则；教学任务仍默认只读生产代码，不创建临时 Demo。
- 只有项目拥有者通过解释、修改或验收证明掌握后，才把新结论加入本文件。
