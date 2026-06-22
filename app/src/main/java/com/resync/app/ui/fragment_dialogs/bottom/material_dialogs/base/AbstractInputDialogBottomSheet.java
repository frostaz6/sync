package com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.resync.app.R;
import com.resync.app.ui.views.core.SheetHeaderView;
import com.resync.app.ui.views.monet.buttons.MonetActionButtons;
import com.resync.app.ui.views.responsive.CustomTextInputEditText;
import com.resync.app.ui.views.responsive.CustomTextInputLayout;
import com.resync.app.helpers.ui.StaticToast;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.dialogs.CustomBottomSheetDialogFragment;
import com.resync.app.dialogs.interfaces.InputDialogBottomSheetInterface;
import com.resync.app.util.Logger;

/* JADX INFO: loaded from: classes.dex */
public abstract class AbstractInputDialogBottomSheet extends CustomBottomSheetDialogFragment implements InputDialogBottomSheetInterface {
    public String logTag = AbstractInputDialogBottomSheet.class.getSimpleName();

    @BindView(R.id.monet_action_buttons)
    MonetActionButtons mMonetActionButtons;

    @BindView(R.id.abstract_input_dialog_bottom_sheet_header)
    SheetHeaderView mSectionHeaderView;

    @BindView(R.id.abstract_input_dialog_bottom_sheet_input)
    public CustomTextInputEditText mTextInputEditText;

    @BindView(R.id.abstract_input_dialog_bottom_sheet_input_second)
    public CustomTextInputEditText mTextInputEditTextSecond;

    @BindView(R.id.abstract_input_dialog_bottom_sheet_input_outlined)
    public CustomTextInputLayout mTextInputLayout;

    @BindView(R.id.abstract_input_dialog_bottom_sheet_input_outlined_second)
    public CustomTextInputLayout mTextInputLayoutSecond;

    class FirstFieldTextWatcher implements TextWatcher {
        FirstFieldTextWatcher() {
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            if (charSequence.length() > 0) {
                AbstractInputDialogBottomSheet.this.mTextInputLayout.setError(null);
            }
        }
    }

    class SecondFieldTextWatcher implements TextWatcher {
        SecondFieldTextWatcher() {
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            if (charSequence.length() > 0) {
                AbstractInputDialogBottomSheet.this.mTextInputLayoutSecond.setError(null);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean onEditorActionFirst(TextView textView, int actionId, KeyEvent keyEvent) {
        if (actionId != 2) {
            return false;
        }
        submitInput();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean onEditorActionSecond(TextView textView, int actionId, KeyEvent keyEvent) {
        if (actionId != 2) {
            return false;
        }
        submitInput();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onNegativeClick(View view) {
        dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPositiveClick(View view) {
        submitInput();
    }

    private void submitInput() {
        String strTrim = this.mTextInputEditText.getText().toString().trim();
        String strTrim2 = this.mTextInputEditTextSecond.getText().toString().trim();
        Logger.verbose(this.logTag, "Text first: " + strTrim);
        Logger.verbose(this.logTag, "Text second: " + strTrim2);
        if (StringUtils.isEmpty(strTrim)) {
            Logger.verbose(this.logTag, "First empty");
            if (!this.mTextInputEditText.isFocused()) {
                Logger.verbose(this.logTag, "First was not focussed, requesting focus");
                this.mTextInputLayout.requestFocus();
                return;
            } else {
                Logger.verbose(this.logTag, "First was focussed, setting error");
                this.mTextInputEditText.clearFocus();
                this.mTextInputLayout.setError("You must put something here");
                return;
            }
        }
        if (StringUtils.isEmpty(strTrim2) && isDoubleInput()) {
            Logger.verbose(this.logTag, "First set, second empty");
            if (!this.mTextInputEditTextSecond.isFocused()) {
                Logger.verbose(this.logTag, "First was not focussed, requesting focus");
                this.mTextInputLayoutSecond.requestFocus();
                return;
            } else {
                Logger.verbose(this.logTag, "Second was focussed, setting error");
                this.mTextInputEditTextSecond.clearFocus();
                this.mTextInputLayoutSecond.setError("You must put something here");
                return;
            }
        }
        Logger.verbose(this.logTag, "Looks good here");
        if (!isDoubleInput()) {
            Logger.verbose(this.logTag, "Positive action: " + strTrim);
            onPositiveButtonClicked(strTrim);
            return;
        }
        Logger.verbose(this.logTag, "Positive action second: " + strTrim + " - " + strTrim2);
        onPositiveButtonClicked(strTrim, strTrim2);
    }

    public void onInputSetup() {
    }

    public void onPositiveButtonClicked(String str) {
        StaticToast.show("TODO");
    }

    public void onPositiveButtonClicked(String str, String str2) {
        StaticToast.show("TODO");
    }

    public void setInputText(String str) {
        this.mTextInputEditText.setText(str);
        this.mTextInputEditText.setSelection(str.length());
    }

    public boolean isDoubleInput() {
        return false;
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public boolean isInitiallyExpanded() {
        return true;
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetInterface
    public int getLayoutRes() {
        return R.layout.abstract_input_dialog_bottom_sheet;
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        ButterKnife.bind(this, view);
        this.mSectionHeaderView.applyColors(isNightMode());
        this.mMonetActionButtons.applyColors(isNightMode());
        this.mTextInputLayout.applyColors(isNightMode());
        this.mTextInputEditText.applyColors(isNightMode());
        this.mTextInputLayoutSecond.applyColors(isNightMode());
        this.mTextInputEditTextSecond.applyColors(isNightMode());
        this.mSectionHeaderView.setTitle(getTitle());
        this.mTextInputLayout.setHelperText(getHint());
        this.mTextInputLayoutSecond.setHelperText(getSecondHint());
        this.mMonetActionButtons.setButtonLabels(getPositiveButtonText(), getNegativeButtonText());
        this.mMonetActionButtons.setListeners(new View.OnClickListener() { // from class: u9.e
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                AbstractInputDialogBottomSheet.this.onPositiveClick(view2);
            }
        }, new View.OnClickListener() { // from class: u9.d
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                AbstractInputDialogBottomSheet.this.onNegativeClick(view2);
            }
        });
        this.mTextInputEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: u9.g
            @Override // android.widget.TextView.OnEditorActionListener
            public final boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                return AbstractInputDialogBottomSheet.this.onEditorActionFirst(textView, actionId, keyEvent);
            }
        });
        this.mTextInputEditTextSecond.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: u9.f
            @Override // android.widget.TextView.OnEditorActionListener
            public final boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                return AbstractInputDialogBottomSheet.this.onEditorActionSecond(textView, actionId, keyEvent);
            }
        });
        this.mTextInputEditText.addTextChangedListener(new FirstFieldTextWatcher());
        this.mTextInputEditTextSecond.addTextChangedListener(new SecondFieldTextWatcher());
        if (getAutocompleteOptions() != null) {
            ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, getAutocompleteOptions());
            arrayAdapter.getFilter().filter(null);
            this.mTextInputEditText.setAdapter(arrayAdapter);
        }
        if (getSecondAutocompleteOptions() != null) {
            ArrayAdapter arrayAdapter2 = new ArrayAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, getSecondAutocompleteOptions());
            arrayAdapter2.getFilter().filter(null);
            this.mTextInputEditTextSecond.setAdapter(arrayAdapter2);
        }
        if (isDoubleInput()) {
            this.mTextInputLayoutSecond.setVisibility(0);
            this.mTextInputEditTextSecond.setVisibility(0);
        } else {
            this.mTextInputLayoutSecond.setVisibility(8);
            this.mTextInputEditTextSecond.setVisibility(8);
        }
        this.mTextInputEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(20);
        onInputSetup();
    }

    public String[] getAutocompleteOptions() {
        return null;
    }

    public String[] getSecondAutocompleteOptions() {
        return null;
    }

    public final String getNegativeButtonText() {
        return "Cancel";
    }

    public String getSecondHint() {
        return null;
    }
}
