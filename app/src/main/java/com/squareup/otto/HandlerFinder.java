package com.squareup.otto;

import java.util.Map;
import java.util.Set;

/* JADX INFO: renamed from: bd.f */
/* JADX INFO: compiled from: HandlerFinder.java */
/* JADX INFO: loaded from: classes3.dex */
interface HandlerFinder {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final HandlerFinder ANNOTATED = new AnnotatedHandlerFinderImpl();

    /* JADX INFO: renamed from: bd.f$a */
    /* JADX INFO: compiled from: HandlerFinder.java */
    static class AnnotatedHandlerFinderImpl implements HandlerFinder {
        AnnotatedHandlerFinderImpl() {
        }

        @Override // com.squareup.otto.HandlerFinder
        public Map<Class<?>, Set<EventHandler>> findAllSubscribers(Object obj) {
            return AnnotatedHandlerFinder.findAllSubscribers(obj);
        }

        @Override // com.squareup.otto.HandlerFinder
        public Map<Class<?>, EventProducer> findAllProducers(Object obj) {
            return AnnotatedHandlerFinder.findAllProducers(obj);
        }
    }

    Map<Class<?>, Set<EventHandler>> findAllSubscribers(Object obj);

    Map<Class<?>, EventProducer> findAllProducers(Object obj);
}
