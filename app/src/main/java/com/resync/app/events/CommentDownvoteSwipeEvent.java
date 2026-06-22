package com.resync.app.events;

import com.resync.app.model.Post;

/* JADX INFO: renamed from: j7.a */
/* JADX INFO: compiled from: CommentDownvoteSwipeEvent.java */
/* JADX INFO: loaded from: classes.dex */
public class CommentDownvoteSwipeEvent {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final Post post;

    public CommentDownvoteSwipeEvent(Post post) {
        this.post = post;
    }
}
