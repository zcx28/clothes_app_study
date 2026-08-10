# Android 视觉迁移说明

## 坐标与视口

- 基线稿多为 375×812 px；Android 验收视口为 360×800 dp。
- 横向换算基准：`360 / 375 = 0.96`。横坐标、宽度和横向间距可先乘 0.96，再吸附到 1dp。
- 纵向不做简单拉伸。Android 状态栏、顶部栏、底部导航栏和手势区使用系统 Insets 后重新分配内容高度。
- 纵向长稿按原内容比例进入 `LazyColumn` 或 `verticalScroll`，不得压缩到 800dp。
- V2 导出用于补充状态和内容，不保证每张都可直接按像素量取；实现时以同一页面 V1 基线结构为主。

## 核心 Token

| 类别 | Token | 建议值 |
|---|---|---|
| 颜色 | Ink | `#111111` |
| 颜色 | Paper | `#FFFFFF` |
| 颜色 | SurfaceMuted | `#F6F6F6` |
| 颜色 | Line | `#D8D8D8` |
| 颜色 | Secondary | `#777777` |
| 颜色 | Disabled | `#D9D9D9` |
| 颜色 | Price | `#E62032` |
| 字体 | Brand | Sans Serif Bold，18sp，约 4sp 字距 |
| 字体 | Display | Sans Serif Bold，28–32sp |
| 字体 | Title | Sans Serif SemiBold，16–20sp |
| 字体 | Body | Sans Serif Regular，12–14sp |
| 字体 | Meta | Sans Serif Regular，10–11sp |
| 间距 | x1/x2/x3/x4/x6/x8 | 4/8/12/16/24/32dp |
| 圆角 | None/Small | 0/2dp；原稿以直角为主 |
| 描边 | Hairline/Strong | 1/1.5dp |
| 图标 | Small/Navigation | 18/22dp 视觉尺寸；触控区域至少 48dp |

这些值是从现有稿件归纳的实现基线，不是品牌方正式设计系统。若像素测量与 Token 冲突，记录差异并以页面视觉证据校正。

## 页面骨架

- 开启 edge-to-edge；状态栏背景透明，图标按背景明暗切换。
- 顶部应用栏内容高度约 48dp，并应用 `statusBarsPadding()`。
- 一级页使用搜索/品牌结构；二级页使用返回键和居中标题或品牌。
- 底部导航内容高度约 62dp，并应用 `navigationBarsPadding()`；只用于首页、选购、购物车、我的。
- 选中导航为 Ink，未选中约 `#A5A5A5`。
- 商品详情、配置器和购物车的固定主操作区应与系统导航区隔离。

## 可复用组件

- 顶部栏、四项底部导航、品牌字标。
- 主按钮、次按钮、禁用按钮和提交中状态。
- 商品卡、分类卡、订单卡、地址卡。
- Tab、单选/复选、尺码选择、步骤进度。
- 表单字段、错误提示、加载/空/错误/成功容器。
- 订单 overflow 菜单、协议/放弃支付/取消订单弹层。

组件只接收状态与事件回调；导航由页面上层处理。

## 图片使用

`assets/cleaned/` 中 5 张图片可作为内容图使用：

| 文件 | 用途 |
|---|---|
| `home-background-clean.png` | 首页主视觉背景 |
| `mode-background-clean.png` | 选购模式背景 |
| `config-jacket-clean.png` | 配置器服装预览 |
| `act-vest-clean.png` | ACT 商品/背心 |
| `profile-mountain-clean.png` | 个人中心山景图 |

页面截图只能用于对照，不可作为交互页面背景。图片裁切使用与稿件一致的 ContentScale，并为重要图片提供语义描述。

## 来源平台外壳排除

- 不画 iOS 时间、信号、电量和 Home Indicator。
- 不画微信小程序右上角胶囊。
- 截图中的省略号只有在产品流程明确给出“更多”菜单时才实现为 Android overflow。
- 使用 Android 顶部返回、系统返回和原生安全区域，同时保持 SYDRA 内容结构和品牌风格。
