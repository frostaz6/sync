package com.resync.app.model;

import java.io.Serializable;
import com.resync.app.model.Trophy;

/* JADX INFO: renamed from: ua.b */
/* JADX INFO: compiled from: Subreddit.java */
/* JADX INFO: loaded from: classes.dex */
public class Subreddit implements Serializable {

    public String name;
    public int errorCode;
    public String kind;

    public Data data;

    public Trophy[] trophies;
}
