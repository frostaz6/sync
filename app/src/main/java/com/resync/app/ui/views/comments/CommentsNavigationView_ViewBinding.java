package com.resync.app.ui.views.comments;

import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.AppCompatImageView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;

/* JADX INFO: loaded from: classes3.dex */
public class CommentsNavigationView_ViewBinding implements Unbinder {
    private CommentsNavigationView target;

    public CommentsNavigationView_ViewBinding(CommentsNavigationView commentsNavigationView, View view) {
        this.target = commentsNavigationView;
        commentsNavigationView.mInnerWrapper = (ViewGroup) Utils.findRequiredViewAsType(view, R.id.inner_wrapper, "field 'mInnerWrapper'", ViewGroup.class);
        commentsNavigationView.mPrevious = (AppCompatImageView) Utils.findRequiredViewAsType(view, R.id.nav_previous, "field 'mPrevious'", AppCompatImageView.class);
        commentsNavigationView.mReply = (AppCompatImageView) Utils.findRequiredViewAsType(view, R.id.nav_reply, "field 'mReply'", AppCompatImageView.class);
        commentsNavigationView.mCollapse = (AppCompatImageView) Utils.findRequiredViewAsType(view, R.id.nav_collapse, "field 'mCollapse'", AppCompatImageView.class);
        commentsNavigationView.mNext = (AppCompatImageView) Utils.findRequiredViewAsType(view, R.id.nav_next, "field 'mNext'", AppCompatImageView.class);
        commentsNavigationView.mIcon = (AppCompatImageView) Utils.findRequiredViewAsType(view, R.id.nav_icon, "field 'mIcon'", AppCompatImageView.class);
    }

    @Override
    public void unbind() {
        CommentsNavigationView commentsNavigationView = this.target;
        if (commentsNavigationView == null) throw new IllegalStateException("Bindings already cleared.");
        this.target = null;
        commentsNavigationView.mInnerWrapper = null;
        commentsNavigationView.mPrevious = null;
        commentsNavigationView.mReply = null;
        commentsNavigationView.mCollapse = null;
        commentsNavigationView.mNext = null;
        commentsNavigationView.mIcon = null;
    }
}
