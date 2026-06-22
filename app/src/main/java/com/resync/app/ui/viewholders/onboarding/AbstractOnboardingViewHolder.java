package com.resync.app.ui.viewholders.onboarding;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.ButterKnife;
import com.google.android.material.chip.ChipGroup;
import com.resync.app.R;
import com.resync.app.ui.views.text.basic.ProductSansTextView;
import com.resync.app.ui.views.util.ConsumingHorizontalScrollView;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.ui.TabletHelper;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.holders.OnboardingViewHolder;

/* JADX INFO: loaded from: classes3.dex */
public abstract class AbstractOnboardingViewHolder extends RecyclerView.ViewHolder implements OnboardingViewHolder {

    protected ChipGroup chipGroup;

    protected ConsumingHorizontalScrollView chipScroll;

    ProductSansTextView description;

    ImageView icon;

    LinearLayout innerLayout;

    ProductSansTextView title;

    public AbstractOnboardingViewHolder(ViewGroup viewGroup) {
        super(inflateBase(viewGroup));
        ButterKnife.bind(this, this.itemView);
        if (this.itemView.getContext().getResources().getBoolean(R.bool.tablet) && TabletHelper.isLandscape() && applyTabletPadding()) {
            this.itemView.findViewById(R.id.left_space).setVisibility(0);
            this.itemView.findViewById(R.id.right_space).setVisibility(0);
        }
        if (this.icon != null && getIconRes() != 0) {
            this.icon.setImageResource(getIconRes());
            this.icon.setImageTintList(ColorStateList.valueOf(MonetUtil.getLinkColorAuto()));
        }
        if (this.title != null) {
            if (StringUtils.isNotEmpty(getTitle())) {
                this.title.setText(getTitle());
            } else {
                this.title.setVisibility(8);
            }
        }
        if (this.description != null) {
            if (StringUtils.isNotEmpty(getDescription())) {
                this.description.setText(getDescription());
            } else {
                this.description.setVisibility(8);
            }
        }
        if (this.chipScroll != null && !showChipGroup()) {
            this.chipScroll.setVisibility(8);
        }
        if (getInnerContentRes() != 0) {
            LayoutInflater.from(viewGroup.getContext()).inflate(getInnerContentRes(), (ViewGroup) this.innerLayout, true);
        }
        setup();
    }

    public static View inflateBase(ViewGroup viewGroup) {
        return LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.viewholder_abstract_onboarding, viewGroup, false);
    }

    public boolean applyTabletPadding() {
        return true;
    }

    public void bind() {
    }

    public String getDescription() {
        return null;
    }

    public int getInnerContentRes() {
        return 0;
    }

    protected void setup() {
    }

    public boolean showChipGroup() {
        return false;
    }
}
