package com.resync.app.ui.viewholders.sections.subreddits;

import android.view.View;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatImageView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;

/* JADX INFO: loaded from: classes3.dex */
public class SubredditHeaderHolder_ViewBinding implements Unbinder {
    private SubredditHeaderHolder target;
    private View moreView;
    private View rowView;

    class MoreClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ SubredditHeaderHolder boundTarget;

        MoreClickListener(SubredditHeaderHolder subredditHeaderHolder) {
            this.boundTarget = subredditHeaderHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onMoreClicked();
        }
    }

    class RowClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ SubredditHeaderHolder boundTarget;

        RowClickListener(SubredditHeaderHolder subredditHeaderHolder) {
            this.boundTarget = subredditHeaderHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onRowClicked();
        }
    }

    public SubredditHeaderHolder_ViewBinding(SubredditHeaderHolder subredditHeaderHolder, View view) {
        this.target = subredditHeaderHolder;
        subredditHeaderHolder.mHeaderTextView = (TextView) Utils.findRequiredViewAsType(view, R.id.holder_subreddit_header_textview, "field 'mHeaderTextView'", TextView.class);
        View viewC = Utils.findRequiredView(view, R.id.holder_subreddit_header_more, "field 'mMoreText' and method 'onMoreClicked'");
        subredditHeaderHolder.mMoreText = (TextView) Utils.castView(viewC, R.id.holder_subreddit_header_more, "field 'mMoreText'", TextView.class);
        this.moreView = viewC;
        viewC.setOnClickListener(new MoreClickListener(subredditHeaderHolder));
        subredditHeaderHolder.mIndicatorIcon = (AppCompatImageView) Utils.findRequiredViewAsType(view, R.id.holder_subreddit_header_icon, "field 'mIndicatorIcon'", AppCompatImageView.class);
        View viewC2 = Utils.findRequiredView(view, R.id.holder_subreddit_header_row, "method 'onRowClicked'");
        this.rowView = viewC2;
        viewC2.setOnClickListener(new RowClickListener(subredditHeaderHolder));
    }

    @Override
    public void unbind() {
        SubredditHeaderHolder target = this.target;
        if (target == null) throw new IllegalStateException("Bindings already cleared.");
        this.target = null;
        target.mHeaderTextView = null;
        target.mMoreText = null;
        target.mIndicatorIcon = null;
        this.moreView.setOnClickListener(null);
        this.moreView = null;
        this.rowView.setOnClickListener(null);
        this.rowView = null;
    }
}
