package com.resync.app.sections.posts.toolbar.chip_groups;

import android.content.Context;
import butterknife.BindView;
import butterknife.OnClick;
import com.bumptech.glide.MaterialPostsHelper;
import com.resync.app.R;
import com.resync.app.ui.views.responsive.chips.ContentChip;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.dialogs.ModSectionSelectionDialogBottomSheet;

/* JADX INFO: loaded from: classes.dex */
public class ModChipGroup extends AbstractChipGroup {

    @BindView(R.id.chip_section)
    ContentChip chipSection;

    public ModChipGroup(Context context) {
        super(context);
    }

    @Override // com.google.android.material.chip.ChipGroupInterface
    public int getLayoutResource() {
        return R.layout.view_chip_group_mod;
    }

    @OnClick(R.id.chip_section)
    public void onSectionClicked() {
        FragmentUtils.show(ModSectionSelectionDialogBottomSheet.class, getFragmentManager());
    }

    @Override // com.resync.app.sections.posts.toolbar.chip_groups.AbstractChipGroup
    public void bind(String str, String str2, String str3) {
        super.bind(str, str2, str3);
        this.chipSection.setText(MaterialPostsHelper.extractModSection(str));
        this.chipSection.setChipIconResource(MaterialPostsHelper.getModSectionIconRes(str));
    }
}
