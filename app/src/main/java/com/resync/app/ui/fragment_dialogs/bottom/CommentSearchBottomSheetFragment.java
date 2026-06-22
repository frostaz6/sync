package com.resync.app.ui.fragment_dialogs.bottom;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.android.volley.VolleyError;
import com.resync.app.R;
import com.resync.app.ui.views.monet.MonetSearchBoxView;
import com.squareup.otto.Subscribe;
import com.resync.app.events.SearchCommentSelected;
import com.resync.app.adapters.CommentAdapter;
import com.resync.app.holders.CommentsSearhHeaderSection;
import com.resync.app.dialogs.CustomBottomSheetDialogFragment;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.model.Post;
import com.resync.app.presenters.CommentsPresenter;
import com.resync.app.presenters.CommentsPresenterImpl;
import com.resync.app.presenters.CommentsView;

/* JADX INFO: loaded from: classes.dex */
public class CommentSearchBottomSheetFragment extends CustomBottomSheetDialogFragment implements CommentsView {
    ConcatAdapter mConcatAdapter;
    CommentAdapter mAdapter;
    protected CommentsPresenter mPresenter;

    @BindView(R.id.bottom_sheet_comment_search_recycler)
    RecyclerView mRecycler;

    @BindView(R.id.monet_search_box)
    MonetSearchBoxView searchBoxView;

    class ScrollListener extends RecyclerView.OnScrollListener {
        ScrollListener() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            CommentSearchBottomSheetFragment.this.hideKeyboard();
        }
    }

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
            CommentSearchBottomSheetFragment.this.mPresenter.filterByUser(charSequence.toString());
        }
    }

    public static Bundle buildArgs(Post post) {
        return CommentsPresenterImpl.buildSearchArgs(post);
    }

    @Override // com.resync.app.presenters.CommentsView
    public void setSortLabel(String str, String str2) {
    }

    @Override // com.resync.app.presenters.CommentsView
    public void setMoreCommentsAnimation() {
    }

    @Override // com.resync.app.presenters.CommentsView
    public void setCollapseAnimation() {
    }

    @Override // com.resync.app.presenters.CommentsView
    public void showNoComments() {
    }

    @Override // com.resync.app.presenters.CommentsView
    public void setSubredditTitle(String str) {
    }

    @Override // com.resync.app.presenters.CommentsView
    public void showSingleThreadNotification() {
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public boolean requiresArguments() {
        return true;
    }

    @Override // com.resync.app.presenters.CommentsView
    public void setEnterExitAnimation() {
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public void hideKeyboard() {
        super.hideKeyboard();
    }

    @Override // com.resync.app.presenters.CommentsView
    public void saveCommentState(Parcelable parcelable) {
    }

    @Override // com.resync.app.presenters.CommentsView
    public void onCommentCollapsed(Post post) {
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public boolean isInitiallyExpanded() {
        return true;
    }

    @Override // com.resync.app.presenters.CommentsView
    public void setNoAnimation() {
    }

    @Override // com.resync.app.presenters.CommentsView
    public void setCursor(Cursor cursor) {
        this.mAdapter.setCursor(cursor);
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public void loadCustomArguments() {
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetInterface
    public int getLayoutRes() {
        return R.layout.comment_search_bottom_sheet;
    }

    @Override // com.resync.app.presenters.CommentsView
    public void showOffline() {
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public void onSheetStateChanged(View view, int state) {
        super.onSheetStateChanged(view, state);
        if (state == 1) {
            hideKeyboard();
        } else if (state == 3) {
            this.searchBoxView.showKeyboard();
        }
    }

    @Override // com.resync.app.presenters.CommentsView
    public void setRefreshing(boolean refreshing) {
    }

    @Override // com.resync.app.presenters.CommentsView
    public void scrollToPosition(int position) {
    }

    @Override // com.resync.app.presenters.BaseViewInterface
    public void showError(VolleyError volleyError) {
    }

    @Override // com.resync.app.presenters.CommentsView
    public void setDefaultAnimation() {
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        ButterKnife.bind(this, view);
        setPresenter(new CommentsPresenterImpl(getActivity(), bundle, this));
        this.searchBoxView.measure(-1, -2);
        int measuredHeight = this.searchBoxView.getMeasuredHeight() + SizeHelper.dpToPx(40);
        this.mAdapter = new CommentAdapter(getActivity(), getPresenter());
        ConcatAdapter concatAdapter = new ConcatAdapter(new ConcatAdapter.Config.Builder().setStableIdMode(ConcatAdapter.Config.StableIdMode.ISOLATED_STABLE_IDS).setIsolateViewTypes(true).build(), (RecyclerView.Adapter<? extends RecyclerView.ViewHolder>[]) new RecyclerView.Adapter[]{new CommentsSearhHeaderSection(measuredHeight), this.mAdapter});
        this.mConcatAdapter = concatAdapter;
        this.mRecycler.setAdapter(concatAdapter);
        this.mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        this.mRecycler.setItemViewCacheSize(20);
        this.mRecycler.setOnScrollListener(new ScrollListener());
        getPresenter().load(true);
        this.searchBoxView.addTextChangedListener(new SearchTextWatcher());
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    protected boolean dismissWithAnimation() {
        return false;
    }

    @Subscribe
    public void onSearchCommentSelected(SearchCommentSelected searchCommentSelected) {
        dismiss();
    }

    @Override // com.resync.app.presenters.CommentsView
    public void notifyDataSetChanged() {
        this.mAdapter.notifyDataSetChanged();
    }

    public CommentsPresenter getPresenter() {
        return this.mPresenter;
    }

    public void setPresenter(CommentsPresenter commentsPresenter) {
        this.mPresenter = commentsPresenter;
        commentsPresenter.restoreArguments(getArguments());
    }

    @Override // com.resync.app.presenters.CommentsView
    public void setExpandAnimation() {
    }

    @Override // com.resync.app.presenters.CommentsView
    public void setActionsAnimation() {
    }
}
