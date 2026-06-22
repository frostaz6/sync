package com.resync.app.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.resync.app.R;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet;
import com.resync.app.ui.views.preferences.MonetColorView;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.util.ColorUtils;

/* JADX INFO: renamed from: r9.c */
/* JADX INFO: compiled from: ColorSelectionFragment.java */
/* JADX INFO: loaded from: classes.dex */
public class ColorSelectionFragment extends AbstractAlertDialogBottomSheet {
    public static final String TAG = ColorSelectionFragment.class.getSimpleName();
    protected int color;
    protected String neutralText;
    protected boolean showDefaults;
    protected View previewView;
    protected TextView labelView;
    protected EditText hexInput;
    protected SeekBar redSeekBar;
    protected SeekBar greenSeekBar;
    protected SeekBar blueSeekBar;
    protected ColorSelectedListener colorSelectedListener;

    /* JADX INFO: renamed from: r9.c$a */
    /* JADX INFO: compiled from: ColorSelectionFragment.java */
    class SeekBarListener implements SeekBar.OnSeekBarChangeListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        boolean tracking = false;

        SeekBarListener() {
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (this.tracking) {
                ColorSelectionFragment.this.updateFromSliders();
            }
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onStartTrackingTouch(SeekBar seekBar) {
            this.tracking = true;
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onStopTrackingTouch(SeekBar seekBar) {
            this.tracking = false;
        }
    }

    /* JADX INFO: renamed from: r9.c$b */
    /* JADX INFO: compiled from: ColorSelectionFragment.java */
    class ColorInputWatcher implements TextWatcher {
        ColorInputWatcher() {
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            ColorSelectionFragment.this.updateFromHex();
        }
    }

    /* JADX INFO: renamed from: r9.c$c */
    /* JADX INFO: compiled from: ColorSelectionFragment.java */
    public interface ColorSelectedListener {
        void onColorSelected(int color);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        hideKeyboard();
        return true;
    }

    public static ColorSelectionFragment newInstance(int color, String neutralText, boolean showDefaults) {
        ColorSelectionFragment colorSelectionFragment = new ColorSelectionFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("color", color);
        if (StringUtils.isNotEmpty(neutralText)) {
            bundle.putString("neutral_text", neutralText);
        }
        bundle.putBoolean("defaults", showDefaults);
        colorSelectionFragment.setArguments(bundle);
        return colorSelectionFragment;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onSwatchClicked(View view) {
        this.hexInput.setText(String.format("%06X", Integer.valueOf(((MonetColorView) view).getColor() & 16777215)));
        EditText editText = this.hexInput;
        editText.setSelection(editText.length());
        updatePreview();
    }

    public void setColorSelectedListener(ColorSelectedListener listener) {
        this.colorSelectedListener = listener;
    }

    void updateFromHex() {
        try {
            int color = Color.parseColor("#" + this.hexInput.getText().toString().toUpperCase(Locale.ENGLISH));
            this.color = color;
            this.redSeekBar.setProgress(Color.red(color));
            this.greenSeekBar.setProgress(Color.green(this.color));
            this.blueSeekBar.setProgress(Color.blue(this.color));
            updatePreview();
        } catch (IllegalArgumentException unused) {
        }
    }

    void updateFromSliders() {
        int iArgb = Color.argb(255, this.redSeekBar.getProgress(), this.greenSeekBar.getProgress(), this.blueSeekBar.getProgress());
        this.color = iArgb;
        this.hexInput.setText(String.format("%06X", Integer.valueOf(iArgb & 16777215)));
        EditText editText = this.hexInput;
        editText.setSelection(editText.length());
        updatePreview();
    }

    void updatePreview() {
        this.previewView.setBackgroundDrawable(new ColorDrawable(this.color));
        boolean dark = ColorUtils.isDark(this.color);
        this.hexInput.setTextColor(dark ? -1 : -16777216);
        this.labelView.setTextColor(dark ? -1 : -16777216);
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getPositiveButtonText() {
        return "Select";
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public void onPositiveButtonClicked() {
        ColorSelectedListener listener = this.colorSelectedListener;
        if (listener != null) {
            listener.onColorSelected(this.color);
        }
        dismissAllowingStateLoss();
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public void loadCustomArguments() {
        super.loadCustomArguments();
        this.color = getArguments().getInt("color");
        this.neutralText = getArguments().getString("neutral_text");
        this.showDefaults = getArguments().getBoolean("defaults");
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getTitle() {
        return "Select color";
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet, com.resync.app.dialogs.CustomBottomSheetDialogFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        View viewInflate = View.inflate(getActivity(), R.layout.dialog_fragment_color_manual, null);
        ((LinearLayout) view.findViewById(R.id.abstract_alert_dialog_bottom_sheet_content)).addView(viewInflate, 0);
        viewInflate.findViewById(R.id.dialog_fragment_color_manual_swatches).setVisibility(this.showDefaults ? 0 : 8);
        viewInflate.findViewById(R.id.swatch_1).setOnClickListener(new View.OnClickListener() { // from class: r9.a
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                ColorSelectionFragment.this.onSwatchClicked(view2);
            }
        });
        viewInflate.findViewById(R.id.swatch_2).setOnClickListener(new View.OnClickListener() { // from class: r9.a
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                ColorSelectionFragment.this.onSwatchClicked(view2);
            }
        });
        viewInflate.findViewById(R.id.swatch_3).setOnClickListener(new View.OnClickListener() { // from class: r9.a
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                ColorSelectionFragment.this.onSwatchClicked(view2);
            }
        });
        viewInflate.findViewById(R.id.swatch_4).setOnClickListener(new View.OnClickListener() { // from class: r9.a
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                ColorSelectionFragment.this.onSwatchClicked(view2);
            }
        });
        viewInflate.findViewById(R.id.swatch_5).setOnClickListener(new View.OnClickListener() { // from class: r9.a
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                ColorSelectionFragment.this.onSwatchClicked(view2);
            }
        });
        viewInflate.findViewById(R.id.swatch_6).setOnClickListener(new View.OnClickListener() { // from class: r9.a
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                ColorSelectionFragment.this.onSwatchClicked(view2);
            }
        });
        viewInflate.findViewById(R.id.swatch_7).setOnClickListener(new View.OnClickListener() { // from class: r9.a
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                ColorSelectionFragment.this.onSwatchClicked(view2);
            }
        });
        viewInflate.findViewById(R.id.swatch_8).setOnClickListener(new View.OnClickListener() { // from class: r9.a
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                ColorSelectionFragment.this.onSwatchClicked(view2);
            }
        });
        this.previewView = viewInflate.findViewById(R.id.dialog_fragment_color_manual_preview);
        this.labelView = (TextView) viewInflate.findViewById(R.id.dialog_fragment_color_manual_color_input_label);
        this.hexInput = (EditText) viewInflate.findViewById(R.id.dialog_fragment_color_manual_color_input);
        this.redSeekBar = (SeekBar) viewInflate.findViewById(R.id.dialog_fragment_color_manual_red);
        this.greenSeekBar = (SeekBar) viewInflate.findViewById(R.id.dialog_fragment_color_manual_green);
        this.blueSeekBar = (SeekBar) viewInflate.findViewById(R.id.dialog_fragment_color_manual_blue);
        this.redSeekBar.setProgress(Color.red(this.color));
        this.greenSeekBar.setProgress(Color.green(this.color));
        this.blueSeekBar.setProgress(Color.blue(this.color));
        SeekBarListener seekBarListener = new SeekBarListener();
        this.redSeekBar.setOnSeekBarChangeListener(seekBarListener);
        this.greenSeekBar.setOnSeekBarChangeListener(seekBarListener);
        this.blueSeekBar.setOnSeekBarChangeListener(seekBarListener);
        this.hexInput.addTextChangedListener(new ColorInputWatcher());
        this.hexInput.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: r9.b
            @Override // android.widget.TextView.OnEditorActionListener
            public final boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                return ColorSelectionFragment.this.onEditorAction(textView, actionId, keyEvent);
            }
        });
        updateFromSliders();
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet
    public String getMessage() {
        return null;
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet
    public String getNeutralButtonText() {
        return this.neutralText;
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet
    public void onNeutralAction() {
        ColorSelectedListener listener = this.colorSelectedListener;
        if (listener != null) {
            listener.onColorSelected(0);
        }
        dismissAllowingStateLoss();
    }
}
