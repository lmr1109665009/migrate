package com.chen.common.component.jackson;

import com.chen.common.utils.DateUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Date;

public class DateSerializer extends JsonSerializer<Date> {
 
    @Override
    public void serialize(Date dateTime, JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(DateUtil.formatDate(dateTime,DateUtil.FORMAT_DATE));
    }
}
