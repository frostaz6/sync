package com.resync.app.util;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.chunk.MediaChunk;
import com.google.android.exoplayer2.source.chunk.MediaChunkIterator;
import com.google.android.exoplayer2.trackselection.BaseTrackSelection;
import com.google.android.exoplayer2.trackselection.ExoTrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionUtil;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import com.resync.app.helpers.WindowUtils;
import com.resync.app.util.InlineVideoTrackSelection;

/* JADX INFO: renamed from: wc.g */
/* JADX INFO: compiled from: InlineVideoTrackSelection.java */
/* JADX INFO: loaded from: classes3.dex */
public class InlineVideoTrackSelection extends BaseTrackSelection {
    public static String TAG = "InlineVideoTrackSelection";

    public int selectedIndex;
    public int selectedWidth;
    public int selectedHeight;

    public static final class Factory implements ExoTrackSelection.Factory {

        private static final class PortraitFormatComparator implements Comparator<Format> {
            private PortraitFormatComparator() {
            }

            @Override // java.util.Comparator
            public int compare(Format format, Format format2) {
                return format.width - format2.width;
            }
        }

        private static final class FormatComparator implements Comparator<Format> {
            private FormatComparator() {
            }

            @Override // java.util.Comparator
            public int compare(Format format, Format format2) {
                return format.width - format2.width;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ ExoTrackSelection createTrackSelection(TrackGroup trackGroup, Format format, int[] iArr, ExoTrackSelection.Definition definition) {
            return new InlineVideoTrackSelection(trackGroup, format, iArr);
        }

        @Override // com.google.android.exoplayer2.trackselection.ExoTrackSelection.Factory
        public ExoTrackSelection[] createTrackSelections(ExoTrackSelection.Definition[] definitionArr, BandwidthMeter bandwidthMeter, MediaSource.MediaPeriodId mediaPeriodId, Timeline timeline) {
            int index = 0;
            final TrackGroup trackGroup = definitionArr[0].group;
            final int[] iArr = definitionArr[0].tracks;
            Logger.verbose(InlineVideoTrackSelection.TAG, "Sorting the available tracks");
            ArrayList arrayList = new ArrayList();
            for (int track : iArr) {
                arrayList.add(trackGroup.getFormat(track));
            }
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                Logger.verbose("Format: " + ((Format) it.next()).id);
            }
            if (trackGroup.getFormat(0).width >= trackGroup.getFormat(0).height) {
                Collections.sort(arrayList, new FormatComparator());
            } else {
                Collections.sort(arrayList, new PortraitFormatComparator());
            }
            Format formatSelected = (Format) arrayList.get(arrayList.size() - 1);
            Logger.verbose(InlineVideoTrackSelection.TAG, "Defaulting temporarily to the best format: " + formatSelected.width + "x" + formatSelected.height);
            for (int i = 0; i < arrayList.size() - 1; i++) {
                Logger.verbose(InlineVideoTrackSelection.TAG, "Available- Index: " + i + " [" + ((Format) arrayList.get(i)).width + "x" + ((Format) arrayList.get(i)).height + "] [" + WindowUtils.getDisplayWidth() + "x" + WindowUtils.getDisplayHeight() + "]");
            }
            while (true) {
                if (index >= arrayList.size() - 1) {
                    break;
                }
                Logger.verbose(InlineVideoTrackSelection.TAG, "Index: " + index + " [" + ((Format) arrayList.get(index)).width + "x" + ((Format) arrayList.get(index)).height + "] [" + WindowUtils.getDisplayWidth() + "x" + WindowUtils.getDisplayHeight() + "]");
                if (((Format) arrayList.get(index)).width >= ((Format) arrayList.get(index)).height) {
                    if (((Format) arrayList.get(index)).width >= WindowUtils.getDisplayWidth()) {
                        Logger.verbose(InlineVideoTrackSelection.TAG, "Selected an adaptive landscape resolution: " + ((Format) arrayList.get(index)).width + " - " + ((Format) arrayList.get(index)).height + " INDEX: " + index);
                        formatSelected = (Format) arrayList.get(index);
                        break;
                    }
                    index++;
                } else {
                    if (((Format) arrayList.get(index)).height >= WindowUtils.getDisplayHeight()) {
                        Logger.verbose(InlineVideoTrackSelection.TAG, "Selected an adaptive portrait resolution: " + ((Format) arrayList.get(index)).width + " - " + ((Format) arrayList.get(index)).height + " INDEX: " + index);
                        formatSelected = (Format) arrayList.get(index);
                        break;
                    }
                    index++;
                }
            }
            final Format format = formatSelected;
            return TrackSelectionUtil.createTrackSelectionsForDefinitions(definitionArr, new TrackSelectionUtil.AdaptiveTrackSelectionFactory() { // from class: wc.h
                @Override // com.google.android.exoplayer2.trackselection.TrackSelectionUtil.AdaptiveTrackSelectionFactory
                public final ExoTrackSelection createAdaptiveTrackSelection(ExoTrackSelection.Definition definition) {
                    return createTrackSelection(trackGroup, format, iArr, definition);
                }
            });
        }
    }

    public InlineVideoTrackSelection(TrackGroup trackGroup, Format format, int... iArr) {
        super(trackGroup, iArr);
        int index = 0;
        this.selectedIndex = 0;
        while (true) {
            if (index >= length()) {
                break;
            }
            Format format2 = getFormat(index);
            if (format2.width == format.width && format2.height == format.height) {
                Logger.verbose(TAG, "Returning stream: " + format2.width + " - " + format2.height);
                this.selectedIndex = index;
                break;
            }
            index++;
        }
        this.selectedWidth = format.width;
        this.selectedHeight = format.height;
    }

    @Override // com.google.android.exoplayer2.trackselection.ExoTrackSelection
    public int getSelectedIndex() {
        return this.selectedIndex;
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
