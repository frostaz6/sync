package com.resync.app.events;

/* JADX INFO: renamed from: h7.h0 */
/* JADX INFO: compiled from: TimeSelectedEvent.java */
/* JADX INFO: loaded from: classes.dex */
public final class TimeSelectedEvent {

    public int startHour;
    public int startMinute;
    public int endHour;
    public int endMinute;

    public TimeSelectedEvent(int startHour, int startMinute, int endHour, int endMinute) {
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endHour = endHour;
        this.endMinute = endMinute;
    }
}
