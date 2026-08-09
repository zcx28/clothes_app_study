# 图片索引

## V1 基线图

`references/screens-v1/` 共 38 张 PNG：35 张页面/状态图、2 张资源图、1 张参考总画板。

| 前缀 | 页面/状态 | 说明 |
|---|---|---|
| 00 | 底部导航图标、AVANT 标题、参考总画板 | 资源与全局参考，不是业务 Route |
| 01 | 启动页 | Splash |
| 02 | 微信快捷登录 | Login 基线视觉；真实微信能力未提供 |
| 03 | 首页主稿、备选稿 | 同一 Home 的视觉迭代 |
| 04 | 选购模式、完全重复稿 | 同一 ShopMode；不得建重复 Route |
| 05 | 配置器系列选择 | Series；长图内容需滚动 |
| 06 | 商品分类服饰/配件 | 同一 Category 的两个 Tab 状态 |
| 07 | ACT 商品列表 | ProductList |
| 08 | ACT 背心详情 | ProductDetail |
| 09 | 配置器步骤 1 | Configurator 状态 |
| 10 | 配置器步骤 2 | Configurator 状态 |
| 11 | 步骤 3 未选择/已选择 | 同 Route 两状态 |
| 12 | 步骤 4 待确认/可完成 | 同 Route 两状态 |
| 13 | 购物车空/未勾选/全选 | 同 Cart 三状态 |
| 14 | 个人中心主稿/微调稿 | 同 Profile 的视觉迭代 |
| 15 | 订单待付款 | Orders Tab 状态 |
| 16 | 订单待发货更多菜单 | Orders Tab + 菜单状态 |
| 17 | 订单已发货更多菜单 | Orders Tab + 菜单状态 |
| 18 | 订单已完成 | Orders Tab 状态 |
| 19 | 售后处理中列表 | Orders/AfterSales 状态 |
| 20 | 订单详情 | OrderDetail |
| 21 | 售后详情 | AfterSalesDetail |
| 22 | 退货订单号 | ReturnOrder |
| 23 | 退货原因 | ReturnReason |
| 24 | 其他原因和凭证 | ReturnEvidence |
| 25 | 地址列表 | AddressList |
| 26 | 新增地址 | AddressAdd |
| 27 | 物流详情 | Logistics |

文件名已经写明完整页面/状态名称，应保留原名以便追溯。

## V2 补充图

`references/figma-v2/` 共 38 张 PNG，其中 37 张状态导出和 1 张联系表。文件名是 Figma Node ID 的文件安全形式，例如 `94-1124.png` = Node `94:1124`。

- `f01-auth/`：协议、登录中、登录失败和协议弹层。
- `f02-f03-commerce-configurator/`：搜索、尺码、确认订单、地址选择、支付与支付结果；含 `contact-sheet.png`。
- `f04-f05-orders-profile/`：取消订单、退款进度、会员、帮助、客服聊天和地址保存状态。

具体 Node 到流程的对应关系见 `PRODUCT_FLOW.md`。

## 联系表

`references/contact-sheets-v1/contact-sheet-01.jpg` 至 `contact-sheet-05.jpg` 只用于快速浏览 38 张 V1 图。它们经过缩放和排版，不能作为像素尺寸、颜色或裁切的最终依据。

## 清理素材

`assets/cleaned/` 的 5 张图片已从页面视觉中整理，可作为 App 内容图加载。它们不包含按钮、导航或页面文字。详见 `DESIGN_GUIDE.md`。
