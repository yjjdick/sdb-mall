package io.sdb.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.sdb.enums.GeneralEnum;

import java.io.IOException;

public class Int2BoolSerializer extends JsonSerializer<Integer> {
    @Override
    public void serialize(Integer integer, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeBoolean(integer == GeneralEnum.TRUE.getCode()?true:false);
    }
}
