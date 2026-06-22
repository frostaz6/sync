package com.squareup.otto;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: renamed from: bd.e */
/* JADX INFO: compiled from: EventProducer.java */
/* JADX INFO: loaded from: classes3.dex */
public class EventProducer {

    final Object target;
    private final Method method;
    private final int hashCode;
    private boolean valid = true;

    EventProducer(Object target, Method method) {
        Objects.requireNonNull(target, "EventProducer target cannot be null.");
        Objects.requireNonNull(method, "EventProducer method cannot be null.");
        this.target = target;
        this.method = method;
        method.setAccessible(true);
        this.hashCode = ((method.hashCode() + 31) * 31) + target.hashCode();
    }

    public void invalidate() {
        this.valid = false;
    }

    public boolean isValid() {
        return this.valid;
    }

    public Object produceEvent() throws InvocationTargetException {
        if (!this.valid) {
            throw new IllegalStateException(toString() + " has been invalidated and can no longer produce events.");
        }
        try {
            return this.method.invoke(this.target, new Object[0]);
        } catch (IllegalAccessException e) {
            throw new AssertionError(e);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof Error) {
                throw ((Error) e.getCause());
            }
            throw e;
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        EventProducer eventProducer = (EventProducer) obj;
        return this.method.equals(eventProducer.method) && this.target == eventProducer.target;
    }

    public int hashCode() {
        return this.hashCode;
    }

    public String toString() {
        return "[EventProducer " + this.method + "]";
    }
}
