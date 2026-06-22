package com.resync.app.sections.posts.toolbar.chip_groups;

import android.view.View;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.ui.views.responsive.chips.ContentChip;
import com.resync.app.util.DebouncingOnClickListener;

/* JADX INFO: loaded from: classes.dex */
public class ModChipGroup_ViewBinding extends AbstractChipGroup_ViewBinding {
    private ModChipGroup target;

    private View viewChipSection;

    class OnSectionClickListener extends DebouncingOnClickListener {

        final /* synthetic */ ModChipGroup target;

        OnSectionClickListener(ModChipGroup modChipGroup) {
            this.target = modChipGroup;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onSectionClicked();
        }
    }

    public ModChipGroup_ViewBinding(ModChipGroup modChipGroup, View view) {
        super(modChipGroup, view);
        this.target = modChipGroup;
        View viewC = Utils.findRequiredView(view, R.id.chip_section, "field 'chipSection' and method 'onSectionClicked'");
        modChipGroup.chipSection = (ContentChip) Utils.castView(viewC, R.id.chip_section, "field 'chipSection'", ContentChip.class);
        this.viewChipSection = viewC;
        viewC.setOnClickListener(new OnSectionClickListener(modChipGroup));
    }

    @Override
    public void unbind() {
        ModChipGroup modChipGroup = this.target;
        if (modChipGroup == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        modChipGroup.chipSection = null;
        if (this.viewChipSection != null) {
            this.viewChipSection.setOnClickListener(null);
            this.viewChipSection = null;
        }
        super.unbind();
    }
}
