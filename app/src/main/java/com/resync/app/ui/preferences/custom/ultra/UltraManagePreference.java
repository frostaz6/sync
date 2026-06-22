package com.resync.app.ui.preferences.custom.ultra;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.View;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.resync.app.R;
import com.resync.app.ui.views.onboarding.OnboardingRestoreButton;
import com.resync.app.ui.views.text.BaseTextView;
import com.resync.app.ui.views.ultra.UltraPurchasedView;
import com.resync.app.helpers.ui.StaticToast;
import java.util.HashMap;
import okhttp3.internal.http.HttpStatusCodesKt;
import org.apache.commons.lang3.ObjectUtils;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.premium.UltraHelper;
import com.resync.app.util.Logger;
import com.resync.app.events.UltraSkusLoaded;

/* JADX INFO: loaded from: classes.dex */
public class UltraManagePreference extends Preference {

    private Integer errorState;
    HashMap subscriptionInfo;

    View boundView;

    LinearLayoutCompat optionsLayout;
    UltraPurchasedView expiryView;
    UltraPurchasedView statusView;

    BaseTextView descriptionTextView;
    OnboardingRestoreButton subscribeButton;

    public UltraManagePreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setKey("ultra_manage_preference");
        setLayoutResource(R.layout.preference_ultra_manage);
        setSelectable(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onSubscriptionInfoLoaded(HttpsCallableResult httpsCallableResult) {
        if (httpsCallableResult.getData() instanceof HashMap) {
            Logger.verbose("iap_helper", "Loaded subscription information...");
            HashMap map = (HashMap) httpsCallableResult.getData();
            if (ObjectUtils.equals(Integer.valueOf(HttpStatusCodesKt.HTTP_OK), map.get("status"))) {
                HashMap map2 = (HashMap) map.get("message");
                this.subscriptionInfo = map2;
                if (map2 != null) {
                    boolean zBooleanValue = ((Boolean) map2.get("cancelled")).booleanValue();
                    boolean zBooleanValue2 = ((Boolean) this.subscriptionInfo.get("lifetime")).booleanValue();
                    if (zBooleanValue && zBooleanValue2) {
                        StaticToast.show("Purchase was cancelled");
                        UltraHelper.clearPurchaseToken();
                        return;
                    }
                }
            } else {
                this.errorState = 3;
            }
            BusSingleton.getBus().post(new UltraSkusLoaded());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onSubscriptionInfoFailed(Exception exc) {
        this.errorState = 3;
        BusSingleton.getBus().post(new UltraSkusLoaded());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onManageClicked(View view) {
        getContext().startActivity(UltraHelper.getManageSubscriptionIntent());
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        preferenceViewHolder.itemView.setEnabled(false);
        View view = preferenceViewHolder.itemView;
        this.boundView = view;
        this.subscribeButton = (OnboardingRestoreButton) view.findViewById(R.id.ultra_subscribe);
        this.descriptionTextView = (BaseTextView) preferenceViewHolder.itemView.findViewById(R.id.ultra_description);
        this.optionsLayout = (LinearLayoutCompat) preferenceViewHolder.itemView.findViewById(R.id.ultra_options);
        this.expiryView = (UltraPurchasedView) preferenceViewHolder.itemView.findViewById(R.id.ultra_purchase_expiry);
        this.statusView = (UltraPurchasedView) preferenceViewHolder.itemView.findViewById(R.id.ultra_purchase_status);
        this.descriptionTextView.refreshTextColor();
        this.subscribeButton.applyColors();
        this.expiryView.applyColors();
        this.statusView.applyColors();
        this.subscribeButton.setOnClickListener(new View.OnClickListener() { // from class: cb.a
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                UltraManagePreference.this.onManageClicked(view2);
            }
        });
        if (UltraHelper.isLifetime()) {
            this.descriptionTextView.setText("You are are a lifetime member of Sync Ultra.");
            this.optionsLayout.setVisibility(8);
            this.subscribeButton.setVisibility(8);
            return;
        }
        if (UltraHelper.isBacker()) {
            this.descriptionTextView.setText("Thanks for being a backer of Sync!");
            this.optionsLayout.setVisibility(8);
            this.subscribeButton.setVisibility(8);
            return;
        }
        this.descriptionTextView.setText("You are currently subscribed to " + UltraHelper.getSubscriptionDisplayName() + ".");
        HashMap map = this.subscriptionInfo;
        if (map != null) {
            boolean zBooleanValue = ((Boolean) map.get("cancelled")).booleanValue();
            if (!((Boolean) this.subscriptionInfo.get("lifetime")).booleanValue()) {
                if (zBooleanValue) {
                    this.expiryView.setPrice("Active until");
                } else {
                    this.expiryView.setPrice("Next payment on");
                }
                this.expiryView.setDescription(DateFormat.format("MMMM dd, yyyy", ((Long) this.subscriptionInfo.get("expires")).longValue()));
                this.expiryView.setIcon(R.drawable.calendar_clock_outline);
                if (zBooleanValue) {
                    this.statusView.setIcon(R.drawable.outline_remove_circle_outline_24);
                    this.statusView.setPrice("No renewal");
                    this.statusView.setDescription("Cancelled");
                } else {
                    this.statusView.setIcon(R.drawable.outline_check_circle_24);
                    this.statusView.setPrice("Auto renewal");
                    this.statusView.setDescription("Active");
                }
            }
        }
        if (this.errorState != null) {
            this.expiryView.showError();
            this.statusView.showError();
        }
    }

    public void refresh() {
        // LIBERATED: this fetched Play subscription details (expiry/cancellation/lifetime) from
        // Sync's dead Ultra backend (Firebase callable "getSubscriptionInformationCallable") to
        // populate the manage-subscription card. The server is gone and Ultra is force-owned, so
        // there is nothing to fetch. Skip the network call and just refresh the UI the same way
        // the old failure path did (onSubscriptionInfoFailed: mark "no details", broadcast UltraSkusLoaded).
        android.util.Log.i("UltraManagePreference", "Skipped subscription info fetch (dead Ultra server)");
        onSubscriptionInfoFailed(null);
    }
}
