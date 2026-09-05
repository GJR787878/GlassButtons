package com.gjr.glassbutton.demo;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gjr.glassbutton.GlassCapsuleButton;
import com.gjr.glassbutton.GlassNavBar;
import com.gjr.glassbutton.GlassRadioButton;

/**
 * GlassButtons 半透明效果检查页。
 *
 * 结构：
 *   ScrollView
 *     └── FrameLayout（可滚动内容）
 *           ├── 底层：100 行密集文字（铺满整个背景）
 *           └── 上层：玻璃按钮组（浮在文字上方，半透明可穿透）
 *   底部：固定玻璃导航栏
 *
 * 滑动页面时，按钮随文字一起滚动，透过半透明按钮可以清晰看到下方的文字，
 * 以此检查玻璃拟态的半透明穿透效果。
 */
public class MainActivity extends Activity {

    private static final String[] SAMPLE_TEXTS = {
            "玻璃拟态按钮半透明穿透效果测试 —— 透过按钮可见底部文字",
            "GlassButtons Frosted Glass Transparency Demo",
            "半透明填充约 20% 不透明度，底部内容清晰可见",
            "顶部白色高光渐变模拟玻璃顶面反光",
            "底部黑色内阴影模拟玻璃厚度感",
            "上亮下暗渐变描边模拟玻璃边缘折射",
            "选中态：蓝色文字 0xFF0A84FF + 2dp 加粗加亮描边",
            "未选中态：白色文字 + 1dp 细描边",
            "按压态：填充轻微提亮保留反馈",
            "从 RamStatusBar 项目提取的按钮风格组件库",
            "纯 Java 实现，无 androidx，无第三方依赖",
            "minSdk 26，compileSdk 34，Java 17",
            "支持 XML 布局与纯代码两种调用方式",
            "包含 GlassCapsuleButton / GlassRadioButton / GlassNavBar",
            "滑动页面观察按钮下方文字的穿透效果",
    };

    private static final int[] TEXT_COLORS = {
            0xFFCCCCCC, 0xFF999999, 0xFFBBBBBB, 0xFF777777,
            0xFFAAAAAA, 0xFF888888,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        float density = getResources().getDisplayMetrics().density;

        FrameLayout root = new FrameLayout(this);
        root.setBackgroundColor(0xFF000000);

        ScrollView scrollView = new ScrollView(this);
        FrameLayout content = new FrameLayout(this);

        // ===== 底层：密集文字背景（100 行） =====
        LinearLayout textLayer = new LinearLayout(this);
        textLayer.setOrientation(LinearLayout.VERTICAL);
        textLayer.setPadding(Math.round(16 * density), Math.round(12 * density),
                Math.round(16 * density), Math.round(12 * density));

        for (int i = 0; i < 100; i++) {
            TextView tv = new TextView(this);
            String text = String.format("%03d  %s", i + 1, SAMPLE_TEXTS[i % SAMPLE_TEXTS.length]);
            tv.setText(text);
            // 每隔几行放大字号，让按钮下的文字更醒目
            if (i % 7 == 3) {
                tv.setTextSize(16);
                tv.setTypeface(null, Typeface.BOLD);
                tv.setTextColor(0xFFE0E0E0);
            } else {
                tv.setTextSize(13);
                tv.setTextColor(TEXT_COLORS[i % TEXT_COLORS.length]);
            }
            tv.setPadding(0, Math.round(5 * density), 0, Math.round(5 * density));
            textLayer.addView(tv);
        }

        content.addView(textLayer, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        // ===== 上层：玻璃按钮组（浮在文字上方） =====
        LinearLayout buttonLayer = new LinearLayout(this);
        buttonLayer.setOrientation(LinearLayout.VERTICAL);
        buttonLayer.setPadding(Math.round(28 * density), 0,
                Math.round(28 * density), Math.round(120 * density));

        FrameLayout.LayoutParams buttonLayerParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        // 把按钮组推到文字中间，正好盖在多行文字上
        buttonLayerParams.topMargin = Math.round(300 * density);
        content.addView(buttonLayer, buttonLayerParams);

        // 标题
        TextView title = new TextView(this);
        title.setText("GlassButtons 效果检查");
        title.setTextSize(24);
        title.setTextColor(0xFFFFFFFF);
        title.setTypeface(null, Typeface.BOLD);
        title.setPadding(0, 0, 0, Math.round(4 * density));
        buttonLayer.addView(title);

        TextView subtitle = new TextView(this);
        subtitle.setText("滑动页面，观察半透明按钮下方的文字穿透");
        subtitle.setTextSize(13);
        subtitle.setTextColor(0xFFDDDDDD);
        subtitle.setPadding(0, 0, 0, Math.round(28 * density));
        buttonLayer.addView(subtitle);

        // --- 普通玻璃按钮 ---
        addSectionLabel(buttonLayer, "▎普通玻璃按钮", density);

        GlassCapsuleButton btnNormal = new GlassCapsuleButton(this);
        btnNormal.setText("默认状态（未选中）—— 底部文字清晰可见");
        buttonLayer.addView(btnNormal, matchWidth(density));

        GlassCapsuleButton btnSelected = new GlassCapsuleButton(this);
        btnSelected.setText("选中状态（蓝字 + 粗描边）");
        btnSelected.setGlassSelected(true);
        buttonLayer.addView(btnSelected, matchWidth(density));

        final GlassCapsuleButton btnToggle = new GlassCapsuleButton(this);
        btnToggle.setText("点我切换选中态 / 未选中态");
        btnToggle.setOnClickListener(v ->
                btnToggle.setGlassSelected(!btnToggle.isGlassSelected()));
        buttonLayer.addView(btnToggle, matchWidth(density));

        // --- 玻璃单选按钮 ---
        addSectionLabel(buttonLayer, "▎玻璃单选按钮（RadioGroup）", density);

        RadioGroup radioGroup = new RadioGroup(this);
        radioGroup.setOrientation(RadioGroup.VERTICAL);

        GlassRadioButton rb1 = new GlassRadioButton(this);
        rb1.setText("选项一：仅显示时间");
        GlassRadioButton rb2 = new GlassRadioButton(this);
        rb2.setText("选项二：时间 + 内存（如 21:11 2.5G/8G）");
        GlassRadioButton rb3 = new GlassRadioButton(this);
        rb3.setText("选项三：仅显示内存（如 2.5G/8G）");

        radioGroup.addView(rb1);
        radioGroup.addView(rb2);
        radioGroup.addView(rb3);
        radioGroup.check(rb2.getId());

        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            View child = radioGroup.getChildAt(i);
            RadioGroup.LayoutParams lp = (RadioGroup.LayoutParams) child.getLayoutParams();
            lp.topMargin = Math.round(12 * density);
            child.setLayoutParams(lp);
        }
        buttonLayer.addView(radioGroup);

        // --- 更多浮动按钮，滚动时依次经过不同文字 ---
        addSectionLabel(buttonLayer, "▎浮动玻璃按钮（滚动观察穿透）", density);

        for (int i = 0; i < 6; i++) {
            GlassCapsuleButton btn = new GlassCapsuleButton(this);
            btn.setText("浮动玻璃按钮 " + (i + 1) + " —— 滑动看下方文字");
            if (i % 2 == 1) {
                btn.setGlassSelected(true);
            }
            buttonLayer.addView(btn, matchWidth(density));
        }

        // 底部提示
        TextView endHint = new TextView(this);
        endHint.setText("—— 已到底部，向上滑动回顾 ——");
        endHint.setTextSize(13);
        endHint.setTextColor(0xFF888888);
        endHint.setGravity(Gravity.CENTER);
        endHint.setPadding(0, Math.round(40 * density), 0, 0);
        buttonLayer.addView(endHint);

        scrollView.addView(content, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        root.addView(scrollView, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        // ===== 底部固定玻璃导航栏 =====
        GlassNavBar navBar = new GlassNavBar(this);
        navBar.addItem(makeCircleIcon(0xFFFFFFFF, density), "主页");
        navBar.addItem(makeCircleIcon(0xFFFFFFFF, density), "配置");
        navBar.addItem(makeCircleIcon(0xFFFFFFFF, density), "设置");
        navBar.setSelected(0);

        FrameLayout.LayoutParams navParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        navParams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        navParams.leftMargin = Math.round(24 * density);
        navParams.rightMargin = Math.round(24 * density);
        navParams.bottomMargin = Math.round(24 * density);
        root.addView(navBar, navParams);

        setContentView(root);
    }

    private void addSectionLabel(LinearLayout parent, String text, float density) {
        TextView label = new TextView(this);
        label.setText(text);
        label.setTextSize(15);
        label.setTextColor(0xFFFFFFFF);
        label.setTypeface(null, Typeface.BOLD);
        label.setPadding(0, Math.round(36 * density), 0, Math.round(12 * density));
        parent.addView(label);
    }

    private LinearLayout.LayoutParams matchWidth(float density) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.topMargin = Math.round(12 * density);
        return lp;
    }

    /** 生成简单圆形图标（演示用，实际项目请传入矢量图标） */
    private android.graphics.drawable.Drawable makeCircleIcon(int color, float density) {
        android.graphics.drawable.GradientDrawable d = new android.graphics.drawable.GradientDrawable();
        d.setShape(android.graphics.drawable.GradientDrawable.OVAL);
        d.setColor(color);
        d.setSize(Math.round(24 * density), Math.round(24 * density));
        return d;
    }
}
