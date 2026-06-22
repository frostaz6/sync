package com.resync.app.ui.fragments.posts;

import android.database.Cursor;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.resync.app.R;
import com.resync.app.ui.activities.BaseActivity;
import com.resync.app.ui.activities.CasualGalleryActivity;
import com.resync.app.ui.fragments.posts.pager.AbstractPagerFragment;
import com.resync.app.ui.fragments.posts.pager.PagerImageFragment;
import com.resync.app.ui.views.hacky.HackyViewPager;
import com.squareup.otto.Subscribe;
import com.resync.app.helpers.ui.DepthPageTransformer;
import com.resync.app.fragments.AdFragment;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.premium.SyncIapHelper;
import com.resync.app.widgets.PagerFactory;
import com.resync.app.events.WindowChromeChangedEvent;
import com.resync.app.events.WindowChromeToggleEvent;
import com.resync.app.ui.interfaces.LockInterface;
import com.resync.app.util.Logger;
import com.resync.app.model.Post;
import com.resync.app.model.Subreddit;

/* JADX INFO: renamed from: com.resync.app.ui.fragments.posts.a */
/* JADX INFO: compiled from: HorizontalPostsFragment.java */
/* JADX INFO: loaded from: classes2.dex */
public class HorizontalPostsFragment extends BasePostsFragment {
    public static final String TAG = HorizontalPostsFragment.class.getSimpleName();
    static int AD_INTERVAL = 8;

    /* JADX INFO: renamed from: v0, reason: collision with root package name */
    HackyViewPager viewPager;
    private PostsPagerAdapter adapter;

    /* JADX INFO: renamed from: x0, reason: collision with root package name */
    boolean chromeVisible = true;

    /* JADX INFO: renamed from: com.resync.app.ui.fragments.posts.a$a */
    /* JADX INFO: compiled from: HorizontalPostsFragment.java */
    class PageChangeListener implements ViewPager.OnPageChangeListener {
        private int previousPosition = -1;
        private int currentPosition = 0;

        PageChangeListener() {
        }

        @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
        public void onPageScrollStateChanged(int state) {
            Fragment previousFragment;
            if (state == 1 && HorizontalPostsFragment.this.adapter.getCount() > 0) {
                int current = HorizontalPostsFragment.this.viewPager.getCurrentItem();
                int previous = current - 1;
                int next = current + 1;
                if (previous >= 0) {
                    HorizontalPostsFragment.this.releasePager(previous);
                }
                HorizontalPostsFragment.this.releasePager(current);
                if (next < HorizontalPostsFragment.this.adapter.getCount()) {
                    HorizontalPostsFragment.this.releasePager(next);
                }
            }
            if (state == 0) {
                int currentPos = this.currentPosition;
                if (currentPos >= 0 && currentPos < HorizontalPostsFragment.this.adapter.getCount()) {
                    Fragment currentFragment = HorizontalPostsFragment.this.adapter.getRegisteredFragment(this.currentPosition);
                    if (currentFragment != null && (currentFragment instanceof AdFragment)) {
                        ((AdFragment) currentFragment).onAdShown();
                    } else if (currentFragment != null && (currentFragment instanceof PagerImageFragment)) {
                        ((PagerImageFragment) currentFragment).autoPlay(true);
                    }
                }
                int previousPos = this.previousPosition;
                if (previousPos < 0 || previousPos >= HorizontalPostsFragment.this.adapter.getCount() || (previousFragment = HorizontalPostsFragment.this.adapter.getRegisteredFragment(this.previousPosition)) == null || !(previousFragment instanceof AdFragment)) {
                    return;
                }
                ((AdFragment) previousFragment).onAdHidden();
            }
        }

        @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
        public void onPageSelected(int position) {
            this.previousPosition = this.currentPosition;
            this.currentPosition = position;
            if (position >= HorizontalPostsFragment.this.adapter.getCount() - 3) {
                HorizontalPostsFragment.this.presenter.onInfiniteScroll();
            }
            if (HorizontalPostsFragment.this.getActivity() instanceof LockInterface) {
                ((LockInterface) HorizontalPostsFragment.this.getActivity()).setLocked(position != 0);
            }
            Fragment selectedFragment = HorizontalPostsFragment.this.adapter.getRegisteredFragment(position);
            if (selectedFragment instanceof PagerImageFragment) {
                ((PagerImageFragment) selectedFragment).onPageSelected();
            }
        }
    }

    /* JADX INFO: renamed from: com.resync.app.ui.fragments.posts.a$b */
    /* JADX INFO: compiled from: HorizontalPostsFragment.java */
    class PostsPagerAdapter extends FragmentStatePagerAdapter {
        private SparseArray<Fragment> registeredFragments;

        public PostsPagerAdapter() {
            super(HorizontalPostsFragment.this.getActivity().getSupportFragmentManager());
            this.registeredFragments = new SparseArray<Fragment>();
        }

        private void clearRegisteredFragments() {
            Logger.verbose(HorizontalPostsFragment.TAG, "Clearing registered fragments!");
            this.registeredFragments.clear();
        }

        @Override // androidx.fragment.app.FragmentStatePagerAdapter, androidx.viewpager.widget.PagerAdapter
        public void destroyItem(ViewGroup viewGroup, int position, Object obj) {
            this.registeredFragments.remove(position);
            super.destroyItem(viewGroup, position, obj);
        }

        @Override // androidx.viewpager.widget.PagerAdapter
        public int getCount() {
            return HorizontalPostsFragment.this.getPagerCount();
        }

        @Override // androidx.fragment.app.FragmentStatePagerAdapter, androidx.viewpager.widget.PagerAdapter
        public Object instantiateItem(ViewGroup viewGroup, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(viewGroup, position);
            this.registeredFragments.put(position, fragment);
            return fragment;
        }

        @Override // androidx.viewpager.widget.PagerAdapter
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            if (getCount() == 0) {
                clearRegisteredFragments();
            }
        }

        @Override // androidx.fragment.app.FragmentStatePagerAdapter
        public Fragment getItem(int position) {
            if (HorizontalPostsFragment.this.isAdPosition(position)) {
                return AdFragment.newInstance();
            }
            AbstractPagerFragment abstractPagerFragment = (AbstractPagerFragment) PagerFactory.create(HorizontalPostsFragment.this.getPresenter(), HorizontalPostsFragment.this.getPostAt(position), HorizontalPostsFragment.this.isChromeVisible());
            abstractPagerFragment.setPresenter(HorizontalPostsFragment.this.getPresenter());
            return abstractPagerFragment;
        }

        public Fragment getRegisteredFragment(int position) {
            return this.registeredFragments.get(position);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void releasePager(int position) {
        Fragment fragment = this.adapter.getRegisteredFragment(position);
        if (fragment == null || !(fragment instanceof AbstractPagerFragment)) {
            return;
        }
        ((AbstractPagerFragment) fragment).releaseMedia();
    }

    private void bindPager(int position) {
        Fragment fragment = this.adapter.getRegisteredFragment(position);
        if (fragment == null || !(fragment instanceof AbstractPagerFragment)) {
            return;
        }
        ((AbstractPagerFragment) fragment).bindPost(getPostAt(position));
    }

    @Override // com.resync.app.ui.fragments.posts.BasePostsFragment
    public void setupViews() {
        this.viewPager.setOverScrollMode(2);
        PostsPagerAdapter adapter = new PostsPagerAdapter();
        this.adapter = adapter;
        this.viewPager.setAdapter(adapter);
        this.viewPager.setCurrentItem(1);
        this.viewPager.setPageTransformer(true, new DepthPageTransformer());
        this.viewPager.addOnPageChangeListener(new PageChangeListener());
    }

    @Override // com.resync.app.ui.fragments.posts.BasePostsFragment
    protected void bindCursor(Cursor cursor) {
        super.bindCursor(cursor);
        this.adapter.notifyDataSetChanged();
        if (this.adapter.getCount() > 0) {
            int current = this.viewPager.getCurrentItem();
            int previous = current - 1;
            int next = current + 1;
            if (previous >= 0) {
                bindPager(previous);
            }
            bindPager(current);
            if (next < this.adapter.getCount()) {
                bindPager(next);
            }
        }
        Bundle bundle = this.savedState;
        if (bundle == null || !bundle.containsKey("HackyViewPager_Pos")) {
            return;
        }
        int savedPosition = this.savedState.getInt("HackyViewPager_Pos", 0);
        this.savedState.remove("HackyViewPager_Pos");
        this.viewPager.setCurrentItem(savedPosition, true);
    }

    boolean areAdsEnabled() {
        return SyncIapHelper.areAdsEnabled(getActivity()) && (getActivity() instanceof CasualGalleryActivity);
    }

    int getPagerCount() {
        if (getCursor() == null) {
            return 0;
        }
        return getCursor().getCount() + getAdCount(getCursor().getCount());
    }

    int getAdCount(int count) {
        if (areAdsEnabled()) {
            return count / AD_INTERVAL;
        }
        return 0;
    }

    Post getPostAt(int position) {
        return Post.fromCursorPosition(getCursor(), position - getAdCount(position));
    }

    public boolean isChromeVisible() {
        return this.chromeVisible;
    }

    boolean isAdPosition(int position) {
        return areAdsEnabled() && position != 0 && position % AD_INTERVAL == 0;
    }

    @Override // com.resync.app.presenters.PostsView
    public void setEnterExitAnimation() {
    }

    @Override // com.resync.app.presenters.PostsView
    public void setNoAnimation() {
    }

    @Override // com.resync.app.ui.fragments.posts.BasePostsFragment, com.resync.app.ui.interfaces.RedditFragmentInterface
    public int getLayoutResId() {
        return R.layout.fragment_posts_horizontal;
    }

    @Override // com.resync.app.ui.fragments.posts.BasePostsFragment, androidx.fragment.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putInt("HackyViewPager_Pos", this.viewPager.getCurrentItem());
        super.onSaveInstanceState(bundle);
    }

    @Override // com.resync.app.presenters.PostsView
    public void setDefaultAnimation() {
    }

    @Override // com.resync.app.presenters.PostsView
    public void setRefreshing(boolean refreshing) {
    }

    @Override // com.resync.app.ui.fragments.posts.BasePostsFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        if (areAdsEnabled()) {
            return;
        }
        ((BaseActivity) getContext()).showAd();
    }

    @Subscribe
    public void onChromeToggle(WindowChromeToggleEvent windowChromeToggleEvent) {
        boolean visible = !isChromeVisible();
        BusSingleton.getBus().post(new WindowChromeChangedEvent(visible));
        this.chromeVisible = visible;
    }

    @Override // com.resync.app.presenters.PostsView
    public void setSubreddit(Subreddit subreddit) {
    }

    @Override // com.resync.app.presenters.PostsView
    public void setSwipeEnabled(boolean enabled) {
    }
}
