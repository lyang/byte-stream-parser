package io.github.lyang.bytestreamparser.scalar.parser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.lyang.bytestreamparser.api.data.testing.TestData;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BinaryParserTest {
  private static final String ID = "bin";
  private BinaryParser<TestData> parser;

  @BeforeEach
  void setUp() {
    parser = new BinaryParser<>(ID, 2);
  }

  @Test
  void applicable() {
    assertThat(new BinaryParser<>(ID, 2).applicable(null)).isTrue();
    assertThat(new BinaryParser<>(ID, ignore -> false, 2).applicable(null)).isFalse();
  }

  @Test
  void pack() throws IOException {
    byte[] expected = {0, 1};
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    parser.pack(expected, output);
    assertThat(output.toByteArray()).isEqualTo(expected);
  }

  @Test
  void pack_insufficient_data() {
    assertThatThrownBy(() -> parser.pack(new byte[0], new ByteArrayOutputStream()))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("bin: value must be of length 2, but was 0");
  }

  @Test
  void parse() throws IOException {
    byte[] expected = {0, 1};
    assertThat(parser.parse(new ByteArrayInputStream(expected))).isEqualTo(expected);
  }

  @Test
  void parse_insufficient_data() {
    assertThatThrownBy(() -> parser.parse(new ByteArrayInputStream(new byte[0])))
        .isInstanceOf(EOFException.class)
        .hasMessage("End of stream reached after reading 0 bytes, bytes expected 2");
  }
}
