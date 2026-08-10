# SYDRA 开发任务与教学任务协作交接

更新时间：2026-08-10  
项目目录：`/Users/zhouxianliang/Documents/ChatGPT/衣服app`  
适用对象：SYDRA 开发 AI、SYDRA 教学 AI、项目拥有者

## 1. 为什么拆成两个任务

本项目以后使用两个长期任务协作：

- **开发任务**：负责确认需求、梳理用户流程、修改生产代码、构建和模拟器验收。
- **教学任务**：负责读取开发任务刚完成的代码，把真实效果拆成可选择的学习主题，并用通俗语言解释完整传递链。

两者共享同一个项目目录，但写入权限不同。开发任务是生产代码的唯一默认写入者；教学任务默认只读生产代码，避免两个 AI 同时修改同一文件。

## 2. 当前刚刚完成的开发效果

### 2.1 首页效果

已实现：

- 全屏品牌背景图。
- 图片形式的 SYDRA Logo。
- `THEME TITLE`、副标题和“即刻探索”按钮。
- 首页底部四个主入口。
- 点击“即刻探索”进入选购模式页。

准确位置：

- `app/src/main/java/com/sydra/app/feature/home/HomeScreen.kt:35`：首页入口 `HomeScreen`。
- `app/src/main/java/com/sydra/app/navigation/SydraNavHost.kt:103`：首页注册及 `onExploreClick` 回调。
- `app/src/main/res/drawable-nodpi/home_background.png`：首页背景。
- `app/src/main/res/drawable-*/sydra_wordmark.png`：不同 Android 屏幕密度使用的品牌 Logo。

### 2.2 选购模式与返回效果

已实现：

- 两张大图分别进入 `CONFIGURATOR MODE` 和 `PRODUCTS LIST MODE`。
- 分类页左上角返回按钮返回两张模式图。
- 点击底部“选购”时始终重新进入两张模式图，不恢复旧分类页。
- Products List 和 Configurator 使用同一个类型安全导航图，但进入不同业务分支。

准确位置：

- `app/src/main/java/com/sydra/app/feature/catalog/ShopScreen.kt:40`：模式选择页。
- `app/src/main/java/com/sydra/app/feature/catalog/ShopScreen.kt:66`：可显示搜索或返回图标的 `ShopHeader`。
- `app/src/main/java/com/sydra/app/navigation/SydraNavHost.kt:116`：模式页路由注册。
- `app/src/main/java/com/sydra/app/navigation/SydraNavHost.kt:256`：四个顶级入口导航。
- `app/src/main/java/com/sydra/app/navigation/SydraNavHost.kt:259`：选购入口关闭 `saveState/restoreState`，确保回到模式首页。
- `app/src/main/java/com/sydra/app/navigation/SydraRoute.kt:38`：类型安全的分类、列表、详情和配置器路由参数。

### 2.3 商品浏览与购买前链路

已实现：

- Products List 分类卡片可点击。
- 左侧分类栏、连接器区域和三列商品网格。
- 点击商品进入商品详情。
- 商品详情展示真实商品图、编号、价格和 S/M/L 尺码。
- “加入购物车”写入共享购物车。
- “立即购买”写入购物车并进入购物车页。

准确位置：

- `app/src/main/java/com/sydra/app/feature/catalog/ShopScreen.kt:153`：分类页面和分类点击事件。
- `app/src/main/java/com/sydra/app/feature/catalog/CatalogFlowScreens.kt:54`：商品列表页面。
- `app/src/main/java/com/sydra/app/feature/catalog/CatalogFlowScreens.kt:230`：商品详情和尺码状态。
- `app/src/main/java/com/sydra/app/feature/catalog/CatalogData.kt:24`：当前本地商品 Repository 替身。
- `app/src/main/java/com/sydra/app/navigation/SydraNavHost.kt:130`：根据 mode 决定进入商品列表还是配置器。
- `app/src/main/java/com/sydra/app/navigation/SydraNavHost.kt:144`：商品列表路由。
- `app/src/main/java/com/sydra/app/navigation/SydraNavHost.kt:157`：商品详情路由。

### 2.4 Configurator 四步选配

已实现：

- 从 Configurator 系列列表进入选配页面。
- 1～4 步的当前步骤状态。
- 上一步、下一步、组件选项和尺码选择。
- 第四步显示 `COMPLETE`。
- 完成后生成配置商品并加入统一购物车。

准确位置：

- `app/src/main/java/com/sydra/app/feature/catalog/ShopScreen.kt:202`：不同选购 mode 对应的系列数据。
- `app/src/main/java/com/sydra/app/feature/catalog/CatalogFlowScreens.kt:351`：配置器主页面和步骤状态。
- `app/src/main/java/com/sydra/app/feature/catalog/CatalogFlowScreens.kt:427`：步骤指示器。
- `app/src/main/java/com/sydra/app/feature/catalog/CatalogFlowScreens.kt:453`：各步骤组件选项。
- `app/src/main/java/com/sydra/app/navigation/SydraNavHost.kt:171`：配置器路由和完成事件。

### 2.5 共享购物车

已实现：

- 标准商品和配置商品写入同一个购物车。
- 商品数量合并、总价计算和移除。
- 空购物车与有商品状态。
- 购物车状态由 Main 层共享，不属于某一个商品页面。

准确位置：

- `app/src/main/java/com/sydra/app/feature/cart/CartViewModel.kt:23`：购物车状态和操作。
- `app/src/main/java/com/sydra/app/feature/cart/CartScreen.kt:34`：空态、有商品状态和总价 UI。
- `app/src/main/java/com/sydra/app/navigation/SydraNavHost.kt:71`：在 `MainScaffold` 创建共享 `CartViewModel`。
- `app/src/main/java/com/sydra/app/navigation/SydraNavHost.kt:181`：购物车页面读取共享状态。

### 2.6 当前真实边界

- 商品数据来自本地 `CatalogRepository` 替身，尚未连接生产 API。
- 购物车目前存在内存中，App 进程重启后会清空。
- “去结算”目前没有地址、订单、支付接口，按钮不应被教学 AI 描述成真实支付完成。
- Audi Type 字体文件没有放入项目；当前不能声称已真正打包 Audi Type。

## 3. 教学任务应该提供的学习选择

教学 AI 每次先读取最新代码和 Git diff，然后使用下面的固定开场格式。

### 固定开场格式

#### A. 刚刚开发任务完成了什么

完整描述用户能看到和操作的效果，必须包括：

- 从哪个页面开始。
- 点击什么。
- 状态发生什么变化。
- 最后进入哪个页面。
- 返回时会回到哪里。

不能只说“增加了一个函数”或“完成导航”。

#### B. 这次你可以学习哪些效果

教学 AI 给出 3～6 个可选效果。每个选项必须包含：

- 可见效果。
- 其中可能陌生的新概念。
- 准确文件、函数和当前行号。
- 学完后项目拥有者能够自己修改什么。

#### C. 项目层次是否发生变化

如果开发新增了路由、ViewModel、Repository、资源或状态层，教学 AI必须说明：

- 原来有哪些层。
- 新增或变化了哪一层。
- 数据或事件现在怎样跨层传递。
- 这些层次变化提供了哪些学习选择。

#### D. 等待项目拥有者选择

教学 AI 只问：

> 你想先学哪一个效果？可以直接回复编号。

项目拥有者选择后，再开始详细讲代码。

## 4. 当前建议学习菜单

### 选项 1：首页图片叠层和自适应尺寸

可见效果：背景图、Logo、标题和按钮叠放在同一个首页，并适配不同手机。

新概念：

- `Box` 叠层。
- `Modifier` 链式布局。
- `ContentScale.Crop/Fit`。
- `dp`、`sp` 与真实像素。
- Android 密度资源目录。

代码：

- `app/src/main/java/com/sydra/app/feature/home/HomeScreen.kt:35`
- `app/src/main/res/drawable-nodpi/home_background.png`
- `app/src/main/res/drawable-*/sydra_wordmark.png`

学完可做到：自己调整 Logo、标题、按钮的大小和位置，并理解为什么不同手机不会简单按像素等比复制。

### 选项 2：点击事件如何从页面传到导航

可见效果：首页点击“即刻探索”后进入两张模式图。

新概念：

- 高阶函数参数 `onExploreClick: () -> Unit`。
- UI 不直接持有 `NavController`。
- 回调从外层传入内层。
- `navigate()` 和路由注册。

代码：

- `app/src/main/java/com/sydra/app/feature/home/HomeScreen.kt:35`
- `app/src/main/java/com/sydra/app/navigation/SydraNavHost.kt:103`
- `app/src/main/java/com/sydra/app/navigation/SydraNavHost.kt:256`

学完可做到：完整解释“用户点击 → 回调执行 → NavController 改变路由 → NavHost 显示新页面”的传递链。

### 选项 3：返回栈为什么会恢复旧页面

可见效果：分类页返回到模式页；重新点击底部“选购”也显示模式页。

新概念：

- Back Stack。
- 嵌套导航图。
- `popBackStack()`。
- `saveState`、`restoreState`、`launchSingleTop`。
- 顶级入口和业务子页面的区别。

代码：

- `app/src/main/java/com/sydra/app/navigation/SydraNavHost.kt:116`
- `app/src/main/java/com/sydra/app/navigation/SydraNavHost.kt:256`
- `app/src/main/java/com/sydra/app/navigation/SydraNavHost.kt:269`

学完可做到：判断某次返回应该“退一页”“回到根页面”还是“恢复之前状态”。

### 选项 4：商品列表为什么使用三列网格

可见效果：左侧固定分类，右侧三列商品；点击商品进入详情。

新概念：

- `Row/Column` 页面分区。
- `LazyVerticalGrid`。
- `GridCells.Fixed(3)`。
- `items` 与列表数据。
- 滚动容器和固定高度。

代码：

- `app/src/main/java/com/sydra/app/feature/catalog/CatalogFlowScreens.kt:54`
- `app/src/main/java/com/sydra/app/feature/catalog/CatalogData.kt:24`

学完可做到：把商品改成两列、三列或自适应列，并理解商品数据如何生成 UI。

### 选项 5：尺码状态和 Compose 重组

可见效果：点击 S/M/L 后，边框和“已选尺码”文字立即变化，加入购物车时使用当前尺码。

新概念：

- `rememberSaveable`。
- `mutableStateOf`。
- 状态改变触发 Recomposition。
- UI 状态和业务状态的边界。

代码：

- `app/src/main/java/com/sydra/app/feature/catalog/CatalogFlowScreens.kt:230`

学完可做到：自己增加 XL、默认尺码、禁用尺码或缺货状态。

### 选项 6：购物车为什么能跨页面共享

可见效果：商品详情和 Configurator 加入的内容都出现在同一个购物车。

新概念：

- `ViewModel`。
- `MutableStateFlow/StateFlow`。
- `collectAsState()`。
- 不可变列表更新。
- ViewModel 的作用域。

代码：

- `app/src/main/java/com/sydra/app/feature/cart/CartViewModel.kt:23`
- `app/src/main/java/com/sydra/app/navigation/SydraNavHost.kt:71`
- `app/src/main/java/com/sydra/app/feature/cart/CartScreen.kt:34`

学完可做到：解释为什么页面退出后购物车仍存在，并能增加数量修改、全选或持久化入口。

### 选项 7：Configurator 如何用一个页面表达四个步骤

可见效果：点击下一步后，同一个页面切换步骤、主图、选项和底部按钮；第四步显示 COMPLETE。

新概念：

- 状态机的初级形式。
- 条件渲染。
- `when` 根据状态生成不同 UI。
- 局部 UI 状态。
- 完成事件向外传递。

代码：

- `app/src/main/java/com/sydra/app/feature/catalog/CatalogFlowScreens.kt:351`
- `app/src/main/java/com/sydra/app/feature/catalog/CatalogFlowScreens.kt:427`
- `app/src/main/java/com/sydra/app/feature/catalog/CatalogFlowScreens.kt:453`

学完可做到：增加步骤、修改步骤顺序、增加“必须选择后才能下一步”的规则。

### 选项 8：项目从页面代码变成了哪些层

可见效果：相同数据和操作可以被列表、详情、配置器、购物车复用。

新概念：

- UI 层。
- Navigation 层。
- ViewModel/UiState 层。
- Repository/Model 层。
- Android Resource 层。

准确位置见下一节。

学完可做到：看到一个新功能时，先判断它应该放在哪一层，而不是把所有代码都写进一个 Screen。

## 5. 当前项目层次和它提供的学习选择

```text
MainActivity
  └─ SydraNavHost / MainScaffold           导航层
       ├─ HomeScreen                       首页 UI
       ├─ ShopScreen / CatalogFlowScreens  选购 UI
       ├─ CartScreen                       购物车 UI
       ├─ CartViewModel                    共享业务状态
       └─ CatalogRepository                本地数据替身

SydraRoute                                 路由类型和参数
res/drawable-*                             图片资源和密度变体
```

这些层次提供四条学习路径：

1. **先学看得见的 UI**：尺寸、间距、图片、文字、三列网格。
2. **再学页面跳转**：回调、Route、NavHost、返回栈。
3. **再学状态变化**：尺码、步骤、购物车、StateFlow。
4. **最后学工程分层**：Screen、ViewModel、Repository、真实 API 如何替换本地数据。

教学 AI 不应强制从架构开始。项目拥有者可以先选择一个看得见的效果，再沿着该效果追踪到对应层。

## 6. 开发任务的工作协议

开发 AI 在任何新功能编码前，必须先向项目拥有者确认以下内容。

### 6.1 功能确认

开发 AI 应先复述：

- 要实现的具体效果。
- 入口页面。
- 用户点击或输入什么。
- 页面和状态怎样变化。
- 成功后到哪里。
- 返回时到哪里。
- 空状态、错误状态、加载状态是否在本次范围。

如果设计图已经给出，可以提出合理默认值；涉及支付、账号、订单、数据安全或会明显改变核心流程的选择，必须等待确认。

### 6.2 用户流程确认

开发 AI 使用下面格式：

```text
入口页面
  ↓ 用户动作
中间页面/状态
  ↓ 用户动作
完成页面/状态
  ↩ 返回目标
```

项目拥有者确认流程后才能写业务代码。

### 6.3 实施前说明

必须说明：

1. 本关目标。
2. 新概念及其业务作用。
3. 预计修改、新增或删除的文件。
4. AI 的动作和项目拥有者的验收动作。

### 6.4 实施与验收

开发 AI 负责：

- 修改生产代码。
- 构建 Debug APK。
- 在模拟器实际点击核心链路。
- 检查返回栈。
- 给出准确文件和函数位置。
- 明确本地 mock、真实接口和未实现边界。

项目拥有者负责：

- 按给出的路径人工验收。
- 使用“页面、操作、实际结果、期望结果、设备”的格式反馈。

## 7. 教学任务的详细讲解协议

项目拥有者选择某个效果后，教学 AI 使用以下顺序讲解。

1. **先完整讲效果**：用户做了什么、看到什么。
2. **画出传递链**：从最外层注册到最内层控件，或从点击事件到状态回到 UI。
3. **列出所有相关函数**：给出准确文件、函数名和当前行号。
4. **逐段翻译代码**：每段代码下方使用生活化语言解释。
5. **解释新概念为什么存在**：不能只解释 Kotlin 语法。
6. **说明可修改点**：大小、间距、状态、目标页面分别改哪里。
7. **给一个小练习**：必须直接修改生产代码中的安全参数，不创建教学 Demo。
8. **检查是否掌握**：让项目拥有者用自己的话解释或完成一次小修改。
9. **确认掌握后更新 `memory.md`**。

教学 AI 默认不得：

- 修改 `.kt`、Gradle 或资源文件。
- 创建最终会删除的教学页面。
- 把“看过”自动记为“掌握”。
- 使用过时行号而不先重新搜索函数。
- 声称本地 mock 是真实后端。

如果项目拥有者明确要求教学 AI 帮忙改一处练习代码，教学 AI应先说明这会临时成为代码写入者，并避免与开发任务同时修改同一文件。

## 8. 两个任务之间的交接循环

```text
项目拥有者向开发任务提出功能
  ↓
开发任务确认功能和用户流程
  ↓
开发、构建、模拟器验收
  ↓
开发任务在本文件“最新开发记录”追加一条记录
  ↓
项目拥有者切换到教学任务
  ↓
教学任务读取最新记录、Git diff 和准确代码位置
  ↓
教学任务列出可选学习效果
  ↓
项目拥有者选择编号
  ↓
教学任务讲解并检查掌握情况
  ↓
确认掌握后更新 memory.md
```

### 文件写入边界

| 文件类型 | 开发任务 | 教学任务 |
|---|---|---|
| Kotlin/Gradle/资源 | 允许修改 | 默认只读 |
| 本交接文档最新开发记录 | 完成后追加 | 读取 |
| `memory.md` | 读取 | 确认掌握后更新 |
| 产品分析/路线图 | 产品或架构变化时更新 | 读取并解释 |

### 开发完成后追加记录的模板

```markdown
### YYYY-MM-DD / 功能名称

- 用户可见效果：
- 完整用户流程：
- 新概念：
- 项目层次变化：
- 修改文件和函数：
- 构建/模拟器结果：
- 当前边界：
- 推荐教学选项：
```

## 9. 最新开发记录

### 2026-08-10 / 首页到两种选购模式和统一购物车

- 用户可见效果：首页进入模式选择；标准商品经过分类、列表、详情和尺码进入购物车；配置器经过四步进入同一个购物车。
- 完整用户流程：`首页 → 模式选择 → Products List/Configurator → 对应分支 → 购物车`。
- 新概念：类型安全路由、嵌套导航、返回栈、 Compose 状态、ViewModel、StateFlow、Repository 替身。
- 项目层次变化：新增 catalog UI、cart UI、CartViewModel、CatalogRepository 和选购子路由。
- 修改文件和函数：见本文第 2 节。
- 构建/模拟器结果：Debug 构建成功；两条分支、返回和购物车已在 `emulator-5554` 验证。
- 当前边界：本地商品数据、内存购物车、未接结算和支付。
- 推荐教学选项：先学选项 2“点击到导航的传递链”，再学选项 6“跨页面共享购物车”。

### 2026-08-10 / 商品列表分类、返回栈与商品素材修正（develop tree）

- 用户可见效果：Products List 页面只保留 ACT、EVENT、AVANT、FORM、CORE 五个分类；移除 LATEST、CONNECTOR、ROUTINE 和 CONNECTORS/D60/D90/D120 区域；五个分类均可点击，商品卡片展示不同本地图片和独立编号。
- 完整用户流程：`首页 → 选购 → Products List → 五分类入口 → 商品列表 → 商品详情`；商品详情返回商品列表，商品列表返回五分类入口，五分类入口返回选购模式页；底部首页入口从选购任意子页面回到首页；分类切换只替换当前列表状态，不把旧分类重复压入返回栈。
- 新概念：Navigation Back Stack 的页面级返回与页面内状态替换、顶级入口清栈、Repository fixture 与本地图片资源绑定、商品 SKU 与商品卡片对齐。
- 项目层次变化：未新增业务层；在现有 Catalog UI、CatalogRepository 和 Main 导航层内修正状态与路由行为，并新增 6 张本地 Pexels 演示图片资源。生产接入后应由 API 返回正式 SKU、商品图和授权素材。
- 修改文件和函数：`feature/catalog/CatalogFlowScreens.kt` 的 `ProductListScreen`、`ProductCategoryRail`；`feature/catalog/CatalogData.kt` 的 `CatalogRepository.products`；`navigation/SydraNavHost.kt` 的 Products List 分类切换和 `navigateToTopLevel` 首页分支；新增 `res/drawable-nodpi/product_01.jpg` 至 `product_06.jpg`；将 `res/values/themes.xml` 的 API 27 导航栏属性拆到 `res/values-v27/themes.xml`。
- 构建/模拟器结果：在 `/Users/zhouxianliang/Documents/ChatGPT/develop tree` 的 `codex/develop-tree` 分支执行 `./gradlew :app:assembleDebug :app:lintDebug --no-daemon` 成功；安装到 `emulator-5554` 后验证五分类显示与点击、商品详情进入、顶部返回、系统返回、分类切换后返回以及商品列表内点击底部首页，均符合流程；未发现崩溃日志。
- 当前边界：商品仍来自本地 `CatalogRepository` 替身；图片为公开演示素材，不代表 SYDRA 正式商品摄影；购物车仍为内存状态，结算、订单和支付未实现。
- 推荐教学选项：先学“分类切换为什么不再制造重复页面”和“顶部/系统返回如何沿 Back Stack 工作”，再学“商品数据、SKU 和本地图片如何从 Repository 传到 ProductTile”。

### 2026-08-10 / 选购模式入口稳定性与加入购物车反馈（develop tree）

- 用户可见效果：从首页点击下方选购入口时，每次都稳定进入五分类页面（图一）；点击分类后进入商品列表（图二），不会因为上一次停留的子页面而直接落到旧分类。商品详情点击“加入购物车”后，在当前详情页底部显示“已加入购物车”短暂提示，购物车同步保留商品。
- 完整用户流程：`首页 → 选购 → 五分类页 →（返回首页后再次点击选购仍回五分类页）→ ACT/EVENT/AVANT/FORM/CORE → 商品列表 → 商品详情 → 选择尺码 → 加入购物车 → Snackbar“已加入购物车” → 底部购物车`；返回按钮仍沿当前页面层级返回，不跳过当前目标页。
- 新概念：嵌套导航入口的 `popUpTo` 清理、顶级路由 `launchSingleTop`/状态恢复策略、Compose `SnackbarHostState` 与协程触发短时反馈。
- 项目层次变化：在现有导航层稳定选购模式入口和顶级选购切换策略；在商品详情 UI 增加局部 Snackbar 状态，购物车写入仍由既有回调和内存 CartViewModel 负责。
- 修改文件和函数：`navigation/SydraNavHost.kt` 的 `ShopModeSelectionScreen` 回调、`TopLevelDestination.SHOP` 和 `navigateTopLevelRoute`；`feature/catalog/CatalogFlowScreens.kt` 的 `ProductDetailScreen`；本记录文件。
- 构建/模拟器结果：在 `/Users/zhouxianliang/Documents/ChatGPT/develop tree` 的 `codex/develop-tree` 分支执行 `./gradlew :app:assembleDebug` 与 `./gradlew :app:lintDebug --no-daemon` 均成功；安装 Debug APK 到 `emulator-5554`，验证首次及返回后再次点击选购均进入五分类页、分类进入商品列表、详情加入购物车出现“已加入购物车”、购物车显示 ACT/SKU 221026/尺码 S/数量 1；未发现崩溃日志。
- 当前边界：提示为标准 Material Snackbar，仅覆盖当前商品详情的本地加入动作；购物车仍是本地内存状态，未实现结算、订单、支付和持久化。
- 推荐教学选项：先学“`popUpTo` 与顶级导航为什么能消除旧状态”，再学“Snackbar 作为局部 UI 状态如何由按钮动作触发而不进入 ViewModel”。

### 2026-08-10 / 个人主页与 wireflow（develop tree）

- 用户可见效果：底部导航点击“我的”进入个人主页；页面按 `示意图/登录 – 14.png` 与 `登录 – 24.png` 的结构展示 SYDRA 山景头图、用户名、MEMBER/T5 会员卡、我的订单五状态入口、SYDRA 会员三张服务卡和收货地址入口；“我的”图标保持选中态。
- 完整用户流程：`任意顶级页面 → 底部“我的” → 个人主页 → 点击订单/会员/收货地址入口 → 当前页面显示“即将开放”反馈`；系统返回回到进入前的顶级页面，底部导航可继续切换首页、选购、购物车和我的。
- 新概念：顶级路由页面的系统栏明暗适配、设计稿内容与 Android 安全区域分离、页面级垂直滚动、局部 Snackbar 反馈，以及通过无障碍 Role/点击标签让图标和卡片成为可操作控件。
- 项目层次变化：新增 `feature/profile/ProfileScreen.kt`；`Profile` 顶级路由从占位 Composable 替换为正式页面；新增 `res/drawable-nodpi/profile_mountain.png`，来源为素材包中的清理版个人中心山景；未新增订单、地址、登录或支付业务层。
- 修改文件和函数：`navigation/SydraNavHost.kt` 的 `MainScaffold` 状态栏判断和 `SydraRoute.Profile` 内容；新增 `feature/profile/ProfileScreen.kt` 的 `ProfileScreen`、`ProfileHero`、`ProfileOrders`、`ProfileMemberLinks`、`ProfileAddress`；新增 `res/drawable-nodpi/profile_mountain.png`。
- 构建/模拟器结果：在 `/Users/zhouxianliang/Documents/ChatGPT/develop tree` 的 `codex/develop-tree` 分支执行 `./gradlew :app:assembleDebug --no-daemon` 与 `./gradlew :app:lintDebug --no-daemon` 均成功；安装到 `emulator-5554` 后验证进入个人主页、页面滚动、入口 Snackbar、系统返回回首页、底部导航切换；未发现崩溃日志。
- 当前边界：用户名、会员等级、会员服务图片和订单状态为本地演示内容；订单、售后、会员信息、使用说明、客服和地址入口目前只给出“即将开放”反馈，尚未接入真实业务路由、账号或接口。
- 推荐教学选项：先学“顶级导航如何承载个人主页并处理返回”，再学“设计稿中的局部状态如何拆成可复用 Compose 组件和点击回调”。

### 2026-08-10 / 个人中心下游 Wireflow 与开发 Spec（develop tree）

- 用户可见效果：本次未修改 App UI；新增一份完整开发规格，把个人主页的十个入口映射到订单、物流、退货售后、会员、帮助、客服和地址管理的目标页面、状态与返回路径。
- 完整用户流程：文档覆盖 `我的 → 全部/五状态订单 → 订单详情/取消/物流/退货/售后`、`我的 → 会员 → 权益`、`我的 → 使用说明 → 帮助文章 → 客服`、`我的 → 客户服务 → 在线聊天`、`我的 → 收货地址 → 列表 → 新增/编辑 → 保存`。
- 新概念：Wireflow、同 Route 多状态、`availableActions` 服务端资格、订单/地址快照、提交幂等、表单草稿恢复、LocalPrototype 与生产数据源替换边界。
- 项目层次变化：新增根目录 `profile-wireflow-development-spec.md`，定义总流程、各页面状态、Route、Feature 目录、领域模型、Repository 契约、八个开发阶段、测试计划、Definition of Done 和产品门禁；未新增或修改 Kotlin/资源文件。
- 修改文件和函数：新增 `profile-wireflow-development-spec.md`；追加本交接记录。没有生产函数变化。
- 构建/模拟器结果：本次为纯文档规划，未改变 APK，因此未重复构建；上一条个人主页记录中的 Debug 构建、Lint 和模拟器验收结果仍有效。已执行 `git diff --check` 验证文档无空白错误。
- 当前边界：取消订单适用状态、售后入口归属、改址节点、退货/凭证/退款规则、会员权益、客服渠道和地址删除规则仍是开发前产品门禁；文档不代表真实后端能力已接入。
- 推荐教学选项：先学“个人页十个入口如何映射到 Route”，再学“为什么订单五状态是同一个列表页面状态”，最后学习“产品门禁如何阻止前端伪造后端能力”。

### 2026-08-10 / 个人中心 API、DTO 与 Domain Model 基础（develop tree）

- 用户可见效果：本次没有新增页面；个人页下游流程获得可替换的数据契约。现有页面行为不变，后续页面可以从统一 API/Model 读取订单、地址、售后和客服状态。
- 完整用户流程：`Profile → Order/Logistics/AfterSale/Address/Member/Help/Support API → DTO → Domain Model`；当前使用 `LocalPrototypeApi`，覆盖五订单状态、取消、物流、售后提交、地址校验/默认/删除、会员、帮助和客服失败重试。
- 新概念：API 契约与实现分离、DTO 与领域 Model 分层、Mapper、`ApiResult.Success/Failure`、错误码、`availableActions`、幂等键和 LocalPrototype 数据源。
- 项目层次变化：新增 `data/api`、`data/dto`、`data/mapper`、`data/local` 和 `domain/model`；未接入网络 URL、鉴权、数据库或第三方服务。
- 修改文件和函数：新增 `data/api/ApiResult.kt`、`data/api/SydraApi.kt`、`data/dto/SydraDtos.kt`、`data/mapper/SydraMappers.kt`、`data/local/LocalPrototypeApi.kt`、`domain/model/SydraModels.kt`；同步更新 `profile-wireflow-development-spec.md`。
- 构建/模拟器结果：在 `/Users/zhouxianliang/Documents/ChatGPT/develop tree` 的 `codex/develop-tree` 分支执行 `./gradlew :app:assembleDebug :app:lintDebug --no-daemon` 成功；本关没有接入新 UI，因此沿用此前个人主页模拟器验收结果，未发现崩溃日志。
- 当前边界：`LocalPrototypeApi` 是离线确定性替身，不代表线上 API；真实 OpenAPI、鉴权、分页、上传、支付、物流、退款、客服和状态迁移仍需服务端契约。
- 推荐教学选项：先学“DTO 为什么不能直接作为 UI Model”，再学“ApiResult 错误如何被 ViewModel 转成 Loading/Empty/Error”，最后学“LocalPrototype 如何替换为网络实现”。

### 2026-08-10 / Profile API 状态接入（main）

- 用户可见效果：个人主页继续保持参考图结构，但用户名、会员等级和订单数量语义现在由 `SydraApiProvider.localPrototype` 返回；加载期间使用稳定内容占位，API 失败时显示“个人信息加载失败”和“重试”。
- 完整用户流程：`底部“我的” → ProfileViewModel.fetchProfile → ProfileDto → ProfileModel → ProfileUiState.Content → ProfileScreen`；失败路径为 `ApiResult.Failure → ProfileUiState.Error → 重试`。
- 新概念：ViewModel 生命周期、`StateFlow` UI 状态、DTO 到 Domain Model 的 Mapper、错误状态到 UI 重试事件。
- 项目层次变化：新增 `feature/profile/ProfileUiState.kt`、`feature/profile/ProfileViewModel.kt`；Profile 页面从硬编码用户文本改为读取 API Model；订单/会员/地址入口仍保持本地“即将开放”边界。
- 修改文件和函数：`feature/profile/ProfileUiState.kt`、`feature/profile/ProfileViewModel.kt`、`feature/profile/ProfileScreen.kt` 的 `ProfileScreen`/`ProfileContent`/`ProfileError`/`ProfileOrders`；`navigation/SydraNavHost.kt` 的 `MainScaffold` 与 `SydraRoute.Profile` 内容；同步更新 `profile-wireflow-development-spec.md`。
- 构建/模拟器结果：在 `/Users/zhouxianliang/Documents/ChatGPT/衣服app` 的 `main` 分支执行 `./gradlew :app:assembleDebug :app:lintDebug --no-daemon` 成功；安装到 `emulator-5554` 后进入个人页并确认 UI 正常，未发现崩溃日志。
- 当前边界：当前 API 仍为离线 `LocalPrototypeApi`；Guest 登录门禁、真实网络失败、下游订单/地址/售后 Route 尚未接入。
- 推荐教学选项：先学“ViewModel 如何把 API 结果转换成页面状态”，再学“为什么 ProfileScreen 不直接调用 API”。

## 10. 两个新任务的启动指令

### 开发任务启动指令

```text
你是 SYDRA 的开发任务。先完整阅读项目根目录 AGENTS.md、project-analysis.md、learning-roadmap.md、memory.md，以及 handoff/development-teaching-handoff.md。

你的默认职责是修改生产代码、构建和模拟器验收。任何新功能开始前，必须先和项目拥有者确认：具体效果、入口、用户动作、状态变化、成功目标、返回目标、异常/空状态范围。先给出用户流程，得到确认后再编码。

完成后必须把用户可见效果、完整流程、新概念、层次变化、准确文件/函数、验证结果、边界和推荐教学主题追加到交接文档“最新开发记录”。不要把“教学讲解”作为主要输出；教学由另一个任务负责。
```

### 教学任务启动指令

```text
你是 SYDRA 的教学任务。先完整阅读项目根目录 AGENTS.md、memory.md，以及 handoff/development-teaching-handoff.md；每次教学前读取最新开发记录和 Git diff，并重新搜索相关函数的当前行号。

你默认只读生产代码。开场必须先完整说明开发任务刚实现的用户可见效果，然后列出 3～6 个可选择的学习效果；每个选项包含陌生概念、准确代码位置和学完能修改什么。如果项目层次发生变化，说明原层次、新层次、数据/事件传递和可选学习路径。等项目拥有者选择后，再按“完整效果 → 传递链 → 所有函数 → 逐段通俗翻译 → 可修改点 → 小练习 → 掌握检查”教学。

不创建教学 Demo，不擅自修改生产代码，不把看过自动记为掌握。项目拥有者确认掌握后，才更新 memory.md。
```
