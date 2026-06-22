package com.resync.app.ui.fragments.preferences;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.resync.app.R;
import com.resync.app.ui.fragments.RedditFragment;
import com.resync.app.ui.activities.PreferencesActivity;
import com.resync.app.ui.views.core.MaterialYouToolbar;
import com.resync.app.ui.views.transparent_status.CustomCoordinatorLayout;
import com.resync.app.helpers.ui.TabletHelper;
import com.resync.app.fragments.preferences.SearchPreferencesFragment;

/* JADX INFO: loaded from: classes2.dex */
public class SearchPreferenceWrapperFragment extends RedditFragment {

    @BindView(R.id.coordinator)
    CustomCoordinatorLayout coordinatorLayout;

    @BindView(R.id.input)
    EditText search;

    @BindView(R.id.toolbar)
    MaterialYouToolbar toolbar;

    class SearchTextWatcher implements TextWatcher {
        SearchTextWatcher() {
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            Fragment fragmentFindFragmentById = SearchPreferenceWrapperFragment.this.getFragmentManager().findFragmentById(R.id.content_wrapper);
            if (fragmentFindFragmentById instanceof SearchPreferencesFragment) {
                ((SearchPreferencesFragment) fragmentFindFragmentById).search(SearchPreferenceWrapperFragment.this.search.getText().toString());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambdaOpenGeneral(View view) {
        ((PreferencesActivity) getActivity()).openMode(getResources().getInteger(R.integer.GENERAL));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambdaFinish(View view) {
        getActivity().finish();
    }

    public static SearchPreferenceWrapperFragment newInstance() {
        return new SearchPreferenceWrapperFragment();
    }

    public void onDarkModeChanged() {
        if (getFragmentManager().findFragmentById(R.id.content_wrapper) instanceof SearchPreferencesFragment) {
            ((SearchPreferencesFragment) getFragmentManager().findFragmentById(R.id.content_wrapper)).onDarkModeChanged();
        }
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View viewInflate = layoutInflater.inflate(getLayoutResId(), viewGroup, false);
        ButterKnife.bind(this, viewInflate);
        return viewInflate;
    }

    @Override // com.resync.app.ui.interfaces.RedditFragmentInterface
    public int getLayoutResId() {
        return R.layout.fragment_search_preference_wrapper;
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        if (bundle == null) {
            getFragmentManager().beginTransaction().replace(R.id.content_wrapper, SearchPreferencesFragment.newInstance()).commit();
        }
        this.search.setTextSize(1, 16.0f);
        this.search.addTextChangedListener(new SearchTextWatcher());
        this.search.requestFocus();
        ((InputMethodManager) getContext().getSystemService("input_method")).showSoftInput(this.search, 1);
        if (!TabletHelper.isTabletMode()) {
            this.toolbar.setNavigationOnClickListener(new View.OnClickListener() { // from class: pa.m1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    SearchPreferenceWrapperFragment.this.lambdaFinish(view2);
                }
            });
            return;
        }
        this.toolbar.useWindowBackground();
        this.coordinatorLayout.markWindowBackground();
        this.toolbar.setNavigationOnClickListener(new View.OnClickListener() { // from class: pa.n1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                SearchPreferenceWrapperFragment.this.lambdaOpenGeneral(view2);
            }
        });
    }
}
