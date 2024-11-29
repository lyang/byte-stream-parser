package com.github.lyang.bytestreamparser.scalar.parser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.lyang.bytestreamparser.api.data.testing.TestData;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UnsignedShortParserTest {
  private ByteArrayInputStream input;
  private UnsignedShortParser<TestData> parser;

  @BeforeEach
  void setUp() {
    input = new ByteArrayInputStream(new byte[] {1, 2, 3});
    parser = new UnsignedShortParser<>("ushort");
  }

  @Test
  void parse() throws IOException {
    assertThat(parser.parse(input)).isEqualTo(258);
    assertThat(input.available()).isEqualTo(1);
  }

  @Test
  void pack() throws IOException {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    parser.pack(258, output);
    assertThat(output.toByteArray()).isEqualTo(new byte[] {1, 2});
  }

  @Test
  void pack_throws_exception_if_too_large() {
    assertThatThrownBy(() -> parser.pack(65536, new ByteArrayOutputStream()))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("ushort: value must be between 0 and 65535, but was 65536");
  }

  @Test
  void pack_throws_exception_if_too_small() {
    assertThatThrownBy(() -> parser.pack(-1, new ByteArrayOutputStream()))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("ushort: value must be between 0 and 65535, but was -1");
  }
}
