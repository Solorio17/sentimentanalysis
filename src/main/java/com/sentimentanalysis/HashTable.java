package com.sentimentanalysis;

import java.util.LinkedList;

public class HashTable<Key, Value> {

    // One entry stored in a bucket
    private static class Entry<K, V> {
        K key;
        V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private LinkedList<Entry<Key, Value>>[] buckets;  // array of linked-list buckets
    private int capacity;                             // current number of buckets
    private int size;                                 // number of key-value pairs
    private static final double LOAD_FACTOR_THRESHOLD = 0.5;

    @SuppressWarnings("unchecked")
    public HashTable(int initialCapacity) {
        this.capacity = initialCapacity;
        this.size = 0;
        buckets = (LinkedList<Entry<Key, Value>>[]) new LinkedList[capacity];

        // initialise every bucket with an empty linked list
        for (int i = 0; i < capacity; i++) {
            buckets[i] = new LinkedList<>();
        }
    }

    /** Insert / update a key-value pair. Triggers rehash when needed. */
    public void put(Key key, Value value) {
        int index = hash(key);

    // Check whether the key already exists
    for (Entry<Key, Value> entry : buckets[index]) {
        if (entry.key.equals(key)) {
            entry.value = value;
            return;
        }
    }

    // Add a new entry
    buckets[index].add(new Entry<>(key, value));
    size++;

    // Resize if the load factor exceeds 0.5
    if ((double) size / capacity > LOAD_FACTOR_THRESHOLD) {
        rehash();
    }
    }

    /** Convenience alias used by some versions of the assignment */
    public void insert(Key key, Value value) {
        put(key, value);
    }

    /** Return the value associated with key, or null if absent. */
    public Value get(Key key) {
        int index = hash(key);
        for (Entry<Key, Value> e : buckets[index]) {
            if (e.key.equals(key)) {
                return e.value;
            }
        }
        return null;
    }

    /** Alias for get() to match the original HashTable API */
    public Value lookup(Key key) {
        return get(key);
    }

    /** Double the number of buckets and rehash every existing entry. */
    @SuppressWarnings("unchecked")
    private void rehash() {
        LinkedList<Entry<Key, Value>>[] oldBuckets = buckets;

    // Double the table's capacity
    capacity *= 2;

    // Create a new array with the larger capacity
    buckets = (LinkedList<Entry<Key, Value>>[]) new LinkedList[capacity];

    // Initialize the new buckets
    for (int i = 0; i < capacity; i++) {
        buckets[i] = new LinkedList<>();
    }

    // Redistribute all existing entries
    for (LinkedList<Entry<Key, Value>> bucket : oldBuckets) {
        for (Entry<Key, Value> entry : bucket) {
            int index = hash(entry.key);
            buckets[index].add(entry);
        }
    }
    }

    private int hash(Key key) {
        return Math.abs(key.hashCode()) % capacity;
    }

    public int size() {
        return size;
    }

    public int capacity() {
        return capacity;
    }
}