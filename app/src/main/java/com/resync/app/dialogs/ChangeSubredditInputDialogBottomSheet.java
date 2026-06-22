package com.resync.app.dialogs;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.bumptech.glide.MaterialPostsHelper;
import com.resync.app.R;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractInputDialogBottomSheet;
import com.resync.app.ui.views.responsive.CustomTextInputEditText;
import com.resync.app.ui.views.responsive.chips.ContentChip;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.managers.MultiredditManager;
import com.resync.app.managers.SubredditsManager;
import com.resync.app.events.SubredditSelectedEvent;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.util.Logger;

/* JADX INFO: renamed from: t9.p */
/* JADX INFO: compiled from: ChangeSubredditInputDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class ChangeSubredditInputDialogBottomSheet extends AbstractInputDialogBottomSheet {
    ContentChip searchChip;
    ContentChip fullSubSearchChip;
    ContentChip profileChip;
    ContentChip domainChip;
    ContentChip seenChip;
    ContentChip multiChip;
    ContentChip subredditChip;
    ArrayList<ContentChip> chips = new ArrayList<>();

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void selectChip(View view) {
        Iterator<ContentChip> it = this.chips.iterator();
        while (it.hasNext()) {
            it.next().setSelected(false);
        }
        if (view instanceof ContentChip) {
            view.setSelected(true);
        }
    }

    private void moveToFront(View view) {
        try {
            ViewGroup viewGroup = (ViewGroup) view.getParent();
            viewGroup.removeView(view);
            viewGroup.addView(view, 0);
        } catch (Exception e) {
            Logger.logException(e);
        }
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractInputDialogBottomSheet
    public void onInputSetup() {
        LinearLayout linearLayout = (LinearLayout) getView().findViewById(R.id.abstract_input_dialog_bottom_sheet_content);
        View viewInflate = View.inflate(getActivity(), R.layout.bottomsheet_change_subreddit_input, null);
        linearLayout.addView(viewInflate, 0);
        this.subredditChip = (ContentChip) viewInflate.findViewById(R.id.input_subreddit);
        this.multiChip = (ContentChip) viewInflate.findViewById(R.id.input_multi);
        this.searchChip = (ContentChip) viewInflate.findViewById(R.id.input_search);
        this.fullSubSearchChip = (ContentChip) viewInflate.findViewById(R.id.input_full_sub_search);
        this.profileChip = (ContentChip) viewInflate.findViewById(R.id.input_profile);
        this.domainChip = (ContentChip) viewInflate.findViewById(R.id.input_domain);
        this.seenChip = (ContentChip) viewInflate.findViewById(R.id.input_seen);
        this.chips.add(this.subredditChip);
        this.chips.add(this.multiChip);
        this.chips.add(this.searchChip);
        this.chips.add(this.fullSubSearchChip);
        this.chips.add(this.profileChip);
        this.chips.add(this.domainChip);
        this.chips.add(this.seenChip);
        View.OnClickListener onClickListener = new View.OnClickListener() { // from class: t9.o
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ChangeSubredditInputDialogBottomSheet.this.selectChip(view);
            }
        };
        this.subredditChip.setOnClickListener(onClickListener);
        this.multiChip.setOnClickListener(onClickListener);
        this.searchChip.setOnClickListener(onClickListener);
        this.fullSubSearchChip.setOnClickListener(onClickListener);
        this.profileChip.setOnClickListener(onClickListener);
        this.domainChip.setOnClickListener(onClickListener);
        this.seenChip.setOnClickListener(onClickListener);
        if (MaterialPostsHelper.isSearch(getSubreddit())) {
            setInputText(MaterialPostsHelper.extractSearchQuery(getSubreddit()));
            this.searchChip.setSelected(true);
            moveToFront(this.searchChip);
        } else if (MaterialPostsHelper.isSubSearch(getSubreddit())) {
            setInputText(MaterialPostsHelper.extractSubSearchQuery(getSubreddit()));
            this.fullSubSearchChip.setSelected(true);
            moveToFront(this.fullSubSearchChip);
        } else if (MaterialPostsHelper.isUserProfile(getSubreddit())) {
            setInputText(MaterialPostsHelper.extractProfileUsername(getSubreddit()));
            this.profileChip.setSelected(true);
            moveToFront(this.profileChip);
        } else if (MaterialPostsHelper.isDomain(getSubreddit())) {
            setInputText(MaterialPostsHelper.extractDomain(getSubreddit()));
            this.domainChip.setSelected(true);
            moveToFront(this.domainChip);
        } else if (MaterialPostsHelper.isSeen(getSubreddit())) {
            setInputText(MaterialPostsHelper.extractSeenSubreddit(getSubreddit()));
            this.seenChip.setSelected(true);
            moveToFront(this.seenChip);
        } else if (MaterialPostsHelper.isMultireddit(getSubreddit())) {
            setInputText(MaterialPostsHelper.extractMultiredditName(getSubreddit()));
            this.multiChip.setSelected(true);
            moveToFront(this.multiChip);
        } else {
            setInputText(getSubreddit());
            this.subredditChip.setSelected(true);
            moveToFront(this.subredditChip);
        }
        CustomTextInputEditText customTextInputEditText = this.mTextInputEditText;
        customTextInputEditText.setSelection(0, customTextInputEditText.getText().length());
        this.mTextInputEditText.dismissDropDown();
        this.mTextInputEditText.requestFocus();
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractInputDialogBottomSheet
    public void onPositiveButtonClicked(String str) {
        if (this.searchChip.isSelected()) {
            if (MaterialPostsHelper.isSearch(getSubreddit())) {
                BusSingleton.getBus().post(new SubredditSelectedEvent(MaterialPostsHelper.buildSearchAddress(str, MaterialPostsHelper.extractSearchSubreddit(getSubreddit()))));
            } else {
                BusSingleton.getBus().post(new SubredditSelectedEvent(MaterialPostsHelper.buildSearchAddress(str, null)));
            }
        } else if (this.fullSubSearchChip.isSelected()) {
            BusSingleton.getBus().post(new SubredditSelectedEvent(MaterialPostsHelper.buildSubSearchAddress(str)));
        } else if (this.profileChip.isSelected()) {
            BusSingleton.getBus().post(new SubredditSelectedEvent(MaterialPostsHelper.buildOverviewProfileAddress(str.replaceAll(StringUtils.SPACE, ""))));
        } else if (this.domainChip.isSelected()) {
            BusSingleton.getBus().post(new SubredditSelectedEvent(MaterialPostsHelper.buildDomainAddress(str.replaceAll(StringUtils.SPACE, ""))));
        } else if (this.seenChip.isSelected()) {
            BusSingleton.getBus().post(new SubredditSelectedEvent(MaterialPostsHelper.buildSeenAddress(str.replaceAll(StringUtils.SPACE, ""))));
        } else if (this.multiChip.isSelected()) {
            BusSingleton.getBus().post(new SubredditSelectedEvent(MaterialPostsHelper.buildMultiredditAddress(str.replaceAll(StringUtils.SPACE, ""))));
        } else {
            BusSingleton.getBus().post(new SubredditSelectedEvent(str.replaceAll(StringUtils.SPACE, "")));
        }
        dismiss();
    }

    @Override // com.resync.app.dialogs.interfaces.InputDialogBottomSheetInterface
    public String getPositiveButtonText() {
        return "Change";
    }

    @Override // com.resync.app.dialogs.interfaces.InputDialogBottomSheetInterface
    public String getHint() {
        return null;
    }

    @Override // com.resync.app.dialogs.interfaces.InputDialogBottomSheetInterface
    public String getTitle() {
        return "Edit current";
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractInputDialogBottomSheet
    public String[] getAutocompleteOptions() {
        if (MaterialPostsHelper.isSearch(getSubreddit()) || MaterialPostsHelper.isSubSearch(getSubreddit()) || MaterialPostsHelper.isUserProfile(getSubreddit()) || MaterialPostsHelper.isDomain(getSubreddit()) || MaterialPostsHelper.isSeen(getSubreddit())) {
            return null;
        }
        return MaterialPostsHelper.isMultireddit(getSubreddit()) ? MultiredditManager.getInstance().getMultireddits().toStringArray() : SubredditsManager.getInstance().getSubreddits().toStringArray();
    }
}
