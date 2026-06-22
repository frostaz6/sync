package com.resync.app.ui.viewholders.onboarding.pages;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.resync.app.R;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.viewholders.onboarding.AbstractOnboardingViewHolder;
import com.resync.app.ui.views.responsive.chips.ContentChip;

/* JADX INFO: loaded from: classes3.dex */
public class HeaderModeViewHolder extends AbstractOnboardingViewHolder {
    ContentChip chipCompact;
    ContentChip chipExpanded;
    TextView headerDescription;
    ImageView previewCompact;
    ImageView previewExpanded;

    public HeaderModeViewHolder(ViewGroup viewGroup) {
        super(viewGroup);
    }

    public static boolean isExpandedHeaderEnabled() {
        return SettingsSingleton.getSettings().toolbarExpanded;
    }

    private void updateChipText() {
        this.chipExpanded.setSelected(isExpandedHeaderEnabled());
        this.chipCompact.setSelected(!isExpandedHeaderEnabled());
    }

    private void updateNavigationDescription() {
        if (isExpandedHeaderEnabled()) {
            this.headerDescription.setText("ReSync now uses a large expanded header at the top of posts. From here you can view the sub icon, sub features and more.");
        } else {
            this.headerDescription.setText("If the expanded header is too much, you might find compact a better size.\n\nYou can still access sub features from the overflow icon (three dots) top right.");
        }
    }

    private void updatePreview() {
        if (isExpandedHeaderEnabled()) {
            this.previewExpanded.setVisibility(0);
            this.previewCompact.setVisibility(8);
        } else {
            this.previewExpanded.setVisibility(8);
            this.previewCompact.setVisibility(0);
        }
    }

    @Override // com.resync.app.ui.viewholders.onboarding.AbstractOnboardingViewHolder
    public String getDescription() {
        return null;
    }

    @Override // com.resync.app.holders.OnboardingViewHolder
    public int getIconRes() {
        return R.drawable.outline_text_fields_24;
    }

    @Override // com.resync.app.ui.viewholders.onboarding.AbstractOnboardingViewHolder
    public int getInnerContentRes() {
        return R.layout.viewholder_inner_header;
    }

    @Override // com.resync.app.holders.OnboardingViewHolder
    public String getTitle() {
        return "Header type";
    }

    public void onChipClicked(View view) {
        if (this.chipExpanded.equals(view)) {
            SettingsSingleton.getSettings().toolbarExpanded = true;
            SettingsSingleton.getInstance().putBoolean("toolbar_expanded", true);
        } else if (this.chipCompact.equals(view)) {
            SettingsSingleton.getSettings().toolbarExpanded = false;
            SettingsSingleton.getInstance().putBoolean("toolbar_expanded", false);
        }
        updateChipText();
        updatePreview();
        updateNavigationDescription();
    }

    @Override // com.resync.app.ui.viewholders.onboarding.AbstractOnboardingViewHolder
    protected void setup() {
        this.previewExpanded = (ImageView) this.itemView.findViewById(R.id.preview_expanded);
        this.previewCompact = (ImageView) this.itemView.findViewById(R.id.preview_compact);
        this.headerDescription = (TextView) this.itemView.findViewById(R.id.header_description);
        ContentChip contentChip = new ContentChip(this.itemView.getContext());
        this.chipExpanded = contentChip;
        contentChip.setText("Expanded");
        this.chipExpanded.setOnClickListener(new View.OnClickListener() { // from class: sb.a
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                HeaderModeViewHolder.this.onChipClicked(view);
            }
        });
        this.chipGroup.addView(this.chipExpanded);
        ContentChip contentChip2 = new ContentChip(this.itemView.getContext());
        this.chipCompact = contentChip2;
        contentChip2.setText("Compact");
        this.chipCompact.setOnClickListener(new View.OnClickListener() { // from class: sb.a
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                HeaderModeViewHolder.this.onChipClicked(view);
            }
        });
        this.chipGroup.addView(this.chipCompact);
        updateChipText();
        updatePreview();
        updateNavigationDescription();
    }

    @Override // com.resync.app.ui.viewholders.onboarding.AbstractOnboardingViewHolder
    public boolean showChipGroup() {
        return true;
    }
}
