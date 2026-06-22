package com.resync.app.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.resync.app.R;
import com.resync.app.ui.sections.concat.FooterHelper;
import com.resync.app.ui.views.drawer.DrawerRecyclerView;
import com.resync.app.ui.views.monet.MonetFab;
import com.squareup.otto.Subscribe;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.adapters.DrawerHeaderAdapter;
import com.resync.app.adapters.SubredditSearchConcatAdapter;
import com.resync.app.events.SearchSubredditsEvent;
import com.resync.app.events.SubredditFilterClickedEvent;
import com.resync.app.events.SubredditSelectedEvent;
import com.resync.app.events.SubredditsChangedEvent;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.events.DrawerClosedEvent;
import com.resync.app.ui.interfaces.PostHost;
import com.resync.app.util.Logger;

/* JADX INFO: loaded from: classes2.dex */
public class DrawerFragment extends Fragment {

    @BindView(R.id.fragment_drawer_fab)
    MonetFab mFab;

    @BindView(R.id.fragment_drawer_recyclerview)
    DrawerRecyclerView mRecyclerView;
    private LinearLayoutManager layoutManager;

    /* JADX INFO: renamed from: p0, reason: collision with root package name */
    private SubredditSearchConcatAdapter adapter;
    FooterHelper footerHelper;

    class DrawerLayoutManager extends LinearLayoutManager {
        DrawerLayoutManager(Context context) {
            super(context);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
        public int getPaddingBottom() {
            FooterHelper footerHelper = DrawerFragment.this.footerHelper;
            if (footerHelper != null) {
                return footerHelper.getFooterHeight();
            }
            return 0;
        }
    }

    class FabScrollListener extends RecyclerView.OnScrollListener {
        FabScrollListener() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (((LinearLayoutManager) DrawerFragment.this.mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition() >= 1) {
                DrawerFragment.this.mFab.show();
            } else if (((LinearLayoutManager) DrawerFragment.this.mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition() == 0) {
                DrawerFragment.this.mFab.hide();
            }
        }
    }

    class KeyboardScrollListener extends RecyclerView.OnScrollListener {
        KeyboardScrollListener() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if (newState == 1) {
                DrawerFragment.this.hideKeyboard();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambdaScrollToTop(View view) {
        this.mRecyclerView.scrollToPosition(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambdaSetFooterHeight(View view) {
        this.footerHelper.setVisibleHeight(view.getMeasuredHeight());
    }

    public static DrawerFragment newInstance() {
        return new DrawerFragment();
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View viewInflate = layoutInflater.inflate(R.layout.fragment_drawer, (ViewGroup) null);
        ButterKnife.bind(this, viewInflate);
        return viewInflate;
    }

    @Override // androidx.fragment.app.Fragment
    /* JADX INFO: renamed from: m2 */
    public void onStart() {
        super.onStart();
        BusSingleton.getBus().register(this);
    }

    @Override // androidx.fragment.app.Fragment
    /* JADX INFO: renamed from: n2 */
    public void onStop() {
        BusSingleton.getBus().unregister(this);
        super.onStop();
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        DrawerLayoutManager layoutManager = new DrawerLayoutManager(getContext());
        this.layoutManager = layoutManager;
        this.mRecyclerView.setLayoutManager(layoutManager);
        this.mFab.hide();
        this.mFab.setOnClickListener(new View.OnClickListener() { // from class: ma.e
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                DrawerFragment.this.lambdaScrollToTop(view2);
            }
        });
        this.mRecyclerView.addOnScrollListener(new FabScrollListener());
        this.mRecyclerView.setHasFixedSize(true);
        this.mRecyclerView.setItemAnimator(null);
        this.mRecyclerView.setOverScrollMode(2);
        this.mRecyclerView.addOnScrollListener(new KeyboardScrollListener());
        this.adapter = new SubredditSearchConcatAdapter(this.mRecyclerView, new DrawerHeaderAdapter(), false);
        FooterHelper footerHelper = new FooterHelper(getContext());
        this.footerHelper = footerHelper;
        this.adapter.setFooterHelper(footerHelper);
        final View viewFindViewById = getView().findViewById(R.id.drawer_inner);
        viewFindViewById.measure(0, 0);
        viewFindViewById.post(new Runnable() { // from class: ma.f
            @Override // java.lang.Runnable
            public final void run() {
                DrawerFragment.this.lambdaSetFooterHeight(viewFindViewById);
            }
        });
        this.adapter.onSubredditsChanged(null);
    }

    @Subscribe
    public void onSearchChanged(SearchSubredditsEvent searchSubredditsEvent) {
        if ((getActivity() instanceof PostHost) && ((PostHost) getActivity()).areAnyPanelsOpen()) {
            this.adapter.onSearchSubreddits(searchSubredditsEvent);
        }
    }

    @Subscribe
    public void onSubredditFilterClicked(SubredditFilterClickedEvent subredditFilterClickedEvent) {
        if (!subredditFilterClickedEvent.filtered) {
            this.mRecyclerView.setDescendantFocusability(131072);
            return;
        }
        this.mRecyclerView.setDescendantFocusability(262144);
        this.mRecyclerView.setItemAnimator(null);
        this.mRecyclerView.invalidate();
        ((LinearLayoutManager) this.mRecyclerView.getLayoutManager()).scrollToPositionWithOffset(0, -this.footerHelper.getSearchBoxOffset());
    }

    @Subscribe
    public void onSubredditSelected(SubredditSelectedEvent subredditSelectedEvent) {
        try {
            this.adapter.notifyAll();
        } catch (Exception e) {
            Logger.logException(e, false);
        }
    }

    @Subscribe
    public void onSubredditsChanged(SubredditsChangedEvent subredditsChangedEvent) {
        this.adapter.onSubredditsChanged(subredditsChangedEvent);
    }

    public void hideKeyboard() {
        ((InputMethodManager) getActivity().getSystemService("input_method")).hideSoftInputFromWindow(getView().getRootView().getWindowToken(), 0);
    }

    public void onDrawerClosed() {
        Logger.verbose("DRAWER", "I'm being hidden nooooo");
        hideKeyboard();
        boolean zIsNotEmpty = StringUtils.isNotEmpty(this.adapter.getQuery());
        this.adapter.onSearchSubreddits(null);
        if (zIsNotEmpty) {
            this.mRecyclerView.scrollToPosition(0);
        }
        BusSingleton.getBus().post(new DrawerClosedEvent());
    }
}
