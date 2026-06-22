package com.resync.app.ui.views.basic;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.webkit.WebView;
import com.bumptech.glide.RedditHelper;
import com.resync.app.jobs.media.DownloadMediaJob;
import com.resync.app.ui.activities.BaseActivity;

/* JADX INFO: loaded from: classes3.dex */
public class SyncWebView extends WebView {

    class MenuClickListener implements MenuItem.OnMenuItemClickListener {

        final /* synthetic */ WebView.HitTestResult hitTestResult;

        MenuClickListener(WebView.HitTestResult hitTestResult) {
            this.hitTestResult = hitTestResult;
        }

        @Override // android.view.MenuItem.OnMenuItemClickListener
        public boolean onMenuItemClick(MenuItem menuItem) {
            if (menuItem.getItemId() == 0) {
                com.resync.app.util.links.LinkHandler.openExternal(SyncWebView.this.getContext(), this.hitTestResult.getExtra());
            } else if (menuItem.getItemId() == 1) {
                RedditHelper.copyUrlToClipboard(SyncWebView.this.getContext(), this.hitTestResult.getExtra());
            } else if (menuItem.getItemId() == 2) {
                RedditHelper.shareTextWithSubject(SyncWebView.this.getContext(), this.hitTestResult.getExtra(), this.hitTestResult.getExtra());
            } else if (menuItem.getItemId() == 3) {
                DownloadMediaJob.download(((BaseActivity) SyncWebView.this.getContext()).getSupportFragmentManager(), null, null, this.hitTestResult.getExtra(), 1);
            }
            return true;
        }
    }

    public SyncWebView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.View
    protected void onCreateContextMenu(ContextMenu contextMenu) {
        super.onCreateContextMenu(contextMenu);
        WebView.HitTestResult hitTestResult = getHitTestResult();
        MenuClickListener menuClickListener = new MenuClickListener(hitTestResult);
        if (hitTestResult.getType() == 5 || hitTestResult.getType() == 8) {
            contextMenu.setHeaderTitle(hitTestResult.getExtra());
            contextMenu.add(0, 0, 0, "Open in browser").setOnMenuItemClickListener(menuClickListener);
            contextMenu.add(0, 1, 0, "Copy link address").setOnMenuItemClickListener(menuClickListener);
            contextMenu.add(0, 2, 0, "Share link").setOnMenuItemClickListener(menuClickListener);
            contextMenu.add(0, 3, 0, "Save image").setOnMenuItemClickListener(menuClickListener);
            return;
        }
        if (hitTestResult.getType() == 1 || hitTestResult.getType() == 7) {
            contextMenu.setHeaderTitle(hitTestResult.getExtra());
            contextMenu.add(0, 0, 0, "Open in browser").setOnMenuItemClickListener(menuClickListener);
            contextMenu.add(0, 1, 0, "Copy link address").setOnMenuItemClickListener(menuClickListener);
            contextMenu.add(0, 2, 0, "Share link").setOnMenuItemClickListener(menuClickListener);
        }
    }

    public SyncWebView(Context context, AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
    }
}
