package com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base;

import android.graphics.Outline;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ProgressBar;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.resync.app.R;
import com.resync.app.RedditApplication;
import com.resync.app.ui.views.core.SheetHeaderView;
import com.resync.app.ui.views.monet.buttons.MonetActionButtons;
import com.resync.app.ui.views.text.BaseTextView;
import com.resync.app.helpers.ui.StaticToast;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.dialogs.CustomBottomSheetDialogFragment;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.dialogs.interfaces.MemeDialogBottomSheetInterface;

/* JADX INFO: loaded from: classes.dex */
public abstract class AbstractMemeBottomSheetFragment extends CustomBottomSheetDialogFragment implements MemeDialogBottomSheetInterface {

    @BindView(R.id.monet_action_buttons)
    MonetActionButtons buttons;

    @BindView(R.id.header)
    SheetHeaderView headerView;

    @BindView(R.id.image)
    ImageView iv;

    @BindView(R.id.image_progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.text)
    BaseTextView textView;

    class ImageLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {

        /* JADX INFO: renamed from: com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractMemeBottomSheetFragment$a$a, reason: collision with other inner class name */
        class DrawableRequestListener implements RequestListener<Drawable> {
            DrawableRequestListener() {
            }

            @Override // com.bumptech.glide.request.RequestListener
            /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
            public boolean onResourceReady(Drawable drawable, Object obj, Target<Drawable> target, DataSource dataSource, boolean z) {
                AbstractMemeBottomSheetFragment.this.progressBar.setVisibility(8);
                return false;
            }

            @Override // com.bumptech.glide.request.RequestListener
            public boolean onLoadFailed(GlideException glideException, Object obj, Target<Drawable> target, boolean z) {
                StaticToast.showWithContext(AbstractMemeBottomSheetFragment.this.getContext(), "Image failed to load");
                AbstractMemeBottomSheetFragment.this.dismiss();
                return false;
            }
        }

        ImageLayoutListener() {
        }

        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            AbstractMemeBottomSheetFragment.this.iv.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            AbstractMemeBottomSheetFragment.this.iv.getLayoutParams().height = (int) (((float) AbstractMemeBottomSheetFragment.this.iv.getWidth() / AbstractMemeBottomSheetFragment.this.getImageWidth()) * AbstractMemeBottomSheetFragment.this.getImageHeight());
            AbstractMemeBottomSheetFragment.this.iv.requestLayout();
            com.bumptech.glide.Glide.with(RedditApplication.getAppContext()).load(AbstractMemeBottomSheetFragment.this.getImageUrl()).listener(new DrawableRequestListener()).into(AbstractMemeBottomSheetFragment.this.iv);
        }
    }

    class RoundedOutlineProvider extends ViewOutlineProvider {
        RoundedOutlineProvider() {
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            outline.setRoundRect(0, 0, view.getWidth(), view.getHeight() + SizeHelper.dpToPx(12), SizeHelper.dpToPx(12));
        }
    }

    @Override // androidx.fragment.app.Fragment
    /* JADX INFO: renamed from: U1 */
    public void onDestroy() {
        com.bumptech.glide.Glide.with(RedditApplication.getAppContext()).clear(this.iv);
        super.onDestroy();
    }

    public String getPositiveButtonText() {
        return null;
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public boolean isInitiallyExpanded() {
        return true;
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetInterface
    public int getLayoutRes() {
        return R.layout.fragment_abstract_meme_bottomsheet;
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        ButterKnife.bind(this, view);
        this.headerView.setTitle(getMemeTitle());
        this.buttons.setButtonLabels(getPositiveButtonText(), getNegativeButtonText());
        this.buttons.setListeners(new View.OnClickListener() { // from class: u9.i
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                AbstractMemeBottomSheetFragment.this.onPositiveButtonClicked(view2);
            }
        }, new View.OnClickListener() { // from class: u9.h
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                AbstractMemeBottomSheetFragment.this.onNegativeButtonClicked(view2);
            }
        });
        this.iv.getViewTreeObserver().addOnGlobalLayoutListener(new ImageLayoutListener());
        if (StringUtils.isNotEmpty(getBodyText())) {
            this.textView.setText(getBodyText());
            this.textView.setVisibility(0);
        }
        if (onProgressReady()) {
            this.headerView.setVisibility(8);
            this.iv.setOutlineProvider(new RoundedOutlineProvider());
            this.iv.setClipToOutline(true);
        }
    }

    public String getNegativeButtonText() {
        return null;
    }

    public String getBodyText() {
        return null;
    }

    public boolean onProgressReady() {
        return false;
    }

    public void onNegativeButtonClicked(View view) {
        dismiss();
    }

    public void onPositiveButtonClicked(View view) {
        dismiss();
    }
}
