package com.resync.app.model;

import java.io.Serializable;

/* JADX INFO: renamed from: p8.a */
/* JADX INFO: compiled from: AbstractFlair.java */
/* JADX INFO: loaded from: classes.dex */
public class AbstractFlair implements Serializable {

    public String id;          // a: flair_template_id / link_flair_id
    private String cssClass;    // b: css class / label

    public AbstractFlair(String str, String str2) {
        this.id = str;
        this.cssClass = str2;
    }

    public String getCssClass() {
        return this.cssClass;
    }
}
