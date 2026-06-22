package com.resync.app.ui.fragment_dialogs.bottom;

import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.ui.views.core.MaterialRow;

/* JADX INFO: loaded from: classes.dex */
public final class PostModBottomSheetFragment_ViewBinding implements Unbinder {
    private PostModBottomSheetFragment target;
    private View modLockClickView;
    private View modSpamClickView;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private View view1;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private View view2;
    private View modDistinguishClickView;
    private View modStickyClickView;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private View view3;
    private View modSpoilerClickView;
    private View modFlairClickView;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private View view4;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private View view5;

    class ClickListener0 extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ PostModBottomSheetFragment target0;

        ClickListener0(PostModBottomSheetFragment postModBottomSheetFragment) {
            this.target0 = postModBottomSheetFragment;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target0.onBanClicked();
        }
    }

    class ClickListener1 extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ PostModBottomSheetFragment target1;

        ClickListener1(PostModBottomSheetFragment postModBottomSheetFragment) {
            this.target1 = postModBottomSheetFragment;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target1.onReportReasonsClicked();
        }
    }

    class ClickListener2 extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ PostModBottomSheetFragment target2;

        ClickListener2(PostModBottomSheetFragment postModBottomSheetFragment) {
            this.target2 = postModBottomSheetFragment;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target2.onLockClicked();
        }
    }

    class ClickListener3 extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ PostModBottomSheetFragment target3;

        ClickListener3(PostModBottomSheetFragment postModBottomSheetFragment) {
            this.target3 = postModBottomSheetFragment;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target3.onSpamClicked();
        }
    }

    class ClickListener4 extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ PostModBottomSheetFragment target4;

        ClickListener4(PostModBottomSheetFragment postModBottomSheetFragment) {
            this.target4 = postModBottomSheetFragment;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target4.onRemoveClicked();
        }
    }

    class ClickListener5 extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ PostModBottomSheetFragment target5;

        ClickListener5(PostModBottomSheetFragment postModBottomSheetFragment) {
            this.target5 = postModBottomSheetFragment;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target5.onApproveClicked();
        }
    }

    class ClickListener6 extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ PostModBottomSheetFragment target6;

        ClickListener6(PostModBottomSheetFragment postModBottomSheetFragment) {
            this.target6 = postModBottomSheetFragment;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target6.onDistinguishClicked();
        }
    }

    class ClickListener7 extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ PostModBottomSheetFragment target7;

        ClickListener7(PostModBottomSheetFragment postModBottomSheetFragment) {
            this.target7 = postModBottomSheetFragment;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target7.onStickyClicked();
        }
    }

    class ClickListener8 extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ PostModBottomSheetFragment target8;

        ClickListener8(PostModBottomSheetFragment postModBottomSheetFragment) {
            this.target8 = postModBottomSheetFragment;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target8.onNsfwClicked();
        }
    }

    class ClickListener9 extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ PostModBottomSheetFragment target9;

        ClickListener9(PostModBottomSheetFragment postModBottomSheetFragment) {
            this.target9 = postModBottomSheetFragment;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target9.onSpoilerClicked();
        }
    }

    class ClickListener10 extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ PostModBottomSheetFragment target10;

        ClickListener10(PostModBottomSheetFragment postModBottomSheetFragment) {
            this.target10 = postModBottomSheetFragment;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target10.onFlairClicked();
        }
    }

    public PostModBottomSheetFragment_ViewBinding(PostModBottomSheetFragment postModBottomSheetFragment, View view) {
        this.target = postModBottomSheetFragment;
        View viewC = Utils.findRequiredView(view, R.id.mod_lock, "field 'mLock' and method 'onLockClicked'");
        postModBottomSheetFragment.mLock = (MaterialRow) Utils.castView(viewC, R.id.mod_lock, "field 'mLock'", MaterialRow.class);
        this.modLockClickView = viewC;
        viewC.setOnClickListener(new ClickListener2(postModBottomSheetFragment));
        View viewC2 = Utils.findRequiredView(view, R.id.mod_spam, "field 'mSpam' and method 'onSpamClicked'");
        postModBottomSheetFragment.mSpam = (MaterialRow) Utils.castView(viewC2, R.id.mod_spam, "field 'mSpam'", MaterialRow.class);
        this.modSpamClickView = viewC2;
        viewC2.setOnClickListener(new ClickListener3(postModBottomSheetFragment));
        View viewC3 = Utils.findRequiredView(view, R.id.mod_remove, "field 'mRemove' and method 'onRemoveClicked'");
        postModBottomSheetFragment.mRemove = (MaterialRow) Utils.castView(viewC3, R.id.mod_remove, "field 'mRemove'", MaterialRow.class);
        this.view1 = viewC3;
        viewC3.setOnClickListener(new ClickListener4(postModBottomSheetFragment));
        View viewC4 = Utils.findRequiredView(view, R.id.mod_approve, "field 'mApprove' and method 'onApproveClicked'");
        postModBottomSheetFragment.mApprove = (MaterialRow) Utils.castView(viewC4, R.id.mod_approve, "field 'mApprove'", MaterialRow.class);
        this.view2 = viewC4;
        viewC4.setOnClickListener(new ClickListener5(postModBottomSheetFragment));
        View viewC5 = Utils.findRequiredView(view, R.id.mod_distinguish, "field 'mDistinguish' and method 'onDistinguishClicked'");
        postModBottomSheetFragment.mDistinguish = (MaterialRow) Utils.castView(viewC5, R.id.mod_distinguish, "field 'mDistinguish'", MaterialRow.class);
        this.modDistinguishClickView = viewC5;
        viewC5.setOnClickListener(new ClickListener6(postModBottomSheetFragment));
        View viewC6 = Utils.findRequiredView(view, R.id.mod_sticky, "field 'mSticky' and method 'onStickyClicked'");
        postModBottomSheetFragment.mSticky = (MaterialRow) Utils.castView(viewC6, R.id.mod_sticky, "field 'mSticky'", MaterialRow.class);
        this.modStickyClickView = viewC6;
        viewC6.setOnClickListener(new ClickListener7(postModBottomSheetFragment));
        View viewC7 = Utils.findRequiredView(view, R.id.mod_nsfw, "field 'mNsfw' and method 'onNsfwClicked'");
        postModBottomSheetFragment.mNsfw = (MaterialRow) Utils.castView(viewC7, R.id.mod_nsfw, "field 'mNsfw'", MaterialRow.class);
        this.view3 = viewC7;
        viewC7.setOnClickListener(new ClickListener8(postModBottomSheetFragment));
        View viewC8 = Utils.findRequiredView(view, R.id.mod_spoiler, "field 'mSpoiler' and method 'onSpoilerClicked'");
        postModBottomSheetFragment.mSpoiler = (MaterialRow) Utils.castView(viewC8, R.id.mod_spoiler, "field 'mSpoiler'", MaterialRow.class);
        this.modSpoilerClickView = viewC8;
        viewC8.setOnClickListener(new ClickListener9(postModBottomSheetFragment));
        View viewC9 = Utils.findRequiredView(view, R.id.mod_flair, "field 'mFlair' and method 'onFlairClicked'");
        postModBottomSheetFragment.mFlair = (MaterialRow) Utils.castView(viewC9, R.id.mod_flair, "field 'mFlair'", MaterialRow.class);
        this.modFlairClickView = viewC9;
        viewC9.setOnClickListener(new ClickListener10(postModBottomSheetFragment));
        View viewC10 = Utils.findRequiredView(view, R.id.mod_ban, "field 'mBan' and method 'onBanClicked'");
        postModBottomSheetFragment.mBan = (MaterialRow) Utils.castView(viewC10, R.id.mod_ban, "field 'mBan'", MaterialRow.class);
        this.view4 = viewC10;
        viewC10.setOnClickListener(new ClickListener0(postModBottomSheetFragment));
        View viewC11 = Utils.findRequiredView(view, R.id.mod_reasons, "field 'mReasons' and method 'onReportReasonsClicked'");
        postModBottomSheetFragment.mReasons = (MaterialRow) Utils.castView(viewC11, R.id.mod_reasons, "field 'mReasons'", MaterialRow.class);
        this.view5 = viewC11;
        viewC11.setOnClickListener(new ClickListener1(postModBottomSheetFragment));
    }

    @Override
    public void unbind() {
        PostModBottomSheetFragment target = this.target;
        if (target == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        target.mLock = null;
        target.mSpam = null;
        target.mRemove = null;
        target.mApprove = null;
        target.mDistinguish = null;
        target.mSticky = null;
        target.mNsfw = null;
        target.mSpoiler = null;
        target.mFlair = null;
        target.mBan = null;
        target.mReasons = null;
        this.modLockClickView.setOnClickListener(null);
        this.modLockClickView = null;
        this.modSpamClickView.setOnClickListener(null);
        this.modSpamClickView = null;
        this.view1.setOnClickListener(null);
        this.view1 = null;
        this.view2.setOnClickListener(null);
        this.view2 = null;
        this.modDistinguishClickView.setOnClickListener(null);
        this.modDistinguishClickView = null;
        this.modStickyClickView.setOnClickListener(null);
        this.modStickyClickView = null;
        this.view3.setOnClickListener(null);
        this.view3 = null;
        this.modSpoilerClickView.setOnClickListener(null);
        this.modSpoilerClickView = null;
        this.modFlairClickView.setOnClickListener(null);
        this.modFlairClickView = null;
        this.view4.setOnClickListener(null);
        this.view4 = null;
        this.view5.setOnClickListener(null);
        this.view5 = null;
    }
}
