package com.resync.app.dialogs;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.resync.app.R;
import com.resync.app.RedditApplication;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractInputDialogBottomSheet;
import com.resync.app.ui.views.responsive.CustomSubtleDividerView;
import java.util.ArrayList;
import okhttp3.internal.http.HttpStatusCodesKt;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.model.AbstractFlair;
import com.resync.app.model.RichTextFlair;
import com.resync.app.model.TextFlair;
import com.resync.app.helpers.PaddingHelper;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.net.oauth.OAuthEmojisRequest;
import com.resync.app.net.oauth.OAuthSelectFlairUserRequest;

/* JADX INFO: renamed from: y9.d */
/* JADX INFO: compiled from: EditUserFlairInputDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class EditUserFlairInputDialogBottomSheet extends AbstractInputDialogBottomSheet {
    String subreddit;
    AbstractFlair flair;

    /* JADX INFO: compiled from: EditUserFlairInputDialogBottomSheet.java */
    class EmojiRecycledViewPool extends RecyclerView.RecycledViewPool {
        EmojiRecycledViewPool() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.RecycledViewPool
        public void putRecycledView(RecyclerView.ViewHolder holder) {
            if (holder instanceof EmojiViewHolder) {
                ((EmojiViewHolder) holder).clearImage();
            }
            super.putRecycledView(holder);
        }
    }

    /* JADX INFO: compiled from: EditUserFlairInputDialogBottomSheet.java */
    class EmojiAdapter extends RecyclerView.Adapter<EmojiViewHolder> {
        ArrayList<OAuthEmojisRequest.Emoji> emojis;

        public EmojiAdapter(ArrayList<OAuthEmojisRequest.Emoji> arrayList) {
            this.emojis = arrayList;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /* JADX INFO: renamed from: N, reason: merged with bridge method [inline-methods] */
        public void onBindViewHolder(EmojiViewHolder item, int position) {
            item.bind(this.emojis.get(position));
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /* JADX INFO: renamed from: O, reason: merged with bridge method [inline-methods] */
        public EmojiViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            return EditUserFlairInputDialogBottomSheet.this.new EmojiViewHolder(new ImageView(viewGroup.getContext()));
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /* JADX INFO: renamed from: n */
        public int getItemCount() {
            return this.emojis.size();
        }
    }

    /* JADX INFO: compiled from: EditUserFlairInputDialogBottomSheet.java */
    class EmojiViewHolder extends RecyclerView.ViewHolder {

        private OAuthEmojisRequest.Emoji emoji;

        public EmojiViewHolder(View view) {
            super(view);
            view.setLayoutParams(new ViewGroup.LayoutParams(-1, SizeHelper.dpToPx(56)));
            PaddingHelper.setPaddingAll(view, 16);
            ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_CENTER);
            view.setOnClickListener(new View.OnClickListener() { // from class: y9.e
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    EditUserFlairInputDialogBottomSheet.EmojiViewHolder.this.onEmojiClick(view2);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void onEmojiClick(View view) {
            EditUserFlairInputDialogBottomSheet.this.mTextInputEditText.append(this.emoji.name);
        }

        public void clearImage() {
            com.bumptech.glide.Glide.with(RedditApplication.getAppContext()).clear((ImageView) this.itemView);
        }

        public void bind(OAuthEmojisRequest.Emoji emoji) {
            this.emoji = emoji;
            com.bumptech.glide.Glide.with(RedditApplication.getAppContext()).load(emoji.url).into((ImageView) this.itemView);
        }
    }

    public static Bundle buildArguments(String str, AbstractFlair abstractFlair) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("subreddit", str);
        bundle.putSerializable("flair", abstractFlair);
        return bundle;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onEmojisLoaded(ArrayList arrayList) {
        if (isDialogShowing()) {
            View customSubtleDividerView = new CustomSubtleDividerView(getActivity());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, SizeHelper.dpToPx(1));
            layoutParams.topMargin = SizeHelper.dpToPx(16);
            customSubtleDividerView.setLayoutParams(layoutParams);
            ((LinearLayout) getView().findViewById(R.id.abstract_input_dialog_bottom_sheet_content)).addView(customSubtleDividerView, 1);
            RecyclerView recyclerView = new RecyclerView(getContext());
            PaddingHelper.setPaddingAll(recyclerView, 16);
            recyclerView.setClipToPadding(false);
            recyclerView.setRecycledViewPool(new EmojiRecycledViewPool());
            recyclerView.setLayoutParams(new LinearLayout.LayoutParams(-1, SizeHelper.dpToPx(HttpStatusCodesKt.HTTP_OK)));
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 6));
            recyclerView.setAdapter(new EmojiAdapter(arrayList));
            ((LinearLayout) getView().findViewById(R.id.abstract_input_dialog_bottom_sheet_content)).addView(recyclerView, 2);
            CustomSubtleDividerView customSubtleDividerView2 = new CustomSubtleDividerView(getActivity());
            customSubtleDividerView2.setLayoutParams(new ViewGroup.LayoutParams(-1, SizeHelper.dpToPx(1)));
            ((LinearLayout) getView().findViewById(R.id.abstract_input_dialog_bottom_sheet_content)).addView(customSubtleDividerView2, 3);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void onEmojisError(VolleyError volleyError) {
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractInputDialogBottomSheet
    public void onInputSetup() {
        AbstractFlair abstractFlair = this.flair;
        if (abstractFlair instanceof RichTextFlair) {
            setInputText(((RichTextFlair) abstractFlair).getDisplayString());
        } else if (abstractFlair instanceof TextFlair) {
            setInputText(((TextFlair) abstractFlair).getDisplayText());
        }
        OAuthRequestManager.sendWithAppContext(new OAuthEmojisRequest(this.subreddit, new Response.Listener() { // from class: y9.c
            @Override // com.android.volley.Response.Listener
            public final void onResponse(Object obj) {
                EditUserFlairInputDialogBottomSheet.this.onEmojisLoaded((ArrayList) obj);
            }
        }, new Response.ErrorListener() { // from class: y9.b
            @Override // com.android.volley.Response.ErrorListener
            public final void onErrorResponse(VolleyError volleyError) {
                EditUserFlairInputDialogBottomSheet.onEmojisError(volleyError);
            }
        }));
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractInputDialogBottomSheet
    public void onPositiveButtonClicked(String str) {
        AbstractFlair abstractFlair = this.flair;
        TextFlair textFlair = new TextFlair(abstractFlair.id, abstractFlair.getCssClass());
        textFlair.setText(this.mTextInputEditText.getText().toString());
        OAuthRequestManager.sendWithAppContext(new OAuthSelectFlairUserRequest(RedditApplication.getAppContext(), this.subreddit, textFlair));
        dismissAllowingStateLoss();
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractInputDialogBottomSheet
    public void setInputText(String str) {
        this.mTextInputEditText.setText(str);
        if (StringUtils.isNotEmpty(str)) {
            this.mTextInputEditText.setSelection(str.length());
        }
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public boolean requiresArguments() {
        return true;
    }

    @Override // com.resync.app.dialogs.interfaces.InputDialogBottomSheetInterface
    public String getPositiveButtonText() {
        return "Submit";
    }

    @Override // com.resync.app.dialogs.interfaces.InputDialogBottomSheetInterface
    public String getHint() {
        return "Flair text";
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public void loadCustomArguments() {
        this.subreddit = getArguments().getString("subreddit");
        this.flair = (AbstractFlair) getArguments().getSerializable("flair");
    }

    @Override // com.resync.app.dialogs.interfaces.InputDialogBottomSheetInterface
    public String getTitle() {
        return "Edit user flair";
    }
}
