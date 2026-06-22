package com.resync.app.ui.preferences.custom;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.RedditHelper;
import com.resync.app.R;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.activities.PreferencesActivity;
import com.resync.app.internal.SyncStyleable;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.ui.StaticToast;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import com.resync.app.singletons.StyleSingleton;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.helpers.ActivityHelper;
import com.resync.app.helpers.ContextHelper;
import com.resync.app.helpers.TypefaceManager;

/* JADX INFO: loaded from: classes.dex */
public class RootPreference extends Preference {

    public View boundView;
    String title;

    String summary;

    int iconResId;
    int mode;
    TextView titleTextView;

    TextView summaryTextView;
    FrameLayout imageWrapper;
    ImageView iconImageView;

    class CacheStatsListener implements Response.Listener<String> {
        CacheStatsListener() {
        }

        @Override // com.android.volley.Response.Listener
        public void onResponse(String str) {
            try {
                JSONObject jSONObject = new JSONObject(str);
                StaticToast.show(jSONObject.getInt("files") + " files\n" + jSONObject.getString("size") + " total");
            } catch (Exception unused) {
            }
        }
    }

    class CacheErrorListener implements Response.ErrorListener {
        CacheErrorListener() {
        }

        @Override // com.android.volley.Response.ErrorListener
        public void onErrorResponse(VolleyError volleyError) {
            StaticToast.show("Error loading cache");
        }
    }

    public RootPreference(final Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, SyncStyleable.SYNC_PREFERENCE);
        this.title = typedArrayObtainStyledAttributes.getString(4);
        this.summary = typedArrayObtainStyledAttributes.getString(7);
        this.iconResId = typedArrayObtainStyledAttributes.getResourceId(0, 0);
        this.mode = typedArrayObtainStyledAttributes.getInteger(2, 0);
        typedArrayObtainStyledAttributes.recycle();
        setKey(Integer.toString(this.mode));
        setLayoutResource(R.layout.preference_root);
        setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: ab.c
            @Override // androidx.preference.Preference.OnPreferenceClickListener
            public final boolean onPreferenceClick(Preference preference) {
                return RootPreference.this.onPreferenceClicked(context, preference);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean onPreferenceClicked(Context context, Preference preference) {
        if (this.mode == context.getResources().getInteger(R.integer.SETUP)) {
            ActivityHelper.restartToOnboarding(ContextHelper.getBaseActivity(getContext()));
            return true;
        }
        if (this.mode != context.getResources().getInteger(R.integer.CACHE)) {
            ((PreferencesActivity) ContextHelper.getBaseActivity(getContext())).openMode(this.mode);
            return true;
        }
        StringRequest stringRequest = new StringRequest(0, "https://ap.syncforreddit.com/stats.json", new CacheStatsListener(), new CacheErrorListener());
        stringRequest.setShouldCache(false);
        OAuthRequestManager.enqueueAux(stringRequest);
        return true;
    }

    protected int getHighlightColor() {
        int linkColor = MonetUtil.getLinkColorAuto();
        return Color.argb(60, Color.red(linkColor), Color.green(linkColor), Color.blue(linkColor));
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        View view = preferenceViewHolder.itemView;
        this.boundView = view;
        TextView textView = (TextView) view.findViewById(R.id.title);
        this.titleTextView = textView;
        textView.setTextColor(StyleSingleton.getTextColorAuto(getContext(), false));
        this.titleTextView.setTextSize(1, RedditHelper.getTitleTextSize(SettingsSingleton.getSettings().fontSize) + 2);
        this.titleTextView.setText(this.title);
        this.titleTextView.setTypeface(TypefaceManager.get(11));
        TextView textView2 = (TextView) this.boundView.findViewById(R.id.summary);
        this.summaryTextView = textView2;
        textView2.setTextColor(MonetUtil.getSecondaryTextColorAuto());
        this.summaryTextView.setTextSize(1, RedditHelper.getSmallHeaderTextSize(SettingsSingleton.getSettings().fontSize));
        this.summaryTextView.setTypeface(TypefaceManager.get(9));
        this.summaryTextView.setText(this.summary);
        this.summaryTextView.setVisibility(0);
        if (StringUtils.isEmpty(this.summary)) {
            this.summaryTextView.setVisibility(8);
        }
        this.imageWrapper = (FrameLayout) this.boundView.findViewById(R.id.image_wrapper);
        ImageView imageView = (ImageView) this.boundView.findViewById(R.id.image);
        this.iconImageView = imageView;
        imageView.setImageResource(this.iconResId);
        this.iconImageView.setImageTintList(ColorStateList.valueOf(StyleSingleton.getTextColorAuto(getContext(), false)));
        if (StringUtils.equalsIgnoreCase(getKey(), (String) preferenceViewHolder.itemView.getTag(R.id.preferences_key_highlight))) {
            this.boundView.setForeground(new ColorDrawable(getHighlightColor()));
        } else {
            this.boundView.setForeground(new ColorDrawable(0));
        }
        preferenceViewHolder.itemView.setTag(R.id.preference_title, getSummary());
        preferenceViewHolder.itemView.setTag(R.id.preference_key, getKey());
    }
}
