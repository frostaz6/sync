package com.resync.app.ui.viewholders.sections.subreddits;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.ButterKnife;
import com.resync.app.R;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.events.FullSubSearchEvent;
import com.resync.app.events.SubredditsChangedEvent;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.helpers.NightHelper;

/* JADX INFO: loaded from: classes3.dex */
public class SubredditHeaderHolder extends RecyclerView.ViewHolder {

    private int mode;
    String subreddit;

    TextView mHeaderTextView;

    AppCompatImageView mIndicatorIcon;

    TextView mMoreText;

    public SubredditHeaderHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    public static SubredditHeaderHolder create(ViewGroup viewGroup) {
        return new SubredditHeaderHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.holder_subreddit_header, viewGroup, false));
    }

    public void bind(int mode, String subreddit) {
        this.mode = mode;
        this.subreddit = subreddit;
        this.mIndicatorIcon.setImageTintList(ColorStateList.valueOf(NightHelper.isNight() ? -8947849 : -6645094));
        this.mMoreText.setVisibility(mode == 10 ? 0 : 8);
        if (mode == 7) {
            this.mHeaderTextView.setText("Feeds");
            if (SettingsSingleton.getSettings().collapseFeeds) {
                this.mIndicatorIcon.setImageResource(R.drawable.ic_expand_more_white_24dp);
                return;
            } else {
                this.mIndicatorIcon.setImageResource(R.drawable.ic_expand_less_white_24dp);
                return;
            }
        }
        if (mode == 1) {
            this.mHeaderTextView.setText("Favorites");
            if (SettingsSingleton.getSettings().collapseFav) {
                this.mIndicatorIcon.setImageResource(R.drawable.ic_expand_more_white_24dp);
                return;
            } else {
                this.mIndicatorIcon.setImageResource(R.drawable.ic_expand_less_white_24dp);
                return;
            }
        }
        if (mode == 2) {
            this.mHeaderTextView.setText("Subscriptions");
            if (SettingsSingleton.getSettings().collapseSubscriptions) {
                this.mIndicatorIcon.setImageResource(R.drawable.ic_expand_more_white_24dp);
                return;
            } else {
                this.mIndicatorIcon.setImageResource(R.drawable.ic_expand_less_white_24dp);
                return;
            }
        }
        if (mode == 3) {
            this.mHeaderTextView.setText("Moderated");
            if (SettingsSingleton.getSettings().collapseModerated) {
                this.mIndicatorIcon.setImageResource(R.drawable.ic_expand_more_white_24dp);
                return;
            } else {
                this.mIndicatorIcon.setImageResource(R.drawable.ic_expand_less_white_24dp);
                return;
            }
        }
        if (mode == 4) {
            this.mHeaderTextView.setText("Custom feeds");
            if (SettingsSingleton.getSettings().collapseCustomFeed) {
                this.mIndicatorIcon.setImageResource(R.drawable.ic_expand_more_white_24dp);
                return;
            } else {
                this.mIndicatorIcon.setImageResource(R.drawable.ic_expand_less_white_24dp);
                return;
            }
        }
        if (mode == 5) {
            this.mHeaderTextView.setText("Top");
            if (SettingsSingleton.getSettings().collapseTop) {
                this.mIndicatorIcon.setImageResource(R.drawable.ic_expand_more_white_24dp);
                return;
            } else {
                this.mIndicatorIcon.setImageResource(R.drawable.ic_expand_less_white_24dp);
                return;
            }
        }
        if (mode == 6) {
            this.mHeaderTextView.setText("Following");
            if (SettingsSingleton.getSettings().collapseFollows) {
                this.mIndicatorIcon.setImageResource(R.drawable.ic_expand_more_white_24dp);
                return;
            } else {
                this.mIndicatorIcon.setImageResource(R.drawable.ic_expand_less_white_24dp);
                return;
            }
        }
        if (mode == 9) {
            this.mHeaderTextView.setText("Subscribed");
            if (SettingsSingleton.getSettings().collapseSearchSubscribed) {
                this.mIndicatorIcon.setImageResource(R.drawable.ic_expand_more_white_24dp);
                return;
            } else {
                this.mIndicatorIcon.setImageResource(R.drawable.ic_expand_less_white_24dp);
                return;
            }
        }
        if (mode == 10) {
            this.mHeaderTextView.setText("Suggested subreddit results");
            if (SettingsSingleton.getSettings().collapseSearchResults) {
                this.mIndicatorIcon.setImageResource(R.drawable.ic_expand_more_white_24dp);
                return;
            } else {
                this.mIndicatorIcon.setImageResource(R.drawable.ic_expand_less_white_24dp);
                return;
            }
        }
        if (mode == 11) {
            this.mHeaderTextView.setText("Actions");
            if (SettingsSingleton.getSettings().collapseSearchAction) {
                this.mIndicatorIcon.setImageResource(R.drawable.ic_expand_more_white_24dp);
                return;
            } else {
                this.mIndicatorIcon.setImageResource(R.drawable.ic_expand_less_white_24dp);
                return;
            }
        }
        if (mode != 8) {
            throw new RuntimeException("Not supported: " + mode);
        }
        this.mHeaderTextView.setText("Recently searched & visited");
        if (SettingsSingleton.getSettings().collapseSearchRecent) {
            this.mIndicatorIcon.setImageResource(R.drawable.ic_expand_more_white_24dp);
        } else {
            this.mIndicatorIcon.setImageResource(R.drawable.ic_expand_less_white_24dp);
        }
    }

    public void onMoreClicked() {
        BusSingleton.getBus().post(new FullSubSearchEvent());
    }

    public void onRowClicked() {
        int mode = this.mode;
        if (mode == 7) {
            if (SettingsSingleton.getSettings().collapseFeeds) {
                SettingsSingleton.getSettings().collapseFeeds = false;
                SettingsSingleton.getInstance().putBoolean("collapse_feeds", false);
            } else {
                SettingsSingleton.getSettings().collapseFeeds = true;
                SettingsSingleton.getInstance().putBoolean("collapse_feeds", true);
            }
        } else if (mode == 1) {
            if (SettingsSingleton.getSettings().collapseFav) {
                SettingsSingleton.getSettings().collapseFav = false;
                SettingsSingleton.getInstance().putBoolean("collapse_fav", false);
            } else {
                SettingsSingleton.getSettings().collapseFav = true;
                SettingsSingleton.getInstance().putBoolean("collapse_fav", true);
            }
        } else if (mode == 2) {
            if (SettingsSingleton.getSettings().collapseSubscriptions) {
                SettingsSingleton.getSettings().collapseSubscriptions = false;
                SettingsSingleton.getInstance().putBoolean("collapse_subscriptions", false);
            } else {
                SettingsSingleton.getSettings().collapseSubscriptions = true;
                SettingsSingleton.getInstance().putBoolean("collapse_subscriptions", true);
            }
        } else if (mode == 3) {
            if (SettingsSingleton.getSettings().collapseModerated) {
                SettingsSingleton.getSettings().collapseModerated = false;
                SettingsSingleton.getInstance().putBoolean("collapse_moderated", false);
            } else {
                SettingsSingleton.getSettings().collapseModerated = true;
                SettingsSingleton.getInstance().putBoolean("collapse_moderated", true);
            }
        } else if (mode == 4) {
            if (SettingsSingleton.getSettings().collapseCustomFeed) {
                SettingsSingleton.getSettings().collapseCustomFeed = false;
                SettingsSingleton.getInstance().putBoolean("collapse_custom_feed", false);
            } else {
                SettingsSingleton.getSettings().collapseCustomFeed = true;
                SettingsSingleton.getInstance().putBoolean("collapse_custom_feed", true);
            }
        } else if (mode == 5) {
            if (SettingsSingleton.getSettings().collapseTop) {
                SettingsSingleton.getSettings().collapseTop = false;
                SettingsSingleton.getInstance().putBoolean("collapse_top", false);
            } else {
                SettingsSingleton.getSettings().collapseTop = true;
                SettingsSingleton.getInstance().putBoolean("collapse_top", true);
            }
        } else if (mode == 6) {
            if (SettingsSingleton.getSettings().collapseFollows) {
                SettingsSingleton.getSettings().collapseFollows = false;
                SettingsSingleton.getInstance().putBoolean("collapse_follows", false);
            } else {
                SettingsSingleton.getSettings().collapseFollows = true;
                SettingsSingleton.getInstance().putBoolean("collapse_follows", true);
            }
        } else if (mode == 9) {
            if (SettingsSingleton.getSettings().collapseSearchSubscribed) {
                SettingsSingleton.getSettings().collapseSearchSubscribed = false;
                SettingsSingleton.getInstance().putBoolean("collapse_search_subscribed", false);
            } else {
                SettingsSingleton.getSettings().collapseSearchSubscribed = true;
                SettingsSingleton.getInstance().putBoolean("collapse_search_subscribed", true);
            }
        } else if (mode == 10) {
            if (SettingsSingleton.getSettings().collapseSearchResults) {
                SettingsSingleton.getSettings().collapseSearchResults = false;
                SettingsSingleton.getInstance().putBoolean("collapse_search_results", false);
            } else {
                SettingsSingleton.getSettings().collapseSearchResults = true;
                SettingsSingleton.getInstance().putBoolean("collapse_search_results", true);
            }
        } else if (mode == 11) {
            if (SettingsSingleton.getSettings().collapseSearchAction) {
                SettingsSingleton.getSettings().collapseSearchAction = false;
                SettingsSingleton.getInstance().putBoolean("collapse_search_actions", false);
            } else {
                SettingsSingleton.getSettings().collapseSearchAction = true;
                SettingsSingleton.getInstance().putBoolean("collapse_search_actions", true);
            }
        } else if (mode == 8) {
            if (SettingsSingleton.getSettings().collapseSearchRecent) {
                SettingsSingleton.getSettings().collapseSearchRecent = false;
                SettingsSingleton.getInstance().putBoolean("collapse_search_recent", false);
            } else {
                SettingsSingleton.getSettings().collapseSearchRecent = true;
                SettingsSingleton.getInstance().putBoolean("collapse_search_recent", true);
            }
        }
        BusSingleton.getBus().post(new SubredditsChangedEvent());
    }
}
