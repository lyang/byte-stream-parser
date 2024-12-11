package org.bytestreamparser.scalar.parser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import org.bytestreamparser.api.data.testing.TestData;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class TxtStringParserTest {
  public static final String VALUE = "123一二三";

  private static TxtStringParser<TestData> createParser(int length, String charset) {
    return new TxtStringParser<>("txt", length, Charset.forName(charset));
  }

  @ParameterizedTest
  @ValueSource(strings = {"US-ASCII", "IBM1047", "ISO-8859-1"})
  void parse_single_byte_charset(String charset) throws IOException {
    TxtStringParser<TestData> parser = createParser(3, charset);
    ByteArrayInputStream input = new ByteArrayInputStream(VALUE.getBytes(charset));
    String parsed = parser.parse(input);
    assertThat(parsed).isEqualTo(VALUE.substring(0, 3));
    assertThat(input.available()).isGreaterThan(0);
  }

  @ParameterizedTest
  @ValueSource(strings = {"UTF-8", "UTF-16"})
  void parse_multi_byte_charset(String charset) throws IOException {
    TxtStringParser<TestData> parser = createParser(4, charset);
    ByteArrayInputStream input = new ByteArrayInputStream(VALUE.getBytes(charset));
    String parsed = parser.parse(input);
    assertThat(parsed).isEqualTo(VALUE.substring(0, 4));
    assertThat(input.available()).isGreaterThan(0);
  }

  @ParameterizedTest
  @ValueSource(strings = {"US-ASCII", "IBM1047", "ISO-8859-1"})
  void parse_single_byte_charset_insufficient_data(String charset) {
    TxtStringParser<TestData> parser = createParser(1, charset);
    ByteArrayInputStream input = new ByteArrayInputStream(new byte[0]);
    assertThatThrownBy(() -> parser.parse(input))
        .isInstanceOf(EOFException.class)
        .hasMessage("End of stream reached after reading 0 bytes, bytes expected 1");
  }

  @ParameterizedTest
  @ValueSource(strings = {"UTF-8", "UTF-16"})
  void parse_multi_byte_charset_insufficient_data(String charset) {
    TxtStringParser<TestData> parser = createParser(1, charset);
    ByteArrayInputStream input = new ByteArrayInputStream(new byte[0]);
    assertThatThrownBy(() -> parser.parse(input))
        .isInstanceOf(EOFException.class)
        .hasMessage("End of stream reached after reading 0 chars, chars expected 1");
  }

  @ParameterizedTest
  @ValueSource(strings = {"US-ASCII", "IBM1047", "ISO-8859-1"})
  void pack_single_byte_charset(String charset) throws IOException {
    TxtStringParser<TestData> parser = createParser(3, charset);
    String expected = VALUE.substring(0, 3);
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    parser.pack(expected, output);
    assertThat(output.toByteArray()).isEqualTo(expected.getBytes(charset));
  }

  @ParameterizedTest
  @ValueSource(strings = {"UTF-8", "UTF-16"})
  void pack_multi_byte_charset(String charset) throws IOException {
    TxtStringParser<TestData> parser = createParser(6, charset);
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    parser.pack(VALUE, output);
    assertThat(output.toByteArray()).isEqualTo(VALUE.getBytes(charset));
  }

  @ParameterizedTest
  @ValueSource(strings = {"US-ASCII", "IBM1047", "ISO-8859-1", "UTF-8", "UTF-16"})
  void pack_insufficient_data(String charset) {
    TxtStringParser<TestData> parser = createParser(6, charset);
    assertThatThrownBy(() -> parser.pack("", new ByteArrayOutputStream()))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("txt: value must be of length 6, but was 0");
  }
}
