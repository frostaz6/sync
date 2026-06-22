package com.squareup.otto;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: renamed from: bd.d */
/* JADX INFO: compiled from: EventHandler.java */
/* JADX INFO: loaded from: classes3.dex */
public class EventHandler {

    private final Object target;
    private final Method method;
    private final int hashCode;
    private boolean valid = true;

    EventHandler(Object target, Method method) {
        Objects.requireNonNull(target, "EventHandler target cannot be null.");
        Objects.requireNonNull(method, "EventHandler method cannot be null.");
        this.target = target;
        this.method = method;
        method.setAccessible(true);
        this.hashCode = ((method.hashCode() + 31) * 31) + target.hashCode();
    }

    public void handleEvent(Object event) throws InvocationTargetException {
        if (!this.valid) {
            throw new IllegalStateException(toString() + " has been invalidated and can no longer handle events.");
        }
        try {
            this.method.invoke(this.target, event);
        } catch (IllegalAccessException e) {
            throw new AssertionError(e);
        } catch (InvocationTargetException e) {
            if (!(e.getCause() instanceof Error)) {
                throw e;
            }
            throw ((Error) e.getCause());
        }
    }

    public void invalidate() {
        this.valid = false;
    }

    public boolean isValid() {
        return this.valid;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        EventHandler eventHandler = (EventHandler) obj;
        return this.method.equals(eventHandler.method) && this.target == eventHandler.target;
    }

    public int hashCode() {
        return this.hashCode;
    }

    public String toString() {
        return "[EventHandler " + this.method + "]";
    }
}
