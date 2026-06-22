package com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base;

import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.ui.views.core.MaterialRow;
import com.resync.app.ui.views.monet.MonetSearchBoxView;
import java.io.Serializable;
import java.util.ArrayList;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.adapters.HeaderDividerSection;
import com.resync.app.text.spans.MonetRoundedCornerSpan;
import com.resync.app.dialogs.CustomBottomSheetDialogFragment;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.dialogs.interfaces.SearchSelectionDialogBottomSheetInterface;
import com.resync.app.util.ColorUtils;

/* JADX INFO: loaded from: classes.dex */
public abstract class AbstractSearchSelectionDialogBottomSheet extends CustomBottomSheetDialogFragment implements SearchSelectionDialogBottomSheetInterface {
    private ArrayList<SearchItem> mItems = new ArrayList<>();
    private SearchAdapter mAdapter;

    @BindView(R.id.abstract_search_selection_dialog_bottom_sheet_add)
    ImageView mAddButton;

    @BindView(R.id.abstract_search_selection_dialog_bottom_sheet_items)
    RecyclerView mRecyclerView;

    @BindView(R.id.monet_search_box)
    MonetSearchBoxView mSearchBox;

    public class AbstractSelectionDialogBottomSheetItemHolder extends RecyclerView.ViewHolder {

        SearchItem boundItem;

        @BindView(R.id.abstract_alert_dialog_bottom_sheet_item_indicator)
        AppCompatImageButton mIndicator;

        @BindView(R.id.abstract_alert_dialog_bottom_sheet_item_row)
        MaterialRow mRow;

        public AbstractSelectionDialogBottomSheetItemHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.mRow.applyColors(AbstractSearchSelectionDialogBottomSheet.this.isNightMode());
        }

        public void bind(SearchItem searchItem) {
            this.boundItem = searchItem;
            if (!(searchItem instanceof PaintedSearchItem)) {
                if (!(searchItem instanceof TaggedSearchItem)) {
                    throw new RuntimeException("Not supported");
                }
                String str = ((TaggedSearchItem) searchItem).secondary;
                this.mRow.setTitle(searchItem.label);
                this.mRow.showUserIcon();
                this.mRow.setUsername(searchItem.label);
                this.mRow.setSecondary(str);
                this.mIndicator.setVisibility(8);
                this.mRow.showMore();
                return;
            }
            int color = ((PaintedSearchItem) searchItem).color;
            this.mRow.setTitle(searchItem.label);
            this.mRow.setSecondary(ColorUtils.toHexColor(color));
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
            spannableStringBuilder.append((CharSequence) searchItem.label);
            spannableStringBuilder.setSpan(new MonetRoundedCornerSpan(color), 0, spannableStringBuilder.length(), 33);
            spannableStringBuilder.append((CharSequence) StringUtils.SPACE);
            this.mRow.setTitle(spannableStringBuilder);
            this.mRow.showUserIcon();
            this.mRow.setUsername(searchItem.label);
            this.mRow.showMore();
            this.mIndicator.setVisibility(8);
        }

        public MaterialRow getRow() {
            return this.mRow;
        }

        @OnClick
        public void onMoreClicked(View view) {
            AbstractSearchSelectionDialogBottomSheet.this.onItemMoreClicked(view, this.boundItem);
        }
    }

    public static class AbstractSelectionDialogBottomSheetItemHolder_ViewBinding implements Unbinder {
        private AbstractSelectionDialogBottomSheetItemHolder target;
        private View moreClickView;

        /* JADX INFO: compiled from: AbstractSearchSelectionDialogBottomSheet$AbstractSelectionDialogBottomSheetItemHolder_ViewBinding.java */
        static class ItemClickListener extends com.resync.app.util.DebouncingOnClickListener {

            /* JADX INFO: renamed from: q, reason: collision with root package name */
            final /* synthetic */ AbstractSelectionDialogBottomSheetItemHolder holder;

            ItemClickListener(AbstractSelectionDialogBottomSheetItemHolder abstractSelectionDialogBottomSheetItemHolder) {
                this.holder = abstractSelectionDialogBottomSheetItemHolder;
            }

            @Override // com.resync.app.util.DebouncingOnClickListener
            public void doClick(View view) {
                this.holder.onMoreClicked(view);
            }
        }

        public AbstractSelectionDialogBottomSheetItemHolder_ViewBinding(AbstractSelectionDialogBottomSheetItemHolder abstractSelectionDialogBottomSheetItemHolder, View view) {
            this.target = abstractSelectionDialogBottomSheetItemHolder;
            abstractSelectionDialogBottomSheetItemHolder.mRow = (MaterialRow) Utils.findRequiredViewAsType(view, R.id.abstract_alert_dialog_bottom_sheet_item_row, "field 'mRow'", MaterialRow.class);
            abstractSelectionDialogBottomSheetItemHolder.mIndicator = (AppCompatImageButton) Utils.findRequiredViewAsType(view, R.id.abstract_alert_dialog_bottom_sheet_item_indicator, "field 'mIndicator'", AppCompatImageButton.class);
            View viewC = Utils.findRequiredView(view, R.id.more, "method 'onMoreClicked'");
            this.moreClickView = viewC;
            viewC.setOnClickListener(new ItemClickListener(abstractSelectionDialogBottomSheetItemHolder));
        }

        @Override // butterknife.Unbinder
        public void unbind() {
            AbstractSelectionDialogBottomSheetItemHolder target = this.target;
            if (target == null) {
                throw new IllegalStateException("Bindings already cleared.");
            }
            this.target = null;
            target.mRow = null;
            target.mIndicator = null;
            View moreView = this.moreClickView;
            if (moreView != null) {
                moreView.setOnClickListener(null);
                this.moreClickView = null;
            }
        }
    }

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
            AbstractSearchSelectionDialogBottomSheet.this.onSearchTextChanged(charSequence);
        }
    }

    class SearchAdapter extends RecyclerView.Adapter<AbstractSelectionDialogBottomSheetItemHolder> {
        public SearchAdapter() {
            setHasStableIds(true);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void onRowClicked(int position, View view) {
            AbstractSearchSelectionDialogBottomSheet abstractSearchSelectionDialogBottomSheet = AbstractSearchSelectionDialogBottomSheet.this;
            abstractSearchSelectionDialogBottomSheet.onItemClicked(abstractSearchSelectionDialogBottomSheet.getItem(position));
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /* JADX INFO: renamed from: P, reason: merged with bridge method [inline-methods] */
        public void onBindViewHolder(AbstractSelectionDialogBottomSheetItemHolder abstractSelectionDialogBottomSheetItemHolder, final int position) {
            abstractSelectionDialogBottomSheetItemHolder.bind(AbstractSearchSelectionDialogBottomSheet.this.getItem(position));
            abstractSelectionDialogBottomSheetItemHolder.getRow().setOnClickListener(new View.OnClickListener() { // from class: com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.d
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    AbstractSearchSelectionDialogBottomSheet.SearchAdapter.this.onRowClicked(position, view);
                }
            });
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /* JADX INFO: renamed from: Q, reason: merged with bridge method [inline-methods] */
        public AbstractSelectionDialogBottomSheetItemHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            return AbstractSearchSelectionDialogBottomSheet.this.new AbstractSelectionDialogBottomSheetItemHolder(LayoutInflater.from(AbstractSearchSelectionDialogBottomSheet.this.getContext()).inflate(R.layout.abstract_selection_dialog_bottom_sheet_item, viewGroup, false));
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /* JADX INFO: renamed from: n */
        public int getItemCount() {
            return AbstractSearchSelectionDialogBottomSheet.this.mItems.size();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /* JADX INFO: renamed from: o */
        public long getItemId(int position) {
            return ((SearchItem) AbstractSearchSelectionDialogBottomSheet.this.mItems.get(position)).getId();
        }
    }

    public class PaintedSearchItem extends SearchItem {
        int color;

        public PaintedSearchItem(String str, int color) {
            super(str);
            this.color = color;
        }
    }

    public static class SearchItem implements Serializable {

        public String label;

        public SearchItem(String str) {
            this.label = str;
        }

        public int getId() {
            return this.label.hashCode();
        }

        public String toString() {
            return this.label;
        }
    }

    public class TaggedSearchItem extends SearchItem {
        public String secondary;

        public TaggedSearchItem(String str, String str2) {
            super(str);
            this.secondary = str2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onSearchFocusChanged(View view, boolean hasFocus) {
        if (hasFocus) {
            this.mSearchBox.showKeyboard();
            expand();
        }
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public boolean isInitiallyExpanded() {
        return false;
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetInterface
    public int getLayoutRes() {
        return R.layout.abstract_search_selection_dialog_bottom_sheet;
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public void onSheetStateChanged(View view, int state) {
        super.onSheetStateChanged(view, state);
        if (state == 1) {
            hideKeyboard();
        }
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        ButterKnife.bind(this, view);
        this.mSearchBox.measure(-1, -2);
        int measuredHeight = this.mSearchBox.getMeasuredHeight() + SizeHelper.dpToPx(40);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ConcatAdapter.Config aVarA = new ConcatAdapter.Config.Builder().setStableIdMode(ConcatAdapter.Config.StableIdMode.ISOLATED_STABLE_IDS).setIsolateViewTypes(true).build();
        this.mAdapter = new SearchAdapter();
        this.mRecyclerView.setAdapter(new ConcatAdapter(aVarA, (RecyclerView.Adapter<? extends RecyclerView.ViewHolder>[]) new RecyclerView.Adapter[]{new HeaderDividerSection(measuredHeight), this.mAdapter}));
        this.mAddButton.setOnClickListener(new View.OnClickListener() { // from class: u9.l
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                AbstractSearchSelectionDialogBottomSheet.this.onAddClicked(view2);
            }
        });
        this.mSearchBox.setMoreClickListener(new View.OnClickListener() { // from class: u9.m
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                AbstractSearchSelectionDialogBottomSheet.this.onSearchClicked(view2);
            }
        });
        this.mSearchBox.setHint(getSearchHint());
        this.mSearchBox.setOnFocusChangeListener(new View.OnFocusChangeListener() { // from class: u9.n
            @Override // android.view.View.OnFocusChangeListener
            public final void onFocusChange(View view2, boolean hasFocus) {
                AbstractSearchSelectionDialogBottomSheet.this.onSearchFocusChanged(view2, hasFocus);
            }
        });
        this.mSearchBox.addTextChangedListener(new SearchTextWatcher());
        setupItems();
    }

    public final SearchItem addItem(SearchItem searchItem) {
        if (searchItem == null) {
            throw new RuntimeException("Null item");
        }
        this.mItems.add(searchItem);
        this.mAdapter.notifyDataSetChanged();
        return searchItem;
    }

    public final SearchItem getItem(int position) {
        return this.mItems.get(position);
    }

    public void setupItems() {
    }

    public final void clearItems() {
        this.mItems.clear();
        SearchAdapter searchAdapter = this.mAdapter;
        if (searchAdapter != null) {
            searchAdapter.notifyDataSetChanged();
        }
    }
}
