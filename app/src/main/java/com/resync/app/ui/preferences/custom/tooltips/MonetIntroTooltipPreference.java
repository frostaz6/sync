package com.resync.app.ui.preferences.custom.tooltips;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.resync.app.R;
import com.resync.app.ui.preferences.custom.tooltips.MonetIntroTooltipPreference;
import com.resync.app.helpers.SizeHelper;

/* JADX INFO: loaded from: classes.dex */
public class MonetIntroTooltipPreference extends Preference {

    public static final String KEY = MonetIntroTooltipPreference.class.getSimpleName();

    public MonetIntroTooltipPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setKey(KEY);
        setLayoutResource(R.layout.preference_monet_intro_tooltip);
        setSelectable(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void onTooltipClicked(View view) {
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        ((FrameLayout.LayoutParams) preferenceViewHolder.itemView.findViewById(R.id.tooltip_card).getLayoutParams()).topMargin = SizeHelper.dpToPx(32);
        ((FrameLayout.LayoutParams) preferenceViewHolder.itemView.findViewById(R.id.tooltip_card).getLayoutParams()).bottomMargin = 0;
        preferenceViewHolder.itemView.findViewById(R.id.tooltip_card).setOnClickListener(new View.OnClickListener() { // from class: bb.b
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MonetIntroTooltipPreference.onTooltipClicked(view);
            }
        });
        preferenceViewHolder.itemView.setEnabled(false);
    }
}
