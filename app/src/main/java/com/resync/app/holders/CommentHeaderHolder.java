package com.resync.app.holders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.ButterKnife;
import com.resync.app.R;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.singleton.UiSingleton;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.widgets.SimpleExoPlayerTextureView;
import com.resync.app.media.images.SyncTypeHelper;
import com.resync.app.helpers.TypefaceManager;
import com.resync.app.holders.BasePostViewHolder;
import com.resync.app.presenters.CommentsPresenter;

/* JADX INFO: renamed from: pb.a */
/* JADX INFO: compiled from: CommentHeaderHolder.java */
/* JADX INFO: loaded from: classes3.dex */
public class CommentHeaderHolder extends BasePostViewHolder {

    RecyclerView.ViewHolder postHolder;
    TextView notificationBar;

    public CommentHeaderHolder(Context context, View view, CommentsPresenter commentsPresenter) {
        super(context, view);
        ButterKnife.bind(this, view);
        int displayMode = UiSingleton.getInstance().getUiMode();
        displayMode = (displayMode == 0 || displayMode == 1 || displayMode == 2) ? 5 : displayMode;
        displayMode = displayMode == 3 ? 4 : displayMode;
        if (displayMode == 5) {
            ViewGroup viewGroup = (ViewGroup) view;
            RecyclerView.ViewHolder previewHolder = SyncTypeHelper.createSimpleHolder(context, viewGroup, commentsPresenter, SyncTypeHelper.getSimpleViewType(commentsPresenter.getPost().getSyncType()));
            this.postHolder = previewHolder;
            ((BasePostViewHolder) previewHolder).setPreviewMode();
            viewGroup.addView(this.postHolder.itemView, 0);
        } else if (displayMode == 4) {
            int cardViewType = SyncTypeHelper.getCardViewType(commentsPresenter.getPost().getSyncType());
            ViewGroup viewGroup2 = (ViewGroup) view;
            RecyclerView.ViewHolder cardHolder = SyncTypeHelper.createCardHolder(context, viewGroup2, commentsPresenter, (cardViewType == 1 && SettingsSingleton.getSettings().commentMediaThumbnail) ? 3 : cardViewType, displayMode);
            this.postHolder = cardHolder;
            ((BasePostViewHolder) cardHolder).setPreviewMode();
            viewGroup2.addView(this.postHolder.itemView, 0);
        }
        ViewGroup viewGroup3 = (ViewGroup) view;
        View viewInflate = LayoutInflater.from(context).inflate(R.layout.comment_banner, viewGroup3, false);
        this.notificationBar = (TextView) viewInflate.findViewById(R.id.fragment_comments_notification_bar);
        viewGroup3.addView(viewInflate, 0);
    }

    private void hideNotification() {
        this.notificationBar.setVisibility(8);
    }

    public static CommentHeaderHolder create(Context context, ViewGroup viewGroup, CommentsPresenter commentsPresenter) {
        return new CommentHeaderHolder(context, LayoutInflater.from(context).inflate(R.layout.holder_comment_header, viewGroup, false), commentsPresenter);
    }

    private void showNotification(String str) {
        int backgroundColor = MonetUtil.getAccent2BackgroundColorAuto();
        int textColor = MonetUtil.getAccent2TextColorAuto();
        this.notificationBar.setVisibility(0);
        this.notificationBar.setBackgroundColor(backgroundColor);
        this.notificationBar.setText(str);
        this.notificationBar.setTextColor(textColor);
        this.notificationBar.setTextSize(1, 12.0f);
        this.notificationBar.setTypeface(TypefaceManager.get(11));
    }

    @Override // com.resync.app.holders.BasePostViewHolder, com.resync.app.holders.RedditViewHolder
    public void recycle() {
        super.recycle();
        RecyclerView.ViewHolder holder = this.postHolder;
        if (holder instanceof BasePostViewHolder) {
            ((BasePostViewHolder) holder).recycle();
        }
    }

    @Override // com.resync.app.holders.BasePostViewHolder
    public boolean canAutoplay() {
        RecyclerView.ViewHolder holder = this.postHolder;
        return (holder == null || !(holder instanceof BasePostViewHolder)) ? super.canAutoplay() : ((BasePostViewHolder) holder).canAutoplay();
    }

    @Override // com.resync.app.holders.BasePostViewHolder
    public SimpleExoPlayerTextureView getTextureView() {
        RecyclerView.ViewHolder holder = this.postHolder;
        return (holder == null || !(holder instanceof BasePostViewHolder)) ? super.getTextureView() : ((BasePostViewHolder) holder).getTextureView();
    }

    @Override // com.resync.app.holders.BasePostViewHolder
    public void attachTextureView(SimpleExoPlayerTextureView simpleExoPlayerTextureView) {
        RecyclerView.ViewHolder holder = this.postHolder;
        if (holder != null && (holder instanceof BasePostViewHolder)) {
            ((BasePostViewHolder) holder).attachTextureView(simpleExoPlayerTextureView);
        }
        super.attachTextureView(simpleExoPlayerTextureView);
    }

    public void bind(CommentsPresenter commentsPresenter) {
        if (commentsPresenter == null || commentsPresenter.getPost() == null) {
            return;
        }
        RecyclerView.ViewHolder holder = this.postHolder;
        if (holder instanceof BasePostViewHolder) {
            ((BasePostViewHolder) holder).bindPost(commentsPresenter.getPost(), 0);
        }
        if (commentsPresenter.isNp()) {
            showNotification("You have been linked to a read-only version\nPlease respect the community by not voting");
            return;
        }
        if (commentsPresenter.isPostDeleted()) {
            showNotification("This is an archived post.\nYou will not be able to vote or comment");
            return;
        }
        if (commentsPresenter.isPostLocked()) {
            showNotification("This is a locked post.\nYou can vote but cannot comment");
        } else if (commentsPresenter.isPostSpoiler()) {
            showNotification("This thread is in contest mode\nContest mode randomizes comment sorting and hides scores");
        } else {
            hideNotification();
        }
    }
}
