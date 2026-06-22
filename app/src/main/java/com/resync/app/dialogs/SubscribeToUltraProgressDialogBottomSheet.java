package com.resync.app.dialogs;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractProgressDialogBottomSheet;
import com.squareup.otto.Subscribe;
import com.resync.app.helpers.ui.StaticToast;
import java.util.HashMap;
import okhttp3.internal.http.HttpStatusCodesKt;
import org.apache.commons.lang3.ObjectUtils;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.premium.UltraErrorHelper;
import com.resync.app.util.Logger;
import com.resync.app.events.UltraFcmLookupIdSet;
import com.resync.app.events.UltraPurchaseCancelledEvent;
import com.resync.app.events.UltraStatusChangedEvent;

/* JADX INFO: renamed from: fa.i */
/* JADX INFO: compiled from: SubscribeToUltraProgressDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class SubscribeToUltraProgressDialogBottomSheet extends AbstractProgressDialogBottomSheet implements OnSuccessListener<InstanceIdResult>, OnFailureListener {

    /* JADX INFO: compiled from: SubscribeToUltraProgressDialogBottomSheet.java */
    class DismissRunnable implements Runnable {
        DismissRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            SubscribeToUltraProgressDialogBottomSheet.this.dismissAllowingStateLoss();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onAddFcmIdFailure(Exception exc) {
        showError(2);
    }

    private void showError(final int errorCode) {
        if (isDialogShowing()) {
            getView().post(new Runnable() { // from class: fa.h
                @Override // java.lang.Runnable
                public final void run() {
                    SubscribeToUltraProgressDialogBottomSheet.this.showErrorAndDismiss(errorCode);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void showErrorAndDismiss(int errorCode) {
        StaticToast.show(UltraErrorHelper.getErrorMessage(errorCode));
        dismissAllowingStateLoss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onAddFcmIdResult(HttpsCallableResult httpsCallableResult) {
        try {
            HashMap map = (HashMap) httpsCallableResult.getData();
            Object obj = map.get("status");
            Logger.verbose("iap_helper", "Status: " + obj);
            if (!ObjectUtils.equals(Integer.valueOf(HttpStatusCodesKt.HTTP_OK), obj)) {
                throw new RuntimeException("Invalid");
            }
            String str = (String) map.get("message");
            Logger.verbose("iap_helper", "Lookup ID: " + str);
            BusSingleton.getBus().post(new UltraFcmLookupIdSet(str));
        } catch (Exception unused) {
            showError(2);
        }
    }

    @Override // com.google.android.gms.tasks.OnSuccessListener
    /* JADX INFO: renamed from: B4, reason: merged with bridge method [inline-methods] */
    public void onSuccess(InstanceIdResult instanceIdResult) {
        String fcmToken = instanceIdResult.getToken();
        Logger.verbose("iap_helper", "FCM token: " + fcmToken);
        HashMap map = new HashMap();
        map.put("fcm_id", fcmToken);
        FirebaseFunctions.getInstance().getHttpsCallable("addFcmIdCallable").call(map).addOnSuccessListener(new OnSuccessListener<HttpsCallableResult>() { // from class: fa.g
            @Override // com.google.android.gms.tasks.OnSuccessListener
            public final void onSuccess(HttpsCallableResult obj) {
                SubscribeToUltraProgressDialogBottomSheet.this.onAddFcmIdResult(obj);
            }
        }).addOnFailureListener(new OnFailureListener() { // from class: fa.f
            @Override // com.google.android.gms.tasks.OnFailureListener
            public final void onFailure(Exception exc) {
                SubscribeToUltraProgressDialogBottomSheet.this.onAddFcmIdFailure(exc);
            }
        });
    }

    @Override // com.google.android.gms.tasks.OnFailureListener
    public void onFailure(Exception exc) {
        showError(2);
    }

    @Subscribe
    public void onUltraPurchaseCancelled(UltraPurchaseCancelledEvent ultraPurchaseCancelledEvent) {
        dismissAllowingStateLoss();
    }

    @Subscribe
    public void onUltraStatusChanged(UltraStatusChangedEvent ultraStatusChangedEvent) {
        try {
            getView().post(new DismissRunnable());
        } catch (Exception e) {
            Logger.logException(e);
        }
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractProgressDialogBottomSheet
    public void onProgressReady() {
        super.onProgressReady();
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this).addOnFailureListener(this);
    }
}
