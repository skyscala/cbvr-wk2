/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ddms.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;





/**
 *
 * @author zeyarlinhtike
 */
public class JsonUtil {
    
    
    private JsonUtil(){}
    
    public static final String toJsonString(Object obj) {

        if (obj != null) {
            Gson gson = new GsonBuilder().setDateFormat(DateUtil.dateFormatterPattern()).create();
            return gson.toJson(obj);
        }

        return null;
    }

    public static final <E> E fromJsonString(String jsonString, Class<E> clz) {
        if (jsonString != null) {
            Gson gson = new Gson();
            return gson.fromJson(jsonString, clz);
        }
        return null;
    }
}
