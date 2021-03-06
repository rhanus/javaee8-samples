package org.javaee8.jcache.configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.FactoryBuilder;
import javax.cache.configuration.MutableCacheEntryListenerConfiguration;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;


/**
 * @author Radim Hanus
 */
public class KeyValueService<K,V> {
    private CacheManager cacheManager;
    private Cache<K, V> primaryCache;
    private Cache<K, V> secondaryCache;

    @PostConstruct
    void init() {
        CachingProvider cachingProvider = Caching.getCachingProvider();
        cacheManager = cachingProvider.getCacheManager();

        MutableConfiguration<K, V> config = new MutableConfiguration<>();
        secondaryCache = cacheManager.createCache("cache.secondary", config);

        // removed entries are inserted into secondary cache by the listener
        config.addCacheEntryListenerConfiguration(
                new MutableCacheEntryListenerConfiguration<>(
                        FactoryBuilder.factoryOf(new KeyValueEntryRemovedListener<>(secondaryCache)),
                        null,
                        true,
                        true)
        );

        primaryCache = cacheManager.createCache("cache.primary", config);
    }

    @PreDestroy
    void destroy() {
        cacheManager.close();
    }

    public void put(K key, V value) {
        primaryCache.put(key, value);
    }

    public V get(K key) {
        return primaryCache.get(key);
    }

    public V getSecondary(K key) {
        return secondaryCache.get(key);
    }

    public void remove(K key) {
        primaryCache.remove(key);
    }
}
