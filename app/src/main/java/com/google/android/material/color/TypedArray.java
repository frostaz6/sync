package com.google.android.material.color;

import android.content.res.ColorStateList;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: renamed from: j0.i */
/* JADX INFO: compiled from: TypedArray.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(bv = {}, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0004\u001a\u0016\u0010\u0004\u001a\u00020\u0003*\u00020\u00002\b\b\u0001\u0010\u0002\u001a\u00020\u0001H\u0002\u001a\u0016\u0010\u0005\u001a\u00020\u0001*\u00020\u00002\b\b\u0001\u0010\u0002\u001a\u00020\u0001H\u0007\u001a\u0014\u0010\u0007\u001a\u00020\u0006*\u00020\u00002\b\b\u0001\u0010\u0002\u001a\u00020\u0001\u001a\u0014\u0010\t\u001a\u00020\b*\u00020\u00002\b\b\u0001\u0010\u0002\u001a\u00020\u0001\u001a\u0016\u0010\n\u001a\u00020\u0001*\u00020\u00002\b\b\u0001\u0010\u0002\u001a\u00020\u0001H\u0007\u001a\u0016\u0010\u000b\u001a\u00020\u0001*\u00020\u00002\b\b\u0001\u0010\u0002\u001a\u00020\u0001H\u0007¨\u0006\f"}, d2 = {"Landroid/content/res/TypedArray;", "", "index", "", "a", "b", "Landroid/content/res/ColorStateList;", "c", "", "d", "e", "f", "core-ktx_release"}, k = 2, mv = {1, 7, 1})
public final class TypedArray {
    private static final void checkAttribute(android.content.res.TypedArray typedArray, int index) {
        if (!typedArray.hasValue(index)) {
            throw new IllegalArgumentException("Attribute not defined in set.");
        }
    }

    public static final int getColorOrThrow(android.content.res.TypedArray typedArray, int index) {
        Intrinsics.checkNotNullParameter(typedArray, "<this>");
        checkAttribute(typedArray, index);
        return typedArray.getColor(index, 0);
    }

    public static final ColorStateList getColorStateListOrThrow(android.content.res.TypedArray typedArray, int index) {
        Intrinsics.checkNotNullParameter(typedArray, "<this>");
        checkAttribute(typedArray, index);
        ColorStateList colorStateList = typedArray.getColorStateList(index);
        if (colorStateList != null) {
            return colorStateList;
        }
        throw new IllegalStateException("Attribute value was not a color or color state list.".toString());
    }

    public static final float getDimensionOrThrow(android.content.res.TypedArray typedArray, int index) {
        Intrinsics.checkNotNullParameter(typedArray, "<this>");
        checkAttribute(typedArray, index);
        return typedArray.getDimension(index, 0.0f);
    }

    public static final int getDimensionPixelSizeOrThrow(android.content.res.TypedArray typedArray, int index) {
        Intrinsics.checkNotNullParameter(typedArray, "<this>");
        checkAttribute(typedArray, index);
        return typedArray.getDimensionPixelSize(index, 0);
    }

    public static final int getResourceIdOrThrow(android.content.res.TypedArray typedArray, int index) {
        Intrinsics.checkNotNullParameter(typedArray, "<this>");
        checkAttribute(typedArray, index);
        return typedArray.getResourceId(index, 0);
    }
}
