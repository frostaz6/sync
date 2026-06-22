package androidx.core.app;

import android.app.Activity;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.net.Uri;
import androidx.core.util.Preconditions;
import java.util.ArrayList;

/* JADX INFO: renamed from: androidx.core.app.m */
/* JADX INFO: compiled from: ShareCompat.java */
/* JADX INFO: loaded from: classes.dex */
public class ShareCompat_m {
    private final Context context;
    private final Intent intent;
    private ArrayList<String> toAddresses;
    private ArrayList<String> ccAddresses;
    private ArrayList<String> bccAddresses;
    private ArrayList<Uri> streams;

    public ShareCompat_m(Context context) {
        Activity activity;
        this.context = (Context) Preconditions.checkNotNull(context);
        Intent action = new Intent().setAction("android.intent.action.SEND");
        this.intent = action;
        action.putExtra("androidx.core.app.EXTRA_CALLING_PACKAGE", context.getPackageName());
        action.putExtra("android.support.v4.app.EXTRA_CALLING_PACKAGE", context.getPackageName());
        action.addFlags(524288);
        while (true) {
            if (!(context instanceof ContextWrapper)) {
                activity = null;
                break;
            } else {
                if (context instanceof Activity) {
                    activity = (Activity) context;
                    break;
                }
                context = ((ContextWrapper) context).getBaseContext();
            }
        }
        if (activity != null) {
            ComponentName componentName = activity.getComponentName();
            this.intent.putExtra("androidx.core.app.EXTRA_CALLING_ACTIVITY", componentName);
            this.intent.putExtra("android.support.v4.app.EXTRA_CALLING_ACTIVITY", componentName);
        }
    }

    private void combineArrayExtra(String str, ArrayList<String> arrayList) {
        String[] stringArrayExtra = this.intent.getStringArrayExtra(str);
        int length = stringArrayExtra != null ? stringArrayExtra.length : 0;
        String[] strArr = new String[arrayList.size() + length];
        arrayList.toArray(strArr);
        if (stringArrayExtra != null) {
            System.arraycopy(stringArrayExtra, 0, strArr, arrayList.size(), length);
        }
        this.intent.putExtra(str, strArr);
    }

    @Deprecated
    public static ShareCompat_m from(Activity activity) {
        return new ShareCompat_m(activity);
    }

    public ShareCompat_m addStream(Uri uri) {
        if (this.streams == null) {
            this.streams = new ArrayList<>();
        }
        this.streams.add(uri);
        return this;
    }

    public Intent getIntent() {
        ArrayList<String> arrayList = this.toAddresses;
        if (arrayList != null) {
            combineArrayExtra("android.intent.extra.EMAIL", arrayList);
            this.toAddresses = null;
        }
        ArrayList<String> arrayList2 = this.ccAddresses;
        if (arrayList2 != null) {
            combineArrayExtra("android.intent.extra.CC", arrayList2);
            this.ccAddresses = null;
        }
        ArrayList<String> arrayList3 = this.bccAddresses;
        if (arrayList3 != null) {
            combineArrayExtra("android.intent.extra.BCC", arrayList3);
            this.bccAddresses = null;
        }
        ArrayList<Uri> arrayList4 = this.streams;
        if (arrayList4 != null && arrayList4.size() > 1) {
            this.intent.setAction("android.intent.action.SEND_MULTIPLE");
            this.intent.putParcelableArrayListExtra("android.intent.extra.STREAM", this.streams);
            migrateExtraStreamToClipData(this.intent, this.streams);
        } else {
            this.intent.setAction("android.intent.action.SEND");
            ArrayList<Uri> arrayList5 = this.streams;
            if (arrayList5 == null || arrayList5.isEmpty()) {
                this.intent.removeExtra("android.intent.extra.STREAM");
                removeExtraStreamItem(this.intent);
            } else {
                this.intent.putExtra("android.intent.extra.STREAM", this.streams.get(0));
                migrateExtraStreamToClipData(this.intent, this.streams);
            }
        }
        return this.intent;
    }

    public ShareCompat_m setStream(Uri uri) {
        this.streams = null;
        if (uri != null) {
            addStream(uri);
        }
        return this;
    }

    private static void migrateExtraStreamToClipData(Intent intent, ArrayList<Uri> arrayList) {
        ClipData clipData = new ClipData(null, new String[]{intent.getType()}, new ClipData.Item(intent.getCharSequenceExtra("android.intent.extra.TEXT"), intent.getStringExtra("android.intent.extra.HTML_TEXT"), null, arrayList.get(0)));
        int size = arrayList.size();
        for (int i = 1; i < size; i++) {
            clipData.addItem(new ClipData.Item(arrayList.get(i)));
        }
        intent.setClipData(clipData);
        intent.addFlags(1);
    }

    private static void removeExtraStreamItem(Intent intent) {
        intent.setClipData(null);
        intent.setFlags(intent.getFlags() & (-2));
    }
}
