package com.resync.app.ui.views.editor;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Build;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import butterknife.ButterKnife;
import com.bumptech.glide.RedditHelper;
import com.resync.app.R;
import com.resync.app.provider.RedditProvider;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.singleton.UiSingleton;
import com.resync.app.ui.activities.BaseActivity;
import com.resync.app.ui.fragment_dialogs.InsertImageDialogFragment;
import com.resync.app.ui.fragment_dialogs.UploadImageDialogFragment;
import com.resync.app.ui.views.buttons.FormattingButton;
import com.resync.app.ui.views.buttons.HideButton;
import com.resync.app.ui.views.responsive.CustomSubtleDividerView;
import com.resync.app.ui.views.text.BaseTextView;
import com.squareup.otto.Subscribe;
import com.resync.app.helpers.ui.AlertDialogHelper;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.ui.StaticToast;
import java.util.ArrayList;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.managers.DraftManager;
import com.resync.app.events.OnImageKeyboardImageEvent;
import com.resync.app.events.TempAccountSelectedEvent;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.premium.SyncIapHelper;
import com.resync.app.model.UserAccount;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.dialogs.PreviewCommentBottomSheetFragment;
import com.resync.app.dialogs.DraftCheckerAlertDialogBottomSheet;
import com.resync.app.premium.UltraHelper;
import com.resync.app.util.Logger;

/* JADX INFO: loaded from: classes3.dex */
public class EditorView extends RelativeLayout {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private BaseActivity activity;
    private boolean messaging;

    FormattingButton buttonBold;

    FormattingButton buttonBugReport;

    FormattingButton buttonBullet;

    FormattingButton buttonDraft;

    FormattingButton buttonEmoji;

    FormattingButton buttonImage;

    FormattingButton buttonInfo;

    FormattingButton buttonItalic;

    FormattingButton buttonLink;

    FormattingButton buttonNumbered;

    FormattingButton buttonPreview;

    FormattingButton buttonPromo;

    FormattingButton buttonSettings;

    FormattingButton buttonSpeech;

    FormattingButton buttonSpoiler;

    FormattingButton buttonStrikethrough;

    FormattingButton buttonSuperscript;

    FormattingButton buttonUser;

    CustomSubtleDividerView dividerView;

    GifCustomEditText editor;

    BaseTextView editorPreview;

    ScrollView editorPreviewWrapper;

    LinearLayout mFormattingWrapper;

    HorizontalScrollView mHorScroll;

    TextView mMaxLength;

    BaseTextView mUserDisplay;

    class StyleActionModeCallback implements ActionMode.Callback {
        StyleActionModeCallback() {
        }

        @Override // android.view.ActionMode.Callback
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            if (menuItem.getItemId() == R.id.style_cut) {
                EditorView.this.editor.onTextContextMenuItem(android.R.id.cut);
                return false;
            }
            if (menuItem.getItemId() == R.id.style_copy) {
                EditorView.this.editor.onTextContextMenuItem(android.R.id.copy);
                return false;
            }
            if (menuItem.getItemId() == R.id.style_paste) {
                EditorView.this.editor.onTextContextMenuItem(android.R.id.paste);
                return false;
            }
            if (menuItem.getItemId() == R.id.style_bold) {
                EditorView.this.onBoldClicked();
                return false;
            }
            if (menuItem.getItemId() == R.id.style_italic) {
                EditorView.this.onItalicClicked();
                return false;
            }
            if (menuItem.getItemId() == R.id.style_strikethrough) {
                EditorView.this.onStrikethroughClicked();
                return false;
            }
            if (menuItem.getItemId() == R.id.style_quote) {
                EditorView.this.onSpeechClicked();
                return false;
            }
            if (menuItem.getItemId() != R.id.style_link) {
                return false;
            }
            EditorView.this.onLinkClicked();
            return false;
        }

        @Override // android.view.ActionMode.Callback
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            try {
                actionMode.getMenuInflater().inflate(R.menu.style_menu, menu);
                menu.removeItem(android.R.id.cut);
                menu.removeItem(android.R.id.copy);
                menu.removeItem(android.R.id.paste);
                return true;
            } catch (Exception e) {
                Logger.logException(e);
                return true;
            }
        }

        @Override // android.view.ActionMode.Callback
        public void onDestroyActionMode(ActionMode actionMode) {
        }

        @Override // android.view.ActionMode.Callback
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }
    }

    class LengthTextWatcher implements TextWatcher {
        LengthTextWatcher() {
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            if (EditorView.this.editor.length() <= 9000) {
                EditorView.this.mMaxLength.setVisibility(8);
                return;
            }
            EditorView.this.mMaxLength.setVisibility(0);
            EditorView.this.mMaxLength.setText(EditorView.this.editor.getText().length() + " / 10000");
        }
    }

    class EmojiMenuListener implements PopupMenu.OnMenuItemClickListener {
        EmojiMenuListener() {
        }

        @Override // android.widget.PopupMenu.OnMenuItemClickListener
        public boolean onMenuItemClick(MenuItem menuItem) {
            if (menuItem.getOrder() == 0) {
                EditorView.this.editor.getText().insert(EditorView.this.editor.getSelectionStart(), "( ͡° ͜ʖ ͡°) ");
            } else if (menuItem.getOrder() == 1) {
                EditorView.this.editor.getText().insert(EditorView.this.editor.getSelectionStart(), "ಠ_ಠ ");
            } else if (menuItem.getOrder() == 2) {
                EditorView.this.editor.getText().insert(EditorView.this.editor.getSelectionStart(), "(╯°□°）╯︵ ┻━┻) ");
            } else if (menuItem.getOrder() == 3) {
                EditorView.this.editor.getText().insert(EditorView.this.editor.getSelectionStart(), "┬──┬ ノ( ゜-゜ノ)");
            } else if (menuItem.getOrder() == 4) {
                EditorView.this.editor.getText().insert(EditorView.this.editor.getSelectionStart(), "¯\\\\\\_(ツ)\\_/¯ ");
            } else if (menuItem.getOrder() == 5) {
                EditorView.this.editor.getText().insert(EditorView.this.editor.getSelectionStart(), "༼ つ ◕_◕ ༽つ");
            } else if (menuItem.getOrder() == 6) {
                EditorView.this.editor.getText().insert(EditorView.this.editor.getSelectionStart(), "ᕕ( ᐛ )ᕗ");
            } else if (menuItem.getOrder() == 7) {
                EditorView.this.editor.getText().insert(EditorView.this.editor.getSelectionStart(), "(☞ﾟヮﾟ)☞");
            } else if (menuItem.getOrder() == 8) {
                EditorView.this.editor.getText().insert(EditorView.this.editor.getSelectionStart(), "ヾ(⌐■_■)ノ♪");
            } else if (menuItem.getOrder() == 9) {
                EditorView.this.editor.getText().insert(EditorView.this.editor.getSelectionStart(), "(ノಠ益ಠ)ノ彡┻━┻");
            } else if (menuItem.getOrder() == 10) {
                EditorView.this.editor.getText().insert(EditorView.this.editor.getSelectionStart(), "ლ(ಠ益ಠლ)");
            } else if (menuItem.getOrder() == 11) {
                EditorView.this.editor.getText().insert(EditorView.this.editor.getSelectionStart(), "(👁 ͜ʖ👁)");
            } else if (menuItem.getOrder() == 12) {
                EditorView.this.editor.getText().insert(EditorView.this.editor.getSelectionStart(), "(งツ)ว");
            } else if (menuItem.getOrder() == 13) {
                EditorView.this.editor.getText().insert(EditorView.this.editor.getSelectionStart(), "ʘ‿ʘ");
            } else if (menuItem.getOrder() == 14) {
                EditorView.this.editor.getText().insert(EditorView.this.editor.getSelectionStart(), "ʕ•ᴥ•ʔ");
            } else if (menuItem.getOrder() == 15) {
                EditorView.this.editor.getText().insert(EditorView.this.editor.getSelectionStart(), "(•_•)");
            } else if (menuItem.getOrder() == 16) {
                EditorView.this.editor.getText().insert(EditorView.this.editor.getSelectionStart(), "( •_•)>⌐■-■");
            } else if (menuItem.getOrder() == 17) {
                EditorView.this.editor.getText().insert(EditorView.this.editor.getSelectionStart(), "(⌐■_■)");
            } else if (menuItem.getOrder() == 18) {
                EditorView.this.editor.getText().insert(EditorView.this.editor.getSelectionStart(), "[̲̅$̲̅(̲̅ ͡° ͜ʖ ͡°̲̅)̲̅$̲̅]");
            } else if (menuItem.getOrder() == 19) {
                EditorView.this.editor.getText().insert(EditorView.this.editor.getSelectionStart(), "(シ_ _)シ");
            }
            return true;
        }
    }

    class DraftsAdapter extends BaseAdapter {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ ArrayList drafts;
        final /* synthetic */ AlertDialog dialog;

        class ItemClickListener implements View.OnClickListener {

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            final /* synthetic */ int position;

            ItemClickListener(int position) {
                this.position = position;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                EditorView.this.editor.getText().insert(EditorView.this.editor.getSelectionStart(), (CharSequence) DraftsAdapter.this.drafts.get(this.position));
                DraftsAdapter.this.dialog.dismiss();
            }
        }

        class DeleteClickListener implements View.OnClickListener {

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            final /* synthetic */ int position;

            class ConfirmListener implements DialogInterface.OnClickListener {
                ConfirmListener() {
                }

                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int which) {
                    DeleteClickListener deleteListener = DeleteClickListener.this;
                    DraftManager.deleteDraft((String) DraftsAdapter.this.drafts.remove(deleteListener.position));
                    if (DraftsAdapter.this.drafts.size() == 0) {
                        DraftsAdapter.this.dialog.dismiss();
                    } else {
                        DraftsAdapter.this.notifyDataSetChanged();
                    }
                }
            }

            DeleteClickListener(int position) {
                this.position = position;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                AlertDialogHelper.newBuilder(EditorView.this.getContext()).setTitle("Remove draft?").setPositiveButton("Remove", new ConfirmListener()).setNegativeButton("Cancel", null).show();
            }
        }

        DraftsAdapter(ArrayList arrayList, AlertDialog dialog) {
            this.drafts = arrayList;
            this.dialog = dialog;
        }

        @Override // android.widget.Adapter
        public int getCount() {
            return this.drafts.size();
        }

        @Override // android.widget.Adapter
        public Object getItem(int position) {
            return null;
        }

        @Override // android.widget.Adapter
        public long getItemId(int position) {
            return ((String) this.drafts.get(position)).hashCode();
        }

        @Override // android.widget.Adapter
        public View getView(int position, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = View.inflate(EditorView.this.getContext(), R.layout.fragment_drafts_row, null);
            }
            BaseTextView baseTextView = (BaseTextView) view.findViewById(R.id.fragment_drafts_list_textview);
            baseTextView.setText((CharSequence) this.drafts.get(position));
            baseTextView.setOnClickListener(new ItemClickListener(position));
            ((HideButton) view.findViewById(R.id.fragment_drafts_list_hide)).setOnClickListener(new DeleteClickListener(position));
            return view;
        }
    }

    class InsertLinkListener implements DialogInterface.OnClickListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ EditText urlField;
        final /* synthetic */ EditText titleField;

        InsertLinkListener(EditText editText, EditText editText2) {
            this.urlField = editText;
            this.titleField = editText2;
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int which) {
            String strGuessUrl = URLUtil.guessUrl(this.urlField.getText().toString());
            if (EditorView.this.editor.getSelectionStart() == EditorView.this.editor.getSelectionEnd()) {
                EditorView.this.editor.getText().insert(EditorView.this.editor.getSelectionStart(), "[" + this.titleField.getText().toString() + "](" + strGuessUrl + ")");
                return;
            }
            String string = EditorView.this.editor.getText().toString();
            String strSubstring = string.substring(0, EditorView.this.editor.getSelectionStart());
            String strSubstring2 = string.substring(EditorView.this.editor.getSelectionEnd());
            StringBuilder builder = new StringBuilder();
            builder.append(strSubstring);
            builder.append("[" + this.titleField.getText().toString() + "](" + this.urlField.getText().toString() + ")");
            int length = builder.length();
            builder.append(strSubstring2);
            EditorView.this.editor.setText(builder);
            EditorView.this.editor.setSelection(length);
        }
    }

    class VisibleButtonsListener implements DialogInterface.OnMultiChoiceClickListener {
        VisibleButtonsListener() {
        }

        @Override // android.content.DialogInterface.OnMultiChoiceClickListener
        public void onClick(DialogInterface dialogInterface, int which, boolean checked) {
            if (which == 0) {
                SettingsSingleton.getSettings().formattingButtonUser = checked;
                SettingsSingleton.getInstance().putBoolean("formatting_button_user", checked);
            } else if (which == 1) {
                SettingsSingleton.getSettings().formattingButtonPreview = checked;
                SettingsSingleton.getInstance().putBoolean("formatting_button_preview", checked);
            } else if (which == 2) {
                SettingsSingleton.getSettings().formattingButtonEmoji = checked;
                SettingsSingleton.getInstance().putBoolean("formatting_button_emoji", checked);
            } else if (which == 3) {
                SettingsSingleton.getSettings().formattingButtonDraft = checked;
                SettingsSingleton.getInstance().putBoolean("formatting_button_draft", checked);
            } else if (which == 4) {
                SettingsSingleton.getSettings().formattingButtonImage = checked;
                SettingsSingleton.getInstance().putBoolean("formatting_button_image", checked);
            } else if (which == 5) {
                SettingsSingleton.getSettings().formattingButtonSpoiler = checked;
                SettingsSingleton.getInstance().putBoolean("formatting_button_spoiler", checked);
            } else if (which == 6) {
                SettingsSingleton.getSettings().formattingButtonLink = checked;
                SettingsSingleton.getInstance().putBoolean("formatting_button_link", checked);
            } else if (which == 7) {
                SettingsSingleton.getSettings().formattingButtonBold = checked;
                SettingsSingleton.getInstance().putBoolean("formatting_button_bold", checked);
            } else if (which == 8) {
                SettingsSingleton.getSettings().formattingButtonItalic = checked;
                SettingsSingleton.getInstance().putBoolean("formatting_button_italic", checked);
            } else if (which == 9) {
                SettingsSingleton.getSettings().formattingButtonStrikethrough = checked;
                SettingsSingleton.getInstance().putBoolean("formatting_button_strikethrough", checked);
            } else if (which == 10) {
                SettingsSingleton.getSettings().formattingButtonSuperscript = checked;
                SettingsSingleton.getInstance().putBoolean("formatting_button_superscript", checked);
            } else if (which == 11) {
                SettingsSingleton.getSettings().formattingButtonSpeech = checked;
                SettingsSingleton.getInstance().putBoolean("formatting_button_speech", checked);
            } else if (which == 12) {
                SettingsSingleton.getSettings().formattingButtonBullet = checked;
                SettingsSingleton.getInstance().putBoolean("formatting_button_bullet", checked);
            } else if (which == 13) {
                SettingsSingleton.getSettings().formattingButtonNumbered = checked;
                SettingsSingleton.getInstance().putBoolean("formatting_button_numbered", checked);
            } else if (which == 14) {
                SettingsSingleton.getSettings().formattingButtonPromo = checked;
                SettingsSingleton.getInstance().putBoolean("formatting_button_promo", checked);
            } else if (which == 15) {
                SettingsSingleton.getSettings().formattingButtonInfo = checked;
                SettingsSingleton.getInstance().putBoolean("formatting_button_info", checked);
            } else if (which == 16) {
                SettingsSingleton.getSettings().formattingButtonBugReport = checked;
                SettingsSingleton.getInstance().putBoolean("formatting_button_bug_report", checked);
            }
            EditorView.this.refreshButtons();
        }
    }

    class AccountLoaderListener implements Loader.OnLoadCompleteListener<Cursor> {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ CursorLoader cursorLoader;

        class AccountButtonClickListener implements View.OnClickListener {

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            final /* synthetic */ ArrayList accounts;

            /* JADX INFO: renamed from: com.resync.app.ui.views.editor.EditorView$g$a$a, reason: collision with other inner class name */
            class AccountSelectListener implements DialogInterface.OnClickListener {
                AccountSelectListener() {
                }

                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int which) {
                    AccountButtonClickListener buttonListener = AccountButtonClickListener.this;
                    int accountIndex = which + 1;
                    EditorView.this.setTempAccount((UserAccount) buttonListener.accounts.get(accountIndex));
                    BusSingleton.getBus().post(new TempAccountSelectedEvent((UserAccount) AccountButtonClickListener.this.accounts.get(accountIndex)));
                }
            }

            AccountButtonClickListener(ArrayList arrayList) {
                this.accounts = arrayList;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                String[] strArr = new String[this.accounts.size() - 1];
                for (int index = 1; index < this.accounts.size(); index++) {
                    strArr[index - 1] = ((UserAccount) this.accounts.get(index)).name;
                }
                AlertDialogHelper.newBuilder(EditorView.this.getContext()).setTitle("Select account").setItems(strArr, new AccountSelectListener()).show();
            }
        }

        AccountLoaderListener(CursorLoader cursorLoader) {
            this.cursorLoader = cursorLoader;
        }

        @Override // androidx.loader.content.Loader.OnLoadCompleteListener
        public void onLoadComplete(Loader<Cursor> loader, Cursor cursor) {
            if (EditorView.this.getContext() == null) {
                return;
            }
            this.cursorLoader.unregisterListener(this);
            ArrayList arrayList = new ArrayList();
            for (int index = 0; index < cursor.getCount(); index++) {
                cursor.moveToPosition(index);
                UserAccount userAccount = new UserAccount();
                userAccount.name = cursor.getString(cursor.getColumnIndex("account_name"));
                userAccount.refreshToken = cursor.getString(cursor.getColumnIndex("account_refresh_token"));
                arrayList.add(userAccount);
            }
            cursor.close();
            EditorView.this.buttonUser.setVisibility((!SettingsSingleton.getSettings().formattingButtonUser || arrayList.size() <= 2) ? 8 : 0);
            if (arrayList.size() > 2) {
                EditorView.this.buttonUser.setOnClickListener(new AccountButtonClickListener(arrayList));
            }
        }
    }

    public EditorView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    private BaseActivity getActivity() {
        return this.activity;
    }

    private FragmentManager getFragmentManager() {
        return getActivity().getSupportFragmentManager();
    }

    public boolean checkDraft(boolean submitting) {
        String body = getBody();
        if (TextUtils.isEmpty(body)) {
            return true;
        }
        FragmentUtils.showWithArguments(DraftCheckerAlertDialogBottomSheet.class, getFragmentManager(), DraftCheckerAlertDialogBottomSheet.buildArguments(body, submitting));
        return false;
    }

    protected int getLayoutRes() {
        return R.layout.view_editor;
    }

    public String getBody() {
        return StringUtils.stripEnd(this.editor.getText().toString(), StringUtils.SPACE);
    }

    public boolean hasContent() {
        return !TextUtils.isEmpty(getBody());
    }

    public void refreshButtons() {
        if (this.messaging) {
            CursorLoader cursorLoader = new CursorLoader(getContext(), RedditProvider.URI_ACCOUNTS, new String[]{"account_name", "account_refresh_token"}, null, null, null);
            cursorLoader.registerListener(0, new AccountLoaderListener(cursorLoader));
            cursorLoader.forceLoad();
        }
        this.buttonPreview.setVisibility(SettingsSingleton.getSettings().formattingButtonPreview ? 0 : 8);
        this.buttonEmoji.setVisibility(SettingsSingleton.getSettings().formattingButtonEmoji ? 0 : 8);
        this.buttonDraft.setVisibility(SettingsSingleton.getSettings().formattingButtonDraft ? 0 : 8);
        this.buttonImage.setVisibility(SettingsSingleton.getSettings().formattingButtonImage ? 0 : 8);
        this.buttonSpoiler.setVisibility(SettingsSingleton.getSettings().formattingButtonSpoiler ? 0 : 8);
        this.buttonLink.setVisibility(SettingsSingleton.getSettings().formattingButtonLink ? 0 : 8);
        this.buttonBold.setVisibility(SettingsSingleton.getSettings().formattingButtonBold ? 0 : 8);
        this.buttonItalic.setVisibility(SettingsSingleton.getSettings().formattingButtonItalic ? 0 : 8);
        this.buttonStrikethrough.setVisibility(SettingsSingleton.getSettings().formattingButtonStrikethrough ? 0 : 8);
        this.buttonSuperscript.setVisibility(SettingsSingleton.getSettings().formattingButtonSuperscript ? 0 : 8);
        this.buttonSpeech.setVisibility(SettingsSingleton.getSettings().formattingButtonSpeech ? 0 : 8);
        this.buttonBullet.setVisibility(SettingsSingleton.getSettings().formattingButtonBullet ? 0 : 8);
        this.buttonNumbered.setVisibility(SettingsSingleton.getSettings().formattingButtonNumbered ? 0 : 8);
        this.buttonPromo.setVisibility(SettingsSingleton.getSettings().formattingButtonPromo ? 0 : 8);
        this.buttonInfo.setVisibility(SettingsSingleton.getSettings().formattingButtonInfo ? 0 : 8);
        this.buttonBugReport.setVisibility(SettingsSingleton.getSettings().formattingButtonBugReport ? 0 : 8);
    }

    public void insertImageDialog(String str) {
        InsertImageDialogFragment insertImageDialogFragmentN3 = InsertImageDialogFragment.newInstance(str);
        insertImageDialogFragmentN3.setEditorView(this);
        insertImageDialogFragmentN3.show(getFragmentManager(), UploadImageDialogFragment.TAG);
    }

    public void insertText(String str) {
        this.editor.getText().insert(this.editor.getSelectionStart(), str);
    }

    public void showError() {
        this.editor.setError("We need something here");
        this.editor.requestFocus();
    }

    public void setMessaging(boolean messaging) {
        this.messaging = messaging;
        refreshButtons();
    }

    public void setActivity(BaseActivity baseActivity) {
        this.activity = baseActivity;
    }

    public void setTempAccount(UserAccount userAccount) {
        this.mUserDisplay.setVisibility(0);
        this.mUserDisplay.setText("Temporarily posting as " + userAccount.name);
    }

    public void setText(String str) {
        this.editor.setText(str);
        GifCustomEditText gifCustomEditText = this.editor;
        gifCustomEditText.setSelection(gifCustomEditText.getText().length());
        this.editor.requestFocus();
    }

    public void setTextAndFocus(String str) {
        setText(str);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        BusSingleton.getBus().register(this);
    }

    public void onBoldClicked() {
        this.editor.getText().insert(this.editor.getSelectionStart(), "**");
        this.editor.getText().insert(this.editor.getSelectionEnd(), "**");
        if (this.editor.getSelectionStart() == this.editor.getSelectionEnd()) {
            this.editor.setSelection(this.editor.getSelectionStart() - 2);
        }
    }

    public void onBugReportClicked() {
        StringBuilder sb2 = new StringBuilder();
        sb2.append("[BUG] Sample title");
        sb2.append("\n");
        sb2.append("---");
        sb2.append("\n");
        sb2.append("**Sample description**");
        sb2.append("\n");
        sb2.append("Sample description body");
        sb2.append("\n");
        sb2.append("\n");
        sb2.append("**Scenario** (steps to reproduce)**");
        sb2.append("\n");
        sb2.append("1. Do something");
        sb2.append("\n");
        sb2.append("2. Do something else");
        sb2.append("\n");
        sb2.append("\n");
        sb2.append("**Result(s)**");
        sb2.append("\n");
        sb2.append("Sample results");
        sb2.append("\n");
        sb2.append("\n");
        sb2.append("**Device information**");
        sb2.append("\n");
        sb2.append("\n");
        sb2.append("    Sync version: v23.06.30-13:39");
        sb2.append("    \n");
        sb2.append("    Sync flavor: free");
        sb2.append("    \n");
        StringBuilder sb3 = new StringBuilder();
        sb3.append("    Removed ads: ");
        sb3.append(!SyncIapHelper.areAdsEnabled(getContext()));
        sb2.append(sb3.toString());
        sb2.append("    \n");
        sb2.append("    \n");
        sb2.append("    View type: " + UiSingleton.getViewModeName(UiSingleton.getInstance().getUiMode()));
        sb2.append("    \n");
        sb2.append("    Push enabled: " + SettingsSingleton.getSettings().pushNotifications);
        sb2.append("    \n");
        sb2.append("    \n");
        sb2.append("    Device: " + Build.DEVICE);
        sb2.append("    \n");
        sb2.append("    Model: " + Build.MANUFACTURER + StringUtils.SPACE + Build.MODEL);
        sb2.append("    \n");
        StringBuilder sb4 = new StringBuilder();
        sb4.append("    Android: ");
        sb4.append(Build.VERSION.RELEASE);
        sb2.append(sb4.toString());
        sb2.append("\n");
        sb2.append("\n");
        this.editor.getText().insert(this.editor.getSelectionStart(), sb2.toString());
    }

    public void onBulletClicked() {
        this.editor.getText().insert(this.editor.getSelectionStart(), "* ");
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        BusSingleton.getBus().unregister(this);
    }

    public void onDraftClicked() {
        if (!DraftManager.hasDrafts()) {
            StaticToast.showWithContext(getContext(), "There are no drafts available!");
            return;
        }
        ArrayList<String> arrayListC = DraftManager.getDrafts();
        if (arrayListC.size() == 0) {
            StaticToast.showWithContext(getContext(), "Error loading drafts");
            return;
        }
        AlertDialog.Builder builder = AlertDialogHelper.newBuilder(getContext());
        View viewInflate = View.inflate(getContext(), R.layout.fragment_drafts, null);
        builder.setView(viewInflate);
        builder.setTitle("Select draft");
        AlertDialog dialog = builder.create();
        ((ListView) viewInflate.findViewById(R.id.fragment_drafts_list)).setAdapter((ListAdapter) new DraftsAdapter(arrayListC, dialog));
        dialog.show();
    }

    public void onEmojiClicked(View view) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.getMenu().add(0, 0, 0, "( ͡° ͜ʖ ͡°)");
        popupMenu.getMenu().add(0, 0, 1, "ಠ_ಠ");
        popupMenu.getMenu().add(0, 0, 2, "(╯°□°）╯︵ ┻━┻)");
        popupMenu.getMenu().add(0, 0, 3, "┬──┬ ノ( ゜-゜ノ)");
        popupMenu.getMenu().add(0, 0, 4, "¯\\_(ツ)_/¯");
        popupMenu.getMenu().add(0, 0, 5, "༼ つ ◕_◕ ༽つ");
        popupMenu.getMenu().add(0, 0, 6, "ᕕ( ᐛ )ᕗ ");
        popupMenu.getMenu().add(0, 0, 7, "(☞ﾟヮﾟ)☞");
        popupMenu.getMenu().add(0, 0, 8, "ヾ(⌐■_■)ノ♪");
        popupMenu.getMenu().add(0, 0, 9, "(ノಠ益ಠ)ノ彡┻━┻");
        popupMenu.getMenu().add(0, 0, 10, "ლ(ಠ益ಠლ)");
        popupMenu.getMenu().add(0, 0, 11, "(👁 ͜ʖ👁)");
        popupMenu.getMenu().add(0, 0, 12, "(งツ)ว");
        popupMenu.getMenu().add(0, 0, 13, "ʘ‿ʘ");
        popupMenu.getMenu().add(0, 0, 14, "ʕ•ᴥ•ʔ");
        popupMenu.getMenu().add(0, 0, 15, "(•_•)");
        popupMenu.getMenu().add(0, 0, 16, "( •_•)>⌐■-■");
        popupMenu.getMenu().add(0, 0, 17, "(⌐■_■)");
        popupMenu.getMenu().add(0, 0, 18, "[̲̅$̲̅(̲̅ ͡° ͜ʖ ͡°̲̅)̲̅$̲̅]");
        popupMenu.getMenu().add(0, 0, 19, "(シ_ _)シ");
        popupMenu.setOnMenuItemClickListener(new EmojiMenuListener());
        popupMenu.show();
    }

    public void onImageClicked() {
        uploadImage();
    }

    public void onInfoClicked() {
        StringBuilder sb2 = new StringBuilder();
        sb2.append("**Device information**");
        sb2.append("\n");
        sb2.append("\n");
        sb2.append("    Sync version: v23.06.30-13:39");
        sb2.append("    \n");
        sb2.append("    Sync flavor: free");
        sb2.append("    \n");
        StringBuilder sb3 = new StringBuilder();
        sb3.append("    Removed ads: ");
        sb3.append(!SyncIapHelper.areAdsEnabled(getContext()));
        sb2.append(sb3.toString());
        sb2.append("    \n");
        sb2.append("    \n");
        sb2.append("    Ultra user: " + UltraHelper.isUltra());
        sb2.append("    \n");
        sb2.append("    View type: " + UiSingleton.getViewModeName(UiSingleton.getInstance().getUiMode()));
        sb2.append("    \n");
        sb2.append("    Push enabled: " + SettingsSingleton.getSettings().pushNotifications);
        sb2.append("    \n");
        sb2.append("    \n");
        sb2.append("    Device: " + Build.DEVICE);
        sb2.append("    \n");
        sb2.append("    Model: " + Build.MANUFACTURER + StringUtils.SPACE + Build.MODEL);
        sb2.append("    \n");
        StringBuilder sb4 = new StringBuilder();
        sb4.append("    Android: ");
        sb4.append(Build.VERSION.RELEASE);
        sb2.append(sb4.toString());
        sb2.append("\n");
        sb2.append("\n");
        this.editor.getText().insert(this.editor.getSelectionStart(), sb2.toString());
    }

    public void onItalicClicked() {
        this.editor.getText().insert(this.editor.getSelectionStart(), "*");
        this.editor.getText().insert(this.editor.getSelectionEnd(), "*");
        if (this.editor.getSelectionStart() == this.editor.getSelectionEnd()) {
            this.editor.setSelection(this.editor.getSelectionStart() - 1);
        }
    }

    public void onLinkClicked() {
        View viewInflate = View.inflate(getContext(), R.layout.links_view, null);
        EditText editText = (EditText) viewInflate.findViewById(R.id.links_view_title);
        EditText editText2 = (EditText) viewInflate.findViewById(R.id.links_view_url);
        if (this.editor.getSelectionStart() != this.editor.getSelectionEnd() && this.editor.getSelectionStart() >= 0 && this.editor.getSelectionEnd() >= 0) {
            editText.setText(this.editor.getText().toString().substring(this.editor.getSelectionStart(), this.editor.getSelectionEnd()));
        }
        AlertDialog dialog = AlertDialogHelper.newBuilder(getContext()).setTitle("Insert link").setPositiveButton("Insert", new InsertLinkListener(editText2, editText)).setNegativeButton("Cancel", null).setCancelable(true).setView(viewInflate).create();
        dialog.getWindow().setSoftInputMode(20);
        dialog.show();
    }

    public void onNumberedClicked() {
        this.editor.getText().insert(this.editor.getSelectionStart(), "1. ");
    }

    public void onPreviewClicked() {
        String string = this.editor.getText().toString();
        if (!StringUtils.isNotEmpty(string)) {
            StaticToast.show("No content to preview");
            return;
        }
        String html = RedditHelper.processMarkdownToHtml(null, string);
        if (StringUtils.isNotEmpty(html)) {
            FragmentUtils.showWithSubreddit(PreviewCommentBottomSheetFragment.class, getFragmentManager(), html);
        } else {
            StaticToast.show("No content to preview");
        }
    }

    public void onPromoClicked() {
        this.editor.getText().insert(this.editor.getSelectionStart(), "Play store link: [Sync for reddit](https://play.google.com/store/apps/details?id=com.resync.app)\n\n");
    }

    public void onSettingsClicked() {
        AlertDialogHelper.newBuilder(getContext()).setTitle("Visible buttons").setMultiChoiceItems(new String[]{"Account button", "Preview button", "Emoji button", "Draft button", "Image button", "Spoiler button", "Link button", "Bold button", "Italic button", "Strikethrough button", "Superscript button", "Speech button", "Bullet button", "Number button", "Promo button", "Info button", "Bug report button"}, new boolean[]{SettingsSingleton.getSettings().formattingButtonUser, SettingsSingleton.getSettings().formattingButtonPreview, SettingsSingleton.getSettings().formattingButtonEmoji, SettingsSingleton.getSettings().formattingButtonDraft, SettingsSingleton.getSettings().formattingButtonImage, SettingsSingleton.getSettings().formattingButtonSpoiler, SettingsSingleton.getSettings().formattingButtonLink, SettingsSingleton.getSettings().formattingButtonBold, SettingsSingleton.getSettings().formattingButtonItalic, SettingsSingleton.getSettings().formattingButtonStrikethrough, SettingsSingleton.getSettings().formattingButtonSuperscript, SettingsSingleton.getSettings().formattingButtonSpeech, SettingsSingleton.getSettings().formattingButtonBullet, SettingsSingleton.getSettings().formattingButtonNumbered, SettingsSingleton.getSettings().formattingButtonPromo, SettingsSingleton.getSettings().formattingButtonInfo, SettingsSingleton.getSettings().formattingButtonBugReport}, new VisibleButtonsListener()).create().show();
    }

    public void onSpeechClicked() {
        BaseTextView baseTextView = this.editorPreview;
        if (baseTextView == null || baseTextView.getSelectionStart() == this.editorPreview.getSelectionEnd()) {
            this.editor.getText().insert(this.editor.getSelectionStart(), "> ");
            return;
        }
        String strSubstring = this.editorPreview.getText().toString().substring(this.editorPreview.getSelectionStart(), this.editorPreview.getSelectionEnd());
        this.editorPreview.clearFocus();
        this.editor.getText().insert(this.editor.getSelectionStart(), "> " + strSubstring.replace("\n", "\n> "));
    }

    public void onSpoilerClicked() {
        this.editor.getText().insert(this.editor.getSelectionStart(), ">!");
        this.editor.getText().insert(this.editor.getSelectionEnd(), "!<");
        if (this.editor.getSelectionStart() == this.editor.getSelectionEnd()) {
            this.editor.setSelection(this.editor.getSelectionStart() - 2);
        }
    }

    public void onStrikethroughClicked() {
        this.editor.getText().insert(this.editor.getSelectionStart(), "~~");
        this.editor.getText().insert(this.editor.getSelectionEnd(), "~~");
        if (this.editor.getSelectionStart() == this.editor.getSelectionEnd()) {
            this.editor.setSelection(this.editor.getSelectionStart() - 2);
        }
    }

    public void onSuperscriptClicked() {
        this.editor.getText().insert(this.editor.getSelectionStart(), "^");
    }

    public void showPreview(String str) {
        this.editorPreviewWrapper.setVisibility(0);
        this.dividerView.setVisibility(0);
        this.editorPreview.setText(str);
        this.editor.requestFocus();
    }

    protected void init() {
        View.inflate(getContext(), getLayoutRes(), this);
        ButterKnife.bind(this);
        this.mHorScroll.setHorizontalFadingEdgeEnabled(true);
        this.mHorScroll.setBackgroundColor(MonetUtil.getCardColorAuto());
        this.editor.setCustomSelectionActionModeCallback(new StyleActionModeCallback());
        this.editor.addTextChangedListener(new LengthTextWatcher());
        refreshButtons();
    }

    public void focus() {
        this.editor.requestFocus();
    }

    public void focusMessage() {
        this.editor.requestFocus();
        this.editor.setHint("Message");
    }

    public void showPreviewHtml(String str) {
        this.editorPreviewWrapper.setVisibility(0);
        this.dividerView.setVisibility(0);
        this.editorPreview.setText(RedditHelper.htmlToTrimmedText(str));
        this.editor.requestFocus();
        this.editor.setHint("Message");
    }

    public void setSelftext(String str) {
        this.editor.setText(str);
        this.editor.requestFocus();
        this.editor.setHint("Enter selftext (optional)");
    }

    @Subscribe
    public void uploadImageKeyboardImage(OnImageKeyboardImageEvent onImageKeyboardImageEvent) {
        UploadImageDialogFragment uploadImageDialogFragmentO3 = UploadImageDialogFragment.newInstance(onImageKeyboardImageEvent.uri, onImageKeyboardImageEvent.mimeType);
        uploadImageDialogFragmentO3.setEditorView(this);
        uploadImageDialogFragmentO3.show(getFragmentManager(), UploadImageDialogFragment.TAG);
    }

    public void focusEditor() {
        this.editor.requestFocus();
    }

    public void showPreviewSpannable(SpannableStringBuilder spannableStringBuilder) {
        this.editorPreviewWrapper.setVisibility(0);
        this.dividerView.setVisibility(0);
        this.editorPreview.setText(spannableStringBuilder);
        this.editor.requestFocus();
    }

    public void setSelftextEmpty(String str) {
        if (TextUtils.isEmpty(str)) {
            this.editor.setText(str);
        }
        this.editor.requestFocus();
        this.editor.setHint("Enter selftext (optional)");
    }

    public void uploadImage() {
        try {
            ((InputMethodManager) getContext().getSystemService("input_method")).hideSoftInputFromWindow(getWindowToken(), 0);
        } catch (Exception unused) {
        }
        UploadImageDialogFragment uploadImageDialogFragmentO3 = UploadImageDialogFragment.newInstance(null, null);
        uploadImageDialogFragmentO3.setEditorView(this);
        uploadImageDialogFragmentO3.show(getFragmentManager(), UploadImageDialogFragment.TAG);
    }
}
