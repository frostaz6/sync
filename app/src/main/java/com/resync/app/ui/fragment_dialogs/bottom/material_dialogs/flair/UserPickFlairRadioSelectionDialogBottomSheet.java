package com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.flair;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.Property;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.resync.app.R;
import com.resync.app.RedditApplication;
import com.resync.app.ui.util.recycler.CustomLinearLayoutManager;
import com.resync.app.ui.views.core.SheetHeaderView;
import com.resync.app.ui.views.monet.buttons.MonetActionButtons;
import com.resync.app.ui.views.responsive.CustomMaterialRadioButton;
import com.resync.app.ui.views.responsive.CustomProgressBar;
import com.resync.app.ui.views.text.spannable.children.image_components.flair.PostFlairTextView;
import com.resync.app.helpers.ui.StaticToast;
import java.util.ArrayList;
import java.util.Collections;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.model.AbstractFlair;
import com.resync.app.model.NoFlair;
import com.resync.app.model.TextFlair;
import com.resync.app.dialogs.CustomBottomSheetDialogFragment;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.net.oauth.OAuthFlairUserSelectorRequest;
import com.resync.app.net.oauth.OAuthSelectFlairUserRequest;
import com.resync.app.net.oauth.OAuthSubredditInfoRequest;
import com.resync.app.dialogs.EditUserFlairInputDialogBottomSheet;
import com.resync.app.model.Data;
import com.resync.app.model.Subreddit;

/* JADX INFO: loaded from: classes.dex */
public class UserPickFlairRadioSelectionDialogBottomSheet extends CustomBottomSheetDialogFragment {
    private ArrayList<AbstractFlair> mFlairs = new ArrayList<>();
    private AbstractFlair mSelectedFlair;

    @BindView(R.id.abstract_radio_selection_dialog_bottom_sheet_root_wrapper)
    RelativeLayout mAsyncRootWrapper;

    @BindView(R.id.abstract_radio_selection_dialog_bottom_sheet_async_wrapper)
    ViewGroup mAsyncWrapper;

    @BindView(R.id.abstract_radio_selection_dialog_bottom_sheet_buttons)
    ViewGroup mButtonsWrapper;

    @BindView(R.id.abstract_radio_selection_dialog_bottom_sheet_main_wrapper)
    ViewGroup mMainWrapper;

    @BindView(R.id.monet_action_buttons)
    MonetActionButtons mMonetActionButtons;

    @BindView(R.id.abstract_radio_selection_dialog_bottom_sheet_async_progress_bar)
    CustomProgressBar mProgressBar;

    @BindView(R.id.abstract_radio_selection_dialog_bottom_sheet_items)
    RecyclerView mRecyclerView;

    @BindView(R.id.abstract_radio_selection_dialog_bottom_sheet_header)
    SheetHeaderView mSectionHeader;

    public class FlairHolder extends RecyclerView.ViewHolder {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        AbstractFlair flair;

        @BindView(R.id.holder_post_flair_radio)
        CustomMaterialRadioButton mPostFlairRadioButton;

        @BindView(R.id.holder_post_flair)
        PostFlairTextView mPostFlairTextView;

        public FlairHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.mPostFlairTextView.setOnClickListener(null);
            this.mPostFlairTextView.setClickable(false);
        }

        public void bind(AbstractFlair abstractFlair) {
            this.flair = abstractFlair;
            this.mPostFlairTextView.bindFlair(abstractFlair);
            this.mPostFlairRadioButton.setChecked(StringUtils.equalsIgnoreCase(this.flair.id, UserPickFlairRadioSelectionDialogBottomSheet.this.mSelectedFlair.id));
        }

        public void recycle() {
            this.mPostFlairTextView.recycle();
        }
    }

    public static class FlairHolder_ViewBinding implements Unbinder {
        private FlairHolder target;

        public FlairHolder_ViewBinding(FlairHolder flairHolder, View view) {
            this.target = flairHolder;
            flairHolder.mPostFlairTextView = (PostFlairTextView) Utils.findRequiredViewAsType(view, R.id.holder_post_flair, "field 'mPostFlairTextView'", PostFlairTextView.class);
            flairHolder.mPostFlairRadioButton = (CustomMaterialRadioButton) Utils.findRequiredViewAsType(view, R.id.holder_post_flair_radio, "field 'mPostFlairRadioButton'", CustomMaterialRadioButton.class);
        }
    
        @Override // butterknife.Unbinder
        public void unbind() {
            FlairHolder target = this.target;
            if (target == null) {
                throw new IllegalStateException("Bindings already cleared.");
            }
            this.target = null;
            target.mPostFlairTextView = null;
            target.mPostFlairRadioButton = null;
        }
    }

    class FlairRecycledViewPool extends RecyclerView.RecycledViewPool {
        FlairRecycledViewPool() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.RecycledViewPool
        public void putRecycledView(RecyclerView.ViewHolder viewHolder) {
            if (viewHolder instanceof FlairHolder) {
                ((FlairHolder) viewHolder).recycle();
            }
            super.putRecycledView(viewHolder);
        }
    }

    class HeightAnimatorListener implements ValueAnimator.AnimatorUpdateListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ int targetHeight;

        HeightAnimatorListener(int targetHeight) {
            this.targetHeight = targetHeight;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            int iIntValue = ((Integer) valueAnimator.getAnimatedValue()).intValue();
            if (iIntValue == this.targetHeight) {
                ViewGroup.LayoutParams layoutParams = UserPickFlairRadioSelectionDialogBottomSheet.this.mAsyncRootWrapper.getLayoutParams();
                layoutParams.height = -2;
                UserPickFlairRadioSelectionDialogBottomSheet.this.mAsyncRootWrapper.setLayoutParams(layoutParams);
            } else {
                ViewGroup.LayoutParams layoutParams2 = UserPickFlairRadioSelectionDialogBottomSheet.this.mAsyncRootWrapper.getLayoutParams();
                layoutParams2.height = iIntValue;
                UserPickFlairRadioSelectionDialogBottomSheet.this.mAsyncRootWrapper.setLayoutParams(layoutParams2);
            }
        }
    }

    class SubredditInfoListener implements Response.Listener<Subreddit> {
        SubredditInfoListener() {
        }

        @Override // com.android.volley.Response.Listener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public void onResponse(Subreddit subreddit) {
            if (UserPickFlairRadioSelectionDialogBottomSheet.this.isDialogShowing()) {
                UserPickFlairRadioSelectionDialogBottomSheet.this.onSubredditLoaded(subreddit);
            }
        }
    }

    class SubredditInfoErrorListener implements Response.ErrorListener {
        SubredditInfoErrorListener() {
        }

        @Override // com.android.volley.Response.ErrorListener
        public void onErrorResponse(VolleyError volleyError) {
            if (UserPickFlairRadioSelectionDialogBottomSheet.this.isDialogShowing()) {
                StaticToast.show("No flair available");
                UserPickFlairRadioSelectionDialogBottomSheet.this.dismiss();
            }
        }
    }

    class FlairAdapter extends RecyclerView.Adapter<FlairHolder> {
        public FlairAdapter() {
            setHasStableIds(true);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void onRowClicked(int position, View view) {
            UserPickFlairRadioSelectionDialogBottomSheet userPickFlairRadioSelectionDialogBottomSheet = UserPickFlairRadioSelectionDialogBottomSheet.this;
            userPickFlairRadioSelectionDialogBottomSheet.selectFlair((AbstractFlair) userPickFlairRadioSelectionDialogBottomSheet.mFlairs.get(position));
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /* JADX INFO: renamed from: P, reason: merged with bridge method [inline-methods] */
        public void onBindViewHolder(FlairHolder flairHolder, final int position) {
            flairHolder.bind((AbstractFlair) UserPickFlairRadioSelectionDialogBottomSheet.this.mFlairs.get(position));
            flairHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.flair.c
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    UserPickFlairRadioSelectionDialogBottomSheet.FlairAdapter.this.onRowClicked(position, view);
                }
            });
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /* JADX INFO: renamed from: Q, reason: merged with bridge method [inline-methods] */
        public FlairHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            return UserPickFlairRadioSelectionDialogBottomSheet.this.new FlairHolder(LayoutInflater.from(UserPickFlairRadioSelectionDialogBottomSheet.this.getContext()).inflate(R.layout.holder_post_flair, viewGroup, false));
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /* JADX INFO: renamed from: n */
        public int getItemCount() {
            return UserPickFlairRadioSelectionDialogBottomSheet.this.mFlairs.size();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /* JADX INFO: renamed from: o */
        public long getItemId(int position) {
            return ((AbstractFlair) UserPickFlairRadioSelectionDialogBottomSheet.this.mFlairs.get(position)).id.hashCode();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onFlairsError(VolleyError volleyError) {
        if (isDialogShowing()) {
            StaticToast.show("No flair available");
            dismiss();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSubredditLoaded(Subreddit subreddit) {
        Data data;
        if (subreddit == null || (data = subreddit.data) == null || !StringUtils.isEmpty(data.userFlairTemplateId)) {
            this.mSelectedFlair = new TextFlair(subreddit.data.userFlairTemplateId, null);
        } else {
            this.mSelectedFlair = new NoFlair();
        }
        OAuthRequestManager.sendWithAppContext(new OAuthFlairUserSelectorRequest(RedditApplication.getAppContext(), new Response.Listener() { // from class: y9.p
            @Override // com.android.volley.Response.Listener
            public final void onResponse(Object obj) {
                UserPickFlairRadioSelectionDialogBottomSheet.this.onFlairsLoaded((AbstractFlair[]) obj);
            }
        }, new Response.ErrorListener() { // from class: y9.o
            @Override // com.android.volley.Response.ErrorListener
            public final void onErrorResponse(VolleyError volleyError) {
                UserPickFlairRadioSelectionDialogBottomSheet.this.onFlairsError(volleyError);
            }
        }, getSubreddit()));
    }

    private void loadSubredditInfo() {
        OAuthRequestManager.sendWithAppContext(new OAuthSubredditInfoRequest(RedditApplication.getAppContext(), null, getSubreddit(), new SubredditInfoListener(), new SubredditInfoErrorListener()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onCancelClicked(View view) {
        dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onEditTextClicked(View view) {
        if (this.mSelectedFlair instanceof NoFlair) {
            return;
        }
        FragmentUtils.showWithArguments(EditUserFlairInputDialogBottomSheet.class, getChildFragmentManager(), EditUserFlairInputDialogBottomSheet.buildArguments(getSubreddit(), this.mSelectedFlair));
        dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSelectClicked(View view) {
        OAuthRequestManager.sendWithAppContext(new OAuthSelectFlairUserRequest(RedditApplication.getAppContext(), getSubreddit(), this.mSelectedFlair));
        dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onFlairsLoaded(AbstractFlair[] abstractFlairArr) {
        if (isDialogShowing()) {
            this.mFlairs.add(new NoFlair());
            Collections.addAll(this.mFlairs, abstractFlairArr);
            int length = abstractFlairArr.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                AbstractFlair abstractFlair = abstractFlairArr[i];
                if (StringUtils.equalsIgnoreCase(abstractFlair.id, this.mSelectedFlair.id)) {
                    this.mSelectedFlair = abstractFlair;
                    break;
                }
                i++;
            }
            this.mRecyclerView.getAdapter().notifyDataSetChanged();
            showItems();
        }
    }

    public void showItems() {
        this.mAsyncWrapper.setVisibility(8);
        this.mMainWrapper.setVisibility(0);
        int height = this.mAsyncRootWrapper.getHeight();
        this.mAsyncRootWrapper.measure(0, 0);
        int measuredHeight = this.mAsyncRootWrapper.getMeasuredHeight();
        if (measuredHeight == 0 || height == 0 || measuredHeight <= height) {
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

    public void selectFlair(AbstractFlair abstractFlair) {
        this.mSelectedFlair = abstractFlair;
        this.mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    public void load() {
        loadSubredditInfo();
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public boolean isInitiallyExpanded() {
        return true;
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetInterface
    public int getLayoutRes() {
        return R.layout.abstract_radio_selection_dialog_bottom_sheet;
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        ButterKnife.bind(this, view);
        this.mAsyncWrapper.setVisibility(0);
        this.mMainWrapper.setVisibility(8);
        this.mSectionHeader.applyColors(isNightMode());
        this.mSectionHeader.setTitle("Edit user flair");
        this.mSectionHeader.setMoreVisible(false);
        this.mProgressBar.applyColors(isNightMode());
        this.mMonetActionButtons.setButtonLabels("Select Flair", "Edit text", "Cancel");
        this.mMonetActionButtons.setListeners(new View.OnClickListener() { // from class: y9.n
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                UserPickFlairRadioSelectionDialogBottomSheet.this.onSelectClicked(view2);
            }
        }, new View.OnClickListener() { // from class: y9.m
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                UserPickFlairRadioSelectionDialogBottomSheet.this.onEditTextClicked(view2);
            }
        }, new View.OnClickListener() { // from class: y9.l
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                UserPickFlairRadioSelectionDialogBottomSheet.this.onCancelClicked(view2);
            }
        });
        this.mRecyclerView.setLayoutManager(new CustomLinearLayoutManager(getActivity()));
        this.mRecyclerView.setAdapter(new FlairAdapter());
        this.mButtonsWrapper.measure(0, 0);
        this.mRecyclerView.setPadding(0, 0, 0, this.mButtonsWrapper.getMeasuredHeight());
        this.mRecyclerView.setRecycledViewPool(new FlairRecycledViewPool());
        load();
    }
}
