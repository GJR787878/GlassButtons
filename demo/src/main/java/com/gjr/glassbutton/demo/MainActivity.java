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
 * 全部界面文案经 L 多语言支持；语言切换后 recreate() 真正重渲染。
 */
public class MainActivity extends Activity {

    private static final String PREFS = "demo_prefs";

    private SharedPreferences sp;
    private FrameLayout root;
    private ScrollView tab1, tab2, tab3;
    private GlassNavBar nav;
    private boolean tablet;

    // 带文案的整行开关：key -> 胶囊（base 文案在 tag）
    private final HashMap<String, GlassCapsuleButton> switchBtns = new HashMap<>();
    // 仅显示 开/关 的整行开关（导航三）：key -> 胶囊
    private final HashMap<String, GlassCapsuleButton> onOffBtns = new HashMap<>();

    private GlassCapsuleButton btnDialog, btnLang, btnInput;
    private RadioGroup dialogGroup, pageRadioGroup;
    private TextView opacityValue;
    private SeekBar opacitySeek;
    private GlassCapsuleButton opacityPreview;
    private final View[] colorSwatches = new View[3];

    private int dp(float v) {
        return Math.round(v * getResources().getDisplayMetrics().density);
    }

    private int MATCH() { return ViewGroup.LayoutParams.MATCH_PARENT; }
    private int WRAP() { return ViewGroup.LayoutParams.WRAP_CONTENT; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences(PREFS, MODE_PRIVATE);
        tablet = getResources().getConfiguration().smallestScreenWidthDp >= 600;

        root = new FrameLayout(this);
        root.setBackgroundColor(0xFF000000);

        tab1 = makeScroll(buildTab1());
        tab2 = makeScroll(buildTab2());
        tab3 = makeScroll(buildTab3());

        root.addView(tab1, new FrameLayout.LayoutParams(MATCH(), MATCH()));
        root.addView(tab2, new FrameLayout.LayoutParams(MATCH(), MATCH()));
        root.addView(tab3, new FrameLayout.LayoutParams(MATCH(), MATCH()));
        tab2.setVisibility(View.GONE);
        tab3.setVisibility(View.GONE);

        buildNav();
        root.addView(nav, navParams());

        setContentView(root);
        loadAll();
    }

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

    private ScrollView makeScroll(View child) {
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

    /** 带文案开关：胶囊显示 base（开/关）。 */
    private GlassCapsuleButton makeLabeledSwitch(String key, String base) {
        GlassCapsuleButton b = new GlassCapsuleButton(this);
        b.setTag(base);
        b.setOnClickListener(v -> {
            sp.edit().putBoolean(key, !sp.getBoolean(key, false)).apply();
            applyLabeled(key);
        });
        switchBtns.put(key, b);
        return b;
    }

    private void applyLabeled(String key) {
        GlassCapsuleButton b = switchBtns.get(key);
        boolean on = sp.getBoolean(key, false);
        b.setGlassSelected(on);
        b.setText(b.getTag() + "（" + L.t(this, on ? "on" : "off") + "）");
    }

    /** 仅显示 开/关 的整行开关（导航三）。 */
    private GlassCapsuleButton makeOnOffSwitch(String key) {
        GlassCapsuleButton b = new GlassCapsuleButton(this);
        b.setOnClickListener(v -> {
            sp.edit().putBoolean(key, !sp.getBoolean(key, false)).apply();
            applyOnOff(key);
        });
        onOffBtns.put(key, b);
        return b;
    }

    private void applyOnOff(String key) {
        GlassCapsuleButton b = onOffBtns.get(key);
        boolean on = sp.getBoolean(key, false);
        b.setGlassSelected(on);
        b.setText(L.t(this, on ? "on" : "off"));
    }

    // ------------------------------------------------------------------
    // 导航一
    // ------------------------------------------------------------------

    private View buildTab1() {
        LinearLayout inner = newInner(112);

        inner.addView(label(L.t(this, "l1")));
        inner.addView(block(makeLabeledSwitch("t1_s1", L.optName(this, 1))));
        inner.addView(label(L.t(this, "l2")));
        inner.addView(block(makeLabeledSwitch("t1_s2", L.optName(this, 2))));
        inner.addView(label(L.t(this, "l3")));
        inner.addView(block(makeLabeledSwitch("t1_s3", L.optName(this, 3))));

        inner.addView(label(L.t(this, "l4")));
        btnDialog = new GlassCapsuleButton(this);
        btnDialog.setOnClickListener(v -> showChoiceDialog());
        inner.addView(block(btnDialog));

        inner.addView(label(L.t(this, "l5")));
        GlassCapsuleButton btnSecond = new GlassCapsuleButton(this);
        btnSecond.setText(L.t(this, "open_second"));
        btnSecond.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, SecondScreenActivity.class)));
        inner.addView(block(btnSecond));

        inner.addView(label(L.t(this, "l6")));
        btnLang = new GlassCapsuleButton(this);
        btnLang.setOnClickListener(v -> {
            int nx = (L.idx(this) + 1) % 3;
            sp.edit().putInt("t1_lang", nx).apply();
            recreate(); // 真正按新语言重建整个界面
        });
        inner.addView(block(btnLang));

        inner.addView(label(L.t(this, "l7")));
        pageRadioGroup = new RadioGroup(this);
        pageRadioGroup.setOrientation(RadioGroup.VERTICAL);
        for (int i = 0; i < 3; i++) {
            GlassRadioButton rb = new GlassRadioButton(this);
            rb.setText(L.radioOpt(this, i + 1));
            rb.setId(View.generateViewId());
            final int idx = i;
            rb.setOnClickListener(v -> sp.edit().putInt("t1_radio", idx).apply());
            RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(MATCH(), WRAP());
            lp.setMargins(0, 0, 0, dp(10));
            pageRadioGroup.addView(rb, lp);
        }
        inner.addView(block(pageRadioGroup));

        inner.addView(label(L.t(this, "l8")));
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
                opacityValue.setText(L.t(MainActivity.this, "cur_opacity") + p + "%");
                opacityPreview.setAlpha(p / 100f);
            }
            @Override public void onStartTrackingTouch(SeekBar sb) {}
            @Override public void onStopTrackingTouch(SeekBar sb) {}
        });
        inner.addView(block(opacitySeek));
        opacityPreview = new GlassCapsuleButton(this);
        opacityPreview.setText(L.t(this, "opacity_preview"));
        inner.addView(block(opacityPreview));

        inner.addView(label(L.t(this, "l9")));
        btnInput = new GlassCapsuleButton(this);
        btnInput.setOnClickListener(v -> showInputDialog());
        inner.addView(block(btnInput));

        inner.addView(label(L.t(this, "l10")));
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
        box.addView(darkTitle(L.t(this, "dlg_title")));

        ScrollView sv = new ScrollView(this);
        dialogGroup = new RadioGroup(this);
        dialogGroup.setOrientation(RadioGroup.VERTICAL);
        int sel = sp.getInt("t1_dialog", 0);
        for (int i = 0; i < 5; i++) {
            GlassRadioButton rb = new GlassRadioButton(this);
            rb.setText(L.dialogOpt(this, i + 1));
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
        done.setText(L.t(this, "done"));
        LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(MATCH(), WRAP());
        bp.topMargin = dp(16);
        box.addView(done, bp);

        AlertDialog dlg = darkDialog(box);
        done.setOnClickListener(v -> dlg.dismiss());
        dlg.show();
    }

    private void showInputDialog() {
        LinearLayout box = darkBox();
        box.addView(darkTitle(L.t(this, "input_title")));

        EditText et = new EditText(this);
        et.setInputType(InputType.TYPE_CLASS_TEXT);
        et.setText(sp.getString("t1_input", ""));
        et.setTextColor(Color.WHITE);
        et.setHintTextColor(0xFF888888);
        et.setHint(L.t(this, "input_hint"));
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
        cancel.setText(L.t(this, "cancel"));
        GlassCapsuleButton ok = new GlassCapsuleButton(this);
        ok.setText(L.t(this, "ok"));
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
            sp.edit().putString("t1_input", et.getText().toString()).apply();
            refreshInput();
            dlg.dismiss();
        });
        dlg.show();
    }

    // ------------------------------------------------------------------
    // 导航二
    // ------------------------------------------------------------------

    private View buildTab2() {
        LinearLayout inner = newInner(112);
        for (int i = 1; i <= 20; i++) {
            inner.addView(label(L.swName(this, i)));
            inner.addView(block(makeLabeledSwitch("t2_s" + i, L.swName(this, i))));
        }
        // 版本 + 检查更新：放在 ScrollView 内容末尾，随内容滚动
        inner.addView(buildVersionRow());
        return inner;
    }

    /** 版本信息 + 检查更新（水平排列，随内容滚动）。 */
    private View buildVersionRow() {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER);

        String version = "1.0";
        try {
            version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (Exception ignored) {}

        GlassCapsuleButton ver = new GlassCapsuleButton(this);
        ver.setText(L.t(this, "cur_version") + version);
        ver.setClickable(false);

        GlassCapsuleButton check = new GlassCapsuleButton(this);
        check.setText(L.t(this, "check_update"));
        check.setOnClickListener(v ->
                Toast.makeText(this, L.t(this, "latest"), Toast.LENGTH_SHORT).show());

        LinearLayout.LayoutParams wl = new LinearLayout.LayoutParams(0, WRAP(), 1f);
        wl.setMargins(dp(6), 0, dp(6), 0);
        row.addView(ver, wl);
        row.addView(check, wl);
        return block(row);
    }

    // ------------------------------------------------------------------
    // 导航三：描述行 + 整行开关，纵向交替（共 10 组）
    // ------------------------------------------------------------------

    private View buildTab3() {
        LinearLayout inner = newInner(112);
        TextView title = new TextView(this);
        title.setText(L.t(this, "tab3_title"));
        title.setTextColor(0xFFFFFFFF);
        title.setTextSize(18);
        LinearLayout.LayoutParams tlp = new LinearLayout.LayoutParams(MATCH(), WRAP());
        tlp.setMargins(0, dp(8), 0, dp(16));
        inner.addView(title, tlp);

        for (int i = 1; i <= 10; i++) {
            String key = "t3_s" + i;
            inner.addView(label(L.desc3(this, i)));   // 描述行
            inner.addView(block(makeOnOffSwitch(key))); // 整行开关
        }
        return inner;
    }

    // ------------------------------------------------------------------
    // 导航
    // ------------------------------------------------------------------

    private void buildNav() {
        nav = new GlassNavBar(this);
        nav.addItem(resIcon(android.R.drawable.ic_menu_view), L.t(this, "nav1"));
        nav.addItem(resIcon(android.R.drawable.ic_menu_manage), L.t(this, "nav2"));
        nav.addItem(resIcon(android.R.drawable.ic_menu_agenda), L.t(this, "nav3"));
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
        dlg.setOnShowListener(d -> {
            android.view.Window w = dlg.getWindow();
            w.setBackgroundDrawable(new ColorDrawable(0xFF1C1C1E));
            // 显式居中，避免部分 ROM 上弹窗偏下
            w.setGravity(android.view.Gravity.CENTER);
            android.view.WindowManager.LayoutParams lp = w.getAttributes();
            lp.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.88f);
            w.setAttributes(lp);
        });
        return dlg;
    }

    // ------------------------------------------------------------------
    // 回读恢复
    // ------------------------------------------------------------------

    private void loadAll() {
        for (String key : switchBtns.keySet()) applyLabeled(key);
        for (String key : onOffBtns.keySet()) applyOnOff(key);
        refreshDialog();
        refreshLang();
        refreshInput();
        refreshColors();

        int radio = sp.getInt("t1_radio", 0);
        ((GlassRadioButton) pageRadioGroup.getChildAt(radio)).setChecked(true);

        int op = sp.getInt("t1_opacity", 50);
        opacitySeek.setProgress(op);
        opacityValue.setText(L.t(this, "cur_opacity") + op + "%");
        opacityPreview.setAlpha(op / 100f);
    }

    private void refreshDialog() {
        btnDialog.setText(L.dialogOpt(this, sp.getInt("t1_dialog", 0) + 1));
    }

    private void refreshLang() {
        btnLang.setGlassSelected(false);
        btnLang.setText(L.t(this, "cur_lang") + L.langName(L.idx(this)));
    }

    private void refreshInput() {
        String s = sp.getString("t1_input", "");
        btnInput.setText(s == null || s.isEmpty() ? L.t(this, "tap_input") : s);
    }

    private void refreshColors() {
        int sel = sp.getInt("t1_color", 0);
        int[] vals = {0xFFFF3B30, 0xFF34C759, 0xFF0A84FF};
        for (int i = 0; i < 3; i++) {
            GradientDrawable d = new GradientDrawable();
            d.setShape(GradientDrawable.OVAL);
            d.setColor(vals[i]);
            if (i == sel) d.setStroke(dp(4), Color.WHITE);
            colorSwatches[i].setBackground(d);
        }
    }
}
