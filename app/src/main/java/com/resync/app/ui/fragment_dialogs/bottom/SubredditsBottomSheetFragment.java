package com.resync.app.ui.fragment_dialogs.bottom;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bumptech.glide.MaterialPostsHelper;
import com.resync.app.R;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.activities.CasualActivity;
import com.resync.app.ui.views.monet.MonetFab;
import com.resync.app.ui.views.monet.MonetSearchBoxView;
import com.resync.app.ui.views.recycler.SubredditsRecyclerView;
import com.reddit.indicatorfastscroll.FastScrollItemIndicator;
import com.reddit.indicatorfastscroll.FastScrollerThumbView;
import com.reddit.indicatorfastscroll.FastScrollerView;
import com.squareup.otto.Subscribe;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.ui.SubredditListMenuHelper;
import java.util.List;
import kotlin.jvm.functions.Function1;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.adapters.HeaderDividerSection;
import com.resync.app.adapters.SubredditConcatSection;
import com.resync.app.adapters.SubredditHeaderConcatSection;
import com.resync.app.adapters.SubredditSearchConcatAdapter;
import com.resync.app.events.FullSubSearchEvent;
import com.resync.app.events.SearchSubredditsEvent;
import com.resync.app.events.SubredditSelectedEvent;
import com.resync.app.events.SubredditsChangedEvent;
import com.resync.app.events.VisitedChangedEvent;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.events.SubmitAllSearchEvent;
import com.resync.app.events.SubmitSubredditSearchEvent;
import com.resync.app.dialogs.CustomBottomSheetDialogFragment;
import com.resync.app.helpers.PaddingHelper;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.holders.BasePostViewHolder;
import com.resync.app.util.Logger;
import com.resync.app.util.MathUtil;
import com.resync.app.util.TextUtils;

/* JADX INFO: loaded from: classes.dex */
public class SubredditsBottomSheetFragment extends CustomBottomSheetDialogFragment {
    static Integer sScrollPosition;
    private boolean mFocusOnOpen = false;
    private Boolean mExpanded;
    private SubredditSearchConcatAdapter mAdapter;

    @BindView(R.id.fab)
    MonetFab mFab;

    @BindView(R.id.bottom_sheet_subreddits_recyclerview_scroller)
    FastScrollerView mFastScroller;

    @BindView(R.id.bottom_sheet_subreddits_recyclerview_scroller_thumb)
    FastScrollerThumbView mFastScrollerThumb;

    @BindView(R.id.bottom_sheet_subreddits_recyclerview)
    SubredditsRecyclerView mRecyclerView;

    @BindView(R.id.monet_search_box)
    MonetSearchBoxView searchBox;

    @BindView(R.id.search_wrapper)
    FrameLayout searchWrapper;

    class SearchTextWatcher implements TextWatcher {
        SearchTextWatcher() {
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            BusSingleton.getBus().post(new SearchSubredditsEvent(charSequence.toString()));
            SubredditsBottomSheetFragment.this.refreshFastScroller();
        }
    }

    class FabScrollListener extends RecyclerView.OnScrollListener {
        FabScrollListener() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            SubredditsBottomSheetFragment.this.hideKeyboard();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (((LinearLayoutManager) SubredditsBottomSheetFragment.this.mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition() >= 1) {
                SubredditsBottomSheetFragment.this.mFab.show();
            } else if (((LinearLayoutManager) SubredditsBottomSheetFragment.this.mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition() == 0) {
                SubredditsBottomSheetFragment.this.mFab.hide();
            }
        }
    }

    class FastScrollIndicatorProvider implements Function1<Integer, FastScrollItemIndicator> {
        FastScrollIndicatorProvider() {
        }

        @Override // kotlin.jvm.functions.Function1
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public FastScrollItemIndicator invoke(Integer num) {
            int position;
            AdapterPosition adapterPosition = SubredditsBottomSheetFragment.this.findAdapterForPosition(num.intValue());
            RecyclerView.Adapter adapter = adapterPosition.adapter;
            if (!(adapter instanceof SubredditHeaderConcatSection) && (adapter instanceof SubredditConcatSection)) {
                SubredditConcatSection subredditConcatSection = (SubredditConcatSection) adapter;
                if (subredditConcatSection.getSectionId() != 7 && subredditConcatSection.getSectionId() != 4 && subredditConcatSection.getSectionId() != 5 && subredditConcatSection.getSectionId() != 3 && subredditConcatSection.getSectionId() != 1 && subredditConcatSection.getSectionId() != 6 && (position = adapterPosition.position) >= 0) {
                    String subredditName = subredditConcatSection.getSubredditAt(position);
                    if (subredditName.startsWith("multi_")) {
                        subredditName = subredditName.replace("multi_", "");
                    }
                    String upperCase = subredditName.substring(0, 1).toUpperCase();
                    if (MathUtil.isInteger(upperCase)) {
                        upperCase = "#";
                    }
                    return new FastScrollItemIndicator.Text(upperCase);
                }
            }
            return null;
        }
    }

    class IndicatorSelectedCallback implements FastScrollerView.ItemIndicatorSelectedCallback {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ int scrollMargin;

        IndicatorSelectedCallback(int scrollMargin) {
            this.scrollMargin = scrollMargin;
        }

        @Override // com.reddit.indicatorfastscroll.FastScrollerView.ItemIndicatorSelectedCallback
        public void onItemIndicatorSelected(FastScrollItemIndicator fastScrollItemIndicator, int position, int scrollOffset) {
            Logger.verbose("Scrolling to position: " + scrollOffset);
            Logger.verbose("Margin: " + this.scrollMargin);
            ((LinearLayoutManager) SubredditsBottomSheetFragment.this.mRecyclerView.getLayoutManager()).scrollToPositionWithOffset(scrollOffset, this.scrollMargin);
        }
    }

    class SearchKeyListener implements DialogInterface.OnKeyListener {
        SearchKeyListener() {
        }

        @Override // android.content.DialogInterface.OnKeyListener
        public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
            if (keyCode == 4 && keyEvent.getAction() == 1) {
                if (StringUtils.isNotEmpty(SubredditsBottomSheetFragment.this.searchBox.getText())) {
                    SubredditsBottomSheetFragment.this.searchBox.setText(null);
                    SubredditsBottomSheetFragment.this.searchBox.clearFocus();
                    SubredditsBottomSheetFragment.this.mAdapter.setShowRecent(false);
                    return true;
                }
                if (SubredditsBottomSheetFragment.this.mAdapter.isRecentSectionVisible()) {
                    SubredditsBottomSheetFragment.this.searchBox.clearFocus();
                    SubredditsBottomSheetFragment.this.mAdapter.setShowRecent(false);
                    return true;
                }
            }
            return false;
        }
    }

    class AdapterPosition {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        RecyclerView.Adapter adapter;
        int position;

        public AdapterPosition(RecyclerView.Adapter adapter, int position) {
            this.adapter = adapter;
            this.position = position;
        }
    }

    public static Bundle buildArgs() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("focus_on_open", Boolean.TRUE);
        return bundle;
    }

    private void hideFastScroller() {
        this.mFab.setTranslationX(-SizeHelper.dpToPx(24));
        this.mFastScroller.setVisibility(8);
        PaddingHelper.setPaddingEnd(this.mRecyclerView, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onFabClicked(View view) {
        this.mRecyclerView.scrollToPosition(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onSearchFocusChanged(View view, boolean hasFocus) {
        if (hasFocus) {
            this.searchBox.showKeyboard();
            expand();
        }
        if (hasFocus) {
            this.mAdapter.setShowRecent(hasFocus);
        }
        refreshFastScroller();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void scrollToSaved() {
        this.mRecyclerView.scrollBy(0, sScrollPosition.intValue());
        sScrollPosition = null;
    }

    private boolean isExpandedArg() {
        Boolean bool = this.mExpanded;
        return bool != null && bool.booleanValue();
    }

    private void showFastScroller() {
        this.mFab.setTranslationX(-SizeHelper.dpToPx(56));
        this.mFastScroller.setVisibility(0);
        PaddingHelper.setPaddingEnd(this.mRecyclerView, 32);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshFastScroller() {
        if (getSheetState() == 3 && StringUtils.isEmpty(this.searchBox.getText()) && !this.searchBox.hasFocus() && SettingsSingleton.getSettings().fastScroller) {
            showFastScroller();
        } else {
            hideFastScroller();
        }
    }

    public static Bundle buildArgs(boolean expanded) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("expanded", Boolean.valueOf(expanded));
        return bundle;
    }

    @Override // androidx.fragment.app.Fragment
    /* JADX INFO: renamed from: U1 */
    public void onDestroy() {
        if (SettingsSingleton.getSettings().subredditsRememberPosition) {
            sScrollPosition = Integer.valueOf(this.mRecyclerView.scrollOffset);
            if (!isExpandedArg()) {
                sScrollPosition = null;
            }
        } else {
            sScrollPosition = null;
        }
        super.onDestroy();
    }

    @Override // androidx.fragment.app.c, androidx.fragment.app.Fragment
    public void onDestroyView() {
        for (int i = 0; i < this.mRecyclerView.getChildCount(); i++) {
            RecyclerView.ViewHolder viewHolder = this.mRecyclerView.getChildViewHolder(this.mRecyclerView.getChildAt(i));
            if (viewHolder instanceof BasePostViewHolder) {
                ((BasePostViewHolder) viewHolder).recycle();
            }
        }
        try {
            this.mRecyclerView.setAdapter(null);
        } catch (Exception e) {
            Logger.logException(e);
        }
        super.onDestroyView();
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public boolean requiresArguments() {
        return true;
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public void hideKeyboard() {
        this.searchBox.hideKeyboard();
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public boolean isInitiallyExpanded() {
        return isExpandedArg() || SettingsSingleton.getSettings().subredditsFullyExpand || this.mFocusOnOpen;
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public void loadCustomArguments() {
        this.mFocusOnOpen = getArguments().getBoolean("expanded");
        if (SettingsSingleton.getSettings().subredditsRememberPosition && sScrollPosition != null) {
            this.mFocusOnOpen = true;
        }
        if (getArguments().getBoolean("focus_on_open")) {
            this.mExpanded = Boolean.TRUE;
        }
    }

    @Subscribe
    public void fullSearchSubs(FullSubSearchEvent fullSubSearchEvent) {
        this.searchBox.clearFocus();
        String strTrim = this.searchBox.getText().trim();
        if (TextUtils.isEmpty(strTrim)) {
            return;
        }
        CasualActivity.openSubreddit(getContext(), MaterialPostsHelper.buildSubSearchAddress(strTrim));
        dismiss();
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetInterface
    public int getLayoutRes() {
        return R.layout.subreddits_dialog_bottom_sheet;
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public void onSheetStateChanged(View view, int state) {
        super.onSheetStateChanged(view, state);
        if (state == 1) {
            hideKeyboard();
        } else if (state == 3 && isExpandedArg()) {
            showKeyboard(this.searchBox.getEditText());
            expand();
            this.mExpanded = null;
        }
        refreshFastScroller();
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment, androidx.fragment.app.Fragment
    @SuppressLint({"ClickableViewAccessibility"})
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        ButterKnife.bind(this, view);
        this.mFab.hide();
        this.mFab.setOnClickListener(new View.OnClickListener() { // from class: s9.l
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                SubredditsBottomSheetFragment.this.onFabClicked(view2);
            }
        });
        this.searchBox.measure(-1, -2);
        int measuredHeight = this.searchBox.getMeasuredHeight() + SizeHelper.dpToPx(40);
        this.searchBox.setMoreClickListener(new View.OnClickListener() { // from class: s9.m
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                SubredditListMenuHelper.showOptionsMenu(view2, true);
            }
        });
        this.searchBox.addTextChangedListener(new SearchTextWatcher());
        this.searchBox.setOnFocusChangeListener(new View.OnFocusChangeListener() { // from class: s9.n
            @Override // android.view.View.OnFocusChangeListener
            public final void onFocusChange(View view2, boolean hasFocus) {
                SubredditsBottomSheetFragment.this.onSearchFocusChanged(view2, hasFocus);
            }
        });
        this.mRecyclerView.setHasFixedSize(true);
        this.mRecyclerView.setItemAnimator(null);
        MonetUtil.disableOverScrollPreS(this.mRecyclerView);
        this.mRecyclerView.addOnScrollListener(new FabScrollListener());
        this.mAdapter = new SubredditSearchConcatAdapter(this.mRecyclerView, new HeaderDividerSection(measuredHeight), true);
        onSubredditsChanged(null);
        this.mFastScroller.setHapticFeedbackEnabled(true);
        this.mFastScroller.setTextColor(ColorStateList.valueOf(MonetUtil.getSecondaryTextColorAuto()));
        this.mFastScroller.setIconColor(ColorStateList.valueOf(MonetUtil.getSecondaryTextColorAuto()));
        this.mFastScroller.setupWithRecyclerView(this.mRecyclerView, new FastScrollIndicatorProvider());
        this.mFastScrollerThumb.setupWithFastScroller(this.mFastScroller);
        this.mFastScroller.setUseDefaultScroller(false);
        this.mFastScroller.getItemIndicatorSelectedCallbacks().add(new IndicatorSelectedCallback(measuredHeight));
        getDialog().setOnKeyListener(new SearchKeyListener());
        if (isExpandedArg()) {
            this.searchBox.requestSearchFocus();
            sScrollPosition = null;
        } else {
            if (!SettingsSingleton.getSettings().subredditsRememberPosition || sScrollPosition == null) {
                return;
            }
            this.mRecyclerView.post(new Runnable() { // from class: s9.o
                @Override // java.lang.Runnable
                public final void run() {
                    SubredditsBottomSheetFragment.this.scrollToSaved();
                }
            });
        }
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    protected boolean dismissWithAnimation() {
        return false;
    }

    @Subscribe
    public void onSearchChanged(SearchSubredditsEvent searchSubredditsEvent) {
        this.mAdapter.onSearchSubreddits(searchSubredditsEvent);
    }

    @OnClick
    public void onSearchWrapperClicked() {
        this.searchBox.requestSearchFocus();
    }

    @Subscribe
    public void onSubredditSelected(SubredditSelectedEvent subredditSelectedEvent) {
        dismiss();
    }

    @Subscribe
    public void onSubredditsChanged(SubredditsChangedEvent subredditsChangedEvent) {
        this.mAdapter.onSubredditsChanged(subredditsChangedEvent);
        refreshFastScroller();
    }

    @Subscribe
    public void onVisitedChanged(VisitedChangedEvent visitedChangedEvent) {
        this.mAdapter.onSubredditsChanged(null);
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public boolean isHideable() {
        return false;
    }

    @Subscribe
    public void submitSearch(SubmitSubredditSearchEvent submitSubredditSearchEvent) {
        this.searchBox.clearFocus();
        String strTrim = this.searchBox.getText().trim();
        if (TextUtils.isEmpty(strTrim)) {
            return;
        }
        if (strTrim.startsWith("http://") || strTrim.startsWith("https://")) {
            CasualActivity.openSubredditSearch(getContext(), strTrim, null);
        } else {
            CasualActivity.openSubredditSearch(getContext(), strTrim, submitSubredditSearchEvent.query);
        }
        dismiss();
    }

    @Subscribe
    public void submitSearchAll(SubmitAllSearchEvent submitAllSearchEvent) {
        this.searchBox.clearFocus();
        String strTrim = this.searchBox.getText().trim();
        if (TextUtils.isEmpty(strTrim)) {
            return;
        }
        if (strTrim.startsWith("http://") || strTrim.startsWith("https://")) {
            CasualActivity.openSubredditSearch(getContext(), strTrim, null);
        } else {
            CasualActivity.openSubredditSearch(getContext(), strTrim, null);
        }
        dismiss();
    }

    public AdapterPosition findAdapterForPosition(int position) {
        List<? extends RecyclerView.Adapter<? extends RecyclerView.ViewHolder>> adapters = this.mAdapter.getConcatAdapter().getAdapters();
        Logger.verbose("ADAPTER: " + adapters.size());
        for (int i = 0; i < adapters.size(); i++) {
            int itemCount = adapters.get(i).getItemCount();
            if (position < itemCount) {
                return new AdapterPosition(adapters.get(i), position);
            }
            position -= itemCount;
            Logger.verbose("ADAPTER: " + adapters.get(i));
        }
        return null;
    }
}
