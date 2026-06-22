package com.resync.app.ui.preferences.custom.tooltips;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.resync.app.R;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.helpers.ContextHelper;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.dialogs.RestoreLegacyDeviceAlertDialogBottomSheet;

/* JADX INFO: loaded from: classes.dex */
public class RootLegacyTooltipPreference extends Preference {

    public static final String KEY = RootLegacyTooltipPreference.class.getSimpleName();

    public RootLegacyTooltipPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setKey(KEY);
        setLayoutResource(R.layout.preference_root_legacy_tooltip);
        setSelectable(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onTooltipClicked(View view) {
        FragmentUtils.show(RestoreLegacyDeviceAlertDialogBottomSheet.class, ContextHelper.getFragmentManager(getContext()));
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        ((FrameLayout.LayoutParams) preferenceViewHolder.itemView.findViewById(R.id.tooltip_card).getLayoutParams()).topMargin = SizeHelper.dpToPx(16);
        ((FrameLayout.LayoutParams) preferenceViewHolder.itemView.findViewById(R.id.tooltip_card).getLayoutParams()).bottomMargin = 0;
        preferenceViewHolder.itemView.findViewById(R.id.tooltip_card).setOnClickListener(new View.OnClickListener() { // from class: bb.c
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                RootLegacyTooltipPreference.this.onTooltipClicked(view);
            }
        });
        preferenceViewHolder.itemView.setEnabled(false);
    }
}
