package com.reddit.indicatorfastscroll;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.reddit.indicatorfastscroll.FastScrollItemIndicator;
import java.util.ArrayList;
import java.util.List;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

/* Stub: cleaned from decompiled Kotlin. Preserves the public API the app
 * uses (setTextColor/setIconColor/setupWithRecyclerView/setUseDefaultScroller/
 * getItemIndicatorSelectedCallbacks + the ItemIndicatorSelectedCallback
 * interface) and the package contract used by FastScrollerThumbView.
 * The actual fast-scroll behaviour is not wired up. */
public class FastScrollerView extends LinearLayout {

    public interface ItemIndicatorSelectedCallback {
        void onItemIndicatorSelected(FastScrollItemIndicator indicator, int indicatorCenterY, int itemPosition);
    }

    private final List<ItemIndicatorSelectedCallback> itemIndicatorSelectedCallbacks = new ArrayList<>();
    private Function1<? super Boolean, Unit> onItemIndicatorTouched;
    private ColorStateList textColor;
    private ColorStateList iconColor;
    private boolean useDefaultScroller = true;
    private RecyclerView recyclerView;
    private Function1<? super Integer, ? extends FastScrollItemIndicator> getItemIndicator;

    public FastScrollerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setOrientation(VERTICAL);
    }

    public FastScrollerView(Context context, AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
        setOrientation(VERTICAL);
    }

    public final List<ItemIndicatorSelectedCallback> getItemIndicatorSelectedCallbacks() {
        return this.itemIndicatorSelectedCallbacks;
    }

    public final ColorStateList getTextColor() {
        return this.textColor;
    }

    public final void setTextColor(ColorStateList colorStateList) {
        this.textColor = colorStateList;
    }

    public final ColorStateList getIconColor() {
        return this.iconColor;
    }

    public final void setIconColor(ColorStateList colorStateList) {
        this.iconColor = colorStateList;
    }

    public final boolean getUseDefaultScroller() {
        return this.useDefaultScroller;
    }

    public final void setUseDefaultScroller(boolean useDefaultScroller) {
        this.useDefaultScroller = useDefaultScroller;
    }

    /* Package contract used by FastScrollerThumbView. */
    public final void setOnItemIndicatorTouched$indicator_fast_scroll_release(Function1<? super Boolean, Unit> function1) {
        this.onItemIndicatorTouched = function1;
    }

    public final void setupWithRecyclerView(RecyclerView recyclerView, Function1<? super Integer, ? extends FastScrollItemIndicator> getItemIndicator) {
        this.recyclerView = recyclerView;
        this.getItemIndicator = getItemIndicator;
    }
}
