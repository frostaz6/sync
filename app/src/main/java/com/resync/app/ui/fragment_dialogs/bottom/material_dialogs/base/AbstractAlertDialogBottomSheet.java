package com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.Property;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.resync.app.R;
import com.resync.app.ui.views.core.SheetHeaderView;
import com.resync.app.ui.views.monet.buttons.MonetActionButtons;
import com.resync.app.ui.views.responsive.CustomProgressBar;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.singletons.StyleSingleton;
import com.resync.app.dialogs.CustomBottomSheetDialogFragment;
import com.resync.app.helpers.TypefaceManager;
import com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface;
import com.resync.app.util.Logger;

/* JADX INFO: loaded from: classes.dex */
public abstract class AbstractAlertDialogBottomSheet extends CustomBottomSheetDialogFragment implements AlertDialogBottomSheetInterface {

    @BindView(R.id.abstract_alert_dialog_bottom_sheet_root_wrapper)
    RelativeLayout mAsyncRootWrapper;

    @BindView(R.id.abstract_alert_dialog_bottom_sheet_async_wrapper)
    ViewGroup mAsyncWrapper;

    @BindView(R.id.abstract_alert_dialog_bottom_sheet_content)
    LinearLayout mContent;

    @BindView(R.id.abstract_alert_dialog_bottom_sheet_main_wrapper)
    ViewGroup mMainWrapper;

    @BindView(R.id.abstract_alert_dialog_bottom_sheet_message)
    TextView mMessageTextView;

    @BindView(R.id.monet_action_buttons)
    MonetActionButtons mMonetActionButtons;

    @BindView(R.id.abstract_alert_dialog_bottom_sheet_async_progress_bar)
    CustomProgressBar mProgressBar;

    @BindView(R.id.abstract_alert_dialog_bottom_sheet_header)
    SheetHeaderView mSectionHeaderView;

    class HeightAnimatorListener implements ValueAnimator.AnimatorUpdateListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ int targetHeight;

        HeightAnimatorListener(int targetHeight) {
            this.targetHeight = targetHeight;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            int iIntValue = ((Integer) valueAnimator.getAnimatedValue()).intValue();
            if (iIntValue == this.targetHeight) {
                ViewGroup.LayoutParams layoutParams = AbstractAlertDialogBottomSheet.this.mAsyncRootWrapper.getLayoutParams();
                layoutParams.height = -2;
                AbstractAlertDialogBottomSheet.this.mAsyncRootWrapper.setLayoutParams(layoutParams);
            } else {
                ViewGroup.LayoutParams layoutParams2 = AbstractAlertDialogBottomSheet.this.mAsyncRootWrapper.getLayoutParams();
                layoutParams2.height = iIntValue;
                AbstractAlertDialogBottomSheet.this.mAsyncRootWrapper.setLayoutParams(layoutParams2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onNegativeClickInternal(View view) {
        onNegativeAction();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onNeutralClickInternal(View view) {
        onNeutralAction();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPositiveClickInternal(View view) {
        onPositiveButtonClicked();
    }

    public void showResult(String str) {
        setMessageText(str);
        this.mAsyncWrapper.setVisibility(8);
        this.mMainWrapper.setVisibility(0);
        int height = this.mAsyncRootWrapper.getHeight();
        this.mAsyncRootWrapper.measure(0, 0);
        int measuredHeight = this.mAsyncRootWrapper.getMeasuredHeight();
        Logger.verbose("Previous height: " + height);
        Logger.verbose("New height: " + measuredHeight);
        if (!isAsync() || measuredHeight == 0 || height == 0 || measuredHeight <= height) {
            return;
        }
        ValueAnimator valueAnimatorOfInt = ValueAnimator.ofInt(height, measuredHeight);
        valueAnimatorOfInt.addUpdateListener(new HeightAnimatorListener(measuredHeight));
        this.mMainWrapper.setAlpha(0.0f);
        ObjectAnimator objectAnimatorOfFloat = ObjectAnimator.ofFloat(this.mMainWrapper, (Property<View, Float>) View.ALPHA, 1.0f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(220L);
        animatorSet.setInterpolator(new FastOutSlowInInterpolator());
        animatorSet.playTogether(valueAnimatorOfInt, objectAnimatorOfFloat);
        animatorSet.start();
    }

    public void onContentSetup() {
    }

    protected void setMessageText(String str) {
        if (StringUtils.isNotEmpty(str)) {
            this.mMessageTextView.setText(str);
            this.mMessageTextView.setVisibility(0);
        }
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public boolean isInitiallyExpanded() {
        return true;
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetInterface
    public int getLayoutRes() {
        return R.layout.abstract_alert_dialog_bottom_sheet;
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        ButterKnife.bind(this, view);
        if (isAsync()) {
            this.mAsyncWrapper.setVisibility(0);
            this.mMainWrapper.setVisibility(8);
        } else {
            showResult(getMessage());
        }
        this.mProgressBar.applyColors(isNightMode());
        this.mSectionHeaderView.applyColors(isNightMode());
        this.mMonetActionButtons.applyColors(isNightMode());
        this.mMessageTextView.setTextColor(StyleSingleton.getTextColor(getContext(), false, isNightMode()));
        this.mMessageTextView.setTextSize(1, 14.0f);
        this.mMessageTextView.setTypeface(TypefaceManager.get(9));
        this.mSectionHeaderView.setTitle(getTitle());
        this.mMonetActionButtons.setButtonLabels(getPositiveButtonText(), getNeutralButtonText(), getNegativeButtonText());
        this.mMonetActionButtons.setListeners(new View.OnClickListener() { // from class: u9.b
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                AbstractAlertDialogBottomSheet.this.onPositiveClickInternal(view2);
            }
        }, new View.OnClickListener() { // from class: u9.c
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                AbstractAlertDialogBottomSheet.this.onNeutralClickInternal(view2);
            }
        }, new View.OnClickListener() { // from class: u9.a
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                AbstractAlertDialogBottomSheet.this.onNegativeClickInternal(view2);
            }
        });
        onContentSetup();
    }

    public void addContentLayout(int layoutRes) {
        this.mContent.addView(LayoutInflater.from(getContext()).inflate(layoutRes, (ViewGroup) this.mContent, false), 0);
    }

    public String getMessage() {
        return null;
    }

    public void onNegativeAction() {
        dismiss();
    }

    public String getNegativeButtonText() {
        return "Cancel";
    }

    public String getNeutralButtonText() {
        return null;
    }

    public boolean isAsync() {
        return false;
    }

    public void onNeutralAction() {
    }
}
