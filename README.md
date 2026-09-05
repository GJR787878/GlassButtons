# GlassButtons · 玻璃拟态按钮组件库

从 [RamStatusBar](https://github.com/GJR787878/RamStatusBar) 提取的苹果风格毛玻璃按钮，封装为独立 Android 组件库，可直接在其他项目中调用。

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
**选中态**：填充加深（50%）+ 2dp 加粗加亮描边 + 蓝色文字 `#0A84FF`
**按压态**：填充轻微提亮

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

## 项目结构

```
GlassButtons/
├── glassbutton/          # 组件库模块
│   └── src/main/java/com/gjr/glassbutton/
│       ├── GlassButtonDrawable.java   # 玻璃背景 Drawable（核心）
│       ├── GlassButtonStyle.java      # 样式常量
│       ├── GlassCapsuleButton.java    # 胶囊按钮
│       ├── GlassRadioButton.java      # 单选按钮
│       └── GlassNavBar.java           # 底部导航
├── demo/               # 演示 App
│   └── src/main/java/com/gjr/glassbutton/demo/MainActivity.java
├── build.gradle
└── settings.gradle
```

## License

MIT
