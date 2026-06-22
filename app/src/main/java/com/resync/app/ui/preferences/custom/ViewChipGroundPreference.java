package com.resync.app.ui.preferences.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.google.android.material.chip.ChipGroup;
import com.resync.app.R;
import com.resync.app.singleton.UiSingleton;
import com.resync.app.ui.views.responsive.chips.ContentChip;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.helpers.ContextHelper;
import com.resync.app.dialogs.ChangeDefaultViewAlertDialogBottomSheet;

/* JADX INFO: loaded from: classes.dex */
public class ViewChipGroundPreference extends Preference {
    public static final String KEY = ViewChipGroundPreference.class.getSimpleName();

    ContentChip slidesChip;
    ContentChip cardsChip;

    ContentChip smallCardsChip;

    ContentChip smallerCardsChip;
    ContentChip compactChip;
    ContentChip listChip;

    public View boundView;

    public ViewChipGroundPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setKey(KEY);
        setLayoutResource(R.layout.preference_view_chip_group);
        setSelectable(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onChipClicked(int viewMode, View view) {
        FragmentUtils.showWithArguments(ChangeDefaultViewAlertDialogBottomSheet.class, ContextHelper.getFragmentManager(getContext()), ChangeDefaultViewAlertDialogBottomSheet.buildArguments(viewMode));
    }

    private void setupChip(ContentChip contentChip, final int viewMode) {
        if (viewMode == UiSingleton.getInstance().getUiMode()) {
            contentChip.setSelected(true);
            ChipGroup chipGroup = (ChipGroup) contentChip.getParent();
            chipGroup.removeView(contentChip);
            chipGroup.addView(contentChip, 0);
        }
        contentChip.setOnClickListener(new View.OnClickListener() { // from class: ab.g
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ViewChipGroundPreference.this.onChipClicked(viewMode, view);
            }
        });
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        View view = preferenceViewHolder.itemView;
        this.boundView = view;
        this.slidesChip = (ContentChip) view.findViewById(R.id.view_mode_slides);
        this.cardsChip = (ContentChip) this.boundView.findViewById(R.id.view_mode_cards);
        this.smallCardsChip = (ContentChip) this.boundView.findViewById(R.id.view_mode_small_cards);
        this.smallerCardsChip = (ContentChip) this.boundView.findViewById(R.id.view_mode_smaller_cards);
        this.compactChip = (ContentChip) this.boundView.findViewById(R.id.view_mode_compact);
        this.listChip = (ContentChip) this.boundView.findViewById(R.id.view_mode_list);
        setupChip(this.slidesChip, 5);
        setupChip(this.cardsChip, 4);
        setupChip(this.smallCardsChip, 3);
        setupChip(this.smallerCardsChip, 2);
        setupChip(this.compactChip, 1);
        setupChip(this.listChip, 0);
    }
}
