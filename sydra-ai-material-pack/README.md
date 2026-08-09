# SYDRA 开发素材说明

本目录提供 SYDRA Android App 的页面参考图、可用图片、产品流程和视觉说明。

## 从 GitHub 获取

仓库地址：<https://github.com/zcx28/clothes_app_study>

```powershell
git clone https://github.com/zcx28/clothes_app_study.git
cd clothes_app_study
```

如果素材分支还没有合并到 `main`，可直接下载：<https://github.com/zcx28/clothes_app_study/archive/refs/heads/agent/sydra-ai-material-pack.zip>

## 文件用途

| 文件或目录 | 用法 |
|---|---|
| `PRODUCT_FLOW.md` | 查询页面、状态、跳转、按钮条件和返回行为 |
| `DESIGN_GUIDE.md` | 查询 360×800 dp 适配、颜色、间距和 Android 平台规则 |
| `SCREEN_INDEX.md` | 根据页面名称定位参考图 |
| `MISSING_SPEC.md` | 确认哪些功能没有后端或完整规格 |
| `PROVENANCE_AND_USAGE.md` | 查询素材来源、证据优先级和使用限制 |
| `manifest/assets.csv` | 查询每张图片的尺寸和 SHA-256 |
| `assets/cleaned/` | 可直接放入 App 的 5 张内容图片 |
| `references/screens-v1/` | 38 张基础页面和状态图 |
| `references/figma-v2/` | 38 张补充状态图 |
| `references/contact-sheets-v1/` | 5 张全局快速总览图 |

## 查阅顺序

1. 用 `PRODUCT_FLOW.md` 确认当前页面的行为和状态。
2. 用 `SCREEN_INDEX.md` 找到对应图片。
3. 先看 `screens-v1/` 的基础页面；存在 V2 补充状态时再看 `figma-v2/`。
4. 需要页面中的真实内容图片时使用 `assets/cleaned/`。
5. 遇到资料缺口或冲突时查看 `MISSING_SPEC.md` 和 `PROVENANCE_AND_USAGE.md`。

页面截图只用于视觉对照，不能作为交互页面背景。素材包没有真实后端、登录或支付能力。
