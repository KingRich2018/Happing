package top.happing.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import top.happing.utils.JsonUtils;
import top.happing.utils.StringUtils;

public class JsonStringArraySerializer extends JsonSerializer<String> {

	@Override
	public void serialize(String value, JsonGenerator gen, SerializerProvider serializers)
			throws IOException, JsonProcessingException {
		String[] arr = {};
		if(StringUtils.isNotEmpty(value)) {
			arr = (String[])JsonUtils.fromJsonString(value, String[].class);
		}
		gen.writeObject(arr);
	}
	
}
