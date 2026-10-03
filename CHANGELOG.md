# 更新日志

## v1.0 — 2026-10-03

### 修复
- **Demo 导航二页面**："当前版本"和"检查更新"按钮原使用独立 `Gravity.BOTTOM` 固定浮层，不随内容滚动且与底部 `GlassNavBar` 重叠；已移入 `ScrollView` 内容末尾，随页面正常滚动
- **Demo 导航二底部 padding**：从 210dp 改回 112dp（不再需要为固定浮层预留空间）

### Demo 增强
- 导航一：弹窗选择、语言切换、输入框、透明度调节、颜色选择、跳转第二屏
- 导航二：20 组带文案开关 + 版本信息/检查更新
- 导航三：10 组描述行 + 整行开关（仅显示开/关）
- 多语言支持（中/英/俄），语言切换后 `recreate()` 真正重渲染
- 深色弹窗（单选、文本输入）


## v1.0.0 — 2026-09-05

### 新增
- **GlassCapsuleButton**：玻璃拟态胶囊按钮，支持 `setGlassSelected()` 切换选中态
- **GlassRadioButton**：玻璃单选按钮，去掉原生圆圈，兼容 RadioGroup
- **GlassNavBar**：玻璃底部导航栏，支持图标+文字、选中高亮、点击回调
- **GlassButtonDrawable**：五层叠加玻璃效果（半透明填充 + 顶部高光 + 底部内阴影 + 边缘亮线 + 渐变描边）
- **GlassButtonStyle**：样式常量与工具方法
- **Demo App**：密集文字背景可滑动界面，直观展示半透明穿透效果
- **GitHub Actions**：push 自动编译 + 签名，产出可安装 APK

### 特性
- 纯 Java + Android framework 实现，无 androidx、无第三方依赖
- 支持 XML 布局与纯代码两种调用方式
- 半透明填充（未选中 20% / 选中 35%），底部内容可穿透可见
- 选中态：蓝色文字 `#0A84FF` + 2dp 加粗加亮描边
- 按压态：填充轻微提亮
- minSdk 26，compileSdk 34，Java 17

### 来源
- 按钮风格提取自 [RamStatusBar](https://github.com/GJR787878/RamStatusBar) 项目的 MainActivity / ColorSettingsActivity / TimeSettingsActivity
