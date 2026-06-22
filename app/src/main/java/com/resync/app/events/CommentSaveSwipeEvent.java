package com.resync.app.events;

import com.resync.app.model.Post;

/* JADX INFO: renamed from: j7.d */
/* JADX INFO: compiled from: CommentSaveSwipeEvent.java */
/* JADX INFO: loaded from: classes.dex */
public class CommentSaveSwipeEvent {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final Post post;

    public CommentSaveSwipeEvent(Post post) {
        this.post = post;
    }
}
