package com.resync.app.dialogs;

import android.content.res.ColorStateList;
import android.graphics.Outline;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.bumptech.glide.RedditHelper;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragmentX;
import com.resync.app.R;
import com.resync.app.RedditApplication;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet;
import com.resync.app.ui.views.images.CustomImageView;
import com.resync.app.ui.views.responsive.CustomProgressBar;
import com.resync.app.ui.views.responsive.CustomSubtleDividerView;
import com.resync.app.ui.views.text.BaseTextView;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.util.WebsitePreviewHelper;
import com.resync.app.util.links.LinkTypeHelper;

/* JADX INFO: compiled from: UrlSelectionDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class UrlSelectionDialogBottomSheet extends AbstractSelectionDialogBottomSheet {
    private String url;
    AbstractSelectionDialogBottomSheet.SelectionItem openInBrowserItem;
    AbstractSelectionDialogBottomSheet.SelectionItem copyLinkItem;
    AbstractSelectionDialogBottomSheet.SelectionItem shareLinkItem;
    ImageView failedImageView;
    CustomImageView previewImage;
    CustomProgressBar progressBar;

    /* JADX INFO: renamed from: t9.y2$a */
    /* JADX INFO: compiled from: UrlSelectionDialogBottomSheet.java */
    class RoundedOutlineProvider extends ViewOutlineProvider {
        RoundedOutlineProvider() {
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), SizeHelper.dpToPxFloat(12));
        }
    }

    /* JADX INFO: renamed from: t9.y2$b */
    /* JADX INFO: compiled from: UrlSelectionDialogBottomSheet.java */
    class YouTubeInitListener implements YouTubePlayer.OnInitializedListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ String videoId;
        final /* synthetic */ int startMillis;
        final /* synthetic */ FrameLayout container;

        YouTubeInitListener(String str, int startMillis, FrameLayout frameLayout) {
            this.videoId = str;
            this.startMillis = startMillis;
            this.container = frameLayout;
        }

        @Override // com.google.android.youtube.player.YouTubePlayer.OnInitializedListener
        public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean z) {
            youTubePlayer.setFullscreen(false);
            try {
                youTubePlayer.loadVideo(this.videoId, this.startMillis);
            } catch (Exception unused) {
            }
        }

        @Override // com.google.android.youtube.player.YouTubePlayer.OnInitializedListener
        public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
            this.container.setVisibility(8);
        }
    }

    /* JADX INFO: renamed from: t9.y2$c */
    /* JADX INFO: compiled from: UrlSelectionDialogBottomSheet.java */
    class PreviewOutlineProvider extends ViewOutlineProvider {
        PreviewOutlineProvider() {
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), SizeHelper.dpToPxFloat(12));
        }
    }

    /* JADX INFO: renamed from: t9.y2$d */
    /* JADX INFO: compiled from: UrlSelectionDialogBottomSheet.java */
    class PreviewLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
        PreviewLayoutListener() {
        }

        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            UrlSelectionDialogBottomSheet.this.previewImage.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            int width = UrlSelectionDialogBottomSheet.this.previewImage.getWidth();
            boolean zContains = WebsitePreviewHelper.getPreviewUrl(UrlSelectionDialogBottomSheet.this.getUrl()).contains("github.com");
            UrlSelectionDialogBottomSheet.this.previewImage.getLayoutParams().height = (int) ((width / (zContains ? 1200 : 600)) * (zContains ? 600 : 350));
            UrlSelectionDialogBottomSheet.this.previewImage.requestLayout();
        }
    }

    /* JADX INFO: renamed from: t9.y2$e */
    /* JADX INFO: compiled from: UrlSelectionDialogBottomSheet.java */
    class PreviewRequestListener implements RequestListener<Drawable> {
        PreviewRequestListener() {
        }

        @Override // com.bumptech.glide.request.RequestListener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public boolean onResourceReady(Drawable drawable, Object obj, Target<Drawable> target, DataSource dataSource, boolean z) {
            UrlSelectionDialogBottomSheet.this.progressBar.setVisibility(8);
            UrlSelectionDialogBottomSheet.this.failedImageView.setVisibility(8);
            return false;
        }

        @Override // com.bumptech.glide.request.RequestListener
        public boolean onLoadFailed(GlideException glideException, Object obj, Target<Drawable> target, boolean z) {
            UrlSelectionDialogBottomSheet.this.progressBar.setVisibility(8);
            UrlSelectionDialogBottomSheet.this.failedImageView.setVisibility(0);
            return false;
        }
    }

    public static Bundle buildArguments(String str) {
        Bundle bundle = new Bundle();
        bundle.putString("url", str);
        return bundle;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onPreviewClicked(View view) {
        if (this.failedImageView.getVisibility() == 0) {
            StaticToast.show("Retrying");
            loadPreview();
        }
    }

    private void loadPreview() {
        this.progressBar.setVisibility(0);
        this.failedImageView.setVisibility(8);
        com.bumptech.glide.Glide.with(RedditApplication.getAppContext()).load(WebsitePreviewHelper.getPreviewUrl(getUrl())).centerCrop().listener(new PreviewRequestListener()).into(this.previewImage);
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet
    public void setupItems() {
        this.openInBrowserItem = addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(R.drawable.ic_open_in_browser_white_24dp, "Open in browser"));
        this.copyLinkItem = addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(R.drawable.ic_content_copy_black_24dp, "Copy link address"));
        this.shareLinkItem = addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(R.drawable.outline_share_24, "Share link"));
        BaseTextView baseTextView = new BaseTextView(getContext(), 9, 3, 3);
        baseTextView.setText(getUrl());
        int padding = SizeHelper.dpToPx(16);
        int halfPadding = padding / 2;
        baseTextView.setPadding(padding, halfPadding, padding, halfPadding);
        baseTextView.setTextIsSelectable(true);
        ((LinearLayout) getView().findViewById(R.id.abstract_selection_dialog_bottom_sheet_main_wrapper)).addView(baseTextView, 1);
        ((LinearLayout.LayoutParams) baseTextView.getLayoutParams()).leftMargin = SizeHelper.dpToPx(16);
        ((LinearLayout.LayoutParams) baseTextView.getLayoutParams()).rightMargin = SizeHelper.dpToPx(16);
        ((LinearLayout.LayoutParams) baseTextView.getLayoutParams()).bottomMargin = SizeHelper.dpToPx(16);
        baseTextView.setBackgroundColor(MonetUtil.getCardColorAuto());
        baseTextView.setOutlineProvider(new RoundedOutlineProvider());
        baseTextView.setClipToOutline(true);
        CustomSubtleDividerView customSubtleDividerView = new CustomSubtleDividerView(getActivity());
        customSubtleDividerView.setLayoutParams(new ViewGroup.LayoutParams(-1, SizeHelper.dpToPx(1)));
        ((LinearLayout) getView().findViewById(R.id.abstract_selection_dialog_bottom_sheet_main_wrapper)).addView(customSubtleDividerView, 2);
        if (LinkTypeHelper.isYouTube(getUrl())) {
            String youTubeId = com.resync.app.util.links.LinkHandler.getYouTubeId(getUrl());
            int startMillis = com.resync.app.util.links.LinkHandler.getYouTubeTimestamp(getUrl()) * 1000;
            YouTubePlayerSupportFragmentX youTubePlayerSupportFragmentX = new YouTubePlayerSupportFragmentX();
            FrameLayout frameLayout = new FrameLayout(getActivity());
            frameLayout.setId(R.id.content_wrapper);
            ((LinearLayout) getView().findViewById(R.id.abstract_selection_dialog_bottom_sheet_main_wrapper)).addView(frameLayout, 1);
            ((LinearLayout.LayoutParams) frameLayout.getLayoutParams()).bottomMargin = SizeHelper.dpToPx(16);
            getFragmentManager().beginTransaction().add(R.id.content_wrapper, youTubePlayerSupportFragmentX, "").commit();
            youTubePlayerSupportFragmentX.initialize("YOUR_YOUTUBE_DATA_API_KEY", new YouTubeInitListener(youTubeId, startMillis, frameLayout));
            return;
        }
        if (WebsitePreviewHelper.shouldShowPreview(getUrl())) {
            LinearLayout linearLayout = (LinearLayout) getView().findViewById(R.id.abstract_selection_dialog_bottom_sheet_main_wrapper);
            View viewInflate = LayoutInflater.from(getContext()).inflate(R.layout.fragment_url_selection_dialog_bottomsheet_site_preview, (ViewGroup) linearLayout, false);
            linearLayout.addView(viewInflate, 1);
            this.previewImage = (CustomImageView) viewInflate.findViewById(R.id.image);
            ImageView imageView = (ImageView) viewInflate.findViewById(R.id.failed);
            this.failedImageView = imageView;
            imageView.setImageTintList(ColorStateList.valueOf(MonetUtil.getSecondaryTextColorAuto()));
            this.progressBar = (CustomProgressBar) viewInflate.findViewById(R.id.progress);
            viewInflate.setBackgroundColor(MonetUtil.getCardColorAuto());
            viewInflate.setOutlineProvider(new PreviewOutlineProvider());
            viewInflate.setClipToOutline(true);
            this.previewImage.setOnClickListener(new View.OnClickListener() { // from class: t9.x2
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    UrlSelectionDialogBottomSheet.this.onPreviewClicked(view);
                }
            });
            this.previewImage.getViewTreeObserver().addOnGlobalLayoutListener(new PreviewLayoutListener());
            loadPreview();
        }
    }

    @Override // androidx.fragment.app.Fragment
    /* JADX INFO: renamed from: U1 */
    public void onDestroy() {
        CustomImageView customImageView = this.previewImage;
        if (customImageView != null) {
            customImageView.recycle();
        }
        super.onDestroy();
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public boolean requiresArguments() {
        return true;
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public void loadCustomArguments() {
        this.url = getArguments().getString("url");
    }

    @Override // com.resync.app.dialogs.interfaces.SelectionDialogBottomSheetInterface
    public String getTitle() {
        return "Link options";
    }

    @Override // com.resync.app.dialogs.interfaces.SelectionDialogBottomSheetInterface
    public void onItemSelected(AbstractSelectionDialogBottomSheet.SelectionItem item) {
        if (item.equals(this.openInBrowserItem)) {
            if (this.url.startsWith("/r/")) {
                com.resync.app.util.links.LinkHandler.openExternal(getContext(), "https://reddit.com" + this.url);
            } else if (this.url.startsWith("r/")) {
                com.resync.app.util.links.LinkHandler.openExternal(getContext(), "https://reddit.com/" + this.url);
            } else {
                com.resync.app.util.links.LinkHandler.openExternal(getContext(), this.url);
            }
        } else if (item.equals(this.copyLinkItem)) {
            RedditHelper.copyUrlToClipboard(getContext(), this.url);
            StaticToast.showWithContext("URL copied to clipboard", getContext());
        } else if (item.equals(this.shareLinkItem)) {
            RedditHelper.shareTextWithSubject(getContext(), "Link", this.url);
        }
        dismiss();
    }
}
