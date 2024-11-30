package io.github.lyang.bytestreamparser.composite.parser;

import static io.github.lyang.bytestreamparser.scalar.util.Predicates.alwaysTrue;

import io.github.lyang.bytestreamparser.api.data.Data;
import io.github.lyang.bytestreamparser.api.parser.DataParser;
import io.github.lyang.bytestreamparser.scalar.parser.NumberParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.Function;
import java.util.function.Predicate;

public class VariableLengthParser<P extends Data<P>, V> extends DataParser<P, V> {
  private final NumberParser<?, Integer> lengthParser;
  private final Function<Integer, DataParser<?, V>> valueParserProvider;
  private final Function<V, Integer> lengthProvider;

  public VariableLengthParser(
      String id,
      NumberParser<?, Integer> lengthParser,
      Function<Integer, DataParser<?, V>> valueParserProvider,
      Function<V, Integer> lengthProvider) {
    this(id, alwaysTrue(), lengthParser, valueParserProvider, lengthProvider);
  }

  public VariableLengthParser(
      String id,
      Predicate<P> applicable,
      NumberParser<?, Integer> lengthParser,
      Function<Integer, DataParser<?, V>> valueParserProvider,
      Function<V, Integer> lengthProvider) {
    super(id, applicable);
    this.lengthParser = lengthParser;
    this.valueParserProvider = valueParserProvider;
    this.lengthProvider = lengthProvider;
  }

  @Override
  public void pack(V value, OutputStream output) throws IOException {
    Integer length = lengthProvider.apply(value);
    lengthParser.pack(length, output);
    valueParserProvider.apply(length).pack(value, output);
  }

  @Override
  public V parse(InputStream input) throws IOException {
    Integer length = lengthParser.parse(input);
    return valueParserProvider.apply(length).parse(input);
  }
}
