package com.resync.app.chat;

/**
 * A Matrix room as shown in the chat rooms list. For Reddit Chat these are almost entirely DMs
 * (the {@code m.direct} account-data lists them), so {@link #displayName} is the other participant's
 * reddit username when there's no explicit room name.
 */
public final class ChatRoom {
    public final String roomId;
    public final String displayName;
    public final String lastBody;
    public final long lastTimestampMs;

    public ChatRoom(String roomId, String displayName, String lastBody, long lastTimestampMs) {
        this.roomId = roomId;
        this.displayName = displayName;
        this.lastBody = lastBody;
        this.lastTimestampMs = lastTimestampMs;
    }
}
