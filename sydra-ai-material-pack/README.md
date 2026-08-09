# SYDRA AI 开发素材包

版本：2026-08-09

用途：向其他 AI 或开发者提供 SYDRA 服装 Android App 的视觉事实、可验证流程、设计约束与缺失规格。

内容边界：仅图片、说明和清单；不含应用源码、可执行文件、后端、接口实现、账号、密钥或生产配置。

## 快速开始

1. 先读本文件和 `PROVENANCE_AND_USAGE.md`。
2. 浏览 `references/contact-sheets-v1/`，快速建立全局页面印象。
3. 按 `SCREEN_INDEX.md` 逐张核对页面与状态。
4. 按 `PRODUCT_FLOW.md` 建路由和状态机。
5. 按 `DESIGN_GUIDE.md` 迁移到 Android 360×800 dp。
6. 把 `AI_PROMPT.md` 全文交给负责开发的 AI。
7. 使用 `manifest/assets.csv` 校验文件尺寸和 SHA-256，避免素材丢失或被误改。

## 目录

```text
sydra-ai-material-pack/
├─ assets/cleaned/                         # 5 张可直接放入 App 的清理素材
├─ references/screens-v1/                  # 38 张基线页面、状态、资源与总画板
├─ references/contact-sheets-v1/           # 5 张快速总览联系表
├─ references/figma-v2/                    # 38 张补充状态导出（含 1 张联系表）
│  ├─ f01-auth/
│  ├─ f02-f03-commerce-configurator/
│  └─ f04-f05-orders-profile/
├─ manifest/assets.csv                     # 路径、类型、像素尺寸、字节数、哈希
├─ AI_PROMPT.md                            # 给其他 AI 的完整开发提示词
├─ PRODUCT_FLOW.md                         # 页面、状态、操作与返回关系
├─ DESIGN_GUIDE.md                         # Android 迁移与视觉规则
├─ SCREEN_INDEX.md                         # 图片索引和重复稿说明
├─ MISSING_SPEC.md                         # 不得自行补造的规格与后端边界
└─ PROVENANCE_AND_USAGE.md                 # 来源、优先级与使用权提示
```

## 证据层级

这套资料经历了两个阶段，不能把所有图片视为同一时间的同一版稿：

1. `screens-v1/` 是基线页面与内容证据，文件名已按页面/状态整理。
2. `figma-v2/` 是后续从 Figma“Android 完整原型”导出的补充状态。当某个状态存在 V2 导出时，以对应 V2 图片和 `PRODUCT_FLOW.md` 为准。
3. `assets/cleaned/` 是可实现内容素材，不是可点击页面截图。
4. `contact-sheets-v1/` 只用于快速对照，不用于量取尺寸。
5. 同名近似稿和完全重复稿是视觉证据，不应创建重复路由。

已知冲突已写入 `PRODUCT_FLOW.md`。不要为实现方便自行选择冲突一方，也不要把无证据推断写成事实。

## 重要禁令

- 不得把整张页面截图作为 App 背景来模拟页面。
- 不得用静态图片模拟按钮、文字、列表、Tab、输入框或导航。
- 不得绘制假的 iOS 状态栏、Home Indicator 或微信小程序右上角胶囊。
- 不得虚构真实登录、支付、库存、订单、退款、客服或服务器接口。
- 不得把本地确定性状态称为线上能力。
- 不得因缺少页面而擅自设计并计入完成范围。

## 图片数量

| 类别 | 数量 | 用途 |
|---|---:|---|
| 清理素材 PNG | 5 | 首页、模式选择、配置器、商品和个人中心内容图 |
| V1 PNG | 38 | 35 张页面/状态、2 张资源、1 张总画板 |
| V1 联系表 JPG | 5 | 全局快速浏览 |
| V2 PNG | 38 | 37 张补充状态、1 张联系表 |
| 合计 | 86 | 81 PNG + 5 JPG |

## 推荐开发目标

- Android 手机竖屏，重点视口 360×800 dp。
- Kotlin、Jetpack Compose、Material 3 基础组件和自定义 Token。
- 类型安全 Navigation Compose。
- ViewModel + StateFlow + 单向数据流。
- 本地确定性数据仅用于资料可验证的原型状态。
- 真实服务接入必须等待正式 API 契约、鉴权方式和服务端环境。
