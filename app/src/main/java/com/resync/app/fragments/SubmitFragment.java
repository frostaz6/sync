package com.resync.app.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.MaterialPostsHelper;
import com.bumptech.glide.RedditHelper;
import com.google.android.exoplayer2.util.MimeTypes;
import com.resync.app.R;
import com.resync.app.RedditApplication;
import com.resync.app.singleton.AccountSingleton;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.activities.BaseActivity;
import com.resync.app.ui.activities.CommentsActivity;
import com.resync.app.ui.fragment_dialogs.bottom.SubredditRulesBottomSheetFragment;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.flair.PostPickFlairSelectionDialogBottomSheet;
import com.resync.app.ui.views.editor.SubmitEditorView;
import com.resync.app.ui.views.monet.MonetSwitch;
import com.resync.app.ui.views.text.spannable.children.image_components.flair.PostFlairTextView;
import com.squareup.otto.Subscribe;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.ui.ProgressDialogHelper;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.helpers.ui.SubredditArrayList;
import java.io.File;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.managers.SubredditsManager;
import com.resync.app.events.FlairSelectedEvent;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.singletons.StyleSingleton;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.model.Subreddits;
import com.resync.app.net.thirdparty.SuggestTitleRequest;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.helpers.ContextHelper;
import com.resync.app.helpers.TypefaceManager;
import com.resync.app.net.thirdparty.ImgurUploadError;
import com.resync.app.net.thirdparty.UploadManager;
import com.resync.app.util.Logger;
import com.resync.app.media.glide.ImageHelper;
import com.resync.app.net.oauth.OAuthSubmitRequest;

/* JADX INFO: compiled from: SubmitFragment.java */
/* JADX INFO: loaded from: classes2.dex */
public class SubmitFragment extends Fragment {
    private MonetSwitch repliesSwitch;
    private MonetSwitch spoilerSwitch;
    private MonetSwitch nsfwSwitch;
    SuggestTitleRequest suggestTitleRequest;
    public Uri imageUri;

    /* JADX INFO: renamed from: p0, reason: collision with root package name */
    int loadedState = 0;
    private int submitType;
    private PostFlairTextView flairView;

    /* JADX INFO: renamed from: s0, reason: collision with root package name */
    private EditText titleEditText;
    private EditText urlEditText;

    /* JADX INFO: renamed from: u0, reason: collision with root package name */
    private TextView suggestTitleView;

    /* JADX INFO: renamed from: v0, reason: collision with root package name */
    private SubmitEditorView editorView;
    private AutoCompleteTextView subredditEditText;

    /* JADX INFO: renamed from: x0, reason: collision with root package name */
    private ImageView imageView;
    private ProgressDialog uploadProgressDialog;
    private Button editButton;

    /* JADX INFO: compiled from: SubmitFragment.java */
    class SubmitErrorListener implements Response.ErrorListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ ProgressDialog progressDialog;

        SubmitErrorListener(ProgressDialog progressDialog) {
            this.progressDialog = progressDialog;
        }

        @Override // com.android.volley.Response.ErrorListener
        public void onErrorResponse(VolleyError volleyError) {
            if (SubmitFragment.this.getActivity() == null) {
                return;
            }
            this.progressDialog.dismiss();
            String message = volleyError.getMessage();
            if ("care to try these again?".equalsIgnoreCase(message)) {
                message = "Bad captcha";
            }
            if (StringUtils.isEmpty(message)) {
                message = "Could not submit at this time";
            }
            Toast.makeText(SubmitFragment.this.getActivity(), message, 1).show();
        }
    }

    /* JADX INFO: compiled from: SubmitFragment.java */
    class UrlEditorActionListener implements TextView.OnEditorActionListener {
        UrlEditorActionListener() {
        }

        @Override // android.widget.TextView.OnEditorActionListener
        public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
            if (actionId != 2) {
                return false;
            }
            SubmitFragment.this.onSubmitClicked();
            return true;
        }
    }

    /* JADX INFO: compiled from: SubmitFragment.java */
    class FlairClickListener implements View.OnClickListener {
        FlairClickListener() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (StringUtils.isEmpty(SubmitFragment.this.subredditEditText.getText())) {
                StaticToast.show("Pick a subreddit");
            } else {
                FragmentUtils.showWithSubreddit(PostPickFlairSelectionDialogBottomSheet.class, SubmitFragment.this.getChildFragmentManager(), SubmitFragment.this.subredditEditText.getText().toString());
            }
        }
    }

    /* JADX INFO: compiled from: SubmitFragment.java */
    class EditImageClickListener implements View.OnClickListener {
        EditImageClickListener() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            SubmitFragment.this.cropImage();
        }
    }

    /* JADX INFO: compiled from: SubmitFragment.java */
    class TakePhotoClickListener implements View.OnClickListener {
        TakePhotoClickListener() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            SubmitFragment.this.takePhoto();
        }
    }

    /* JADX INFO: compiled from: SubmitFragment.java */
    class ChooseImageClickListener implements View.OnClickListener {
        ChooseImageClickListener() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            Intent intent = new Intent("android.intent.action.GET_CONTENT", (Uri) null);
            String[] strArr = {"image/png", "image/jpg", MimeTypes.IMAGE_JPEG};
            intent.setType("*/*");
            intent.putExtra("android.intent.extra.MIME_TYPES", strArr);
            SubmitFragment.this.startActivityForResult(intent, 1);
        }
    }

    /* JADX INFO: compiled from: SubmitFragment.java */
    class SuggestTitleClickListener implements View.OnClickListener {

        /* JADX INFO: compiled from: SubmitFragment.java */
        class SuggestTitleListener implements Response.Listener<String> {
            SuggestTitleListener() {
            }

            @Override // com.android.volley.Response.Listener
            /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
            public void onResponse(String str) {
                if (ContextHelper.isActivityFinishing(SubmitFragment.this.getActivity())) {
                    return;
                }
                SubmitFragment.this.titleEditText.setText(str);
                StaticToast.showWithContext(SubmitFragment.this.getActivity(), "Title found: " + str);
            }
        }

        /* JADX INFO: compiled from: SubmitFragment.java */
        class SuggestTitleErrorListener implements Response.ErrorListener {
            SuggestTitleErrorListener() {
            }

            @Override // com.android.volley.Response.ErrorListener
            public void onErrorResponse(VolleyError volleyError) {
                StaticToast.showWithContext(SubmitFragment.this.getActivity(), "Failed to find a title");
            }
        }

        SuggestTitleClickListener() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            SuggestTitleRequest suggestTitleRequest = SubmitFragment.this.suggestTitleRequest;
            if (suggestTitleRequest != null) {
                suggestTitleRequest.cancel();
            }
            SubmitFragment fragment = SubmitFragment.this;
            fragment.suggestTitleRequest = new SuggestTitleRequest(fragment.urlEditText.getText().toString().trim(), new SuggestTitleListener(), new SuggestTitleErrorListener());
            OAuthRequestManager.enqueueAux(SubmitFragment.this.suggestTitleRequest);
        }
    }

    /* JADX INFO: compiled from: SubmitFragment.java */
    class UploadResponseListener implements Response.Listener<String> {
        UploadResponseListener() {
        }

        @Override // com.android.volley.Response.Listener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public void onResponse(String str) {
            try {
                if (SubmitFragment.this.uploadProgressDialog != null && SubmitFragment.this.uploadProgressDialog.isShowing()) {
                    SubmitFragment.this.uploadProgressDialog.dismiss();
                }
            } catch (Exception e) {
                Logger.logException(e);
            }
            SubmitFragment.this.urlEditText.setText(str);
            SubmitFragment.this.trySubmit();
        }
    }

    /* JADX INFO: compiled from: SubmitFragment.java */
    class UploadErrorListener implements Response.ErrorListener {
        UploadErrorListener() {
        }

        @Override // com.android.volley.Response.ErrorListener
        public void onErrorResponse(VolleyError volleyError) {
            if (volleyError instanceof ImgurUploadError) {
                try {
                    if (SubmitFragment.this.uploadProgressDialog == null || !SubmitFragment.this.uploadProgressDialog.isShowing()) {
                        return;
                    }
                    SubmitFragment.this.uploadProgressDialog.dismiss();
                    StaticToast.showWithContext(SubmitFragment.this.getActivity(), volleyError.getMessage());
                    return;
                } catch (Exception e) {
                    Logger.logException(e);
                    return;
                }
            }
            try {
                if (SubmitFragment.this.uploadProgressDialog == null || !SubmitFragment.this.uploadProgressDialog.isShowing()) {
                    return;
                }
                SubmitFragment.this.uploadProgressDialog.dismiss();
                StaticToast.showWithContext(SubmitFragment.this.getActivity(), "Failed to upload image");
            } catch (Exception e) {
                Logger.logException(e);
            }
        }
    }

    /* JADX INFO: compiled from: SubmitFragment.java */
    class SubmitResponseListener implements Response.Listener<String> {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ ProgressDialog progressDialog;
        final /* synthetic */ String subreddit;

        SubmitResponseListener(ProgressDialog progressDialog, String str) {
            this.progressDialog = progressDialog;
            this.subreddit = str;
        }

        @Override // com.android.volley.Response.Listener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public void onResponse(String str) {
            if (SubmitFragment.this.getActivity() == null) {
                return;
            }
            this.progressDialog.dismiss();
            SubmitFragment fragment = SubmitFragment.this;
            fragment.startActivity(CommentsActivity.createIntent(fragment.getActivity(), str, null, this.subreddit, false));
            SubmitFragment.this.getActivity().finish();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onRulesClicked(View view) {
        String string = this.subredditEditText.getText().toString();
        if (StringUtils.isEmpty(string)) {
            StaticToast.show("Enter a subreddit");
        } else {
            FragmentUtils.showWithSubreddit(SubredditRulesBottomSheetFragment.class, getParentFragmentManager(), string);
        }
    }

    public static SubmitFragment newInstance(int submitType, String str, String str2, boolean z, String str3, String str4) {
        SubmitFragment fragment = new SubmitFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("submit_type", submitType);
        if (!MaterialPostsHelper.isAggregateFeed(str) && !MaterialPostsHelper.isModAbout(str) && !MaterialPostsHelper.isSearch(str) && !MaterialPostsHelper.isSubSearch(str) && !MaterialPostsHelper.isDomain(str) && !MaterialPostsHelper.isUserProfile(str)) {
            bundle.putString("subreddit", str);
        }
        bundle.putString("l", str2);
        bundle.putBoolean("p", z);
        bundle.putString("t", str3);
        bundle.putString("m", str4);
        fragment.setArguments(bundle);
        return fragment;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void trySubmit() {
        if (getActivity() == null || ((BaseActivity) getActivity()).isClosing() || isDetached() || isRemoving() || !isVisible()) {
            return;
        }
        submitPost(null, null);
    }

    private void submitPost(String str, String str2) {
        String string;
        int kind = 1;
        ProgressDialog progressDialogShow = ProgressDialog.show(getActivity(), "", "Submitting", true);
        progressDialogShow.setCancelable(true);
        String string2 = this.titleEditText.getText().toString();
        String string3 = this.subredditEditText.getText().toString();
        int type = this.submitType;
        if (type == 0) {
            kind = 0;
            string = this.editorView.getBody();
        } else {
            string = type == 2 ? this.urlEditText.getText().toString() : this.urlEditText.getText().toString();
        }
        OAuthRequestManager.send(getActivity(), new OAuthSubmitRequest(getActivity(), string2, string, string3, kind, str, str2, this.repliesSwitch.isChecked(), this.spoilerSwitch.isChecked(), this.nsfwSwitch.isChecked(), this.flairView.getFlair(), new SubmitResponseListener(progressDialogShow, string3), new SubmitErrorListener(progressDialogShow)));
        progressDialogShow.show();
    }

    public void cropImage() {
        try {
            CropImage.activity(this.imageUri).setGuidelines(CropImageView.Guidelines.ON).setOutputUri(ImageHelper.createTempImageUri()).start(getContext(), this);
        } catch (Exception e) {
            StaticToast.show("Error starting cropper");
            Logger.logException(e);
        }
    }

    public void onImageSelected(Uri uri) {
        this.imageUri = uri;
        this.imageView.setVisibility(0);
        this.editButton.setEnabled(true);
        com.bumptech.glide.Glide.with(RedditApplication.getAppContext()).load(uri).into(this.imageView);
        this.loadedState = 1;
    }

    public void onPhotoCaptured(Uri uri) {
        this.imageView.setVisibility(0);
        getView().findViewById(R.id.edit).setEnabled(true);
        com.bumptech.glide.Glide.with(RedditApplication.getAppContext()).load(uri).into(this.imageView);
        this.loadedState = 3;
    }

    public void onSubmitClicked() {
        ((InputMethodManager) getActivity().getSystemService("input_method")).hideSoftInputFromWindow(this.titleEditText.getWindowToken(), 0);
        if (!AccountSingleton.getInstance().isLoggedIn()) {
            StaticToast.showWithContext("You must be logged in to submit links", getActivity());
            return;
        }
        if (this.titleEditText.getText().toString().length() <= 0) {
            StaticToast.showWithContext("You must enter a title", getActivity());
            return;
        }
        if (this.subredditEditText.getText().toString().length() <= 0) {
            StaticToast.showWithContext("You must enter a subreddit", getActivity());
            return;
        }
        if (this.submitType != 2) {
            trySubmit();
            return;
        }
        ProgressDialog progressDialogA = ProgressDialogHelper.create(getActivity());
        this.uploadProgressDialog = progressDialogA;
        progressDialogA.setProgressStyle(1);
        this.uploadProgressDialog.setMessage("Uploading...");
        try {
            if (this.imageUri.toString().startsWith("content://")) {
                long jAvailable = getActivity().getContentResolver().openInputStream(this.imageUri).available();
                Logger.verbose("Max: " + jAvailable);
                this.uploadProgressDialog.setMax((int) jAvailable);
            } else {
                long length = new File(this.imageUri.toString()).length();
                Logger.verbose("Max: " + length);
                this.uploadProgressDialog.setMax((int) (length / 1024));
            }
        } catch (Exception e) {
            Logger.logException(e);
        }
        this.uploadProgressDialog.setIndeterminate(false);
        this.uploadProgressDialog.show();
        UploadManager.upload(getActivity(), this.imageUri, new UploadResponseListener(), new UploadErrorListener());
    }

    public void takePhoto() {
        try {
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            Uri uriA = ImageHelper.createTempImageUri();
            intent.putExtra("output", uriA);
            intent.setFlags(1);
            intent.setFlags(2);
            this.imageUri = uriA;
            Logger.verbose("Filename: " + this.imageUri);
            startActivityForResult(intent, 3);
        } catch (Exception e) {
            StaticToast.show("Error starting camera");
            Logger.logException(e);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 1) {
            if (intent == null || intent.getData() == null) {
                StaticToast.showWithContext(getActivity(), "Selection cancelled");
                return;
            }
            onImageSelected(Uri.parse(intent.getDataString()));
        } else if (requestCode == 3) {
            try {
                getActivity().getContentResolver().notifyChange(this.imageUri, null);
                Logger.verbose("Filename: " + this.imageUri);
                onPhotoCaptured(this.imageUri);
            } catch (Exception e) {
                Logger.logException(e);
            }
        }
        if (requestCode == 203) {
            CropImage.ActivityResult activityResultB = CropImage.getActivityResult(intent);
            if (resultCode != -1) {
                if (resultCode == 204) {
                    StaticToast.showWithContext(getContext(), "Error editing image!");
                }
            } else {
                if (intent == null) {
                    return;
                }
                this.imageView.setImageBitmap(null);
                this.imageUri = activityResultB.getUri();
                com.bumptech.glide.Glide.with(RedditApplication.getAppContext()).load(this.imageUri).into(this.imageView);
            }
        }
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        String string = getArguments().getString("m");
        if (getArguments().containsKey("m")) {
            getArguments().remove("m");
        }
        String string2 = getArguments().getString("t");
        if (getArguments().containsKey("t")) {
            getArguments().remove("t");
        }
        this.submitType = getArguments().getInt("submit_type");
        boolean z = getArguments().getBoolean("p");
        View viewInflate = layoutInflater.inflate(R.layout.fragment_submit, (ViewGroup) null);
        SubredditArrayList subredditArrayList = new SubredditArrayList();
        subredditArrayList.addAll(SubredditsManager.getInstance().getSubreddits());
        int i = 0;
        while (true) {
            String[] strArr = Subreddits.DEFAULTS;
            if (i >= strArr.length) {
                break;
            }
            if (!subredditArrayList.contains(strArr[i])) {
                subredditArrayList.add(strArr[i]);
            }
            i++;
        }
        String[] strArr2 = new String[subredditArrayList.size()];
        subredditArrayList.toArray(strArr2);
        ((AutoCompleteTextView) viewInflate.findViewById(R.id.subreddit)).setAdapter(new ArrayAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, strArr2));
        PostFlairTextView postFlairTextView = (PostFlairTextView) viewInflate.findViewById(R.id.flair);
        this.flairView = postFlairTextView;
        postFlairTextView.setTextSize(1, RedditHelper.getSmallHeaderTextSize(SettingsSingleton.getSettings().fontSize));
        EditText editText = (EditText) viewInflate.findViewById(R.id.title);
        this.titleEditText = editText;
        editText.setTextColor(StyleSingleton.getTextColorAuto(RedditApplication.getAppContext(), false));
        this.titleEditText.setHintTextColor(MonetUtil.getSecondaryTextColorAuto());
        this.titleEditText.setTextSize(1, RedditHelper.getSmallHeaderTextSize(SettingsSingleton.getSettings().fontSize));
        this.titleEditText.setHorizontallyScrolling(false);
        this.titleEditText.setMaxLines(Integer.MAX_VALUE);
        if (bundle != null && bundle.containsKey("t")) {
            this.titleEditText.setText(bundle.getString("t"));
        } else if (!TextUtils.isEmpty(string2)) {
            this.titleEditText.setText(string2);
            EditText editText2 = this.titleEditText;
            editText2.setSelection(editText2.length());
            this.titleEditText.clearFocus();
            getActivity().getWindow().setSoftInputMode(3);
        }
        this.titleEditText.setTypeface(TypefaceManager.getRegular());
        this.suggestTitleView = (TextView) viewInflate.findViewById(R.id.suggest_title);
        this.urlEditText = (EditText) viewInflate.findViewById(R.id.url);
        if (bundle != null && bundle.containsKey("u")) {
            this.urlEditText.setText(bundle.getString("u"));
        }
        this.urlEditText.setTypeface(TypefaceManager.getRegular());
        String string3 = getArguments().getString("l");
        if (string3 != null && string3.length() > 0) {
            this.urlEditText.setText(string3);
        }
        SubmitEditorView submitEditorView = (SubmitEditorView) viewInflate.findViewById(R.id.editor_view);
        this.editorView = submitEditorView;
        submitEditorView.setActivity((BaseActivity) getActivity());
        if (bundle != null && bundle.containsKey("m")) {
            this.editorView.setSelftextEmpty(bundle.getString("m"));
        } else if (TextUtils.isEmpty(string)) {
            this.editorView.setSelftextEmpty(null);
        } else {
            this.editorView.setSelftextEmpty(string);
        }
        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) viewInflate.findViewById(R.id.subreddit);
        this.subredditEditText = autoCompleteTextView;
        autoCompleteTextView.setTextColor(StyleSingleton.getTextColorAuto(RedditApplication.getAppContext(), false));
        this.subredditEditText.setHintTextColor(MonetUtil.getSecondaryTextColorAuto());
        if (bundle != null && bundle.containsKey("s")) {
            this.subredditEditText.setText(bundle.getString("s"));
        }
        this.subredditEditText.setTypeface(TypefaceManager.getRegular());
        this.subredditEditText.setOnEditorActionListener(new UrlEditorActionListener());
        this.repliesSwitch = (MonetSwitch) viewInflate.findViewById(R.id.replies);
        this.spoilerSwitch = (MonetSwitch) viewInflate.findViewById(R.id.spoiler);
        this.nsfwSwitch = (MonetSwitch) viewInflate.findViewById(R.id.nsfw);
        this.repliesSwitch.setTextSize(1, RedditHelper.getSmallHeaderTextSize(SettingsSingleton.getSettings().fontSize));
        this.repliesSwitch.setTextColor(StyleSingleton.getTextColorAuto(RedditApplication.getAppContext(), false));
        this.spoilerSwitch.setTextSize(1, RedditHelper.getSmallHeaderTextSize(SettingsSingleton.getSettings().fontSize));
        this.spoilerSwitch.setTextColor(StyleSingleton.getTextColorAuto(RedditApplication.getAppContext(), false));
        this.nsfwSwitch.setTextSize(1, RedditHelper.getSmallHeaderTextSize(SettingsSingleton.getSettings().fontSize));
        this.nsfwSwitch.setTextColor(StyleSingleton.getTextColorAuto(RedditApplication.getAppContext(), false));
        this.flairView.setOnClickListener(new FlairClickListener());
        ((TextView) viewInflate.findViewById(R.id.rules)).setTextColor(StyleSingleton.getTextColorAuto(RedditApplication.getAppContext(), false));
        viewInflate.findViewById(R.id.rules).setOnClickListener(new View.OnClickListener() { // from class: ma.m
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                SubmitFragment.this.onRulesClicked(view);
            }
        });
        this.imageView = (ImageView) viewInflate.findViewById(R.id.image);
        this.editButton = (Button) viewInflate.findViewById(R.id.edit);
        String string4 = getArguments().getString("subreddit");
        if (string4 != null && string4.length() > 0 && !string4.equalsIgnoreCase("frontpage") && !string4.equalsIgnoreCase("frontpage") && !string4.contains("multi_") && !string4.contains("+") && !string4.contains("-")) {
            this.subredditEditText.setText(string4.replace("redditsync_casual_", ""));
        }
        int type = this.submitType;
        if (type == 1) {
            this.urlEditText.setVisibility(0);
            this.urlEditText.setTextColor(StyleSingleton.getTextColorAuto(RedditApplication.getAppContext(), false));
            this.urlEditText.setHintTextColor(MonetUtil.getSecondaryTextColorAuto());
            viewInflate.findViewById(R.id.url_div).setVisibility(0);
            this.suggestTitleView.setVisibility(0);
            this.suggestTitleView.setTextColor(StyleSingleton.getTextColorAuto(RedditApplication.getAppContext(), false));
        } else if (type == 0) {
            this.editorView.setVisibility(0);
        } else if (type == 2) {
            viewInflate.findViewById(R.id.photo).setVisibility(0);
            ((Button) viewInflate.findViewById(R.id.edit)).setOnClickListener(new EditImageClickListener());
            Button button = (Button) viewInflate.findViewById(R.id.take);
            button.setOnClickListener(new TakePhotoClickListener());
            if (!getActivity().getPackageManager().hasSystemFeature("android.hardware.camera")) {
                button.setEnabled(false);
            }
            ((Button) viewInflate.findViewById(R.id.choose)).setOnClickListener(new ChooseImageClickListener());
        }
        if (bundle != null && bundle.containsKey("loaded")) {
            int loaded = bundle.getInt("loaded");
            this.loadedState = loaded;
            if (loaded == 3 || loaded == 2 || loaded == 1) {
                Uri uri = (Uri) bundle.getParcelable("uri");
                this.imageUri = uri;
                int state = this.loadedState;
                if (state == 3) {
                    onPhotoCaptured(uri);
                } else if (state == 1) {
                    onImageSelected(uri);
                }
            }
        }
        if (z) {
            onImageSelected(Uri.parse(string3));
        }
        this.suggestTitleView.setOnClickListener(new SuggestTitleClickListener());
        this.titleEditText.requestFocus();
        return viewInflate;
    }

    @Override // androidx.fragment.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        EditText editText = this.titleEditText;
        if (editText != null && editText.getText() != null && this.titleEditText.getText().toString() != null && this.titleEditText.getText().toString().length() > 0) {
            bundle.putString("t", this.titleEditText.getText().toString());
        }
        EditText editText2 = this.urlEditText;
        if (editText2 != null && editText2.getText() != null && this.urlEditText.getText().toString() != null && this.urlEditText.getText().toString().length() > 0) {
            bundle.putString("u", this.urlEditText.getText().toString());
        }
        if (this.editorView.hasContent()) {
            bundle.putString("m", this.editorView.getBody());
        }
        AutoCompleteTextView autoCompleteTextView = this.subredditEditText;
        if (autoCompleteTextView != null && autoCompleteTextView.getText() != null && this.subredditEditText.getText().toString() != null && this.subredditEditText.getText().toString().length() > 0) {
            bundle.putString("s", this.subredditEditText.getText().toString());
        }
        Uri uri = this.imageUri;
        if (uri != null) {
            bundle.putParcelable("uri", uri);
        }
        super.onSaveInstanceState(bundle);
    }

    @Override // androidx.fragment.app.Fragment
    /* JADX INFO: renamed from: m2 */
    public void onStart() {
        super.onStart();
        BusSingleton.getBus().register(this);
    }

    @Override // androidx.fragment.app.Fragment
    /* JADX INFO: renamed from: n2 */
    public void onStop() {
        BusSingleton.getBus().unregister(this);
        super.onStop();
    }

    @Subscribe
    public void onFlairSelected(FlairSelectedEvent flairSelectedEvent) {
        this.flairView.bindFlair(flairSelectedEvent.flair);
    }

    public boolean canExit() {
        if (this.submitType == 0) {
            return this.editorView.checkDraft(false);
        }
        return true;
    }
}
