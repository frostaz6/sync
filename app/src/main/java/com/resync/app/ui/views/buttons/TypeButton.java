package com.resync.app.ui.views.buttons;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.resync.app.R;
import com.resync.app.widgets.ColorFilterProvider;
import com.resync.app.model.Post;
import com.resync.app.util.links.LinkTypeHelper;

/* JADX INFO: loaded from: classes3.dex */
public class TypeButton extends AbstractRedditButton {
    public TypeButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // com.resync.app.ui.views.buttons.AbstractRedditButton
    protected void applyStyle() {
        super.applyStyle();
        setColorFilter(ColorFilterProvider.getColorFilter(getFilterColor()));
    }

    public void applyPostType(Post post) {
        if (post.getSyncType() == 7 || LinkTypeHelper.isGiphy(post.getUrl()) || LinkTypeHelper.isGfycat(post.getUrl())) {
            setImageResource(R.drawable.ic_gif_white_24dp);
        } else if (post.getSyncType() == 1) {
            if (post.getUrl().contains("v.redd.it") || post.getUrl().contains("youtube.com") || post.getUrl().contains("youtu.be")) {
                setImageResource(R.drawable.ic_play_circle_outline_white_24dp);
            } else {
                setImageResource(R.drawable.ic_image_white_24dp);
            }
        } else if (post.getSyncType() == 6 || post.getSyncType() == 10) {
            setImageResource(R.drawable.ic_collections_black_24dp);
        } else if (post.getSyncType() == 2) {
            setImageResource(R.drawable.ic_text_fields_white_24dp);
        } else if (post.getSyncType() == 3 || post.getSyncType() == 9) {
            setImageResource(R.drawable.ic_link_white_24dp);
        } else if (post.getSyncType() == 4) {
            setImageResource(R.drawable.ic_play_circle_outline_white_24dp);
        } else {
            setImageResource(R.drawable.ic_comment_white_24dp);
        }
        setScaleType(ImageView.ScaleType.CENTER_INSIDE);
    }
}
