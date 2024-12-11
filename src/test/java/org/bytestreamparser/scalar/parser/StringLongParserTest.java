package org.bytestreamparser.scalar.parser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import org.bytestreamparser.api.data.testing.TestData;
import org.junit.jupiter.api.Test;

class StringLongParserTest {
  private static StringLongParser<TestData> createParser(int length) {
    TxtStringParser<TestData> stringParser =
        new TxtStringParser<>("str", length, Charset.defaultCharset());
    return new StringLongParser<>("str-long", stringParser, length);
  }

  @Test
  void parse() throws IOException {
    InputStream input = new ByteArrayInputStream("0123".getBytes());
    StringLongParser<TestData> parser = createParser(3);
    assertThat(parser.parse(input)).isEqualTo(12);
    assertThat(input.available()).isEqualTo(1);
  }

  @Test
  void parse_too_large_number() {
    String largeNumber = Long.MAX_VALUE + "0";
    InputStream input = new ByteArrayInputStream(largeNumber.getBytes());
    StringLongParser<TestData> parser = createParser(largeNumber.length());
    assertThatThrownBy(() -> parser.parse(input))
        .isInstanceOf(NumberFormatException.class)
        .hasMessage("For input string: \"92233720368547758070\"");
  }

  @Test
  void pack() throws IOException {
    StringLongParser<TestData> parser = createParser(3);
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    parser.pack(12L, output);
    assertThat(output.toByteArray()).isEqualTo("012".getBytes());
  }

  @Test
  void pack_too_large_number() {
    StringLongParser<TestData> parser = createParser(3);
    assertThatThrownBy(() -> parser.pack(1234L, new ByteArrayOutputStream()))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("str-long: value must be of length 3, but was 4");
  }
}
