package me.anacoimbra.androidfirebase.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.Map;

/**
 * Created by aninh on 02/28/2018.
 */

public class JsonUtils {

    public static <T> T map2Object(Map<String, Object> map, Class<T> tClass) {
        Gson gson = new Gson();
        JsonElement element = gson.toJsonTree(map);
        return gson.fromJson(element, tClass);
    }
}
