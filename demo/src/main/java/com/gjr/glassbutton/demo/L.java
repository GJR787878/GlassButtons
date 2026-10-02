package com.gjr.glassbutton.demo;

import android.content.Context;

import java.util.HashMap;

/**
 * 纯代码多语言支持（无 strings.xml 也能真正切换界面语言）。
 *
 * 用法：
 *   L.t(ctx, "key")            取当前语言下的文案
 *   L.optName(ctx,n) 等         取带序号的组合文案
 * 切换语言：把 t1_lang 写入 SharedPreferences 后调用 Activity.recreate()，
 * 整个界面会按新语言重建——这才是"真正生效"的语言切换。
 */
public final class L {

    public static final int ZH = 0, EN = 1, TW = 2;
    private static final String PREFS = "demo_prefs";

    private L() {}

    public static int idx(Context c) {
        return c.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getInt("t1_lang", 0);
    }

    private static final HashMap<String, String[]> M = new HashMap<>();

    private static void p(String k, String zh, String en, String tw) {
        M.put(k, new String[]{zh, en, tw});
    }

    static {
        // 通用
        p("on", "开", "On", "開");
        p("off", "关", "Off", "關");

        // 导航
        p("nav1", "导航一", "Tab 1", "導航一");
        p("nav2", "导航二", "Tab 2", "導航二");
        p("nav3", "导航三", "Tab 3", "導航三");

        // 导航一各 label
        p("l1", "选项一：开关", "Option 1: Switch", "選項一：開關");
        p("l2", "选项二：开关", "Option 2: Switch", "選項二：開關");
        p("l3", "选项三：开关", "Option 3: Switch", "選項三：開關");
        p("l4", "选项四：弹窗五选项", "Option 4: Dialog (5 choices)", "選項四：彈窗五選項");
        p("l5", "选项五：全屏二级界面", "Option 5: Full-screen page", "選項五：全屏二級界面");
        p("l6", "选项六：点击循环切换语言", "Option 6: Tap to cycle language", "選項六：點擊循環切換語言");
        p("l7", "选项七：页面内互斥单选组", "Option 7: In-page radio group", "選項七：頁面內互斥單選組");
        p("l8", "选项八：透明度滑杆（0-100）", "Option 8: Opacity slider (0-100)", "選項八：透明度滑桿（0-100）");
        p("l9", "选项九：点击输入文本", "Option 9: Tap to enter text", "選項九：點擊輸入文字");
        p("l10", "选项十：颜色选择器", "Option 10: Color picker", "選項十：顏色選擇器");

        // 弹窗
        p("dlg_title", "请选择一个选项", "Choose an option", "請選擇一個選項");
        p("done", "完成", "Done", "完成");
        p("cancel", "取消", "Cancel", "取消");
        p("ok", "确认", "OK", "確認");

        // 二级界面
        p("open_second", "打开全屏二级界面", "Open full-screen page", "打開全屏二級界面");
        p("second_title", "全屏二级界面", "Full-screen Page", "全屏二級界面");
        p("second_sub", "请从下面三个选项中选择一个：", "Choose one of the three options:", "請從下面三個選項中選擇一個：");
        p("back", "返回", "Back", "返回");

        // 语言按钮
        p("cur_lang", "当前语言：", "Language: ", "當前語言：");
        p("langname", "简体中文", "English", "繁體中文");

        // 透明度
        p("cur_opacity", "当前透明度：", "Opacity: ", "當前透明度：");
        p("opacity_preview", "透明度预览", "Opacity preview", "透明度預覽");

        // 文本输入
        p("input_title", "请输入文本", "Enter text", "請輸入文字");
        p("input_hint", "输入胶囊要显示的文字", "Text shown on the capsule", "輸入膠囊要顯示的文字");
        p("tap_input", "点击输入文本", "Tap to enter text", "點擊輸入文字");

        // 颜色
        p("colorname", "红色", "Red", "紅色");

        // 导航二
        p("cur_version", "当前版本：v", "Version: v", "當前版本：v");
        p("check_update", "检查更新", "Check update", "檢查更新");
        p("latest", "已是最新版本", "Already up to date", "已是最新版本");

        // 导航三
        p("tab3_title", "导航三：描述 + 开关", "Tab 3: Description + Switch", "導航三：描述 + 開關");
    }

    public static String t(Context c, String k) {
        String[] s = M.get(k);
        return s == null ? k : s[idx(c)];
    }

    public static String langName(int i) {
        return M.get("langname")[i];
    }

    public static String colorName(int i) {
        return M.get("colorname")[i];
    }

    // ---------------- 带序号的组合文案 ----------------

    public static String optName(Context c, int n) {
        switch (idx(c)) {
            case EN: return "Option " + n;
            case TW: return "選項" + cn(n);
            default: return "选项" + cn(n);
        }
    }

    public static String swName(Context c, int n) {
        switch (idx(c)) {
            case EN: return "Switch " + n;
            default: return (idx(c) == TW ? "開關" : "开关") + cn(n);
        }
    }

    public static String secondOpt(Context c, int n) {
        switch (idx(c)) {
            case EN: return "Page option " + n;
            case TW: return "二級選項" + cn(n);
            default: return "二级选项" + cn(n);
        }
    }

    public static String radioOpt(Context c, int n) {
        switch (idx(c)) {
            case EN: return "Radio option " + n;
            case TW: return "單選項" + cn(n);
            default: return "单选项" + cn(n);
        }
    }

    public static String dialogOpt(Context c, int n) {
        switch (idx(c)) {
            case EN: return "Option " + n;
            case TW: return "彈窗選項" + cn(n);
            default: return "弹窗选项" + cn(n);
        }
    }

    /** 导航三：每个开关的二十字描述（描述行）。 */
    public static String desc3(Context c, int n) {
        switch (idx(c)) {
            case EN: return "This is the twenty character description text for switch " + n;
            case TW: return "這是第" + cn(n) + "個開關的二十字描述文字示例";
            default: return "这是第" + cn(n) + "个开关的二十字描述文字示例";
        }
    }

    static String cn(int n) {
        String[] d = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        if (n < 10) return d[n];
        if (n == 10) return "十";
        if (n < 20) return "十" + d[n - 10];
        if (n == 20) return "二十";
        return String.valueOf(n);
    }
}
