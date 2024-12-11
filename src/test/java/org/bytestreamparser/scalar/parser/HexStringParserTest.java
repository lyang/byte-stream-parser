package org.bytestreamparser.scalar.parser;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HexFormat;
import org.bytestreamparser.api.data.testing.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HexStringParserTest {
  private ByteArrayInputStream input;

  @BeforeEach
  void setUp() {
    input = new ByteArrayInputStream(HexFormat.of().parseHex("0123"));
  }

  @Test
  void pack() throws IOException {
    HexStringParser<TestData> parser = new HexStringParser<>("hex", 2);
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    parser.pack("01", output);
    assertThat(output.toByteArray()).isEqualTo(HexFormat.of().parseHex("01"));
  }

  @Test
  void pack_odd_length_value() throws IOException {
    HexStringParser<TestData> parser = new HexStringParser<>("hex", 3);
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    parser.pack("123", output);
    assertThat(output.toByteArray()).isEqualTo(HexFormat.of().parseHex("0123"));
  }

  @Test
  void parse() throws IOException {
    HexStringParser<TestData> parser = new HexStringParser<>("hex", 2);
    assertThat(parser.parse(input)).isEqualTo("01");
  }

  @Test
  void parse_odd_length_value() throws IOException {
    HexStringParser<TestData> parser = new HexStringParser<>("hex", 3);
    assertThat(parser.parse(input)).isEqualTo("123");
  }
}
