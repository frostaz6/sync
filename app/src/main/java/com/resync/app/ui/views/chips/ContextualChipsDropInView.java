package com.resync.app.ui.views.chips;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import butterknife.ButterKnife;
import com.resync.app.R;
import com.resync.app.sections.posts.toolbar.chip_groups.ChipWrapper;
import com.resync.app.ui.fragments.MessagingFragment;
import com.resync.app.ui.fragments.posts.BasePostsFragment;
import com.resync.app.helpers.ContextHelper;
import com.resync.app.util.Logger;

/* JADX INFO: loaded from: classes3.dex */
public class ContextualChipsDropInView extends FrameLayout {

    private Rect hitRect;
    private int[] location;

    ChipWrapper chipWrapper;

    public ContextualChipsDropInView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.hitRect = new Rect();
        this.location = new int[2];
        LayoutInflater.from(context).inflate(R.layout.view_contextual_chips_drop_in, (ViewGroup) this, true);
        ButterKnife.bind(this);
        refresh();
    }

    public void minimize() {
        this.chipWrapper.minimize();
    }

    public boolean containsPoint(float x, float y) {
        if (getVisibility() != 0) {
            Logger.verbose("RAW: WAS NOT VISIBLE");
            return false;
        }
        Logger.verbose("RAW: " + x + " - " + y);
        getLocationInWindow(this.location);
        StringBuilder sb = new StringBuilder();
        sb.append("RAW: ");
        sb.append(this.location);
        Logger.verbose(sb.toString());
        Rect rect = this.hitRect;
        int[] loc = this.location;
        rect.set(loc[0], loc[1], loc[0] + getWidth(), this.location[1] + getHeight());
        return this.hitRect.contains((int) x, (int) y);
    }

    public void refresh() {
        this.chipWrapper.refresh();
        BasePostsFragment postsFragment = ContextHelper.getPostsFragment(getContext());
        MessagingFragment messagingFragment = ContextHelper.getMessagingFragment(getContext());
        if (postsFragment != null) {
            this.chipWrapper.bindSection(postsFragment.getColumnCount(), postsFragment.getSubredditName(), postsFragment.getAccess(), postsFragment.getSort());
        } else if (messagingFragment != null) {
            this.chipWrapper.bindSection(2, messagingFragment.getModeString(), null, null);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }
}
