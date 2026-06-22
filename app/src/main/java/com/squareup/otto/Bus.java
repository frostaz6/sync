package com.squareup.otto;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;

/* JADX INFO: renamed from: bd.b */
/* JADX INFO: compiled from: Bus.java */
/* JADX INFO: loaded from: classes3.dex */
public class Bus {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final ConcurrentMap<Class<?>, Set<EventHandler>> handlersByType;
    private final ConcurrentMap<Class<?>, EventProducer> producersByType;
    private final String identifier;
    private final ThreadEnforcer enforcer;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final HandlerFinder handlerFinder;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final ThreadLocal<ConcurrentLinkedQueue<EventWithHandler>> eventsToDispatch;
    private final ThreadLocal<Boolean> isDispatching;
    private final ConcurrentMap<Class<?>, Set<Class<?>>> flattenHierarchyCache;

    /* JADX INFO: renamed from: bd.b$a */
    /* JADX INFO: compiled from: Bus.java */
    class EventsToDispatchThreadLocal extends ThreadLocal<ConcurrentLinkedQueue<EventWithHandler>> {
        EventsToDispatchThreadLocal() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // java.lang.ThreadLocal
        public ConcurrentLinkedQueue<EventWithHandler> initialValue() {
            return new ConcurrentLinkedQueue<>();
        }
    }

    /* JADX INFO: renamed from: bd.b$b */
    /* JADX INFO: compiled from: Bus.java */
    class IsDispatchingThreadLocal extends ThreadLocal<Boolean> {
        IsDispatchingThreadLocal() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // java.lang.ThreadLocal
        public Boolean initialValue() {
            return Boolean.FALSE;
        }
    }

    /* JADX INFO: renamed from: bd.b$c */
    /* JADX INFO: compiled from: Bus.java */
    static class EventWithHandler {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final Object event;
        final EventHandler handler;

        public EventWithHandler(Object event, EventHandler handler) {
            this.event = event;
            this.handler = handler;
        }
    }

    public Bus(ThreadEnforcer threadEnforcer) {
        this(threadEnforcer, "default");
    }

    private void dispatchProducerResultToHandler(EventHandler eventHandler, EventProducer eventProducer) {
        Object event;
        try {
            event = eventProducer.produceEvent();
        } catch (InvocationTargetException e) {
            throwRuntimeException("Producer " + eventProducer + " threw an exception.", e);
            event = null;
        }
        if (event == null) {
            return;
        }
        dispatch(event, eventHandler);
    }

    private Set<Class<?>> getClassesFor(Class<?> cls) {
        LinkedList linkedList = new LinkedList();
        HashSet hashSet = new HashSet();
        linkedList.add(cls);
        while (!linkedList.isEmpty()) {
            Class cls2 = (Class) linkedList.remove(0);
            hashSet.add(cls2);
            Class superclass = cls2.getSuperclass();
            if (superclass != null) {
                linkedList.add(superclass);
            }
        }
        return hashSet;
    }

    private static void throwRuntimeException(String str, InvocationTargetException invocationTargetException) {
        Throwable cause = invocationTargetException.getCause();
        if (cause != null) {
            throw new RuntimeException(str + ": " + cause.getMessage(), cause);
        }
        throw new RuntimeException(str + ": " + invocationTargetException.getMessage(), invocationTargetException);
    }

    protected void dispatch(Object obj, EventHandler eventHandler) {
        try {
            eventHandler.handleEvent(obj);
        } catch (InvocationTargetException e) {
            throwRuntimeException("Could not dispatch event: " + obj.getClass() + " to handler " + eventHandler, e);
        }
    }

    protected void dispatchQueuedEvents() {
        if (this.isDispatching.get().booleanValue()) {
            return;
        }
        this.isDispatching.set(Boolean.TRUE);
        while (true) {
            try {
                EventWithHandler eventWithHandler = this.eventsToDispatch.get().poll();
                if (eventWithHandler == null) {
                    return;
                }
                if (eventWithHandler.handler.isValid()) {
                    dispatch(eventWithHandler.event, eventWithHandler.handler);
                }
            } finally {
                this.isDispatching.set(Boolean.FALSE);
            }
        }
    }

    protected void enqueueEvent(Object obj, EventHandler eventHandler) {
        this.eventsToDispatch.get().offer(new EventWithHandler(obj, eventHandler));
    }

    Set<Class<?>> flattenHierarchy(Class<?> cls) {
        Set<Class<?>> set = this.flattenHierarchyCache.get(cls);
        if (set != null) {
            return set;
        }
        Set<Class<?>> classes = getClassesFor(cls);
        Set<Class<?>> classesPutIfAbsent = this.flattenHierarchyCache.putIfAbsent(cls, classes);
        return classesPutIfAbsent == null ? classes : classesPutIfAbsent;
    }

    Set<EventHandler> getHandlersForEventType(Class<?> cls) {
        return this.handlersByType.get(cls);
    }

    EventProducer getProducerForEventType(Class<?> cls) {
        return this.producersByType.get(cls);
    }

    public void post(Object obj) {
        Objects.requireNonNull(obj, "Event to post must not be null.");
        this.enforcer.enforce(this);
        boolean dispatched = false;
        Iterator<Class<?>> it = flattenHierarchy(obj.getClass()).iterator();
        while (it.hasNext()) {
            Set<EventHandler> handlers = getHandlersForEventType(it.next());
            if (handlers != null && !handlers.isEmpty()) {
                dispatched = true;
                Iterator<EventHandler> it2 = handlers.iterator();
                while (it2.hasNext()) {
                    enqueueEvent(obj, it2.next());
                }
            }
        }
        if (!dispatched && !(obj instanceof DeadEvent)) {
            post(new DeadEvent(this, obj));
        }
        dispatchQueuedEvents();
    }

    public void register(Object obj) {
        Set<EventHandler> handlersPutIfAbsent;
        Objects.requireNonNull(obj, "Object to register must not be null.");
        this.enforcer.enforce(this);
        Map<Class<?>, EventProducer> foundProducers = this.handlerFinder.findAllProducers(obj);
        for (Class<?> cls : foundProducers.keySet()) {
            EventProducer eventProducer = foundProducers.get(cls);
            EventProducer eventProducerPutIfAbsent = this.producersByType.putIfAbsent(cls, eventProducer);
            if (eventProducerPutIfAbsent != null) {
                throw new IllegalArgumentException("Producer method for type " + cls + " found on type " + eventProducer.target.getClass() + ", but already registered by type " + eventProducerPutIfAbsent.target.getClass() + ".");
            }
            Set<EventHandler> set = this.handlersByType.get(cls);
            if (set != null && !set.isEmpty()) {
                Iterator<EventHandler> it = set.iterator();
                while (it.hasNext()) {
                    dispatchProducerResultToHandler(it.next(), eventProducer);
                }
            }
        }
        Map<Class<?>, Set<EventHandler>> foundHandlersMap = this.handlerFinder.findAllSubscribers(obj);
        for (Class<?> cls2 : foundHandlersMap.keySet()) {
            Set<EventHandler> handlers = this.handlersByType.get(cls2);
            if (handlers == null && (handlersPutIfAbsent = this.handlersByType.putIfAbsent(cls2, (handlers = new CopyOnWriteArraySet<>()))) != null) {
                handlers = handlersPutIfAbsent;
            }
            if (!handlers.addAll(foundHandlersMap.get(cls2))) {
                throw new IllegalArgumentException("Object already registered.");
            }
        }
        for (Map.Entry<Class<?>, Set<EventHandler>> entry : foundHandlersMap.entrySet()) {
            EventProducer eventProducer2 = this.producersByType.get(entry.getKey());
            if (eventProducer2 != null && eventProducer2.isValid()) {
                for (EventHandler eventHandler : entry.getValue()) {
                    if (!eventProducer2.isValid()) {
                        break;
                    } else if (eventHandler.isValid()) {
                        dispatchProducerResultToHandler(eventHandler, eventProducer2);
                    }
                }
            }
        }
    }

    public void unregister(Object obj) {
        Objects.requireNonNull(obj, "Object to unregister must not be null.");
        this.enforcer.enforce(this);
        for (Map.Entry<Class<?>, EventProducer> entry : this.handlerFinder.findAllProducers(obj).entrySet()) {
            Class<?> key = entry.getKey();
            EventProducer producer = getProducerForEventType(key);
            EventProducer value = entry.getValue();
            if (value == null || !value.equals(producer)) {
                throw new IllegalArgumentException("Missing event producer for an annotated method. Is " + obj.getClass() + " registered?");
            }
            this.producersByType.remove(key).invalidate();
        }
        for (Map.Entry<Class<?>, Set<EventHandler>> entry2 : this.handlerFinder.findAllSubscribers(obj).entrySet()) {
            Set<EventHandler> currentHandlers = getHandlersForEventType(entry2.getKey());
            Set<EventHandler> eventMethodsInListener = entry2.getValue();
            if (currentHandlers == null || !currentHandlers.containsAll(eventMethodsInListener)) {
                throw new IllegalArgumentException("Missing event handler for an annotated method. Is " + obj.getClass() + " registered?");
            }
            for (EventHandler eventHandler : currentHandlers) {
                if (eventMethodsInListener.contains(eventHandler)) {
                    eventHandler.invalidate();
                }
            }
            currentHandlers.removeAll(eventMethodsInListener);
        }
    }

    public String toString() {
        return "[Bus \"" + this.identifier + "\"]";
    }

    public Bus(ThreadEnforcer threadEnforcer, String str) {
        this(threadEnforcer, str, HandlerFinder.ANNOTATED);
    }

    Bus(ThreadEnforcer threadEnforcer, String str, HandlerFinder handlerFinder) {
        this.handlersByType = new ConcurrentHashMap();
        this.producersByType = new ConcurrentHashMap();
        this.eventsToDispatch = new EventsToDispatchThreadLocal();
        this.isDispatching = new IsDispatchingThreadLocal();
        this.flattenHierarchyCache = new ConcurrentHashMap();
        this.enforcer = threadEnforcer;
        this.identifier = str;
        this.handlerFinder = handlerFinder;
    }
}
