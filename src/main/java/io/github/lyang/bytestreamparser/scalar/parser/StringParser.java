package io.github.lyang.bytestreamparser.scalar.parser;

import io.github.lyang.bytestreamparser.api.data.Data;
import io.github.lyang.bytestreamparser.api.parser.DataParser;
import java.util.function.Predicate;

public abstract class StringParser<P extends Data<P>> extends DataParser<P, String> {
  protected StringParser(String id, Predicate<P> applicable) {
    super(id, applicable);
  }
}
