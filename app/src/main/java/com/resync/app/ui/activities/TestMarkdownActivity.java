package com.resync.app.ui.activities;

import android.os.Bundle;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.resync.app.R;
import com.resync.app.util.links.LinkHelper;

/* JADX INFO: loaded from: classes.dex */
public class TestMarkdownActivity extends BaseActivity {
    @OnClick
    public void onAnimatedEmoteClicked() {
        LinkHelper.open(getActivity(), "https://old.reddit.com/r/pics/comments/shct8u/gary_sinise_here_i_found_these_great_bts_photos/hv2743t/");
    }

    @Override // com.resync.app.ui.activities.BaseActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_test_markdown);
        ButterKnife.bind(this);
    }

    @OnClick
    public void onEmoteClicked() {
        LinkHelper.open(getActivity(), "https://www.reddit.com/r/wallstreetbets/comments/st0fbk/daily_discussion_thread_for_february_15_2022/hx0zabp/");
    }

    @OnClick
    public void onInlineGifClicked() {
        LinkHelper.open(getActivity(), "https://www.reddit.com/r/AbsoluteUnits/comments/lbf57f/we_are_now_part_of_the_powerups_alpha/gphasgw/");
    }

    @OnClick
    public void onInlineGifNoSizeClicked() {
        LinkHelper.open(getActivity(), "https://www.reddit.com/r/redditsync/comments/st20d7/markdown_testing_thread/hx11b82/");
    }

    @OnClick
    public void onLotsOfAwardsClicked() {
        LinkHelper.open(getActivity(), "https://new.reddit.com/r/pics/comments/shct8u/gary_sinise_here_i_found_these_great_bts_photos/");
    }

    @OnClick
    public void onSelftextGifClicked() {
        LinkHelper.open(getActivity(), "https://www.reddit.com/r/redditsync/comments/st20d7/markdown_testing_thread/");
    }

    @OnClick
    public void onSpoilerClicked() {
        LinkHelper.open(getActivity(), "https://www.reddit.com/r/redditsync/comments/st20d7/markdown_testing_thread/hx1n9jo/");
    }

    @OnClick
    public void onUserImageFlairClicked() {
        LinkHelper.open(getActivity(), "https://old.reddit.com/r/wallstreetbets/comments/ssyhyn/elon_musk_donates_57_billion_tesla_stock_worlds/hx1436m/");
    }
}
