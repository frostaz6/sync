package com.resync.app.dialogs;

import android.os.Bundle;
import com.resync.app.R;
import com.resync.app.singleton.AccountSingleton;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.events.OnBlockMessageAuthorEvent;
import com.resync.app.events.OnDeleteMessageAuthorEvent;
import com.resync.app.events.OnMarkMessageViewedEvent;
import com.resync.app.events.OnMessageReplyEvent;
import com.resync.app.events.OnViewMessageAuthorEvent;
import com.resync.app.events.OnViewMessageContextEvent;
import com.resync.app.model.Message;

/* JADX INFO: compiled from: MessageSelectionDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class MessageSelectionDialogBottomSheet extends AbstractSelectionDialogBottomSheet {
    Message message;
    AbstractSelectionDialogBottomSheet.SelectionItem replyItem;
    AbstractSelectionDialogBottomSheet.SelectionItem contextItem;
    AbstractSelectionDialogBottomSheet.SelectionItem markAsReadItem;
    AbstractSelectionDialogBottomSheet.SelectionItem profileItem;
    AbstractSelectionDialogBottomSheet.SelectionItem blockItem;
    AbstractSelectionDialogBottomSheet.SelectionItem deleteItem;

    public static Bundle buildArguments(Message message) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("message", message);
        return bundle;
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet
    public void setupItems() {
        this.replyItem = addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(R.drawable.outline_reply, "Reply"));
        if (this.message.kind == 1) {
            this.contextItem = addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(R.drawable.outline_post_24, "Context"));
        }
        if (this.message.isNew) {
            this.markAsReadItem = addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(R.drawable.outline_check_24, "Mark as read"));
        }
        this.profileItem = addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(R.drawable.outline_account_circle_24, "Profile"));
        Message message = this.message;
        if (message.kind == 4 && !message.author.equalsIgnoreCase(AccountSingleton.getInstance().getUsername())) {
            this.blockItem = addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(R.drawable.outline_report_24, "Block"));
        }
        Message message2 = this.message;
        if (message2.kind != 4 || message2.author.equalsIgnoreCase(AccountSingleton.getInstance().getUsername())) {
            return;
        }
        this.deleteItem = addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(R.drawable.outline_delete_24, "Delete"));
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public boolean requiresArguments() {
        return true;
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public void loadCustomArguments() {
        this.message = (Message) getArguments().getSerializable("message");
    }

    @Override // com.resync.app.dialogs.interfaces.SelectionDialogBottomSheetInterface
    public String getTitle() {
        return this.message.subject.equalsIgnoreCase("comment reply") ? "Comment reply options" : this.message.subject.equalsIgnoreCase("post reply") ? "Post reply options" : this.message.subject.equalsIgnoreCase("username mention") ? "Username mention options" : "Message options";
    }

    @Override // com.resync.app.dialogs.interfaces.SelectionDialogBottomSheetInterface
    public void onItemSelected(AbstractSelectionDialogBottomSheet.SelectionItem item) {
        if (item.equals(this.replyItem)) {
            BusSingleton.getBus().post(new OnMessageReplyEvent(this.message));
        } else if (item.equals(this.contextItem)) {
            BusSingleton.getBus().post(new OnViewMessageContextEvent(this.message));
        } else if (item.equals(this.markAsReadItem)) {
            BusSingleton.getBus().post(new OnMarkMessageViewedEvent(this.message));
        } else if (item.equals(this.profileItem)) {
            BusSingleton.getBus().post(new OnViewMessageAuthorEvent(this.message));
        } else if (item.equals(this.blockItem)) {
            BusSingleton.getBus().post(new OnBlockMessageAuthorEvent(this.message));
        } else if (item.equals(this.deleteItem)) {
            BusSingleton.getBus().post(new OnDeleteMessageAuthorEvent(this.message));
        }
        dismiss();
    }
}
