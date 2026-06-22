package com.resync.app.text.html;

import java.util.ArrayList;
import java.util.Objects;
import com.resync.app.text.spans.CustomUrlSpan;
import com.resync.app.text.spans.SyncSpan;

/* JADX INFO: renamed from: nc.b */
/* JADX INFO: compiled from: ReferencedImages.java */
/* JADX INFO: loaded from: classes3.dex */
public class ReferencedImages extends ArrayList<ReferencedImages.Ref> {

    /* JADX INFO: compiled from: ReferencedImages.java */
    public static class Ref {
        public SyncSpan span;
        public CustomUrlSpan urlSpan;

        public Ref(SyncSpan syncSpan, CustomUrlSpan customUrlSpan) {
            this.span = syncSpan;
            this.urlSpan = customUrlSpan;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            return Objects.equals(this.urlSpan.getUrl(), ((Ref) obj).urlSpan.getUrl());
        }

        public int hashCode() {
            return Objects.hash(this.urlSpan);
        }
    }

    public String register(Ref ref) {
        if (contains(ref)) {
            return Integer.toString(indexOf(ref) + 1);
        }
        add(ref);
        return Integer.toString(size());
    }
}
