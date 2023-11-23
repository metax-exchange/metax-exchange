package org.metax.exchange.core.util;

import java.util.Collection;
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

    public static <T, K> T findSingletonBy(Collection<T> collection, Function<T, K> function, K key) {
        Optional<T> result = collection.stream().filter(item -> key.equals(function.apply(item))).findFirst();
        return result.orElse(null);
    }

}
