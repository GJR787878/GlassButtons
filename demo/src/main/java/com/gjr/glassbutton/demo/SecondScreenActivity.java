package com.gjr.glassbutton.demo;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gjr.glassbutton.GlassCapsuleButton;
import com.gjr.glassbutton.GlassRadioButton;

/**
 * 全屏二级界面（导航一 · 选项五）。
 * 纯黑底、与主界面同一套美术语言；含 3 个互斥选项，状态持久化；
 * 底部自绘毛玻璃「返回」按钮，点击 finish()。
 */
public class SecondScreenActivity extends Activity {

    private SharedPreferences sp;
    private boolean tablet;

    private int dp(float v) {
        return Math.round(v * getResources().getDisplayMetrics().density);
    }

    private int MATCH() { return ViewGroup.LayoutParams.MATCH_PARENT; }
    private int WRAP() { return ViewGroup.LayoutParams.WRAP_CONTENT; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("demo_prefs", MODE_PRIVATE);
        tablet = getResources().getConfiguration().smallestScreenWidthDp >= 600;

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(0xFF000000);
        int left = tablet ? dp(104) : dp(24);
        root.setPadding(left, dp(48), dp(24), dp(32));

        TextView title = new TextView(this);
        title.setText("全屏二级界面");
        title.setTextColor(0xFFFFFFFF);
        title.setTextSize(20);
        LinearLayout.LayoutParams tlp = new LinearLayout.LayoutParams(MATCH(), WRAP());
        tlp.setMargins(0, 0, 0, dp(24));
        root.addView(title, tlp);

        TextView sub = new TextView(this);
        sub.setText("请从下面三个选项中选择一个：");
        sub.setTextColor(0xFFCCCCCC);
        sub.setTextSize(14);
        LinearLayout.LayoutParams slp = new LinearLayout.LayoutParams(MATCH(), WRAP());
        slp.setMargins(0, 0, 0, dp(16));
        root.addView(sub, slp);

        RadioGroup group = new RadioGroup(this);
        group.setOrientation(RadioGroup.VERTICAL);
        int sel = sp.getInt("t1_second_sel", 0);
        for (int i = 0; i < 3; i++) {
            GlassRadioButton rb = new GlassRadioButton(this);
            rb.setText("二级选项" + (i + 1));
            rb.setId(View.generateViewId());
            final int idx = i;
            rb.setOnClickListener(v -> sp.edit().putInt("t1_second_sel", idx).apply());
            if (i == sel) rb.setChecked(true);
            RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(MATCH(), WRAP());
            lp.setMargins(0, 0, 0, dp(12));
            group.addView(rb, lp);
        }
        root.addView(group, new LinearLayout.LayoutParams(MATCH(), WRAP()));

        // 弹性空白，把返回按钮压到底部
        View spacer = new View(this);
        root.addView(spacer, new LinearLayout.LayoutParams(MATCH(), 0, 1f));

        GlassCapsuleButton back = new GlassCapsuleButton(this);
        back.setText("返回");
        back.setOnClickListener(v -> finish());
        LinearLayout.LayoutParams blp = new LinearLayout.LayoutParams(MATCH(), WRAP());
        blp.gravity = Gravity.CENTER_HORIZONTAL;
        root.addView(back, blp);

        setContentView(root);
    }
}
