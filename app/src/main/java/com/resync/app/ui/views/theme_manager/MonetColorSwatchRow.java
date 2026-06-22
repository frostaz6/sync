package com.resync.app.ui.views.theme_manager;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.resync.app.color.Palette;
import com.resync.app.R;

/* JADX INFO: loaded from: classes3.dex */
public class MonetColorSwatchRow extends LinearLayout {

    TextView accent1Swatch;

    TextView accent2Swatch;

    TextView accent3Swatch;

    TextView neutral1Swatch;

    TextView neutral2Swatch;

    public MonetColorSwatchRow(Context context, Palette palette, int shade) {
        super(context);
        LinearLayout.inflate(getContext(), R.layout.view_color_swatch_row, this);
        ButterKnife.bind(this);
        this.neutral1Swatch.setBackgroundColor(palette.neutral1(shade));
        this.neutral1Swatch.setText("N1-" + shade);
        this.neutral1Swatch.setTextColor(shade < 600 ? -16777216 : -1);
        this.neutral2Swatch.setBackgroundColor(palette.neutral2(shade));
        this.neutral2Swatch.setText("N2-" + shade);
        this.neutral2Swatch.setTextColor(shade < 600 ? -16777216 : -1);
        this.accent1Swatch.setBackgroundColor(palette.accent1(shade));
        this.accent1Swatch.setText("A1-" + shade);
        this.accent1Swatch.setTextColor(shade < 600 ? -16777216 : -1);
        this.accent2Swatch.setBackgroundColor(palette.accent2(shade));
        this.accent2Swatch.setText("A2-" + shade);
        this.accent2Swatch.setTextColor(shade < 600 ? -16777216 : -1);
        this.accent3Swatch.setBackgroundColor(palette.accent3(shade));
        this.accent3Swatch.setText("A3-" + shade);
        this.accent3Swatch.setTextColor(shade >= 600 ? -1 : -16777216);
    }
}
