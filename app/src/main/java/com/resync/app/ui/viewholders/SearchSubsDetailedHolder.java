package com.resync.app.ui.viewholders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import butterknife.ButterKnife;
import com.resync.app.R;
import com.resync.app.singleton.UiSingleton;
import com.resync.app.ui.views.core.SubView;
import com.resync.app.ui.views.responsive.CustomMaterialCardView;
import com.resync.app.ui.views.text.BaseTextView;
import com.resync.app.ui.views.text.spannable.children.HtmlTextView;
import com.resync.app.ui.views.text.spannable.children.other.SearchSubsDetailedDescription;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.helpers.PaddingHelper;
import com.resync.app.holders.BasePostViewHolder;
import com.resync.app.model.Post;
import com.resync.app.util.links.LinkHelper;

/* JADX INFO: loaded from: classes3.dex */
public class SearchSubsDetailedHolder extends BasePostViewHolder {

    HtmlTextView body;

    SearchSubsDetailedDescription description;

    LinearLayout mBaseView;

    CustomMaterialCardView mCardView;

    SubView subView;

    BaseTextView title;

    private SearchSubsDetailedHolder(Context context, View view) {
        super(context, view, 0);
        ButterKnife.bind(this, view);
        if (UiSingleton.getInstance().getUiMode() == 4) {
            PaddingHelper.setPaddingAll(this.mCardView, 8);
        } else {
            PaddingHelper.setPaddingAll(this.mCardView, 0);
            this.mCardView.setRadius(0.0f);
        }
    }

    public static SearchSubsDetailedHolder create(Context context, ViewGroup viewGroup) {
        return new SearchSubsDetailedHolder(context, LayoutInflater.from(context).inflate(R.layout.holder_search_subs_detailed, viewGroup, false));
    }

    public void onSubClicked() {
        LinkHelper.open(this.context, "/r/" + getPost().getSubreddit());
    }

    public void bind(Post post) {
        super.bindPost(post, 0);
        this.subView.setSubreddit(post.getSubreddit());
        this.title.setText(post.getSubreddit());
        this.description.bind(post);
        if (!StringUtils.isNotEmpty(post.getAuthor())) {
            this.body.setVisibility(8);
        } else {
            this.body.setVisibility(0);
            this.body.setHtml(post.getAuthor());
        }
    }
}
