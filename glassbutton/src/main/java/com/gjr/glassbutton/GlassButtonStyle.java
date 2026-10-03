package com.gjr.glassbutton;

import android.graphics.drawable.GradientDrawable;

/**
 * GlassButtons 玻璃拟态样式：常量与背景工厂。
 *
 * 所有颜色均可通过静态 setter 全局配置，适配不同项目主题。
 * 默认值提取自 RamStatusBar 项目（MainActivity / ColorSettingsActivity / TimeSettingsActivity）。
 */
public final class GlassButtonStyle {

    /** 选中态强调色（默认 iOS 蓝），可通过 {@link #setAccentColor(int)} 全局修改 */
    private static int sColorAccent = 0xFF0A84FF;

    /** 普通文字白色 */
    public static final int COLOR_WHITE = 0xFFFFFFFF;

    /** 玻璃底：半透明深灰（约 70% 不透明度），可通过 {@link #setNavBgColor(int)} 全局修改 */
    private static int sColorNavBg = 0xB31C1C1E;

    /** 默认细描边：25% 白 */
    public static final int COLOR_NAV_BORDER = 0x40FFFFFF;

    /** 选中态淡白底：18% 白（用于导航项等轻量高亮），可通过 {@link #setSelectedBgColor(int)} 全局修改 */
    private static int sColorTabSelectedBg = 0x2EFFFFFF;

    private GlassButtonStyle() {
    }

    // ==================== 全局主题配置 ====================

    /** 获取当前选中态强调色 */
    public static int getAccentColor() { return sColorAccent; }

    /** 全局设置选中态强调色（文字、图标、描边高亮），影响所有组件 */
    public static void setAccentColor(int color) { sColorAccent = color; }

    /** 获取当前玻璃底色 */
    public static int getNavBgColor() { return sColorNavBg; }

    /** 全局设置玻璃底色（半透明填充），影响所有组件 */
    public static void setNavBgColor(int color) { sColorNavBg = color; }

    /** 获取当前选中态高亮底色 */
    public static int getSelectedBgColor() { return sColorTabSelectedBg; }

    /** 全局设置选中态高亮底色（导航项等轻量高亮） */
    public static void setSelectedBgColor(int color) { sColorTabSelectedBg = color; }

    /**
     * 创建玻璃胶囊按钮背景。
     *
     * @param density        屏幕密度（{@code context.getResources().getDisplayMetrics().density}）
     * @param cornerRadiusDp 圆角半径，单位 dp（原项目为 28dp）
     * @param selected       是否选中态：选中 = 2dp 纯白描边，否则 1dp 淡白描边
     */
    public static GradientDrawable createGlassBackground(float density, float cornerRadiusDp, boolean selected) {
        GradientDrawable bg = new GradientDrawable();
        bg.setColor(sColorNavBg);
        bg.setCornerRadius(Math.round(cornerRadiusDp * density));
        if (selected) {
            bg.setStroke(Math.round(2 * density), COLOR_WHITE);
        } else {
            bg.setStroke(Math.round(1 * density), COLOR_NAV_BORDER);
        }
        return bg;
    }

    /**
     * 创建选中态淡白高亮背景（导航项等），全圆角。
     */
    public static GradientDrawable createSelectedHighlight() {
        GradientDrawable bg = new GradientDrawable();
        bg.setColor(sColorTabSelectedBg);
        bg.setCornerRadius(1000f);
        return bg;
    }
}
