package com.resync.app.managers;

import android.content.Context;
import android.content.DialogInterface;
import com.resync.app.helpers.ui.AlertDialogHelper;

/* JADX INFO: renamed from: q8.a */
/* JADX INFO: compiled from: BlockManager.java */
/* JADX INFO: loaded from: classes.dex */
public final class BlockManager {

    /* JADX INFO: compiled from: BlockManager.java */
    static class BlockClickListener implements DialogInterface.OnClickListener {
        BlockClickListener() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int which) {
        }
    }

    public static void confirmBlockUser(Context context, String str) {
        AlertDialogHelper.newBuilder(context).setMessage("Block user " + str + "?").setPositiveButton("Block", new BlockClickListener()).setNegativeButton("Cancel", null).create().show();
    }
}
