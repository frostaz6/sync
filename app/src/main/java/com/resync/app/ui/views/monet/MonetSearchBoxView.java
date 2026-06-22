package com.resync.app.ui.views.monet;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Outline;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.core.graphics.ColorUtils;
import butterknife.ButterKnife;
import com.resync.app.R;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.internal.SyncStyleable;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.ui.TabletHelper;
import okhttp3.internal.http2.Http2Connection;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.events.SubmitAllSearchEvent;
import com.resync.app.events.SubmitSubredditSearchEvent;
import com.resync.app.helpers.ContextHelper;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.helpers.TypefaceManager;
import com.resync.app.ui.interfaces.DarkModeAware;
import com.resync.app.util.RippleUtils;

/* JADX INFO: loaded from: classes3.dex */
public class MonetSearchBoxView extends FrameLayout implements DarkModeAware {

    private boolean labelMode;
    private View.OnClickListener moreClickListener;

    EditText editText;

    ImageView moreIcon;

    ImageView searchIcon;

    RelativeLayout searchWrapper;

    TextView textView;

    class SearchEditorActionListener implements TextView.OnEditorActionListener {

        final /* synthetic */ Context context;

        SearchEditorActionListener(Context context) {
            this.context = context;
        }

        @Override // android.widget.TextView.OnEditorActionListener
        public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
            if (actionId != 3) {
                return false;
            }
            String subreddit = ContextHelper.getAboutTargetSubreddit(this.context);
            if (StringUtils.isEmpty(subreddit)) {
                BusSingleton.getBus().post(new SubmitAllSearchEvent());
                return true;
            }
            BusSingleton.getBus().post(new SubmitSubredditSearchEvent(subreddit));
            return true;
        }
    }

    class MoreIconTextWatcher implements TextWatcher {
        MoreIconTextWatcher() {
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            if (count > 0) {
                MonetSearchBoxView.this.moreIcon.setSelected(true);
                MonetSearchBoxView.this.moreIcon.setImageResource(R.drawable.outline_close_24);
            } else {
                MonetSearchBoxView.this.moreIcon.setSelected(false);
                MonetSearchBoxView.this.moreIcon.setImageResource(R.drawable.outline_more_vert_24);
            }
        }
    }

    class RoundedOutlineProvider extends ViewOutlineProvider {
        RoundedOutlineProvider() {
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), SizeHelper.dpToPxFloat(25));
        }
    }

    public MonetSearchBoxView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, SyncStyleable.SEARCH_BOX_VIEW);
        boolean hideMore = typedArrayObtainStyledAttributes.getBoolean(1, false);
        this.labelMode = typedArrayObtainStyledAttributes.getBoolean(0, false);
        String string = typedArrayObtainStyledAttributes.getString(2);
        typedArrayObtainStyledAttributes.recycle();
        init(context, hideMore, string);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onMoreClicked(View view) {
        if (this.moreIcon.isSelected()) {
            this.editText.setText((CharSequence) null);
            return;
        }
        View.OnClickListener onClickListener = this.moreClickListener;
        if (onClickListener != null) {
            onClickListener.onClick(view);
        }
    }

    private void init(Context context, boolean hideMore, String hint) {
        setId(R.id.monet_search_box);
        LayoutInflater.from(context).inflate(R.layout.view_monet_search_box, (ViewGroup) this, true);
        ButterKnife.bind(this);
        applyColors();
        this.editText.setHint(hint);
        this.editText.setTypeface(TypefaceManager.get(11));
        this.editText.setTextSize(1, 16.0f);
        this.editText.setClickable(false);
        this.editText.setBackground(null);
        this.editText.setPadding(0, 0, 0, 0);
        if (SettingsSingleton.getSettings().incognitoKeyboard) {
            this.editText.setImeOptions(Http2Connection.OKHTTP_CLIENT_WINDOW_SIZE);
        }
        this.editText.setOnEditorActionListener(new SearchEditorActionListener(context));
        this.textView.setHint(hint);
        this.textView.setTypeface(TypefaceManager.get(11));
        this.textView.setTextSize(1, 20.0f);
        this.textView.setClickable(false);
        this.textView.setBackground(null);
        this.textView.setPadding(0, 0, 0, 0);
        this.moreIcon.setOnClickListener(new View.OnClickListener() { // from class: ic.b
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MonetSearchBoxView.this.onMoreClicked(view);
            }
        });
        this.editText.addTextChangedListener(new MoreIconTextWatcher());
        if (hideMore) {
            this.moreIcon.setVisibility(8);
        }
        if (this.labelMode) {
            this.editText.setVisibility(8);
            this.textView.setVisibility(0);
        } else {
            this.editText.setVisibility(0);
            this.textView.setVisibility(8);
        }
    }

    private void applyColors() {
        if (this.labelMode) {
            this.searchWrapper.setForeground(RippleUtils.getSelectableBackground());
        }
        this.searchWrapper.setOutlineProvider(new RoundedOutlineProvider());
        this.searchWrapper.setClipToOutline(true);
        if (!this.labelMode || TabletHelper.isTabletMode()) {
            this.searchWrapper.setBackgroundColor(MonetUtil.getCardColorAuto());
            int tertiaryColor = MonetUtil.getTertiaryTextColorAuto();
            applyTextColors(this.editText, tertiaryColor);
            applyTextColors(this.textView, tertiaryColor);
            applyIconTint(tertiaryColor);
            return;
        }
        if (MonetUtil.isAmoled()) {
            this.searchWrapper.setBackgroundColor(MonetUtil.getCardColorAuto());
        } else {
            this.searchWrapper.setBackgroundColor(MonetUtil.getBackgroundColorAuto());
        }
        int primaryColor = MonetUtil.getPrimaryTextColorAuto();
        applyTextColors(this.editText, primaryColor);
        applyTextColors(this.textView, primaryColor);
        applyIconTint(primaryColor);
    }

    private void applyIconTint(int color) {
        this.searchIcon.setImageTintList(ColorStateList.valueOf(color));
        this.moreIcon.setImageTintList(ColorStateList.valueOf(color));
    }

    private void applyTextColors(TextView textView, int color) {
        textView.setTextColor(color);
        textView.setHintTextColor(ColorUtils.setAlphaComponent(color, 127));
        textView.setLinkTextColor(color);
    }

    public void addTextChangedListener(TextWatcher textWatcher) {
        this.editText.addTextChangedListener(textWatcher);
    }

    public void clearSearchFocus() {
        this.editText.clearFocus();
    }

    public EditText getEditText() {
        return this.editText;
    }

    public String getText() {
        return this.editText.getText().toString();
    }

    public void hideKeyboard() {
        ((InputMethodManager) getContext().getSystemService("input_method")).hideSoftInputFromWindow(this.editText.getWindowToken(), 0);
        clearSearchFocus();
    }

    @Override // com.resync.app.ui.interfaces.DarkModeAware
    public void onDarkModeChanged() {
        applyColors();
    }

    public void requestSearchFocus() {
        this.editText.requestFocus();
    }

    public void setMoreClickListener(View.OnClickListener onClickListener) {
        this.moreClickListener = onClickListener;
    }

    public void setWrapperClickListener(View.OnClickListener onClickListener) {
        this.searchWrapper.setOnClickListener(onClickListener);
    }

    public void setHint(String str) {
        this.editText.setHint(str);
    }

    public void setText(String str) {
        this.editText.setText(str);
    }

    public void showKeyboard() {
        try {
            this.editText.requestFocus();
            ((InputMethodManager) getContext().getSystemService("input_method")).showSoftInput(this.editText, 1);
        } catch (Exception unused) {
        }
    }

    @Override // android.view.View
    public void setOnFocusChangeListener(View.OnFocusChangeListener onFocusChangeListener) {
        this.editText.setOnFocusChangeListener(onFocusChangeListener);
    }
}
