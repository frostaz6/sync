package com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.util.Property;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.ui.views.core.MaterialRow;
import com.resync.app.ui.views.core.SheetHeaderView;
import com.resync.app.ui.views.monet.buttons.MonetActionButtons;
import com.resync.app.ui.views.responsive.CustomProgressBar;
import java.io.Serializable;
import java.util.ArrayList;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.text.spans.MonetRoundedCornerSpan;
import com.resync.app.dialogs.CustomBottomSheetDialogFragment;
import com.resync.app.helpers.NightHelper;
import com.resync.app.dialogs.interfaces.SelectionDialogBottomSheetInterface;
import com.resync.app.util.ColorUtils;
import com.resync.app.util.Logger;

/* JADX INFO: loaded from: classes.dex */
public abstract class AbstractSelectionDialogBottomSheet extends CustomBottomSheetDialogFragment implements SelectionDialogBottomSheetInterface {
    private ArrayList<SelectionItem> mItems = new ArrayList<>();

    @BindView(R.id.abstract_selection_dialog_bottom_sheet_root_wrapper)
    RelativeLayout mAsyncRootWrapper;

    @BindView(R.id.abstract_selection_dialog_bottom_sheet_async_wrapper)
    RelativeLayout mAsyncWrapper;

    @BindView(R.id.abstract_selection_dialog_bottom_sheet_buttons)
    View mBottomButtons;

    @BindView(R.id.abstract_selection_dialog_bottom_sheet_main_wrapper)
    LinearLayout mMainWrapper;

    @BindView(R.id.monet_action_buttons)
    MonetActionButtons mMonetActionButtons;

    @BindView(R.id.abstract_selection_dialog_bottom_sheet_async_progress_bar)
    CustomProgressBar mProgressBar;

    @BindView(R.id.abstract_selection_dialog_bottom_sheet_items)
    RecyclerView mRecyclerView;

    @BindView(R.id.abstract_selection_dialog_bottom_sheet_header)
    SheetHeaderView mSectionHeader;

    class AbstractSelectionDialogBottomSheetItemHolder extends RecyclerView.ViewHolder {

        SelectionItem boundItem;

        @BindView(R.id.abstract_alert_dialog_bottom_sheet_item_indicator)
        AppCompatImageButton mIndicator;

        @BindView(R.id.abstract_alert_dialog_bottom_sheet_item_row)
        MaterialRow mRow;

        public AbstractSelectionDialogBottomSheetItemHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.mRow.applyColorsAuto();
        }

        public void bind(SelectionItem selectionItem) {
            this.boundItem = selectionItem;
            if (selectionItem instanceof SubredditNameItem) {
                this.mRow.showSubredditIcon();
                this.mRow.setTitle(selectionItem.title);
                this.mRow.setSubreddit(((SubredditNameItem) selectionItem).getSubreddit());
                this.mIndicator.setVisibility(8);
                return;
            }
            if (selectionItem instanceof RemovableSubredditItem) {
                this.mRow.showSubredditIcon();
                this.mRow.setTitle(selectionItem.title);
                this.mRow.setSubreddit(((RemovableSubredditItem) selectionItem).getSubreddit());
                this.mIndicator.setVisibility(0);
                this.mIndicator.setImageTintList(ColorStateList.valueOf(NightHelper.isNight() ? -8947849 : -6645094));
                this.mIndicator.setImageResource(R.drawable.ic_more_vert_white_24dp);
                this.mIndicator.setEnabled(true);
                return;
            }
            if (selectionItem instanceof UsernameItem) {
                this.mRow.showUserIcon();
                this.mRow.setTitle(selectionItem.title);
                this.mRow.setUsername(((UsernameItem) selectionItem).getUsername());
                this.mIndicator.setVisibility(8);
                return;
            }
            if (selectionItem instanceof ImageSelectionItem) {
                this.mRow.setTitle(selectionItem.title);
                this.mRow.showImage();
                this.mRow.loadImage(((ImageSelectionItem) selectionItem).getImageUrl());
                this.mIndicator.setVisibility(8);
                return;
            }
            if (selectionItem instanceof PaintedUserItem) {
                int color = ((PaintedUserItem) selectionItem).color;
                this.mRow.setTitle(selectionItem.title);
                this.mRow.setSecondary(ColorUtils.toHexColor(color));
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                spannableStringBuilder.append((CharSequence) selectionItem.title);
                spannableStringBuilder.setSpan(new MonetRoundedCornerSpan(color), 0, spannableStringBuilder.length(), 33);
                spannableStringBuilder.append((CharSequence) StringUtils.SPACE);
                this.mRow.setTitle(spannableStringBuilder);
                this.mRow.showUserIcon();
                this.mRow.setUsername(selectionItem.title);
                this.mIndicator.setVisibility(8);
                this.mRow.showMore();
                return;
            }
            if (selectionItem instanceof TaggedUserItem) {
                String str = ((TaggedUserItem) selectionItem).secondary;
                this.mRow.setTitle(selectionItem.title);
                this.mRow.showUserIcon();
                this.mRow.setUsername(selectionItem.title);
                this.mRow.setSecondary(str);
                this.mIndicator.setVisibility(8);
                this.mRow.showMore();
                return;
            }
            this.mRow.setTitle(selectionItem.title);
            this.mRow.setIconResource(selectionItem.iconRes);
            this.mRow.setHighlighted(selectionItem.isHighlighted());
            if (!(selectionItem instanceof ExpandableSelectionItem)) {
                this.mIndicator.setVisibility(8);
                return;
            }
            this.mIndicator.setVisibility(0);
            this.mIndicator.setImageTintList(ColorStateList.valueOf(NightHelper.isNight() ? -8947849 : -6645094));
            this.mIndicator.setImageResource(R.drawable.ic_expand_more_white_24dp);
            this.mIndicator.setEnabled(false);
            this.mIndicator.setClickable(false);
        }

        public MaterialRow getRow() {
            return this.mRow;
        }

        @OnClick
        public void onIndicatorClicked(View view) {
            AbstractSelectionDialogBottomSheet.this.onItemMoreClicked(view, this.boundItem);
        }
    }

    public static class AbstractSelectionDialogBottomSheetItemHolder_ViewBinding implements Unbinder {
        private AbstractSelectionDialogBottomSheetItemHolder target;
        private View indicatorClickView;

        /* JADX INFO: compiled from: AbstractSelectionDialogBottomSheet$AbstractSelectionDialogBottomSheetItemHolder_ViewBinding.java */
        static class ItemClickListener extends com.resync.app.util.DebouncingOnClickListener {

            /* JADX INFO: renamed from: q, reason: collision with root package name */
            final /* synthetic */ AbstractSelectionDialogBottomSheetItemHolder holder;

            ItemClickListener(AbstractSelectionDialogBottomSheetItemHolder abstractSelectionDialogBottomSheetItemHolder) {
                this.holder = abstractSelectionDialogBottomSheetItemHolder;
            }

            @Override // com.resync.app.util.DebouncingOnClickListener
            public void doClick(View view) {
                this.holder.onIndicatorClicked(view);
            }
        }

        public AbstractSelectionDialogBottomSheetItemHolder_ViewBinding(AbstractSelectionDialogBottomSheetItemHolder abstractSelectionDialogBottomSheetItemHolder, View view) {
            this.target = abstractSelectionDialogBottomSheetItemHolder;
            abstractSelectionDialogBottomSheetItemHolder.mRow = (MaterialRow) Utils.findRequiredViewAsType(view, R.id.abstract_alert_dialog_bottom_sheet_item_row, "field 'mRow'", MaterialRow.class);
            View viewC = Utils.findRequiredView(view, R.id.abstract_alert_dialog_bottom_sheet_item_indicator, "field 'mIndicator' and method 'onIndicatorClicked'");
            abstractSelectionDialogBottomSheetItemHolder.mIndicator = (AppCompatImageButton) Utils.castView(viewC, R.id.abstract_alert_dialog_bottom_sheet_item_indicator, "field 'mIndicator'", AppCompatImageButton.class);
            this.indicatorClickView = viewC;
            viewC.setOnClickListener(new ItemClickListener(abstractSelectionDialogBottomSheetItemHolder));
        }

        @Override // butterknife.Unbinder
        public void unbind() {
            AbstractSelectionDialogBottomSheetItemHolder target = this.target;
            if (target == null) {
                throw new IllegalStateException("Bindings already cleared.");
            }
            this.target = null;
            target.mRow = null;
            target.mIndicator = null;
            View indicatorView = this.indicatorClickView;
            if (indicatorView != null) {
                indicatorView.setOnClickListener(null);
                this.indicatorClickView = null;
            }
        }
    }

    class HeightAnimatorListener implements ValueAnimator.AnimatorUpdateListener {

        final /* synthetic */ int targetHeight;

        HeightAnimatorListener(int targetHeight) {
            this.targetHeight = targetHeight;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            int iIntValue = ((Integer) valueAnimator.getAnimatedValue()).intValue();
            if (iIntValue == this.targetHeight) {
                ViewGroup.LayoutParams layoutParams = AbstractSelectionDialogBottomSheet.this.mAsyncRootWrapper.getLayoutParams();
                layoutParams.height = -2;
                AbstractSelectionDialogBottomSheet.this.mAsyncRootWrapper.setLayoutParams(layoutParams);
            } else {
                ViewGroup.LayoutParams layoutParams2 = AbstractSelectionDialogBottomSheet.this.mAsyncRootWrapper.getLayoutParams();
                layoutParams2.height = iIntValue;
                AbstractSelectionDialogBottomSheet.this.mAsyncRootWrapper.setLayoutParams(layoutParams2);
            }
        }
    }

    class SelectionAdapter extends RecyclerView.Adapter<AbstractSelectionDialogBottomSheetItemHolder> {

        class ItemClickListener implements View.OnClickListener {

            final /* synthetic */ int position;

            ItemClickListener(int position) {
                this.position = position;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                AbstractSelectionDialogBottomSheet abstractSelectionDialogBottomSheet = AbstractSelectionDialogBottomSheet.this;
                abstractSelectionDialogBottomSheet.onItemSelected(abstractSelectionDialogBottomSheet.getItem(this.position));
            }
        }

        public SelectionAdapter() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /* JADX INFO: renamed from: N, reason: merged with bridge method [inline-methods] */
        public void onBindViewHolder(AbstractSelectionDialogBottomSheetItemHolder abstractSelectionDialogBottomSheetItemHolder, int position) {
            abstractSelectionDialogBottomSheetItemHolder.bind(AbstractSelectionDialogBottomSheet.this.getItem(position));
            abstractSelectionDialogBottomSheetItemHolder.getRow().setOnClickListener(new ItemClickListener(position));
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /* JADX INFO: renamed from: O, reason: merged with bridge method [inline-methods] */
        public AbstractSelectionDialogBottomSheetItemHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            return AbstractSelectionDialogBottomSheet.this.new AbstractSelectionDialogBottomSheetItemHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.abstract_selection_dialog_bottom_sheet_item, viewGroup, false));
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /* JADX INFO: renamed from: n */
        public int getItemCount() {
            return AbstractSelectionDialogBottomSheet.this.mItems.size();
        }
    }

    public class ExpandableSelectionItem extends SelectionItem {
        public ExpandableSelectionItem(int iconRes, String str, boolean highlighted) {
            super(iconRes, str, highlighted);
        }
    }

    public class ImageSelectionItem extends SelectionItem {

        String imageUrl;

        public ImageSelectionItem(String str, String str2) {
            super(str);
            this.imageUrl = str2;
        }

        public String getImageUrl() {
            return this.imageUrl;
        }
    }

    public static class RemovableSubredditItem extends SelectionItem {
        public RemovableSubredditItem(String str) {
            super(str);
        }

        public String getSubreddit() {
            return this.title;
        }
    }

    public class PaintedUserItem extends SelectionItem {

        int color;
    }

    public class RecentSubredditItem extends SelectionItem {

        String title2;

        String id;

        public RecentSubredditItem(String str, String str2, String str3) {
            super(R.drawable.outline_access_time_24, str);
            this.title2 = str2;
            this.id = str3;
        }

        public String getId() {
            return this.id;
        }

        public String getTitle2() {
            return this.title2;
        }
    }

    public class SubredditNameItem extends SelectionItem {
        public String getSubreddit() {
            return this.title;
        }
    }

    public class TaggedUserItem extends SelectionItem {

        public String secondary;
    }

    public static class UsernameItem extends SelectionItem {
        public UsernameItem(String str) {
            super(str);
        }

        public String getUsername() {
            return this.title;
        }
    }

    public void setupItems() {
    }

    public final SelectionItem removeItem(SelectionItem selectionItem) {
        if (selectionItem == null) {
            throw new RuntimeException("Null item");
        }
        this.mItems.remove(selectionItem);
        this.mRecyclerView.getAdapter().notifyDataSetChanged();
        return selectionItem;
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public boolean isInitiallyExpanded() {
        return true;
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetInterface
    public int getLayoutRes() {
        return R.layout.abstract_selection_dialog_bottom_sheet;
    }

    /* JADX INFO: renamed from: m0, reason: merged with bridge method [inline-methods] */
    public void onMoreClicked(View view) {
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        ButterKnife.bind(this, view);
        if (isAsync()) {
            this.mAsyncWrapper.setVisibility(0);
            this.mMainWrapper.setVisibility(8);
        } else {
            showItems();
        }
        this.mSectionHeader.applyColors(isNightMode());
        this.mSectionHeader.setTitle(getTitle());
        this.mSectionHeader.setMoreVisible(hasMore());
        this.mSectionHeader.setDragButtonVisible(!isTopSheet());
        this.mProgressBar.applyColors(isNightMode());
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.mRecyclerView.setAdapter(new SelectionAdapter());
        if (hasMore()) {
            this.mSectionHeader.setMoreClickListener(new View.OnClickListener() { // from class: u9.o
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    AbstractSelectionDialogBottomSheet.this.onMoreClicked(view2);
                }
            });
        }
        setupItems();
    }

    public final SelectionItem addItem(SelectionItem selectionItem) {
        if (selectionItem == null) {
            throw new RuntimeException("Null item");
        }
        this.mItems.add(selectionItem);
        this.mRecyclerView.getAdapter().notifyDataSetChanged();
        return selectionItem;
    }

    public final SelectionItem getItem(int position) {
        return this.mItems.get(position);
    }

    public boolean isAsync() {
        return false;
    }

    public boolean hasMore() {
        return false;
    }

    public void showItems() {
        this.mAsyncWrapper.setVisibility(8);
        this.mMainWrapper.setVisibility(0);
        int height = this.mAsyncRootWrapper.getHeight();
        this.mAsyncRootWrapper.measure(0, 0);
        int measuredHeight = this.mAsyncRootWrapper.getMeasuredHeight();
        Logger.verbose("Previous height: " + height);
        Logger.verbose("New height: " + measuredHeight);
        if (!isAsync() || measuredHeight == 0 || height == 0 || measuredHeight <= height) {
            return;
        }
        ValueAnimator valueAnimatorOfInt = ValueAnimator.ofInt(height, measuredHeight);
        valueAnimatorOfInt.addUpdateListener(new HeightAnimatorListener(measuredHeight));
        this.mMainWrapper.setAlpha(0.0f);
        ObjectAnimator objectAnimatorOfFloat = ObjectAnimator.ofFloat(this.mMainWrapper, (Property<View, Float>) View.ALPHA, 1.0f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(220L);
        animatorSet.setInterpolator(new FastOutSlowInInterpolator());
        animatorSet.playTogether(valueAnimatorOfInt, objectAnimatorOfFloat);
        animatorSet.start();
    }

    public void onItemMoreClicked(View view, SelectionItem selectionItem) {
    }

    public static class SelectionItem implements Serializable {

        public int iconRes;
        public String title;
        public Object tag;

        public boolean highlighted;

        public SelectionItem() {
        }

        public SelectionItem(String str) {
            this.title = str;
        }

        public boolean isHighlighted() {
            return this.highlighted;
        }

        public String getTitle() {
            return this.title;
        }

        public String toString() {
            return this.title;
        }

        public SelectionItem(int iconRes, String str) {
            this.iconRes = iconRes;
            this.title = str;
        }

        public SelectionItem(int iconRes, String str, boolean highlighted) {
            this.iconRes = iconRes;
            this.title = str;
            this.highlighted = highlighted;
        }
    }
}
