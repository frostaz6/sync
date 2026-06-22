package com.resync.app.ui.views.buttons;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatImageButton;
import com.resync.app.ui.activities.CasualGalleryActivity;
import com.resync.app.internal.SyncStyleable;
import com.resync.app.widgets.ColorFilterProvider;
import com.resync.app.helpers.NightHelper;
import com.resync.app.helpers.ThemeHelper;

/* JADX INFO: loaded from: classes3.dex */
public class AbstractRedditButton extends AppCompatImageButton {

    private int filterKey;

    private boolean hasBackground;

    private boolean extendedBackground;
    private boolean disabled;

    public AbstractRedditButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.filterKey = 1;
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, SyncStyleable.REDDIT_BUTTON);
        this.filterKey = typedArrayObtainStyledAttributes.getInt(0, 1);
        this.hasBackground = typedArrayObtainStyledAttributes.getBoolean(2, false);
        this.extendedBackground = typedArrayObtainStyledAttributes.getBoolean(1, false);
        typedArrayObtainStyledAttributes.recycle();
        applyStyle();
    }

    protected int getFilterColor() {
        return resolveFilterColor(NightHelper.isNight());
    }

    protected int resolveFilterColor(boolean night) {
        if (this.disabled) {
            return -6645094;
        }
        int filterKey = this.filterKey;
        if (filterKey == 1) {
            if (getContext() instanceof CasualGalleryActivity) {
                return -1;
            }
            return night ? -8947849 : -6645094;
        }
        if (filterKey == 2) {
            return -1;
        }
        throw new IllegalArgumentException("Invalid filter key");
    }

    protected void applyStyle() {
        if (!this.hasBackground) {
            ThemeHelper.applyEmptyButtonBackground(this);
        } else if (this.extendedBackground) {
            ThemeHelper.applyExtendedButtonBackground(getContext(), this);
        } else {
            ThemeHelper.applyButtonBackground(getContext(), this);
        }
    }

    public void disable() {
        this.disabled = true;
        setColorFilter(ColorFilterProvider.getColorFilter(getFilterColor()));
    }
}
