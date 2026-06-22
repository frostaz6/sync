package com.resync.app.holders;

import android.content.Context;
import android.view.View;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import com.resync.app.helpers.ContextHelper;

/* JADX INFO: renamed from: ob.a */
/* JADX INFO: compiled from: BaseViewHolder.java */
/* JADX INFO: loaded from: classes3.dex */
public class BaseViewHolder extends RecyclerView.ViewHolder {

    protected Context context;

    public BaseViewHolder(Context context, View view) {
        super(view);
        this.context = context;
    }

    public Context getContext() {
        return this.context;
    }

    public FragmentManager getFragmentManager() {
        return ContextHelper.getFragmentManager(this.context);
    }
}
