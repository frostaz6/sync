package com.resync.app.helpers.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.resync.app.R;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.viewholders.comments.CommentHolder;
import com.resync.app.events.CommentDownvoteSwipeEvent;
import com.resync.app.events.CommentProfileSwipeEvent;
import com.resync.app.events.CommentReplySwipeEvent;
import com.resync.app.events.CommentSaveSwipeEvent;
import com.resync.app.events.CommentShareSwipeEvent;
import com.resync.app.events.CommentUpvoteSwipeEvent;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.util.VibrateUtils;
import com.resync.app.model.Post;

/* JADX INFO: renamed from: gb.m */
/* JADX INFO: compiled from: ReplyTouchHelperCallback.java */
/* JADX INFO: loaded from: classes2.dex */
public class ReplyTouchHelperCallback extends ItemTouchHelper.Callback {
    private Context context;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    boolean firstTriggered;
    boolean secondTriggered;
    boolean thirdTriggered;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    Drawable firstIcon;
    Drawable secondIcon;
    Drawable thirdIcon;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    int secondColor;
    int thirdColor;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    Paint paint = new Paint();

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    int firstColor = getActionColor(SettingsSingleton.getSettings().commentNavigationFirst);

    public ReplyTouchHelperCallback(Context context) {
        this.context = context;
        Drawable drawableD = getActionIcon(SettingsSingleton.getSettings().commentNavigationFirst);
        this.firstIcon = drawableD;
        Drawable drawableMutate = drawableD.mutate();
        this.firstIcon = drawableMutate;
        drawableMutate.setTint(-1);
        this.secondColor = getActionColor(SettingsSingleton.getSettings().commentNavigationSecond);
        Drawable drawableD2 = getActionIcon(SettingsSingleton.getSettings().commentNavigationSecond);
        this.secondIcon = drawableD2;
        Drawable drawableMutate2 = drawableD2.mutate();
        this.secondIcon = drawableMutate2;
        drawableMutate2.setTint(-1);
        this.thirdColor = getActionColor(SettingsSingleton.getSettings().commentNavigationThird);
        Drawable drawableD3 = getActionIcon(SettingsSingleton.getSettings().commentNavigationThird);
        this.thirdIcon = drawableD3;
        Drawable drawableMutate3 = drawableD3.mutate();
        this.thirdIcon = drawableMutate3;
        drawableMutate3.setTint(-1);
    }

    private int getActionColor(int actionCode) {
        if (actionCode == 0) {
            return -12927123;
        }
        if (actionCode == 1) {
            return -35584;
        }
        if (actionCode == 2) {
            return -7039745;
        }
        if (actionCode == 3) {
            return -1324481;
        }
        if (actionCode == 4) {
            return -12285185;
        }
        if (actionCode == 5) {
            return -720809;
        }
        throw new RuntimeException("Unsupported code: " + actionCode);
    }

    private Drawable getActionIcon(int actionCode) {
        if (actionCode == 0) {
            return this.context.getResources().getDrawable(R.drawable.outline_reply);
        }
        if (actionCode == 1) {
            return this.context.getResources().getDrawable(R.drawable.outline_arrow_upward_24);
        }
        if (actionCode == 2) {
            return this.context.getResources().getDrawable(R.drawable.outline_arrow_downward_24);
        }
        if (actionCode == 3) {
            return this.context.getResources().getDrawable(R.drawable.outline_bookmark_border_24);
        }
        if (actionCode == 4) {
            return this.context.getResources().getDrawable(R.drawable.outline_account_circle_24);
        }
        if (actionCode == 5) {
            return this.context.getResources().getDrawable(R.drawable.outline_share_24);
        }
        throw new RuntimeException("Unsupported code: " + actionCode);
    }

    private void performAction(Post post, int actionCode) {
        if (actionCode == 0) {
            BusSingleton.getBus().post(new CommentReplySwipeEvent(post));
            return;
        }
        if (actionCode == 1) {
            BusSingleton.getBus().post(new CommentUpvoteSwipeEvent(post));
            return;
        }
        if (actionCode == 2) {
            BusSingleton.getBus().post(new CommentDownvoteSwipeEvent(post));
            return;
        }
        if (actionCode == 3) {
            BusSingleton.getBus().post(new CommentSaveSwipeEvent(post));
            return;
        }
        if (actionCode == 4) {
            BusSingleton.getBus().post(new CommentProfileSwipeEvent(post));
        } else {
            if (actionCode == 5) {
                BusSingleton.getBus().post(new CommentShareSwipeEvent(post));
                return;
            }
            throw new RuntimeException("Unsupported code: " + actionCode);
        }
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if (viewHolder instanceof CommentHolder) {
            Post post = ((CommentHolder) viewHolder).getPost();
            if (this.thirdTriggered) {
                performAction(post, SettingsSingleton.getSettings().commentNavigationThird);
            } else if (this.secondTriggered) {
                performAction(post, SettingsSingleton.getSettings().commentNavigationSecond);
            } else if (this.firstTriggered) {
                performAction(post, SettingsSingleton.getSettings().commentNavigationFirst);
            }
        }
        this.firstTriggered = false;
        this.secondTriggered = false;
        this.thirdTriggered = false;
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
    public long getAnimationDuration(RecyclerView recyclerView, int animationType, float animateDx, float animateDy) {
        return (long) (super.getAnimationDuration(recyclerView, animationType, animateDx, animateDy) * 1.25f);
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
    public float getMoveThreshold(RecyclerView.ViewHolder viewHolder) {
        return 1.0f;
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof CommentHolder) {
            return ItemTouchHelper.Callback.makeMovementFlags(3, 16);
        }
        return 0;
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
    public float getSwipeEscapeVelocity(float defaultValue) {
        return defaultValue * 5.0f;
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
    public float getSwipeThreshold(RecyclerView.ViewHolder viewHolder) {
        return 1.0f;
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
    public float getSwipeVelocityThreshold(float defaultValue) {
        return defaultValue * 0.5f;
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
    public boolean isLongPressDragEnabled() {
        // No drag-reorder here (swipe-action helper). Default true makes ItemTouchHelper run its
        // own long-press detection, which competed with the comment row's collapse long-press
        // (double haptic + spotty detection). Disable it so long-press is purely the row's.
        return false;
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
    public void onChildDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float deltaX, float deltaY, int actionState, boolean isCurrentlyActive) {
        int backgroundColor;
        Drawable drawable;
        if (deltaX == 0.0f) {
            this.firstTriggered = false;
            this.secondTriggered = false;
            this.thirdTriggered = false;
        }
        float scaledDx = (float) (((double) deltaX) * 0.9d);
        if (isCurrentlyActive) {
            Rect rect = new Rect();
            viewHolder.itemView.getWidth();
            viewHolder.itemView.getDrawingRect(rect);
            int columnWidth = rect.width() / 8;
            int inset = SizeHelper.dpToPx(68);
            int zone2Left = (columnWidth * 7) - inset;
            Rect rect2 = new Rect(zone2Left, rect.top, (columnWidth * 8) - inset, rect.bottom);
            int zone3Left = (columnWidth * 6) - inset;
            Rect rect3 = new Rect(zone3Left, rect.top, zone2Left, rect.bottom);
            int zone4Left = (columnWidth * 5) - inset;
            Rect rect4 = new Rect(zone4Left, rect.top, zone3Left, rect.bottom);
            Rect rect5 = new Rect(columnWidth - inset, rect.top, zone4Left, rect.bottom);
            int iconSize = SizeHelper.dpToPx(26);
            int top = viewHolder.itemView.getTop() + (((viewHolder.itemView.getBottom() - viewHolder.itemView.getTop()) - iconSize) / 2);
            int swipeX = (int) (rect.width() - Math.abs(scaledDx));
            if (rect2.contains(swipeX, 0)) {
                backgroundColor = this.firstColor;
                drawable = this.firstIcon;
                if (!this.firstTriggered) {
                    this.firstTriggered = true;
                    this.secondTriggered = false;
                    this.thirdTriggered = false;
                    VibrateUtils.vibrate(SettingsSingleton.getSettings().commentSwipeActionsVibrate);
                }
            } else if (rect3.contains(swipeX, 0)) {
                backgroundColor = this.secondColor;
                drawable = this.secondIcon;
                if (!this.secondTriggered) {
                    this.firstTriggered = false;
                    this.secondTriggered = true;
                    this.thirdTriggered = false;
                    VibrateUtils.vibrate(SettingsSingleton.getSettings().commentSwipeActionsVibrate);
                }
            } else if (rect4.contains(swipeX, 0)) {
                backgroundColor = this.thirdColor;
                drawable = this.thirdIcon;
                if (!this.thirdTriggered) {
                    this.firstTriggered = false;
                    this.secondTriggered = false;
                    this.thirdTriggered = true;
                    VibrateUtils.vibrate(SettingsSingleton.getSettings().commentSwipeActionsVibrate);
                }
            } else if (rect5.contains(swipeX, 0)) {
                backgroundColor = this.thirdColor;
                drawable = this.thirdIcon;
            } else {
                backgroundColor = ColorUtils.setAlphaComponent(MonetUtil.getBackgroundColorAuto(), 51);
                drawable = this.firstIcon;
                this.firstTriggered = false;
                this.secondTriggered = false;
                this.thirdTriggered = false;
            }
            this.paint.setColor(backgroundColor);
            canvas.drawRect(viewHolder.itemView.getLeft(), viewHolder.itemView.getTop(), viewHolder.itemView.getRight(), viewHolder.itemView.getBottom(), this.paint);
            if (drawable != null) {
                int iconLeft = Math.max((viewHolder.itemView.getWidth() - ((int) Math.abs(viewHolder.itemView.getTranslationX()))) + SizeHelper.dpToPx(16), ((rect2.right - iconSize) - SizeHelper.dpToPx(32)) + inset);
                drawable.setBounds(iconLeft, top, iconLeft + iconSize, iconSize + top);
                drawable.draw(canvas);
            }
            if (actionState == 1) {
                super.onChildDraw(canvas, recyclerView, viewHolder, -Math.min(Math.abs(scaledDx), Math.abs(rect.width() - rect4.left)), deltaY, actionState, isCurrentlyActive);
                return;
            }
        }
        super.onChildDraw(canvas, recyclerView, viewHolder, scaledDx, deltaY, actionState, isCurrentlyActive);
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return true;
    }
}
