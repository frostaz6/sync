package com.resync.app.ui.fragments.preferences.filters;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.resync.app.R;
import com.resync.app.RedditApplication;
import com.resync.app.ui.fragments.RedditFragment;
import com.resync.app.ui.views.core.MaterialYouToolbar;
import com.resync.app.helpers.ui.AlertDialogHelper;
import com.resync.app.helpers.ui.MonetUtil;
import java.util.ArrayList;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.singletons.StyleSingleton;
import com.resync.app.helpers.FilterHelper;
import com.resync.app.helpers.NightHelper;
import com.resync.app.helpers.ThemeHelper;
import com.resync.app.helpers.TypefaceManager;

/* JADX INFO: loaded from: classes2.dex */
public class FilterFragment extends RedditFragment {

    @BindView(R.id.fragment_filter_list)
    ListView mListView;
    private int mode;

    /* JADX INFO: renamed from: s0, reason: collision with root package name */
    private ArrayList<String> filters;
    private FilterAdapter adapter;

    class CloseClickListener implements View.OnClickListener {
        CloseClickListener() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            FilterFragment.this.getActivity().finish();
        }
    }

    class FilterMenuListener implements Toolbar.OnMenuItemClickListener {

        class ClearAllClickListener implements DialogInterface.OnClickListener {
            ClearAllClickListener() {
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int which) {
                FilterHelper.getInstance().clear(FilterFragment.this.mode);
                FilterFragment.this.filters = FilterHelper.getInstance().getFilterList(FilterFragment.this.mode);
                FilterFragment.this.adapter.notifyDataSetChanged();
            }
        }

        /* JADX INFO: renamed from: com.resync.app.ui.fragments.preferences.filters.FilterFragment$b$b, reason: collision with other inner class name */
        class EditorActionListener implements TextView.OnEditorActionListener {
            final /* synthetic */ EditText editText;
            final /* synthetic */ androidx.appcompat.app.AlertDialog dialog;

            EditorActionListener(EditText editText, androidx.appcompat.app.AlertDialog dialog) {
                this.editText = editText;
                this.dialog = dialog;
            }

            @Override // android.widget.TextView.OnEditorActionListener
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId != 2) {
                    return false;
                }
                String string = this.editText.getText().toString();
                if (!TextUtils.isEmpty(string)) {
                    FilterFragment.this.addFilter(string);
                }
                this.dialog.dismiss();
                return true;
            }
        }

        class AddShowListener implements DialogInterface.OnShowListener {
            final /* synthetic */ androidx.appcompat.app.AlertDialog dialog;
            final /* synthetic */ EditText editText;

            class AddButtonClickListener implements View.OnClickListener {
                AddButtonClickListener() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    String string = AddShowListener.this.editText.getText().toString();
                    if (!TextUtils.isEmpty(string)) {
                        FilterFragment.this.addFilter(string);
                    }
                    AddShowListener.this.dialog.dismiss();
                }
            }

            AddShowListener(androidx.appcompat.app.AlertDialog dialog, EditText editText) {
                this.dialog = dialog;
                this.editText = editText;
            }

            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface dialogInterface) {
                this.dialog.getButton(-1).setOnClickListener(new AddButtonClickListener());
            }
        }

        FilterMenuListener() {
        }

        @Override // androidx.appcompat.widget.Toolbar.OnMenuItemClickListener
        public boolean onMenuItemClick(MenuItem menuItem) {
            if (menuItem.getItemId() == R.id.menu_remove_all) {
                AlertDialogHelper.newBuilder(FilterFragment.this.getContext()).setTitle("Remove all filters?").setPositiveButton("Remove all", new ClearAllClickListener()).setNegativeButton("Cancel", null).show();
            }
            if (menuItem.getItemId() == R.id.menu_add) {
                View dialogView = View.inflate(FilterFragment.this.getActivity(), R.layout.fragment_filter_add, null);
                EditText editText = (EditText) dialogView.findViewById(R.id.filter_add);
                editText.setTextColor(StyleSingleton.getTextColorAuto(RedditApplication.getAppContext(), false));
                editText.setHintTextColor(MonetUtil.getSecondaryTextColorAuto());
                androidx.appcompat.app.AlertDialog dialog = AlertDialogHelper.newBuilder(FilterFragment.this.getContext()).setTitle("Add filter").setPositiveButton("Filter", null).setNegativeButton("Cancel", null).setCancelable(true).setView(dialogView).create();
                dialog.getWindow().setSoftInputMode(20);
                editText.setOnEditorActionListener(new EditorActionListener(editText, dialog));
                dialog.setOnShowListener(new AddShowListener(dialog, editText));
                dialog.show();
            }
            return true;
        }
    }

    class FilterItemClickListener implements AdapterView.OnItemClickListener {

        class RemoveClickListener implements DialogInterface.OnClickListener {

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            final /* synthetic */ int position;

            RemoveClickListener(int position) {
                this.position = position;
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int which) {
                FilterFragment.this.removeFilter((String) FilterFragment.this.filters.get(this.position));
            }
        }

        FilterItemClickListener() {
        }

        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            AlertDialog.Builder builder = AlertDialogHelper.newBuilder(FilterFragment.this.getContext());
            builder.setItems(new String[]{"Remove"}, new RemoveClickListener(position));
            androidx.appcompat.app.AlertDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
        }
    }

    class FilterAdapter extends BaseAdapter {
        FilterAdapter() {
        }

        @Override // android.widget.Adapter
        public int getCount() {
            return FilterFragment.this.filters.size();
        }

        @Override // android.widget.Adapter
        public Object getItem(int position) {
            return null;
        }

        @Override // android.widget.Adapter
        public long getItemId(int position) {
            return 0L;
        }

        @Override // android.widget.Adapter
        public View getView(int position, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(FilterFragment.this.getActivity()).inflate(R.layout.fragment_filter_row, (ViewGroup) null, false);
                TextView textView = (TextView) view.findViewById(R.id.fragment_filter_row_title);
                textView.setTextColor(NightHelper.isNight() ? -5526613 : -10066330);
                textView.setTypeface(TypefaceManager.getMedium());
                ThemeHelper.applyButtonBackground(FilterFragment.this.getActivity(), view);
            }
            ((TextView) view.findViewById(R.id.fragment_filter_row_title)).setText((CharSequence) FilterFragment.this.filters.get(position));
            return view;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addFilter(String str) {
        String strTrim = str.trim();
        int mode = this.mode;
        if (mode != 0 && mode != 4) {
            strTrim = strTrim.replaceAll(StringUtils.SPACE, "");
        }
        if (this.mode == 1) {
            strTrim = strTrim.toLowerCase(Locale.ENGLISH);
        }
        FilterHelper.getInstance().add(this.mode, strTrim);
        this.filters = FilterHelper.getInstance().getFilterList(this.mode);
        this.adapter.notifyDataSetChanged();
    }

    private void loadFilters() {
        this.filters = FilterHelper.getInstance().getFilterList(this.mode);
    }

    public static FilterFragment newInstance(int mode) {
        FilterFragment filterFragment = new FilterFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("mode", mode);
        filterFragment.setArguments(bundle);
        return filterFragment;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeFilter(String str) {
        FilterHelper.getInstance().removeFilter(this.mode, str);
        this.filters = FilterHelper.getInstance().getFilterList(this.mode);
        this.adapter.notifyDataSetChanged();
    }

    private String getTitle() {
        int mode = this.mode;
        if (1 == mode) {
            return "Domain filters";
        }
        if (mode == 0) {
            return "Keyword filters";
        }
        if (2 == mode) {
            return "Subreddit filters";
        }
        if (3 == mode) {
            return "User filters";
        }
        if (4 == mode) {
            return "Flair filters";
        }
        throw new RuntimeException("Invalid mode");
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View viewInflate = layoutInflater.inflate(getLayoutResId(), (ViewGroup) null);
        ButterKnife.bind(this, viewInflate);
        return viewInflate;
    }

    @Override // com.resync.app.ui.interfaces.RedditFragmentInterface
    public int getLayoutResId() {
        return R.layout.fragment_filter;
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.mode = getArguments().getInt("mode");
        MaterialYouToolbar materialYouToolbar = (MaterialYouToolbar) view.findViewById(R.id.toolbar);
        this.toolbar = materialYouToolbar;
        materialYouToolbar.setNavigationIcon(R.drawable.ic_close_activity_white_24dp);
        this.toolbar.setTitle(getTitle());
        this.toolbar.inflateMenu(R.menu.filters);
        this.toolbar.refreshTextColors();
        this.toolbar.setNavigationOnClickListener(new CloseClickListener());
        this.toolbar.setOnMenuItemClickListener(new FilterMenuListener());
        loadFilters();
        FilterAdapter adapter = new FilterAdapter();
        this.adapter = adapter;
        this.mListView.setAdapter((ListAdapter) adapter);
        this.mListView.setOnItemClickListener(new FilterItemClickListener());
    }
}
