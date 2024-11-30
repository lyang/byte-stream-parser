package io.github.lyang.bytestreamparser.scalar.parser;

import static io.github.lyang.bytestreamparser.scalar.util.Preconditions.check;
import static io.github.lyang.bytestreamparser.scalar.util.Predicates.alwaysTrue;

import io.github.lyang.bytestreamparser.api.data.Data;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class BcdStringParser<P extends Data<P>> extends HexStringParser<P> {
  private static final Pattern BCD_STRING = Pattern.compile("[0-9]*");
  private static final String ERROR_MESSAGE = "%s: Invalid BCD String %s";

  public BcdStringParser(String id, int length) {
    this(id, alwaysTrue(), length);
  }

  public BcdStringParser(String id, Predicate<P> applicable, int length) {
    super(id, applicable, length);
  }

  @Override
  public void pack(String value, OutputStream output) throws IOException {
    check(BCD_STRING.matcher(value).matches(), ERROR_MESSAGE, getId(), value);
    super.pack(value, output);
  }

  @Override
  public String parse(InputStream input) throws IOException {
    String parsed = super.parse(input);
    check(BCD_STRING.matcher(parsed).matches(), ERROR_MESSAGE, getId(), parsed);
    return parsed;
  }
}
