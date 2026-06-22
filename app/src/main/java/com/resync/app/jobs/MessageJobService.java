package com.resync.app.jobs;

import android.content.Context;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.common.util.concurrent.ListenableFuture;
import com.resync.app.helpers.Callback;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.model.Message;
import com.resync.app.util.ArrayUtil;
import com.resync.app.util.Logger;
import com.resync.app.net.oauth.OAuthNewMessagesRequest;
import com.resync.app.net.oauth.OAuthServiceMessageCountRequest;
import com.resync.app.helpers.messaging.MessagingHelper;

/* JADX INFO: loaded from: classes.dex */
public class MessageJobService extends ListenableWorker {

    public static String TAG = "MessageJobService";

    class WorkCompleter implements Callback {

        final /* synthetic */ CallbackToFutureAdapter.Completer completer;

        WorkCompleter(CallbackToFutureAdapter.Completer completer) {
            this.completer = completer;
        }

        @Override // com.resync.app.helpers.Callback
        public void onSuccess() {
            this.completer.set(ListenableWorker.Result.success());
        }

        @Override // com.resync.app.helpers.Callback
        public void onFinished() {
            this.completer.set(ListenableWorker.Result.failure());
        }
    }

    class MessageCountListener implements Response.Listener<Integer> {

        final /* synthetic */ Callback callback;

        MessageCountListener(Callback callback) {
            this.callback = callback;
        }

        @Override // com.android.volley.Response.Listener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public void onResponse(Integer num) {
            if (num == null || num.intValue() <= 0) {
                Logger.verbose(MessageJobService.TAG, "There are no messages to grab...");
                this.callback.onFinished();
            } else {
                Logger.verbose(MessageJobService.TAG, "There are messages to grab!");
                MessageJobService.this.fetchMessages(this.callback);
            }
        }
    }

    class MessageCountErrorListener implements Response.ErrorListener {

        final /* synthetic */ Callback callback;

        MessageCountErrorListener(Callback callback) {
            this.callback = callback;
        }

        @Override // com.android.volley.Response.ErrorListener
        public void onErrorResponse(VolleyError volleyError) {
            this.callback.onSuccess();
        }
    }

    class NewMessagesListener implements Response.Listener<Message[]> {

        final /* synthetic */ Callback callback;

        NewMessagesListener(Callback callback) {
            this.callback = callback;
        }

        @Override // com.android.volley.Response.Listener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public void onResponse(Message[] messageArr) {
            Logger.verbose(MessageJobService.TAG, "New messages: " + ArrayUtil.getLength(messageArr));
            MessagingHelper.notifyMessages(MessageJobService.this.getApplicationContext(), messageArr);
            this.callback.onFinished();
        }
    }

    class NewMessagesErrorListener implements Response.ErrorListener {

        final /* synthetic */ Callback callback;

        NewMessagesErrorListener(Callback callback) {
            this.callback = callback;
        }

        @Override // com.android.volley.Response.ErrorListener
        public void onErrorResponse(VolleyError volleyError) {
            this.callback.onSuccess();
        }
    }

    public MessageJobService(Context context, WorkerParameters workerParameters) {
        super(context, workerParameters);
    }

    private void checkMessageCount(Callback callback) {
        Logger.verbose(TAG, "Checking if we should check messages");
        OAuthRequestManager.send(getApplicationContext(), new OAuthServiceMessageCountRequest(getApplicationContext(), new MessageCountListener(callback), new MessageCountErrorListener(callback)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fetchMessages(Callback callback) {
        Logger.verbose(TAG, "Checking messages inner");
        OAuthRequestManager.send(getApplicationContext(), new OAuthNewMessagesRequest(getApplicationContext(), new NewMessagesListener(callback), new NewMessagesErrorListener(callback)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Object startWorkInternal(CallbackToFutureAdapter.Completer completer) throws Exception {
        WorkCompleter workCompleter = new WorkCompleter(completer);
        checkMessageCount(workCompleter);
        return workCompleter;
    }

    @Override // androidx.work.ListenableWorker
    public ListenableFuture<ListenableWorker.Result> startWork() {
        Logger.verbose(TAG, "MessageJobService started!");
        return CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver() { // from class: j8.d
            @Override // androidx.concurrent.futures.CallbackToFutureAdapter.Resolver
            public final Object attachCompleter(CallbackToFutureAdapter.Completer completer) throws Exception {
                return MessageJobService.this.startWorkInternal(completer);
            }
        });
    }
}
