package com.resync.app.model;

import android.text.TextUtils;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.util.Logger;

/* JADX INFO: renamed from: o8.h */
/* JADX INFO: compiled from: RedditVideoDescription.java */
/* JADX INFO: loaded from: classes.dex */
public class RedditVideoDescription {

    public String videoUrl;   // a: DASH video url
    public String audioUrl;   // b: DASH audio url

    public RedditVideoDescription(String str, String str2, String str3) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        if (StringUtils.startsWith(str2, "http")) {
            this.videoUrl = str2;
        } else {
            this.videoUrl = "https://v.redd.it/" + str + "/" + str2;
        }
        if (StringUtils.isNotEmpty(str3)) {
            if (StringUtils.startsWith(str3, "http")) {
                this.audioUrl = str3;
            } else {
                this.audioUrl = "https://v.redd.it/" + str + "/" + str3;
            }
        }
        Logger.verbose("DONE");
    }

    public boolean hasVideo() {
        return !TextUtils.isEmpty(this.videoUrl);
    }
}
