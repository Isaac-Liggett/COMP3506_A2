// @edu:student-assignment

package uq.comp3506.a2.structures;

import java.util.ArrayList;
import java.util.List;

/**
 * Supplied by the COMP3506/7505 teaching team, Semester 2, 2025.
 * <p>
 * NOTE: You should go and carefully read the documentation provided in the
 * MapInterface.java file - this explains some of the required functionality.
 */
public class UnorderedMap<K, V> implements MapInterface<K, V> {


    /**
     * you will need to put some member variables here to track your
     * data, size, capacity, etc...
     */
    private Entry<K, V>[] data;
    private int capacity;
    private int size;
    private float load_factor;

    private static final Entry TOMBSTONE = new Entry<>(null, null);
    /**
     * Constructs an empty UnorderedMap
     */
    public UnorderedMap() {
        this.data = new Entry[16];
        this.capacity = 16;
        this.size = 0;
        this.load_factor = 0;
    }

    /**
     * returns the size of the structure in terms of pairs
     * @return the number of kv pairs stored
     */
    @Override
    public int size() {
        return this.size;
    }

    /**
     * helper to indicate if the structure is empty or not
     * @return true if the map contains no key-value pairs, false otherwise.
     */
    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    /**
     * Clears all elements from the map. That means, after calling clear(),
     * the return of size() should be 0, and the data structure should appear
     * to be "empty".
     */
    @Override
    public void clear() {
        this.data = (Entry<K, V>[]) new Entry[16];
        this.size = 0;
        this.capacity = 16;
    }

    private void resize(){
        Entry<K, V>[] old_data = this.data.clone();

        this.capacity *= 2;
        this.size = 0;
        this.data = (Entry<K, V>[]) new Entry[this.capacity];

        for(int i = 0; i < old_data.length; i++){
            if (old_data[i] != null && old_data[i] != this.TOMBSTONE){
                this.put(old_data[i].getKey(), old_data[i].getValue());
            }
        }
    }

    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key, the old value
     * is replaced by the specified value.
     *
     * @param key   the key with which the specified value is to be associated
     * @param value the payload data value to be associated with the specified key
     * @return the previous value associated with key, or null if there was no such key
     */
    @Override
    public V put(K key, V value) {
        int location = Math.abs(key.hashCode()) % this.capacity;

        try{
            while(this.data[location] != null && this.data[location] != this.TOMBSTONE){
                if (this.data[location].getKey() == key) {
                    this.size -= 1;
                    break;
                }
                location += 1;
            }

            this.data[location] = new Entry<K, V>(key, value);
        }catch(ArrayIndexOutOfBoundsException e){
            this.resize();
            this.put(key, value);
            this.size -= 1;
        }
        this.size += 1;
        return value;
    }

    /**
     * Looks up the specified key in this map, returning its associated value
     * if such key exists.
     *
     * @param key the key with which the specified value is to be associated
     * @return the value associated with key, or null if there was no such key
     */
    @Override
    public V get(K key) {
        int location = key.hashCode() % this.capacity;

        while(this.data[location] != null && this.data[location].getKey() != key){
            if(this.data[location].getKey() == null){
                return null;
            }
            location += 1;
        }

        if(this.data[location] == null){
            return null;
        }
        return this.data[location].getValue();
    }

    /**
     * Looks up the specified key in this map, and removes the key-value pair
     * if the key exists.
     *
     * @param key the key with which the specified value is to be associated
     * @return the value associated with key, or null if there was no such key
     */
    @Override
    public V remove(K key) {
        int location = key.hashCode() % this.capacity;

        while(this.data[location] != null && this.data[location].getKey() != key){
            location += 1;
        }

        if(this.data[location] == null){
            return null;
        }

        V value = this.data[location].getValue();

        this.data[location] = this.TOMBSTONE;
        this.size -= 1;
        return value;
    }

    /**
     * Finds and returns all keys currently stored in the map.
     *
     * @return an ArrayList of keys used currently in the map
     */
    public List<K> keys(){
        List<K> keys = new ArrayList<>();
        for (Entry<K, V> data : this.data) {
            if (data != null && data != this.TOMBSTONE) {
                keys.add(data.getKey());
            }
        }
        return keys;
    }

    /**
     * Finds and returns all keys and values currently stored in the map as Entry Objects.
     *
     * @return an ArrayList of key and value Entries used currently in the map
     */
    public List<Entry<K, V>> entries(){
        List<Entry<K, V>> entries = new ArrayList<>();
        for (Entry<K, V> data : this.data) {
            if (data != null && data != this.TOMBSTONE) {
                entries.add(data);
            }
        }
        return entries;
    }

    public boolean hasAll(List<K> keys){
        for(K key : keys){
            if(this.get(key) == null){
                return false;
            }
        }
        return true;
    }
}
