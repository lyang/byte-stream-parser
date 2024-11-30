package io.github.lyang.bytestreamparser.composite.parser;

import static io.github.lyang.bytestreamparser.scalar.util.Predicates.alwaysTrue;

import io.github.lyang.bytestreamparser.api.data.Data;
import io.github.lyang.bytestreamparser.api.parser.DataParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

public class ListParser<P extends Data<P>, V> extends DataParser<P, List<V>> {
  private final DataParser<?, V> itemParser;

  public ListParser(String id, DataParser<?, V> itemParser) {
    this(id, alwaysTrue(), itemParser);
  }

  public ListParser(String id, Predicate<P> applicable, DataParser<?, V> itemParser) {
    super(id, applicable);
    this.itemParser = itemParser;
  }

  @Override
  public void pack(List<V> values, OutputStream output) throws IOException {
    for (V value : values) {
      itemParser.pack(value, output);
    }
  }

  @Override
  public List<V> parse(InputStream input) throws IOException {
    LinkedList<V> values = new LinkedList<>();
    while (input.available() > 0) {
      values.add(itemParser.parse(input));
    }
    return values;
  }
}
