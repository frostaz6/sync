package com.resync.app.presenters;

import android.os.Bundle;
import java.util.ArrayList;
import com.resync.app.model.Message;

/* JADX INFO: renamed from: ya.e */
/* JADX INFO: compiled from: MessagingPresenter.java */
/* JADX INFO: loaded from: classes.dex */
public interface MessagingPresenter extends BasePresenter {
    void saveState(Bundle bundle);

    void onMessageClicked(Message message);

    void blockAuthor(Message message);

    void viewContext(Message message);

    void deleteMessage(Message message);

    int getMode();

    void load(boolean refresh);

    void setMode(Bundle bundle, int mode);

    void composeMessage();

    ArrayList<Message> getMessages();

    void replyToMessage(Message message);

    void viewAuthor(Message message);

    void markViewed(Message message);

    int getMessageCount();
}
