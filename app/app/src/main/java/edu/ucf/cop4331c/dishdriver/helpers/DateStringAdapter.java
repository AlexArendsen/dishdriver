package edu.ucf.cop4331c.dishdriver.helpers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by copper on 4/8/17.
 */

public class DateStringAdapter implements JsonDeserializer<Date> {

    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String src = json.getAsJsonPrimitive().getAsString();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        try {
            return fmt.parse(src);
        } catch (Exception e) {
            throw new JsonParseException("Date deserialization error: " + e.getMessage());
        }
    }

}
