package com.resync.app.ui.views.drawer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import com.bumptech.glide.MaterialPostsHelper;
import com.resync.app.ui.activities.CasualActivity;
import com.resync.app.ui.views.responsive.CustomEditText;
import com.squareup.otto.Subscribe;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.events.FullSubSearchEvent;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.events.CloseDrawerEvent;
import com.resync.app.events.DrawerClosedEvent;
import com.resync.app.events.SubmitAllSearchEvent;
import com.resync.app.events.SubmitSubredditSearchEvent;
import com.resync.app.helpers.ContextHelper;
import com.resync.app.util.Logger;
import com.resync.app.util.TextUtils;

/* JADX INFO: loaded from: classes3.dex */
public class DrawerEditText extends CustomEditText {

    class SearchEditorActionListener implements TextView.OnEditorActionListener {
        SearchEditorActionListener() {
        }

        @Override // android.widget.TextView.OnEditorActionListener
        public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
            if (actionId != 3) {
                return false;
            }
            String subreddit = ContextHelper.getAboutTargetSubreddit(DrawerEditText.this.getContext());
            if (StringUtils.isEmpty(subreddit)) {
                BusSingleton.getBus().post(new SubmitAllSearchEvent());
                return true;
            }
            BusSingleton.getBus().post(new SubmitSubredditSearchEvent(subreddit));
            return true;
        }
    }

    public DrawerEditText(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setOnEditorActionListener(new SearchEditorActionListener());
    }

    @Subscribe
    public void fullSearchSubs(FullSubSearchEvent fullSubSearchEvent) {
        clearFocus();
        String strTrim = getText().toString().trim();
        if (TextUtils.isEmpty(strTrim)) {
            return;
        }
        CasualActivity.openSubreddit(getContext(), MaterialPostsHelper.buildSubSearchAddress(strTrim));
    }

    @Override // android.widget.TextView, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        try {
            BusSingleton.getBus().register(this);
        } catch (Exception e) {
            Logger.logException(e);
        }
    }

    @Override // android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        try {
            BusSingleton.getBus().unregister(this);
        } catch (Exception e) {
            Logger.logException(e);
        }
    }

    @Subscribe
    public void onDrawerClosed(DrawerClosedEvent drawerClosedEvent) {
        setText((CharSequence) null);
    }

    @Subscribe
    public void submitSearch(SubmitSubredditSearchEvent submitSubredditSearchEvent) {
        ((InputMethodManager) getContext().getSystemService("input_method")).hideSoftInputFromWindow(getWindowToken(), 0);
        clearFocus();
        BusSingleton.getBus().post(new CloseDrawerEvent());
        String strTrim = getText().toString().trim();
        if (TextUtils.isEmpty(strTrim)) {
            return;
        }
        if (strTrim.startsWith("http://") || strTrim.startsWith("https://")) {
            CasualActivity.openSubredditSearch(getContext(), strTrim, null);
        } else {
            CasualActivity.openSubredditSearch(getContext(), strTrim, submitSubredditSearchEvent.query);
        }
    }

    @Subscribe
    public void submitSearchAll(SubmitAllSearchEvent submitAllSearchEvent) {
        ((InputMethodManager) getContext().getSystemService("input_method")).hideSoftInputFromWindow(getWindowToken(), 0);
        clearFocus();
        BusSingleton.getBus().post(new CloseDrawerEvent());
        String strTrim = getText().toString().trim();
        if (TextUtils.isEmpty(strTrim)) {
            return;
        }
        if (strTrim.startsWith("http://") || strTrim.startsWith("https://")) {
            CasualActivity.openSubredditSearch(getContext(), strTrim, null);
        } else {
            CasualActivity.openSubredditSearch(getContext(), strTrim, null);
        }
    }
}
