package Handlers;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class InstantAdapter extends TypeAdapter<Instant> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;
    private static final ZoneId zone = ZoneId.systemDefault();

    @Override
    public void write(JsonWriter out, Instant value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            LocalDateTime localDateTime = LocalDateTime.ofInstant(value, zone);
            out.value(formatter.format(value));
        }
    }

    @Override
    public Instant read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        } else {
            String dateStr = in.nextString();
            LocalDateTime localDateTime = LocalDateTime.parse(dateStr, formatter);
            return localDateTime.atZone(zone).toInstant();
        }
    }
}
