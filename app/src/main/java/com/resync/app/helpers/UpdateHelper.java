package com.resync.app.helpers;

import android.app.Activity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;

/* compiled from: UpdateHelper.java */
public class UpdateHelper implements InstallStateUpdatedListener {

    private AppUpdateManager appUpdateManager;
    private final int requestCode = 1001;

    private /* synthetic */ void onUpdateInfo(Activity activity, AppUpdateInfo appUpdateInfo) {
        if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
            try {
                this.appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.FLEXIBLE, activity, this.requestCode);
            } catch (Exception unused) {
            }
        } else if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
            this.appUpdateManager.completeUpdate();
        }
    }

    @Override // com.google.android.play.core.install.InstallStateUpdatedListener
    public void onStateUpdate(InstallState installState) {
        if (installState.installStatus() == InstallStatus.DOWNLOADED) {
            this.appUpdateManager.completeUpdate();
        }
    }

    public void checkForUpdate(final Activity activity) {
        AppUpdateManager manager = AppUpdateManagerFactory.create(activity);
        this.appUpdateManager = manager;
        manager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override // com.google.android.gms.tasks.OnSuccessListener
            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                UpdateHelper.this.onUpdateInfo(activity, appUpdateInfo);
            }
        });
        this.appUpdateManager.registerListener(this);
    }
}
