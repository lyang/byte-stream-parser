package org.bytestreamparser.api.data;

import java.util.Set;
import java.util.function.UnaryOperator;

public interface Data<T extends Data<T>> {

  Set<String> fields();

  <V> V get(String id);

  <V> T set(String id, V value);

  T clear(String id);

  default <V> T mutate(String id, UnaryOperator<V> mutator) {
    return set(id, mutator.apply(get(id)));
  }
}
