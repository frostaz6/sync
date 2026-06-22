package com.resync.app.ui.viewholders.posts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.resync.app.R;
import com.resync.app.singleton.UiSingleton;
import com.resync.app.ui.views.posts.simple.SimpleLinearLayout;
import com.resync.app.ui.views.responsive.CustomMaterialCardView;
import com.resync.app.ui.views.text.spannable.children.HtmlTextView;
import com.resync.app.ui.views.text.spannable.children.comments.CommentDescriptionTextView;
import com.resync.app.ui.views.text.spannable.children.image_components.awards.AwardsTextView;
import com.resync.app.ui.views.text.spannable.children.posts.PostTitleTextView;
import com.resync.app.text.html.Options;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.helpers.PaddingHelper;
import com.resync.app.dialogs.CommentMoreDialogBottomSheet;
import com.resync.app.holders.BasePostViewHolder;
import com.resync.app.util.Logger;
import com.resync.app.model.Post;
import com.resync.app.util.links.LinkHelper;
import com.resync.app.presenters.BaseCursorPresenter;

/* JADX INFO: loaded from: classes3.dex */
public class PostCommentHolder extends BasePostViewHolder {

    AwardsTextView awards;

    HtmlTextView content;

    CommentDescriptionTextView description;

    View root;

    PostTitleTextView title;

    public PostCommentHolder(Context context, BaseCursorPresenter baseCursorPresenter, View view, int uiMode) {
        super(context, view, uiMode);
        setPresenter(baseCursorPresenter);
        this.title.setVisitedDimDisabled(true);
        if (getUiMode() == 4 && UiSingleton.isFullWidth(getUiMode())) {
            PaddingHelper.setPaddingHorizontal((View) this.title.getParent(), 6, 6);
            View view2 = this.root;
            if (view2 instanceof CustomMaterialCardView) {
                ((CustomMaterialCardView) view2).setRadius(0.0f);
            }
        }
    }

    public static PostCommentHolder create(Context context, ViewGroup viewGroup, BaseCursorPresenter baseCursorPresenter, int uiMode) {
        return new PostCommentHolder(context, baseCursorPresenter, LayoutInflater.from(context).inflate(layoutForMode(uiMode), viewGroup, false), uiMode);
    }

    private static int layoutForMode(int uiMode) {
        if (uiMode == 5) {
            return R.layout.holder_post_comment_simple;
        }
        if (uiMode == 0) {
            return R.layout.holder_post_comment_list;
        }
        if (uiMode == 1) {
            return R.layout.holder_post_comment_compact;
        }
        if (uiMode == 2) {
            return R.layout.holder_post_comment_smaller_cards;
        }
        if (uiMode == 3 || uiMode == 4) {
            return R.layout.holder_post_comment_cards;
        }
        throw new RuntimeException("Unsupported mode: " + uiMode);
    }

    @Override // com.resync.app.holders.BasePostViewHolder
    public void bindPost(Post post, int position) {
        View view = this.root;
        if (view instanceof SimpleLinearLayout) {
            ((SimpleLinearLayout) view).setPosition(getLayoutPosition());
        }
        if (getPost() != null && post != null && post.equals(getPost()) && post.hasSameUserTag(getPost())) {
            Logger.verbose("BAILED SimpleHolder");
            return;
        }
        super.bindPost(post, position);
        this.title.bind(post);
        this.description.bind(post, null, true);
        this.awards.bind(post);
        this.content.setHtmlWithOptions(new Options().withPost(post), getPost().getBodyProcessed());
    }

    public void onRootClicked() {
        getPresenter().openComments(getPost());
    }

    public boolean onRootLongClicked() {
        FragmentUtils.showWithArguments(CommentMoreDialogBottomSheet.class, getFragmentManager(), CommentMoreDialogBottomSheet.buildArgs(getPost(), false));
        return true;
    }

    public void onTitleClicked() {
        LinkHelper.open(this.context, "/r/" + getPost().getSubreddit() + "/comments/" + getPost().getLinkId());
    }
}
