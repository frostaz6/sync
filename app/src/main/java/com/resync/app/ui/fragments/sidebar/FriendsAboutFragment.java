package com.resync.app.ui.fragments.sidebar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.resync.app.R;
import com.resync.app.RedditApplication;
import com.resync.app.ui.fragments.RedditFragment;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet;
import com.resync.app.ui.views.core.MaterialRow;
import com.resync.app.ui.views.core.SheetHeaderView;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.helpers.ui.SubredditArrayList;
import java.util.ArrayList;
import java.util.Iterator;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.net.oauth.OAuthFriendsRequest;

/* JADX INFO: loaded from: classes2.dex */
public class FriendsAboutFragment extends RedditFragment {

    @BindView(R.id.header)
    SheetHeaderView mHeader;

    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    private String subreddit;

    /* JADX INFO: renamed from: s0, reason: collision with root package name */
    private ArrayList<AbstractSelectionDialogBottomSheet.UsernameItem> items = new ArrayList<>();

    class MultiHolder extends RecyclerView.ViewHolder {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        AbstractSelectionDialogBottomSheet.UsernameItem item;

        @BindView(R.id.abstract_alert_dialog_bottom_sheet_item_indicator)
        AppCompatImageButton mIndicator;

        @BindView(R.id.abstract_alert_dialog_bottom_sheet_item_row)
        MaterialRow mRow;

        public MultiHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.mRow.applyColorsAuto();
        }

        public void bind(AbstractSelectionDialogBottomSheet.UsernameItem usernameItem) {
            this.item = usernameItem;
            this.mRow.showUserIcon();
            this.mRow.setTitle(usernameItem.title);
            this.mRow.setUsername(usernameItem.getUsername());
            this.mIndicator.setVisibility(8);
        }

        public MaterialRow getRow() {
            return this.mRow;
        }

        @OnClick
        public void onIndicatorClicked(View view) {
            FriendsAboutFragment.this.openProfile(this.item);
        }
    }

    public static class MultiHolder_ViewBinding implements Unbinder {
        private MultiHolder target;
        private View indicatorClickView;

        /* JADX INFO: compiled from: FriendsAboutFragment$MultiHolder_ViewBinding.java */
        static class ClickListener extends com.resync.app.util.DebouncingOnClickListener {

            /* JADX INFO: renamed from: q, reason: collision with root package name */
            final /* synthetic */ MultiHolder holder;

            ClickListener(MultiHolder multiHolder) {
                this.holder = multiHolder;
            }

            @Override // com.resync.app.util.DebouncingOnClickListener
            public void doClick(View view) {
                this.holder.onIndicatorClicked(view);
            }
        }

        public MultiHolder_ViewBinding(MultiHolder multiHolder, View view) {
            this.target = multiHolder;
            multiHolder.mRow = (MaterialRow) Utils.findRequiredViewAsType(view, R.id.abstract_alert_dialog_bottom_sheet_item_row, "field 'mRow'", MaterialRow.class);
            View viewC = Utils.findRequiredView(view, R.id.abstract_alert_dialog_bottom_sheet_item_indicator, "field 'mIndicator' and method 'onIndicatorClicked'");
            multiHolder.mIndicator = (AppCompatImageButton) Utils.castView(viewC, R.id.abstract_alert_dialog_bottom_sheet_item_indicator, "field 'mIndicator'", AppCompatImageButton.class);
            this.indicatorClickView = viewC;
            viewC.setOnClickListener(new ClickListener(multiHolder));
        }
    
        @Override // butterknife.Unbinder
        public void unbind() {
            MultiHolder target = this.target;
            if (target == null) {
                throw new IllegalStateException("Bindings already cleared.");
            }
            this.target = null;
            target.mRow = null;
            target.mIndicator = null;
            View indicatorView = this.indicatorClickView;
            if (indicatorView != null) {
                indicatorView.setOnClickListener(null);
                this.indicatorClickView = null;
            }
        }
    }

    class FriendsListener implements Response.Listener<String[]> {
        FriendsListener() {
        }

        @Override // com.android.volley.Response.Listener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public void onResponse(String[] strArr) {
            if (FriendsAboutFragment.this.isAttached()) {
                SubredditArrayList subredditArrayList = new SubredditArrayList(strArr);
                subredditArrayList.sortDefault();
                Iterator<String> it = subredditArrayList.iterator();
                while (it.hasNext()) {
                    FriendsAboutFragment.this.items.add(new AbstractSelectionDialogBottomSheet.UsernameItem(it.next()));
                }
                FriendsAboutFragment.this.setupAdapter();
            }
        }
    }

    class FriendsErrorListener implements Response.ErrorListener {
        FriendsErrorListener() {
        }

        @Override // com.android.volley.Response.ErrorListener
        public void onErrorResponse(VolleyError volleyError) {
            if (FriendsAboutFragment.this.isAttached()) {
                StaticToast.showWithContext(FriendsAboutFragment.this.getContext(), "Error loading multireddits");
            }
        }
    }

    class FriendsAdapter extends RecyclerView.Adapter<MultiHolder> {

        class RowClickListener implements View.OnClickListener {

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            final /* synthetic */ int position;

            RowClickListener(int position) {
                this.position = position;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                FriendsAboutFragment friendsAboutFragment = FriendsAboutFragment.this;
                friendsAboutFragment.openProfile((AbstractSelectionDialogBottomSheet.UsernameItem) friendsAboutFragment.items.get(this.position));
            }
        }

        public FriendsAdapter() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /* JADX INFO: renamed from: N, reason: merged with bridge method [inline-methods] */
        public void onBindViewHolder(MultiHolder multiHolder, int position) {
            multiHolder.bind((AbstractSelectionDialogBottomSheet.UsernameItem) FriendsAboutFragment.this.items.get(position));
            multiHolder.getRow().setOnClickListener(new RowClickListener(position));
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /* JADX INFO: renamed from: O, reason: merged with bridge method [inline-methods] */
        public MultiHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            return FriendsAboutFragment.this.new MultiHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.abstract_selection_dialog_bottom_sheet_item, viewGroup, false));
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /* JADX INFO: renamed from: n */
        public int getItemCount() {
            return FriendsAboutFragment.this.items.size();
        }
    }

    private void loadFriends() {
        OAuthRequestManager.sendWithAppContext(new OAuthFriendsRequest(RedditApplication.getAppContext(), new FriendsListener(), new FriendsErrorListener()));
    }

    public static FriendsAboutFragment newInstance(String str) {
        FriendsAboutFragment friendsAboutFragment = new FriendsAboutFragment();
        Bundle bundle = new Bundle();
        bundle.putString("subreddit", str);
        friendsAboutFragment.setArguments(bundle);
        return friendsAboutFragment;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setupAdapter() {
        this.mRecycler.setAdapter(new FriendsAdapter());
        this.mRecycler.getAdapter().notifyDataSetChanged();
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View viewInflate = layoutInflater.inflate(getLayoutResId(), (ViewGroup) null);
        ButterKnife.bind(this, viewInflate);
        return viewInflate;
    }

    @Override // com.resync.app.ui.interfaces.RedditFragmentInterface
    public int getLayoutResId() {
        return R.layout.fragment_multi;
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.subreddit = getArguments().getString("subreddit");
        this.mHeader.setDividerVisible(true);
        this.mHeader.setTitle("Friends");
        this.mHeader.setMoreVisible(false);
        this.mHeader.setDragButtonVisible(false);
        loadFriends();
    }

    public void openProfile(AbstractSelectionDialogBottomSheet.UsernameItem usernameItem) {
        com.resync.app.util.links.LinkHandler.openProfile(getActivity(), usernameItem.getTitle());
    }
}
