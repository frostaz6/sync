package com.squareup.otto;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: renamed from: bd.a */
/* JADX INFO: compiled from: AnnotatedHandlerFinder.java */
/* JADX INFO: loaded from: classes3.dex */
public final class AnnotatedHandlerFinder {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final ConcurrentMap<Class<?>, Map<Class<?>, Method>> PRODUCERS_CACHE = new ConcurrentHashMap();
    private static final ConcurrentMap<Class<?>, Map<Class<?>, Set<Method>>> SUBSCRIBERS_CACHE = new ConcurrentHashMap();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Map<Class<?>, EventProducer> findAllProducers(Object listener) {
        Class<?> cls = listener.getClass();
        HashMap map = new HashMap();
        Map<Class<?>, Method> map2 = PRODUCERS_CACHE.get(cls);
        if (map2 == null) {
            map2 = new HashMap<>();
            loadAnnotatedProducerMethods(cls, map2);
        }
        if (!map2.isEmpty()) {
            for (Map.Entry<Class<?>, Method> entry : map2.entrySet()) {
                map.put(entry.getKey(), new EventProducer(listener, entry.getValue()));
            }
        }
        return map;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Map<Class<?>, Set<EventHandler>> findAllSubscribers(Object listener) {
        Class<?> cls = listener.getClass();
        HashMap map = new HashMap();
        Map<Class<?>, Set<Method>> map2 = SUBSCRIBERS_CACHE.get(cls);
        if (map2 == null) {
            map2 = new HashMap<>();
            loadAnnotatedSubscriberMethods(cls, map2);
        }
        if (!map2.isEmpty()) {
            for (Map.Entry<Class<?>, Set<Method>> entry : map2.entrySet()) {
                HashSet hashSet = new HashSet();
                Iterator<Method> it = entry.getValue().iterator();
                while (it.hasNext()) {
                    hashSet.add(new EventHandler(listener, it.next()));
                }
                map.put(entry.getKey(), hashSet);
            }
        }
        return map;
    }

    private static void loadAnnotatedMethods(Class<?> cls, Map<Class<?>, Method> producerMethods, Map<Class<?>, Set<Method>> subscriberMethods) {
        for (Method method : cls.getDeclaredMethods()) {
            if (!method.isBridge()) {
                if (method.isAnnotationPresent(Subscribe.class)) {
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    if (parameterTypes.length != 1) {
                        throw new IllegalArgumentException("Method " + method + " has @Subscribe annotation but requires " + parameterTypes.length + " arguments.  Methods must require a single argument.");
                    }
                    Class<?> cls2 = parameterTypes[0];
                    if (cls2.isInterface()) {
                        throw new IllegalArgumentException("Method " + method + " has @Subscribe annotation on " + cls2 + " which is an interface.  Subscription must be on a concrete class type.");
                    }
                    if ((1 & method.getModifiers()) == 0) {
                        throw new IllegalArgumentException("Method " + method + " has @Subscribe annotation on " + cls2 + " but is not 'public'.");
                    }
                    Set<Method> hashSet = subscriberMethods.get(cls2);
                    if (hashSet == null) {
                        hashSet = new HashSet<>();
                        subscriberMethods.put(cls2, hashSet);
                    }
                    hashSet.add(method);
                } else if (method.isAnnotationPresent(Produce.class)) {
                    Class<?>[] parameterTypes2 = method.getParameterTypes();
                    if (parameterTypes2.length != 0) {
                        throw new IllegalArgumentException("Method " + method + "has @Produce annotation but requires " + parameterTypes2.length + " arguments.  Methods must require zero arguments.");
                    }
                    if (method.getReturnType() == Void.class) {
                        throw new IllegalArgumentException("Method " + method + " has a return type of void.  Must declare a non-void type.");
                    }
                    Class<?> returnType = method.getReturnType();
                    if (returnType.isInterface()) {
                        throw new IllegalArgumentException("Method " + method + " has @Produce annotation on " + returnType + " which is an interface.  Producers must return a concrete class type.");
                    }
                    if (returnType.equals(Void.TYPE)) {
                        throw new IllegalArgumentException("Method " + method + " has @Produce annotation but has no return type.");
                    }
                    if ((1 & method.getModifiers()) == 0) {
                        throw new IllegalArgumentException("Method " + method + " has @Produce annotation on " + returnType + " but is not 'public'.");
                    }
                    if (producerMethods.containsKey(returnType)) {
                        throw new IllegalArgumentException("Producer for type " + returnType + " has already been registered.");
                    }
                    producerMethods.put(returnType, method);
                } else {
                    continue;
                }
            }
        }
        PRODUCERS_CACHE.put(cls, producerMethods);
        SUBSCRIBERS_CACHE.put(cls, subscriberMethods);
    }

    private static void loadAnnotatedProducerMethods(Class<?> cls, Map<Class<?>, Method> producerMethods) {
        loadAnnotatedMethods(cls, producerMethods, new HashMap());
    }

    private static void loadAnnotatedSubscriberMethods(Class<?> cls, Map<Class<?>, Set<Method>> subscriberMethods) {
        loadAnnotatedMethods(cls, new HashMap(), subscriberMethods);
    }
}
