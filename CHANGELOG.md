# 更新日志

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
