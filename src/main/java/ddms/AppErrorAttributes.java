/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ddms;

import java.util.Map;
import java.util.TreeMap;
import javax.validation.ConstraintViolationException;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.web.error.ErrorAttributeOptions;

import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

/**
 *
 * @author zlhso
 */
@Component
@Log4j2
public class AppErrorAttributes extends DefaultErrorAttributes{

    
    private static final String TAG = AppErrorAttributes.class.getName();
    
    private static final String ERR ="error";
    private static final String ERRDESC ="error_description";
    private static final String ERRCLASS ="error_class";
    private static final String VALIDATION ="validation";
    
    
    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        
        Map<String,Object> map=new TreeMap<>();
        try{
            map.putAll(super.getErrorAttributes(webRequest, options));
            Throwable t=getError(webRequest);            
            if(t!=null){
                map.put(ERRCLASS, t.getClass().getSimpleName());
                map.put(ERRDESC, t.getMessage());
                map.put(ERR, t.getMessage());
                if(t instanceof ConstraintViolationException){                    
                    ConstraintViolationException ce=(ConstraintViolationException)t;
                    map.put(VALIDATION, ConstraintViolationMessageFactory.create(ce));
                }
            }
        }catch(Exception ex){
            log.debug(TAG, ex.getMessage());
        }
        return map;
    }
    
    
    
}
