package org.metax.exchange.core.util;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public final class StreamUtils {

    public static <T> Collector<T, ?, T> singletonCollector() {
        return Collectors.collectingAndThen(
                Collectors.toList(),
                list -> {
                    if (list.size() > 1) {
                        throw new IllegalStateException("List contains more than one element: " + list);
                    }
                    return list.size() > 0 ? list.get(0) : null;
                });
    }

    /**
     * 在集合中根据给定键值通过函数筛选单个元素。
     *
     * @param collection 要搜索的集合
     * @param function   用于将元素映射为键的函数
     * @param key        要匹配的键值
     * @param <T>        集合元素的类型
     * @param <K>        键的类型
     * @return 匹配的元素，如果没有匹配则为 null
     */
    public static <T, K> T findSingleByKey(Collection<T> collection, Function<T, K> function, K key) {
        Optional<T> result = collection.stream().filter(item -> key.equals(function.apply(item))).findFirst();
        return result.orElse(null);
    }

    /**
     * 在集合中查找由给定键列表指定的元素。
     *
     * @param collection 要搜索的集合
     * @param function   用于将元素映射为键的函数
     * @param keys       要查找的键列表
     * @param <T>        集合元素的类型
     * @param <K>        键的类型
     * @return 匹配的元素列表
     */
    public static <T, K> List<T> filterByKeys(Collection<T> collection, Function<T, K> function, List<K> keys) {
        return collection.stream().filter(item -> keys.contains(function.apply(item))).toList();
    }

}
