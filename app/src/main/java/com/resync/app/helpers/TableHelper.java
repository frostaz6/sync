package com.resync.app.helpers;

import android.content.Context;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import com.resync.app.ui.views.text.spannable.children.HtmlTextView;
import com.resync.app.ui.views.util.ConsumingHorizontalScrollView;
import java.util.Iterator;
import com.resync.app.text.html.Options;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.resync.app.model.Post;

/* JADX INFO: renamed from: t7.l0 */
/* JADX INFO: compiled from: TableHelper.java */
/* JADX INFO: loaded from: classes.dex */
public class TableHelper {
    public static View buildTableView(Context context, String html, Post post) {
        int padding = (int) SizeHelper.dpToPxFloat(4);
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(-2, -2);
        TableLayout tableLayout = new TableLayout(context);
        tableLayout.setLayoutParams(new TableLayout.LayoutParams(-2, -2));
        System.currentTimeMillis();
        Iterator<Element> it = Jsoup.parse(html).select("table").iterator();
        while (it.hasNext()) {
            Elements elementsSelect = it.next().select("tr");
            for (int rowIndex = 0; rowIndex < elementsSelect.size(); rowIndex++) {
                TableRow tableRow = new TableRow(context);
                tableRow.setLayoutParams(layoutParams);
                if (rowIndex == 0) {
                    Elements elementsSelect2 = elementsSelect.get(rowIndex).select("th");
                    for (int cellIndex = 0; cellIndex < elementsSelect2.size(); cellIndex++) {
                        HtmlTextView htmlTextView = new HtmlTextView(context, 3, 5, 1);
                        tableRow.addView(htmlTextView);
                        htmlTextView.setHtmlWithOptions(Options.compact().withPost(post), elementsSelect2.get(cellIndex).html());
                        htmlTextView.setPadding(padding, padding, padding, padding);
                    }
                } else {
                    Elements elementsSelect3 = elementsSelect.get(rowIndex).select("td");
                    for (int cellIndex = 0; cellIndex < elementsSelect3.size(); cellIndex++) {
                        HtmlTextView htmlTextView2 = new HtmlTextView(context, 9, 5, 1);
                        tableRow.addView(htmlTextView2);
                        htmlTextView2.setHtmlWithOptions(Options.compact().withPost(post), elementsSelect3.get(cellIndex).html());
                        htmlTextView2.setPadding(padding, padding, padding, padding);
                    }
                }
                tableLayout.addView(tableRow);
            }
        }
        ConsumingHorizontalScrollView consumingHorizontalScrollView = new ConsumingHorizontalScrollView(context);
        consumingHorizontalScrollView.setClipToPadding(false);
        consumingHorizontalScrollView.addView(tableLayout);
        consumingHorizontalScrollView.setHorizontalScrollBarEnabled(false);
        consumingHorizontalScrollView.setOverScrollMode(2);
        return consumingHorizontalScrollView;
    }
}
