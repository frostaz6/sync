package com.resync.app.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.resync.app.dialogs.OnboardingSkipAlertDialogBottomSheet;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.resync.app.R;
import com.resync.app.ui.viewholders.onboarding.AbstractOnboardingViewHolder;
import com.resync.app.ui.viewholders.onboarding.pages.FinallyViewHolder;
import com.resync.app.ui.viewholders.onboarding.pages.HeaderModeViewHolder;
import com.resync.app.ui.viewholders.onboarding.pages.LandingViewHolder;
import com.resync.app.ui.viewholders.onboarding.pages.NavigationModeViewHolder;
import com.resync.app.ui.viewholders.onboarding.pages.SettingsViewHolder;
import com.resync.app.ui.viewholders.onboarding.pages.TabletViewHolder;
import com.resync.app.ui.viewholders.onboarding.pages.TermsViewHolder;
import com.resync.app.ui.viewholders.onboarding.pages.ViewTypeViewHolder;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.helpers.ui.TabletHelper;
import java.util.ArrayList;
import java.util.List;
import com.resync.app.events.NotificationPermissionEvent;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.util.Logger;
import com.resync.app.fragments.preferences.BasePreferencesFragment;

/* JADX INFO: loaded from: classes.dex */
public class OnboardingActivity extends BaseMaterialActivity {

    @BindView(R.id.back)
    TextView back;

    ArrayList<Class<? extends AbstractOnboardingViewHolder>> pages = new ArrayList<>();

    @BindView(R.id.next)
    TextView next;

    @BindView(R.id.view_pager)
    ViewPager2 pager;

    class PageChangeCallback extends ViewPager2.OnPageChangeCallback {
        PageChangeCallback() {
        }

        @Override // androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }

        @Override // androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
        public void onPageSelected(int position) {
            if (position == 1) {
                OnboardingActivity.this.next.setText("Accept");
            } else if (position < OnboardingActivity.this.pager.getAdapter().getItemCount() - 1) {
                OnboardingActivity.this.next.setText("Next");
            } else {
                OnboardingActivity.this.next.setText("Finish");
            }
            if (position == 0) {
                OnboardingActivity.this.back.setVisibility(8);
                OnboardingActivity.this.back.setText("Skip");
            } else {
                OnboardingActivity.this.back.setVisibility(0);
                OnboardingActivity.this.back.setText("Back");
            }
        }
    }

    class FinishRunnable implements Runnable {
        FinishRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            OnboardingActivity.this.startActivity(new Intent(OnboardingActivity.this, (Class<?>) MainActivity.class));
            OnboardingActivity.this.finish();
        }
    }

    class PagesAdapter extends RecyclerView.Adapter<AbstractOnboardingViewHolder> {
        PagesAdapter() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /* JADX INFO: renamed from: N, reason: merged with bridge method [inline-methods] */
        public void onBindViewHolder(AbstractOnboardingViewHolder abstractOnboardingViewHolder, int position) {
            try {
                abstractOnboardingViewHolder.getClass().getMethod("bind", new Class[0]).invoke(abstractOnboardingViewHolder, new Object[0]);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /* JADX INFO: renamed from: O, reason: merged with bridge method [inline-methods] */
        public void onBindViewHolder(AbstractOnboardingViewHolder abstractOnboardingViewHolder, int position, List<Object> list) {
            super.onBindViewHolder(abstractOnboardingViewHolder, position, list);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /* JADX INFO: renamed from: P, reason: merged with bridge method [inline-methods] */
        public AbstractOnboardingViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            try {
                return OnboardingActivity.this.pages.get(viewType).getConstructor(ViewGroup.class).newInstance(viewGroup);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /* JADX INFO: renamed from: Q, reason: merged with bridge method [inline-methods] */
        public void onViewRecycled(AbstractOnboardingViewHolder abstractOnboardingViewHolder) {
            super.onViewRecycled(abstractOnboardingViewHolder);
            Logger.verbose("RECYCLED");
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /* JADX INFO: renamed from: n */
        public int getItemCount() {
            return OnboardingActivity.this.pages.size();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int position) {
            return position;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onBackClicked(View view) {
        if (this.pager.getCurrentItem() == 0) {
            FragmentUtils.show(OnboardingSkipAlertDialogBottomSheet.class, getSupportFragmentManager());
        } else {
            this.pager.setCurrentItem(this.pager.getCurrentItem() - 1);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onNextClicked(View view) {
        if (this.pager.getCurrentItem() == this.pager.getAdapter().getItemCount() - 1) {
            finishAfterDelay(0);
        } else {
            ViewPager2 viewPager2 = this.pager;
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    }

    @Override // com.resync.app.ui.activities.BaseMaterialActivity
    protected int getNavigationBarColor(boolean night) {
        return MonetUtil.getWindowBackgroundColor(night);
    }

    @Override // com.resync.app.ui.activities.BaseMaterialActivity
    protected void rebuildTheme() {
        super.rebuildTheme();
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof BasePreferencesFragment) {
                ((BasePreferencesFragment) fragment).onDarkModeChanged();
            }
        }
    }

    public void finishAfterDelay(int delayMs) {
        getWindow().getDecorView().postDelayed(new FinishRunnable(), delayMs);
    }

    public void requestNotificationPermission() {
        requestPermissions(new String[]{"android.permission.POST_NOTIFICATIONS"}, 10);
    }

    @Override // com.resync.app.ui.activities.BaseMaterialActivity, com.resync.app.ui.activities.BaseActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ButterKnife.bind(this);
        this.pages.add(LandingViewHolder.class);
        this.pages.add(TermsViewHolder.class);
        // ReSync: removed the in-app-purchase restore onboarding page.
        this.pages.add(HeaderModeViewHolder.class);
        if (!TabletHelper.isTabletResource()) {
            this.pages.add(NavigationModeViewHolder.class);
        }
        this.pages.add(ViewTypeViewHolder.class);
        this.pages.add(SettingsViewHolder.class);
        if (TabletHelper.isTabletResource()) {
            this.pages.add(TabletViewHolder.class);
        }
        this.pages.add(FinallyViewHolder.class);
        this.pager.setAdapter(new PagesAdapter());
        this.pager.setOffscreenPageLimit(this.pages.size());
        this.pager.registerOnPageChangeCallback(new PageChangeCallback());
        this.next.setOnClickListener(new View.OnClickListener() { // from class: m9.m
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                OnboardingActivity.this.onNextClicked(view);
            }
        });
        this.back.setOnClickListener(new View.OnClickListener() { // from class: m9.n
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                OnboardingActivity.this.onBackClicked(view);
            }
        });
    }

    @Override // com.resync.app.ui.activities.BaseActivity, android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyUp(int keyCode, KeyEvent keyEvent) {
        if (keyEvent.isCanceled()) {
            return super.onKeyUp(keyCode, keyEvent);
        }
        if (keyCode != 4) {
            try {
                return super.onKeyUp(keyCode, keyEvent);
            } catch (Exception unused) {
                return true;
            }
        }
        if (this.pager.getCurrentItem() > 0) {
            ViewPager2 viewPager2 = this.pager;
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() - 1);
        } else {
            FragmentUtils.show(OnboardingSkipAlertDialogBottomSheet.class, getSupportFragmentManager());
        }
        return true;
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onRequestPermissionsResult(int requestCode, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(requestCode, strArr, iArr);
        if (strArr.length <= 0 || requestCode != 10) {
            return;
        }
        if (iArr[0] == 0) {
            StaticToast.showWithContext(getActivity(), "Permission granted!");
        } else {
            StaticToast.showWithContext(getActivity(), "Permission not granted!");
        }
        BusSingleton.getBus().post(new NotificationPermissionEvent());
    }

    @Override // com.resync.app.ui.activities.BaseActivity
    protected void setupContentView() {
        setContentView(R.layout.activity_onboarding);
    }
}
