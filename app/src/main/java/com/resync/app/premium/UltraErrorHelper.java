package com.resync.app.premium;

/* JADX INFO: renamed from: uc.a */
/* JADX INFO: compiled from: UltraErrorHelper.java */
/* JADX INFO: loaded from: classes3.dex */
public class UltraErrorHelper {
    public static String getErrorMessage(int errorCode) {
        switch (errorCode) {
            case 0:
                return "Could not connect to Google Play (" + errorCode + ")";
            case 1:
                return "Error retrieving Sync Ultra information (" + errorCode + ")";
            case 2:
                return "Failed to start subscription purchase (" + errorCode + ")";
            case 3:
                return "Could not get subscription details (" + errorCode + ")";
            case 4:
            case 5:
                return "Error completing setup. Please check in Google Play as your subscription may still be active.";
            case 6:
                return "Your subscription to Sync Ultra details from Google Play have failed to validate. This is usually due to cancelled or expired subscriptions. Please check in Google Play as your subscription may still be active.";
            case 7:
                return "Your subscription to Sync Ultra could not be found. This may be due to Google Play failing to return your subscription or if you are using a different Google Account. Please check in Google Play as your subscription may still be active.";
            default:
                throw new RuntimeException("Unhandled error code" + errorCode);
        }
    }
}
