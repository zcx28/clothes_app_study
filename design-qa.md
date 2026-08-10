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
