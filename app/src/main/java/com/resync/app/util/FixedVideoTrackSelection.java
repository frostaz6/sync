package com.resync.app.util;

import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.trackselection.BaseTrackSelection;

/* JADX INFO: renamed from: wc.e */
/* JADX INFO: compiled from: FixedVideoTrackSelection.java */
/* JADX INFO: loaded from: classes3.dex */
public abstract class FixedVideoTrackSelection extends BaseTrackSelection {

    public static String TAG = "FixedVideoTrackSelection";

    protected FixedVideoTrackSelection(TrackGroup trackGroup, int... iArr) {
        super(trackGroup, iArr);
    }
}
