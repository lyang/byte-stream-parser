package io.github.lyang.bytestreamparser.scalar.parser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.lyang.bytestreamparser.api.data.testing.TestData;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HexFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BcdStringParserTest {
  private BcdStringParser<TestData> parser;
  private ByteArrayInputStream input;

  @BeforeEach
  void setUp() {
    input = new ByteArrayInputStream(new byte[] {0x01, 0x23});
    parser = new BcdStringParser<>("bcd", 3);
  }

  @Test
  void pack() throws IOException {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    parser.pack("123", output);
    assertThat(output.toByteArray()).isEqualTo(new byte[] {0x01, 0x23});
  }

  @Test
  void pack_invalid_bcd_string() {
    assertThatThrownBy(() -> parser.pack("ABC", new ByteArrayOutputStream()))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("bcd: Invalid BCD String ABC");
  }

  @Test
  void parse() throws IOException {
    assertThat(parser.parse(input)).isEqualTo("123");
    assertThat(input.available()).isEqualTo(0);
  }

  @Test
  void parse_invalid_bcd_string() {
    assertThatThrownBy(
            () -> parser.parse(new ByteArrayInputStream(HexFormat.of().parseHex("abcd"))))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("bcd: Invalid BCD String bcd");
  }
}
