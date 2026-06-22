package com.resync.app.ui.viewholders.onboarding;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.google.android.material.chip.ChipGroup;
import com.resync.app.R;
import com.resync.app.ui.views.text.basic.ProductSansTextView;
import com.resync.app.ui.views.util.ConsumingHorizontalScrollView;

/* JADX INFO: loaded from: classes3.dex */
public class AbstractOnboardingViewHolder_ViewBinding implements Unbinder {
    private AbstractOnboardingViewHolder target;

    public AbstractOnboardingViewHolder_ViewBinding(AbstractOnboardingViewHolder abstractOnboardingViewHolder, View view) {
        this.target = abstractOnboardingViewHolder;
        abstractOnboardingViewHolder.icon = (ImageView) Utils.findOptionalViewAsType(view, R.id.icon, "field 'icon'", ImageView.class);
        abstractOnboardingViewHolder.title = (ProductSansTextView) Utils.findOptionalViewAsType(view, R.id.title, "field 'title'", ProductSansTextView.class);
        abstractOnboardingViewHolder.description = (ProductSansTextView) Utils.findOptionalViewAsType(view, R.id.description, "field 'description'", ProductSansTextView.class);
        abstractOnboardingViewHolder.innerLayout = (LinearLayout) Utils.findOptionalViewAsType(view, R.id.inner, "field 'innerLayout'", LinearLayout.class);
        abstractOnboardingViewHolder.chipScroll = (ConsumingHorizontalScrollView) Utils.findOptionalViewAsType(view, R.id.chip_scroll, "field 'chipScroll'", ConsumingHorizontalScrollView.class);
        abstractOnboardingViewHolder.chipGroup = (ChipGroup) Utils.findOptionalViewAsType(view, R.id.chip_group, "field 'chipGroup'", ChipGroup.class);
    }

    @Override
    public void unbind() {
        AbstractOnboardingViewHolder target = this.target;
        if (target == null) throw new IllegalStateException("Bindings already cleared.");
        this.target = null;
    }
}
