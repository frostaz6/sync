package com.resync.app.util;

import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.chunk.MediaChunk;
import com.google.android.exoplayer2.source.chunk.MediaChunkIterator;
import com.google.android.exoplayer2.trackselection.BaseTrackSelection;
import com.google.android.exoplayer2.trackselection.ExoTrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionUtil;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import java.util.List;
import com.resync.app.util.BestVideoTrackSelection;

/* JADX INFO: renamed from: wc.c */
/* JADX INFO: compiled from: BestVideoTrackSelection.java */
/* JADX INFO: loaded from: classes3.dex */
public class BestVideoTrackSelection extends BaseTrackSelection {

    public int selectedWidth;
    public int selectedHeight;

    public static final class Factory implements ExoTrackSelection.Factory {
        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ ExoTrackSelection createTrackSelection(ExoTrackSelection.Definition definition) {
            return new BestVideoTrackSelection(definition.group, definition.tracks);
        }

        @Override // com.google.android.exoplayer2.trackselection.ExoTrackSelection.Factory
        public ExoTrackSelection[] createTrackSelections(ExoTrackSelection.Definition[] definitionArr, BandwidthMeter bandwidthMeter, MediaSource.MediaPeriodId mediaPeriodId, Timeline timeline) {
            return TrackSelectionUtil.createTrackSelectionsForDefinitions(definitionArr, new TrackSelectionUtil.AdaptiveTrackSelectionFactory() { // from class: wc.b
                @Override // com.google.android.exoplayer2.trackselection.TrackSelectionUtil.AdaptiveTrackSelectionFactory
                public final ExoTrackSelection createAdaptiveTrackSelection(ExoTrackSelection.Definition definition) {
                    return BestVideoTrackSelection.Factory.createTrackSelection(definition);
                }
            });
        }
    }

    public BestVideoTrackSelection(TrackGroup trackGroup, int... iArr) {
        super(trackGroup, iArr);
    }

    @Override // com.google.android.exoplayer2.trackselection.ExoTrackSelection
    public int getSelectedIndex() {
        Logger.verbose(FixedVideoTrackSelection.TAG, "Defaulting temporarily to the best format: " + getFormat(0).width + "x" + getFormat(0).height);
        this.selectedWidth = getFormat(0).width;
        this.selectedHeight = getFormat(0).height;
        return 0;
    }

    @Override // com.google.android.exoplayer2.trackselection.ExoTrackSelection
    public Object getSelectionData() {
        return null;
    }

    @Override // com.google.android.exoplayer2.trackselection.ExoTrackSelection
    public int getSelectionReason() {
        return 2;
    }

    @Override // com.google.android.exoplayer2.trackselection.ExoTrackSelection
    public void updateSelectedTrack(long playbackPositionUs, long bufferedDurationUs, long availableDurationUs, List<? extends MediaChunk> list, MediaChunkIterator[] mediaChunkIteratorArr) {
    }
}
