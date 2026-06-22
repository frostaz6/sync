package com.resync.app.ui.fragments.sidebar;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.MaterialPostsHelper;
import com.resync.app.R;
import com.resync.app.RedditApplication;
import com.resync.app.ui.fragments.RedditFragment;
import com.resync.app.singleton.AccountSingleton;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet;
import com.resync.app.ui.views.core.MaterialRow;
import com.resync.app.ui.views.core.SheetHeaderView;
import com.resync.app.helpers.ui.PopupMenuHelper;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.helpers.ui.SubredditArrayList;
import java.util.ArrayList;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.helpers.NightHelper;
import com.resync.app.dialogs.NewMultiInputDialogBottomSheet;
import com.resync.app.dialogs.RemoveMultiAlertDialogBottomSheet;
import com.resync.app.util.Logger;
import com.resync.app.net.oauth.OAuthMultisInnerRequest;
import com.resync.app.net.oauth.OAuthSubredditInfoRequest;

/* JADX INFO: loaded from: classes2.dex */
public class MultiAboutFragment extends RedditFragment {

    @BindView(R.id.header)
    SheetHeaderView mHeader;

    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    private String subreddit;

    /* JADX INFO: renamed from: s0, reason: collision with root package name */
    private ArrayList<AbstractSelectionDialogBottomSheet.RemovableSubredditItem> items = new ArrayList<>();

    class MultiHolder extends RecyclerView.ViewHolder {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        AbstractSelectionDialogBottomSheet.RemovableSubredditItem item;

        @BindView(R.id.abstract_alert_dialog_bottom_sheet_item_indicator)
        AppCompatImageButton mIndicator;

        @BindView(R.id.abstract_alert_dialog_bottom_sheet_item_row)
        MaterialRow mRow;

        public MultiHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.mRow.applyColorsAuto();
        }

        public void bind(AbstractSelectionDialogBottomSheet.RemovableSubredditItem subredditItem) {
            this.item = subredditItem;
            this.mRow.showSubredditIcon();
            this.mRow.setTitle(subredditItem.title);
            this.mRow.setSubreddit(subredditItem.getSubreddit());
            this.mIndicator.setVisibility(0);
            this.mIndicator.setImageTintList(ColorStateList.valueOf(NightHelper.isNight() ? -8947849 : -6645094));
            this.mIndicator.setImageResource(R.drawable.ic_more_vert_white_24dp);
            this.mIndicator.setEnabled(true);
        }

        public MaterialRow getRow() {
            return this.mRow;
        }

        @OnClick
        public void onIndicatorClicked(View view) {
            MultiAboutFragment.this.showMenu(view, this.item);
        }
    }

    public static class MultiHolder_ViewBinding implements Unbinder {
        private MultiHolder target;
        private View indicatorClickView;

        /* JADX INFO: compiled from: MultiAboutFragment$MultiHolder_ViewBinding.java */
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

    class MoreClickListener implements View.OnClickListener {

        /* JADX INFO: renamed from: com.resync.app.ui.fragments.sidebar.MultiAboutFragment$a$a, reason: collision with other inner class name */
        class MenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            final /* synthetic */ MenuItem addItem;

            MenuItemClickListener(MenuItem menuItem) {
                this.addItem = menuItem;
            }

            @Override // androidx.appcompat.widget.PopupMenu.OnMenuItemClickListener
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (!menuItem.equals(this.addItem)) {
                    return true;
                }
                MultiAboutFragment.this.showAddDialog();
                return true;
            }
        }

        MoreClickListener() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            PopupMenu popupMenuC = PopupMenuHelper.create(view, 80);
            popupMenuC.setOnMenuItemClickListener(new MenuItemClickListener(popupMenuC.getMenu().add("Add subreddit")));
            popupMenuC.show();
        }
    }

    class MultisListener implements Response.Listener<String[]> {
        MultisListener() {
        }

        @Override // com.android.volley.Response.Listener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public void onResponse(String[] strArr) {
            if (MultiAboutFragment.this.isAttached()) {
                SubredditArrayList subredditArrayList = new SubredditArrayList(strArr);
                subredditArrayList.sort(SubredditArrayList.SORT_DEFAULT);
                for (String str : subredditArrayList) {
                    Logger.verbose("MULTI ITEM: " + str);
                    MultiAboutFragment.this.items.add(new AbstractSelectionDialogBottomSheet.RemovableSubredditItem(str));
                    OAuthRequestManager.sendWithAppContext(new OAuthSubredditInfoRequest(MultiAboutFragment.this.getActivity(), null, str, null, null));
                }
                MultiAboutFragment.this.setupAdapter();
            }
        }
    }

    class MultisErrorListener implements Response.ErrorListener {
        MultisErrorListener() {
        }

        @Override // com.android.volley.Response.ErrorListener
        public void onErrorResponse(VolleyError volleyError) {
            if (MultiAboutFragment.this.isAttached()) {
                StaticToast.showWithContext(MultiAboutFragment.this.getContext(), "Error loading multireddits");
            }
        }
    }

    class RemoveMenuListener implements PopupMenu.OnMenuItemClickListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ AbstractSelectionDialogBottomSheet.RemovableSubredditItem item;

        RemoveMenuListener(AbstractSelectionDialogBottomSheet.RemovableSubredditItem subredditItem) {
            this.item = subredditItem;
        }

        @Override // androidx.appcompat.widget.PopupMenu.OnMenuItemClickListener
        public boolean onMenuItemClick(MenuItem menuItem) {
            Bundle bundleK4 = RemoveMultiAlertDialogBottomSheet.buildArguments(MaterialPostsHelper.extractMultiredditName(MultiAboutFragment.this.subreddit), this.item);
            RemoveMultiAlertDialogBottomSheet removeMultiAlertDialogBottomSheet = new RemoveMultiAlertDialogBottomSheet();
            removeMultiAlertDialogBottomSheet.setArguments(bundleK4);
            removeMultiAlertDialogBottomSheet.setTargetFragment(MultiAboutFragment.this, 0);
            removeMultiAlertDialogBottomSheet.show(MultiAboutFragment.this.getChildFragmentManager(), "MultiredditAsyncSelectionDialogBottomSheet");
            return true;
        }
    }

    class MultisAdapter extends RecyclerView.Adapter<MultiHolder> {

        class RowClickListener implements View.OnClickListener {

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            final /* synthetic */ int position;

            RowClickListener(int position) {
                this.position = position;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                MultiAboutFragment multiAboutFragment = MultiAboutFragment.this;
                multiAboutFragment.openMulti((AbstractSelectionDialogBottomSheet.RemovableSubredditItem) multiAboutFragment.items.get(this.position));
            }
        }

        public MultisAdapter() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /* JADX INFO: renamed from: N, reason: merged with bridge method [inline-methods] */
        public void onBindViewHolder(MultiHolder multiHolder, int position) {
            multiHolder.bind((AbstractSelectionDialogBottomSheet.RemovableSubredditItem) MultiAboutFragment.this.items.get(position));
            multiHolder.getRow().setOnClickListener(new RowClickListener(position));
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /* JADX INFO: renamed from: O, reason: merged with bridge method [inline-methods] */
        public MultiHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            return MultiAboutFragment.this.new MultiHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.abstract_selection_dialog_bottom_sheet_item, viewGroup, false));
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /* JADX INFO: renamed from: n */
        public int getItemCount() {
            return MultiAboutFragment.this.items.size();
        }
    }

    public static MultiAboutFragment newInstance(String str) {
        MultiAboutFragment multiAboutFragment = new MultiAboutFragment();
        Bundle bundle = new Bundle();
        bundle.putString("subreddit", str);
        multiAboutFragment.setArguments(bundle);
        return multiAboutFragment;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setupAdapter() {
        this.mRecycler.setAdapter(new MultisAdapter());
        this.mRecycler.getAdapter().notifyDataSetChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showAddDialog() {
        if (getChildFragmentManager().findFragmentByTag("NewMultiInputDialogBottomSheet") == null) {
            Bundle bundle = new Bundle();
            bundle.putString(FragmentUtils.keySubreddit(), MaterialPostsHelper.extractMultiredditName(this.subreddit));
            NewMultiInputDialogBottomSheet newMultiInputDialogBottomSheet = new NewMultiInputDialogBottomSheet();
            newMultiInputDialogBottomSheet.setArguments(bundle);
            newMultiInputDialogBottomSheet.setTargetFragment(this, 0);
            newMultiInputDialogBottomSheet.show(getChildFragmentManager(), "NewMultiInputDialogBottomSheet");
        }
    }

    private void load() {
        String str;
        String str2;
        String strReplaceFirst = this.subreddit.replaceFirst("multi_", "");
        String username = AccountSingleton.getInstance().getUsername();
        if (strReplaceFirst.contains("/m/")) {
            String[] strArrSplit = strReplaceFirst.split("/m/");
            String str3 = strArrSplit[1];
            String str4 = strArrSplit[0];
            getView().findViewById(R.id.section_header_view_more).setVisibility(8);
            str2 = str4;
            str = str3;
        } else {
            str = strReplaceFirst;
            str2 = username;
        }
        OAuthRequestManager.sendWithAppContext(new OAuthMultisInnerRequest(RedditApplication.getAppContext(), str2, str, new MultisListener(), new MultisErrorListener()));
    }

    public void openMulti(AbstractSelectionDialogBottomSheet.RemovableSubredditItem subredditItem) {
        com.resync.app.util.links.LinkHandler.openMulti(getActivity(), subredditItem.getTitle());
    }

    public void showMenu(View view, AbstractSelectionDialogBottomSheet.RemovableSubredditItem subredditItem) {
        PopupMenu popupMenuC = PopupMenuHelper.create(view, 5);
        popupMenuC.getMenu().add("Remove");
        popupMenuC.setOnMenuItemClickListener(new RemoveMenuListener(subredditItem));
        popupMenuC.show();
    }

    public final void removeItem(AbstractSelectionDialogBottomSheet.RemovableSubredditItem subredditItem) {
        if (subredditItem == null) {
            throw new RuntimeException("Null item");
        }
        this.items.remove(subredditItem);
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
        this.mHeader.setDragButtonVisible(false);
        this.mHeader.setDividerVisible(true);
        this.mHeader.setTitle(MaterialPostsHelper.extractMultiredditName(this.subreddit));
        this.mHeader.setMoreVisible(true);
        this.mHeader.setMoreClickListener(new MoreClickListener());
        load();
    }

    public final void addItem(AbstractSelectionDialogBottomSheet.RemovableSubredditItem subredditItem) {
        if (subredditItem == null) {
            throw new RuntimeException("Null item");
        }
        this.items.add(subredditItem);
        this.mRecycler.getAdapter().notifyDataSetChanged();
    }
}
