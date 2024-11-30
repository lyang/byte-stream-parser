package io.github.lyang.bytestreamparser.composite.parser;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.lyang.bytestreamparser.api.data.testing.TestData;
import io.github.lyang.bytestreamparser.api.parser.DataParser;
import io.github.lyang.bytestreamparser.scalar.parser.NumberParser;
import io.github.lyang.bytestreamparser.scalar.parser.TxtStringParser;
import io.github.lyang.bytestreamparser.scalar.parser.UnsignedByteParser;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.function.Function;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VariableLengthParserTest {
  private static final String CONTENT = "123456789";
  private static final NumberParser<TestData, Integer> LENGTH_PARSER = new UnsignedByteParser<>("");
  private static final Function<Integer, DataParser<?, String>> CONTENT_PARSER_PROVIDER =
      length -> new TxtStringParser<>("", length, Charset.defaultCharset());
  private static final Function<String, Integer> LENGTH_PROVIDER = String::length;
  private VariableLengthParser<TestData, String> parser;

  private static InputStream prepareInput(int length) {
    byte[] bytes = new byte[CONTENT.length() + 1];
    ByteBuffer buffer = ByteBuffer.wrap(bytes);
    buffer.put((byte) length);
    buffer.put(CONTENT.getBytes());
    return new ByteArrayInputStream(buffer.array());
  }

  @BeforeEach
  void setUp() {
    parser =
        new VariableLengthParser<>("var", LENGTH_PARSER, CONTENT_PARSER_PROVIDER, LENGTH_PROVIDER);
  }

  @Test
  void parse_dynamic_length() throws IOException {
    for (int length = 0; length < CONTENT.length(); length++) {
      String parsed = parser.parse(prepareInput(length));
      assertThat(parsed).isEqualTo(CONTENT.substring(0, length));
    }
  }

  @Test
  void pack_dynamic_length() throws IOException {
    for (int length = 1; length < CONTENT.length(); length++) {
      String dynamic = CONTENT.substring(0, length);
      ByteArrayOutputStream output = new ByteArrayOutputStream();
      parser.pack(dynamic, output);
      byte[] packed = output.toByteArray();
      assertThat(Integer.valueOf(packed[0])).isEqualTo(length);
      assertThat(new String(packed, 1, length)).isEqualTo(dynamic);
    }
  }
}
