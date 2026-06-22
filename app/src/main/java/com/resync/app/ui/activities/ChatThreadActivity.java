package com.resync.app.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.resync.app.R;
import com.resync.app.chat.ChatMessage;
import com.resync.app.chat.RedditChatManager;
import com.resync.app.helpers.ui.StaticToast;

import java.util.ArrayList;
import java.util.List;

/**
 * A single Reddit Chat conversation (Matrix room): shows recent messages, sends new ones, and
 * lightly polls for incoming messages while open. Read-only-safe: send failures just toast.
 */
public class ChatThreadActivity extends BaseActivity {

    private static final long POLL_MS = 4000L;

    private String roomId;
    private RecyclerView recycler;
    private TextView status;
    private EditText input;
    private Button send;
    private final MessagesAdapter adapter = new MessagesAdapter();
    private final Handler handler = new Handler(Looper.getMainLooper());
    private boolean active;

    public static void start(Context context, String roomId, String title) {
        Intent i = new Intent(context, ChatThreadActivity.class);
        i.putExtra("room_id", roomId);
        i.putExtra("title", title);
        context.startActivity(i);
    }

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_chat_thread);
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        roomId = getIntent().getStringExtra("room_id");
        String title = getIntent().getStringExtra("title");

        ((TextView) findViewById(R.id.chat_thread_title)).setText(title != null ? title : "Chat");
        status = (TextView) findViewById(R.id.chat_thread_status);
        recycler = (RecyclerView) findViewById(R.id.chat_thread_recycler);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setStackFromEnd(true); // newest at the bottom
        recycler.setLayoutManager(lm);
        recycler.setAdapter(adapter);

        input = (EditText) findViewById(R.id.chat_thread_input);
        send = (Button) findViewById(R.id.chat_thread_send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { doSend(); }
        });

        if (TextUtils.isEmpty(roomId)) {
            setStatus("Conversation unavailable");
            return;
        }
        load(true);
    }

    @Override public void onResume() {
        super.onResume();
        active = true;
        handler.postDelayed(poll, POLL_MS);
    }

    @Override protected void onPause() {
        active = false;
        handler.removeCallbacks(poll);
        super.onPause();
    }

    private final Runnable poll = new Runnable() {
        @Override public void run() {
            if (!active) return;
            load(false);
            handler.postDelayed(this, POLL_MS);
        }
    };

    private void load(final boolean showSpinner) {
        if (showSpinner) setStatus("Loading…");
        RedditChatManager.get(this).loadMessages(roomId,
                new RedditChatManager.Callback<List<ChatMessage>>() {
            @Override public void onResult(List<ChatMessage> messages) {
                if (isFinishing()) return;
                boolean changed = adapter.setMessages(messages);
                setStatus(messages.isEmpty() ? "No messages yet" : null);
                if (changed) recycler.scrollToPosition(adapter.getItemCount() - 1);
            }
            @Override public void onError(String reason) {
                if (isFinishing()) return;
                if (adapter.getItemCount() == 0) setStatus("Couldn't load\n" + reason);
            }
        });
    }

    private void doSend() {
        final String text = input.getText().toString().trim();
        if (TextUtils.isEmpty(text)) return;
        input.setText("");
        send.setEnabled(false);
        RedditChatManager.get(this).send(roomId, text, new RedditChatManager.Callback<String>() {
            @Override public void onResult(String eventId) {
                if (isFinishing()) return;
                send.setEnabled(true);
                load(false); // refresh to show the sent message with its real event
            }
            @Override public void onError(String reason) {
                if (isFinishing()) return;
                send.setEnabled(true);
                input.setText(text); // restore so the user doesn't lose it
                StaticToast.showWithContext(ChatThreadActivity.this, "Couldn't send: " + reason);
            }
        });
    }

    private void setStatus(String text) {
        if (text == null) {
            status.setVisibility(View.GONE);
        } else {
            status.setText(text);
            status.setVisibility(View.VISIBLE);
        }
    }

    // ---- adapter ----

    private static final class MessagesAdapter extends RecyclerView.Adapter<MsgVH> {
        private final List<ChatMessage> items = new ArrayList<>();

        /** Replaces the list; returns true if it actually changed (by count or last event id). */
        boolean setMessages(List<ChatMessage> next) {
            String oldLast = items.isEmpty() ? null : items.get(items.size() - 1).eventId;
            String newLast = next.isEmpty() ? null : next.get(next.size() - 1).eventId;
            if (items.size() == next.size() && TextUtils.equals(oldLast, newLast)) {
                return false;
            }
            items.clear();
            items.addAll(next);
            notifyDataSetChanged();
            return true;
        }

        @Override public MsgVH onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chat_message, parent, false);
            return new MsgVH(v);
        }

        @Override public void onBindViewHolder(MsgVH h, int position) {
            ChatMessage m = items.get(position);
            h.sender.setText(m.mine ? "You" : m.senderName);
            h.body.setText(m.body);
            int g = m.mine ? Gravity.END : Gravity.START;
            ((LinearLayout) h.itemView).setGravity(g);
            h.sender.setGravity(g);
            h.body.setGravity(g);
        }

        @Override public int getItemCount() {
            return items.size();
        }
    }

    static final class MsgVH extends RecyclerView.ViewHolder {
        final TextView sender;
        final TextView body;
        MsgVH(View v) {
            super(v);
            sender = (TextView) v.findViewById(R.id.chat_msg_sender);
            body = (TextView) v.findViewById(R.id.chat_msg_body);
        }
    }
}
