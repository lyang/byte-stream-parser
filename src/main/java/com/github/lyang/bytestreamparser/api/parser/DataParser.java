package com.github.lyang.bytestreamparser.api.parser;

import com.github.lyang.bytestreamparser.api.data.Data;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.Predicate;

public abstract class DataParser<P extends Data<P>, V> {
  protected final String id;
  private final Predicate<P> applicable;

  protected DataParser(String id, Predicate<P> applicable) {
    this.id = id;
    this.applicable = applicable;
  }

  public String getId() {
    return id;
  }

  public boolean applicable(P parent) {
    return applicable.test(parent);
  }

  public abstract void pack(V value, OutputStream output) throws IOException;

  public abstract V parse(InputStream input) throws IOException;
}
