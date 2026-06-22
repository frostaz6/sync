package com.resync.app.ui.sections.concat;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.resync.app.R;
import com.resync.app.ui.sections.concat.SearchOptionsSection;
import com.resync.app.ui.viewholders.drawer.DrawerHeaderHolder;
import com.resync.app.ui.views.text.BaseTextView;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.util.Logger;

/* JADX INFO: renamed from: com.resync.app.ui.sections.concat.a */
/* JADX INFO: compiled from: FooterHelper.java */
/* JADX INFO: loaded from: classes.dex */
public final class FooterHelper {

    int visibleHeight;
    int headerHeight;
    int searchBoxHeight;
    int headerRowHeight;

    int rowHeight;

    int searchOptionsHeight;
    int headerCount;
    int sectionCount;

    boolean searchOptionsVisible;

    public FooterHelper(Context context) {
        measure(context);
    }

    private void measure(Context context) {
        SearchOptionsSection.SearchOptionsHolder searchOptionsHolderE = SearchOptionsSection.SearchOptionsHolder.create(context, null);
        searchOptionsHolderE.itemView.measure(0, 0);
        this.searchOptionsHeight = searchOptionsHolderE.itemView.getMeasuredHeight();
        DrawerHeaderHolder drawerHeaderHolderP = DrawerHeaderHolder.create(context, null);
        drawerHeaderHolderP.itemView.measure(0, 0);
        this.headerHeight = drawerHeaderHolderP.itemView.getMeasuredHeight();
        Logger.verbose("DRAWER", "Header height: " + this.headerHeight);
        this.searchBoxHeight = ((ViewGroup) drawerHeaderHolderP.itemView.findViewById(R.id.search_box_parent)).getMeasuredHeight();
        Logger.verbose("DRAWER", "Search box height: " + this.searchBoxHeight);
        ViewGroup viewGroup = (ViewGroup) View.inflate(context, R.layout.holder_subreddit_header, null);
        ((BaseTextView) viewGroup.findViewById(R.id.holder_subreddit_header_textview)).setText("Test");
        viewGroup.measure(0, 0);
        Logger.verbose("DRAWER", "Header row height: " + viewGroup.getMeasuredHeight());
        this.headerRowHeight = viewGroup.getMeasuredHeight();
        ViewGroup viewGroup2 = (ViewGroup) View.inflate(context, R.layout.holder_subreddit, null);
        viewGroup2.measure(0, 0);
        Logger.verbose("DRAWER", "Row height: " + viewGroup2.getMeasuredHeight());
        this.rowHeight = viewGroup2.getMeasuredHeight();
        Logger.verbose("DRAWER", "Visible height: " + this.visibleHeight);
    }

    public int getSearchBoxOffset() {
        return this.headerHeight - this.searchBoxHeight;
    }

    public int getFooterHeight() {
        int availableHeight = this.visibleHeight - this.searchBoxHeight;
        Logger.verbose("DRAWER", "Total headers: " + this.headerCount);
        Logger.verbose("DRAWER", "Total rows: " + this.sectionCount);
        int footerHeight = (availableHeight - (this.headerCount * this.headerRowHeight)) - (this.sectionCount * this.rowHeight);
        if (this.searchOptionsVisible) {
            footerHeight -= this.searchOptionsHeight;
        }
        int iMax = Math.max(0, footerHeight);
        if (iMax != 0) {
            return iMax;
        }
        Logger.verbose("DRAWER", "No footer required");
        return SizeHelper.dpToPx(92);
    }

    public void setSearchOptionsVisible(boolean visible) {
        this.searchOptionsVisible = visible;
    }

    public void setHeaderCount(int headerCount) {
        this.headerCount = headerCount;
    }

    public void setSectionCount(int sectionCount) {
        this.sectionCount = sectionCount;
    }

    public void setVisibleHeight(int visibleHeight) {
        this.visibleHeight = visibleHeight;
    }
}
