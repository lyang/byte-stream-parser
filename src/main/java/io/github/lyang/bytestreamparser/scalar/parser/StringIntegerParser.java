package io.github.lyang.bytestreamparser.scalar.parser;

import static io.github.lyang.bytestreamparser.scalar.util.Preconditions.check;
import static io.github.lyang.bytestreamparser.scalar.util.Predicates.alwaysTrue;

import io.github.lyang.bytestreamparser.api.data.Data;
import io.github.lyang.bytestreamparser.scalar.util.Strings;
import java.util.function.Predicate;

public class StringIntegerParser<P extends Data<P>> extends StringNumberParser<P, Integer> {
  private static final String INVALID_LENGTH = "%s: value must be of length %d, but was %d";
  private final int length;

  public StringIntegerParser(String id, StringParser<?> stringParser, int length) {
    this(id, alwaysTrue(), stringParser, length);
  }

  public StringIntegerParser(
      String id, Predicate<P> applicable, StringParser<?> stringParser, int length) {
    super(id, applicable, stringParser);
    this.length = length;
  }

  @Override
  protected String fromNumber(Integer value) {
    String padded = Strings.padStart(String.valueOf(value), length, '0');
    check(padded.length() == length, INVALID_LENGTH, getId(), length, padded.length());
    return padded;
  }

  @Override
  protected Integer toNumber(String value) {
    return Integer.valueOf(value);
  }
}
