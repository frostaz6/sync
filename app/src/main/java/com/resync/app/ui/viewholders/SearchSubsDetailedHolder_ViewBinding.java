package com.resync.app.ui.viewholders;

import android.view.View;
import android.widget.LinearLayout;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.ui.views.core.SubView;
import com.resync.app.ui.views.responsive.CustomMaterialCardView;
import com.resync.app.ui.views.text.BaseTextView;
import com.resync.app.ui.views.text.spannable.children.HtmlTextView;
import com.resync.app.ui.views.text.spannable.children.other.SearchSubsDetailedDescription;
import com.resync.app.util.DebouncingOnClickListener;

/* JADX INFO: loaded from: classes3.dex */
public class SearchSubsDetailedHolder_ViewBinding implements Unbinder {
    private SearchSubsDetailedHolder target;
    private View cardView;

    class CardClickListener extends DebouncingOnClickListener {

        final /* synthetic */ SearchSubsDetailedHolder boundTarget;

        CardClickListener(SearchSubsDetailedHolder searchSubsDetailedHolder) {
            this.boundTarget = searchSubsDetailedHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onSubClicked();
        }
    }

    public SearchSubsDetailedHolder_ViewBinding(SearchSubsDetailedHolder searchSubsDetailedHolder, View view) {
        this.target = searchSubsDetailedHolder;
        searchSubsDetailedHolder.mBaseView = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.holder_subreddit_base, "field 'mBaseView'", LinearLayout.class);
        searchSubsDetailedHolder.subView = (SubView) Utils.findRequiredViewAsType(view, R.id.subview, "field 'subView'", SubView.class);
        View viewC = Utils.findRequiredView(view, R.id.holder_subreddit_card, "field 'mCardView' and method 'onSubClicked'");
        searchSubsDetailedHolder.mCardView = (CustomMaterialCardView) Utils.castView(viewC, R.id.holder_subreddit_card, "field 'mCardView'", CustomMaterialCardView.class);
        this.cardView = viewC;
        viewC.setOnClickListener(new CardClickListener(searchSubsDetailedHolder));
        searchSubsDetailedHolder.title = (BaseTextView) Utils.findRequiredViewAsType(view, R.id.holder_subreddit_title, "field 'title'", BaseTextView.class);
        searchSubsDetailedHolder.description = (SearchSubsDetailedDescription) Utils.findRequiredViewAsType(view, R.id.holder_subreddit_description, "field 'description'", SearchSubsDetailedDescription.class);
        searchSubsDetailedHolder.body = (HtmlTextView) Utils.findRequiredViewAsType(view, R.id.holder_subreddit_body, "field 'body'", HtmlTextView.class);
    }

    @Override
    public void unbind() {
        SearchSubsDetailedHolder target = this.target;
        if (target == null) throw new IllegalStateException("Bindings already cleared.");
        this.target = null;
        target.mBaseView = null;
        target.mCardView = null;
        this.cardView.setOnClickListener(null);
        this.cardView = null;
    }
}
