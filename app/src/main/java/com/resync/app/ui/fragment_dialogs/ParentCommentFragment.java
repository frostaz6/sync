package com.resync.app.ui.fragment_dialogs;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import androidx.core.widget.NestedScrollView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.resync.app.R;
import com.resync.app.ui.viewholders.comments.CommentHolder;
import com.resync.app.dialogs.CustomBottomSheetDialogFragment;
import com.resync.app.model.Post;

/* JADX INFO: loaded from: classes.dex */
public class ParentCommentFragment extends CustomBottomSheetDialogFragment {
    private Post mPost;
    private CommentHolder mCommentHolder;

    @BindView(R.id.linearLayout)
    FrameLayout mFrameLayout;

    @BindView(R.id.scrollView)
    NestedScrollView mScrollView;

    public static ParentCommentFragment newInstance(Post post) {
        ParentCommentFragment parentCommentFragment = new ParentCommentFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("Post", post);
        parentCommentFragment.setArguments(bundle);
        return parentCommentFragment;
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetInterface
    public int getLayoutRes() {
        return R.layout.fragment_parent_comment;
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        ButterKnife.bind(this, view);
        this.mPost = (Post) getArguments().getSerializable("Post");
        CommentHolder commentHolderX = CommentHolder.createContinue(getActivity(), this.mFrameLayout, null);
        this.mCommentHolder = commentHolderX;
        this.mFrameLayout.addView(commentHolderX.itemView);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -2);
        layoutParams.gravity = 17;
        this.mCommentHolder.itemView.setLayoutParams(layoutParams);
        this.mCommentHolder.enableCopyMode();
        this.mCommentHolder.bind(this.mPost);
        super.onViewCreated(view, bundle);
    }
}
