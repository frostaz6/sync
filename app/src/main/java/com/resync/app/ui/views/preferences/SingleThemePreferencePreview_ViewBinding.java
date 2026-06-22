package com.resync.app.ui.views.preferences;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;

/* JADX INFO: loaded from: classes3.dex */
public class SingleThemePreferencePreview_ViewBinding implements Unbinder {
    private SingleThemePreferencePreview target;

    public SingleThemePreferencePreview_ViewBinding(SingleThemePreferencePreview singleThemePreferencePreview, View view) {
        this.target = singleThemePreferencePreview;
        singleThemePreferencePreview.toolbar = (TextView) Utils.findRequiredViewAsType(view, R.id.view_toolbar, "field 'toolbar'", TextView.class);
        singleThemePreferencePreview.bottomNav = Utils.findRequiredView(view, R.id.view_bottom_nav, "field 'bottomNav'");
        singleThemePreferencePreview.contentOne = Utils.findRequiredView(view, R.id.content_one, "field 'contentOne'");
        singleThemePreferencePreview.contentOneTitle = (TextView) Utils.findRequiredViewAsType(view, R.id.content_one_title, "field 'contentOneTitle'", TextView.class);
        singleThemePreferencePreview.contentOneLink = (TextView) Utils.findRequiredViewAsType(view, R.id.content_one_link, "field 'contentOneLink'", TextView.class);
        singleThemePreferencePreview.contentOneSelftext = (TextView) Utils.findRequiredViewAsType(view, R.id.content_one_selftext, "field 'contentOneSelftext'", TextView.class);
        singleThemePreferencePreview.contentTwoTitle = (TextView) Utils.findRequiredViewAsType(view, R.id.content_two_title, "field 'contentTwoTitle'", TextView.class);
        singleThemePreferencePreview.contentTwoLink = (TextView) Utils.findRequiredViewAsType(view, R.id.content_two_link, "field 'contentTwoLink'", TextView.class);
        singleThemePreferencePreview.contentTwoSelftext = (TextView) Utils.findRequiredViewAsType(view, R.id.content_two_selftext, "field 'contentTwoSelftext'", TextView.class);
        singleThemePreferencePreview.contentThreeTitle = (TextView) Utils.findRequiredViewAsType(view, R.id.content_three_title, "field 'contentThreeTitle'", TextView.class);
        singleThemePreferencePreview.contentThreeLink = (TextView) Utils.findRequiredViewAsType(view, R.id.content_three_link, "field 'contentThreeLink'", TextView.class);
        singleThemePreferencePreview.contentThreeSelftext = (TextView) Utils.findRequiredViewAsType(view, R.id.content_three_selftext, "field 'contentThreeSelftext'", TextView.class);
        singleThemePreferencePreview.contentTwo = Utils.findRequiredView(view, R.id.content_two, "field 'contentTwo'");
        singleThemePreferencePreview.contentThree = Utils.findRequiredView(view, R.id.content_three, "field 'contentThree'");
        singleThemePreferencePreview.fab = Utils.findRequiredView(view, R.id.fab, "field 'fab'");
        singleThemePreferencePreview.bottomNavItemOne = (ImageView) Utils.findRequiredViewAsType(view, R.id.view_bottom_nav_item_one, "field 'bottomNavItemOne'", ImageView.class);
        singleThemePreferencePreview.bottomNavItemTwo = (ImageView) Utils.findRequiredViewAsType(view, R.id.view_bottom_nav_item_two, "field 'bottomNavItemTwo'", ImageView.class);
        singleThemePreferencePreview.bottomNavItemThree = (ImageView) Utils.findRequiredViewAsType(view, R.id.view_bottom_nav_item_three, "field 'bottomNavItemThree'", ImageView.class);
    }

    @Override
    public void unbind() {
        SingleThemePreferencePreview singleThemePreferencePreview = this.target;
        if (singleThemePreferencePreview == null) throw new IllegalStateException("Bindings already cleared.");
        this.target = null;
        singleThemePreferencePreview.toolbar = null;
        singleThemePreferencePreview.bottomNav = null;
        singleThemePreferencePreview.contentOne = null;
        singleThemePreferencePreview.contentOneTitle = null;
        singleThemePreferencePreview.contentOneLink = null;
        singleThemePreferencePreview.contentOneSelftext = null;
        singleThemePreferencePreview.contentTwoTitle = null;
        singleThemePreferencePreview.contentTwoLink = null;
        singleThemePreferencePreview.contentTwoSelftext = null;
        singleThemePreferencePreview.contentThreeTitle = null;
        singleThemePreferencePreview.contentThreeLink = null;
        singleThemePreferencePreview.contentThreeSelftext = null;
        singleThemePreferencePreview.contentTwo = null;
        singleThemePreferencePreview.contentThree = null;
        singleThemePreferencePreview.fab = null;
        singleThemePreferencePreview.bottomNavItemOne = null;
        singleThemePreferencePreview.bottomNavItemTwo = null;
        singleThemePreferencePreview.bottomNavItemThree = null;
    }
}
