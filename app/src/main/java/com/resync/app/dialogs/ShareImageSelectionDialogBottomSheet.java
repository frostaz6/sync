package com.resync.app.dialogs;

import android.os.Bundle;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import com.bumptech.glide.RedditHelper;
import com.resync.app.R;
import com.resync.app.jobs.media.DownloadMediaJob;
import com.resync.app.jobs.media.DownloadRedditVideoJob;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet;
import com.resync.app.ui.views.monet.MonetSwitch;
import com.resync.app.ui.views.responsive.CustomSubtleDividerView;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.util.TextUtils;
import com.resync.app.util.links.LinkTypeHelper;

/* JADX INFO: renamed from: t9.k2 */
/* JADX INFO: compiled from: ShareImageSelectionDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class ShareImageSelectionDialogBottomSheet extends AbstractSelectionDialogBottomSheet {
    private String title;
    private String subreddit;
    private String url;
    AbstractSelectionDialogBottomSheet.SelectionItem directItem;
    AbstractSelectionDialogBottomSheet.SelectionItem linkItem;
    AbstractSelectionDialogBottomSheet.SelectionItem clipboardItem;

    public static Bundle buildArguments(String str, String str2, String str3) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("title", str);
        bundle.putSerializable("subreddit", str2);
        bundle.putSerializable("url", str3);
        return bundle;
    }

    public static boolean isShareableImage(String str) {
        if (LinkTypeHelper.isRedditVideo(str)) {
            return false;
        }
        String fileExtensionFromUrl = MimeTypeMap.getFileExtensionFromUrl(str);
        if (TextUtils.isEmpty(fileExtensionFromUrl) || "gif".equals(fileExtensionFromUrl)) {
            return false;
        }
        if ("jpg".equals(fileExtensionFromUrl) || "jpeg".equals(fileExtensionFromUrl) || "pjpeg".equals(fileExtensionFromUrl) || "png".equals(fileExtensionFromUrl)) {
            return true;
        }
        if ("mp4".equals(fileExtensionFromUrl) || "webm".equals(fileExtensionFromUrl)) {
            return false;
        }
        "webp".equals(fileExtensionFromUrl);
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void onIncludeTitleToggled(CompoundButton compoundButton, boolean z) {
        SettingsSingleton.getSettings().imageShareTitle = z;
        SettingsSingleton.getInstance().putBoolean("image_share_title", z);
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet
    public void setupItems() {
        MonetSwitch monetSwitch = new MonetSwitch(getContext());
        monetSwitch.setText("Include title");
        monetSwitch.setTextColor(MonetUtil.getLinkColorAuto());
        monetSwitch.setChecked(SettingsSingleton.getSettings().imageShareTitle);
        monetSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: t9.j2
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                ShareImageSelectionDialogBottomSheet.onIncludeTitleToggled(compoundButton, z);
            }
        });
        int padding = SizeHelper.dpToPx(16);
        monetSwitch.setPadding(padding, padding, padding, padding);
        ((LinearLayout) getView().findViewById(R.id.abstract_selection_dialog_bottom_sheet_main_wrapper)).addView(monetSwitch, 1);
        CustomSubtleDividerView customSubtleDividerView = new CustomSubtleDividerView(getActivity());
        customSubtleDividerView.setLayoutParams(new ViewGroup.LayoutParams(-1, SizeHelper.dpToPx(1)));
        ((LinearLayout) getView().findViewById(R.id.abstract_selection_dialog_bottom_sheet_main_wrapper)).addView(customSubtleDividerView, 2);
        this.directItem = addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(R.drawable.outline_image_24, "Direct"));
        this.linkItem = addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(R.drawable.ic_link_black_24dp, "Link"));
        if (isShareableImage(this.url)) {
            this.clipboardItem = addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(R.drawable.clipboard_plus_outline, "Clipboard"));
        }
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public boolean requiresArguments() {
        return true;
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public void loadCustomArguments() {
        this.title = getArguments().getString("title");
        this.subreddit = getArguments().getString("subreddit");
        this.url = getArguments().getString("url");
    }

    @Override // com.resync.app.dialogs.interfaces.SelectionDialogBottomSheetInterface
    public String getTitle() {
        return "Share Image";
    }

    @Override // com.resync.app.dialogs.interfaces.SelectionDialogBottomSheetInterface
    public void onItemSelected(AbstractSelectionDialogBottomSheet.SelectionItem item) {
        if (item.equals(this.directItem)) {
            if (LinkTypeHelper.isRedditVideo(this.url)) {
                DownloadRedditVideoJob.download(getParentFragmentManager(), this.title, this.subreddit, this.url, 2);
            } else {
                DownloadMediaJob.download(getParentFragmentManager(), this.title, this.subreddit, this.url, 2);
            }
        } else if (item.equals(this.linkItem)) {
            RedditHelper.sharePostLink(getActivity(), this.title, this.url);
        } else if (item.equals(this.clipboardItem)) {
            DownloadMediaJob.download(getParentFragmentManager(), this.title, this.subreddit, this.url, 3);
        }
        dismiss();
    }
}
