# 来源、优先级与使用提示

## 来源

- V1 页面整理来源：本地 `frontend/product-screens/screens/`。
- 清理素材来源：本地 `frontend/product-screens/assets/`。
- 联系表来源：本地 `frontend/product-screens/review/`。
- V2 补充状态：从 Figma“Android 完整原型”相关节点导出的 PNG。
- Figma 参考文件：<https://www.figma.com/design/sdgfREvBQKYn4TwNCcAGPa/%E8%A1%A3%E6%9C%8D?node-id=0-1>
- 原始示意图仅用于溯源；本包优先使用已经整理、清理和编号的文件。

## 事实使用顺序

1. `PRODUCT_FLOW.md`：当前包内已整理的页面、状态、返回和数据变化。
2. 对于 V2 明确补充的状态，使用对应 `references/figma-v2/` 图片。
3. 其他内容和视觉基线使用 `references/screens-v1/`。
4. App 内容图片使用 `assets/cleaned/`。
5. `contact-sheets-v1/` 只做快速浏览。
6. 有冲突时记录冲突，不能为了实现方便静默选边。

## 可追溯性

- V1 文件名包含序号、模块、页面/状态和原图名。
- V2 文件名对应 Figma Node ID，连字符替代冒号。
- `manifest/assets.csv` 包含相对路径、像素尺寸、字节数和 SHA-256。

## 使用权提示

本包没有附带品牌方或设计素材权利人的正式授权文件，因此不能把本文件理解为版权、商标或商业使用许可。仓库拥有者和使用者应自行确认：

- SYDRA 名称、Logo、商品图和摄影图的权利归属。
- Figma 文件及其导出图片是否允许公开、再分发和商业使用。
- 上架应用、宣传和训练用途是否需要额外授权。

在权利未确认前，建议仅用于当前项目开发、内部评审和学习，不要擅自用于其他品牌或公开商业发布。

## 隐私与安全

素材包不应包含 `.env`、`local.properties`、签名文件、Token、账号、支付凭据或真实用户数据。若后续补充图片，上传前应检查 EXIF、截图通知、手机号、地址和订单号等敏感信息。
