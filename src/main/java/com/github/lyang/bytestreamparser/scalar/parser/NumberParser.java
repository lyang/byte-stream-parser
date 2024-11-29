package com.github.lyang.bytestreamparser.scalar.parser;

import com.github.lyang.bytestreamparser.api.data.Data;
import com.github.lyang.bytestreamparser.api.parser.DataParser;
import java.util.function.Predicate;

public abstract class NumberParser<P extends Data<P>, V extends Number> extends DataParser<P, V> {
  protected NumberParser(String id, Predicate<P> applicable) {
    super(id, applicable);
  }
}
