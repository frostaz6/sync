package com.resync.app.sections.preferences.toolbar;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.resync.app.R;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.ui.interfaces.DarkModeAware;

/* JADX INFO: loaded from: classes.dex */
public class PreferencesSmallSubredditToolbarView extends FrameLayout implements DarkModeAware {

    private TextView titleView;

    public PreferencesSmallSubredditToolbarView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        FrameLayout.inflate(context, R.layout.view_preferences_small_subreddit_toolbar, this);
        this.titleView = (TextView) findViewById(R.id.toolbar_title_small);
        applyColors();
    }

    private void applyColors() {
        this.titleView.setTextColor(MonetUtil.getPrimaryTextColorAuto());
    }

    public void setTitle(String str) {
        this.titleView.setText(str);
    }

    @Override // com.resync.app.ui.interfaces.DarkModeAware
    public void onDarkModeChanged() {
        applyColors();
    }
}
