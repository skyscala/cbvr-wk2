/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ddms;

import ddms.util.ProcessResultFactory;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;

import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.MediaType;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;





/**
 *
 * @author zlhso
 */
@Component
@Log4j2
@CrossOrigin
public class AppErrorController extends BasicErrorController{

    private static final String TAG = AppErrorController.class.getName();
    
    private static final String STATUS ="status";
    private static final String ERR ="error";
    private static final String ERRDESC ="error_description";
    private static final String ERRCLASS ="error_class";
    private static final String VALIDATION ="validation";
    
   
    
    
    public AppErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes, new ErrorProperties());
    }

    
    
    @Override
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        try{
            
            
            Map<String,Object> map=super.getErrorAttributes(request, super.getErrorAttributeOptions(request, MediaType.ALL));
            log.debug("Error Attributes: "+map);            
            Object temp=map.get(STATUS);
            int status=Integer.parseInt(temp.toString()); 
            Map<String,Object> refineMap = ProcessResultFactory.createFailResult();
            cpyErr(map, refineMap, ERR, ERRDESC);
            cpyErr(map, refineMap, ERRCLASS);
            cpyErr(map, refineMap, VALIDATION);
            
                   
            return ResponseEntity.status(status).body(refineMap);
        }catch(Exception ex){
            log.debug(TAG, ex);
            Map<String,Object> map= ProcessResultFactory.createFailResult();
            map.put(ERRCLASS, ex.getClass().getSimpleName());
            return ResponseEntity.badRequest().body(map);
        }
        
    }
    
    
    private void cpyErr(Map<String,Object> src,Map<String,Object> des,String k){
        Object v=src.get(k);
        if(v!=null){
            des.put(k, v);
        }
    }
    private void cpyErr(Map<String,Object> src,Map<String,Object> des,String k1,String k2){
        Object v=src.get(k1);
        if(v!=null){
            des.put(k2, v);
        }
    }
}
