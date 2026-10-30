package com.resync.app.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;
import com.resync.app.R;
import com.resync.app.RedditApplication;
import com.resync.app.ui.fragments.RedditFragment;
import com.resync.app.ui.activities.media.MultiImageActivity;
import com.resync.app.ui.fragment_dialogs.ImagePeekDialogFragment;
import com.resync.app.ui.views.images.CustomImageView;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.events.GridItemSelected;
import com.resync.app.helpers.WindowHelper;
import com.resync.app.helpers.WindowUtils;
import com.resync.app.model.Post;

/* JADX INFO: renamed from: ma.g */
/* JADX INFO: compiled from: ImageGridFragment.java */
/* JADX INFO: loaded from: classes2.dex */
public class ImageGridFragment extends RedditFragment {

    /* JADX INFO: renamed from: u0, reason: collision with root package name */
    private static String EXTRA_IMAGE_URLS = "image_urls";
    private String[] imageUrls;

    /* JADX INFO: renamed from: s0, reason: collision with root package name */
    private GridView gridView;
    private ImageGridAdapter adapter;

    /* JADX INFO: renamed from: ma.g$a */
    /* JADX INFO: compiled from: ImageGridFragment.java */
    class FinishOnClickListener implements View.OnClickListener {
        FinishOnClickListener() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            ImageGridFragment.this.getActivity().finish();
        }
    }

    /* JADX INFO: renamed from: ma.g$b */
    /* JADX INFO: compiled from: ImageGridFragment.java */
    class GridScrollListener implements AbsListView.OnScrollListener {
        GridScrollListener() {
        }

        @Override // android.widget.AbsListView.OnScrollListener
        public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        }

        @Override // android.widget.AbsListView.OnScrollListener
        public void onScrollStateChanged(AbsListView absListView, int scrollState) {
            ImageGridFragment.this.adapter.setScrollState(scrollState);
        }
    }

    /* JADX INFO: renamed from: ma.g$c */
    /* JADX INFO: compiled from: ImageGridFragment.java */
    class ItemClickListener implements AdapterView.OnItemClickListener {
        ItemClickListener() {
        }

        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            // Empty cells only pad the last grid row; they aren't album pages.
            if (position < 0 || position >= ImageGridFragment.this.imageUrls.length) {
                return;
            }
            BusSingleton.getBus().post(new GridItemSelected(position));
        }
    }

    /* JADX INFO: renamed from: ma.g$d */
    /* JADX INFO: compiled from: ImageGridFragment.java */
    class ItemLongClickListener implements AdapterView.OnItemLongClickListener {
        ItemLongClickListener() {
        }

        @Override // android.widget.AdapterView.OnItemLongClickListener
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
            if (position >= ImageGridFragment.this.imageUrls.length) {
                ImageGridFragment.this.getActivity().finish();
                return true;
            }
            if (ImageGridFragment.this.getActivity() instanceof MultiImageActivity) {
                ImagePeekDialogFragment.show(ImageGridFragment.this.getActivity(), Post.createForImageUrl(((MultiImageActivity) ImageGridFragment.this.getActivity()).getUrl(position)));
            }
            return true;
        }
    }

    /* JADX INFO: renamed from: ma.g$e */
    /* JADX INFO: compiled from: ImageGridFragment.java */
    public static class ImageGridAdapter extends BaseAdapter {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final Activity activity;
        private final GridView gridView;
        private final String[] imageUrls;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private int scrollState = 0;
        private final int columnWidth = ImageGridFragment.getColumnWidth();

        public ImageGridAdapter(Activity activity, GridView gridView, String[] strArr) {
            this.activity = activity;
            this.gridView = gridView;
            this.imageUrls = strArr;
        }

        public void setScrollState(int scrollState) {
            boolean wasFlinging = this.scrollState == 2;
            this.scrollState = scrollState;
            if (wasFlinging) {
                this.gridView.invalidateViews();
            }
        }

        @Override // android.widget.Adapter
        public int getCount() {
            int length = this.imageUrls.length;
            while (length % RedditApplication.getAppContext().getResources().getInteger(R.integer.grid_columns) != 0) {
                length++;
            }
            return length;
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
            ViewHolder viewHolder;
            if (view == null) {
                view = this.activity.getLayoutInflater().inflate(R.layout.fragment_image_grid_item, (ViewGroup) null);
                viewHolder = new ViewHolder();
                viewHolder.topPadding = view.findViewById(R.id.top_padding);
                viewHolder.bottomPadding = view.findViewById(R.id.bottom_padding);
                viewHolder.imageWrapper = view.findViewById(R.id.image_view_wrapper);
                viewHolder.imageView = (CustomImageView) view.findViewById(R.id.image_view);
                viewHolder.textView = (TextView) view.findViewById(R.id.image_view_text);
                viewHolder.imageView.getLayoutParams().width = this.columnWidth;
                viewHolder.imageView.getLayoutParams().height = this.columnWidth;
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            if (position < this.gridView.getNumColumns()) {
                viewHolder.topPadding.setVisibility(0);
            } else {
                viewHolder.topPadding.setVisibility(8);
            }
            if (position == getCount() - 1) {
                viewHolder.bottomPadding.setVisibility(0);
            } else {
                viewHolder.bottomPadding.setVisibility(8);
            }
            if (position >= this.imageUrls.length) {
                viewHolder.imageWrapper.setVisibility(4);
                viewHolder.imageView.setVisibility(4);
                viewHolder.textView.setVisibility(8);
                return view;
            }
            viewHolder.imageWrapper.setVisibility(0);
            viewHolder.imageView.setVisibility(0);
            String str = this.imageUrls[position];
            viewHolder.url = str;
            if ("ad".equals(str)) {
                viewHolder.textView.setVisibility(0);
                com.bumptech.glide.Glide.with(RedditApplication.getAppContext()).clear(viewHolder.imageView);
            } else {
                viewHolder.textView.setVisibility(8);
                com.bumptech.glide.Glide.with(RedditApplication.getAppContext()).load(viewHolder.url).into(viewHolder.imageView);
            }
            return view;
        }
    }

    /* JADX INFO: renamed from: ma.g$f */
    /* JADX INFO: compiled from: ImageGridFragment.java */
    static class ViewHolder {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        View topPadding;
        View bottomPadding;
        View imageWrapper;
        CustomImageView imageView;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        String url;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        TextView textView;

        ViewHolder() {
        }
    }

    public static int getColumnWidth() {
        int integer = RedditApplication.getAppContext().getResources().getInteger(R.integer.grid_columns);
        int dimensionPixelOffset = RedditApplication.getAppContext().getResources().getDimensionPixelOffset(R.dimen.static_padding_regular);
        return (((WindowUtils.getDisplayWidth() - dimensionPixelOffset) - dimensionPixelOffset) - (dimensionPixelOffset * (integer - 1))) / integer;
    }

    public static ImageGridFragment newInstance(String[] strArr) {
        ImageGridFragment imageGridFragment = new ImageGridFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArray(EXTRA_IMAGE_URLS, strArr);
        imageGridFragment.setArguments(bundle);
        return imageGridFragment;
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(getLayoutResId(), (ViewGroup) null);
    }

    @Override // com.resync.app.ui.interfaces.RedditFragmentInterface
    public int getLayoutResId() {
        return R.layout.fragment_image_grid;
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        view.setOnClickListener(new FinishOnClickListener());
        this.imageUrls = getArguments().getStringArray(EXTRA_IMAGE_URLS);
        this.gridView = (GridView) view.findViewById(R.id.image_grid);
        ImageGridAdapter imageGridAdapter = new ImageGridAdapter(getActivity(), this.gridView, this.imageUrls);
        this.adapter = imageGridAdapter;
        this.gridView.setAdapter((ListAdapter) imageGridAdapter);
        int actionBarHeight = WindowHelper.getActionBarHeight(getActivity());
        GridView gridView = this.gridView;
        gridView.setPadding(gridView.getPaddingLeft(), actionBarHeight, this.gridView.getPaddingRight(), 0);
        this.gridView.setOnScrollListener(new GridScrollListener());
        this.gridView.setOnItemClickListener(new ItemClickListener());
        this.gridView.setOnItemLongClickListener(new ItemLongClickListener());
    }
}
