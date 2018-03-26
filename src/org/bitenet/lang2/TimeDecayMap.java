package org.bitenet.lang2;

import java.util.HashMap;

public class TimeDecayMap<K,V > {
HashMap<K,V> mine;
HashMap<K,Long> timeStamps;
public static final int MAX_SIZE = 200;
	public TimeDecayMap() {
	mine  =  new HashMap<K,V>();
	timeStamps = new HashMap<K,Long>();
	}
public void put(K k, V v) {
	mine.put(k, v);
	timeStamps.put(k, System.currentTimeMillis());
	while(mine.size()>MAX_SIZE) {
		long oldest = Long.MAX_VALUE;
		K c = null;
		K temp;
		while ((temp=timeStamps.keySet().iterator().next())!=null) {
			if(oldest>timeStamps.get(temp)) {
				oldest = timeStamps.get(temp);
				c = temp;
			}
		}
		timeStamps.remove(c);
		mine.remove(c);
	}
}
public boolean containsKey(K k) {
	return mine.containsKey(k);
}
public V get(K k) {
	return mine.get(k);
}
}
