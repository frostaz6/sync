package com.resync.app.ui.views.posts.simple;

import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.ui.views.text.spannable.children.HtmlTextView;

/* JADX INFO: loaded from: classes3.dex */
public class TwitterEmbedded_ViewBinding implements Unbinder {
    private TwitterEmbedded target;

    public TwitterEmbedded_ViewBinding(TwitterEmbedded twitterEmbedded, View view) {
        this.target = twitterEmbedded;
        twitterEmbedded.mThumbnail = (PostThumbnailView) Utils.findRequiredViewAsType(view, R.id.twitter_thumbnail, "field 'mThumbnail'", PostThumbnailView.class);
        twitterEmbedded.mTwitterHtmlView = (HtmlTextView) Utils.findRequiredViewAsType(view, R.id.twitter_html, "field 'mTwitterHtmlView'", HtmlTextView.class);
        twitterEmbedded.mDescription = (TwitterDescription) Utils.findRequiredViewAsType(view, R.id.twitter_description, "field 'mDescription'", TwitterDescription.class);
    }

    @Override // butterknife.Unbinder
    public void unbind() {
        TwitterEmbedded twitterEmbedded = this.target;
        if (twitterEmbedded == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        twitterEmbedded.mThumbnail = null;
        twitterEmbedded.mTwitterHtmlView = null;
        twitterEmbedded.mDescription = null;
    }
}
