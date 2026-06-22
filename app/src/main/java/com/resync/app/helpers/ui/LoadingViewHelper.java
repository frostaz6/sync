package com.resync.app.helpers.ui;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.android.volley.ErrorUtil;
import com.android.volley.VolleyError;
import com.android.volley.errors.OAuthError;
import com.android.volley.errors.RateLimitedError;
import com.google.android.material.snackbar.Snackbar;
import com.resync.app.R;
import com.resync.app.ui.activities.BaseActivity;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.helpers.ContextHelper;
import com.resync.app.dialogs.RateLimitedDeviceAlertDialogBottomSheet;
import com.resync.app.util.Logger;
import com.resync.app.net.SubredditNotFoundError;

/* JADX INFO: renamed from: gb.g */
/* JADX INFO: compiled from: LoadingViewHelper.java */
/* JADX INFO: loaded from: classes2.dex */
public class LoadingViewHelper {

    Context context;
    SwipeRefreshLayout swipeRefreshLayout;
    Snackbar snackbar;
    VolleyError lastError;

    RetryListener retryListener;

    View anchorView;

    /* JADX INFO: renamed from: gb.g$a */
    /* JADX INFO: compiled from: LoadingViewHelper.java */
    class TextLongClickListener implements View.OnLongClickListener {

        final /* synthetic */ Runnable debugRunnable;

        TextLongClickListener(Runnable runnable) {
            this.debugRunnable = runnable;
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            Runnable runnable = this.debugRunnable;
            if (runnable != null) {
                view.post(runnable);
                return true;
            }
            StaticToast.showWithContext(LoadingViewHelper.this.context, "No debug information available");
            return true;
        }
    }

    /* JADX INFO: renamed from: gb.g$b */
    /* JADX INFO: compiled from: LoadingViewHelper.java */
    class RateLimitRetryListener implements RetryListener {
        RateLimitRetryListener() {
        }

        @Override // com.resync.app.helpers.ui.LoadingViewHelper.RetryListener
        public void onRetryAction() {
            FragmentUtils.show(RateLimitedDeviceAlertDialogBottomSheet.class, ContextHelper.getFragmentManager(LoadingViewHelper.this.context));
        }
    }

    /* JADX INFO: renamed from: gb.g$c */
    /* JADX INFO: compiled from: LoadingViewHelper.java */
    class RateLimitRunnable implements Runnable {
        RateLimitRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            FragmentUtils.show(RateLimitedDeviceAlertDialogBottomSheet.class, ContextHelper.getFragmentManager(LoadingViewHelper.this.context));
        }
    }

    /* JADX INFO: renamed from: gb.g$d */
    /* JADX INFO: compiled from: LoadingViewHelper.java */
    class InvalidTokenRunnable implements Runnable {

        final /* synthetic */ VolleyError error;

        InvalidTokenRunnable(VolleyError volleyError) {
            this.error = volleyError;
        }

        @Override // java.lang.Runnable
        public void run() {
            AlertDialogHelper.newBuilder(LoadingViewHelper.this.context).setView(ErrorUtil.getErrorView(LoadingViewHelper.this.context, this.error)).setTitle("Invalid token").create().show();
        }
    }

    /* JADX INFO: renamed from: gb.g$e */
    /* JADX INFO: compiled from: LoadingViewHelper.java */
    class NotFoundRunnable implements Runnable {

        final /* synthetic */ VolleyError error;

        NotFoundRunnable(VolleyError volleyError) {
            this.error = volleyError;
        }

        @Override // java.lang.Runnable
        public void run() {
            AlertDialogHelper.newBuilder(LoadingViewHelper.this.context).setView(ErrorUtil.getErrorView(LoadingViewHelper.this.context, this.error)).setTitle(this.error.getMessage()).create().show();
        }
    }

    /* JADX INFO: renamed from: gb.g$f */
    /* JADX INFO: compiled from: LoadingViewHelper.java */
    class HttpErrorRunnable implements Runnable {

        final /* synthetic */ VolleyError error;

        HttpErrorRunnable(VolleyError volleyError) {
            this.error = volleyError;
        }

        @Override // java.lang.Runnable
        public void run() {
            AlertDialogHelper.newBuilder(LoadingViewHelper.this.context).setView(ErrorUtil.getErrorView(LoadingViewHelper.this.context, this.error)).setTitle("Error loading page").create().show();
        }
    }

    /* JADX INFO: renamed from: gb.g$g */
    /* JADX INFO: compiled from: LoadingViewHelper.java */
    class GenericErrorRunnable implements Runnable {

        final /* synthetic */ VolleyError error;

        GenericErrorRunnable(VolleyError volleyError) {
            this.error = volleyError;
        }

        @Override // java.lang.Runnable
        public void run() {
            AlertDialogHelper.newBuilder(LoadingViewHelper.this.context).setView(ErrorUtil.getErrorView(LoadingViewHelper.this.context, this.error)).setTitle("Error loading page").create().show();
        }
    }

    /* JADX INFO: renamed from: gb.g$h */
    /* JADX INFO: compiled from: LoadingViewHelper.java */
    class ActionClickListener implements View.OnClickListener {

        final /* synthetic */ RetryListener listener;

        ActionClickListener(RetryListener retryListener) {
            this.listener = retryListener;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            LoadingViewHelper.this.snackbar = null;
            RetryListener retryListener = this.listener;
            if (retryListener != null) {
                retryListener.onRetryAction();
            }
        }
    }

    /* JADX INFO: renamed from: gb.g$i */
    /* JADX INFO: compiled from: LoadingViewHelper.java */
    class SnackbarDismissCallback extends Snackbar.Callback {
        SnackbarDismissCallback() {
        }

        @Override // com.google.android.material.snackbar.Snackbar.Callback, com.google.android.material.snackbar.BaseTransientBottomBar.BaseCallback
        public void onDismissed(Snackbar snackbar, int event) {
            LoadingViewHelper.this.snackbar = null;
        }
    }

    /* JADX INFO: renamed from: gb.g$j */
    /* JADX INFO: compiled from: LoadingViewHelper.java */
    class ActionLongClickListener implements View.OnLongClickListener {

        final /* synthetic */ Runnable debugRunnable;

        ActionLongClickListener(Runnable runnable) {
            this.debugRunnable = runnable;
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            Runnable runnable = this.debugRunnable;
            if (runnable != null) {
                view.post(runnable);
                return true;
            }
            StaticToast.showWithContext(LoadingViewHelper.this.context, "No debug information available");
            return true;
        }
    }

    /* JADX INFO: renamed from: gb.g$k */
    /* JADX INFO: compiled from: LoadingViewHelper.java */
    public interface RetryListener {
        void onRetryAction();
    }

    public LoadingViewHelper(Context context, SwipeRefreshLayout swipeRefreshLayout, RetryListener retryListener) {
        this.context = context;
        this.swipeRefreshLayout = swipeRefreshLayout;
        this.retryListener = retryListener;
    }

    private void showSnackbar(String message, int actionRes, RetryListener retryListener, Runnable runnable) {
        if (this.context == null || TextUtils.isEmpty(message)) {
            return;
        }
        Snackbar snackbar = this.snackbar;
        if (snackbar == null || !snackbar.isShown()) {
            Snackbar snackbarB = SnackbarHelper.make(getAnchorView(), message, -2);
            this.snackbar = snackbarB;
            snackbarB.getView().setId(R.id.snackbar);
            this.snackbar.setAction(actionRes, new ActionClickListener(retryListener));
            this.snackbar.addCallback(new SnackbarDismissCallback());
            try {
                this.snackbar.getView().findViewById(R.id.snackbar_action).setOnLongClickListener(new ActionLongClickListener(runnable));
            } catch (Exception e) {
                Logger.logException(e);
            }
            try {
                this.snackbar.getView().findViewById(R.id.snackbar_text).setOnLongClickListener(new TextLongClickListener(runnable));
            } catch (Exception e) {
                Logger.logException(e);
            }
            this.snackbar.show();
        }
    }

    public void dismiss() {
        Snackbar snackbar = this.snackbar;
        if (snackbar == null || !snackbar.isShown()) {
            return;
        }
        this.snackbar.dismiss();
        this.snackbar = null;
    }

    protected View getAnchorView() {
        View view = this.anchorView;
        return view != null ? view : ((BaseActivity) this.context).findViewById(R.id.coordinator);
    }

    public boolean isShowingMessage(int messageRes) {
        if (!isShown()) {
            return false;
        }
        return this.context.getString(messageRes).equals(((TextView) this.snackbar.getView().findViewById(R.id.snackbar_text)).getText());
    }

    public boolean isShown() {
        Snackbar snackbar = this.snackbar;
        return snackbar != null && snackbar.isShown();
    }

    public void hide() {
        Snackbar snackbar = this.snackbar;
        if (snackbar == null || !snackbar.isShown()) {
            return;
        }
        this.snackbar.dismiss();
    }

    public void showError(int messageRes) {
        showError(messageRes, R.string.common_retry, this.retryListener);
    }

    public void showError(int messageRes, int actionRes, RetryListener retryListener) {
        SwipeRefreshLayout swipeRefreshLayout = this.swipeRefreshLayout;
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
        showSnackbar(this.context.getResources().getString(messageRes), actionRes, retryListener, null);
    }

    public void showError(VolleyError volleyError) {
        this.lastError = volleyError;
        SwipeRefreshLayout swipeRefreshLayout = this.swipeRefreshLayout;
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
        if (volleyError instanceof RateLimitedError) {
            showSnackbar("You are being rate limited by Reddit", R.string.common_help, new RateLimitRetryListener(), new RateLimitRunnable());
            return;
        }
        if (volleyError instanceof OAuthError) {
            showSnackbar(this.context.getResources().getString(R.string.common_oauth_error), R.string.common_retry, this.retryListener, new InvalidTokenRunnable(volleyError));
            return;
        }
        if (volleyError instanceof SubredditNotFoundError) {
            showSnackbar(volleyError.getMessage(), R.string.common_retry, this.retryListener, new NotFoundRunnable(volleyError));
            return;
        }
        if (volleyError == null || volleyError.networkResponse == null) {
            showSnackbar(this.context.getResources().getString(R.string.common_generic_error), R.string.common_retry, this.retryListener, new GenericErrorRunnable(volleyError));
            return;
        }
        showSnackbar("Error loading page: " + volleyError.networkResponse.statusCode, R.string.common_retry, this.retryListener, new HttpErrorRunnable(volleyError));
    }

    public void setRefreshing(boolean z) {
        setRefreshing(z, false);
    }

    public void setRefreshing(boolean refreshing, boolean dismissSnackbar) {
        Snackbar snackbar;
        SwipeRefreshLayout swipeRefreshLayout = this.swipeRefreshLayout;
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(refreshing);
        }
        if (dismissSnackbar && (snackbar = this.snackbar) != null && snackbar.isShown()) {
            this.snackbar.dismiss();
        }
    }

    public void setAnchorView(View view) {
        this.anchorView = view;
    }

    public void showMessage(String str) {
        if (this.context == null || TextUtils.isEmpty(str)) {
            return;
        }
        Snackbar snackbar = this.snackbar;
        if (snackbar == null || !snackbar.isShown()) {
            Snackbar snackbarB = SnackbarHelper.make(getAnchorView(), str, 0);
            this.snackbar = snackbarB;
            snackbarB.getView().setId(R.id.snackbar);
            this.snackbar.show();
        }
    }
}
