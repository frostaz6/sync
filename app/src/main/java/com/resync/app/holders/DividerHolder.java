package com.resync.app.holders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.resync.app.R;
import com.resync.app.ui.views.buttons.NextButton;
import com.resync.app.ui.views.buttons.PreviousButton;
import com.resync.app.ui.views.text.BaseTextView;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.events.ScrollToBottomOfPostsEvent;
import com.resync.app.events.ScrollToTopOfPostsEvent;
import com.resync.app.model.Post;

/* JADX INFO: renamed from: ub.a */
/* JADX INFO: compiled from: DividerHolder.java */
/* JADX INFO: loaded from: classes3.dex */
public class DividerHolder extends RecyclerView.ViewHolder {

    private PreviousButton previousButton;
    private NextButton nextButton;
    private BaseTextView textView;
    private Context context;

    /* JADX INFO: renamed from: ub.a$a */
    /* JADX INFO: compiled from: DividerHolder.java */
    class ScrollToTopClickListener implements View.OnClickListener {
        ScrollToTopClickListener() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            BusSingleton.getBus().post(new ScrollToTopOfPostsEvent());
        }
    }

    /* JADX INFO: renamed from: ub.a$b */
    /* JADX INFO: compiled from: DividerHolder.java */
    class ScrollToBottomClickListener implements View.OnClickListener {
        ScrollToBottomClickListener() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            BusSingleton.getBus().post(new ScrollToBottomOfPostsEvent());
        }
    }

    /* JADX INFO: renamed from: ub.a$c */
    /* JADX INFO: compiled from: DividerHolder.java */
    class LoadingTimeLongClickListener implements View.OnLongClickListener {

        final /* synthetic */ Post post;

        LoadingTimeLongClickListener(Post post) {
            this.post = post;
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            StaticToast.showWithContext(DividerHolder.this.context, "Loading time: " + this.post.getLikes() + "ms");
            return true;
        }
    }

    private DividerHolder(Context context, View view) {
        super(view);
        this.context = context;
        this.textView = (BaseTextView) view.findViewById(R.id.divider_textview);
        this.previousButton = (PreviousButton) view.findViewById(R.id.divider_previous);
        this.nextButton = (NextButton) view.findViewById(R.id.divider_next);
        this.previousButton.setOnClickListener(new ScrollToTopClickListener());
        this.nextButton.setOnClickListener(new ScrollToBottomClickListener());
    }

    public static DividerHolder create(Context context, ViewGroup viewGroup) {
        return new DividerHolder(context, LayoutInflater.from(context).inflate(R.layout.viewholder_divider, viewGroup, false));
    }

    public void bind(Post post) {
        this.textView.setText("Page " + (post.getLevel() + 1));
        this.textView.setOnLongClickListener(new LoadingTimeLongClickListener(post));
    }
}
