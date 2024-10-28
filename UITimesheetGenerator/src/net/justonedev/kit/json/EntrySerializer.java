package net.justonedev.kit.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Used to ensure XOR when entering entries, such that pause is only printed for an entry if and only if
 * vacation is false.
 */
public class EntrySerializer extends JsonSerializer<Month.Entry> {
    @Override
    public void serialize(Month.Entry entry, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("action", entry.getAction());
        gen.writeNumberField("day", entry.getDay());
        gen.writeStringField("start", entry.getStart());
        gen.writeStringField("end", entry.getEnd());

        // Serialize only one: pause or vacation
        if (entry.isVacation()) {
            gen.writeBooleanField("vacation", true);
        } else {
            gen.writeStringField("pause", entry.getPause());
        }

        gen.writeEndObject();
    }
}
