package io.github.lyang.bytestreamparser.api.data;

import static io.github.lyang.bytestreamparser.api.data.testing.DataAssert.assertValue;
import static org.assertj.core.api.Assertions.assertThat;

import io.github.lyang.bytestreamparser.api.data.testing.TestData;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DataTest {
  public static final String ID = "id";
  private TestData data;

  @BeforeEach
  void setUp() {
    data = new TestData();
  }

  @Test
  void fields() {
    assertThat(data.fields()).isEmpty();
    data.set(ID, 1);
    assertThat(data.fields()).isEqualTo(Set.of(ID));
  }

  @Test
  void get() {
    assertValue(data).hasValue(ID, null);
    data.set(ID, 1);
    assertValue(data).hasValue(ID, 1);
  }

  @Test
  void set() {
    assertValue(data).hasValue(ID, null);
    data.set(ID, 1);
    assertValue(data).hasValue(ID, 1);
  }

  @Test
  void clear() {
    data.set(ID, 1);
    assertThat(data.fields()).contains(ID);
    assertThat(data.clear(ID).fields()).isEmpty();
  }

  @Test
  void mutate() {
    data.set(ID, 1);
    data.<Integer>mutate(ID, v1 -> v1 * 2).<Integer>mutate(ID, v -> v * 10);
    assertValue(data).hasValue(ID, 20);
  }
}
