package com.resync.app.ui.fragment_dialogs.bottom;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.resync.app.R;
import com.resync.app.RedditApplication;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.activities.PreferencesActivity;
import com.resync.app.ui.views.core.SheetHeaderView;
import com.squareup.otto.Subscribe;
import com.resync.app.helpers.ui.PopupMenuHelper;
import com.resync.app.widgets.recycler.CommentActionItemMoveCallback;
import com.resync.app.dialogs.TranslateAllAsyncAlertDialogBottomSheet;
import com.resync.app.events.CloseActionsBottomsheetEvent;
import com.resync.app.events.CommentSearchEvent;
import com.resync.app.events.RefreshEvent;
import com.resync.app.events.ReplyClickedEvent;
import com.resync.app.events.StartLiveModeEvent;
import com.resync.app.events.ToggleCollapseEvent;
import com.resync.app.events.ViewAllImagesEvent;
import com.resync.app.events.ViewOtherDiscussionsEvent;
import com.resync.app.events.ViewSidebarEvent;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.widgets.MoreActionsItemView;
import com.resync.app.dialogs.CustomBottomSheetDialogFragment;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.helpers.CommentActionsManager;
import com.resync.app.dialogs.CommentSortDialogBottomSheet;
import com.resync.app.dialogs.RecentlyViewedSelectionDialogBottomSheet;

/* JADX INFO: loaded from: classes.dex */
public class CommentActionsBottomSheetFragment extends CustomBottomSheetDialogFragment {

    @BindView(R.id.header)
    SheetHeaderView headerView;

    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    public class CommentActionHolder extends RecyclerView.ViewHolder {

        private int actionId;

        public CommentActionHolder(View view) {
            super(view);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void onActionClicked(int actionId, View view) {
            CommentActionsBottomSheetFragment commentActionsBottomSheetFragment = CommentActionsBottomSheetFragment.this;
            commentActionsBottomSheetFragment.performCommentAction(commentActionsBottomSheetFragment.getActivity(), CommentActionsBottomSheetFragment.this.getParentFragmentManager(), CommentActionsBottomSheetFragment.this.getSubreddit(), actionId, CommentActionsBottomSheetFragment.this);
            CommentActionsBottomSheetFragment.this.dismiss();
        }

        public void bind(final int actionId) {
            this.actionId = actionId;
            ((MoreActionsItemView) this.itemView).setLabel(CommentActionsManager.getLabel(actionId));
            ((MoreActionsItemView) this.itemView).setIcon(CommentActionsManager.getIconRes(actionId));
            this.itemView.setOnClickListener(new View.OnClickListener() { // from class: s9.e
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    CommentActionsBottomSheetFragment.CommentActionHolder.this.onActionClicked(actionId, view);
                }
            });
        }

        public int getActionId() {
            return this.actionId;
        }
    }

    class CommentActionsAdapter extends RecyclerView.Adapter<CommentActionHolder> implements CommentActionItemMoveCallback.Callback {
        public CommentActionsAdapter() {
            setHasStableIds(true);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /* JADX INFO: renamed from: N, reason: merged with bridge method [inline-methods] */
        public void onBindViewHolder(CommentActionHolder holder, int position) {
            holder.bind(CommentActionsManager.getInstance().getActions().get(position).intValue());
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /* JADX INFO: renamed from: O, reason: merged with bridge method [inline-methods] */
        public CommentActionHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            return CommentActionsBottomSheetFragment.this.new CommentActionHolder(new MoreActionsItemView(viewGroup.getContext()));
        }

        @Override // com.resync.app.widgets.recycler.CommentActionItemMoveCallback.Callback
        public void onItemMove(int fromAction, int toAction) {
            int fromIndex = CommentActionsManager.getInstance().getActions().indexOf(Integer.valueOf(fromAction));
            int toIndex = CommentActionsManager.getInstance().getActions().indexOf(Integer.valueOf(toAction));
            CommentActionsManager.getInstance().getActions().remove(fromIndex);
            CommentActionsManager.getInstance().getActions().add(toIndex, Integer.valueOf(fromAction));
            int newIndex = CommentActionsManager.getInstance().getActions().indexOf(Integer.valueOf(fromAction));
            CommentActionsManager.getInstance().save();
            notifyItemMoved(fromIndex, newIndex);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /* JADX INFO: renamed from: n */
        public int getItemCount() {
            return CommentActionsManager.getInstance().getActions().size();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /* JADX INFO: renamed from: o */
        public long getItemId(int position) {
            return CommentActionsManager.getInstance().getActions().get(position).intValue();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean onMenuVisibilityClick(MenuItem menuItem, MenuItem menuItem2, MenuItem menuItem3, MenuItem menuItem4, MenuItem menuItem5, MenuItem menuItem6, MenuItem menuItem7, MenuItem menuItem8, MenuItem menuItem9, MenuItem menuItem10, MenuItem menuItem11, MenuItem menuItem12, MenuItem menuItem13, MenuItem menuItem14) {
        if (menuItem14.equals(menuItem)) {
            CommentActionsManager.getInstance().reset();
            this.recyclerView.getAdapter().notifyDataSetChanged();
            return true;
        }
        if (menuItem14.equals(menuItem2)) {
            boolean enabled = !SettingsSingleton.getSettings().comment_action_reply;
            SettingsSingleton.getSettings().comment_action_reply = enabled;
            SettingsSingleton.getInstance().putBoolean("comment_action_reply", enabled);
        } else if (menuItem14.equals(menuItem3)) {
            boolean enabled = !SettingsSingleton.getSettings().comment_action_refresh;
            SettingsSingleton.getSettings().comment_action_refresh = enabled;
            SettingsSingleton.getInstance().putBoolean("comment_action_refresh", enabled);
        } else if (menuItem14.equals(menuItem4)) {
            boolean enabled = !SettingsSingleton.getSettings().comment_action_other;
            SettingsSingleton.getSettings().comment_action_other = enabled;
            SettingsSingleton.getInstance().putBoolean("comment_action_other", enabled);
        } else if (menuItem14.equals(menuItem5)) {
            boolean enabled = !SettingsSingleton.getSettings().comment_action_about;
            SettingsSingleton.getSettings().comment_action_about = enabled;
            SettingsSingleton.getInstance().putBoolean("comment_action_about", enabled);
        } else if (menuItem14.equals(menuItem6)) {
            boolean enabled = !SettingsSingleton.getSettings().comment_action_images;
            SettingsSingleton.getSettings().comment_action_images = enabled;
            SettingsSingleton.getInstance().putBoolean("comment_action_images", enabled);
        } else if (menuItem14.equals(menuItem7)) {
            boolean enabled = !SettingsSingleton.getSettings().comment_action_collapse_all;
            SettingsSingleton.getSettings().comment_action_collapse_all = enabled;
            SettingsSingleton.getInstance().putBoolean("comment_action_collapse_all", enabled);
        } else if (menuItem14.equals(menuItem8)) {
            boolean enabled = !SettingsSingleton.getSettings().comment_action_sports_mode;
            SettingsSingleton.getSettings().comment_action_sports_mode = enabled;
            SettingsSingleton.getInstance().putBoolean("comment_action_sports_mode", enabled);
        } else if (menuItem14.equals(menuItem9)) {
            boolean enabled = !SettingsSingleton.getSettings().comment_action_settings;
            SettingsSingleton.getSettings().comment_action_settings = enabled;
            SettingsSingleton.getInstance().putBoolean("comment_action_settings", enabled);
        } else if (menuItem14.equals(menuItem10)) {
            boolean enabled = !SettingsSingleton.getSettings().comment_action_sort;
            SettingsSingleton.getSettings().comment_action_sort = enabled;
            SettingsSingleton.getInstance().putBoolean("comment_action_sort", enabled);
        } else if (menuItem14.equals(menuItem11)) {
            boolean enabled = !SettingsSingleton.getSettings().comment_action_search;
            SettingsSingleton.getSettings().comment_action_search = enabled;
            SettingsSingleton.getInstance().putBoolean("comment_action_search", enabled);
        } else if (menuItem14.equals(menuItem12)) {
            boolean enabled = !SettingsSingleton.getSettings().comment_action_recents;
            SettingsSingleton.getSettings().comment_action_recents = enabled;
            SettingsSingleton.getInstance().putBoolean("comment_action_recents", enabled);
        } else if (menuItem14.equals(menuItem13)) {
            boolean enabled = !SettingsSingleton.getSettings().comment_action_translate;
            SettingsSingleton.getSettings().comment_action_translate = enabled;
            SettingsSingleton.getInstance().putBoolean("comment_action_translate", enabled);
        }
        CommentActionsManager.getInstance().refreshFromSettings();
        this.recyclerView.getAdapter().notifyDataSetChanged();
        return true;
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public final boolean shouldPadTopSheet() {
        return false;
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public boolean isInitiallyExpanded() {
        return true;
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public boolean isTopSheet() {
        return this.topSheet;
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetInterface
    public int getLayoutRes() {
        return R.layout.dialog_fragment_comment_actions_bottom_sheet;
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        ButterKnife.bind(this, view);
        this.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        CommentActionsAdapter adapter = new CommentActionsAdapter();
        this.recyclerView.setAdapter(adapter);
        new ItemTouchHelper(new CommentActionItemMoveCallback(adapter)).attachToRecyclerView(this.recyclerView);
        this.headerView.setDragButtonVisible(!isTopSheet());
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    protected boolean dismissWithAnimation() {
        return false;
    }

    @Subscribe
    public void onClose(CloseActionsBottomsheetEvent closeActionsBottomsheetEvent) {
        dismiss();
    }

    @OnClick
    public void showMore(View view) {
        PopupMenu popupMenuC = PopupMenuHelper.create(view, 5);
        SubMenu subMenuAddSubMenu = popupMenuC.getMenu().addSubMenu("Action visibility");
        final MenuItem menuItemAdd = subMenuAddSubMenu.add("About");
        menuItemAdd.setCheckable(true);
        menuItemAdd.setChecked(SettingsSingleton.getSettings().comment_action_about);
        final MenuItem menuItemAdd2 = subMenuAddSubMenu.add("Collapse all");
        menuItemAdd2.setCheckable(true);
        menuItemAdd2.setChecked(SettingsSingleton.getSettings().comment_action_collapse_all);
        final MenuItem menuItemAdd3 = subMenuAddSubMenu.add("Other discussions");
        menuItemAdd3.setCheckable(true);
        menuItemAdd3.setChecked(SettingsSingleton.getSettings().comment_action_other);
        final MenuItem menuItemAdd4 = subMenuAddSubMenu.add("Recents");
        menuItemAdd4.setCheckable(true);
        menuItemAdd4.setChecked(SettingsSingleton.getSettings().comment_action_recents);
        final MenuItem menuItemAdd5 = subMenuAddSubMenu.add("Refresh");
        menuItemAdd5.setCheckable(true);
        menuItemAdd5.setChecked(SettingsSingleton.getSettings().comment_action_refresh);
        final MenuItem menuItemAdd6 = subMenuAddSubMenu.add("Reply");
        menuItemAdd6.setCheckable(true);
        menuItemAdd6.setChecked(SettingsSingleton.getSettings().comment_action_reply);
        final MenuItem menuItemAdd7 = subMenuAddSubMenu.add("Settings");
        menuItemAdd7.setCheckable(true);
        menuItemAdd7.setChecked(SettingsSingleton.getSettings().comment_action_settings);
        final MenuItem menuItemAdd8 = subMenuAddSubMenu.add("Search");
        menuItemAdd8.setCheckable(true);
        menuItemAdd8.setChecked(SettingsSingleton.getSettings().comment_action_search);
        final MenuItem menuItemAdd9 = subMenuAddSubMenu.add("Sort");
        menuItemAdd9.setCheckable(true);
        menuItemAdd9.setChecked(SettingsSingleton.getSettings().comment_action_sort);
        final MenuItem menuItemAdd10 = subMenuAddSubMenu.add("Sports mode");
        menuItemAdd10.setCheckable(true);
        menuItemAdd10.setChecked(SettingsSingleton.getSettings().comment_action_sports_mode);
        final MenuItem menuItemAdd11 = subMenuAddSubMenu.add("Translate");
        menuItemAdd11.setCheckable(true);
        menuItemAdd11.setChecked(SettingsSingleton.getSettings().comment_action_translate);
        final MenuItem menuItemAdd12 = subMenuAddSubMenu.add("View images");
        menuItemAdd12.setCheckable(true);
        menuItemAdd12.setChecked(SettingsSingleton.getSettings().comment_action_images);
        final MenuItem menuItemAdd13 = popupMenuC.getMenu().add("Reset order");
        popupMenuC.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() { // from class: s9.d
            @Override // androidx.appcompat.widget.PopupMenu.OnMenuItemClickListener
            public final boolean onMenuItemClick(MenuItem menuItem) {
                return CommentActionsBottomSheetFragment.this.onMenuVisibilityClick(menuItemAdd13, menuItemAdd6, menuItemAdd5, menuItemAdd3, menuItemAdd, menuItemAdd12, menuItemAdd2, menuItemAdd10, menuItemAdd7, menuItemAdd9, menuItemAdd8, menuItemAdd4, menuItemAdd11, menuItem);
            }
        });
        popupMenuC.show();
    }

    public void performCommentAction(FragmentActivity fragmentActivity, FragmentManager fragmentManager, String str, int actionCode, CommentActionsBottomSheetFragment commentActionsBottomSheetFragment) {
        if (actionCode == 0) {
            BusSingleton.getBus().post(new ReplyClickedEvent());
            return;
        }
        if (1 == actionCode) {
            BusSingleton.getBus().post(new RefreshEvent());
            return;
        }
        if (2 == actionCode) {
            BusSingleton.getBus().post(new ViewOtherDiscussionsEvent());
            return;
        }
        if (3 == actionCode) {
            BusSingleton.getBus().post(new ViewSidebarEvent());
            return;
        }
        if (4 == actionCode) {
            BusSingleton.getBus().post(new ViewAllImagesEvent());
            return;
        }
        if (5 == actionCode) {
            BusSingleton.getBus().post(new ToggleCollapseEvent());
            return;
        }
        if (6 == actionCode) {
            BusSingleton.getBus().post(new StartLiveModeEvent());
            return;
        }
        if (7 == actionCode) {
            Intent intent = new Intent(fragmentActivity, (Class<?>) PreferencesActivity.class);
            intent.putExtra("mode", RedditApplication.getInteger(R.integer.COMMENTS));
            fragmentActivity.startActivity(intent);
        } else {
            if (8 == actionCode) {
                FragmentUtils.showWithPost(CommentSortDialogBottomSheet.class, fragmentManager, getPost());
                return;
            }
            if (9 == actionCode) {
                BusSingleton.getBus().post(new CommentSearchEvent());
            } else if (10 == actionCode) {
                FragmentUtils.show(RecentlyViewedSelectionDialogBottomSheet.class, fragmentManager);
            } else if (11 == actionCode) {
                FragmentUtils.showWithPostId(TranslateAllAsyncAlertDialogBottomSheet.class, fragmentManager, getPost().getId());
            }
        }
    }
}
