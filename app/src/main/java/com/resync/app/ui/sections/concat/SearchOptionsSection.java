package com.resync.app.ui.sections.concat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.ui.sections.concat.SearchOptionsSection;
import com.resync.app.ui.views.monet.buttons.MonetActionButtons;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.events.SubmitAllSearchEvent;
import com.resync.app.events.SubmitSubredditSearchEvent;
import com.resync.app.helpers.ContextHelper;
import com.resync.app.util.TextUtils;

/* JADX INFO: loaded from: classes.dex */
public class SearchOptionsSection extends RecyclerView.Adapter<SearchOptionsSection.SearchOptionsHolder> {
    boolean visible;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static class SearchOptionsHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.monet_action_buttons)
        MonetActionButtons mMonetActionButtons;

        public SearchOptionsHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void submitSubredditSearch(String str, View view) {
            BusSingleton.getBus().post(new SubmitSubredditSearchEvent(str));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void submitAllSearch(View view) {
            BusSingleton.getBus().post(new SubmitAllSearchEvent());
        }

        public static SearchOptionsHolder create(Context context, ViewGroup viewGroup) {
            View viewInflate = LayoutInflater.from(context).inflate(R.layout.holder_search_options, viewGroup, false);
            viewInflate.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
            return new SearchOptionsHolder(viewInflate);
        }

        public void bind() {
            final String subreddit = ContextHelper.getAboutTargetSubreddit(this.itemView.getContext());
            if (TextUtils.isEmpty(subreddit)) {
                this.mMonetActionButtons.setButtonLabels("Search all", null, null);
            } else {
                this.mMonetActionButtons.setButtonLabels("Search " + subreddit, "Search all");
            }
            this.mMonetActionButtons.setListeners(new View.OnClickListener() { // from class: com.resync.app.ui.sections.concat.b
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    SearchOptionsSection.SearchOptionsHolder.submitSubredditSearch(subreddit, view);
                }
            }, new View.OnClickListener() { // from class: com.resync.app.ui.sections.concat.c
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    SearchOptionsSection.SearchOptionsHolder.submitAllSearch(view);
                }
            });
        }
    }

    public static class SearchOptionsHolder_ViewBinding implements Unbinder {
        private SearchOptionsHolder target;

        public SearchOptionsHolder_ViewBinding(SearchOptionsHolder searchOptionsHolder, View view) {
            this.target = searchOptionsHolder;
            searchOptionsHolder.mMonetActionButtons = (MonetActionButtons) Utils.findRequiredViewAsType(view, R.id.monet_action_buttons, "field 'mMonetActionButtons'", MonetActionButtons.class);
        }

        @Override
        public void unbind() {
            SearchOptionsHolder searchOptionsHolder = this.target;
            if (searchOptionsHolder == null) {
                throw new IllegalStateException("Bindings already cleared.");
            }
            this.target = null;
            searchOptionsHolder.mMonetActionButtons = null;
        }
    }

    public SearchOptionsSection() {
        setHasStableIds(true);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* JADX INFO: renamed from: N, reason: merged with bridge method [inline-methods] */
    public void onBindViewHolder(SearchOptionsHolder searchOptionsHolder, int position) {
        searchOptionsHolder.bind();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public SearchOptionsHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return SearchOptionsHolder.create(viewGroup.getContext(), viewGroup);
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* JADX INFO: renamed from: n */
    public int getItemCount() {
        return this.visible ? 1 : 0;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* JADX INFO: renamed from: o */
    public long getItemId(int position) {
        return 14L;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int position) {
        return 100;
    }
}
