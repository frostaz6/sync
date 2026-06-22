package com.resync.app.widgets;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;
import com.resync.app.R;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.activities.BaseActivity;
import com.resync.app.ui.activities.CasualActivity;
import com.resync.app.helpers.ui.MonetUtil;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.events.DragCloseEvent;
import com.resync.app.events.DragStartingEvent;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.helpers.WindowHelper;
import com.resync.app.helpers.WindowUtils;
import com.resync.app.ui.interfaces.DarkModeAware;
import com.resync.app.ui.interfaces.SnackbarInterface;
import com.resync.app.ui.interfaces.ToolbarInterface;
import com.resync.app.util.Logger;

/* JADX INFO: renamed from: ac.a */
/* JADX INFO: compiled from: DragLayout.java */
/* JADX INFO: loaded from: classes3.dex */
public class DragLayout extends FrameLayout implements DarkModeAware {
    private int backgroundColor;
    DragHelperWrapper dragHelperWrapper;
    private CloseListener closeListener;
    ColorDrawable dimDrawable;
    private boolean closing;

    private int closeThreshold;
    private View contentView;
    private ViewGroup container;

    private float totalDx;

    private float totalDy;

    private float lastX;
    private float lastY;

    private float startX;
    private boolean disabled;
    private boolean tracking;
    private int offset;

    private boolean closed;
    private int edgeSize;

    /* JADX INFO: renamed from: ac.a$a */
    /* JADX INFO: compiled from: DragLayout.java */
    class DragContainer extends RelativeLayout {
        DragContainer(Context context) {
            super(context);
        }

        @Override // android.widget.RelativeLayout, android.view.ViewGroup, android.view.View
        protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
            try {
                super.onLayout(changed, left, top, right, bottom);
            } catch (NullPointerException unused) {
            }
            if (DragLayout.this.tracking) {
                offsetLeftAndRight(DragLayout.this.offset);
            }
        }

        @Override // android.widget.RelativeLayout, android.view.View, android.view.ViewParent
        public void requestLayout() {
            if (DragLayout.this.closing) {
                return;
            }
            super.requestLayout();
        }
    }

    /* JADX INFO: renamed from: ac.a$b */
    /* JADX INFO: compiled from: DragLayout.java */
    private class DragCallback extends ViewDragHelper.Callback {
        private DragCallback() {
        }

        @Override // androidx.customview.widget.ViewDragHelper.Callback
        public int clampViewPositionHorizontal(View view, int left, int dx) {
            return Math.max(0, left);
        }

        @Override // androidx.customview.widget.ViewDragHelper.Callback
        public int getViewHorizontalDragRange(View view) {
            return DragLayout.this.container.getWidth();
        }

        @Override // androidx.customview.widget.ViewDragHelper.Callback
        public void onEdgeTouched(int edgeFlags, int pointerId) {
            DragLayout dragLayout = DragLayout.this;
            dragLayout.dragHelperWrapper.captureChildView(dragLayout.container, pointerId);
        }

        @Override // androidx.customview.widget.ViewDragHelper.Callback
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            super.onEdgeDragStarted(edgeFlags, pointerId);
        }

        @Override // androidx.customview.widget.ViewDragHelper.Callback
        public void onViewCaptured(View view, int activePointerId) {
            super.onViewCaptured(view, activePointerId);
        }

        @Override // androidx.customview.widget.ViewDragHelper.Callback
        public void onViewDragStateChanged(int state) {
            if (state == 0) {
                if (DragLayout.this.container.getLeft() == 0 && DragLayout.this.closeListener != null) {
                    DragLayout.this.closeListener.onSettled();
                }
                DragLayout.this.onDragFinished();
            } else if (state == 1) {
                if (DragLayout.this.closeListener != null) {
                    DragLayout.this.closeListener.onDragging();
                }
                DragLayout.this.onDragStarting();
            }
            super.onViewDragStateChanged(state);
        }

        @Override // androidx.customview.widget.ViewDragHelper.Callback
        public void onViewPositionChanged(View view, int left, int top, int dx, int dy) {
            DragLayout.this.invalidate();
        }

        @Override // androidx.customview.widget.ViewDragHelper.Callback
        public void onViewReleased(View view, float xvel, float yvel) {
            if (Math.abs(xvel) > DragLayout.this.dragHelperWrapper.getMinVelocity()) {
                if (xvel > 0.0f) {
                    DragLayout.this.closeRight();
                } else {
                    DragLayout.this.settleBack();
                }
            } else if (DragLayout.this.container.getLeft() > DragLayout.this.closeThreshold) {
                DragLayout.this.closeRight();
            } else {
                DragLayout.this.settleBack();
            }
            super.onViewReleased(view, xvel, 0.0f);
        }

        @Override // androidx.customview.widget.ViewDragHelper.Callback
        public boolean tryCaptureView(View view, int pointerId) {
            boolean zEquals = view.equals(DragLayout.this.container);
            if (zEquals) {
                BusSingleton.getBus().post(new DragStartingEvent());
            }
            return zEquals;
        }

        /* synthetic */ DragCallback(DragLayout dragLayout, DragContainer marker) {
            this();
        }
    }

    /* JADX INFO: renamed from: ac.a$c */
    /* JADX INFO: compiled from: DragLayout.java */
    public interface CloseListener {
        void onDragging();

        void onSettled();
    }

    /* JADX INFO: renamed from: ac.a$d */
    /* JADX INFO: compiled from: DragLayout.java */
    class DragHelperWrapper {

        private ViewDragHelper dragHelper;

        public DragHelperWrapper(DragLayout dragLayout, float minVelocity) {
            ViewDragHelper viewDragHelperCreate = ViewDragHelper.create(dragLayout, SettingsSingleton.getSettings().swipeSensitivity, new DragCallback(DragLayout.this, null));
            this.dragHelper = viewDragHelperCreate;
            viewDragHelperCreate.setMinVelocity(minVelocity);
        }

        public void cancel() {
            this.dragHelper.cancel();
        }

        public void captureChildView(View view, int activePointerId) {
            this.dragHelper.captureChildView(view, activePointerId);
        }

        public boolean continueSettling(boolean deferCallbacks) {
            return this.dragHelper.continueSettling(deferCallbacks);
        }

        public float getMinVelocity() {
            return this.dragHelper.getMinVelocity();
        }

        public void processTouchEvent(MotionEvent motionEvent) {
            this.dragHelper.processTouchEvent(motionEvent);
        }

        public boolean shouldInterceptTouchEvent(MotionEvent motionEvent) {
            return this.dragHelper.shouldInterceptTouchEvent(motionEvent);
        }

        public boolean smoothSlideViewTo(View view, int finalLeft, int finalTop) {
            return this.dragHelper.smoothSlideViewTo(view, finalLeft, finalTop);
        }
    }

    private DragLayout(Context context, int backgroundColor) {
        super(context);
        this.closing = false;
        this.backgroundColor = backgroundColor;
        float density = context.getResources().getDisplayMetrics().density;
        this.edgeSize = (int) ((20.0f * density) + 0.5f);
        if (SettingsSingleton.getSettings().swipeDim) {
            this.dimDrawable = new ColorDrawable(-16777216);
        }
        this.closeThreshold = (int) (density * 120.0f);
        this.dragHelperWrapper = new DragHelperWrapper(this, 1000.0f * density);
        this.container = new DragContainer(context);
        this.container.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        addView(this.container);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    public boolean closeRight() {
        DragHelperWrapper helper = this.dragHelperWrapper;
        ViewGroup viewGroup = this.container;
        if (!helper.smoothSlideViewTo(viewGroup, viewGroup.getWidth(), 0)) {
            return false;
        }
        BusSingleton.getBus().post(new DragCloseEvent());
        this.closing = true;
        ViewCompat.postInvalidateOnAnimation(this);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onDragFinished() {
        Logger.verbose("Drag finished");
        setBackgroundDrawable(null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean settleBack() {
        if (!this.dragHelperWrapper.smoothSlideViewTo(this.container, 0, 0)) {
            return false;
        }
        ViewCompat.postInvalidateOnAnimation(this);
        return true;
    }

    public static DragLayout create(Activity activity, int layoutRes) {
        View viewInflate = View.inflate(activity, layoutRes, null);
        viewInflate.setBackgroundColor(0);
        DragLayout dragLayout = new DragLayout(activity, MonetUtil.getWindowBackgroundColorAuto());
        dragLayout.container.addView(viewInflate);
        dragLayout.contentView = viewInflate;
        viewInflate.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
        return dragLayout;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onDragStarting() {
        Logger.verbose("Starting drag");
        setBackgroundDrawable(this.dimDrawable);
    }

    @Override // android.view.View
    public void computeScroll() {
        if (this.dimDrawable != null) {
            // restored (float) cast dropped by JADX — see Vineflower ac/a.java; int/int div-by-zero otherwise (getWidth()==0 before layout)
            this.dimDrawable.setAlpha((int) ((1.0f - ((float) this.container.getLeft() / this.container.getWidth())) * 0.6f * 255.0f));
        }
        if (this.dragHelperWrapper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        } else {
            this.offset = this.container.getLeft();
        }
        if (this.container.getLeft() != this.container.getWidth() || this.closed) {
            return;
        }
        this.closed = true;
        this.container.setVisibility(8);
        ((Activity) getContext()).overridePendingTransition(0, 0);
        ((Activity) getContext()).finish();
        ((Activity) getContext()).overridePendingTransition(0, 0);
    }

    @Override // com.resync.app.ui.interfaces.DarkModeAware
    public void onDarkModeChanged() {
        this.backgroundColor = MonetUtil.getWindowBackgroundColorAuto();
        applyBackground();
    }

    public boolean isClosed() {
        return this.closed;
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        View viewFindViewById;
        int action = MotionEventCompat.getActionMasked(motionEvent);
        Logger.verbose("Touch event: " + this.disabled + StringUtils.SPACE + this.closing + StringUtils.SPACE + action);
        if (((getContext() instanceof CasualActivity) && ((CasualActivity) getContext()).areAnyPanelsOpen()) || this.disabled || this.closing) {
            return false;
        }
        if (action == 3 || action == 1) {
            this.dragHelperWrapper.cancel();
            this.tracking = false;
            return false;
        }
        getContext();
        if ((getContext() instanceof ToolbarInterface) && motionEvent.getY() > WindowHelper.getActionBarHeight(getContext())) {
            this.dragHelperWrapper.cancel();
            return false;
        }
        int height = (!(getContext() instanceof BaseActivity) || (viewFindViewById = ((BaseActivity) getContext()).findViewById(R.id.ad_wrapper)) == null) ? 0 : viewFindViewById.getHeight();
        if ((getContext() instanceof SnackbarInterface) && ((SnackbarInterface) getContext()).isContentLoading() && motionEvent.getY() >= WindowUtils.getDisplayHeight() - (SizeHelper.dpToPxFloat(56) + height)) {
            this.dragHelperWrapper.cancel();
            return false;
        }
        if (action == 0) {
            this.tracking = true;
            this.startX = motionEvent.getX();
            this.totalDy = 0.0f;
            this.totalDx = 0.0f;
            this.lastX = motionEvent.getX();
            this.lastY = motionEvent.getY();
        } else if (action == 2) {
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            this.totalDx += Math.abs(x - this.lastX);
            float fAbs = this.totalDy + Math.abs(y - this.lastY);
            this.totalDy = fAbs;
            this.lastX = x;
            this.lastY = y;
            if (this.totalDx < fAbs) {
                this.dragHelperWrapper.cancel();
                return false;
            }
            if (motionEvent.getX() - this.startX < 0.0f) {
                this.dragHelperWrapper.cancel();
                return false;
            }
        }
        try {
            return this.dragHelperWrapper.shouldInterceptTouchEvent(motionEvent);
        } catch (Exception unused) {
            return true;
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.disabled) {
            return false;
        }
        try {
            this.dragHelperWrapper.processTouchEvent(motionEvent);
            return true;
        } catch (Exception unused) {
            return true;
        }
    }

    public void setCloseListener(CloseListener listener) {
        this.closeListener = listener;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public void applyBackground() {
        ((Activity) getContext()).getWindow().setBackgroundDrawable(new ColorDrawable(0));
        this.contentView.setBackgroundDrawable(new ColorDrawable(this.backgroundColor));
    }

    @Override // android.view.View
    public void setBackgroundColor(int color) {
        this.contentView.setBackgroundDrawable(new ColorDrawable(color));
    }
}
