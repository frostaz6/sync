package com.resync.app.presenters;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.resync.app.R;
import com.resync.app.singleton.AccountSingleton;
import com.resync.app.ui.fragment_dialogs.EditFragment;
import java.util.ArrayList;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.model.Message;
import com.resync.app.managers.BlockManager;
import com.resync.app.helpers.CrashlyticsHelper;
import com.resync.app.util.ArrayUtil;
import com.resync.app.util.Logger;
import com.resync.app.model.Post;
import com.resync.app.util.links.LinkHelper;
import com.resync.app.net.oauth.OAuthDeleteMessageRequest;
import com.resync.app.net.oauth.OAuthMarkMessageRequest;
import com.resync.app.net.oauth.OAuthMessagesRequest;
import com.resync.app.presenters.MessagingView;

/* JADX INFO: renamed from: ya.f */
/* JADX INFO: compiled from: MessagingPresenterImpl.java */
/* JADX INFO: loaded from: classes.dex */
public class MessagingPresenterImpl implements MessagingPresenter {

    protected boolean reachedEnd;
    protected int mode;
    protected Context context;
    protected MessagingView view;

    protected ArrayList<Message> messages;

    protected OAuthMessagesRequest messagesRequest;

    /* JADX INFO: renamed from: ya.f$a */
    /* JADX INFO: compiled from: MessagingPresenterImpl.java */
    class MessagesResponseListener implements Response.Listener<Message[]> {
        MessagesResponseListener() {
        }

        @Override // com.android.volley.Response.Listener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public void onResponse(Message[] messageArr) {
            MessagingPresenterImpl messagingPresenterImpl = MessagingPresenterImpl.this;
            if (messagingPresenterImpl.context == null) {
                return;
            }
            messagingPresenterImpl.view.setRefreshing(false);
            if (ArrayUtil.isEmpty(messageArr)) {
                MessagingPresenterImpl.this.view.showStatusMessage(R.string.no_messages);
                return;
            }
            int size = MessagingPresenterImpl.this.messages.size();
            for (int i = 0; i < messageArr.length; i++) {
                if (!MessagingPresenterImpl.this.messages.contains(messageArr[i])) {
                    MessagingPresenterImpl.this.messages.add(messageArr[i]);
                }
            }
            if (MessagingPresenterImpl.this.messages.size() == size) {
                MessagingPresenterImpl.this.reachedEnd = true;
                Logger.verbose("End of messaging.");
            }
            MessagingPresenterImpl.this.applyEnterExitAnimation();
            MessagingPresenterImpl.this.view.notifyDataChanged();
            MessagingPresenterImpl.this.messagesRequest = null;
        }
    }

    /* JADX INFO: renamed from: ya.f$b */
    /* JADX INFO: compiled from: MessagingPresenterImpl.java */
    class MessagesErrorListener implements Response.ErrorListener {
        MessagesErrorListener() {
        }

        @Override // com.android.volley.Response.ErrorListener
        public void onErrorResponse(VolleyError volleyError) {
            MessagingPresenterImpl.this.onLoadError(volleyError);
        }
    }

    public MessagingPresenterImpl(Context context, Bundle bundle, MessagingView messagingView) {
        this.context = context;
        this.view = messagingView;
        if (bundle == null || !bundle.containsKey("messages")) {
            this.messages = new ArrayList<>();
        } else {
            this.messages = (ArrayList) bundle.getSerializable("messages");
        }
    }

    public static Bundle buildArgs(int mode) {
        Bundle bundle = new Bundle();
        bundle.putInt("mode", mode);
        return bundle;
    }

    @Override // com.resync.app.presenters.MessagingPresenter
    public void onMessageClicked(Message message) {
        markViewed(message);
        int kind = message.kind;
        if (kind == 1) {
            viewContext(message);
        } else if (kind == 4) {
            replyToMessage(message);
        }
    }

    @Override // com.resync.app.presenters.MessagingPresenter
    public void blockAuthor(Message message) {
        if (message == null) {
            return;
        }
        BlockManager.confirmBlockUser(this.context, message.author);
    }

    public void cancelMessagesRequest() {
        if (this.messagesRequest != null) {
            Logger.verbose("MessagingPresenterImpl", "Cancelling messages request");
            this.messagesRequest.cancel();
            this.messagesRequest = null;
        }
    }

    @Override // com.resync.app.presenters.BasePresenter
    public void reset() {
        Logger.verbose("MessagingPresenterImpl", "Resetting messaging presenter");
        this.reachedEnd = false;
        cancelMessagesRequest();
        applyEnterExitAnimation();
        this.messages.clear();
        this.view.notifyDataChanged();
    }

    public void requestMessages(boolean z) {
        String str;
        if (this.messages.size() > 0) {
            str = this.messages.get(Math.max(this.messages.size() - 1, 0)).parentId;
        } else {
            str = null;
        }
        OAuthMessagesRequest oAuthMessagesRequest = new OAuthMessagesRequest(this.context, this.mode, str, new MessagesResponseListener(), new MessagesErrorListener());
        this.messagesRequest = oAuthMessagesRequest;
        OAuthRequestManager.send(this.context, oAuthMessagesRequest);
    }

    @Override // com.resync.app.presenters.MessagingPresenter
    public void viewContext(Message message) {
        markViewed(message);
        LinkHelper.open(this.context, message.context);
    }

    public void applyEnterExitAnimation() {
        this.view.setEnterExitAnimation();
    }

    public void onLoadError(VolleyError volleyError) {
        this.view.showError(volleyError);
    }

    @Override // com.resync.app.presenters.MessagingPresenter
    public void deleteMessage(Message message) {
        Context context = this.context;
        OAuthRequestManager.send(context, new OAuthDeleteMessageRequest(context, message));
        applyEnterExitAnimation();
        this.messages.remove(message);
        this.view.notifyDataChanged();
    }

    @Override // com.resync.app.presenters.BasePresenter
    public void refresh() {
        reset();
        load(true);
    }

    @Override // com.resync.app.presenters.BasePresenter
    public void saveState(Bundle bundle) {
        ArrayList<Message> arrayList = this.messages;
        if (arrayList == null || arrayList.size() <= 0) {
            return;
        }
        bundle.putSerializable("messages", this.messages);
    }

    @Override // com.resync.app.presenters.MessagingPresenter
    public int getMode() {
        return this.mode;
    }

    @Override // com.resync.app.presenters.BasePresenter
    public void onStart() {
    }

    @Override // com.resync.app.presenters.MessagingPresenter
    public void load(boolean z) {
        if (this.reachedEnd) {
            return;
        }
        this.view.setRefreshing(true);
        if (z) {
            applyEnterExitAnimation();
        }
        if (this.messagesRequest == null) {
            requestMessages(z);
        }
    }

    @Override // com.resync.app.presenters.BasePresenter
    public void recover() {
        refresh();
    }

    @Override // com.resync.app.presenters.MessagingPresenter
    public void setMode(Bundle bundle, int mode) {
        bundle.remove("mode");
        bundle.putInt("mode", mode);
        this.mode = mode;
        reset();
        load(true);
    }

    @Override // com.resync.app.presenters.MessagingPresenter
    public void composeMessage() {
        EditFragment.newMessageInstance(null).show(((AppCompatActivity) this.context).getSupportFragmentManager(), "EditFragment");
    }

    @Override // com.resync.app.presenters.MessagingPresenter
    public ArrayList<Message> getMessages() {
        return this.messages;
    }

    @Override // com.resync.app.presenters.MessagingPresenter
    public void replyToMessage(Message message) {
        markViewed(message);
        int kind = message.kind;
        if (kind == 4) {
            try {
                EditFragment.newMessageReplyInstance(Post.fromMessage(message, 4)).show(((AppCompatActivity) this.context).getSupportFragmentManager(), "EditFragment");
                return;
            } catch (Exception e) {
                CrashlyticsHelper.logException(e);
                return;
            }
        }
        if (kind == 1) {
            try {
                EditFragment.newCommentInstance(null, null, Post.fromMessage(message, 1)).show(((AppCompatActivity) this.context).getSupportFragmentManager(), "EditFragment");
            } catch (Exception e) {
                CrashlyticsHelper.logException(e);
            }
        }
    }

    @Override // com.resync.app.presenters.MessagingPresenter
    public void viewAuthor(Message message) {
        if (message == null) {
            return;
        }
        if (AccountSingleton.getInstance().getUsername().equalsIgnoreCase(message.author)) {
            com.resync.app.util.links.LinkHandler.openProfile(this.context, message.dest);
        } else {
            com.resync.app.util.links.LinkHandler.openProfile(this.context, message.author);
        }
    }

    @Override // com.resync.app.presenters.BasePresenter
    public void onDestroy() {
    }

    @Override // com.resync.app.presenters.BasePresenter
    public void onStop() {
    }

    @Override // com.resync.app.presenters.MessagingPresenter
    public void markViewed(Message message) {
        if (message.isNew) {
            Context context = this.context;
            OAuthRequestManager.send(context, new OAuthMarkMessageRequest(context, message.kind, message.id));
            message.isNew = false;
            this.view.notifyDataChanged();
        }
    }

    @Override // com.resync.app.presenters.BasePresenter
    public void restoreArguments(Bundle bundle) {
        this.mode = bundle.getInt("mode");
    }

    @Override // com.resync.app.presenters.MessagingPresenter
    public int getMessageCount() {
        return this.messages.size();
    }
}
