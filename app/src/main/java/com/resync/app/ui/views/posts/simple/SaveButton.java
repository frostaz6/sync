package com.resync.app.ui.views.posts.simple;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import com.resync.app.R;
import com.resync.app.ui.activities.BaseActivity;
import com.resync.app.ui.fragment_dialogs.SavedFragment;
import com.resync.app.widgets.ColorFilterProvider;
import com.resync.app.managers.SaveManager;
import com.resync.app.helpers.CrashlyticsHelper;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.model.Post;

/* JADX INFO: loaded from: classes3.dex */
public class SaveButton extends SimpleButton {

    Post post;

    boolean saved;

    public SaveButton(final Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setOnClickListener(new View.OnClickListener() {
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                SaveButton.this.onClicked(context, view);
            }
        });
        setOnLongClickListener(new View.OnLongClickListener() {
            @Override // android.view.View.OnLongClickListener
            public final boolean onLongClick(View view) {
                return SaveButton.this.onLongClicked(context, view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onClicked(Context context, View view) {
        SaveManager.toggleSave(context, this.post);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean onLongClicked(Context context, View view) {
        if (this.post.isSaved()) {
            SaveManager.toggleSave(context, this.post);
            return true;
        }
        try {
            SavedFragment.newInstance(this.post).show(((BaseActivity) context).getSupportFragmentManager(), "saved_fragment");
            return true;
        } catch (Exception e) {
            CrashlyticsHelper.logException(e);
            return true;
        }
    }

    public void bind(Post post) {
        this.post = post;
        boolean saved = post.isSaved();
        this.saved = saved;
        if (saved) {
            setColorFilter(ColorFilterProvider.getColorFilter(-1324481));
            setImageResource(R.drawable.outline_bookmark_24);
            setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        } else {
            applyDefaultTint();
            setImageResource(R.drawable.outline_bookmark_border_24);
            setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        }
        int padding = SizeHelper.dpToPx(5);
        setPadding(padding, padding, padding, padding);
    }
}
