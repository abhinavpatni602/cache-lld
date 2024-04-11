package com.abhinav.cache.factories;

import com.abhinav.cache.Cache;
import com.abhinav.cache.CacheType;
import com.abhinav.cache.policies.LFUEvictionPolicy;
import com.abhinav.cache.policies.LRUEvictionPolicy;
import com.abhinav.cache.storage.HashMapBasedStorage;

public class CacheFactory<Key, Value> {

    public Cache<Key, Value> getCache(CacheType cacheType, int capacity) {
        switch (cacheType){
            case LFU:
                return new Cache<>(new LFUEvictionPolicy<Key>(),
                    new HashMapBasedStorage<Key, Value>(capacity));
            case LRU:
                return new Cache<>(new LRUEvictionPolicy<Key>(),
                    new HashMapBasedStorage<Key, Value>(capacity));
            default:
                throw new RuntimeException("Cache type is not supported");
        }
    }
}
