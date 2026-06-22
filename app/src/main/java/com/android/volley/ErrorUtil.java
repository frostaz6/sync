package com.android.volley;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import com.resync.app.helpers.ui.StaticToast;
import java.util.Map;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.util.Logger;

/* JADX INFO: loaded from: classes.dex */
public final class ErrorUtil {
    static final String TAG = "ErrorUtil";

    private ErrorUtil() {
    }

    public static ScrollView getErrorView(Context context, VolleyError volleyError) {
        String str = TAG;
        Logger.verbose(str, "Error: " + volleyError);
        ScrollView scrollView = new ScrollView(context);
        TableLayout tableLayout = new TableLayout(context);
        tableLayout.setColumnShrinkable(1, true);
        tableLayout.setPadding((int) SizeHelper.dpToPxFloat(24), (int) SizeHelper.dpToPxFloat(10), (int) SizeHelper.dpToPxFloat(24), (int) SizeHelper.dpToPxFloat(20));
        tableLayout.addView(getRow(context, volleyError, "Volley Error", null));
        tableLayout.addView(getRow(context, volleyError, "Error class", volleyError.getClass().getSimpleName()));
        if (!com.resync.app.util.TextUtils.isEmpty(volleyError.getMessage())) {
            Logger.verbose(str, "Message: " + volleyError.getMessage());
            tableLayout.addView(getRow(context, volleyError, "Message", volleyError.getMessage()));
        }
        Logger.verbose(str, "Network time: " + volleyError.getNetworkTimeMs() + " ms");
        StringBuilder sb2 = new StringBuilder();
        sb2.append(volleyError.getNetworkTimeMs());
        sb2.append(" ms");
        tableLayout.addView(getRow(context, volleyError, "Network time", sb2.toString()));
        if (volleyError.getCause() != null) {
            Logger.verbose(str, "Error cause" + volleyError.getCause().getClass().getSimpleName());
            tableLayout.addView(getRow(context, volleyError, "Error cause", volleyError.getCause().getClass().getSimpleName()));
        }
        if (volleyError.networkResponse != null) {
            Logger.verbose(str, "Network response");
            Logger.verbose(str, "Message" + new String(volleyError.networkResponse.data));
            Logger.verbose(str, "Status code" + Integer.toString(volleyError.networkResponse.statusCode));
            Logger.verbose(str, "Not modified" + Boolean.toString(volleyError.networkResponse.notModified));
            tableLayout.addView(getRow(context, volleyError, "Network response", null));
            tableLayout.addView(getRow(context, volleyError, "Message", new String(volleyError.networkResponse.data)));
            tableLayout.addView(getRow(context, volleyError, "Status code", Integer.toString(volleyError.networkResponse.statusCode)));
            tableLayout.addView(getRow(context, volleyError, "Not modified", Boolean.toString(volleyError.networkResponse.notModified)));
            if (volleyError.networkResponse.headers != null) {
                tableLayout.addView(getRow(context, volleyError, "Headers", null));
                Map<String, String> map = volleyError.networkResponse.headers;
                for (String str2 : map.keySet()) {
                    tableLayout.addView(getRow(context, volleyError, str2, map.get(str2)));
                }
            }
        }
        scrollView.addView(tableLayout);
        return scrollView;
    }

    private static TableRow getRow(final Context context, VolleyError volleyError, final String str, final String str2) {
        TableRow tableRow = new TableRow(context);
        tableRow.setPadding(0, (int) SizeHelper.dpToPxFloat(8), 0, 0);
        TextView textView = new TextView(context);
        textView.setTextSize(1, 10.0f);
        textView.setText(str);
        tableRow.addView(textView, 0);
        if (com.resync.app.util.TextUtils.isEmpty(str2)) {
            textView.setTypeface(Typeface.DEFAULT_BOLD);
            tableRow.setPadding(0, (int) SizeHelper.dpToPxFloat(16), 0, 0);
        }
        TextView textView2 = new TextView(context);
        textView2.setEllipsize(TextUtils.TruncateAt.END);
        textView2.setHorizontallyScrolling(false);
        textView2.setSingleLine();
        textView2.setTextSize(1, 10.0f);
        textView2.setText(str2);
        textView2.setPadding((int) SizeHelper.dpToPxFloat(20), 0, 0, 0);
        if (!com.resync.app.util.TextUtils.isEmpty(str2)) {
            tableRow.setOnClickListener(new View.OnClickListener() { // from class: com.android.volley.ErrorUtil.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (!str2.contains("<html")) {
                        new AlertDialog.Builder(context).setTitle(str).setMessage(str2).setNegativeButton("Dismiss", null).setPositiveButton("Copy", new DialogInterface.OnClickListener() { // from class: com.android.volley.ErrorUtil.1.1
                            @Override // android.content.DialogInterface.OnClickListener
                            public void onClick(DialogInterface dialogInterface, int which) {
                                ((ClipboardManager) context.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("reddit sync link", str2));
                                StaticToast.showWithContext("Text copied to clipboard", context);
                            }
                        }).create().show();
                        return;
                    }
                    WebView webView = new WebView(context);
                    webView.loadData(str2, "text/html; charset=UTF-8", null);
                    new AlertDialog.Builder(context).setTitle(str + ": Webpage detected").setView(webView).setNegativeButton("Dismiss", null).create().show();
                }
            });
        }
        tableRow.addView(textView2, 1);
        return tableRow;
    }
}
