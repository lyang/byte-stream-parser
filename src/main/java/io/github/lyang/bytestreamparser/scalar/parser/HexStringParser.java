package io.github.lyang.bytestreamparser.scalar.parser;

import static io.github.lyang.bytestreamparser.scalar.util.InputStreams.readFully;
import static io.github.lyang.bytestreamparser.scalar.util.Predicates.alwaysTrue;

import io.github.lyang.bytestreamparser.api.data.Data;
import io.github.lyang.bytestreamparser.scalar.util.Strings;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HexFormat;
import java.util.function.Predicate;

public class HexStringParser<P extends Data<P>> extends StringParser<P> {
  private static final HexFormat HEX_FORMAT = HexFormat.of();
  private final int length;

  public HexStringParser(String id, int length) {
    this(id, alwaysTrue(), length);
  }

  public HexStringParser(String id, Predicate<P> applicable, int length) {
    super(id, applicable);
    this.length = length;
  }

  private static int toByteSize(int digits) {
    return (digits + 1) / 2;
  }

  @Override
  public void pack(String value, OutputStream output) throws IOException {
    String padded = Strings.padStart(value, toByteSize(length) * 2, '0');
    output.write(HEX_FORMAT.parseHex(padded));
  }

  @Override
  public String parse(InputStream input) throws IOException {
    String parsed = HEX_FORMAT.formatHex(readFully(input, toByteSize(length)));
    return parsed.substring(parsed.length() - length);
  }
}
