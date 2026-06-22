package com.resync.app.helpers;

import android.graphics.Typeface;
import com.resync.app.RedditApplication;
import java.lang.ref.WeakReference;
import com.resync.app.util.Logger;

/* JADX INFO: renamed from: t7.r0 */
/* JADX INFO: compiled from: TypefaceManager.java */
/* JADX INFO: loaded from: classes.dex */
public final class TypefaceManager {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static String LOG_TAG = "TypefaceManager";
    protected static WeakReference<Typeface> cacheRegular;
    protected static WeakReference<Typeface> cacheLight;
    protected static WeakReference<Typeface> cacheMedium;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    protected static WeakReference<Typeface> cacheBold;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    protected static WeakReference<Typeface> cacheSlab;
    protected static WeakReference<Typeface> cacheSlabLight;
    protected static WeakReference<Typeface> cacheCondensed;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    protected static WeakReference<Typeface> cacheCondensedBold;
    protected static WeakReference<Typeface> cacheItalic;
    protected static WeakReference<Typeface> cacheSansSerif;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    protected static WeakReference<Typeface> cacheProductSans;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    protected static WeakReference<Typeface> cacheGoogleSansMedium;
    protected static WeakReference<Typeface> cacheGoogleSansBold;
    protected static WeakReference<Typeface> cacheGoogleSansItalic;
    protected static WeakReference<Typeface> cacheMono;

    public static synchronized Typeface getRegular() {
        return get(9);
    }

    public static synchronized Typeface getBold() {
        return get(3);
    }

    public static synchronized Typeface getMedium() {
        return get(2);
    }

    public static synchronized Typeface get(int typefaceId) {
        if (RedditApplication.getAppContext() == null) {
            return null;
        }
        if (typefaceId == 9) {
            if (isValid(cacheRegular)) {
                return cacheRegular.get();
            }
            WeakReference<Typeface> weakReferenceF = createWeakRef(typefaceId);
            cacheRegular = weakReferenceF;
            return weakReferenceF.get();
        }
        if (typefaceId == 1) {
            if (isValid(cacheLight)) {
                return cacheLight.get();
            }
            WeakReference<Typeface> weakReferenceF2 = createWeakRef(typefaceId);
            cacheLight = weakReferenceF2;
            return weakReferenceF2.get();
        }
        if (typefaceId == 2) {
            if (isValid(cacheMedium)) {
                return cacheMedium.get();
            }
            WeakReference<Typeface> weakReferenceF3 = createWeakRef(typefaceId);
            cacheMedium = weakReferenceF3;
            return weakReferenceF3.get();
        }
        if (typefaceId == 3) {
            if (isValid(cacheBold)) {
                return cacheBold.get();
            }
            WeakReference<Typeface> weakReferenceF4 = createWeakRef(typefaceId);
            cacheBold = weakReferenceF4;
            return weakReferenceF4.get();
        }
        if (typefaceId == 4) {
            if (isValid(cacheSlab)) {
                return cacheSlab.get();
            }
            WeakReference<Typeface> weakReferenceF5 = createWeakRef(typefaceId);
            cacheSlab = weakReferenceF5;
            return weakReferenceF5.get();
        }
        if (typefaceId == 5) {
            if (isValid(cacheSlabLight)) {
                return cacheSlabLight.get();
            }
            WeakReference<Typeface> weakReferenceF6 = createWeakRef(typefaceId);
            cacheSlabLight = weakReferenceF6;
            return weakReferenceF6.get();
        }
        if (typefaceId == 6) {
            if (isValid(cacheCondensed)) {
                return cacheCondensed.get();
            }
            WeakReference<Typeface> weakReferenceF7 = createWeakRef(typefaceId);
            cacheCondensed = weakReferenceF7;
            return weakReferenceF7.get();
        }
        if (typefaceId == 7) {
            if (isValid(cacheCondensedBold)) {
                return cacheCondensedBold.get();
            }
            WeakReference<Typeface> weakReferenceF8 = createWeakRef(typefaceId);
            cacheCondensedBold = weakReferenceF8;
            return weakReferenceF8.get();
        }
        if (typefaceId == 8) {
            if (isValid(cacheItalic)) {
                return cacheItalic.get();
            }
            WeakReference<Typeface> weakReferenceF9 = createWeakRef(typefaceId);
            cacheItalic = weakReferenceF9;
            return weakReferenceF9.get();
        }
        if (typefaceId == 10) {
            if (isValid(cacheSansSerif)) {
                return cacheSansSerif.get();
            }
            WeakReference<Typeface> weakReference = new WeakReference<Typeface>(Typeface.SANS_SERIF);
            cacheSansSerif = weakReference;
            return weakReference.get();
        }
        if (typefaceId == 11) {
            if (isValid(cacheProductSans)) {
                return cacheProductSans.get();
            }
            WeakReference<Typeface> weakReferenceF10 = createWeakRef(typefaceId);
            cacheProductSans = weakReferenceF10;
            return weakReferenceF10.get();
        }
        if (typefaceId == 12) {
            if (isValid(cacheGoogleSansMedium)) {
                return cacheGoogleSansMedium.get();
            }
            WeakReference<Typeface> weakReferenceF11 = createWeakRef(typefaceId);
            cacheGoogleSansMedium = weakReferenceF11;
            return weakReferenceF11.get();
        }
        if (typefaceId == 13) {
            if (isValid(cacheGoogleSansBold)) {
                return cacheGoogleSansBold.get();
            }
            WeakReference<Typeface> weakReferenceF12 = createWeakRef(typefaceId);
            cacheGoogleSansBold = weakReferenceF12;
            return weakReferenceF12.get();
        }
        if (typefaceId == 14) {
            if (isValid(cacheGoogleSansItalic)) {
                return cacheGoogleSansItalic.get();
            }
            WeakReference<Typeface> weakReferenceF13 = createWeakRef(typefaceId);
            cacheGoogleSansItalic = weakReferenceF13;
            return weakReferenceF13.get();
        }
        if (typefaceId != 15) {
            throw new RuntimeException("Typeface not supported: " + typefaceId);
        }
        if (isValid(cacheMono)) {
            return cacheMono.get();
        }
        WeakReference<Typeface> weakReferenceF14 = createWeakRef(typefaceId);
        cacheMono = weakReferenceF14;
        return weakReferenceF14.get();
    }

    protected static synchronized String getAssetName(int typefaceId) {
        if (typefaceId == 9) {
            return "Roboto-Regular.ttf";
        }
        if (typefaceId == 1) {
            return "Roboto-Light.ttf";
        }
        if (typefaceId == 2) {
            return "Roboto-Medium.ttf";
        }
        if (typefaceId == 3) {
            return "Roboto-Bold.ttf";
        }
        if (typefaceId == 4) {
            return "Roboto-Slab.ttf";
        }
        if (typefaceId == 5) {
            return "RobotoSlab-Light.ttf";
        }
        if (typefaceId == 6) {
            return "RobotoCondensed-Regular.ttf";
        }
        if (typefaceId == 7) {
            return "RobotoCondensed-Bold.ttf";
        }
        if (typefaceId == 8) {
            return "Roboto-Italic.ttf";
        }
        if (typefaceId == 11) {
            return "ProductSans-Regular.ttf";
        }
        if (typefaceId == 12) {
            return "GoogleSans-Medium.ttf";
        }
        if (typefaceId == 13) {
            return "GoogleSans-Bold.ttf";
        }
        if (typefaceId == 14) {
            return "GoogleSans-Italic.ttf";
        }
        if (typefaceId == 15) {
            return "Roboto-Mono.ttf";
        }
        throw new RuntimeException("Typeface not supported: " + typefaceId);
    }

    protected static synchronized WeakReference<Typeface> createWeakRef(int typefaceId) {
        return new WeakReference<Typeface>(loadTypeface(typefaceId));
    }

    protected static synchronized boolean isValid(WeakReference<Typeface> ref) {
        if (ref != null && ref.get() != null) {
            return true;
        }
        return false;
    }

    protected static synchronized Typeface loadTypeface(int typefaceId) {
        Typeface typefaceCreateFromAsset;
        typefaceCreateFromAsset = null;
        String assetName = getAssetName(typefaceId);
        try {
            Logger.verbose(LOG_TAG, "Loading typeface: " + assetName);
            typefaceCreateFromAsset = Typeface.createFromAsset(RedditApplication.getAppContext().getAssets(), assetName);
        } catch (Exception e) {
            Logger.logException(e);
            CrashlyticsHelper.logException(e);
        }
        return typefaceCreateFromAsset;
    }
}
