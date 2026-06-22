package com.resync.app.sections.posts.toolbar.chip_groups;

import android.view.View;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.util.DebouncingOnClickListener;

/* JADX INFO: loaded from: classes.dex */
public class DomainChipGroup_ViewBinding extends AbstractChipGroup_ViewBinding {
    private DomainChipGroup target;

    private View viewChipDomainOptions;

    class OnDomainOptionsClickListener extends DebouncingOnClickListener {

        final /* synthetic */ DomainChipGroup target;

        OnDomainOptionsClickListener(DomainChipGroup domainChipGroup) {
            this.target = domainChipGroup;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onDomainOptionsClicked();
        }
    }

    public DomainChipGroup_ViewBinding(DomainChipGroup domainChipGroup, View view) {
        super(domainChipGroup, view);
        this.target = domainChipGroup;
        View viewC = Utils.findRequiredView(view, R.id.chip_domain_options, "method 'onDomainOptionsClicked'");
        this.viewChipDomainOptions = viewC;
        viewC.setOnClickListener(new OnDomainOptionsClickListener(domainChipGroup));
    }

    @Override
    public void unbind() {
        DomainChipGroup domainChipGroup = this.target;
        if (domainChipGroup == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        if (this.viewChipDomainOptions != null) {
            this.viewChipDomainOptions.setOnClickListener(null);
            this.viewChipDomainOptions = null;
        }
        super.unbind();
    }
}
