package com.resync.app.ui.views.posts.simple;

import android.content.Context;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatImageView;
import com.resync.app.RedditApplication;
import com.resync.app.util.Logger;
import com.resync.app.model.Post;
import com.resync.app.util.links.LinkTypeHelper;

/* JADX INFO: loaded from: classes3.dex */
public class IndicatorView extends AppCompatImageView {
    public IndicatorView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void bind(Post post, int visibility) {
        Logger.verbose("TYPE: " + post.getSyncType() + " - " + post.getUrl());
        if (post.shouldBlurNsfw()) {
            setImageBitmap(RedditApplication.playNsfwBitmap);
            setVisibility(0);
            return;
        }
        if (post.getSyncType() == 4 && LinkTypeHelper.isStreamable(post.getUrl())) {
            setImageBitmap(RedditApplication.playStreamableBitmap);
            setVisibility(0);
            return;
        }
        if (post.getSyncType() == 1 && LinkTypeHelper.isYouTube(post.getUrl())) {
            setImageBitmap(RedditApplication.playVideoBitmap);
            setVisibility(0);
            return;
        }
        if (post.getSyncType() == 4 && LinkTypeHelper.isRedgifs(post.getUrl())) {
            setImageBitmap(RedditApplication.playRedgifsBitmap);
            setVisibility(0);
            return;
        }
        if (post.getSyncType() == 4 && LinkTypeHelper.isRedditVideo(post.getUrl())) {
            setImageBitmap(RedditApplication.playRedditBitmap);
            setVisibility(0);
            return;
        }
        if (post.getSyncType() == 4 && post.getUrl().contains("preview.redd.it")) {
            setImageBitmap(RedditApplication.playGifBitmap);
            setVisibility(0);
            return;
        }
        if (post.getSyncType() == 4 && LinkTypeHelper.isGfycat(post.getUrl())) {
            setImageBitmap(RedditApplication.playGfycatBitmap);
            setVisibility(0);
            return;
        }
        if (post.getSyncType() == 4 && LinkTypeHelper.isGiphy(post.getUrl())) {
            setImageBitmap(RedditApplication.playGiphyBitmap);
            setVisibility(0);
            return;
        }
        if (post.getSyncType() == 7) {
            setImageBitmap(RedditApplication.playGifBitmap);
            setVisibility(0);
            return;
        }
        if (post.getSyncType() == 10) {
            setImageBitmap(RedditApplication.playAlbumBitmap);
            setVisibility(0);
            return;
        }
        if (post.isSpoiler()) {
            setImageBitmap(RedditApplication.playSpoilerBitmap);
            setVisibility(0);
        } else {
            if (post.getSyncType() == 9 && visibility == 8) {
                setImageBitmap(RedditApplication.playExternalBitmap);
                setVisibility(0);
                return;
            }
            Logger.verbose("HIDING INDICATOE: " + post.getSyncType());
            setVisibility(8);
        }
    }
}
