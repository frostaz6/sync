package com.resync.app.ui.viewholders.sections.subreddits;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.ui.views.buttons.MoreButton;
import com.resync.app.ui.views.buttons.SaveButton;
import com.resync.app.ui.views.buttons.SortButton;
import com.resync.app.ui.views.buttons.ViewModeButton;
import com.resync.app.ui.views.core.SubView;

/* JADX INFO: loaded from: classes3.dex */
public class SubredditHolder_ViewBinding implements Unbinder {
    private SubredditHolder target;
    private View saveView;
    private View moreView;

    class SaveClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ SubredditHolder boundTarget;

        SaveClickListener(SubredditHolder subredditHolder) {
            this.boundTarget = subredditHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onSaveClicked();
        }
    }

    class MoreClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ SubredditHolder boundTarget;

        MoreClickListener(SubredditHolder subredditHolder) {
            this.boundTarget = subredditHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onMoreClicked(view);
        }
    }

    public SubredditHolder_ViewBinding(SubredditHolder subredditHolder, View view) {
        this.target = subredditHolder;
        subredditHolder.mSubView = (SubView) Utils.findRequiredViewAsType(view, R.id.subreddit_icon, "field 'mSubView'", SubView.class);
        subredditHolder.mCustomTextview = (TextView) Utils.findRequiredViewAsType(view, R.id.subreddit_textview, "field 'mCustomTextview'", TextView.class);
        subredditHolder.mDefaultTextview = Utils.findRequiredView(view, R.id.subreddit_textview_default, "field 'mDefaultTextview'");
        View viewC = Utils.findRequiredView(view, R.id.subreddit_save, "field 'mSaveButton' and method 'onSaveClicked'");
        subredditHolder.mSaveButton = (SaveButton) Utils.castView(viewC, R.id.subreddit_save, "field 'mSaveButton'", SaveButton.class);
        this.saveView = viewC;
        viewC.setOnClickListener(new SaveClickListener(subredditHolder));
        View viewC2 = Utils.findRequiredView(view, R.id.subreddit_more, "field 'mMoreButton' and method 'onMoreClicked'");
        subredditHolder.mMoreButton = (MoreButton) Utils.castView(viewC2, R.id.subreddit_more, "field 'mMoreButton'", MoreButton.class);
        this.moreView = viewC2;
        viewC2.setOnClickListener(new MoreClickListener(subredditHolder));
        subredditHolder.mSortButton = (SortButton) Utils.findRequiredViewAsType(view, R.id.subreddit_sort, "field 'mSortButton'", SortButton.class);
        subredditHolder.mViewModeButton = (ViewModeButton) Utils.findRequiredViewAsType(view, R.id.subreddit_view_mode, "field 'mViewModeButton'", ViewModeButton.class);
        subredditHolder.mActionsWrapper = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.subreddit_actions_wrapper, "field 'mActionsWrapper'", LinearLayout.class);
    }

    @Override
    public void unbind() {
        SubredditHolder target = this.target;
        if (target == null) throw new IllegalStateException("Bindings already cleared.");
        this.target = null;
        target.mSubView = null;
        target.mCustomTextview = null;
        target.mDefaultTextview = null;
        target.mSaveButton = null;
        target.mMoreButton = null;
        target.mSortButton = null;
        target.mViewModeButton = null;
        target.mActionsWrapper = null;
        this.saveView.setOnClickListener(null);
        this.saveView = null;
        this.moreView.setOnClickListener(null);
        this.moreView = null;
    }
}
