package com.resync.app.sections.posts.toolbar.chip_groups;

import android.view.View;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.ui.views.responsive.chips.ContentChip;

/* JADX INFO: loaded from: classes.dex */
public class ProfileChipGroup_ViewBinding extends AbstractChipGroup_ViewBinding {
    private ProfileChipGroup target;

    private View viewChipSection;

    private View viewChipAbout;
    private View viewChipTag;
    private View viewChipPaint;

    private View viewChipFollow;
    private View viewChipFriend;
    private View viewChipBlock;

    private View viewChipMessage;

    private View viewChipShare;
    private View viewChipTrophies;

    class OnMessageClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ ProfileChipGroup target;

        OnMessageClickListener(ProfileChipGroup profileChipGroup) {
            this.target = profileChipGroup;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onMessageClicked();
        }
    }

    class OnShareClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ ProfileChipGroup target;

        OnShareClickListener(ProfileChipGroup profileChipGroup) {
            this.target = profileChipGroup;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onShareClicked();
        }
    }

    class OnTrophyClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ ProfileChipGroup target;

        OnTrophyClickListener(ProfileChipGroup profileChipGroup) {
            this.target = profileChipGroup;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onTrophyClicked();
        }
    }

    class OnSectionClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ ProfileChipGroup target;

        OnSectionClickListener(ProfileChipGroup profileChipGroup) {
            this.target = profileChipGroup;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onSectionClicked();
        }
    }

    class OnAboutClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ ProfileChipGroup target;

        OnAboutClickListener(ProfileChipGroup profileChipGroup) {
            this.target = profileChipGroup;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onAboutClicked();
        }
    }

    class OnTagClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ ProfileChipGroup target;

        OnTagClickListener(ProfileChipGroup profileChipGroup) {
            this.target = profileChipGroup;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onTagClicked();
        }
    }

    class OnTagLongClickListener implements View.OnLongClickListener {

        final /* synthetic */ ProfileChipGroup target;

        OnTagLongClickListener(ProfileChipGroup profileChipGroup) {
            this.target = profileChipGroup;
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            return this.target.onTagLongClicked();
        }
    }

    class OnPaintClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ ProfileChipGroup target;

        OnPaintClickListener(ProfileChipGroup profileChipGroup) {
            this.target = profileChipGroup;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onPaintClicked();
        }
    }

    class OnPaintLongClickListener implements View.OnLongClickListener {

        final /* synthetic */ ProfileChipGroup target;

        OnPaintLongClickListener(ProfileChipGroup profileChipGroup) {
            this.target = profileChipGroup;
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            return this.target.onPaintLongClicked();
        }
    }

    class OnFollowClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ ProfileChipGroup target;

        OnFollowClickListener(ProfileChipGroup profileChipGroup) {
            this.target = profileChipGroup;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onFollowClicked();
        }
    }

    class OnFriendClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ ProfileChipGroup target;

        OnFriendClickListener(ProfileChipGroup profileChipGroup) {
            this.target = profileChipGroup;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onFriendClicked();
        }
    }

    class OnBlockClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ ProfileChipGroup target;

        OnBlockClickListener(ProfileChipGroup profileChipGroup) {
            this.target = profileChipGroup;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onBlockClicked();
        }
    }

    public ProfileChipGroup_ViewBinding(ProfileChipGroup profileChipGroup, View view) {
        super(profileChipGroup, view);
        this.target = profileChipGroup;
        View viewC = Utils.findRequiredView(view, R.id.chip_section, "field 'chipSection' and method 'onSectionClicked'");
        profileChipGroup.chipSection = (ContentChip) Utils.castView(viewC, R.id.chip_section, "field 'chipSection'", ContentChip.class);
        this.viewChipSection = viewC;
        viewC.setOnClickListener(new OnSectionClickListener(profileChipGroup));
        View viewC2 = Utils.findRequiredView(view, R.id.chip_about, "field 'chipAbout' and method 'onAboutClicked'");
        profileChipGroup.chipAbout = (ContentChip) Utils.castView(viewC2, R.id.chip_about, "field 'chipAbout'", ContentChip.class);
        this.viewChipAbout = viewC2;
        viewC2.setOnClickListener(new OnAboutClickListener(profileChipGroup));
        View viewC3 = Utils.findRequiredView(view, R.id.chip_tag, "field 'chipTag', method 'onTagClicked', and method 'onTagLongClicked'");
        profileChipGroup.chipTag = (ContentChip) Utils.castView(viewC3, R.id.chip_tag, "field 'chipTag'", ContentChip.class);
        this.viewChipTag = viewC3;
        viewC3.setOnClickListener(new OnTagClickListener(profileChipGroup));
        viewC3.setOnLongClickListener(new OnTagLongClickListener(profileChipGroup));
        View viewC4 = Utils.findRequiredView(view, R.id.chip_paint, "field 'chipPaint', method 'onPaintClicked', and method 'onPaintLongClicked'");
        profileChipGroup.chipPaint = (ContentChip) Utils.castView(viewC4, R.id.chip_paint, "field 'chipPaint'", ContentChip.class);
        this.viewChipPaint = viewC4;
        viewC4.setOnClickListener(new OnPaintClickListener(profileChipGroup));
        viewC4.setOnLongClickListener(new OnPaintLongClickListener(profileChipGroup));
        View viewC5 = Utils.findRequiredView(view, R.id.chip_follow, "field 'chipFollow' and method 'onFollowClicked'");
        profileChipGroup.chipFollow = (ContentChip) Utils.castView(viewC5, R.id.chip_follow, "field 'chipFollow'", ContentChip.class);
        this.viewChipFollow = viewC5;
        viewC5.setOnClickListener(new OnFollowClickListener(profileChipGroup));
        View viewC6 = Utils.findRequiredView(view, R.id.chip_friend, "field 'chipFriend' and method 'onFriendClicked'");
        profileChipGroup.chipFriend = (ContentChip) Utils.castView(viewC6, R.id.chip_friend, "field 'chipFriend'", ContentChip.class);
        this.viewChipFriend = viewC6;
        viewC6.setOnClickListener(new OnFriendClickListener(profileChipGroup));
        View viewC7 = Utils.findRequiredView(view, R.id.chip_block, "field 'chipBlock' and method 'onBlockClicked'");
        profileChipGroup.chipBlock = (ContentChip) Utils.castView(viewC7, R.id.chip_block, "field 'chipBlock'", ContentChip.class);
        this.viewChipBlock = viewC7;
        viewC7.setOnClickListener(new OnBlockClickListener(profileChipGroup));
        View viewC8 = Utils.findRequiredView(view, R.id.chip_message, "field 'chipMessage' and method 'onMessageClicked'");
        profileChipGroup.chipMessage = (ContentChip) Utils.castView(viewC8, R.id.chip_message, "field 'chipMessage'", ContentChip.class);
        this.viewChipMessage = viewC8;
        viewC8.setOnClickListener(new OnMessageClickListener(profileChipGroup));
        View viewC9 = Utils.findRequiredView(view, R.id.chip_share, "field 'chipShare' and method 'onShareClicked'");
        profileChipGroup.chipShare = (ContentChip) Utils.castView(viewC9, R.id.chip_share, "field 'chipShare'", ContentChip.class);
        this.viewChipShare = viewC9;
        viewC9.setOnClickListener(new OnShareClickListener(profileChipGroup));
        View viewC10 = Utils.findRequiredView(view, R.id.chip_tophies, "method 'onTrophyClicked'");
        this.viewChipTrophies = viewC10;
        viewC10.setOnClickListener(new OnTrophyClickListener(profileChipGroup));
    }

    @Override
    public void unbind() {
        ProfileChipGroup profileChipGroup = this.target;
        if (profileChipGroup == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        profileChipGroup.chipSection = null;
        profileChipGroup.chipAbout = null;
        profileChipGroup.chipTag = null;
        profileChipGroup.chipPaint = null;
        profileChipGroup.chipFollow = null;
        profileChipGroup.chipFriend = null;
        profileChipGroup.chipBlock = null;
        profileChipGroup.chipMessage = null;
        profileChipGroup.chipShare = null;
        if (this.viewChipSection != null) {
            this.viewChipSection.setOnClickListener(null);
            this.viewChipSection = null;
        }
        if (this.viewChipAbout != null) {
            this.viewChipAbout.setOnClickListener(null);
            this.viewChipAbout = null;
        }
        if (this.viewChipTag != null) {
            this.viewChipTag.setOnClickListener(null);
            this.viewChipTag = null;
        }
        if (this.viewChipPaint != null) {
            this.viewChipPaint.setOnClickListener(null);
            this.viewChipPaint = null;
        }
        if (this.viewChipFollow != null) {
            this.viewChipFollow.setOnClickListener(null);
            this.viewChipFollow = null;
        }
        if (this.viewChipFriend != null) {
            this.viewChipFriend.setOnClickListener(null);
            this.viewChipFriend = null;
        }
        if (this.viewChipBlock != null) {
            this.viewChipBlock.setOnClickListener(null);
            this.viewChipBlock = null;
        }
        if (this.viewChipMessage != null) {
            this.viewChipMessage.setOnClickListener(null);
            this.viewChipMessage = null;
        }
        if (this.viewChipShare != null) {
            this.viewChipShare.setOnClickListener(null);
            this.viewChipShare = null;
        }
        if (this.viewChipTrophies != null) {
            this.viewChipTrophies.setOnClickListener(null);
            this.viewChipTrophies = null;
        }
        super.unbind();
    }
}
