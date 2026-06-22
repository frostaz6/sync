package com.resync.app.sections.posts.toolbar.chip_groups;

import android.view.View;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.ui.views.responsive.chips.ContentChip;

/* JADX INFO: loaded from: classes.dex */
public class MessagingChipGroup_ViewBinding extends AbstractChipGroup_ViewBinding {
    private MessagingChipGroup target;

    private View viewChipSection;

    private View viewChipMessage;
    private View viewChipMark;

    class OnSectionClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ MessagingChipGroup target;

        OnSectionClickListener(MessagingChipGroup messagingChipGroup) {
            this.target = messagingChipGroup;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onSectionClicked();
        }
    }

    class OnMessageClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ MessagingChipGroup target;

        OnMessageClickListener(MessagingChipGroup messagingChipGroup) {
            this.target = messagingChipGroup;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onMessageClicked();
        }
    }

    class OnMarkAllClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ MessagingChipGroup target;

        OnMarkAllClickListener(MessagingChipGroup messagingChipGroup) {
            this.target = messagingChipGroup;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onMarkAllClicked();
        }
    }

    public MessagingChipGroup_ViewBinding(MessagingChipGroup messagingChipGroup, View view) {
        super(messagingChipGroup, view);
        this.target = messagingChipGroup;
        View viewC = Utils.findRequiredView(view, R.id.chip_section, "field 'chipSection' and method 'onSectionClicked'");
        messagingChipGroup.chipSection = (ContentChip) Utils.castView(viewC, R.id.chip_section, "field 'chipSection'", ContentChip.class);
        this.viewChipSection = viewC;
        viewC.setOnClickListener(new OnSectionClickListener(messagingChipGroup));
        View viewC2 = Utils.findRequiredView(view, R.id.chip_message, "method 'onMessageClicked'");
        this.viewChipMessage = viewC2;
        viewC2.setOnClickListener(new OnMessageClickListener(messagingChipGroup));
        View viewC3 = Utils.findRequiredView(view, R.id.chip_mark, "method 'onMarkAllClicked'");
        this.viewChipMark = viewC3;
        viewC3.setOnClickListener(new OnMarkAllClickListener(messagingChipGroup));
    }

    @Override
    public void unbind() {
        MessagingChipGroup messagingChipGroup = this.target;
        if (messagingChipGroup == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        messagingChipGroup.chipSection = null;
        if (this.viewChipSection != null) {
            this.viewChipSection.setOnClickListener(null);
            this.viewChipSection = null;
        }
        if (this.viewChipMessage != null) {
            this.viewChipMessage.setOnClickListener(null);
            this.viewChipMessage = null;
        }
        if (this.viewChipMark != null) {
            this.viewChipMark.setOnClickListener(null);
            this.viewChipMark = null;
        }
        super.unbind();
    }
}
