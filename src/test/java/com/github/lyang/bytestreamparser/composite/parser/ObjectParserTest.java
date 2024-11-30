package com.github.lyang.bytestreamparser.composite.parser;

import static com.github.lyang.bytestreamparser.api.data.testing.DataAssert.assertValue;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.lyang.bytestreamparser.api.data.testing.TestData;
import com.github.lyang.bytestreamparser.scalar.parser.BinaryParser;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ObjectParserTest {
  private static final String F1 = "F1";
  private static final String F2 = "F2";
  private static final String F3 = "F3";
  private ByteArrayInputStream input;
  private ObjectParser<TestData, TestData> parser;

  @BeforeEach
  void setUp() {
    input = new ByteArrayInputStream(new byte[] {0, 1, 2, 3});
    BinaryParser<TestData> field1Parser = new BinaryParser<>(F1, 1);
    BinaryParser<TestData> field2Parser = new BinaryParser<>(F2, 2);
    BinaryParser<TestData> field3Parser = new BinaryParser<>(F3, ignore -> false, 3);
    parser =
        new ObjectParser<>("map", TestData::new, List.of(field1Parser, field2Parser, field3Parser));
  }

  @Test
  void pack() throws IOException {
    TestData value = new TestData();
    value.set(F1, new byte[] {0}).set(F2, new byte[] {1, 2}).set(F3, new byte[] {3, 4, 5});
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    parser.pack(value, output);
    assertThat(output.toByteArray()).isEqualTo(new byte[] {0, 1, 2});
  }

  @Test
  void parse() throws IOException {
    TestData value = parser.parse(input);
    assertValue(value).hasValue(F1, new byte[] {0}).hasValue(F2, new byte[] {1, 2});
    assertThat(value.fields()).doesNotContain(F3);
    assertThat(input.available()).isEqualTo(1);
  }
}
