package com.resync.app.helpers;

import android.view.View;

/* JADX INFO: renamed from: t7.u */
/* JADX INFO: compiled from: PaddingHelper.java */
/* JADX INFO: loaded from: classes.dex */
public final class PaddingHelper {
    public static void clearPadding(View view) {
        view.setPadding(0, 0, 0, 0);
    }

    public static void setPaddingBottom(View view, int bottom) {
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), SizeHelper.dpToPx(bottom));
    }

    public static void setPaddingStart(View view, int start) {
        view.setPadding(SizeHelper.dpToPx(start), view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
    }

    public static void setPaddingAll(View view, int padding) {
        int paddingPx = SizeHelper.dpToPx(padding);
        view.setPadding(paddingPx, paddingPx, paddingPx, paddingPx);
    }

    public static void setPaddingHorizontal(View view, int left, int right) {
        view.setPadding(SizeHelper.dpToPx(left), view.getPaddingTop(), SizeHelper.dpToPx(right), view.getPaddingBottom());
    }

    public static void setPaddingLeftRightBottom(View view, int left, int right, int bottom) {
        view.setPadding(SizeHelper.dpToPx(left), view.getPaddingTop(), SizeHelper.dpToPx(right), SizeHelper.dpToPx(bottom));
    }

    public static void setPaddingLeftTopRight(View view, int left, int top, int right) {
        view.setPadding(SizeHelper.dpToPx(left), SizeHelper.dpToPx(top), SizeHelper.dpToPx(right), view.getPaddingBottom());
    }

    public static void setPaddingLTRB(View view, int left, int top, int right, int bottom) {
        view.setPadding(SizeHelper.dpToPx(left), SizeHelper.dpToPx(top), SizeHelper.dpToPx(right), SizeHelper.dpToPx(bottom));
    }

    public static void setPaddingEnd(View view, int end) {
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), SizeHelper.dpToPx(end), view.getPaddingBottom());
    }

    public static void setPaddingTopBottom(View view, int top, int bottom) {
        view.setPadding(view.getPaddingLeft(), SizeHelper.dpToPx(top), view.getPaddingRight(), SizeHelper.dpToPx(bottom));
    }

    public static void setPaddingTop(View view, int top) {
        view.setPadding(view.getPaddingLeft(), SizeHelper.dpToPx(top), view.getPaddingRight(), view.getPaddingBottom());
    }
}
