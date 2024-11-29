package com.github.lyang.bytestreamparser.scalar.parser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.lyang.bytestreamparser.api.data.testing.TestData;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import org.junit.jupiter.api.Test;

class StringIntegerParserTest {
  private static StringIntegerParser<TestData> createParser(int length) {
    TxtStringParser<TestData> stringParser =
        new TxtStringParser<>("str", length, Charset.defaultCharset());
    return new StringIntegerParser<>("str-int", stringParser, length);
  }

  @Test
  void parse() throws IOException {
    InputStream input = new ByteArrayInputStream("0123".getBytes());
    StringIntegerParser<TestData> parser = createParser(3);
    assertThat(parser.parse(input)).isEqualTo(12);
    assertThat(input.available()).isEqualTo(1);
  }

  @Test
  void parse_too_large_number() {
    String largeNumber = Integer.MAX_VALUE + "0";
    InputStream input = new ByteArrayInputStream(largeNumber.getBytes());
    StringIntegerParser<TestData> parser = createParser(largeNumber.length());
    assertThatThrownBy(() -> parser.parse(input))
        .isInstanceOf(NumberFormatException.class)
        .hasMessage("For input string: \"21474836470\"");
  }

  @Test
  void pack() throws IOException {
    StringIntegerParser<TestData> parser = createParser(3);
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    parser.pack(12, output);
    assertThat(output.toByteArray()).isEqualTo("012".getBytes());
  }

  @Test
  void pack_too_large_number() {
    StringIntegerParser<TestData> parser = createParser(3);
    assertThatThrownBy(() -> parser.pack(1234, new ByteArrayOutputStream()))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("str-int: value must be of length 3, but was 4");
  }
}
