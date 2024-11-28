package com.github.lyang.bytestreamparser.scalar.parser;

import static com.github.lyang.bytestreamparser.scalar.util.InputStreams.readFully;
import static com.github.lyang.bytestreamparser.scalar.util.Preconditions.check;
import static com.github.lyang.bytestreamparser.scalar.util.Predicates.alwaysTrue;

import com.github.lyang.bytestreamparser.api.data.Data;
import com.github.lyang.bytestreamparser.api.parser.DataParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.Predicate;

public class BinaryParser<P extends Data<P>> extends DataParser<P, byte[]> {
  private static final String ERROR_MESSAGE = "%s: value must be of length %d, but was %d";
  private final int length;

  public BinaryParser(String id, int length) {
    this(id, alwaysTrue(), length);
  }

  public BinaryParser(String id, Predicate<P> applicable, int length) {
    super(id, applicable);
    this.length = length;
  }

  @Override
  public void pack(byte[] value, OutputStream output) throws IOException {
    check(length == value.length, ERROR_MESSAGE, id, length, value.length);
    output.write(value);
  }

  @Override
  public byte[] parse(InputStream input) throws IOException {
    return readFully(input, length);
  }
}
