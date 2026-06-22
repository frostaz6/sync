package com.resync.app.chat;

/**
 * A single Matrix {@code m.room.message} event, flattened for display.
 *
 * <p>Reddit Chat is plain (unencrypted) Matrix — see {@link RedditChatClient}. This is just the
 * fields we render: who sent it, the text body, when, and whether it's our own message.
 */
public final class ChatMessage {
    public final String eventId;
    public final String senderId;
    public final String senderName;
    public final String body;
    public final long timestampMs;
    public final boolean mine;

    public ChatMessage(String eventId, String senderId, String senderName, String body,
                       long timestampMs, boolean mine) {
        this.eventId = eventId;
        this.senderId = senderId;
        this.senderName = senderName;
        this.body = body;
        this.timestampMs = timestampMs;
        this.mine = mine;
    }
}
