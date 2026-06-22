package com.resync.app.ui.viewholders.onboarding.pages;

import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import com.resync.app.R;
import com.resync.app.ui.viewholders.onboarding.AbstractOnboardingViewHolder;
import com.resync.app.helpers.ContextHelper;
import com.resync.app.fragments.preferences.PreferencesOnboardingTabletFragment;

/* JADX INFO: loaded from: classes3.dex */
public class TabletViewHolder extends AbstractOnboardingViewHolder {
    public TabletViewHolder(ViewGroup viewGroup) {
        super(viewGroup);
    }

    @Override // com.resync.app.ui.viewholders.onboarding.AbstractOnboardingViewHolder
    public String getDescription() {
        return "Tablet mode is an optimised interface for large screen devices featuring the new navigation rail. When enabled you will be able to use dual pane mode and tabbed comments.";
    }

    @Override // com.resync.app.holders.OnboardingViewHolder
    public int getIconRes() {
        return R.drawable.outline_tablet_24;
    }

    @Override // com.resync.app.ui.viewholders.onboarding.AbstractOnboardingViewHolder
    public int getInnerContentRes() {
        return R.layout.viewholder_inner_tablet;
    }

    @Override // com.resync.app.holders.OnboardingViewHolder
    public String getTitle() {
        return "Tablet setup";
    }

    @Override // com.resync.app.ui.viewholders.onboarding.AbstractOnboardingViewHolder
    protected void setup() {
        this.itemView.findViewById(R.id.tablet_content).getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.resync.app.ui.viewholders.onboarding.pages.TabletViewHolder.1
            @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
            public void onGlobalLayout() {
                TabletViewHolder.this.itemView.findViewById(R.id.tablet_content).getViewTreeObserver().removeOnGlobalLayoutListener(this);
                ContextHelper.getFragmentManager(TabletViewHolder.this.itemView.getContext()).beginTransaction().replace(R.id.tablet_content, PreferencesOnboardingTabletFragment.newInstance()).commit();
            }
        });
    }
}
