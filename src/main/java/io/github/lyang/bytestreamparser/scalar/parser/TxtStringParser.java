package io.github.lyang.bytestreamparser.scalar.parser;

import static io.github.lyang.bytestreamparser.scalar.util.InputStreams.readFully;
import static io.github.lyang.bytestreamparser.scalar.util.Preconditions.check;
import static io.github.lyang.bytestreamparser.scalar.util.Predicates.alwaysTrue;

import io.github.lyang.bytestreamparser.api.data.Data;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.function.Predicate;

public class TxtStringParser<P extends Data<P>> extends StringParser<P> {
  private static final String INVALID_LENGTH = "%s: value must be of length %d, but was %d";
  private static final String MARK_SUPPORT_CHECK =
      "%s: %s#markSupported() required to parse %s charset";
  private final int length;
  private final Charset charset;

  public TxtStringParser(String id, int length, Charset charset) {
    this(id, alwaysTrue(), length, charset);
  }

  public TxtStringParser(String id, Predicate<P> applicable, int length, Charset charset) {
    super(id, applicable);
    this.length = length;
    this.charset = charset;
  }

  @Override
  public void pack(String value, OutputStream output) throws IOException {
    check(value.length() == length, INVALID_LENGTH, getId(), length, value.length());
    output.write(value.getBytes(charset));
  }

  @Override
  public String parse(InputStream input) throws IOException {
    if (charset.newEncoder().maxBytesPerChar() == 1) {
      return parseSingleByteCharset(input);
    } else {
      return parseMultiByteCharset(input);
    }
  }

  private String parseSingleByteCharset(InputStream input) throws IOException {
    return new String(readFully(input, length), charset);
  }

  private String parseMultiByteCharset(InputStream input) throws IOException {
    check(
        input.markSupported(),
        MARK_SUPPORT_CHECK,
        getId(),
        input.getClass().getSimpleName(),
        charset.name());
    input.mark(Integer.MAX_VALUE);
    String parsed = readFully(input, length, charset);
    input.reset();
    input.skipNBytes(parsed.getBytes(charset).length);
    return parsed;
  }
}
