/* Licensed under MIT 2024. */
package ui.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class MonthSerializer extends JsonSerializer<Month> {

	@Override
	public void serialize(Month month, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		gen.writeStartObject();
		gen.writeStringField("$schema", month.getSchema());
		gen.writeNumberField("year", month.getYear());
		gen.writeNumberField("month", month.getMonth());
		gen.writeStringField("pred_transfer", month.getPredTransfer());
		gen.writeStringField("succ_transfer", month.getSuccTransfer());

		gen.writeFieldName("entries");
		JsonSerializer<Object> entrySerializer = serializers.findValueSerializer(Month.Entry.class);
		gen.writeStartArray();
		for (Month.Entry entry : month.getEntries()) {
			entrySerializer.serialize(entry, gen, serializers);
		}
		gen.writeEndArray();

		gen.writeEndObject();
	}
}
