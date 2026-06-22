package com.resync.app.text.spans;

import android.content.Intent;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.text.style.UpdateAppearance;
import android.view.View;
import com.bumptech.glide.RedditHelper;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.activities.PreferencesActivity;
import com.resync.app.ui.activities.PreferencesSearchActivity;
import com.resync.app.ui.fragment_dialogs.ImagePeekDialogFragment;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.TableDialogBottomSheet;
import com.resync.app.helpers.ui.StaticToast;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.media.images.ImageHelper;
import com.resync.app.ads.AmazonAnalyticsRequest;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.text.spans.CustomClickableSpan;
import com.resync.app.text.spans.SyncSpan;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.helpers.ContextHelper;
import com.resync.app.dialogs.AmpDetectedAlertDialogBottomSheet;
import com.resync.app.dialogs.UnderscoreDetectedAlertDialogBottomSheet;
import com.resync.app.dialogs.UrlSelectionDialogBottomSheet;
import com.resync.app.premium.UltraHelper;
import com.resync.app.util.Logger;
import com.resync.app.model.Post;
import com.resync.app.util.links.LinkHelper;
import com.resync.app.util.links.LinkTypeHelper;

/* JADX INFO: renamed from: mb.d */
/* JADX INFO: compiled from: CustomUrlSpan.java */
/* JADX INFO: loaded from: classes3.dex */
public class CustomUrlSpan extends CharacterStyle implements UpdateAppearance, SyncSpan, CustomClickableSpan {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private String url;

    public CustomUrlSpan(String url) {
        this.url = url;
    }

    public String getUrl() {
        Logger.debug("TAPPED: " + this.url);
        if (!StringUtils.startsWith(this.url, "/u/")) {
            return this.url;
        }
        return "https://reddit.com" + this.url;
    }

    @Override // com.resync.app.text.spans.CustomClickableSpan
    public void onClick(View view) {
        Logger.verbose("Clicked: " + getUrl());
        if (StringUtils.contains(getUrl(), "amazon.com/") && StringUtils.contains(getUrl(), "/dp/")) {
            OAuthRequestManager.enqueueAux(new AmazonAnalyticsRequest(getUrl()));
        }
        if (StringUtils.startsWith(getUrl(), "sync-flair://")) {
            StaticToast.show(getUrl().replace("sync-flair://", ""));
            return;
        }
        if (StringUtils.startsWith(getUrl(), "sync-code://")) {
            RedditHelper.copyToClipboardWithToast(getUrl().replace("sync-code://", ""), view.getContext());
            return;
        }
        if (StringUtils.startsWith(getUrl(), "sync-settings")) {
            try {
                if (getUrl().equals("sync-settings://search")) {
                    ContextHelper.getBaseActivity(view.getContext()).startActivity(new Intent(view.getContext(), (Class<?>) PreferencesSearchActivity.class));
                } else {
                    Intent intent = new Intent(view.getContext(), (Class<?>) PreferencesActivity.class);
                    String strReplace = getUrl().replace("sync-settings://", "");
                    int mode = Integer.parseInt(strReplace.split("-")[0]);
                    String highlight = strReplace.split("-")[1];
                    intent.putExtra("mode", mode);
                    intent.putExtra("highlight", highlight);
                    ContextHelper.getBaseActivity(view.getContext()).startActivity(intent);
                }
                return;
            } catch (Exception e) {
                Logger.logException(e);
                StaticToast.show("Error loading linked setting");
                return;
            }
        }
        if (StringUtils.startsWith(getUrl(), "<table>")) {
            FragmentUtils.showWithUrl(TableDialogBottomSheet.class, ContextHelper.getFragmentManager(view.getContext()), getUrl());
            return;
        }
        if (StringUtils.startsWith(getUrl(), "chrome://")) {
            RedditHelper.copyToClipboard(view.getContext(), getUrl(), true);
            return;
        }
        if (LinkTypeHelper.isValidUrl(getUrl())) {
            if (getUrl().contains("%5C_")) {
                if (getUrl().contains("reddit.com")) {
                    LinkHelper.open(view.getContext(), getUrl().replaceAll("%5C_", "_"));
                    return;
                } else {
                    FragmentUtils.showWithUrl(UnderscoreDetectedAlertDialogBottomSheet.class, ContextHelper.getFragmentManager(view.getContext()), getUrl());
                    return;
                }
            }
            if (getUrl().contains("/amp/s/") && getUrl().contains("google") && SettingsSingleton.getSettings().bypass_amp && UltraHelper.isUltra()) {
                FragmentUtils.showWithUrl(AmpDetectedAlertDialogBottomSheet.class, ContextHelper.getFragmentManager(view.getContext()), getUrl());
            } else {
                LinkHelper.open(view.getContext(), getUrl());
            }
        }
    }

    @Override // com.resync.app.text.spans.CustomClickableSpan
    public void onLongClick(View view) {
        if (StringUtils.startsWith(getUrl(), "sync-flair://")) {
            RedditHelper.copyToClipboardWithToast(getUrl().replace("sync-flair://", ""), view.getContext());
            return;
        }
        if (StringUtils.startsWith(getUrl(), "sync-code://")) {
            RedditHelper.copyToClipboardWithToast(getUrl().replace("sync-code://", ""), view.getContext());
            return;
        }
        if (StringUtils.startsWith(getUrl(), "<table>")) {
            FragmentUtils.showWithUrl(TableDialogBottomSheet.class, ContextHelper.getFragmentManager(view.getContext()), getUrl());
            return;
        }
        if (LinkTypeHelper.isImage(getUrl())) {
            ImagePeekDialogFragment.show(view.getContext(), Post.createForLink(ImageHelper.normalizeImageUrl(getUrl())));
        } else if (LinkTypeHelper.isValidUrl(getUrl())) {
            FragmentUtils.showWithArguments(UrlSelectionDialogBottomSheet.class, ContextHelper.getFragmentManager(view.getContext()), UrlSelectionDialogBottomSheet.buildArguments(getUrl()));
        }
    }

    @Override // android.text.style.CharacterStyle
    public void updateDrawState(TextPaint textPaint) {
        textPaint.setColor(textPaint.linkColor);
    }
}
