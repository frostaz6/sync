package com.resync.app.ui.viewholders.sections.search;

import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.ui.views.core.SubView;
import com.resync.app.util.DebouncingOnClickListener;

/* JADX INFO: loaded from: classes3.dex */
public class SearchItemHolder_ViewBinding implements Unbinder {
    private SearchItemHolder target;
    private View moreView;

    class MoreClickListener extends DebouncingOnClickListener {

        final /* synthetic */ SearchItemHolder boundTarget;

        MoreClickListener(SearchItemHolder searchItemHolder) {
            this.boundTarget = searchItemHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onMoreClicked(view);
        }
    }

    public SearchItemHolder_ViewBinding(SearchItemHolder searchItemHolder, View view) {
        this.target = searchItemHolder;
        searchItemHolder.mSubIconView = (SubView) Utils.findRequiredViewAsType(view, R.id.subreddit_icon, "field 'mSubIconView'", SubView.class);
        searchItemHolder.mSubTextView = (TextView) Utils.findRequiredViewAsType(view, R.id.subreddit_textview, "field 'mSubTextView'", TextView.class);
        searchItemHolder.mSubscriberTextView = (TextView) Utils.findRequiredViewAsType(view, R.id.count_textview, "field 'mSubscriberTextView'", TextView.class);
        View viewC = Utils.findRequiredView(view, R.id.subreddit_more, "method 'onMoreClicked'");
        this.moreView = viewC;
        viewC.setOnClickListener(new MoreClickListener(searchItemHolder));
    }

    @Override
    public void unbind() {
        SearchItemHolder target = this.target;
        if (target == null) throw new IllegalStateException("Bindings already cleared.");
        this.target = null;
        target.mSubIconView = null;
        target.mSubTextView = null;
        target.mSubscriberTextView = null;
        this.moreView.setOnClickListener(null);
        this.moreView = null;
    }
}
