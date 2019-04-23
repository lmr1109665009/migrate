package com.chen.common.component.jackson;

import com.chen.common.utils.DateUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.Date;

public class DateTimeDeserializer extends JsonDeserializer<Date>{

    public Date deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException {
        String date = jp.getText();
        return DateUtil.getDate(date, DateUtil.FORMAT_DATETIME);
    }
}
