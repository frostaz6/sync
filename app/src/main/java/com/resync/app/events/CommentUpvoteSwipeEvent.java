package com.resync.app.events;

import com.resync.app.model.Post;

/* JADX INFO: renamed from: j7.f */
/* JADX INFO: compiled from: CommentUpvoteSwipeEvent.java */
/* JADX INFO: loaded from: classes.dex */
public class CommentUpvoteSwipeEvent {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final Post post;

    public CommentUpvoteSwipeEvent(Post post) {
        this.post = post;
    }
}
