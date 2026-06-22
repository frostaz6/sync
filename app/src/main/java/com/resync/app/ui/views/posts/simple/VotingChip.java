package com.resync.app.ui.views.posts.simple;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.core.text.PrecomputedTextCompat;
import androidx.core.widget.TextViewCompat;
import com.resync.app.R;
import com.resync.app.RedditApplication;
import com.resync.app.singleton.AccountSingleton;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.views.text.BaseTextView;
import com.resync.app.helpers.ui.MaterialUtil;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.managers.VoteManager;
import com.resync.app.helpers.ConnectivityHelper;
import com.resync.app.helpers.PaddingHelper;
import com.resync.app.helpers.ScoreHelper;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.helpers.TypefaceManager;
import com.resync.app.ui.interfaces.DarkModeAware;
import com.resync.app.util.RippleUtils;
import com.resync.app.model.Post;

/* JADX INFO: loaded from: classes3.dex */
public class VotingChip extends AppCompatTextView implements DarkModeAware {
    private GradientDrawable background;

    private int currentBackgroundColor;
    private Post post;
    private int pendingVote;

    @SuppressLint({"UseCompatTextViewDrawableApis"})
    public VotingChip(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.pendingVote = 0;
        setup();
        setOnClickListener(new View.OnClickListener() {
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                VotingChip.this.onClicked(view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onClicked(View view) {
        int pendingVote = this.pendingVote;
        if (pendingVote == -1) {
            downvote();
        } else if (pendingVote == 1) {
            upvote();
        }
    }

    private void setup() {
        setTextSize(1, BaseTextView.getTextSize(SettingsSingleton.getSettings().slide_size_description));
        setTextColor(MonetUtil.getTertiaryTextColorAuto());
        setTypeface(TypefaceManager.get(13));
        setCompoundDrawablePadding(SizeHelper.dpToPx(8));
        setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getContext(), R.drawable.ic_navigate_previous_white_24dp), (Drawable) null, ContextCompat.getDrawable(getContext(), R.drawable.ic_navigate_next_white_24dp), (Drawable) null);
        setCompoundDrawableTintList(ColorStateList.valueOf(MonetUtil.getTertiaryTextColorAuto()));
        PaddingHelper.setPaddingHorizontal(this, 6, 6);
        GradientDrawable gradientDrawable = new GradientDrawable();
        this.background = gradientDrawable;
        gradientDrawable.setCornerRadius(MaterialUtil.getCornerRadius());
        if (MonetUtil.isAmoled()) {
            int strokeColor = ColorUtils.setAlphaComponent(MonetUtil.getPrimaryTextColor(true), 50);
            this.currentBackgroundColor = 0;
            this.background.setColor(0);
            this.background.setCornerRadius(MaterialUtil.getCornerRadius());
            this.background.setStroke(SizeHelper.dpToPx(1), strokeColor);
        } else {
            int cardColor = MonetUtil.getCardColorAuto();
            this.currentBackgroundColor = cardColor;
            this.background.setColor(cardColor);
        }
        setBackground(this.background);
        setForeground(RippleUtils.getRoundedCornersRipple(getContext()));
    }

    @Override // com.resync.app.ui.interfaces.DarkModeAware
    public void onDarkModeChanged() {
        setup();
    }

    public void downvote() {
        if (!AccountSingleton.getInstance().isLoggedIn()) {
            StaticToast.show(getContext(), R.string.common_generic_error_logged_out);
            return;
        }
        if (this.post.isArchived()) {
            StaticToast.show(getContext(), R.string.error_archived);
        } else if (ConnectivityHelper.isConnected(getContext())) {
            VoteManager.voteOnPost(getContext(), 0, this.post, -1);
        } else {
            StaticToast.show("You must be online to perform post actions");
        }
    }

    @Override // android.widget.TextView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (getForeground() instanceof RippleDrawable) {
            RippleDrawable rippleDrawable = (RippleDrawable) getForeground();
            rippleDrawable.setHotspot(motionEvent.getX(), motionEvent.getY());
            if (motionEvent.getX() > getWidth() / 2) {
                Post post = this.post;
                if (post == null || post.isDownvoted()) {
                    rippleDrawable.setColor(ColorStateList.valueOf(MonetUtil.getRippleColorAuto()));
                } else {
                    rippleDrawable.setColor(ColorStateList.valueOf(-7039745));
                }
                if (motionEvent.getAction() == 1) {
                    this.pendingVote = -1;
                }
            } else {
                Post post2 = this.post;
                if (post2 == null || post2.isUpvoted()) {
                    rippleDrawable.setColor(ColorStateList.valueOf(MonetUtil.getRippleColorAuto()));
                } else {
                    rippleDrawable.setColor(ColorStateList.valueOf(-35584));
                }
                if (motionEvent.getAction() == 1) {
                    this.pendingVote = 1;
                }
            }
        }
        return super.onTouchEvent(motionEvent);
    }

    @SuppressLint({"SetTextI18n"})
    public void bind(Post post) {
        this.post = post;
        setTextFuture(PrecomputedTextCompat.getTextFuture(ScoreHelper.formatCountCompact(post.getScore()), TextViewCompat.getTextMetricsParams(this), RedditApplication.threadPool));
        int textColor = MonetUtil.getTertiaryTextColorAuto();
        int backgroundColor = MonetUtil.isAmoled() ? 0 : MonetUtil.getCardColorAuto();
        if (post.isUpvoted()) {
            backgroundColor = 1442804992;
            textColor = -35584;
        } else if (post.isDownvoted()) {
            backgroundColor = 1435800831;
            textColor = -7039745;
        }
        if (backgroundColor != this.currentBackgroundColor) {
            setTextColor(textColor);
            this.background.setColor(backgroundColor);
            this.currentBackgroundColor = backgroundColor;
            if (post.isUpvoted()) {
                getCompoundDrawables()[0].setTint(-35584);
                getCompoundDrawables()[2].setTint(MonetUtil.getTertiaryTextColorAuto());
            } else if (post.isDownvoted()) {
                getCompoundDrawables()[0].setTint(MonetUtil.getTertiaryTextColorAuto());
                getCompoundDrawables()[2].setTint(-7039745);
            } else {
                getCompoundDrawables()[0].setTint(MonetUtil.getTertiaryTextColorAuto());
                getCompoundDrawables()[2].setTint(MonetUtil.getTertiaryTextColorAuto());
            }
        }
    }

    public void upvote() {
        if (!AccountSingleton.getInstance().isLoggedIn()) {
            StaticToast.show(getContext(), R.string.common_generic_error_logged_out);
            return;
        }
        if (this.post.isArchived()) {
            StaticToast.show(getContext(), R.string.error_archived);
        } else if (ConnectivityHelper.isConnected(getContext())) {
            VoteManager.voteOnPost(getContext(), 0, this.post, 1);
        } else {
            StaticToast.show("You must be online to perform post actions");
        }
    }
}
