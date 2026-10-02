package com.gjr.glassbutton.demo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gjr.glassbutton.GlassCapsuleButton;
import com.gjr.glassbutton.GlassNavBar;
import com.gjr.glassbutton.GlassRadioButton;

import java.util.HashMap;

/**
 * GlassButtons 综合演示（纯 Java、无 XML 布局、无第三方依赖）。
 *
 * 结构：
 *   FrameLayout root（纯黑）
 *   ├── 三个全屏 ScrollView（导航一/二/三），full-bleed 铺到悬浮导航背后
 *   ├── 导航二专用的固定底部条（版本号 + 检查更新）
 *   └── GlassNavBar 悬浮：手机底部横排；平板（sw600）左侧竖排，内容 left padding 避让
 *
 * 所有选项状态写入 SharedPreferences：退出重开、旋转屏幕均保留。
 * 弹窗统一深色主题。
 */
public class MainActivity extends Activity {

    private static final String PREFS = "demo_prefs";

    private SharedPreferences sp;
    private FrameLayout root;
    private ScrollView tab1, tab2, tab3;
    private LinearLayout tab2Footer;
    private GlassNavBar nav;
    private boolean tablet;

    // 通用整行开关：key -> 胶囊（base 文案存在 tag 里）
    private final HashMap<String, GlassCapsuleButton> switchBtns = new HashMap<>();

    // 导航一需要回读刷新的控件
    private GlassCapsuleButton btnDialog, btnLang, btnInput;
    private RadioGroup dialogGroup, pageRadioGroup;
    private TextView opacityValue;
    private SeekBar opacitySeek;
    private GlassCapsuleButton opacityPreview;
    private final View[] colorSwatches = new View[3];

    // 导航三的行与小开关
    private final GlassCapsuleButton[] tab3Indicators = new GlassCapsuleButton[10];

    private static final String[] LANGS = {"简体中文", "English", "繁體中文"};
    private static final int[] COLORS = {0xFFFF3B30, 0xFF34C759, 0xFF0A84FF};
    // 正好 20 个汉字的描述
    private static final String DESC20 = "开关描述这是一段整整二十个汉字的测试文字";

    private int dp(float v) {
        return Math.round(v * getResources().getDisplayMetrics().density);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences(PREFS, MODE_PRIVATE);
        tablet = getResources().getConfiguration().smallestScreenWidthDp >= 600;

        root = new FrameLayout(this);
        root.setBackgroundColor(0xFF000000);

        tab1 = makeScroll(buildTab1(), 112);
        tab2 = makeScroll(buildTab2(), 210);
        tab3 = makeScroll(buildTab3(), 112);

        root.addView(tab1, new FrameLayout.LayoutParams(MATCH(), MATCH()));
        root.addView(tab2, new FrameLayout.LayoutParams(MATCH(), MATCH()));
        root.addView(tab3, new FrameLayout.LayoutParams(MATCH(), MATCH()));
        tab2.setVisibility(View.GONE);
        tab3.setVisibility(View.GONE);

        buildTab2Footer();
        root.addView(tab2Footer);

        buildNav();
        root.addView(nav, navParams());

        setContentView(root);
        loadAll();
    }

    private int MATCH() { return ViewGroup.LayoutParams.MATCH_PARENT; }
    private int WRAP() { return ViewGroup.LayoutParams.WRAP_CONTENT; }

    // ------------------------------------------------------------------
    // 通用构造
    // ------------------------------------------------------------------

    private LinearLayout newInner(int bottomDp) {
        LinearLayout inner = new LinearLayout(this);
        inner.setOrientation(LinearLayout.VERTICAL);
        int left = tablet ? dp(104) : dp(24);
        inner.setPadding(left, dp(48), dp(24), dp(bottomDp));
        return inner;
    }

    private ScrollView makeScroll(View child, int bottomDp) {
        ScrollView sv = new ScrollView(this);
        sv.setBackgroundColor(0xFF000000);
        sv.addView(child, new ScrollView.LayoutParams(MATCH(), WRAP()));
        return sv;
    }

    private TextView label(String text) {
        TextView t = new TextView(this);
        t.setText(text);
        t.setTextColor(0xFFCCCCCC);
        t.setTextSize(14);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(MATCH(), WRAP());
        lp.setMargins(0, dp(8), 0, dp(8));
        t.setLayoutParams(lp);
        return t;
    }

    private View block(View v) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(MATCH(), WRAP());
        lp.setMargins(0, 0, 0, dp(20));
        v.setLayoutParams(lp);
        return v;
    }

    private GlassCapsuleButton makeSwitch(String key, String base) {
        GlassCapsuleButton b = new GlassCapsuleButton(this);
        b.setTag(base);
        b.setOnClickListener(v -> {
            boolean nx = !sp.getBoolean(key, false);
            sp.edit().putBoolean(key, nx).apply();
            applySwitch(key);
        });
        switchBtns.put(key, b);
        return b;
    }

    private void applySwitch(String key) {
        GlassCapsuleButton b = switchBtns.get(key);
        boolean on = sp.getBoolean(key, false);
        b.setGlassSelected(on);
        b.setText(b.getTag() + "（" + (on ? "开" : "关") + "）");
    }

    // ------------------------------------------------------------------
    // 导航一：10 个选项
    // ------------------------------------------------------------------

    private View buildTab1() {
        LinearLayout inner = newInner(112);

        // 1-3 开关
        inner.addView(label("选项一：开关"));
        inner.addView(block(makeSwitch("t1_s1", "选项一")));
        inner.addView(label("选项二：开关"));
        inner.addView(block(makeSwitch("t1_s2", "选项二")));
        inner.addView(label("选项三：开关"));
        inner.addView(block(makeSwitch("t1_s3", "选项三")));

        // 4 弹窗 5 选项
        inner.addView(label("选项四：弹窗五选项"));
        btnDialog = new GlassCapsuleButton(this);
        btnDialog.setOnClickListener(v -> showChoiceDialog());
        inner.addView(block(btnDialog));

        // 5 全屏二级界面
        inner.addView(label("选项五：全屏二级界面"));
        GlassCapsuleButton btnSecond = new GlassCapsuleButton(this);
        btnSecond.setText("打开全屏二级界面");
        btnSecond.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, SecondScreenActivity.class)));
        inner.addView(block(btnSecond));

        // 6 三语言循环
        inner.addView(label("选项六：点击循环切换语言"));
        btnLang = new GlassCapsuleButton(this);
        btnLang.setOnClickListener(v -> {
            int nx = (sp.getInt("t1_lang", 0) + 1) % 3;
            sp.edit().putInt("t1_lang", nx).apply();
            refreshLang();
        });
        inner.addView(block(btnLang));

        // 7 页面内三互斥单选
        inner.addView(label("选项七：页面内互斥单选组"));
        pageRadioGroup = new RadioGroup(this);
        pageRadioGroup.setOrientation(RadioGroup.VERTICAL);
        for (int i = 0; i < 3; i++) {
            GlassRadioButton rb = new GlassRadioButton(this);
            rb.setText("单选项" + (i + 1));
            rb.setId(View.generateViewId());
            final int idx = i;
            rb.setOnClickListener(v -> sp.edit().putInt("t1_radio", idx).apply());
            RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(MATCH(), WRAP());
            lp.setMargins(0, 0, 0, dp(10));
            pageRadioGroup.addView(rb, lp);
        }
        inner.addView(block(pageRadioGroup));

        // 8 透明度滑杆
        inner.addView(label("选项八：透明度滑杆（0-100）"));
        opacityValue = new TextView(this);
        opacityValue.setTextColor(0xFF0A84FF);
        opacityValue.setTextSize(14);
        opacityValue.setGravity(Gravity.CENTER);
        inner.addView(block(opacityValue));
        opacitySeek = new SeekBar(this);
        opacitySeek.setMax(100);
        try {
            opacitySeek.setProgressTintList(ColorStateList.valueOf(0xFF0A84FF));
            opacitySeek.setThumbTintList(ColorStateList.valueOf(0xFF0A84FF));
        } catch (Throwable ignored) {}
        opacitySeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar sb, int p, boolean fromUser) {
                sp.edit().putInt("t1_opacity", p).apply();
                opacityValue.setText("当前透明度：" + p + "%");
                opacityPreview.setAlpha(p / 100f);
            }
            @Override public void onStartTrackingTouch(SeekBar sb) {}
            @Override public void onStopTrackingTouch(SeekBar sb) {}
        });
        inner.addView(block(opacitySeek));
        opacityPreview = new GlassCapsuleButton(this);
        opacityPreview.setText("透明度预览");
        inner.addView(block(opacityPreview));

        // 9 文本输入
        inner.addView(label("选项九：点击输入文本"));
        btnInput = new GlassCapsuleButton(this);
        btnInput.setOnClickListener(v -> showInputDialog());
        inner.addView(block(btnInput));

        // 10 三色选择器
        inner.addView(label("选项十：颜色选择器"));
        LinearLayout colorRow = new LinearLayout(this);
        colorRow.setOrientation(LinearLayout.HORIZONTAL);
        colorRow.setGravity(Gravity.CENTER);
        for (int i = 0; i < 3; i++) {
            final int idx = i;
            View sw = new View(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dp(56), dp(56));
            lp.setMargins(dp(12), 0, dp(12), 0);
            sw.setLayoutParams(lp);
            sw.setOnClickListener(v -> {
                sp.edit().putInt("t1_color", idx).apply();
                refreshColors();
            });
            colorSwatches[i] = sw;
            colorRow.addView(sw);
        }
        inner.addView(block(colorRow));

        return inner;
    }

    private void showChoiceDialog() {
        LinearLayout box = darkBox();
        box.addView(darkTitle("请选择一个选项"));

        ScrollView sv = new ScrollView(this);
        dialogGroup = new RadioGroup(this);
        dialogGroup.setOrientation(RadioGroup.VERTICAL);
        int sel = sp.getInt("t1_dialog", 0);
        for (int i = 0; i < 5; i++) {
            GlassRadioButton rb = new GlassRadioButton(this);
            rb.setText("弹窗选项" + (i + 1));
            rb.setId(View.generateViewId());
            final int idx = i;
            rb.setOnClickListener(v -> {
                sp.edit().putInt("t1_dialog", idx).apply();
                refreshDialog();
            });
            if (i == sel) rb.setChecked(true);
            RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(MATCH(), WRAP());
            lp.setMargins(0, 0, 0, dp(10));
            dialogGroup.addView(rb, lp);
        }
        sv.addView(dialogGroup, new ScrollView.LayoutParams(MATCH(), WRAP()));
        box.addView(sv);

        GlassCapsuleButton done = new GlassCapsuleButton(this);
        done.setText("完成");
        LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(MATCH(), WRAP());
        bp.topMargin = dp(16);
        box.addView(done, bp);

        AlertDialog dlg = darkDialog(box);
        done.setOnClickListener(v -> dlg.dismiss());
        dlg.show();
    }

    private void showInputDialog() {
        LinearLayout box = darkBox();
        box.addView(darkTitle("请输入文本"));

        EditText et = new EditText(this);
        et.setInputType(InputType.TYPE_CLASS_TEXT);
        et.setText(sp.getString("t1_input", ""));
        et.setTextColor(Color.WHITE);
        et.setHintTextColor(0xFF888888);
        et.setHint("输入胶囊要显示的文字");
        GradientDrawable etBg = new GradientDrawable();
        etBg.setCornerRadius(dp(16));
        etBg.setColor(0xFF2C2C2E);
        etBg.setStroke(dp(1), 0x40FFFFFF);
        et.setBackground(etBg);
        et.setPadding(dp(16), dp(12), dp(16), dp(12));
        box.addView(et, new LinearLayout.LayoutParams(MATCH(), WRAP()));

        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        GlassCapsuleButton cancel = new GlassCapsuleButton(this);
        cancel.setText("取消");
        GlassCapsuleButton ok = new GlassCapsuleButton(this);
        ok.setText("确认");
        LinearLayout.LayoutParams wl = new LinearLayout.LayoutParams(0, WRAP(), 1f);
        wl.setMargins(dp(6), 0, dp(6), 0);
        row.addView(cancel, wl);
        row.addView(ok, wl);
        LinearLayout.LayoutParams rp = new LinearLayout.LayoutParams(MATCH(), WRAP());
        rp.topMargin = dp(16);
        box.addView(row, rp);

        AlertDialog dlg = darkDialog(box);
        cancel.setOnClickListener(v -> dlg.dismiss());
        ok.setOnClickListener(v -> {
            String s = et.getText().toString();
            sp.edit().putString("t1_input", s).apply();
            refreshInput();
            dlg.dismiss();
        });
        dlg.show();
    }

    // ------------------------------------------------------------------
    // 导航二：20 开关 + 固定底部条（版本号 / 检查更新）
    // ------------------------------------------------------------------

    private View buildTab2() {
        LinearLayout inner = newInner(210);
        for (int i = 1; i <= 20; i++) {
            inner.addView(label("开关" + cn(i)));
            inner.addView(block(makeSwitch("t2_s" + i, "开关" + cn(i))));
        }
        return inner;
    }

    private void buildTab2Footer() {
        tab2Footer = new LinearLayout(this);
        tab2Footer.setOrientation(LinearLayout.HORIZONTAL);
        tab2Footer.setGravity(Gravity.CENTER);
        tab2Footer.setVisibility(View.GONE);

        String version = "1.0";
        try {
            version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (Exception ignored) {}

        GlassCapsuleButton ver = new GlassCapsuleButton(this);
        ver.setText("当前版本：v" + version);
        ver.setClickable(false);

        GlassCapsuleButton check = new GlassCapsuleButton(this);
        check.setText("检查更新");
        check.setOnClickListener(v ->
                Toast.makeText(this, "已是最新版本", Toast.LENGTH_SHORT).show());

        LinearLayout.LayoutParams wl = new LinearLayout.LayoutParams(0, WRAP(), 1f);
        wl.setMargins(dp(6), 0, dp(6), 0);
        int left = tablet ? dp(104) : dp(24);
        FrameLayout.LayoutParams fp = new FrameLayout.LayoutParams(MATCH(), WRAP());
        fp.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        fp.leftMargin = left;
        fp.rightMargin = dp(24);
        fp.bottomMargin = dp(108);
        tab2Footer.setLayoutParams(fp);
        tab2Footer.addView(ver, wl);
        tab2Footer.addView(check, wl);
    }

    // ------------------------------------------------------------------
    // 导航三：10 行"二十字描述 + 开关"，交替排布
    // ------------------------------------------------------------------

    private View buildTab3() {
        LinearLayout inner = newInner(112);
        for (int i = 0; i < 10; i++) {
            final int idx = i;
            String key = "t3_s" + (i + 1);

            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setGravity(Gravity.CENTER_VERTICAL);
            GradientDrawable rowBg = new GradientDrawable();
            rowBg.setCornerRadius(dp(28));
            rowBg.setColor(0x331C1C1E);
            rowBg.setStroke(dp(1), 0x40FFFFFF);
            row.setBackground(rowBg);
            row.setPadding(dp(16), dp(10), dp(16), dp(10));

            TextView desc = new TextView(this);
            desc.setText((i + 1) + ". " + DESC20);
            desc.setTextColor(Color.WHITE);
            desc.setTextSize(13);

            GlassCapsuleButton indicator = new GlassCapsuleButton(this);
            indicator.setClickable(false);
            tab3Indicators[i] = indicator;
            LinearLayout.LayoutParams ilp = new LinearLayout.LayoutParams(dp(72), WRAP());

            LinearLayout.LayoutParams dlp = new LinearLayout.LayoutParams(0, WRAP(), 1f);

            // 交替：偶数行 描述在左/开关在右；奇数行 开关在左/描述在右
            if (i % 2 == 0) {
                row.addView(desc, dlp);
                row.addView(indicator, ilp);
            } else {
                row.addView(indicator, ilp);
                row.addView(desc, dlp);
            }

            row.setOnClickListener(v -> {
                boolean nx = !sp.getBoolean(key, false);
                sp.edit().putBoolean(key, nx).apply();
                refreshTab3Row(idx);
            });

            inner.addView(block(row));
        }
        return inner;
    }

    private void refreshTab3Row(int i) {
        boolean on = sp.getBoolean("t3_s" + (i + 1), false);
        GlassCapsuleButton ind = tab3Indicators[i];
        ind.setGlassSelected(on);
        ind.setText(on ? "开" : "关");
    }

    // ------------------------------------------------------------------
    // 导航
    // ------------------------------------------------------------------

    private void buildNav() {
        nav = new GlassNavBar(this);
        nav.addItem(resIcon(android.R.drawable.ic_menu_view), "导航一");
        nav.addItem(resIcon(android.R.drawable.ic_menu_manage), "导航二");
        nav.addItem(resIcon(android.R.drawable.ic_menu_agenda), "导航三");

        if (tablet) {
            nav.setOrientation(LinearLayout.VERTICAL);
            nav.setSideWidthDp(72f);
        }

        nav.setOnItemSelectedListener(this::selectTab);
    }

    private Drawable resIcon(int id) {
        return getResources().getDrawable(id);
    }

    private FrameLayout.LayoutParams navParams() {
        FrameLayout.LayoutParams p;
        if (tablet) {
            int h = getResources().getDisplayMetrics().heightPixels / 2;
            p = new FrameLayout.LayoutParams(WRAP(), h);
            p.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
            p.leftMargin = dp(20);
        } else {
            p = new FrameLayout.LayoutParams(MATCH(), WRAP());
            p.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
            p.leftMargin = dp(24);
            p.rightMargin = dp(24);
            p.bottomMargin = dp(24);
        }
        return p;
    }

    private void selectTab(int index) {
        tab1.setVisibility(index == 0 ? View.VISIBLE : View.GONE);
        tab2.setVisibility(index == 1 ? View.VISIBLE : View.GONE);
        tab3.setVisibility(index == 2 ? View.VISIBLE : View.GONE);
        tab2Footer.setVisibility(index == 1 ? View.VISIBLE : View.GONE);
    }

    // ------------------------------------------------------------------
    // 深色弹窗
    // ------------------------------------------------------------------

    private LinearLayout darkBox() {
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setBackgroundColor(0xFF1C1C1E);
        box.setPadding(dp(20), dp(20), dp(20), dp(20));
        return box;
    }

    private TextView darkTitle(String s) {
        TextView t = new TextView(this);
        t.setText(s);
        t.setTextColor(Color.WHITE);
        t.setTextSize(18);
        t.setPadding(0, 0, 0, dp(14));
        return t;
    }

    private AlertDialog darkDialog(View content) {
        AlertDialog dlg = new AlertDialog.Builder(this,
                android.R.style.Theme_Material_Dialog_Alert).create();
        dlg.setView(content);
        dlg.setOnShowListener(d -> dlg.getWindow()
                .setBackgroundDrawable(new ColorDrawable(0xFF1C1C1E)));
        return dlg;
    }

    // ------------------------------------------------------------------
    // 回读恢复
    // ------------------------------------------------------------------

    private void loadAll() {
        for (String key : switchBtns.keySet()) applySwitch(key);
        refreshDialog();
        refreshLang();
        refreshInput();
        refreshColors();

        int radio = sp.getInt("t1_radio", 0);
        ((GlassRadioButton) pageRadioGroup.getChildAt(radio)).setChecked(true);

        int op = sp.getInt("t1_opacity", 50);
        opacitySeek.setProgress(op);
        opacityValue.setText("当前透明度：" + op + "%");
        opacityPreview.setAlpha(op / 100f);

        for (int i = 0; i < 10; i++) refreshTab3Row(i);
    }

    private void refreshDialog() {
        btnDialog.setText("弹窗选项" + (sp.getInt("t1_dialog", 0) + 1));
    }

    private void refreshLang() {
        btnLang.setGlassSelected(true);
        btnLang.setText("当前语言：" + LANGS[sp.getInt("t1_lang", 0)]);
    }

    private void refreshInput() {
        String s = sp.getString("t1_input", "");
        btnInput.setText(s == null || s.isEmpty() ? "点击输入文本" : s);
    }

    private void refreshColors() {
        int sel = sp.getInt("t1_color", 0);
        for (int i = 0; i < 3; i++) {
            GradientDrawable d = new GradientDrawable();
            d.setShape(GradientDrawable.OVAL);
            d.setColor(COLORS[i]);
            if (i == sel) d.setStroke(dp(4), Color.WHITE);
            colorSwatches[i].setBackground(d);
        }
    }

    private static String cn(int n) {
        String[] d = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        if (n < 10) return d[n];
        if (n == 10) return "十";
        if (n < 20) return "十" + d[n - 10];
        if (n == 20) return "二十";
        return String.valueOf(n);
    }
}
