package com.resync.app.sections.posts.toolbar.chip_groups;

import android.content.Context;
import butterknife.BindView;
import butterknife.OnClick;
import com.bumptech.glide.MaterialPostsHelper;
import com.resync.app.R;
import com.resync.app.ui.views.responsive.chips.ContentChip;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.events.OnComposeMessageEvent;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.dialogs.MarkAllAsReadAlertDialogBottomSheet;
import com.resync.app.dialogs.MessagingSectionSelectionDialogBottomSheet;

/* JADX INFO: loaded from: classes.dex */
public class MessagingChipGroup extends AbstractChipGroup {

    @BindView(R.id.chip_section)
    ContentChip chipSection;

    public MessagingChipGroup(Context context) {
        super(context);
    }

    @Override // com.google.android.material.chip.ChipGroupInterface
    public int getLayoutResource() {
        return R.layout.view_chip_group_messaging;
    }

    @OnClick(R.id.chip_mark)
    public void onMarkAllClicked() {
        FragmentUtils.show(MarkAllAsReadAlertDialogBottomSheet.class, getFragmentManager());
    }

    @OnClick(R.id.chip_message)
    public void onMessageClicked() {
        BusSingleton.getBus().post(new OnComposeMessageEvent());
    }

    @OnClick(R.id.chip_section)
    public void onSectionClicked() {
        FragmentUtils.show(MessagingSectionSelectionDialogBottomSheet.class, getFragmentManager());
    }

    @Override // com.resync.app.sections.posts.toolbar.chip_groups.AbstractChipGroup
    public void bind(String str, String str2, String str3) {
        super.bind(str, str2, str3);
        this.chipSection.setText(MaterialPostsHelper.getMessagingSectionTitleFromString(str));
        this.chipSection.setChipIconResource(MaterialPostsHelper.getMessagingSectionIconResFromString(str));
    }
}
