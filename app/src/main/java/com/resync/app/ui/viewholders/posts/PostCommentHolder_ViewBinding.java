package com.resync.app.ui.viewholders.posts;

import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.ui.views.text.spannable.children.HtmlTextView;
import com.resync.app.ui.views.text.spannable.children.comments.CommentDescriptionTextView;
import com.resync.app.ui.views.text.spannable.children.image_components.awards.AwardsTextView;
import com.resync.app.ui.views.text.spannable.children.posts.PostTitleTextView;

/* JADX INFO: loaded from: classes3.dex */
public class PostCommentHolder_ViewBinding implements Unbinder {
    private PostCommentHolder target;
    private View rootView;
    private View titleView;

    class RootClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ PostCommentHolder boundTarget;

        RootClickListener(PostCommentHolder postCommentHolder) {
            this.boundTarget = postCommentHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onRootClicked();
        }
    }

    class RootLongClickListener implements View.OnLongClickListener {

        final /* synthetic */ PostCommentHolder boundTarget;

        RootLongClickListener(PostCommentHolder postCommentHolder) {
            this.boundTarget = postCommentHolder;
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            return this.boundTarget.onRootLongClicked();
        }
    }

    class TitleClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ PostCommentHolder boundTarget;

        TitleClickListener(PostCommentHolder postCommentHolder) {
            this.boundTarget = postCommentHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onTitleClicked();
        }
    }

    public PostCommentHolder_ViewBinding(PostCommentHolder postCommentHolder, View view) {
        this.target = postCommentHolder;
        View viewC = Utils.findRequiredView(view, R.id.root, "method 'onRootClicked' and method 'onRootLongClicked'");
        postCommentHolder.root = viewC;
        this.rootView = viewC;
        viewC.setOnClickListener(new RootClickListener(postCommentHolder));
        viewC.setOnLongClickListener(new RootLongClickListener(postCommentHolder));
        View viewC2 = Utils.findRequiredView(view, R.id.title, "field 'title' and method 'onTitleClicked'");
        postCommentHolder.title = (PostTitleTextView) Utils.castView(viewC2, R.id.title, "field 'title'", PostTitleTextView.class);
        this.titleView = viewC2;
        viewC2.setOnClickListener(new TitleClickListener(postCommentHolder));
        postCommentHolder.description = (CommentDescriptionTextView) Utils.findRequiredViewAsType(view, R.id.description, "field 'description'", CommentDescriptionTextView.class);
        postCommentHolder.awards = (AwardsTextView) Utils.findRequiredViewAsType(view, R.id.awards, "field 'awards'", AwardsTextView.class);
        postCommentHolder.content = (HtmlTextView) Utils.findRequiredViewAsType(view, R.id.content, "field 'content'", HtmlTextView.class);
    }

    @Override
    public void unbind() {
        PostCommentHolder target = this.target;
        if (target == null) throw new IllegalStateException("Bindings already cleared.");
        this.target = null;
        this.rootView.setOnClickListener(null);
        this.rootView.setOnLongClickListener(null);
        this.rootView = null;
        this.titleView.setOnClickListener(null);
        this.titleView = null;
    }
}
