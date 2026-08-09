# SYDRA 教学开发素材包

这是一套供教学 AI 使用的 Android App 素材。AI 应依据图片和说明，一次带学习者完成一个小任务，不要一次生成整套工程后让学习者自行理解。

## 使用方法

1. 把整个 `sydra-ai-material-pack/` 放进开发工作区。
2. 将 `AI_PROMPT.md` 全文发送给教学 AI。
3. AI 每次只讲解并完成一关。
4. 学习者按 AI 给出的路径运行和验收，通过后回复 `ok`。

## AI 需要查阅的文件

| 文件 | 用途 |
|---|---|
| `AI_PROMPT.md` | 教学方式和开发要求 |
| `PRODUCT_FLOW.md` | 页面、状态、跳转和返回关系 |
| `DESIGN_GUIDE.md` | Android 视觉迁移规则 |
| `SCREEN_INDEX.md` | 图片与页面对应关系 |
| `MISSING_SPEC.md` | 不得虚构的后端和产品能力 |
| `PROVENANCE_AND_USAGE.md` | 素材来源和使用提示 |
| `manifest/assets.csv` | 图片尺寸和 SHA-256 |

## 图片目录

- `assets/cleaned/`：5 张可直接用于 App 的内容图片。
- `references/screens-v1/`：38 张基础页面和状态图。
- `references/contact-sheets-v1/`：5 张快速总览图。
- `references/figma-v2/`：38 张补充状态图。

共 86 张图片。页面截图只用于对照，不能作为交互页面背景。

## 开发边界

- 目标为 Android 手机竖屏，重点验收 360×800 dp。
- 使用 Kotlin、Jetpack Compose、Navigation Compose、ViewModel 和 StateFlow。
- 无后端时只实现明确标注的本地教学状态。
- 不绘制 iOS 状态栏、Home Indicator 或微信小程序胶囊。
- 不虚构登录、支付、库存、订单或服务器接口。
- 重复稿和同一页面的不同状态不能创建成重复路由。

具体页面和视觉细节由 AI 按当前教学任务查阅，无需学习者预先读完全部文档。
