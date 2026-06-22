package de.jetwick.snacktory;

import java.io.Serializable;
import java.util.Map;

/* JADX INFO: loaded from: classes3.dex */
public class MapEntry<K, V> implements Map.Entry<K, V>, Serializable {
    private K key;
    private V value;

    public MapEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override // java.util.Map.Entry
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        MapEntry<K, V> mapEntry = (MapEntry<K, V>) obj;
        K thisKey = this.key;
        K otherKey = mapEntry.key;
        if (thisKey != otherKey && (thisKey == null || !thisKey.equals(otherKey))) {
            return false;
        }
        V thisValue = this.value;
        V otherValue = mapEntry.value;
        if (thisValue != otherValue) {
            return thisValue != null && thisValue.equals(otherValue);
        }
        return true;
    }

    @Override // java.util.Map.Entry
    public K getKey() {
        return this.key;
    }

    @Override // java.util.Map.Entry
    public V getValue() {
        return this.value;
    }

    @Override // java.util.Map.Entry
    public int hashCode() {
        K key = this.key;
        int iHashCode = (133 + (key != null ? key.hashCode() : 0)) * 19;
        V value = this.value;
        return iHashCode + (value != null ? value.hashCode() : 0);
    }

    @Override // java.util.Map.Entry
    public V setValue(V value) {
        this.value = value;
        return value;
    }

    public String toString() {
        return getKey() + ", " + getValue();
    }
}
