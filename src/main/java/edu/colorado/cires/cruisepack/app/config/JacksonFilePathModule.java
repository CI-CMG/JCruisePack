package edu.colorado.cires.cruisepack.app.config;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.util.VersionUtil;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.stereotype.Component;

@Component
public class JacksonFilePathModule extends SimpleModule {

  public JacksonFilePathModule() {
    super(VersionUtil.parseVersion("0.1", "edu.colorado.cires.cruisepack.app.config", "JacksonFilePathModule"));
    addDeserializer(Path.class, new PathDeserializer());
    addSerializer(Path.class, new PathSerializer());
  }

  @Override
  public String getModuleName() {
    return getClass().getSimpleName();
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

  @Override
  public boolean equals(Object o) {
    return this == o;
  }

  private static class PathDeserializer extends StdDeserializer<Path> {

    public PathDeserializer() {
      super(Path.class);
    }

    @Override
    public Path deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
      if (p.getText() == null) {
        return null;
      }
      return Paths.get(p.getText().trim());
    }
  }

  private static class PathSerializer extends JsonSerializer<Path> {

    @Override
    public void serialize(Path value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
      if (value == null) {
        gen.writeNull();
      } else {
        gen.writeString(value.toString());
      }
    }
  }
}
