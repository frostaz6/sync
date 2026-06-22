package com.resync.app.helpers;

import android.app.Activity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

/* JADX INFO: renamed from: t7.n */
/* JADX INFO: compiled from: FragmentHelper.java */
/* JADX INFO: loaded from: classes.dex */
public final class FragmentHelper {
    public static boolean replaceFragment(AppCompatActivity appCompatActivity, Fragment fragment, int containerId) {
        if (!isActivityValid(appCompatActivity)) {
            return false;
        }
        FragmentManager supportFragmentManager = appCompatActivity.getSupportFragmentManager();
        if (!isFragmentManagerUsable(supportFragmentManager)) {
            return false;
        }
        supportFragmentManager.beginTransaction().replace(containerId, fragment).commit();
        return true;
    }

    protected static boolean isActivityValid(Activity activity) {
        return (activity == null || activity.isFinishing() || activity.isDestroyed() || activity.isChangingConfigurations()) ? false : true;
    }

    protected static boolean isFragmentManagerUsable(FragmentManager fragmentManager) {
        return (fragmentManager == null || fragmentManager.isStateSaved()) ? false : true;
    }

    protected static boolean isFragmentOfClass(Fragment fragment, Class<? extends Fragment> cls) {
        return fragment != null && fragment.getClass().equals(cls);
    }

    public static boolean removeFragment(AppCompatActivity appCompatActivity, Class<? extends Fragment> cls, int containerId) {
        if (!isActivityValid(appCompatActivity)) {
            return false;
        }
        FragmentManager supportFragmentManager = appCompatActivity.getSupportFragmentManager();
        if (!isFragmentManagerUsable(supportFragmentManager)) {
            return false;
        }
        Fragment fragmentFindFragmentById = supportFragmentManager.findFragmentById(containerId);
        if (!isFragmentOfClass(fragmentFindFragmentById, cls)) {
            return false;
        }
        supportFragmentManager.beginTransaction().remove(fragmentFindFragmentById).commit();
        return true;
    }
}
