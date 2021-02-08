/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ddms.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.ReflectionUtils;

/**
 *
 * @author zlhso
 */
@Log4j2
public class ReflcUtil {
    
    private ReflcUtil(){}
    
    public static <T> Map<String,Object> transformToMap(Class<T> clz,T src){
        Field[] fields=clz.getDeclaredFields();
        Map<String,Object> map=new HashMap<>();
        for(Field f:fields){
            try{
                ReflectionUtils.makeAccessible(f);
                Object value=f.get(src);
                log.debug("{}={}",f.getName(),value);
                map.put(f.getName(), value);

            }catch(Exception ex){
                log.debug("Error - {}", ex.getMessage());
            }
        }
        return map;
    }
}
