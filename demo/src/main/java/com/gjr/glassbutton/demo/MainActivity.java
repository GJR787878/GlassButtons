package com.gjr.glassbutton.demo;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
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
 * GlassButtons 组件演示页。
 * 纯代码构建 UI，黑色背景（与 RamStatusBar 一致），展示三种组件效果。
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        float density = getResources().getDisplayMetrics().density;

        FrameLayout root = new FrameLayout(this);
        root.setBackgroundColor(0xFF000000);

        // 背景放一张渐变图，便于观察玻璃半透明穿透效果
        View bgGradient = new View(this);
        GradientDrawable bg = new GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                new int[]{0xFF1A237E, 0xFF4A148C, 0xFF004D40, 0xFF1A237E});
        bgGradient.setBackground(bg);
        bgGradient.setAlpha(0.6f);
        root.addView(bgGradient, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        ScrollView scrollView = new ScrollView(this);
        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(Math.round(32 * density), Math.round(40 * density),
                Math.round(32 * density), Math.round(140 * density));

        // 标题
        TextView title = new TextView(this);
        title.setText("GlassButtons 演示");
        title.setTextSize(22);
        title.setTextColor(0xFFFFFFFF);
        content.addView(title);

        TextView subtitle = new TextView(this);
        subtitle.setText("玻璃拟态胶囊按钮 · 单选按钮 · 底部导航");
        subtitle.setTextSize(13);
        subtitle.setTextColor(0xFFAAAAAA);
        subtitle.setPadding(0, Math.round(8 * density), 0, Math.round(32 * density));
        content.addView(subtitle);

        // === 1. 普通玻璃按钮 ===
        content.addView(makeSectionLabel("普通玻璃按钮", density));

        GlassCapsuleButton btnNormal = new GlassCapsuleButton(this);
        btnNormal.setText("默认状态（未选中）");
        content.addView(btnNormal, matchWidthWrapHeight(density));

        GlassCapsuleButton btnSelected = new GlassCapsuleButton(this);
        btnSelected.setText("选中状态（蓝字 + 粗描边）");
        btnSelected.setGlassSelected(true);
        content.addView(btnSelected, matchWidthWrapHeight(density));

        // 可切换按钮
        final GlassCapsuleButton btnToggle = new GlassCapsuleButton(this);
        btnToggle.setText("点我切换选中态");
        btnToggle.setOnClickListener(v -> btnToggle.setGlassSelected(!btnToggle.isGlassSelected()));
        content.addView(btnToggle, matchWidthWrapHeight(density));

        // === 2. 玻璃单选按钮 ===
        content.addView(makeSectionLabel("玻璃单选按钮（RadioGroup）", density));

        RadioGroup radioGroup = new RadioGroup(this);
        radioGroup.setOrientation(RadioGroup.VERTICAL);

        GlassRadioButton rb1 = new GlassRadioButton(this);
        rb1.setText("选项一：仅显示时间");
        GlassRadioButton rb2 = new GlassRadioButton(this);
        rb2.setText("选项二：时间 + 内存");
        GlassRadioButton rb3 = new GlassRadioButton(this);
        rb3.setText("选项三：仅显示内存");

        radioGroup.addView(rb1);
        radioGroup.addView(rb2);
        radioGroup.addView(rb3);
        radioGroup.check(rb2.getId());

        // 给单选按钮加间距
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            View child = radioGroup.getChildAt(i);
            RadioGroup.LayoutParams lp = (RadioGroup.LayoutParams) child.getLayoutParams();
            lp.topMargin = Math.round(12 * density);
            child.setLayoutParams(lp);
        }
        content.addView(radioGroup);

        // === 3. 底部导航栏 ===
        content.addView(makeSectionLabel("玻璃底部导航栏", density));
        TextView navHint = new TextView(this);
        navHint.setText("（导航栏在页面底部，点击切换选中项）");
        navHint.setTextSize(12);
        navHint.setTextColor(0xFF888888);
        navHint.setPadding(0, 0, 0, Math.round(16 * density));
        content.addView(navHint);

        scrollView.addView(content, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        root.addView(scrollView, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        // 底部导航
        GlassNavBar navBar = new GlassNavBar(this);
        navBar.addItem(makeIcon(0xFFFFFFFF, density), "主页");
        navBar.addItem(makeIcon(0xFFFFFFFF, density), "配置");
        navBar.addItem(makeIcon(0xFFFFFFFF, density), "设置");
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

    private TextView makeSectionLabel(String text, float density) {
        TextView label = new TextView(this);
        label.setText(text);
        label.setTextSize(15);
        label.setTextColor(0xFFFFFFFF);
        label.setPadding(0, Math.round(40 * density), 0, Math.round(12 * density));
        return label;
    }

    private LinearLayout.LayoutParams matchWidthWrapHeight(float density) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.topMargin = Math.round(12 * density);
        return lp;
    }

    /** 生成一个简单的圆形图标（演示用，实际项目请传入自己的矢量图标） */
    private android.graphics.drawable.Drawable makeIcon(int color, float density) {
        GradientDrawable d = new GradientDrawable();
        d.setShape(GradientDrawable.OVAL);
        d.setColor(color);
        d.setSize(Math.round(24 * density), Math.round(24 * density));
        return d;
    }
}
