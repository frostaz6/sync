package com.resync.app.discord;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.appcompat.LocaleProvider;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import com.resync.app.R;
import com.resync.app.discord.OverlappingPanelsLayout;
import com.resync.app.discord.PanelState;
import com.resync.app.ui.views.chips.ContextualChipsDropInView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import kotlin.jvm.internal.SourceDebugExtension;
import com.resync.app.helpers.ContextHelper;
import com.resync.app.helpers.WindowHelper;
import com.resync.app.helpers.WindowUtils;

/* JADX INFO: compiled from: OverlappingPanelsLayout.kt */
/* JADX INFO: loaded from: classes.dex */
@SourceDebugExtension({"SMAP\nOverlappingPanelsLayout.kt\nKotlin\n*S Kotlin\n*F\n+ 1 OverlappingPanelsLayout.kt\ncom/resync/app/discord/OverlappingPanelsLayout\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,1009:1\n1855#2,2:1010\n*S KotlinDebug\n*F\n+ 1 OverlappingPanelsLayout.kt\ncom/resync/app/discord/OverlappingPanelsLayout\n*L\n964#1:1010,2\n*E\n"})
public class OverlappingPanelsLayout extends FrameLayout {
    public static final Companion COMPANION = new Companion(null);
    private float startPanelOpenedCenterPanelX;
    private float endPanelOpenedCenterPanelX;
    private ValueAnimator valueAnimator;
    private final ArrayList<PanelStateListener> startPanelStateListeners;
    private final ArrayList<PanelStateListener> endPanelStateListeners;
    private Panel selectedPanel;
    private LockState startPanelLockState;
    private LockState endPanelLockState;
    private PanelState startPanelState;
    private PanelState endPanelState;
    private boolean startPanelUseFullPortraitWidth;
    private Function0<Unit> pendingUpdate;
    private float targetCenterPanelX;
    private List<Rect> childGestureRegions;
    private ScrollDirection scrollDirection;
    private final boolean homeGestureSupported;
    private View startPanel;
    protected View centerPanel;
    private View endPanel;

    private boolean isLeftToRight;
    private float scrollSlop;
    private float homeGestureFromBottomThreshold;

    private float minFlingDpPerSecond;

    private VelocityTracker velocityTracker;

    private int startPanelWidth;
    private int endPanelWidth;

    private boolean isScrollingHorizontally;
    private boolean isInsideCenterPanelOnActionDown;
    private boolean isHomeSystemGesture;
    private float downX;

    private float downY;
    private float xOffsetFromCenterPanel;

    /* JADX INFO: compiled from: OverlappingPanelsLayout.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    /* JADX INFO: compiled from: OverlappingPanelsLayout.kt */
    public enum LockState {
        OPEN,
        CLOSE,
        UNLOCKED
    }

    /* JADX INFO: compiled from: OverlappingPanelsLayout.kt */
    public enum Panel {
        START,
        CENTER,
        END
    }

    /* JADX INFO: compiled from: OverlappingPanelsLayout.kt */
    public interface PanelStateListener {
        void onPanelStateChange(PanelState panelState);
    }

    /* JADX INFO: compiled from: OverlappingPanelsLayout.kt */
    public enum ScrollDirection {
        LEFT,
        RIGHT
    }

    /* JADX INFO: compiled from: OverlappingPanelsLayout.kt */
    public /* synthetic */ static class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[Panel.values().length];
            try {
                iArr[OverlappingPanelsLayout.Panel.START.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[OverlappingPanelsLayout.Panel.END.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr[OverlappingPanelsLayout.Panel.CENTER.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            $EnumSwitchMapping$0 = iArr;
        }
    }

    /* JADX INFO: compiled from: OverlappingPanelsLayout.kt */
    final class ClosePanelsPendingUpdate extends Lambda<Unit> implements Function0<Unit> {
        final /* synthetic */ boolean animated;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        ClosePanelsPendingUpdate(boolean animated) {
            super(0);
            this.animated = animated;
        }

        @Override // kotlin.jvm.functions.Function0
        public /* bridge */ /* synthetic */ Unit invoke() {
            invoke2();
            return Unit.INSTANCE;
        }

        /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
        public final void invoke2() {
            OverlappingPanelsLayout.this.closePanels(this.animated);
        }
    }

    /* JADX INFO: compiled from: OverlappingPanelsLayout.kt */
    final class OpenEndPanelPendingUpdate extends Lambda<Unit> implements Function0<Unit> {
        final /* synthetic */ boolean animated;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        OpenEndPanelPendingUpdate(boolean animated) {
            super(0);
            this.animated = animated;
        }

        @Override // kotlin.jvm.functions.Function0
        public /* bridge */ /* synthetic */ Unit invoke() {
            invoke2();
            return Unit.INSTANCE;
        }

        /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
        public final void invoke2() {
            OverlappingPanelsLayout.this.openEndPanel(this.animated);
        }
    }

    /* JADX INFO: compiled from: OverlappingPanelsLayout.kt */
    final class OpenStartPanelPendingUpdate extends Lambda<Unit> implements Function0<Unit> {
        final /* synthetic */ boolean animated;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        OpenStartPanelPendingUpdate(boolean animated) {
            super(0);
            this.animated = animated;
        }

        @Override // kotlin.jvm.functions.Function0
        public /* bridge */ /* synthetic */ Unit invoke() {
            invoke2();
            return Unit.INSTANCE;
        }

        /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
        public final void invoke2() {
            OverlappingPanelsLayout.this.openStartPanel(this.animated);
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public OverlappingPanelsLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Intrinsics.checkNotNullParameter(context, "context");
        this.isLeftToRight = true;
        this.startPanelOpenedCenterPanelX = Float.MIN_VALUE;
        this.endPanelOpenedCenterPanelX = Float.MAX_VALUE;
        this.startPanelStateListeners = new ArrayList<PanelStateListener>();
        this.endPanelStateListeners = new ArrayList<PanelStateListener>();
        this.selectedPanel = OverlappingPanelsLayout.Panel.CENTER;
        LockState unlockedState = OverlappingPanelsLayout.LockState.UNLOCKED;
        this.startPanelLockState = unlockedState;
        this.endPanelLockState = unlockedState;
        PanelState.Closed closedState = PanelState.Closed.INSTANCE;
        this.startPanelState = closedState;
        this.endPanelState = closedState;
        this.childGestureRegions = java.util.Collections.emptyList();
        this.homeGestureSupported = Build.VERSION.SDK_INT >= 29;
        initFromAttributes(attributeSet);
    }

    private final void handlePanelsLaid() {
        View childAt = getChildAt(0);
        Intrinsics.checkNotNullExpressionValue(childAt, "getChildAt(0)");
        this.startPanel = childAt;
        View childAt2 = getChildAt(1);
        Intrinsics.checkNotNullExpressionValue(childAt2, "getChildAt(1)");
        setCenterPanel(childAt2);
        View childAt3 = getChildAt(2);
        Intrinsics.checkNotNullExpressionValue(childAt3, "getChildAt(2)");
        this.endPanel = childAt3;
        View view = this.startPanel;
        View view2 = null;
        if (view == null) {
            Intrinsics.throwUninitializedPropertyAccessException("startPanel");
            view = null;
        }
        view.setVisibility(4);
        View view3 = this.startPanel;
        if (view3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("startPanel");
            view3 = null;
        }
        view3.setElevation(0.0f);
        handleStartPanelClosed();
        getCenterPanel().setVisibility(0);
        getCenterPanel().setElevation(0.0f);
        View view4 = this.endPanel;
        if (view4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("endPanel");
            view4 = null;
        }
        view4.setVisibility(4);
        View view5 = this.endPanel;
        if (view5 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("endPanel");
            view5 = null;
        }
        view5.setElevation(0.0f);
        handleEndPanelClosed();
        applyStartPanelWidth();
        applyEndPanelWidth();
        updateStartPanelOpenedCenterPanelX();
        updateEndPanelOpenedCenterPanelX();
        Function0<Unit> function0 = this.pendingUpdate;
        if (function0 != null) {
            function0.invoke();
        }
        this.pendingUpdate = null;
        View view6 = this.startPanel;
        if (view6 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("startPanel");
            view6 = null;
        }
        view6.addOnLayoutChangeListener(new View.OnLayoutChangeListener() { // from class: g7.d
            @Override // android.view.View.OnLayoutChangeListener
            public final void onLayoutChange(View changedView, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                OverlappingPanelsLayout.onStartPanelLayoutChange(OverlappingPanelsLayout.this, changedView, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom);
            }
        });
        View view7 = this.endPanel;
        if (view7 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("endPanel");
        } else {
            view2 = view7;
        }
        view2.addOnLayoutChangeListener(new View.OnLayoutChangeListener() { // from class: g7.e
            @Override // android.view.View.OnLayoutChangeListener
            public final void onLayoutChange(View changedView, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                OverlappingPanelsLayout.onEndPanelLayoutChange(OverlappingPanelsLayout.this, changedView, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void onStartPanelLayoutChange(OverlappingPanelsLayout this$0, View view, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        boolean leftToRight = this$0.isLeftToRight;
        if (leftToRight && right != oldRight) {
            this$0.updateStartPanelOpenedCenterPanelX();
        } else {
            if (leftToRight || left == oldLeft) {
                return;
            }
            this$0.updateStartPanelOpenedCenterPanelX();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void onEndPanelLayoutChange(OverlappingPanelsLayout this$0, View view, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        boolean leftToRight = this$0.isLeftToRight;
        if (leftToRight && left != oldLeft) {
            this$0.updateEndPanelOpenedCenterPanelX();
        } else {
            if (leftToRight || right == oldRight) {
                return;
            }
            this$0.updateEndPanelOpenedCenterPanelX();
        }
    }

    private final void initFromAttributes(AttributeSet attributeSet) {
        LocaleProvider localeProvider = LocaleProvider.INSTANCE;
        Context context = getContext();
        Intrinsics.checkNotNullExpressionValue(context, "context");
        localeProvider.getLocale(context);
        this.isLeftToRight = true;
        this.scrollSlop = getResources().getDimension(com.resync.app.R.dimen.overlapping_panels_scroll_slop);
        this.homeGestureFromBottomThreshold = getResources().getDimension(com.resync.app.R.dimen.overlapping_panels_home_gesture_from_bottom_threshold);
        this.minFlingDpPerSecond = getResources().getDimension(com.resync.app.R.dimen.overlapping_panels_min_fling_dp_per_second);
        if (getResources().getConfiguration().orientation == 1) {
            int widthPixels = getResources().getDisplayMetrics().widthPixels;
        } else {
            int heightPixels = getResources().getDisplayMetrics().heightPixels;
        }
        if (WindowHelper.isLandscape(getContext())) {
            this.startPanelWidth = (int) (((double) WindowUtils.getDisplayWidth()) * 0.45d);
            this.endPanelWidth = (int) (((double) WindowUtils.getDisplayWidth()) * 0.5d);
        } else {
            this.startPanelWidth = (int) (WindowUtils.getDisplayWidth() * 0.75f);
            this.endPanelWidth = (int) (WindowUtils.getDisplayWidth() * 0.85f);
        }
    }

    private final boolean isTouchingCenterPanelAtRest(MotionEvent motionEvent) {
        float rawX = motionEvent.getRawX();
        float x = getCenterPanel().getX();
        float maxX = Math.max(this.startPanelOpenedCenterPanelX, this.endPanelOpenedCenterPanelX);
        float minX = Math.min(this.startPanelOpenedCenterPanelX, this.endPanelOpenedCenterPanelX);
        float width = getCenterPanel().getWidth() + minX;
        boolean beyondMax = rawX > maxX;
        boolean beforeWidth = rawX < width;
        boolean atMax = x == maxX;
        boolean atMin = x == minX;
        if (atMax && beyondMax) {
            return true;
        }
        return atMin && beforeWidth;
    }

    private final boolean isInGestureExclusionRegion(MotionEvent motionEvent) {
        boolean found;
        ContextualChipsDropInView contextualChipsDropInView;
        float rawX = motionEvent.getRawX();
        float rawY = motionEvent.getRawY();
        Iterator<Rect> it = this.childGestureRegions.iterator();
        do {
            found = false;
            if (!it.hasNext()) {
                if (!Intrinsics.areEqual(getEndPanelState(), PanelState.Opened.INSTANCE) || (contextualChipsDropInView = (ContextualChipsDropInView) ContextHelper.getBaseActivity(getContext()).getWindow().getDecorView().findViewById(com.resync.app.R.id.contextual_chips)) == null) {
                    return false;
                }
                return contextualChipsDropInView.containsPoint(rawX, rawY);
            }
            Rect rect = (Rect) it.next();
            boolean withinX = rawX >= ((float) rect.left) && rawX <= ((float) rect.right);
            boolean withinY = rawY <= ((float) rect.bottom) && rawY >= ((float) rect.top);
            if (withinX && withinY) {
                found = true;
            }
        } while (!found);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void openEndPanel(boolean animated) {
        if (this.centerPanel == null) {
            this.pendingUpdate = new OpenEndPanelPendingUpdate(animated);
        } else {
            animateCenterPanelTo(this.endPanelOpenedCenterPanelX, animated, 250L);
        }
    }

    private final void openPanel(Panel panel) {
        int ordinalCase = WhenMappings.$EnumSwitchMapping$0[panel.ordinal()];
        if (ordinalCase == 1) {
            openStartPanel(false);
        } else if (ordinalCase == 2) {
            openEndPanel(false);
        } else {
            if (ordinalCase != 3) {
                return;
            }
            closePanels(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void openStartPanel(boolean animated) {
        if (this.centerPanel == null) {
            this.pendingUpdate = new OpenStartPanelPendingUpdate(animated);
        } else if (this.startPanelLockState == OverlappingPanelsLayout.LockState.OPEN) {
            setCenterPanelX(this.startPanelOpenedCenterPanelX);
        } else {
            animateCenterPanelTo(this.startPanelOpenedCenterPanelX, animated, 250L);
        }
    }

    private final void applyEndPanelWidth() {
        View view = this.endPanel;
        View view2 = null;
        if (view == null) {
            Intrinsics.throwUninitializedPropertyAccessException("endPanel");
            view = null;
        }
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = this.endPanelWidth;
        View view3 = this.endPanel;
        if (view3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("endPanel");
        } else {
            view2 = view3;
        }
        view2.setLayoutParams(layoutParams);
    }

    private final void applyStartPanelWidth() {
        View view = this.startPanel;
        if (view != null) {
            View view2 = null;
            if (view == null) {
                Intrinsics.throwUninitializedPropertyAccessException("startPanel");
                view = null;
            }
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.width = this.startPanelUseFullPortraitWidth ? -1 : this.startPanelWidth;
            View view3 = this.startPanel;
            if (view3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("startPanel");
            } else {
                view2 = view3;
            }
            view2.setLayoutParams(layoutParams);
        }
    }

    private final boolean shouldFollowTouchScroll(MotionEvent motionEvent) {
        float targetX = clampCenterPanelX(rawXWithOffset(motionEvent));
        boolean movedSignificantly = Math.abs(targetX - getCenterPanel().getX()) > getResources().getDisplayMetrics().density;
        if (targetX == 0.0f) {
            return true;
        }
        if (targetX == this.startPanelOpenedCenterPanelX) {
            return true;
        }
        return ((targetX > this.endPanelOpenedCenterPanelX ? 1 : (targetX == this.endPanelOpenedCenterPanelX ? 0 : -1)) == 0) || movedSignificantly;
    }

    private final void handleFling(MotionEvent motionEvent) {
        float targetX = rawXWithOffset(motionEvent);
        VelocityTracker velocityTracker = this.velocityTracker;
        if (velocityTracker != null) {
            velocityTracker.computeCurrentVelocity(1000);
        }
        VelocityTracker velocityTracker2 = this.velocityTracker;
        float xVelocity = velocityTracker2 != null ? velocityTracker2.getXVelocity() : Float.MIN_VALUE;
        boolean towardStart = false;
        boolean isFling = Math.abs(xVelocity) > this.minFlingDpPerSecond;
        if (!this.isLeftToRight ? xVelocity < 0.0f : xVelocity > 0.0f) {
            towardStart = true;
        }
        if (isFling) {
            if (towardStart) {
                Panel selectedPanel = this.selectedPanel;
                if (selectedPanel == OverlappingPanelsLayout.Panel.END) {
                    closePanels(true);
                    return;
                } else if (selectedPanel == OverlappingPanelsLayout.Panel.CENTER) {
                    openStartPanel(true);
                    return;
                }
            } else {
                Panel selectedPanel2 = this.selectedPanel;
                if (selectedPanel2 == OverlappingPanelsLayout.Panel.START) {
                    closePanels(true);
                    return;
                } else if (selectedPanel2 == OverlappingPanelsLayout.Panel.CENTER) {
                    openEndPanel(true);
                    return;
                }
            }
        }
        float maxX = Math.max(this.startPanelOpenedCenterPanelX, this.endPanelOpenedCenterPanelX);
        float minX = Math.min(this.startPanelOpenedCenterPanelX, this.endPanelOpenedCenterPanelX);
        float half = 2;
        if (targetX > maxX / half) {
            openPanel(panelTowardsStart());
        } else if (targetX < minX / half) {
            openPanel(panelTowardsEnd());
        } else {
            closePanels();
        }
    }

    private final void handleScroll(MotionEvent motionEvent) {
        setCenterPanelX(clampCenterPanelX(rawXWithOffset(motionEvent)));
    }

    private final void setCenterPanelX(float newX) {
        float oldX = getCenterPanel().getX();
        getCenterPanel().setX(newX);
        updatePanelStates(oldX, newX);
    }

    private final void animateCenterPanelTo(float targetX, boolean animated, long duration) {
        float startX = getCenterPanel().getX();
        ValueAnimator valueAnimator = this.valueAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        float clampedTargetX = clampCenterPanelX(targetX);
        this.targetCenterPanelX = clampedTargetX;
        if (animated) {
            ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(startX, clampedTargetX);
            valueAnimatorOfFloat.setInterpolator(new LinearOutSlowInInterpolator());
            valueAnimatorOfFloat.setDuration(duration);
            this.valueAnimator = valueAnimatorOfFloat;
            valueAnimatorOfFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: g7.b
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    OverlappingPanelsLayout.onLinearOutSlowInUpdate(OverlappingPanelsLayout.this, valueAnimator2);
                }
            });
        } else {
            ValueAnimator valueAnimatorOfFloat2 = ValueAnimator.ofFloat(startX, clampedTargetX);
            valueAnimatorOfFloat2.setInterpolator(new FastOutSlowInInterpolator());
            valueAnimatorOfFloat2.setDuration(duration);
            this.valueAnimator = valueAnimatorOfFloat2;
            valueAnimatorOfFloat2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: g7.c
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    OverlappingPanelsLayout.onFastOutSlowInUpdate(OverlappingPanelsLayout.this, valueAnimator2);
                }
            });
        }
        ValueAnimator valueAnimator2 = this.valueAnimator;
        if (valueAnimator2 != null) {
            valueAnimator2.start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void onLinearOutSlowInUpdate(OverlappingPanelsLayout this$0, ValueAnimator animator) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Intrinsics.checkNotNullParameter(animator, "animator");
        Object animatedValue = animator.getAnimatedValue();
        Intrinsics.checkNotNull(animatedValue, "null cannot be cast to non-null type kotlin.Float");
        this$0.setCenterPanelX(((Float) animatedValue).floatValue());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void onFastOutSlowInUpdate(OverlappingPanelsLayout this$0, ValueAnimator animator) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Intrinsics.checkNotNullParameter(animator, "animator");
        Object animatedValue = animator.getAnimatedValue();
        Intrinsics.checkNotNull(animatedValue, "null cannot be cast to non-null type kotlin.Float");
        this$0.setCenterPanelX(((Float) animatedValue).floatValue());
    }

    private final float dxFromDown(float downX, MotionEvent motionEvent) {
        return motionEvent.getX() - downX;
    }

    private final float dyFromDown(float downY, MotionEvent motionEvent) {
        return motionEvent.getY() - downY;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void closePanels(boolean animated) {
        if (this.centerPanel == null) {
            this.pendingUpdate = new ClosePanelsPendingUpdate(animated);
        } else {
            animateCenterPanelTo(0.0f, animated, 200L);
        }
    }

    private final PanelState computeEndPanelState(float oldCenterX, float newCenterX) {
        boolean leftToRight = this.isLeftToRight;
        if (leftToRight && newCenterX >= 0.0f) {
            return PanelState.Closed.INSTANCE;
        }
        if (leftToRight || newCenterX > 0.0f) {
            return (newCenterX > this.endPanelOpenedCenterPanelX ? 1 : (newCenterX == this.endPanelOpenedCenterPanelX ? 0 : -1)) == 0 ? PanelState.Opened.INSTANCE : (!leftToRight || newCenterX >= oldCenterX) ? (leftToRight || newCenterX <= oldCenterX) ? PanelState.Opening.INSTANCE : PanelState.Closing.INSTANCE : PanelState.Closing.INSTANCE;
        }
        return PanelState.Closed.INSTANCE;
    }

    private final Panel panelTowardsStart() {
        return this.isLeftToRight ? OverlappingPanelsLayout.Panel.START : OverlappingPanelsLayout.Panel.END;
    }

    private final LockState homewardLockState() {
        return this.isLeftToRight ? this.startPanelLockState : this.endPanelLockState;
    }

    private final float clampCenterPanelX(float centerPanelX) {
        LockState startLock = this.startPanelLockState;
        LockState openLock = OverlappingPanelsLayout.LockState.OPEN;
        if (startLock == openLock) {
            return this.startPanelOpenedCenterPanelX;
        }
        if (this.endPanelLockState == openLock) {
            return this.endPanelOpenedCenterPanelX;
        }
        LockState homewardLock = homewardLockState();
        LockState closeLock = OverlappingPanelsLayout.LockState.CLOSE;
        float minX = 0.0f;
        float maxX = (homewardLock == closeLock || (this.selectedPanel == OverlappingPanelsLayout.Panel.CENTER && this.scrollDirection == OverlappingPanelsLayout.ScrollDirection.LEFT)) ? 0.0f : Math.max(this.startPanelOpenedCenterPanelX, this.endPanelOpenedCenterPanelX);
        if (oppositeLockState() != closeLock && (this.selectedPanel != OverlappingPanelsLayout.Panel.CENTER || this.scrollDirection != OverlappingPanelsLayout.ScrollDirection.RIGHT)) {
            minX = Math.min(this.startPanelOpenedCenterPanelX, this.endPanelOpenedCenterPanelX);
        }
        return centerPanelX > maxX ? maxX : centerPanelX < minX ? minX : centerPanelX;
    }

    private final Panel panelTowardsEnd() {
        return this.isLeftToRight ? OverlappingPanelsLayout.Panel.END : OverlappingPanelsLayout.Panel.START;
    }

    private final LockState oppositeLockState() {
        return this.isLeftToRight ? this.endPanelLockState : this.startPanelLockState;
    }

    private final PanelState computeStartPanelState(float oldCenterX, float newCenterX) {
        boolean leftToRight = this.isLeftToRight;
        if (leftToRight && newCenterX <= 0.0f) {
            return PanelState.Closed.INSTANCE;
        }
        if (leftToRight || newCenterX < 0.0f) {
            return (newCenterX > this.startPanelOpenedCenterPanelX ? 1 : (newCenterX == this.startPanelOpenedCenterPanelX ? 0 : -1)) == 0 ? PanelState.Opened.INSTANCE : (!leftToRight || newCenterX <= oldCenterX) ? (leftToRight || newCenterX >= oldCenterX) ? PanelState.Opening.INSTANCE : PanelState.Closing.INSTANCE : PanelState.Closing.INSTANCE;
        }
        return PanelState.Closed.INSTANCE;
    }

    private final float rawXWithOffset(MotionEvent motionEvent) {
        return motionEvent.getRawX() + this.xOffsetFromCenterPanel;
    }

    private final void updatePanelStates(float oldCenterX, float newCenterX) {
        View view;
        View view2;
        if ((this.isLeftToRight && getCenterPanel().getX() > 0.0f) || (!this.isLeftToRight && getCenterPanel().getX() < 0.0f)) {
            View view3 = this.startPanel;
            if (view3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("startPanel");
                view3 = null;
            }
            view3.setVisibility(0);
            handleStartPanelOpened();
        } else {
            View view4 = this.startPanel;
            if (view4 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("startPanel");
                view4 = null;
            }
            view4.setVisibility(4);
            handleStartPanelClosed();
        }
        if ((this.isLeftToRight && getCenterPanel().getX() < 0.0f) || (!this.isLeftToRight && getCenterPanel().getX() > 0.0f)) {
            view = this.endPanel;
            if (view == null) {
                Intrinsics.throwUninitializedPropertyAccessException("endPanel");
                view = null;
            }
            view.setVisibility(0);
            handleEndPanelOpened();
        } else {
            view2 = this.endPanel;
            if (view2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("endPanel");
                view2 = null;
            }
            view2.setVisibility(4);
            handleEndPanelClosed();
        }
        if (getCenterPanel().getX() == 0.0f) {
            getCenterPanel().setBackgroundColor(0);
        } else {
            getCenterPanel().setBackgroundColor(com.resync.app.helpers.ui.MonetUtil.getWindowBackgroundColorAuto());
        }
        boolean panelOpen = newCenterX != 0.0f;
        if (!panelOpen) {
            this.selectedPanel = OverlappingPanelsLayout.Panel.CENTER;
        } else if (newCenterX == this.startPanelOpenedCenterPanelX) {
            this.selectedPanel = OverlappingPanelsLayout.Panel.START;
        } else if (newCenterX == this.endPanelOpenedCenterPanelX) {
            this.selectedPanel = OverlappingPanelsLayout.Panel.END;
        }
        boolean atRest = (newCenterX == this.endPanelOpenedCenterPanelX) || (newCenterX == this.startPanelOpenedCenterPanelX);
        getCenterPanel().setImportantForAccessibility(atRest ? 4 : 0);
        boolean centeredOrResting = !panelOpen || atRest;
        float elevation;
        if (centeredOrResting) {
            elevation = getResources().getDimension(com.resync.app.R.dimen.overlapping_panels_center_panel_non_resting_elevation);
        } else {
            elevation = getResources().getDimension(com.resync.app.R.dimen.overlapping_panels_center_panel_non_resting_elevation);
        }
        getCenterPanel().setElevation(elevation);
        PanelState centerState = computeStartPanelState(oldCenterX, newCenterX);
        if (!Intrinsics.areEqual(this.startPanelState, centerState)) {
            this.startPanelState = centerState;
            if (Intrinsics.areEqual(centerState, PanelState.Closing.INSTANCE)) {
                com.resync.app.singletons.BusSingleton.getBus().post(new com.resync.app.events.DragStartingEvent());
            }
            Iterator<PanelStateListener> it = this.startPanelStateListeners.iterator();
            while (it.hasNext()) {
                it.next().onPanelStateChange(this.startPanelState);
            }
        }
        PanelState endState = computeEndPanelState(oldCenterX, newCenterX);
        if (!Intrinsics.areEqual(this.endPanelState, endState)) {
            this.endPanelState = endState;
            Iterator<PanelStateListener> it2 = this.endPanelStateListeners.iterator();
            while (it2.hasNext()) {
                it2.next().onPanelStateChange(this.endPanelState);
            }
        }
    }

    private final void updateEndPanelOpenedCenterPanelX() {
        View view = this.endPanel;
        if (view == null) {
            Intrinsics.throwUninitializedPropertyAccessException("endPanel");
            view = null;
        }
        float openedX = -view.getWidth();
        this.endPanelOpenedCenterPanelX = openedX;
        if (!this.isLeftToRight) {
            openedX = -openedX;
        }
        this.endPanelOpenedCenterPanelX = openedX;
    }

    private final void updateStartPanelOpenedCenterPanelX() {
        View view = this.startPanel;
        if (view == null) {
            Intrinsics.throwUninitializedPropertyAccessException("startPanel");
            view = null;
        }
        float width = view.getWidth();
        this.startPanelOpenedCenterPanelX = width;
        if (!this.isLeftToRight) {
            width = -width;
        }
        this.startPanelOpenedCenterPanelX = width;
    }

    public final void handleStartPanelClosed() {
    }

    public final void openEndPanel() {
        openEndPanel(false);
    }

    public final void openStartPanel() {
        openStartPanel(false);
    }

    public final void registerStartPanelStateListeners(PanelStateListener... panelStateListenerArgs) {
        Intrinsics.checkNotNullParameter(panelStateListenerArgs, "panelStateListenerArgs");
        for (PanelStateListener listener : panelStateListenerArgs) {
            this.startPanelStateListeners.add(listener);
        }
    }

    protected final void setCenterPanel(View view) {
        Intrinsics.checkNotNullParameter(view, "<set-?>");
        this.centerPanel = view;
    }

    public final void setEndPanelLockState(LockState lockState) {
        Intrinsics.checkNotNullParameter(lockState, "lockState");
        this.endPanelLockState = lockState;
        if (lockState == OverlappingPanelsLayout.LockState.OPEN) {
            openEndPanel();
        }
    }

    public final void setStartPanelLockState(LockState lockState) {
        Intrinsics.checkNotNullParameter(lockState, "lockState");
        this.startPanelLockState = lockState;
        if (lockState == OverlappingPanelsLayout.LockState.OPEN) {
            openStartPanel();
        }
    }

    public final void handleEndPanelOpened() {
    }

    public final void handleStartPanelOpened() {
    }

    public final void closePanels() {
        closePanels(false);
    }

    protected final View getCenterPanel() {
        View view = this.centerPanel;
        if (view != null) {
            return view;
        }
        Intrinsics.throwUninitializedPropertyAccessException("centerPanel");
        return null;
    }

    public PanelState getEndPanelState() {
        return this.endPanelState;
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        int actionMasked = event.getActionMasked();
        if (actionMasked != 0) {
            if (actionMasked != 1) {
                if (actionMasked == 2) {
                    if (!this.isScrollingHorizontally) {
                        float dx = dxFromDown(this.downX, event);
                        float dy = dyFromDown(this.downY, event);
                        boolean inExclusion = isInGestureExclusionRegion(event);
                        float width = WindowUtils.getDisplayWidth();
                        int signDx = dx != 0.0f ? (dx < 0.0f ? -1 : 1) : 0;
                        if (signDx < 0 && this.downX < width * 0.9f && oppositeLockState() == OverlappingPanelsLayout.LockState.UNLOCKED && !Intrinsics.areEqual(getStartPanelState(), PanelState.Opened.INSTANCE)) {
                            return false;
                        }
                        if (signDx < 0 && getCenterPanel().getTranslationX() == 0.0f && oppositeLockState() == OverlappingPanelsLayout.LockState.CLOSE) {
                            return false;
                        }
                        if (!(Math.abs(dx) > this.scrollSlop)) {
                            return false;
                        }
                        if (!(Math.abs(dx) > Math.abs(dy))) {
                            return false;
                        }
                        if (inExclusion) {
                            return false;
                        }
                        this.isScrollingHorizontally = true;
                    }
                } else if (actionMasked != 3) {
                    return this.isInsideCenterPanelOnActionDown;
                }
            }
            VelocityTracker velocityTracker = this.velocityTracker;
            if (velocityTracker != null) {
                velocityTracker.recycle();
            }
            this.velocityTracker = null;
            if (!this.isScrollingHorizontally && !this.isInsideCenterPanelOnActionDown) {
                return false;
            }
            return true;
        }
        this.isScrollingHorizontally = false;
        this.isInsideCenterPanelOnActionDown = isTouchingCenterPanelAtRest(event);
        this.xOffsetFromCenterPanel = getCenterPanel().getX() - event.getRawX();
        this.downX = event.getX();
        float eventY = event.getY();
        this.downY = eventY;
        this.isHomeSystemGesture = Math.abs(eventY - getResources().getDisplayMetrics().heightPixels) < this.homeGestureFromBottomThreshold && this.homeGestureSupported;
        VelocityTracker velocityTracker2 = this.velocityTracker;
        if (velocityTracker2 == null) {
            VelocityTracker velocityTrackerObtain = VelocityTracker.obtain();
            this.velocityTracker = velocityTrackerObtain;
            if (velocityTrackerObtain != null) {
                velocityTrackerObtain.addMovement(event);
            }
        } else {
            velocityTracker2.clear();
        }
        return this.isInsideCenterPanelOnActionDown;
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (getChildCount() == 3 && this.centerPanel == null) {
            handlePanelsLaid();
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        if (this.isHomeSystemGesture) {
            return false;
        }
        int actionMasked = event.getActionMasked();
        if (actionMasked != 0) {
            if (actionMasked != 1) {
                if (actionMasked == 2) {
                    if (isInGestureExclusionRegion(event)) {
                        return false;
                    }
                    float dx = dxFromDown(this.downX, event);
                    if (Math.abs(dx) > this.scrollSlop && this.scrollDirection == null) {
                        this.scrollDirection = dx > 0.0f ? OverlappingPanelsLayout.ScrollDirection.RIGHT : OverlappingPanelsLayout.ScrollDirection.LEFT;
                    }
                    VelocityTracker velocityTracker = this.velocityTracker;
                    if (velocityTracker != null) {
                        velocityTracker.addMovement(event);
                    }
                    if (shouldFollowTouchScroll(event)) {
                        handleScroll(event);
                    }
                    return true;
                }
                if (actionMasked != 3) {
                    return false;
                }
            }
            boolean tap = this.isInsideCenterPanelOnActionDown && Math.abs(event.getX() - this.downX) < this.scrollSlop && !this.isScrollingHorizontally;
            if (tap) {
                closePanels();
            } else {
                VelocityTracker velocityTracker2 = this.velocityTracker;
                if (velocityTracker2 != null) {
                    velocityTracker2.addMovement(event);
                }
                handleFling(event);
            }
            this.isInsideCenterPanelOnActionDown = false;
            this.isScrollingHorizontally = false;
            this.scrollDirection = null;
        }
        return true;
    }

    public PanelState getStartPanelState() {
        return this.startPanelState;
    }

    public final void handleEndPanelClosed() {
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public OverlappingPanelsLayout(Context context, AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
        Intrinsics.checkNotNullParameter(context, "context");
        this.isLeftToRight = true;
        this.startPanelOpenedCenterPanelX = Float.MIN_VALUE;
        this.endPanelOpenedCenterPanelX = Float.MAX_VALUE;
        this.startPanelStateListeners = new ArrayList<PanelStateListener>();
        this.endPanelStateListeners = new ArrayList<PanelStateListener>();
        this.selectedPanel = OverlappingPanelsLayout.Panel.CENTER;
        LockState unlockedState = OverlappingPanelsLayout.LockState.UNLOCKED;
        this.startPanelLockState = unlockedState;
        this.endPanelLockState = unlockedState;
        PanelState.Closed closedState = PanelState.Closed.INSTANCE;
        this.startPanelState = closedState;
        this.endPanelState = closedState;
        this.childGestureRegions = java.util.Collections.emptyList();
        this.homeGestureSupported = Build.VERSION.SDK_INT >= 29;
        initFromAttributes(attributeSet);
    }
}
