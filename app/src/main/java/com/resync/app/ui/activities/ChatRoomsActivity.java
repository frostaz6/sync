package com.resync.app.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.resync.app.R;
import com.resync.app.chat.ChatRoom;
import com.resync.app.chat.RedditChatManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Lists the user's Reddit Chat rooms/DMs (Matrix). Tapping one opens {@link ChatThreadActivity}.
 * Reuses {@link RedditChatManager}, which mints a Matrix session from the existing reddit bearer.
 */
public class ChatRoomsActivity extends BaseActivity {

    private RecyclerView recycler;
    private TextView status;
    private final RoomsAdapter adapter = new RoomsAdapter();

    public static void start(Context context) {
        context.startActivity(new Intent(context, ChatRoomsActivity.class));
    }

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_chat_rooms);
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        recycler = (RecyclerView) findViewById(R.id.chat_rooms_recycler);
        status = (TextView) findViewById(R.id.chat_rooms_status);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);
        load();
    }

    private void load() {
        setStatus("Loading chats…");
        RedditChatManager.get(this).loadRooms(new RedditChatManager.Callback<List<ChatRoom>>() {
            @Override public void onResult(List<ChatRoom> rooms) {
                if (isFinishing()) return;
                adapter.setRooms(rooms);
                setStatus(rooms.isEmpty() ? "No chats yet" : null);
            }
            @Override public void onError(String reason) {
                if (isFinishing()) return;
                setStatus("Couldn't load chat\n" + reason);
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

    private final class RoomsAdapter extends RecyclerView.Adapter<RoomVH> {
        private final List<ChatRoom> rooms = new ArrayList<>();

        void setRooms(List<ChatRoom> r) {
            rooms.clear();
            rooms.addAll(r);
            notifyDataSetChanged();
        }

        @Override public RoomVH onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chat_room, parent, false);
            return new RoomVH(v);
        }

        @Override public void onBindViewHolder(RoomVH h, int position) {
            final ChatRoom room = rooms.get(position);
            h.name.setText(room.displayName);
            h.last.setText(room.lastBody != null ? room.lastBody : "");
            h.itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    ChatThreadActivity.start(ChatRoomsActivity.this, room.roomId, room.displayName);
                }
            });
        }

        @Override public int getItemCount() {
            return rooms.size();
        }
    }

    static final class RoomVH extends RecyclerView.ViewHolder {
        final TextView name;
        final TextView last;
        RoomVH(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.chat_room_name);
            last = (TextView) v.findViewById(R.id.chat_room_last);
        }
    }
}
