package com.resync.app.ui.views.posts;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.bumptech.glide.MaterialPostsHelper;
import com.resync.app.R;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.fragment_dialogs.EditFragment;
import com.resync.app.ui.fragment_dialogs.bottom.ActionsBottomSheetFragment;
import com.resync.app.ui.views.monet.MonetFab;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.events.HideReadEvent;
import com.resync.app.helpers.ContextHelper;
import com.resync.app.dialogs.SubmitSelectionDialogBottomSheet;

/* JADX INFO: loaded from: classes3.dex */
public class PostsFab extends MonetFab implements View.OnClickListener, View.OnLongClickListener {
    int mode;
    String text;

    public PostsFab(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setup();
    }

    private void setup() {
        setOnClickListener(this);
        setOnLongClickListener(this);
        if (SettingsSingleton.getSettings().mainFab) {
            return;
        }
        setVisibility(8);
    }

    public void configure(int mode, String text) {
        this.mode = mode;
        this.text = text;
        if (mode == 2) {
            setImageResource(R.drawable.outline_more_horiz_24);
            return;
        }
        if (mode != 0 && mode != 3) {
            if (mode == 1) {
                setImageResource(R.drawable.outline_email_24);
            }
        } else if (SettingsSingleton.getSettings().mainFabAction == 0) {
            setImageResource(R.drawable.outline_add_24);
        } else if (SettingsSingleton.getSettings().mainFabAction == 1) {
            setImageResource(R.drawable.notification_clear_all);
        } else {
            setImageResource(R.drawable.outline_more_horiz_24);
        }
    }

    @Override // com.google.android.material.floatingactionbutton.FloatingActionButton, android.widget.ImageView, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (SettingsSingleton.getSettings().navigationBottom) {
            return;
        }
        ((CoordinatorLayout.LayoutParams) getLayoutParams()).setAnchorId(-1);
        ((CoordinatorLayout.LayoutParams) getLayoutParams()).gravity = 85;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int mode = this.mode;
        if (mode == 2) {
            ActionsBottomSheetFragment.show(getContext());
            return;
        }
        if (mode != 0 && mode != 3) {
            if (mode == 1) {
                EditFragment.newMessageInstance(MaterialPostsHelper.extractProfileUsername(this.text)).show(ContextHelper.getFragmentManager(getContext()), "EditFragment");
            }
        } else if (SettingsSingleton.getSettings().mainFabAction == 0) {
            SubmitSelectionDialogBottomSheet.show(getContext());
        } else if (SettingsSingleton.getSettings().mainFabAction == 1) {
            BusSingleton.getBus().post(new HideReadEvent());
        } else {
            ActionsBottomSheetFragment.show(getContext());
        }
    }

    @Override // android.view.View.OnLongClickListener
    public boolean onLongClick(View view) {
        int mode = this.mode;
        if ((mode != 2 && mode != 0) || SettingsSingleton.getSettings().mainFabAction != 2) {
            return true;
        }
        ActionsBottomSheetFragment.performFirstAction(getContext());
        return true;
    }
}
