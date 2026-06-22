package com.resync.app.dialogs;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.core.content.FileProvider;
import com.bumptech.glide.RedditHelper;
import com.google.android.exoplayer2.util.MimeTypes;
import com.resync.app.R;
import com.resync.app.RedditApplication;
import com.resync.app.jobs.media.AbstractDownloadMediaJob;
import com.resync.app.jobs.media.DownloadMediaJob;
import com.resync.app.jobs.media.DownloadRedditVideoJob;
import com.resync.app.receiver.download.ShareReceiver;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet;
import com.resync.app.ui.viewholders.posts.simple.SimpleHolder;
import com.resync.app.ui.views.monet.MonetSwitch;
import com.resync.app.ui.views.responsive.CustomSubtleDividerView;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.ui.StaticToast;
import java.io.File;
import java.io.FileOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.helpers.WindowUtils;
import com.resync.app.util.Logger;
import com.resync.app.util.TextUtils;
import com.resync.app.model.Post;
import com.resync.app.util.links.LinkTypeHelper;

/* JADX INFO: renamed from: t9.m2 */
/* JADX INFO: compiled from: SharePostDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class SharePostDialogBottomSheet extends AbstractSelectionDialogBottomSheet {
    private Post post;
    private SimpleHolder previewHolder;
    AbstractSelectionDialogBottomSheet.SelectionItem postPreviewItem;
    AbstractSelectionDialogBottomSheet.SelectionItem postCommentsItem;
    AbstractSelectionDialogBottomSheet.SelectionItem linkItem;
    AbstractSelectionDialogBottomSheet.SelectionItem imageItem;

    /* JADX INFO: compiled from: SharePostDialogBottomSheet.java */
    class ShareThread extends Thread {

        final /* synthetic */ Bitmap bitmap;

        ShareThread(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            try {
                File file = new File(SharePostDialogBottomSheet.this.getContext().getExternalCacheDir(), "shared");
                if (!file.exists()) {
                    file.mkdirs();
                }
                File file2 = new File(file, System.currentTimeMillis() + ".jpg");
                FileOutputStream fileOutputStream = new FileOutputStream(file2);
                this.bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                File file3 = new File(RedditApplication.getAppContext().getExternalCacheDir(), "shared/" + System.currentTimeMillis() + ".jpg");
                FileUtils.copyFile(file2, file3);
                ShareReceiver.share(new AbstractDownloadMediaJob.ShareInfo(SharePostDialogBottomSheet.this.post.getTitle(), FileProvider.getUriForFile(RedditApplication.getAppContext(), "com.resync.app.provider", file3), MimeTypes.IMAGE_JPEG));
            } catch (Exception e) {
                Logger.logException(e);
                StaticToast.showWithContext(SharePostDialogBottomSheet.this.getActivity(), "Error sharing post preview");
            }
        }
    }

    public static Bundle buildArguments(Post post) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("Post", post);
        return bundle;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void onIncludeTitleChanged(CompoundButton compoundButton, boolean z) {
        SettingsSingleton.getSettings().imageShareTitle = z;
        SettingsSingleton.getInstance().putBoolean("image_share_title", z);
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet
    public void setupItems() {
        MonetSwitch monetSwitch = new MonetSwitch(getContext());
        monetSwitch.setText("Include title");
        monetSwitch.setTextColor(MonetUtil.getLinkColorAuto());
        monetSwitch.setChecked(SettingsSingleton.getSettings().imageShareTitle);
        monetSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: t9.l2
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                SharePostDialogBottomSheet.onIncludeTitleChanged(compoundButton, z);
            }
        });
        int padding = SizeHelper.dpToPx(16);
        monetSwitch.setPadding(padding, padding, padding, padding);
        ((LinearLayout) getView().findViewById(R.id.abstract_selection_dialog_bottom_sheet_main_wrapper)).addView(monetSwitch, 1);
        CustomSubtleDividerView customSubtleDividerView = new CustomSubtleDividerView(getActivity());
        customSubtleDividerView.setLayoutParams(new ViewGroup.LayoutParams(-1, SizeHelper.dpToPx(1)));
        ((LinearLayout) getView().findViewById(R.id.abstract_selection_dialog_bottom_sheet_main_wrapper)).addView(customSubtleDividerView, 2);
        this.postPreviewItem = addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(R.drawable.outline_post_24, "Post preview"));
        this.postCommentsItem = addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(R.drawable.outline_mode_comment_24, "Post comments"));
        if (StringUtils.isNotEmpty(this.post.getUrl())) {
            this.linkItem = addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(R.drawable.ic_link_black_24dp, "Link"));
        }
        if ((1 == this.post.getSyncType() || 7 == this.post.getSyncType() || 4 == this.post.getSyncType()) && !LinkTypeHelper.isYouTube(this.post.getUrl())) {
            this.imageItem = addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(R.drawable.outline_image_24, "Image"));
        }
        SimpleHolder simpleHolderJ = SimpleHolder.create(getContext(), (ViewGroup) getActivity().getWindow().getDecorView(), null);
        this.previewHolder = simpleHolderJ;
        simpleHolderJ.setFirstPost();
        this.previewHolder.bindPost(this.post, 0);
        this.previewHolder.itemView.getLayoutParams().width = Math.min(WindowUtils.getDisplayWidth(), WindowUtils.getDisplayHeight());
        if (this.previewHolder.itemView.getLayoutParams() instanceof FrameLayout.LayoutParams) {
            ((FrameLayout.LayoutParams) this.previewHolder.itemView.getLayoutParams()).gravity = 17;
        }
        ((ViewGroup) getActivity().getWindow().getDecorView()).addView(this.previewHolder.itemView);
        this.previewHolder.itemView.setVisibility(4);
    }

    void shareImage() {
        if (LinkTypeHelper.isRedditVideo(this.post.getUrl())) {
            DownloadRedditVideoJob.download(getChildFragmentManager(), this.post.getTitle(), this.post.getSubreddit(), this.post.getUrl(), 2);
        } else {
            DownloadMediaJob.download(getChildFragmentManager(), this.post.getTitle(), this.post.getSubreddit(), this.post.getUrl(), 2);
        }
        dismiss();
    }

    public void sharePreview() {
        this.previewHolder.itemView.setDrawingCacheEnabled(true);
        this.previewHolder.itemView.buildDrawingCache();
        Bitmap drawingCache = this.previewHolder.itemView.getDrawingCache();
        if (drawingCache == null) {
            StaticToast.showWithContext(getActivity(), "Error sharing post preview (no bitmap)");
        } else {
            new ShareThread(drawingCache).start();
            dismiss();
        }
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public boolean requiresArguments() {
        return true;
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public void loadCustomArguments() {
        this.post = (Post) getArguments().getSerializable("Post");
    }

    @Override // com.resync.app.dialogs.interfaces.SelectionDialogBottomSheetInterface
    public String getTitle() {
        return "Share";
    }

    @Override // androidx.fragment.app.c, android.content.DialogInterface.OnDismissListener
    public void onDismiss(DialogInterface dialogInterface) {
        ((ViewGroup) getActivity().getWindow().getDecorView()).removeView(this.previewHolder.itemView);
        super.onDismiss(dialogInterface);
    }

    @Override // com.resync.app.dialogs.interfaces.SelectionDialogBottomSheetInterface
    public void onItemSelected(AbstractSelectionDialogBottomSheet.SelectionItem item) {
        if (item.equals(this.postPreviewItem)) {
            sharePreview();
            return;
        }
        if (item.equals(this.postCommentsItem)) {
            RedditHelper.sharePostLink(getActivity(), SettingsSingleton.getSettings().imageShareTitle ? this.post.getTitle() : null, "https://www.reddit.com/r/" + getPost().getSubreddit() + "/comments/" + getPost().getId() + "/_/");
            dismiss();
            return;
        }
        if (!item.equals(this.linkItem)) {
            if (item.equals(this.imageItem)) {
                shareImage();
                return;
            }
            return;
        }
        if (TextUtils.isEmpty(this.post.getUrl())) {
            RedditHelper.sharePostLink(getActivity(), SettingsSingleton.getSettings().imageShareTitle ? this.post.getTitle() : null, "https://www.reddit.com/r/" + getPost().getSubreddit() + "/comments/" + getPost().getId() + "/_/");
        } else {
            RedditHelper.sharePostLink(getActivity(), SettingsSingleton.getSettings().imageShareTitle ? this.post.getTitle() : null, this.post.getUrl());
        }
        dismiss();
    }
}
