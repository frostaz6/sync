package com.resync.app.ui.viewholders.posts.card;

import android.view.View;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.ui.views.posts.cards.CardSelftextPreviewTextView;
import com.resync.app.ui.views.text.spannable.children.HtmlTableView;

/* JADX INFO: loaded from: classes3.dex */
public class CardSelftextHolder_ViewBinding extends AbstractCardPostHolder_ViewBinding {

    private CardSelftextHolder target;

    public CardSelftextHolder_ViewBinding(CardSelftextHolder cardSelftextHolder, View view) {
        super(cardSelftextHolder, view);
        this.target = cardSelftextHolder;
        cardSelftextHolder.selftext = (CardSelftextPreviewTextView) Utils.findRequiredViewAsType(view, R.id.selftext, "field 'selftext'", CardSelftextPreviewTextView.class);
        cardSelftextHolder.mTableView = (HtmlTableView) Utils.findRequiredViewAsType(view, R.id.holder_card_tableview, "field 'mTableView'", HtmlTableView.class);
    }

    @Override
    public void unbind() {
        CardSelftextHolder target = this.target;
        if (target == null) throw new IllegalStateException("Bindings already cleared.");
        this.target = null;
        target.mTableView = null;
        super.unbind();
    }
}
