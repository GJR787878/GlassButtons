# GlassButtons · 玻璃拟态按钮组件库

从 [RamStatusBar](https://github.com/GJR787878/RamStatusBar) 提取的苹果风格毛玻璃按钮，封装为独立 Android 组件库，可直接在其他项目中调用。

> 📦 **最新 Demo APK 下载**：[v1.0.0 demo-release.apk](https://github.com/GJR787878/GlassButtons/releases/download/v1.0.0/demo-release.apk)
> 安装后滑动页面，观察半透明按钮下方的文字穿透效果。

## 视觉效果

参考 iOS 26 Liquid Glass 与深色玻璃按钮设计，由五层叠加实现玻璃质感：

| 层级 | 效果 |
|------|------|
| 1 | 半透明深色填充（玻璃底色，约 35% 不透明度） |
| 2 | 顶部白色高光渐变（玻璃顶面反光） |
| 3 | 底部黑色内阴影（玻璃厚度感） |
| 4 | 顶部细亮线（玻璃边缘反光） |
| 5 | 上亮下暗渐变描边（玻璃边缘折射） |

**默认态**：通透玻璃底 + 1dp 细描边 + 白色文字
**选中态**：填充加深（35%）+ 2dp 加粗加亮描边 + 蓝色文字 `#0A84FF`
**按压态**：填充轻微提亮

## 样式自定义

所有视觉参数集中在 `GlassButtonDrawable.java` 顶部的常量中，可直接修改：

| 常量 | 默认值 | 说明 |
|------|--------|------|
| `FILL_NORMAL` | `0x331C1C1E` | 未选中填充色（前两位=不透明度，33≈20%） |
| `FILL_SELECTED` | `0x591C1C1E` | 选中填充色（59≈35%） |
| `FILL_PRESSED` | `0x4C1C1C1E` | 按压填充色 |
| `HIGHLIGHT_TOP` | `0x4DFFFFFF` | 顶部高光起始色（4D≈30%白） |
| `SHADOW_BOTTOM` | `0x40000000` | 底部内阴影结束色 |
| `TOP_GLINT` | `0x66FFFFFF` | 顶部边缘亮线 |

> 不透明度速查：`00`=0%，`33`=20%，`59`=35%，`80`=50%，`B3`=70%，`FF`=100%

选中态文字色在 `GlassButtonStyle.COLOR_ACCENT`（默认 `0xFF0A84FF` iOS 蓝），修改此处可全局换色。

## 组件列表

| 组件 | 说明 |
|------|------|
| `GlassCapsuleButton` | 玻璃胶囊按钮，支持 `setGlassSelected()` 切换选中态 |
| `GlassRadioButton` | 玻璃胶囊单选按钮，去掉原生圆圈，兼容 RadioGroup |
| `GlassNavBar` | 玻璃底部导航栏，支持图标+文字、选中高亮、点击回调 |
| `GlassButtonDrawable` | 底层玻璃背景 Drawable，可直接套用到任意 View |
| `GlassButtonStyle` | 样式常量与工具方法 |

## 快速使用

### 方式一：作为 Library 模块依赖

1. 将 `glassbutton/` 目录拷贝到你的项目根目录
2. 在 `settings.gradle` 中添加：
   ```groovy
   include ':glassbutton'
   ```
3. 在 app 模块的 `build.gradle` 中添加：
   ```groovy
   dependencies {
       implementation project(':glassbutton')
   }
   ```

### 方式二：单文件拷贝（零依赖）

所有组件均为纯 Java + Android framework 实现，不依赖 androidx。将以下文件直接拷贝到你的项目即可：

- `glassbutton/src/main/java/com/gjr/glassbutton/GlassButtonDrawable.java`
- `glassbutton/src/main/java/com/gjr/glassbutton/GlassButtonStyle.java`
- `glassbutton/src/main/java/com/gjr/glassbutton/GlassCapsuleButton.java`
- `glassbutton/src/main/java/com/gjr/glassbutton/GlassRadioButton.java`
- `glassbutton/src/main/java/com/gjr/glassbutton/GlassNavBar.java`

> 如果使用 XML 布局调用，还需拷贝 `res/values/attrs.xml`。纯代码调用无需任何资源文件。

## 代码示例

### 玻璃胶囊按钮

```java
GlassCapsuleButton btn = new GlassCapsuleButton(context);
btn.setText("选择背景颜色");
btn.setGlassSelected(false);  // 默认态
btn.setOnClickListener(v -> {
    btn.setGlassSelected(!btn.isGlassSelected());
});
```

### 玻璃单选按钮（RadioGroup）

```java
RadioGroup group = new RadioGroup(context);
group.setOrientation(RadioGroup.VERTICAL);

GlassRadioButton rb1 = new GlassRadioButton(context);
rb1.setText("仅显示时间");
GlassRadioButton rb2 = new GlassRadioButton(context);
rb2.setText("时间 + 内存");

group.addView(rb1);
group.addView(rb2);
group.check(rb1.getId());
```

### 玻璃底部导航栏

```java
GlassNavBar nav = new GlassNavBar(context);
nav.addItem(getDrawable(R.drawable.ic_home), "主页");
nav.addItem(getDrawable(R.drawable.ic_config), "配置");
nav.addItem(getDrawable(R.drawable.ic_settings), "设置");
nav.setSelected(0);
nav.setOnItemSelectedListener(index -> {
    // 切换页面
});
```

### XML 布局

```xml
<com.gjr.glassbutton.GlassCapsuleButton
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="按钮"
    app:glassSelected="false"
    app:glassCornerRadius="28dp" />
```

## 自定义属性

| 属性 | 组件 | 说明 | 默认值 |
|------|------|------|--------|
| `glassSelected` | GlassCapsuleButton | 是否选中态 | false |
| `glassCornerRadius` | 全部 | 圆角半径（dp） | 28dp |
| `glassHeight` | GlassNavBar | 导航栏高度（dp） | 76dp |

## 环境要求

- `minSdk` 26（Android 8.0）
- `compileSdk` 34
- Java 17
- 无第三方依赖，纯 Android framework 实现

## 从 RamStatusBar 迁移

原项目中每个 Activity 都重复定义了 `createGlassButtonBg()` 和 `updateModeButtonStyles()`，迁移到本组件库后：

```java
// 原来（每个 Activity 重复 30+ 行）
GradientDrawable bg = new GradientDrawable();
bg.setColor(0xB31C1C1E);
bg.setCornerRadius(Math.round(28 * density));
bg.setStroke(Math.round(1 * density), 0x40FFFFFF);
button.setBackground(bg);

// 现在（一行搞定，自动带五层玻璃效果）
GlassCapsuleButton button = new GlassCapsuleButton(context);
```

单选按钮同理，用 `GlassRadioButton` 替换 `RadioButton` + `setButtonDrawable(null)` + 手动设置背景的组合。

## 常见问题

**Q: 按钮背景不够透明，看不到底部文字？**
A: 降低 `FILL_NORMAL` 的不透明度，比如从 `0x33` 改成 `0x1A`（10%）。

**Q: 能在 XML 里用吗？**
A: 可以，需将 `attrs.xml` 一并拷贝到项目的 `res/values/` 目录。纯代码调用无需任何资源。

**Q: 支持 androidx 项目吗？**
A: 支持。组件继承自 `android.widget.Button` / `RadioButton`，不依赖 androidx，可在任何 Android 项目中使用。

**Q: minSdk 能降到更低吗？**
A: 组件本身用到的 API（`GradientDrawable`、`LinearGradient`、`Canvas.clipPath`）在 API 16+ 即可用，当前设为 26 是与 RamStatusBar 保持一致，可按需调低。

## 项目结构

```
GlassButtons/
├── glassbutton/              # 组件库模块（拷到其他项目即可用）
│   ├── build.gradle
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/com/gjr/glassbutton/
│       │   ├── GlassButtonDrawable.java   # 玻璃背景 Drawable（核心，五层叠加）
│       │   ├── GlassButtonStyle.java      # 样式常量与工具方法
│       │   ├── GlassCapsuleButton.java    # 玻璃胶囊按钮
│       │   ├── GlassRadioButton.java      # 玻璃单选按钮
│       │   └── GlassNavBar.java           # 玻璃底部导航栏
│       └── res/values/attrs.xml           # XML 自定义属性
├── demo/                     # 演示 App（可独立编译安装）
│   ├── build.gradle
│   └── src/main/
│       ├── AndroidManifest.xml
│       └── java/com/gjr/glassbutton/demo/MainActivity.java
├── .github/workflows/build-demo.yml       # GitHub Actions 自动编译+签名
├── build.gradle
├── settings.gradle
├── gradle.properties
├── gradlew / gradlew.bat / gradle/wrapper/
└── README.md
```

## 更新日志

见 [CHANGELOG.md](CHANGELOG.md)。

## License

MIT
