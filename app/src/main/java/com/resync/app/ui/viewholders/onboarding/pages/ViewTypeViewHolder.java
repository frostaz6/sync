package com.resync.app.ui.viewholders.onboarding.pages;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.resync.app.R;
import com.resync.app.singleton.UiSingleton;
import com.resync.app.ui.viewholders.onboarding.AbstractOnboardingViewHolder;
import com.resync.app.ui.views.onboarding.OnboardingFrameLayout;
import com.resync.app.ui.views.onboarding.OnboardingPostTypeLinearLayout;
import com.resync.app.ui.views.responsive.chips.ContentChip;

/* JADX INFO: loaded from: classes3.dex */
public class ViewTypeViewHolder extends AbstractOnboardingViewHolder {
    ContentChip chipCards;
    ContentChip chipCompact;
    ContentChip chipList;
    ContentChip chipSlides;
    ContentChip chipSmallCards;
    ContentChip chipSmallerCards;
    OnboardingFrameLayout previewWrapper;
    TextView typeDescription;
    OnboardingPostTypeLinearLayout wrapper;

    public ViewTypeViewHolder(ViewGroup viewGroup) {
        super(viewGroup);
    }

    private void setupChip() {
        this.chipSlides.setSelected(UiSingleton.getInstance().getUiMode() == 5);
        this.chipCards.setSelected(UiSingleton.getInstance().getUiMode() == 4);
        this.chipSmallCards.setSelected(UiSingleton.getInstance().getUiMode() == 3);
        this.chipSmallerCards.setSelected(UiSingleton.getInstance().getUiMode() == 2);
        this.chipCompact.setSelected(UiSingleton.getInstance().getUiMode() == 1);
        this.chipList.setSelected(UiSingleton.getInstance().getUiMode() == 0);
        this.wrapper.buildPreview();
        if (UiSingleton.getInstance().getUiMode() == 5) {
            this.typeDescription.setText("The best way to enjoy ReSync. Enjoy full sized images, autoplaying videos and more.");
            return;
        }
        if (UiSingleton.getInstance().getUiMode() == 4) {
            this.typeDescription.setText("The classic view for ReSync. Also featuring autoplaying videos and large image previews.");
            return;
        }
        if (UiSingleton.getInstance().getUiMode() == 3) {
            this.typeDescription.setText("Similar to cards but with small image previews.");
            return;
        }
        if (UiSingleton.getInstance().getUiMode() == 2) {
            this.typeDescription.setText("For the space saver.");
        } else if (UiSingleton.getInstance().getUiMode() == 1) {
            this.typeDescription.setText("For the space saver.");
        } else if (UiSingleton.getInstance().getUiMode() == 0) {
            this.typeDescription.setText("For the space saver.");
        }
    }

    @Override // com.resync.app.ui.viewholders.onboarding.AbstractOnboardingViewHolder
    public String getDescription() {
        return super.getDescription();
    }

    @Override // com.resync.app.holders.OnboardingViewHolder
    public int getIconRes() {
        return R.drawable.outline_view_agenda_24;
    }

    @Override // com.resync.app.ui.viewholders.onboarding.AbstractOnboardingViewHolder
    public int getInnerContentRes() {
        return R.layout.viewholder_inner_view_type;
    }

    @Override // com.resync.app.holders.OnboardingViewHolder
    public String getTitle() {
        return "Post view type";
    }

    public void onChipClicked(View view) {
        if (view.equals(this.chipSlides)) {
            UiSingleton.getInstance().setUiMode(5);
        } else if (view.equals(this.chipCards)) {
            UiSingleton.getInstance().setUiMode(4);
        } else if (view.equals(this.chipSmallCards)) {
            UiSingleton.getInstance().setUiMode(3);
        } else if (view.equals(this.chipSmallerCards)) {
            UiSingleton.getInstance().setUiMode(2);
        } else if (view.equals(this.chipCompact)) {
            UiSingleton.getInstance().setUiMode(1);
        } else if (view.equals(this.chipList)) {
            UiSingleton.getInstance().setUiMode(0);
        }
        setupChip();
    }

    @Override // com.resync.app.ui.viewholders.onboarding.AbstractOnboardingViewHolder
    protected void setup() {
        OnboardingFrameLayout onboardingFrameLayout = (OnboardingFrameLayout) this.itemView.findViewById(R.id.preview_wrapper);
        this.previewWrapper = onboardingFrameLayout;
        onboardingFrameLayout.disableTouch();
        this.wrapper = (OnboardingPostTypeLinearLayout) this.itemView.findViewById(R.id.preview_inner);
        this.typeDescription = (TextView) this.itemView.findViewById(R.id.post_type_description);
        ContentChip contentChip = new ContentChip(this.itemView.getContext());
        this.chipSlides = contentChip;
        contentChip.setText("Slides");
        this.chipSlides.setOnClickListener(new View.OnClickListener() { // from class: sb.f
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ViewTypeViewHolder.this.onChipClicked(view);
            }
        });
        this.chipGroup.addView(this.chipSlides);
        ContentChip contentChip2 = new ContentChip(this.itemView.getContext());
        this.chipCards = contentChip2;
        contentChip2.setText("Cards");
        this.chipCards.setOnClickListener(new View.OnClickListener() { // from class: sb.f
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ViewTypeViewHolder.this.onChipClicked(view);
            }
        });
        this.chipGroup.addView(this.chipCards);
        ContentChip contentChip3 = new ContentChip(this.itemView.getContext());
        this.chipSmallCards = contentChip3;
        contentChip3.setText("Small cards");
        this.chipSmallCards.setOnClickListener(new View.OnClickListener() { // from class: sb.f
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ViewTypeViewHolder.this.onChipClicked(view);
            }
        });
        this.chipGroup.addView(this.chipSmallCards);
        ContentChip contentChip4 = new ContentChip(this.itemView.getContext());
        this.chipSmallerCards = contentChip4;
        contentChip4.setText("Smaller cards");
        this.chipSmallerCards.setOnClickListener(new View.OnClickListener() { // from class: sb.f
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ViewTypeViewHolder.this.onChipClicked(view);
            }
        });
        this.chipGroup.addView(this.chipSmallerCards);
        ContentChip contentChip5 = new ContentChip(this.itemView.getContext());
        this.chipCompact = contentChip5;
        contentChip5.setText("Compact");
        this.chipCompact.setOnClickListener(new View.OnClickListener() { // from class: sb.f
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ViewTypeViewHolder.this.onChipClicked(view);
            }
        });
        this.chipGroup.addView(this.chipCompact);
        ContentChip contentChip6 = new ContentChip(this.itemView.getContext());
        this.chipList = contentChip6;
        contentChip6.setText("List");
        this.chipList.setOnClickListener(new View.OnClickListener() { // from class: sb.f
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ViewTypeViewHolder.this.onChipClicked(view);
            }
        });
        this.chipGroup.addView(this.chipList);
        setupChip();
    }

    @Override // com.resync.app.ui.viewholders.onboarding.AbstractOnboardingViewHolder
    public boolean showChipGroup() {
        return true;
    }
}
