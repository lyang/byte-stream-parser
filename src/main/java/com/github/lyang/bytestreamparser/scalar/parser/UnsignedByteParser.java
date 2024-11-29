package com.github.lyang.bytestreamparser.scalar.parser;

import static com.github.lyang.bytestreamparser.scalar.util.InputStreams.readFully;
import static com.github.lyang.bytestreamparser.scalar.util.Preconditions.check;
import static com.github.lyang.bytestreamparser.scalar.util.Predicates.alwaysTrue;

import com.github.lyang.bytestreamparser.api.data.Data;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.Predicate;

public class UnsignedByteParser<P extends Data<P>> extends NumberParser<P, Integer> {
  private static final String ERROR_MESSAGE = "%s: value must be between 0 and 255, but was %d";

  public UnsignedByteParser(String id) {
    this(id, alwaysTrue());
  }

  public UnsignedByteParser(String id, Predicate<P> applicable) {
    super(id, applicable);
  }

  @Override
  public void pack(Integer value, OutputStream output) throws IOException {
    check(value <= 0xFF, ERROR_MESSAGE, getId(), value);
    check(value >= 0x00, ERROR_MESSAGE, getId(), value);
    output.write(value.byteValue());
  }

  @Override
  public Integer parse(InputStream input) throws IOException {
    return readFully(input, 1)[0] & 0xFF;
  }
}
