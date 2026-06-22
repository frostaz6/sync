package com.resync.app.sections.posts.toolbar.chip_groups;

import android.view.View;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.util.DebouncingOnClickListener;

/* JADX INFO: loaded from: classes.dex */
public class MultiSubredditChipGroup_ViewBinding extends AbstractChipGroup_ViewBinding {
    private MultiSubredditChipGroup target;

    private View viewChipManage;

    class OnManageClickListener extends DebouncingOnClickListener {

        final /* synthetic */ MultiSubredditChipGroup target;

        OnManageClickListener(MultiSubredditChipGroup multiSubredditChipGroup) {
            this.target = multiSubredditChipGroup;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onManageClicked();
        }
    }

    public MultiSubredditChipGroup_ViewBinding(MultiSubredditChipGroup multiSubredditChipGroup, View view) {
        super(multiSubredditChipGroup, view);
        this.target = multiSubredditChipGroup;
        View viewC = Utils.findRequiredView(view, R.id.chip_manage, "method 'onManageClicked'");
        this.viewChipManage = viewC;
        viewC.setOnClickListener(new OnManageClickListener(multiSubredditChipGroup));
    }

    @Override
    public void unbind() {
        MultiSubredditChipGroup multiSubredditChipGroup = this.target;
        if (multiSubredditChipGroup == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        if (this.viewChipManage != null) {
            this.viewChipManage.setOnClickListener(null);
            this.viewChipManage = null;
        }
        super.unbind();
    }
}
