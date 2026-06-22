package com.resync.app.ui.fragment_dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatDialogFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.resync.app.R;
import com.resync.app.helpers.ui.AlertDialogHelper;
import com.resync.app.widgets.ColorFilterProvider;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.fragments.SavedCategoryFilterFragment;
import com.resync.app.helpers.ContextHelper;
import com.resync.app.helpers.NightHelper;
import com.resync.app.model.Post;
import com.resync.app.net.oauth.OAuthHasGoldRequest;
import com.resync.app.net.oauth.OAuthSavedCategoriesRequest;

/* JADX INFO: loaded from: classes.dex */
public class SavedFragment extends AppCompatDialogFragment {

    @BindView(R.id.fragment_saved_error_icon)
    ImageView errorIcon;

    @BindView(R.id.fragment_saved_error_text)
    TextView errorText;

    @BindView(R.id.fragment_saved_error_wrapper)
    LinearLayout errorWrapper;

    @BindView(R.id.fragment_saved_progress)
    ProgressBar progressBar;

    @BindView(R.id.fragment_saved_progress_wrapper)
    RelativeLayout progressBarWrapper;

    class GoldCheckListener implements Response.Listener<Boolean> {

        final /* synthetic */ int mode;
        final /* synthetic */ Post post;

        class SavedResponseListener implements Response.Listener<String[]> {
            SavedResponseListener() {
            }

            @Override // com.android.volley.Response.Listener
            /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
            public void onResponse(String[] strArr) {
                if (ContextHelper.isActivityFinishing(SavedFragment.this.getActivity())) {
                    return;
                }
                if (strArr.length == 0) {
                    GoldCheckListener goldCheckListener = GoldCheckListener.this;
                    if (goldCheckListener.mode == 1) {
                        SavedFragment.this.showError("You have no saved categories", R.drawable.ic_error_red_24dp);
                        return;
                    }
                }
                SavedCategoryFilterFragment.newInstance(strArr, GoldCheckListener.this.post).show(SavedFragment.this.getActivity().getSupportFragmentManager(), "saved_category_filter_fragment");
                SavedFragment.this.dismissSafely();
            }
        }

        class CategoriesErrorListener implements Response.ErrorListener {
            CategoriesErrorListener() {
            }

            @Override // com.android.volley.Response.ErrorListener
            public void onErrorResponse(VolleyError volleyError) {
                SavedFragment.this.showError("Error grabbing saved categories", R.drawable.ic_error_red_24dp);
            }
        }

        GoldCheckListener(int mode, Post post) {
            this.mode = mode;
            this.post = post;
        }

        @Override // com.android.volley.Response.Listener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public void onResponse(Boolean bool) {
            if (ContextHelper.isActivityFinishing(SavedFragment.this.getActivity())) {
                return;
            }
            if (!bool.booleanValue()) {
                SavedFragment.this.showError("This feature requires reddit gold", R.drawable.ic_error_red_24dp);
                return;
            }
            int mode = this.mode;
            if (mode == 0) {
                SavedSubredditFilterFragment.newInstance().show(SavedFragment.this.getActivity().getSupportFragmentManager(), "saved_subreddit_filter_fragment");
                SavedFragment.this.dismissSafely();
            } else {
                if (mode == 1 || mode == 2) {
                    OAuthRequestManager.send(SavedFragment.this.getActivity(), new OAuthSavedCategoriesRequest(SavedFragment.this.getActivity(), new SavedResponseListener(), new CategoriesErrorListener()));
                    return;
                }
                throw new RuntimeException("Unsupported mode: " + this.mode);
            }
        }
    }

    class GoldCheckErrorListener implements Response.ErrorListener {
        GoldCheckErrorListener() {
        }

        @Override // com.android.volley.Response.ErrorListener
        public void onErrorResponse(VolleyError volleyError) {
            if (ContextHelper.isActivityFinishing(SavedFragment.this.getActivity())) {
                return;
            }
            SavedFragment.this.showError("Error checking gold status", R.drawable.ic_error_red_24dp);
        }
    }

    public static SavedFragment newInstance(Post post) {
        SavedFragment savedFragment = new SavedFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("mode", 2);
        bundle.putSerializable("Post", post);
        savedFragment.setArguments(bundle);
        return savedFragment;
    }

    @Override // androidx.appcompat.app.AppCompatDialogFragment, androidx.fragment.app.c
    public Dialog onCreateDialog(Bundle bundle) {
        int mode = getArguments().getInt("mode");
        Post post = getArguments().containsKey("Post") ? (Post) getArguments().getSerializable("Post") : null;
        View viewInflate = View.inflate(getActivity(), R.layout.fragment_saved, null);
        ButterKnife.bind(this, viewInflate);
        OAuthRequestManager.send(getActivity(), new OAuthHasGoldRequest(getActivity(), new GoldCheckListener(mode, post), new GoldCheckErrorListener()));
        return AlertDialogHelper.newBuilder(getActivity()).setView(viewInflate).setCancelable(true).create();
    }

    protected void dismissSafely() {
        try {
            dismiss();
        } catch (Exception unused) {
        }
    }

    protected void showError(String str, int iconRes) {
        this.progressBarWrapper.setVisibility(8);
        this.errorWrapper.setVisibility(0);
        this.errorText.setText(str);
        this.errorIcon.setImageResource(iconRes);
        this.errorIcon.setColorFilter(ColorFilterProvider.getColorFilter(NightHelper.isNight() ? -1 : -16777216));
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        dismissSafely();
        super.onPause();
    }
}
