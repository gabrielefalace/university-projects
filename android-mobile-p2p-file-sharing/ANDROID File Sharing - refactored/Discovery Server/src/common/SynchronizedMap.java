package common;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SynchronizedMap<K, V> implements Serializable {

	private HashMap<K, V> map;
	
	private transient ReadWriteLock rw_lock;
	private transient Lock readLock;
	private transient Lock writeLock;
	
	public SynchronizedMap(){
		map = new HashMap<K, V>();
		rw_lock = new ReentrantReadWriteLock();
		
		readLock = rw_lock.readLock();
		writeLock = rw_lock.writeLock();
	}
	
	public void put(K key, V value){
		writeLock.lock();
		map.put(key, value);
		writeLock.unlock();
	}
	
	public V get(K key){
		readLock.lock();
		V value = map.get(key);
		readLock.unlock();
		return value;
	}
	
	public int size(){
		readLock.lock();
		int size = map.size();
		readLock.unlock();
		return size;
	}
	
	public boolean containsKey(K key){
		readLock.lock();
		boolean res = map.containsKey(key);
		readLock.unlock();
		return res;
	}
	
	public boolean containsValue(V value){
		readLock.lock();
		boolean res = map.containsValue(value);
		readLock.unlock();
		return res;
	}
	
	public void remove(K key){
		writeLock.lock();
		map.remove(key);
		writeLock.unlock();
	}
	
	private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
		s.defaultReadObject();
		rw_lock = new ReentrantReadWriteLock();
		readLock = rw_lock.readLock();
		writeLock = rw_lock.writeLock();
	}
	
	public Collection<V> getValues(){
		readLock.lock();
		Collection<V> values = map.values();
		readLock.unlock();
		return values;
	}
	
	public Set<K> getKeys(){
		readLock.lock();
		Set<K> keys = map.keySet();
		readLock.unlock();
		return keys;
	}
}
