package goit.homeworks;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class Utils {

    public static <T> T parseFromJson(String json, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, clazz);
        } catch (Throwable e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static <T> String parseToJson(T object) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(object);
        } catch (Throwable e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static <T> List<T> parseObjectsJsonArray(String json, TypeReference<T[]> typeReference) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return List.of(mapper.readValue(json, typeReference));
        } catch (Throwable e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static <T> List<T> parseObjectsJson(String json, TypeReference<List<T>> typeReference) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, typeReference);
        } catch (Throwable e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
