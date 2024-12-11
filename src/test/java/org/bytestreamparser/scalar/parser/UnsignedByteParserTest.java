package org.bytestreamparser.scalar.parser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.bytestreamparser.api.data.testing.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UnsignedByteParserTest {

  private ByteArrayInputStream input;
  private UnsignedByteParser<TestData> parser;

  @BeforeEach
  void setUp() {
    input = new ByteArrayInputStream(new byte[] {1, 2});
    parser = new UnsignedByteParser<>("ubyte");
  }

  @Test
  void parse() throws IOException {
    assertThat(parser.parse(input)).isEqualTo(1);
    assertThat(input.available()).isEqualTo(1);
  }

  @Test
  void pack() throws IOException {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    parser.pack(1, output);
    assertThat(output.toByteArray()).isEqualTo(new byte[] {1});
  }

  @Test
  void pack_throws_exception_if_too_large() {
    assertThatThrownBy(() -> parser.pack(256, new ByteArrayOutputStream()))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("ubyte: value must be between 0 and 255, but was 256");
  }

  @Test
  void pack_throws_exception_if_too_small() {
    assertThatThrownBy(() -> parser.pack(-1, new ByteArrayOutputStream()))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("ubyte: value must be between 0 and 255, but was -1");
  }
}
