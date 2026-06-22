package com.resync.app.model;

import java.io.Serializable;

/* JADX INFO: renamed from: ua.a */
/* JADX INFO: compiled from: Data.java */
/* JADX INFO: loaded from: classes.dex */
public class Data implements Serializable {
    public String bannerBackgroundImage;      // A: banner_background_image / banner_img
    public String displayName;                // B: display_name
    public String descriptionHtml;            // C: description (html)
    public String publicDescriptionHtml;      // D: public_description (html)
    public String userFlairTemplateId;        // E: user_flair_template_id
    public String iconUrl;                     // F: community/header icon
    public String id;                          // G: id
    public boolean userIsSubscriber;           // H: user_is_subscriber
    public boolean userHasFavorited;           // I: user_has_favorited

    public String name;                        // a: name
    public String accountAge;                  // b: account age string
    public String formattedDate;               // c: formatted joined date

    public String joinedDate;                  // q: "Joined on ..." string

    public int totalKarma;                     // r: total_karma

    public int linkKarma;                      // s: link_karma
    public int commentKarma;                   // t: comment_karma

    public int subscribers;                    // u: subscribers
    public int accountsActive;                 // v: accounts_active
    public boolean over18;                     // w: over18
    public boolean isFriend;                   // x: is_friend

    public String keyColor;                    // y: key_color
    public String bannerBackgroundColor;       // z: banner_background_color
}
