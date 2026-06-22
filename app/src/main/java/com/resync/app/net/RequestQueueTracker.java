package com.resync.app.net;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Tracks in-flight requests for a {@link RequestQueue} via the public
 * {@link RequestQueue.RequestEventListener} API.
 *
 * <p>The original Sync APK shipped a forked Volley whose {@code RequestQueue} exposed a
 * {@code isRequestInFlight(RequestFilter)} method that iterated the private
 * {@code mCurrentRequests} set. Stock Volley 1.2.1 (the Maven replacement) does not expose that
 * set, so this helper reconstructs the same capability by listening to request lifecycle events:
 * a request is considered in-flight from {@code REQUEST_QUEUED} until {@code REQUEST_FINISHED}.
 * {@link #isRequestInFlight(RequestQueue.RequestFilter)} then applies the filter to that set,
 * exactly mirroring the fork's semantics.
 */
public final class RequestQueueTracker {

    private final Set<Request<?>> inFlight = Collections.synchronizedSet(new HashSet<Request<?>>());

    private RequestQueueTracker() {
    }

    /** Attaches a tracker to {@code queue} and returns it. */
    public static RequestQueueTracker attach(RequestQueue queue) {
        final RequestQueueTracker tracker = new RequestQueueTracker();
        queue.addRequestEventListener(new RequestQueue.RequestEventListener() {
            @Override
            public void onRequestEvent(Request<?> request, int event) {
                if (event == RequestQueue.RequestEvent.REQUEST_QUEUED) {
                    tracker.inFlight.add(request);
                } else if (event == RequestQueue.RequestEvent.REQUEST_FINISHED) {
                    tracker.inFlight.remove(request);
                }
            }
        });
        return tracker;
    }

    /** Returns true if any in-flight request matches {@code filter}. */
    public boolean isRequestInFlight(RequestQueue.RequestFilter filter) {
        synchronized (inFlight) {
            for (Request<?> request : inFlight) {
                if (filter.apply(request)) {
                    return true;
                }
            }
        }
        return false;
    }
}
