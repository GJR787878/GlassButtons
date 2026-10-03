package com.gjr.glassbutton;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

/**
 * GlassButtons 通用弹窗组件。
 *
 * 自动根据选项数量选择展示模式：
 *   - 选项数 ≤ threshold（默认 5）：居中弹窗，宽度 88% 屏宽
 *   - 选项数 > threshold：全屏二级界面，内部 ScrollView 可滚动
 *
 * 用法：
 *   GlassDialog.showChoice(context, "请选择", items, selectedIndex, (index, item) -> {
 *       // 处理选择
 *   });
 *
 *   // 自定义阈值（超过 8 个才全屏）：
 *   GlassDialog.showChoice(context, "请选择", items, selectedIndex, 8, callback);
 */
public final class GlassDialog {

    /** 默认阈值：超过 5 个选项切换全屏 */
    public static final int DEFAULT_THRESHOLD = 5;

    public interface OnChoiceListener {
        void onChoice(int index, String item);
    }

    private GlassDialog() {}

    /**
     * 显示选择弹窗（默认阈值 5）。
     */
    public static Dialog showChoice(Context ctx, String title, List<String> items,
                                    int selectedIndex, OnChoiceListener listener) {
        return showChoice(ctx, title, items, selectedIndex, DEFAULT_THRESHOLD, listener);
    }

    /**
     * 显示选择弹窗，自定义全屏阈值。
     *
     * @param threshold 选项数超过此值则用全屏二级界面，否则用居中弹窗
     */
    public static Dialog showChoice(Context ctx, String title, List<String> items,
                                    int selectedIndex, int threshold,
                                    OnChoiceListener listener) {
        boolean fullscreen = items.size() > threshold;
        Dialog dlg = new Dialog(ctx, android.R.style.Theme_Translucent_NoTitleBar);

        // 内容容器
        LinearLayout box = new LinearLayout(ctx);
        box.setOrientation(LinearLayout.VERTICAL);

        // 标题
        TextView titleView = new TextView(ctx);
        titleView.setText(title);
        titleView.setTextColor(0xFFFFFFFF);
        titleView.setTextSize(18);
        titleView.setPadding(0, 0, 0, dp(ctx, 14));
        box.addView(titleView);

        // 选项列表（ScrollView 包裹，两种模式都支持滚动）
        ScrollView sv = new ScrollView(ctx);
        LinearLayout options = new LinearLayout(ctx);
        options.setOrientation(LinearLayout.VERTICAL);
        float density = ctx.getResources().getDisplayMetrics().density;

        for (int i = 0; i < items.size(); i++) {
            GlassCapsuleButton btn = new GlassCapsuleButton(ctx);
            btn.setText(items.get(i));
            btn.setGlassSelected(i == selectedIndex);
            final int idx = i;
            btn.setOnClickListener(v -> {
                if (listener != null) listener.onChoice(idx, items.get(idx));
                dlg.dismiss();
            });
            LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            bp.bottomMargin = Math.round(10 * density);
            options.addView(btn, bp);
        }
        sv.addView(options, new ScrollView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        box.addView(sv);

        // 外层圆角容器
        FrameLayout wrapper = new FrameLayout(ctx);
        GradientDrawable bg = new GradientDrawable();
        bg.setColor(0xFF1C1C1E);
        bg.setCornerRadius(dp(ctx, 20));
        wrapper.setBackground(bg);
        wrapper.setPadding(dp(ctx, 20), dp(ctx, 20), dp(ctx, 20), dp(ctx, 20));
        wrapper.addView(box, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        dlg.setContentView(wrapper);
        android.view.Window w = dlg.getWindow();
        w.setBackgroundDrawable(new ColorDrawable(0x00000000));
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.dimAmount = 0.6f;
        lp.x = 0;
        lp.y = 0;

        if (fullscreen) {
            // 全屏二级界面
            w.setGravity(Gravity.CENTER);
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            lp.dimAmount = 0.85f;
            // 全屏模式 wrapper 铺满，内容垂直居中偏上
            wrapper.setPadding(dp(ctx, 24), dp(ctx, 48), dp(ctx, 24), dp(ctx, 48));
        } else {
            // 居中弹窗
            w.setGravity(Gravity.CENTER);
            lp.width = (int) (ctx.getResources().getDisplayMetrics().widthPixels * 0.88f);
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        }
        w.setAttributes(lp);
        dlg.show();
        return dlg;
    }

    private static int dp(Context ctx, float v) {
        return Math.round(v * ctx.getResources().getDisplayMetrics().density);
    }
}
