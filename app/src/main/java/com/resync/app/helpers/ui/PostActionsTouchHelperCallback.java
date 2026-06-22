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
import com.resync.app.media.images.SyncTypeHelper;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.events.StopExoplayerEvent;
import com.resync.app.managers.SaveManager;
import com.resync.app.managers.VoteManager;
import com.resync.app.helpers.PostsHelper;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.holders.BasePostViewHolder;
import com.resync.app.util.VibrateUtils;
import com.resync.app.model.Post;
import com.resync.app.presenters.PostsPresenter;

/* JADX INFO: renamed from: gb.k */
/* JADX INFO: compiled from: PostActionsTouchHelperCallback.java */
/* JADX INFO: loaded from: classes2.dex */
public class PostActionsTouchHelperCallback extends ItemTouchHelper.Callback {
    private Context context;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private PostsPresenter presenter;
    boolean firstTriggered;
    boolean secondTriggered;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    boolean thirdTriggered;
    Drawable firstIcon;
    Drawable secondIcon;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    Drawable thirdIcon;
    int secondColor;
    int thirdColor;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    Paint paint = new Paint();

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    int firstColor = getActionColor(SettingsSingleton.getSettings().postNavigationFirst);

    public PostActionsTouchHelperCallback(Context context, PostsPresenter postsPresenter) {
        this.context = context;
        this.presenter = postsPresenter;
        Drawable drawableD = getActionIcon(SettingsSingleton.getSettings().postNavigationFirst);
        this.firstIcon = drawableD;
        Drawable drawableMutate = drawableD.mutate();
        this.firstIcon = drawableMutate;
        drawableMutate.setTint(-1);
        this.secondColor = getActionColor(SettingsSingleton.getSettings().postNavigationSecond);
        Drawable drawableD2 = getActionIcon(SettingsSingleton.getSettings().postNavigationSecond);
        this.secondIcon = drawableD2;
        Drawable drawableMutate2 = drawableD2.mutate();
        this.secondIcon = drawableMutate2;
        drawableMutate2.setTint(-1);
        this.thirdColor = getActionColor(SettingsSingleton.getSettings().postNavigationThird);
        Drawable drawableD3 = getActionIcon(SettingsSingleton.getSettings().postNavigationThird);
        this.thirdIcon = drawableD3;
        Drawable drawableMutate3 = drawableD3.mutate();
        this.thirdIcon = drawableMutate3;
        drawableMutate3.setTint(-1);
    }

    private int getActionColor(int actionCode) {
        if (actionCode == 0) {
            return -35584;
        }
        if (actionCode == 1) {
            return -7039745;
        }
        if (actionCode == 2) {
            return -1324481;
        }
        if (actionCode == 3) {
            return -12285185;
        }
        if (actionCode == 4) {
            return -720809;
        }
        if (actionCode == 5 || actionCode == 6) {
            return -4776932;
        }
        throw new RuntimeException("Unsupported code: " + actionCode);
    }

    private Drawable getActionIcon(int actionCode) {
        if (actionCode == 0) {
            return this.context.getResources().getDrawable(R.drawable.outline_arrow_upward_24);
        }
        if (actionCode == 1) {
            return this.context.getResources().getDrawable(R.drawable.outline_arrow_downward_24);
        }
        if (actionCode == 2) {
            return this.context.getResources().getDrawable(R.drawable.outline_bookmark_border_24);
        }
        if (actionCode == 3) {
            return this.context.getResources().getDrawable(R.drawable.outline_account_circle_24);
        }
        if (actionCode == 4) {
            return this.context.getResources().getDrawable(R.drawable.outline_share_24);
        }
        if (actionCode == 5) {
            return this.context.getResources().getDrawable(R.drawable.ic_close_white_24dp);
        }
        if (actionCode == 6) {
            return this.context.getResources().getDrawable(R.drawable.outline_eye_hide);
        }
        throw new RuntimeException("Unsupported code: " + actionCode);
    }

    private void performAction(RecyclerView.ViewHolder viewHolder, Post post, int actionCode) {
        if (actionCode == 0) {
            VoteManager.voteOnPost(this.context, 0, post, 1);
            return;
        }
        if (actionCode == 1) {
            VoteManager.voteOnPost(this.context, 0, post, -1);
            return;
        }
        if (actionCode == 2) {
            SaveManager.toggleSave(this.context, post);
            return;
        }
        if (actionCode == 3) {
            com.resync.app.util.links.LinkHandler.openProfile(this.context, post.getAuthor());
            return;
        }
        if (actionCode == 4) {
            this.presenter.sharePost(post);
            return;
        }
        if (actionCode == 5) {
            this.presenter.hidePost(post);
        } else {
            if (actionCode == 6) {
                PostsHelper.markViewed(this.context, post);
                return;
            }
            throw new RuntimeException("Unsupported code: " + actionCode);
        }
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        BusSingleton.getBus().post(new StopExoplayerEvent());
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (viewHolder instanceof BasePostViewHolder) {
            performAction(viewHolder, ((BasePostViewHolder) viewHolder).getPost(), SettingsSingleton.getSettings().postNavigationThird);
        }
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if (viewHolder instanceof BasePostViewHolder) {
            Post post = ((BasePostViewHolder) viewHolder).getPost();
            if (this.thirdTriggered) {
                performAction(viewHolder, post, SettingsSingleton.getSettings().postNavigationThird);
            } else if (this.secondTriggered) {
                performAction(viewHolder, post, SettingsSingleton.getSettings().postNavigationSecond);
            } else if (this.firstTriggered) {
                performAction(viewHolder, post, SettingsSingleton.getSettings().postNavigationFirst);
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
        if ((viewHolder instanceof BasePostViewHolder) && SyncTypeHelper.supportsSwipeActions(((BasePostViewHolder) viewHolder).getPost().getSyncType())) {
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
        // own long-press detection, competing with the row's long-press. Disable it.
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
