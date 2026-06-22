package com.resync.app.ui.views.comments;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.views.monet.MonetFab;
import com.resync.app.events.ReplyClickedEvent;
import com.resync.app.events.ReplyLongClickedEvent;
import com.resync.app.singletons.BusSingleton;

/* JADX INFO: loaded from: classes3.dex */
public class CommentsReplyFab extends MonetFab implements View.OnClickListener, View.OnLongClickListener {
    public CommentsReplyFab(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    private void init() {
        setOnClickListener(this);
        setOnLongClickListener(this);
        if (SettingsSingleton.getSettings().commentNavBar) {
            setVisibility(8);
        }
        if (SettingsSingleton.getSettings().commentsReplyFab) {
            return;
        }
        setVisibility(8);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        BusSingleton.getBus().post(new ReplyClickedEvent());
    }

    @Override // android.view.View.OnLongClickListener
    public boolean onLongClick(View view) {
        BusSingleton.getBus().post(new ReplyLongClickedEvent());
        return true;
    }
}
