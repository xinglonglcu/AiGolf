package com.xlong.data.net;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fanei on 2018/1/23.
 * Email : faneizn@gmail.com
 */

public class ItemTypeAdapterFactory implements TypeAdapterFactory {

    private final static String KEY_DATAS = "datas";
    private final static String KEY_EMPTY_STRING = "";

    public <T> TypeAdapter<T> create(Gson gson, final TypeToken<T> type) {

        final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);
        final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
        final Class<T> rawType = (Class<T>) type.getRawType();

        return new TypeAdapter<T>() {

            public void write(JsonWriter out, T value) throws IOException {
                delegate.write(out, value);
            }

            public T read(JsonReader in) throws IOException {

                //兼容服务端将[]类型的数据返回成""或者null
                if (rawType == List.class || rawType == ArrayList.class) {
                    if (in.peek() != JsonToken.BEGIN_ARRAY) {
                        in.skipValue();
                        return delegate.fromJsonTree(new JsonArray());
                    }
                }
                //兼容服务端将String类型的数据返回成 非String 和 非数字 类型
                else if (rawType == String.class) {
                    if (in.peek() != JsonToken.STRING && in.peek() != JsonToken.NUMBER
                            && in.peek() != JsonToken.BOOLEAN && in.peek() != JsonToken.NULL) {
                        in.skipValue();
                        return delegate.fromJsonTree(new JsonPrimitive(""));
                    }
                }
                //兼容服务端将NUMBER类型的数据返回成 非NUMBER 和 非String 类型
                else if (rawType == Integer.class || rawType == int.class
                        || rawType == Double.class || rawType == double.class
                        || rawType == Float.class || rawType == float.class
                        || rawType == Short.class || rawType == short.class
                        || rawType == Long.class || rawType == long.class) {
                    if (in.peek() != JsonToken.NUMBER && in.peek() != JsonToken.STRING) {
                        in.skipValue();
                        return delegate.fromJsonTree(new JsonPrimitive(0));
                    }
                }
                //兼容服务端将Boolean类型的数据返回成 非Boolean 类型
                else if (rawType == Boolean.class || rawType == boolean.class) {
                    if (in.peek() != JsonToken.BOOLEAN) {
                        in.skipValue();
                        return delegate.fromJsonTree(new JsonPrimitive(false));
                    }
                }
                //兼容服务端将JsonObject类型的数据返回成 非JsonObject 类型
                else {
                    if (in.peek() != JsonToken.BEGIN_OBJECT && in.peek() != JsonToken.NULL) {
                        in.skipValue();
                        return delegate.fromJsonTree(JsonNull.INSTANCE);
                    }
                }

                JsonElement jsonElement = elementAdapter.read(in);
                if (jsonElement.isJsonObject()) {
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    if (jsonObject.has(KEY_DATAS)) {
                        JsonElement datas = jsonObject.get(KEY_DATAS);
                        if (datas.isJsonPrimitive()) {
                            if (KEY_EMPTY_STRING.equals(datas.getAsString())) {
                                jsonElement = new JsonObject();
                            }
                        }
                    }
                }

                return delegate.fromJsonTree(jsonElement);
            }
        }.

                nullSafe();
    }
}