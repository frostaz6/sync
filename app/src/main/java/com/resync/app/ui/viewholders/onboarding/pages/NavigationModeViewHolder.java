package com.resync.app.ui.viewholders.onboarding.pages;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.resync.app.R;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.viewholders.onboarding.AbstractOnboardingViewHolder;
import com.resync.app.ui.views.responsive.chips.ContentChip;
import com.squareup.otto.Subscribe;
import com.resync.app.events.OnOnboardingNavigationModeSelectedEvent;

/* JADX INFO: loaded from: classes3.dex */
public class NavigationModeViewHolder extends AbstractOnboardingViewHolder {
    ContentChip chipBoth;
    ContentChip chipBottom;
    ContentChip chipDrawer;
    TextView navigationDescription;
    ImageView previewBottom;
    ImageView previewDrawer;

    public NavigationModeViewHolder(ViewGroup viewGroup) {
        super(viewGroup);
    }

    public static boolean isBothNavigationEnabled() {
        return SettingsSingleton.getSettings().useDrawer && SettingsSingleton.getSettings().navigationBottom;
    }

    public static boolean isBottomNavigationEnabled() {
        return !SettingsSingleton.getSettings().useDrawer && SettingsSingleton.getSettings().navigationBottom;
    }

    public static boolean isDrawerNavigationEnabled() {
        return SettingsSingleton.getSettings().useDrawer && !SettingsSingleton.getSettings().navigationBottom;
    }

    private void updateChipText() {
        this.chipBottom.setSelected(isBottomNavigationEnabled());
        this.chipDrawer.setSelected(isDrawerNavigationEnabled());
        this.chipBoth.setSelected(isBothNavigationEnabled());
    }

    private void updateNavigationDescription() {
        if (isBottomNavigationEnabled()) {
            this.navigationDescription.setText("By default ReSync now uses bottom navigation which is best suited to big devices and gesture navigation.");
        } else if (isDrawerNavigationEnabled()) {
            this.navigationDescription.setText("Drawer navigation is the classic way to use ReSync. Subreddits can be changed by using a drawer pulled out from the left.");
        } else if (isBothNavigationEnabled()) {
            this.navigationDescription.setText("Bottom and drawer navigation is the best of both worlds.");
        }
    }

    private void updatePreview() {
        if (isBottomNavigationEnabled()) {
            this.previewDrawer.setVisibility(8);
            this.previewBottom.setVisibility(0);
        } else if (isDrawerNavigationEnabled()) {
            this.previewDrawer.setVisibility(0);
            this.previewBottom.setVisibility(8);
        } else if (isBothNavigationEnabled()) {
            this.previewDrawer.setVisibility(0);
            this.previewBottom.setVisibility(0);
        }
    }

    @Override // com.resync.app.ui.viewholders.onboarding.AbstractOnboardingViewHolder
    public String getDescription() {
        return null;
    }

    @Override // com.resync.app.holders.OnboardingViewHolder
    public int getIconRes() {
        return R.drawable.outline_swap_horiz_24;
    }

    @Override // com.resync.app.ui.viewholders.onboarding.AbstractOnboardingViewHolder
    public int getInnerContentRes() {
        return R.layout.viewholder_inner_navigation;
    }

    @Override // com.resync.app.holders.OnboardingViewHolder
    public String getTitle() {
        return "Navigation type";
    }

    public void onChipClicked(View view) {
        if (this.chipBottom.equals(view)) {
            SettingsSingleton.getSettings().useDrawer = false;
            SettingsSingleton.getInstance().putBoolean("use_drawer", false);
            SettingsSingleton.getSettings().navigationBottom = true;
            SettingsSingleton.getInstance().putBoolean("navigation_bottom", true);
        } else if (this.chipDrawer.equals(view)) {
            SettingsSingleton.getSettings().useDrawer = true;
            SettingsSingleton.getInstance().putBoolean("use_drawer", true);
            SettingsSingleton.getSettings().navigationBottom = false;
            SettingsSingleton.getInstance().putBoolean("navigation_bottom", false);
        } else if (this.chipBoth.equals(view)) {
            SettingsSingleton.getSettings().useDrawer = true;
            SettingsSingleton.getInstance().putBoolean("use_drawer", true);
            SettingsSingleton.getSettings().navigationBottom = true;
            SettingsSingleton.getInstance().putBoolean("navigation_bottom", true);
        }
        updateChipText();
        updatePreview();
        updateNavigationDescription();
    }

    @Subscribe
    public void onNavigationModeSelected(OnOnboardingNavigationModeSelectedEvent onOnboardingNavigationModeSelectedEvent) {
        updateChipText();
        updatePreview();
        updateNavigationDescription();
    }

    @Override // com.resync.app.ui.viewholders.onboarding.AbstractOnboardingViewHolder
    protected void setup() {
        this.previewDrawer = (ImageView) this.itemView.findViewById(R.id.preview_drawer);
        this.previewBottom = (ImageView) this.itemView.findViewById(R.id.preview_bottom);
        this.navigationDescription = (TextView) this.itemView.findViewById(R.id.navigation_description);
        ContentChip contentChip = new ContentChip(this.itemView.getContext());
        this.chipBottom = contentChip;
        contentChip.setText("Bottom");
        this.chipBottom.setOnClickListener(new View.OnClickListener() { // from class: sb.d
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                NavigationModeViewHolder.this.onChipClicked(view);
            }
        });
        this.chipGroup.addView(this.chipBottom);
        ContentChip contentChip2 = new ContentChip(this.itemView.getContext());
        this.chipDrawer = contentChip2;
        contentChip2.setText("Drawer");
        this.chipDrawer.setOnClickListener(new View.OnClickListener() { // from class: sb.d
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                NavigationModeViewHolder.this.onChipClicked(view);
            }
        });
        this.chipGroup.addView(this.chipDrawer);
        ContentChip contentChip3 = new ContentChip(this.itemView.getContext());
        this.chipBoth = contentChip3;
        contentChip3.setText("Both");
        this.chipBoth.setOnClickListener(new View.OnClickListener() { // from class: sb.d
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                NavigationModeViewHolder.this.onChipClicked(view);
            }
        });
        this.chipGroup.addView(this.chipBoth);
        updateChipText();
        updatePreview();
        updateNavigationDescription();
    }

    @Override // com.resync.app.ui.viewholders.onboarding.AbstractOnboardingViewHolder
    public boolean showChipGroup() {
        return true;
    }
}
