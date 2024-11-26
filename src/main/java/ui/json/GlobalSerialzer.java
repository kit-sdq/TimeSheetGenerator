/* Licensed under MIT 2024. */
package ui.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class GlobalSerialzer extends JsonSerializer<Global> {
	@Override
	public void serialize(Global global, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		gen.writeStartObject();
		gen.writeStringField("$schema", global.getSchema());
		gen.writeStringField("name", global.getName());
		gen.writeNumberField("staffId", global.getStaffId());
		gen.writeStringField("department", global.getDepartment());
		gen.writeStringField("workingTime", global.getWorkingTime());
		gen.writeNumberField("wage", global.getWage());
		gen.writeStringField("workingArea", global.getWorkingArea());
		gen.writeEndObject();
	}
}
