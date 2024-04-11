package com.abhinav.cache.policies;


import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;

/**
 * Eviction policy based on LRU algorithm.
 *
 * @param <Key> Key type.
 */
public class LFUEvictionPolicy<Key> implements EvictionPolicy<Key> {
    Map<Key,Integer> keyFreqMap;
    Map<Integer, LinkedHashSet<Key>> freqKeyMap;
    int minFrequency;
    int maxFrequency;

    public LFUEvictionPolicy() {
        keyFreqMap = new HashMap<>();
        freqKeyMap = new HashMap<>();
        minFrequency = Integer.MAX_VALUE;
        maxFrequency = Integer.MIN_VALUE;
    }

    @Override
    public void keyAccessed(Key key) {
        int currFreq = 0;
        if(keyFreqMap.containsKey(key)){
            currFreq = keyFreqMap.get(key);
            keyFreqMap.put(key, currFreq + 1);
            freqKeyMap.get(currFreq).remove(key);
            freqKeyMap.computeIfAbsent(currFreq + 1, a -> new LinkedHashSet<>()).add(key);
        } else {
            keyFreqMap.put(key, currFreq + 1);
        }

        minFrequency = Math.min(minFrequency, currFreq+1);
        maxFrequency = Math.max(maxFrequency, currFreq + 1);
    }

    @Override
    public Key evictKey() {
        if(!freqKeyMap.containsKey(minFrequency)){
            return null;
        }

        LinkedHashSet<Key> frequencyKeyOrder = freqKeyMap.get(minFrequency);
        if(!frequencyKeyOrder.isEmpty()){
            Iterator<Key> iterator = frequencyKeyOrder.iterator();
            Key leastFrequentKey = iterator.next();
            frequencyKeyOrder.remove(leastFrequentKey);
            if (frequencyKeyOrder.isEmpty()) {
                freqKeyMap.remove(minFrequency);
                if (minFrequency == keyFreqMap.get(leastFrequentKey)) {
                    for(int i=minFrequency+1; i<=maxFrequency; i++){
                        if(freqKeyMap.containsKey(i)){
                            minFrequency = i;
                            break;
                        }
                    }
                }
            }
            keyFreqMap.remove(leastFrequentKey);
            return leastFrequentKey;
        }

        return null;
    }
}
