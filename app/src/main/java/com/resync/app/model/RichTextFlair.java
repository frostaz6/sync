package com.resync.app.model;

import java.util.ArrayList;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.text.spans.EfficientImageSpan;
import com.resync.app.widgets.text.SpannableTextViewStringBuilder;

/* JADX INFO: renamed from: p8.c */
/* JADX INFO: compiled from: RichTextFlair.java */
/* JADX INFO: loaded from: classes.dex */
public class RichTextFlair extends AbstractFlair {
    ArrayList<Object> segments;

    /* JADX INFO: renamed from: p8.c$a */
    /* JADX INFO: compiled from: RichTextFlair.java */
    class EmojiSegment {

        public String text;       // a: emoji text (key "a")
        public String imageUrl;   // b: emoji image url (key "u")

        public EmojiSegment(String text, String imageUrl) {
            this.text = text;
            this.imageUrl = imageUrl;
        }
    }

    /* JADX INFO: renamed from: p8.c$b */
    /* JADX INFO: compiled from: RichTextFlair.java */
    class TextSegment {

        public String text;       // a: plain text (key "t")

        public TextSegment(String text) {
            this.text = text;
        }
    }

    public RichTextFlair(String id, String cssClass) {
        super(id, cssClass);
        this.segments = new ArrayList<>();
    }

    public void addEmoji(String text, String imageUrl) {
        this.segments.add(new EmojiSegment(text, imageUrl));
    }

    public void addText(String text) {
        this.segments.add(new TextSegment(text));
    }

    public String getRawString() {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < this.segments.size(); i++) {
            if (this.segments.get(i) instanceof EmojiSegment) {
                arrayList.add(((EmojiSegment) this.segments.get(i)).imageUrl);
            } else if (this.segments.get(i) instanceof TextSegment) {
                arrayList.add(((TextSegment) this.segments.get(i)).text);
            }
        }
        return StringUtils.join(arrayList, ",");
    }

    public String getDisplayString() {
        StringBuilder sb2 = new StringBuilder();
        for (int i = 0; i < this.segments.size(); i++) {
            if (this.segments.get(i) instanceof EmojiSegment) {
                sb2.append(((EmojiSegment) this.segments.get(i)).text);
            } else if (this.segments.get(i) instanceof TextSegment) {
                sb2.append(((TextSegment) this.segments.get(i)).text);
            }
        }
        return sb2.toString();
    }

    public void appendToBuilder(SpannableTextViewStringBuilder spannableTextViewStringBuilder, int size) {
        for (int i = 0; i < this.segments.size(); i++) {
            if (this.segments.get(i) instanceof EmojiSegment) {
                String imageUrl = ((EmojiSegment) this.segments.get(i)).imageUrl;
                spannableTextViewStringBuilder.appendWithSpans(imageUrl, new EfficientImageSpan(imageUrl, size));
            } else if (this.segments.get(i) instanceof TextSegment) {
                spannableTextViewStringBuilder.append(((TextSegment) this.segments.get(i)).text);
            }
        }
    }
}
