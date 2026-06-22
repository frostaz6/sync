package com.resync.app.ui.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.chip.ChipGroup;
import com.resync.app.R;
import com.resync.app.RedditApplication;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.views.comments.tabbed.CommentsTabbedScrollView;
import java.util.ArrayList;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.managers.SubredditIconManager;
import com.resync.app.util.Logger;
import com.resync.app.model.Post;
import com.resync.app.widgets.TabbedChip;

/* JADX INFO: loaded from: classes2.dex */
public class TabbedCommentsFragment extends RedditFragment {

    @BindView(R.id.chips)
    CommentsTabbedScrollView chipScroller;

    @BindView(R.id.chip_group)
    ChipGroup chipWrapper;

    class IconTarget extends SimpleTarget<Drawable> {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ TabbedChip chip;

        IconTarget(TabbedChip tabbedChip) {
            this.chip = tabbedChip;
        }

        @Override // com.bumptech.glide.request.target.Target
        /* JADX INFO: renamed from: h, reason: merged with bridge method [inline-methods] */
        public void onResourceReady(Drawable drawable, Transition<? super Drawable> transition) {
            this.chip.setChipIcon(drawable);
        }
    }

    private boolean hasTabForPost(Post post) {
        return getFragmentManager().findFragmentByTag(post.getId()) != null;
    }

    private void hideAllTabs() {
        Logger.verbose("TabbedCommentsFragment", "Hiding all: " + this.chipWrapper.getChildCount());
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransactionM = fragmentManager.beginTransaction();
        for (int i = 0; i < this.chipWrapper.getChildCount(); i++) {
            TabbedChip tabbedChip = (TabbedChip) this.chipWrapper.getChildAt(i);
            tabbedChip.setSelected(false);
            CommentsFragment commentsFragment = (CommentsFragment) fragmentManager.findFragmentByTag((String) tabbedChip.getTag());
            Logger.verbose("TabbedCommentsFragment", "Hiding: " + commentsFragment);
            fragmentTransactionM = fragmentTransactionM.hide(commentsFragment);
        }
        fragmentTransactionM.commit();
    }

    private boolean hasSelectedTab() {
        for (int i = 0; i < this.chipWrapper.getChildCount(); i++) {
            if (((TabbedChip) this.chipWrapper.getChildAt(i)).isSelected()) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onTabClicked(Post post, TabbedChip tabbedChip, View view) {
        hideAllTabs();
        showCommentsFragment(post.getId());
        tabbedChip.setSelected(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onTabReselected(Post post, View view) {
        setSelectedPost(post);
        if (hasSelectedTab()) {
            return;
        }
        selectFirstTab();
    }

    private void removeAllTabs() {
        Logger.verbose("TabbedCommentsFragment", "Removing all: " + this.chipWrapper.getChildCount());
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransactionM = fragmentManager.beginTransaction();
        for (int i = 0; i < this.chipWrapper.getChildCount(); i++) {
            TabbedChip tabbedChip = (TabbedChip) this.chipWrapper.getChildAt(i);
            tabbedChip.setSelected(false);
            CommentsFragment commentsFragment = (CommentsFragment) fragmentManager.findFragmentByTag((String) tabbedChip.getTag());
            Logger.verbose("TabbedCommentsFragment", "Hiding: " + commentsFragment);
            fragmentTransactionM = fragmentTransactionM.remove(commentsFragment);
        }
        fragmentTransactionM.commitAllowingStateLoss();
        this.chipWrapper.removeAllViews();
    }

    private void removeCommentsFragment(String str) {
        FragmentManager fragmentManager = getFragmentManager();
        CommentsFragment commentsFragment = (CommentsFragment) fragmentManager.findFragmentByTag(str);
        Logger.verbose("TabbedCommentsFragment", "Removing comments fragment: " + commentsFragment);
        fragmentManager.beginTransaction().remove(commentsFragment).commit();
    }

    private void restoreTabs() {
        ArrayList<Fragment> arrayList = new ArrayList(getFragmentManager().getFragments());
        if (arrayList.size() <= 0) {
            Logger.verbose("TabbedCommentsFragment", "No fragment chips restored");
            return;
        }
        for (Fragment fragment : arrayList) {
            if (fragment instanceof CommentsFragment) {
                CommentsFragment commentsFragment = (CommentsFragment) fragment;
                Logger.verbose("TabbedCommentsFragment", "Restoring comments fragment: " + commentsFragment);
                addTab(commentsFragment.presenter.getPost(), fragment.isHidden() ^ true);
            }
        }
    }

    private void selectTabForPost(Post post) {
        for (int i = 0; i < this.chipWrapper.getChildCount(); i++) {
            TabbedChip tabbedChip = (TabbedChip) this.chipWrapper.getChildAt(i);
            String str = (String) tabbedChip.getTag();
            if (StringUtils.equals(post.getId(), str)) {
                tabbedChip.setSelected(true);
                showCommentsFragment(str);
            }
        }
    }

    private void showCommentsFragment(String str) {
        FragmentManager fragmentManager = getFragmentManager();
        CommentsFragment commentsFragment = (CommentsFragment) fragmentManager.findFragmentByTag(str);
        Logger.verbose("TabbedCommentsFragment", "Showing comments fragment: " + commentsFragment);
        fragmentManager.beginTransaction().show(commentsFragment).commit();
    }

    private void updateScrollerVisibility() {
        if (this.chipWrapper.getChildCount() <= 0 || !SettingsSingleton.getSettings().tabletTabs) {
            this.chipScroller.setVisibility(8);
        } else {
            this.chipScroller.setVisibility(0);
        }
    }

    private void addCommentsFragment(Post post) {
        CommentsFragment commentsFragmentH3 = CommentsFragment.newInstanceFromPost(post, SettingsSingleton.getSettings().commentSort, false);
        Logger.verbose("TabbedCommentsFragment", "Adding comments fragment: " + commentsFragmentH3);
        getFragmentManager().beginTransaction().add(R.id.tabbed_comments_content, commentsFragmentH3, post.getId()).commit();
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    private void addTab(final Post post, boolean selected) {
        final TabbedChip tabbedChip = new TabbedChip(getContext());
        tabbedChip.setSelected(selected);
        tabbedChip.setChecked(true);
        tabbedChip.setCloseIconResource(R.drawable.outline_close_24);
        tabbedChip.setTag(post.getId());
        String strSubstring = StringUtils.substring(post.getTitle(), 0, Math.min(12, post.getTitle().length()));
        if (strSubstring.length() < post.getTitle().length()) {
            strSubstring = strSubstring + "…";
        }
        tabbedChip.setText(strSubstring);
        tabbedChip.setOnClickListener(new View.OnClickListener() { // from class: ma.p
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                TabbedCommentsFragment.this.onTabClicked(post, tabbedChip, view);
            }
        });
        tabbedChip.setOnClickListener(new View.OnClickListener() { // from class: ma.o
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                TabbedCommentsFragment.this.onTabReselected(post, view);
            }
        });
        String iconUrl = SubredditIconManager.getInstance().getIcon(post.getSubreddit());
        if (StringUtils.isNotEmpty(iconUrl)) {
            com.bumptech.glide.Glide.with(RedditApplication.getAppContext()).load(iconUrl).circleCrop().into(new IconTarget(tabbedChip));
        }
        this.chipWrapper.addView(tabbedChip, 0);
        updateScrollerVisibility();
    }

    public void setSelectedPost(Post post) {
        int i = 0;
        while (true) {
            if (i >= this.chipWrapper.getChildCount()) {
                break;
            }
            TabbedChip tabbedChip = (TabbedChip) this.chipWrapper.getChildAt(i);
            String str = (String) tabbedChip.getTag();
            if (StringUtils.equals(post.getId(), str)) {
                removeCommentsFragment(str);
                this.chipWrapper.removeView(tabbedChip);
                break;
            }
            i++;
        }
        updateScrollerVisibility();
    }

    public void selectFirstTab() {
        if (this.chipWrapper.getChildCount() > 0) {
            TabbedChip tabbedChip = (TabbedChip) this.chipWrapper.getChildAt(0);
            tabbedChip.setSelected(true);
            showCommentsFragment((String) tabbedChip.getTag());
        }
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View viewInflate = layoutInflater.inflate(getLayoutResId(), viewGroup, false);
        ButterKnife.bind(this, viewInflate);
        return viewInflate;
    }

    @Override // com.resync.app.ui.interfaces.RedditFragmentInterface
    public int getLayoutResId() {
        return R.layout.fragment_tabbed_comments;
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        if (bundle != null) {
            restoreTabs();
        }
    }

    public void addOrSelectTab(Post post) {
        if (!SettingsSingleton.getSettings().tabletTabs) {
            removeAllTabs();
        }
        if (hasTabForPost(post)) {
            hideAllTabs();
            selectTabForPost(post);
        } else {
            hideAllTabs();
            addTab(post, true);
            addCommentsFragment(post);
        }
    }
}
