package com.resync.app.model;

import java.io.Serializable;

/* JADX INFO: renamed from: o8.d */
/* JADX INFO: compiled from: Message.java */
/* JADX INFO: loaded from: classes.dex */
public class Message implements Serializable {
    public boolean comment;          // A: marked true for comment-reply inbox types
    public int depth;                // B: nesting depth (indent multiplier)
    public long notificationId;      // C: notification id / timestamp

    public int kind;                 // a: kind (t1/t4/...)
    public String id;                // b: id
    public String timeAgo;           // c: relative created time

    public String author;            // q: author

    public String dest;              // r: dest

    public String subreddit;         // s: subreddit
    public String subject;           // t: subject

    public String bodyProcessed;     // u: processed body html
    public String bodyRaw;           // v: raw body
    public String parentId;          // w: parent/thread message name
    public String linkTitle;         // x: link_title

    public String context;           // y: context
    public boolean isNew;            // z: new (unread)

    public boolean equals(Object obj) {
        return obj instanceof Message ? ((Message) obj).id.equals(this.id) : super.equals(obj);
    }
}
