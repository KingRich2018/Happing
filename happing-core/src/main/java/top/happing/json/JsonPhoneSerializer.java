package top.happing.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import top.happing.utils.StringUtils;

public class JsonPhoneSerializer extends JsonSerializer<String> {

	@Override
	public void serialize(String phone, JsonGenerator gen, SerializerProvider serializers)
			throws IOException, JsonProcessingException {
		if(StringUtils.isNotEmpty(phone) && phone.length() == 11) {
			phone = phone.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
		} else {
			phone = "";
		}
		gen.writeString(phone);
	}
}
