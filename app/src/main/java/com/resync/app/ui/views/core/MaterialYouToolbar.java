package com.resync.app.ui.views.core;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.resync.app.R;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.fragment_dialogs.bottom.ActionsBottomSheetFragment;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.NightHelper;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.helpers.TypefaceManager;
import com.resync.app.ui.interfaces.DarkModeAware;
import com.resync.app.util.Logger;

/* JADX INFO: loaded from: classes3.dex */
public class MaterialYouToolbar extends MaterialToolbar implements DarkModeAware {
    public Paint backgroundPaint;

    /* JADX INFO: renamed from: p0, reason: collision with root package name */
    public Paint dividerPaint;
    private int insetTop;
    private boolean useWindowBackground;

    class WindowInsetsListener implements View.OnApplyWindowInsetsListener {
        WindowInsetsListener() {
        }

        @Override // android.view.View.OnApplyWindowInsetsListener
        public WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
            MaterialYouToolbar.this.insetTop = windowInsets.getSystemWindowInsetTop();
            MaterialYouToolbar materialYouToolbar = MaterialYouToolbar.this;
            materialYouToolbar.setPadding(0, materialYouToolbar.insetTop, 0, 0);
            MaterialYouToolbar.this.setMinimumHeight(SizeHelper.dpToPx(64) + MaterialYouToolbar.this.insetTop);
            for (int childIndex = 0; childIndex < MaterialYouToolbar.this.getChildCount(); childIndex++) {
                ((Toolbar.LayoutParams) MaterialYouToolbar.this.getChildAt(childIndex).getLayoutParams()).gravity = 16;
            }
            return windowInsets;
        }
    }

    public MaterialYouToolbar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.backgroundPaint = new Paint();
        this.dividerPaint = new Paint();
        this.insetTop = 0;
        setId(R.id.toolbar);
        setOutlineProvider(null);
        setNavigationIcon(R.drawable.outline_arrow_back_24);
        Logger.verbose("PAINT: " + this.backgroundPaint);
        refreshPaints();
        this.dividerPaint.setAlpha(0);
        setOnApplyWindowInsetsListener(new WindowInsetsListener());
    }

    private PorterDuffColorFilter tintFilter(int color) {
        return new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean onMoreLongClick(View view) {
        ActionsBottomSheetFragment.performFirstAction(getContext());
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void attachMoreLongClick() {
        View viewFindViewById = findViewById(R.id.menu_more);
        if (viewFindViewById != null) {
            viewFindViewById.setOnLongClickListener(new View.OnLongClickListener() { // from class: ac.b
                @Override // android.view.View.OnLongClickListener
                public final boolean onLongClick(View view) {
                    return MaterialYouToolbar.this.onMoreLongClick(view);
                }
            });
        }
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void inflateMenu(int resId) {
        super.inflateMenu(resId);
        refreshPaints();
        post(new Runnable() { // from class: ac.c
            @Override // java.lang.Runnable
            public final void run() {
                MaterialYouToolbar.this.attachMoreLongClick();
            }
        });
    }

    public void useWindowBackground() {
        this.useWindowBackground = true;
        refreshPaints();
    }

    public void setBackgroundPaintAlpha(float alpha) {
        int alphaInt = (int) (alpha * 255.0f);
        if (this.backgroundPaint.getAlpha() != alphaInt) {
            this.backgroundPaint.setAlpha(alphaInt);
            invalidate();
        }
    }

    public void setDividerPaintAlpha(float alpha) {
        int alphaInt = (int) (alpha * 255.0f);
        if (this.dividerPaint.getAlpha() != alphaInt) {
            this.dividerPaint.setAlpha(alphaInt);
            invalidate();
        }
    }

    public void refreshPaints() {
        Paint paint = this.backgroundPaint;
        if (paint != null) {
            int alpha = paint.getAlpha();
            if (this.useWindowBackground) {
                this.backgroundPaint.setColor(MonetUtil.getWindowBackgroundColorAuto());
            } else {
                this.backgroundPaint.setColor(MonetUtil.getBackgroundColorAuto());
            }
            this.backgroundPaint.setAlpha(alpha);
        }
        Paint paint2 = this.dividerPaint;
        if (paint2 != null) {
            int alpha2 = paint2.getAlpha();
            if (this.useWindowBackground) {
                this.dividerPaint.setColor(MonetUtil.getDividerColorAuto());
            } else {
                this.dividerPaint.setColor(MonetUtil.getToolbarColorAuto());
            }
            this.dividerPaint.setAlpha(alpha2);
        }
        applyTextColors(NightHelper.isNight());
        invalidate();
    }

    public void refreshTextColors() {
        applyTextColors(NightHelper.isNight());
    }

    public void applyTextColors(boolean night) {
        int textColor = MonetUtil.getPrimaryTextColor(night);
        setTitleTextColor(textColor);
        setSubtitleTextColor(textColor);
        Logger.verbose("Child count: " + getChildCount());
        for (int childIndex = 0; childIndex < getChildCount(); childIndex++) {
            if (getChildAt(childIndex) instanceof AppCompatImageButton) {
                ((AppCompatImageButton) getChildAt(childIndex)).setColorFilter(tintFilter(textColor));
            } else if (getChildAt(childIndex) instanceof ImageButton) {
                ((ImageButton) getChildAt(childIndex)).setColorFilter(tintFilter(textColor));
            } else if (getChildAt(childIndex) instanceof TextView) {
                ((TextView) getChildAt(childIndex)).setTypeface(TypefaceManager.get(11));
                if (isTitleCentered()) {
                    ((TextView) getChildAt(childIndex)).setTextSize(1, 24.0f);
                } else {
                    ((TextView) getChildAt(childIndex)).setTextSize(1, 20.0f);
                }
            }
        }
        Drawable navigationIcon = getNavigationIcon();
        if (navigationIcon != null) {
            navigationIcon.mutate();
            navigationIcon.setColorFilter(tintFilter(textColor));
            setNavigationIcon(navigationIcon);
        }
        tintMenuIcons(getMenu(), textColor);
    }

    public void tintMenuIcons(Menu menu, int color) {
        for (int menuIndex = 0; menuIndex < menu.size(); menuIndex++) {
            MenuItem item = menu.getItem(menuIndex);
            if (item.getIcon() != null) {
                Drawable drawableMutate = item.getIcon().mutate();
                drawableMutate.setColorFilter(tintFilter(color));
                item.setIcon(drawableMutate);
            }
        }
    }

    @Override // com.resync.app.ui.interfaces.DarkModeAware
    public void onDarkModeChanged() {
        refreshPaints();
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float top = SettingsSingleton.getSettings().transparentStatus ? 0 : this.insetTop;
        canvas.drawRect(0.0f, top, getWidth(), getHeight(), this.backgroundPaint);
        if (this.backgroundPaint.getAlpha() == 255) {
            canvas.drawRect(0.0f, top, getWidth(), getHeight(), this.dividerPaint);
        }
    }

    @Override // com.google.android.material.appbar.MaterialToolbar, androidx.appcompat.widget.Toolbar, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override // android.view.View
    public void setBackgroundColor(int color) {
        this.backgroundPaint.setColor(color);
    }

    @Override // android.view.View
    public void setLayoutParams(ViewGroup.LayoutParams layoutParams) {
        super.setLayoutParams(layoutParams);
        if (SettingsSingleton.getSettings().autohideToolbar || !(layoutParams instanceof AppBarLayout.LayoutParams)) {
            return;
        }
        ((AppBarLayout.LayoutParams) layoutParams).setScrollFlags(0);
    }
}
