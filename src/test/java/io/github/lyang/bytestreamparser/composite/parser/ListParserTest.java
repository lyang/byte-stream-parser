package io.github.lyang.bytestreamparser.composite.parser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.lyang.bytestreamparser.api.data.testing.TestData;
import io.github.lyang.bytestreamparser.scalar.parser.TxtStringParser;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ListParserTest {
  private ListParser<TestData, String> parser;

  @BeforeEach
  void setUp() {
    parser = new ListParser<>("list", new TxtStringParser<>("item", 2, Charset.defaultCharset()));
  }

  @Test
  void pack() throws IOException {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    parser.pack(List.of("bc", "de"), output);
    assertThat(output.toByteArray()).isEqualTo("bcde".getBytes());
  }

  @Test
  void pack_incompatible_values() {
    assertThatThrownBy(() -> parser.pack(List.of("abc", "def"), new ByteArrayOutputStream()))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("item: value must be of length 2, but was 3");
  }

  @Test
  void parse() throws IOException {
    List<String> parsed = parser.parse(new ByteArrayInputStream("abcd".getBytes()));
    assertThat(parsed).isEqualTo(List.of("ab", "cd"));
  }

  @Test
  void parse_insufficient_data() {
    assertThatThrownBy(() -> parser.parse(new ByteArrayInputStream("abc".getBytes())))
        .isInstanceOf(EOFException.class)
        .hasMessage("End of stream reached after reading 1 chars, chars expected 2");
  }
}
