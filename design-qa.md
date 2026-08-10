# Shop Mode Selection Design QA

- Source visual truth: `示意图/登录 – 7.png`
- Implementation screenshot: `design-qa/shop-mode-selection-emulator.png`
- Side-by-side evidence: `design-qa/shop-mode-selection-comparison.png`
- State: Shop mode-selection screen with the Shop bottom destination selected
- Emulator viewport: 1080 × 2400 px, Android density 420 dpi
- Source: 375 × 812 px
- Normalization: implementation scaled to 375 × 833 px for equal-width comparison; the extra height is native Android system navigation chrome

## Full-view comparison

The final screen preserves the source hierarchy: native status area, compact SYDRA header, two equal full-bleed fashion cards, white editorial labels, and a persistent four-icon bottom bar. Android owns the status and system-navigation areas. The iOS status icons and mini-program capsule are intentionally not reproduced.

Focused comparison was performed on the two card crops, title blocks, and bottom navigation because those are the fidelity-critical regions named by the user.

## Required fidelity surfaces

- Fonts and typography: bold white uppercase hierarchy, line break, letter spacing, and contrast match the source closely. The temporary text-rendered SYDRA wordmark remains a P3 refinement until a production logo/font asset is supplied.
- Spacing and layout rhythm: header, two equal cards, left text inset, dividers, and bottom navigation follow the source structure. Native Android system-bar height is an intentional platform difference.
- Colors and visual tokens: white/black/gray palette, selected black icon, and unselected gray icons match the source direction.
- Image quality and asset fidelity: final project assets are wide, high-resolution editorial photographs with safe left copy space; hats, faces, outerwear, and backpacks remain inside the card crops.
- Copy and content: `CONFIGURATOR MODE` and `PRODUCTS LIST MODE` match the source, and each card exposes a button role for accessibility.

## Comparison history

1. Initial pass — blocked:
   - P1: portrait images lost the hats and focal framing when center-cropped.
   - P2: title scale and bottom icons were visibly smaller than the source.
2. Fixes:
   - Replaced both portrait assets with wide card-specific images.
   - Increased title and mode typography.
   - Increased bottom icon size and reserved native Android navigation inset so icons are no longer compressed.
3. Final pass:
   - Both complete subjects are visible with matching negative space.
   - Text hierarchy and bottom icons are legible and proportionally aligned with the source.
   - Configurator and Products List card interactions both navigate successfully; no crash was logged.

## Follow-up polish

- P3: replace the spaced text `S Y D R A` with the official production wordmark asset when available.

final result: passed

## Catalog flow addendum — 2026-08-10

- Source visual truth: `sydra-ai-material-pack/references/screens-v1/07-商品列表-ACT分类（登录 – 8）.png`, `08-商品详情-ACT背心（登录 – 9）.png`, `09-配置器-步骤1-选择单品（登录 – 16）.png`, `12-配置器-步骤4-可完成（登录 – 20）.png`, `13-购物车-空状态（登录 – 10）.png`
- Implementation evidence: `design-qa/product-list-act-final.png`, `design-qa/product-detail-act-final.png`, `design-qa/configurator-step4-final.png`, and `design-qa/cart-final.png`.
- Emulator viewport: 1080 × 2400 px, Android density 420 dpi.
- Source viewport: 375 × 812 px; comparisons were normalized by width and treated native Android status/navigation bars as platform-owned chrome.
- States checked: Products List ACT, category rail switch to EVENT, Product Detail with S/M/L selection, cart with a standard product, Configurator steps 1–4, and cart with a completed configuration.

### Comparison and iteration history

1. Initial catalog pass: the product grid used two columns while the source uses three; this was recorded as P2 because it changed the primary browsing density.
2. Fix: changed the catalog grid to three columns and recaptured `design-qa/product-list-act-final.png`; the first product remains tappable and opens the detail screen.
3. Final interaction pass: verified category rail switching, product detail, add-to-cart, Configurator COMPLETE, and shared cart state on the emulator. No P0/P1/P2 issue remains.

### Required fidelity surfaces

- Typography: black uppercase hierarchy, compact metadata, and the source's editorial letter spacing are retained; Audi Type is still a P3 dependency because the licensed font file is not in the project.
- Spacing/layout: three-column product grid, left category rail, connector row, detail action bar, four configurator steps, and cart rows follow the source structure.
- Colors/tokens: black/white/gray surfaces and selected/unselected states match the reference direction.
- Image fidelity: supplied `act-vest` and `config-jacket` assets are used as real product images; no CSS-drawn product placeholders are used.
- Copy/content: product list, size choices, add-to-cart, COMPLETE, and empty/filled cart states are present.

Final catalog result: passed

## 个人中心交易下游 QA — 2026-08-10

- Source visual truth:
  - `sydra-ai-material-pack/references/screens-v1/15-订单列表-待付款（购买订单 – 6）.png`
  - `sydra-ai-material-pack/references/screens-v1/20-订单详情（购买订单 – 3）.png`
  - `sydra-ai-material-pack/references/screens-v1/21-售后详情（购买订单 – 5）.png`
  - `sydra-ai-material-pack/references/screens-v1/24-退货-填写问题并上传凭证（退款 – 2）.png`
  - `sydra-ai-material-pack/references/screens-v1/25-收货地址-地址列表（修改地址）.png`
  - `sydra-ai-material-pack/references/screens-v1/26-收货地址-新增地址（修改地址 – 1）.png`
  - `sydra-ai-material-pack/references/screens-v1/27-物流详情-运输轨迹（物流轨迹）.png`
- Implementation screenshots: `design-qa/commerce-order-list-final.png`, `commerce-order-detail-final.png`, `commerce-logistics-final.png`, `commerce-aftersale-detail-final.png`, `commerce-return-evidence-final.png`, `commerce-address-list-final.png`, and `commerce-address-form-final.png`.
- Side-by-side evidence: matching `design-qa/commerce-*-comparison.png` files.
- States: pending-payment list, order snapshot detail, populated logistics timeline, return evidence with a selected Photo Picker image, submitted after-sale detail, populated address list, and empty add-address form.
- Emulator viewport: 1080 × 2400 px at Android 420 dpi; native Compose content has no CSS size or browser device-scale factor.
- Source pixels: 375 × 812 px. Each implementation screenshot was proportionally normalized to 365 × 812 px and placed beside the native 375 × 812 source in one 756 × 812 comparison image. Android status/navigation chrome remains platform-owned and explains the 10 px normalized width difference.

### Full-view and focused comparison

The final comparisons preserve the source hierarchy: compact centered title bar, pale gray canvas, white rounded content groups, black SYDRA wordmark, red amount emphasis, order status tabs, product snapshot, logistics timeline, address cards/forms, and persistent bottom submission actions. The order-list comparison uses the same pending-payment state rather than an all-orders state.

Focused crops were not required because each combined artifact contains two native-width phone screens at readable 812 px height; CTA shape/safe area, product image, timeline nodes, form controls, and the selected evidence thumbnail are directly legible in the same comparison input.

### Required fidelity surfaces

- Fonts and typography: Chinese title hierarchy, compact metadata, uppercase product naming, bold black labels, muted supporting text, and red monetary emphasis follow the references. The project continues to use Android/system fallbacks where the licensed Audi Type font is unavailable; this remains P3.
- Spacing and layout rhythm: page margins, card grouping, order tabs, timeline indentation, form sections, and footer actions align with the source rhythm. Android safe-area padding is intentionally larger than the iOS Home Indicator region.
- Colors and visual tokens: `SydraInk`, `SydraMuted`, `SydraCanvas`, `SydraLine`, and `SydraAccent` centralize the black/white/gray/red system used across all seven screens.
- Image quality and asset fidelity: order cards/details use the project's real product bitmap and official wordmark asset. The selected return evidence is rendered from the actual Android Photo Picker URI as a cropped thumbnail; it is not a placeholder drawing.
- Copy and content: order numbers, statuses, prices, logistics nodes, after-sale fields, address labels, validation messages, and local-prototype disclaimers are app-specific and functional. Different sample names, order IDs, and address-card counts are accepted fixture differences rather than structural drift.

### Comparison history

1. Initial pass — blocked:
   - P2: return/address footer buttons overlapped the Android system-navigation area on the 1080 × 2400 emulator.
   - P2: Material pill CTAs drifted from the reference's sharp editorial black rectangles.
   - P2: a selected return credential was represented only by a text row, so the user could not visually confirm the chosen image.
2. Fixes:
   - Added `navigationBarsPadding()` to return and address bottom actions.
   - Changed primary and outlined commerce actions to sharp rectangular shapes.
   - Added background thumbnail decoding for Photo Picker content URIs and rendered each selected credential with a removable image preview.
3. Post-fix pass:
   - `commerce-address-list-comparison.png`, `commerce-address-form-comparison.png`, and `commerce-return-evidence-comparison.png` show safe, rectangular actions above native navigation.
   - The return comparison visibly contains the chosen image thumbnail and remove control.
   - No actionable P0/P1/P2 visual issue remains. Manual province/city/district text entry is an accepted local-data constraint until a real region dictionary/API is supplied.

### Primary interactions checked

- Profile → all/five-status orders → tab switch → order detail → back.
- Pending-receipt order → logistics → refresh/back.
- Return confirmation → reason → evidence → Android Photo Picker → selected thumbnail → submit → after-sale detail.
- Profile → after-sale list/detail.
- Profile → address list → add/edit → empty validation; footer buttons remain above system navigation.
- Cancel order → keep order / confirm cancellation → pending-payment empty state; Profile counts refresh.
- Top app-bar and Android system Back behavior were checked; no app crash or `FATAL EXCEPTION` was observed.

### Follow-up polish

- P3: replace system typography with the licensed production font when supplied.
- P3: replace manual region text fields with a service-backed Android region picker after the authoritative address dictionary is chosen.

final result: passed
