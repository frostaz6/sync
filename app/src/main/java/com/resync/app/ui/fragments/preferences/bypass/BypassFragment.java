package com.resync.app.ui.fragments.preferences.bypass;

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
import com.resync.app.ui.fragments.RedditFragment;
import com.resync.app.ui.views.core.MaterialYouToolbar;
import com.resync.app.helpers.ui.AlertDialogHelper;
import java.util.ArrayList;
import java.util.Locale;
import com.resync.app.helpers.BypassHelper;
import com.resync.app.helpers.NightHelper;
import com.resync.app.helpers.ThemeHelper;
import com.resync.app.helpers.TypefaceManager;
import com.resync.app.util.links.LinkTypeHelper;

/* JADX INFO: loaded from: classes2.dex */
public class BypassFragment extends RedditFragment {

    @BindView(R.id.fragment_bypass_list)
    ListView mListView;
    private ArrayList<String> domains;

    /* JADX INFO: renamed from: s0, reason: collision with root package name */
    private DomainAdapter adapter;

    class CloseClickListener implements View.OnClickListener {
        CloseClickListener() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            BypassFragment.this.getActivity().finish();
        }
    }

    class AddMenuListener implements Toolbar.OnMenuItemClickListener {

        class AddEditorActionListener implements TextView.OnEditorActionListener {

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            final /* synthetic */ EditText editText;
            final /* synthetic */ androidx.appcompat.app.AlertDialog dialog;

            AddEditorActionListener(EditText editText, androidx.appcompat.app.AlertDialog dialog) {
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
                    BypassFragment.this.addDomain(string);
                }
                this.dialog.dismiss();
                return true;
            }
        }

        /* JADX INFO: renamed from: com.resync.app.ui.fragments.preferences.bypass.BypassFragment$b$b, reason: collision with other inner class name */
        class DialogInterfaceOnShowListenerC0083b implements DialogInterface.OnShowListener {
            final /* synthetic */ androidx.appcompat.app.AlertDialog dialog;
            final /* synthetic */ EditText editText;

            class AddButtonClickListener implements View.OnClickListener {
                AddButtonClickListener() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    String string = DialogInterfaceOnShowListenerC0083b.this.editText.getText().toString();
                    if (!TextUtils.isEmpty(string)) {
                        BypassFragment.this.addDomain(string);
                    }
                    DialogInterfaceOnShowListenerC0083b.this.dialog.dismiss();
                }
            }

            DialogInterfaceOnShowListenerC0083b(androidx.appcompat.app.AlertDialog dialog, EditText editText) {
                this.dialog = dialog;
                this.editText = editText;
            }

            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface dialogInterface) {
                this.dialog.getButton(-1).setOnClickListener(new AddButtonClickListener());
            }
        }

        AddMenuListener() {
        }

        @Override // androidx.appcompat.widget.Toolbar.OnMenuItemClickListener
        public boolean onMenuItemClick(MenuItem menuItem) {
            if (menuItem.getItemId() == R.id.menu_add) {
                View dialogView = View.inflate(BypassFragment.this.getActivity(), R.layout.fragment_bypass_add, null);
                EditText editText = (EditText) dialogView.findViewById(R.id.bypass_add);
                androidx.appcompat.app.AlertDialog dialog = AlertDialogHelper.newBuilder(BypassFragment.this.getContext()).setTitle("Add domain").setPositiveButton("Add", null).setNegativeButton("Cancel", null).setCancelable(true).setView(dialogView).create();
                dialog.getWindow().setSoftInputMode(20);
                editText.setOnEditorActionListener(new AddEditorActionListener(editText, dialog));
                dialog.setOnShowListener(new DialogInterfaceOnShowListenerC0083b(dialog, editText));
                dialog.show();
            }
            return true;
        }
    }

    class DomainItemClickListener implements AdapterView.OnItemClickListener {

        class RemoveClickListener implements DialogInterface.OnClickListener {

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            final /* synthetic */ int position;

            RemoveClickListener(int position) {
                this.position = position;
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int which) {
                BypassFragment.this.removeDomain((String) BypassFragment.this.domains.get(this.position));
            }
        }

        DomainItemClickListener() {
        }

        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            AlertDialog.Builder builder = AlertDialogHelper.newBuilder(BypassFragment.this.getContext());
            builder.setItems(new String[]{"Remove"}, new RemoveClickListener(position));
            androidx.appcompat.app.AlertDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
        }
    }

    class DomainAdapter extends BaseAdapter {
        DomainAdapter() {
        }

        @Override // android.widget.Adapter
        public int getCount() {
            return BypassFragment.this.domains.size();
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
                view = LayoutInflater.from(BypassFragment.this.getActivity()).inflate(R.layout.fragment_bypass_row, (ViewGroup) null, false);
                TextView textView = (TextView) view.findViewById(R.id.fragment_bypass_row_title);
                textView.setTextColor(NightHelper.isNight() ? -5526613 : -10066330);
                textView.setTypeface(TypefaceManager.getMedium());
                ThemeHelper.applyButtonBackground(BypassFragment.this.getActivity(), view);
            }
            ((TextView) view.findViewById(R.id.fragment_bypass_row_title)).setText((CharSequence) BypassFragment.this.domains.get(position));
            return view;
        }
    }

    public static BypassFragment newInstance() {
        return new BypassFragment();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeDomain(String str) {
        BypassHelper.getInstance().remove(str);
        this.domains = BypassHelper.getInstance().getDomains();
        this.adapter.notifyDataSetChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addDomain(String str) {
        BypassHelper.getInstance().add(LinkTypeHelper.getHost(str.trim().toLowerCase(Locale.ENGLISH)));
        this.domains = BypassHelper.getInstance().getDomains();
        this.adapter.notifyDataSetChanged();
    }

    private void loadDomains() {
        this.domains = BypassHelper.getInstance().getDomains();
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View viewInflate = layoutInflater.inflate(getLayoutResId(), (ViewGroup) null);
        ButterKnife.bind(this, viewInflate);
        return viewInflate;
    }

    @Override // com.resync.app.ui.interfaces.RedditFragmentInterface
    public int getLayoutResId() {
        return R.layout.fragment_bypass;
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        MaterialYouToolbar materialYouToolbar = (MaterialYouToolbar) view.findViewById(R.id.toolbar);
        this.toolbar = materialYouToolbar;
        materialYouToolbar.setNavigationIcon(R.drawable.ic_close_activity_white_24dp);
        this.toolbar.setTitle("Bypass domains");
        this.toolbar.inflateMenu(R.menu.bypass);
        this.toolbar.refreshTextColors();
        this.toolbar.setNavigationOnClickListener(new CloseClickListener());
        this.toolbar.setOnMenuItemClickListener(new AddMenuListener());
        loadDomains();
        DomainAdapter adapter = new DomainAdapter();
        this.adapter = adapter;
        this.mListView.setAdapter((ListAdapter) adapter);
        this.mListView.setOnItemClickListener(new DomainItemClickListener());
    }
}
