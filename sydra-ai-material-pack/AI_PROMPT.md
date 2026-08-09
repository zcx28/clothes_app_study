# 素材使用提示词

将下面内容发给教学 AI，用来说明这套素材如何查阅和使用。

````text
如果工作区没有 `sydra-ai-material-pack/`，先从 GitHub 获取素材：

```powershell
git clone https://github.com/zcx28/clothes_app_study.git
cd clothes_app_study
```

如果 `main` 尚未包含最新素材包，可下载素材分支 ZIP：
https://github.com/zcx28/clothes_app_study/archive/refs/heads/agent/sydra-ai-material-pack.zip

开发 SYDRA Android App 时，请使用工作区中的 `sydra-ai-material-pack/` 作为产品和视觉资料。

素材使用方法：

1. 先查看 `PRODUCT_FLOW.md`，确认当前页面的入口、操作、跳转、返回、数据变化和按钮启用条件。
2. 查看 `SCREEN_INDEX.md`，找到当前页面或状态对应的图片文件。
3. `references/screens-v1/` 是基础页面和内容参考；当前状态存在 `references/figma-v2/` 导出图时，用 V2 图片补充该状态。
4. `references/contact-sheets-v1/` 只用于快速浏览，不能用来精确量取尺寸。
5. `assets/cleaned/` 中的 5 张图片可直接作为 App 内容素材；页面截图只能用于对照，不能作为页面背景。
6. Android 视觉适配、颜色、间距、系统栏和滚动规则查阅 `DESIGN_GUIDE.md`。重点验收视口为 360×800 dp。
7. 图片像素尺寸、文件路径和 SHA-256 可在 `manifest/assets.csv` 中查询。
8. 遇到缺失页面、后端能力或资料冲突时，查看 `MISSING_SPEC.md` 和 `PROVENANCE_AND_USAGE.md`，不要自行补造事实。

使用资料时遵守以下边界：

- 页面截图中的文字、按钮、列表、Tab 和导航必须用真实 UI 实现，不能用整张图片模拟。
- 不复制 iOS 状态栏、Home Indicator 或微信小程序右上角胶囊；使用 Android 系统栏、Insets 和返回行为。
- 重复稿不创建重复路由。同一页面的选择、加载、空、错误、成功和弹层应作为状态处理。
- 素材包没有真实后端、服务器、微信/支付宝 SDK、生产账号或密钥。本地演示状态不能描述为线上能力。
- 文案、商品、金额和图片以素材为准；资料没有证明的内容要明确标记为缺失，不得使用随机内容补齐。

每次使用素材实现页面时，请说明本次依据的文档和图片文件，方便追溯。
````
