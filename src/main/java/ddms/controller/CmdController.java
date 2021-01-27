/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ddms.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author zlhso
 */

@RestController
@CrossOrigin
@Log4j2
public class CmdController {
    
    @PostMapping(value = "/invoke-cmd")
    public ResponseEntity<Object> invoke(@RequestBody Map<String,Object> payload){
    
        log.info("Incoming reqest: {}",payload);
        
        //Random rand=new Random();
        boolean flag=payload.containsKey("proceed");
        if(flag){
            Map<String,Object> map=new HashMap<>();
            map.put("cmdId", UUID.randomUUID().toString());            
            return ResponseEntity.ok(map);
        }
        
        throw new UnsupportedOperationException("Not supported operation.");
    }
    
    
    @PostMapping(value = "/nofity")
    public ResponseEntity<Object> notify(@RequestBody Map<String,Object> payload){
    
        log.info("Incoming reqest: {}",payload);        
        Map<String,Object> map=new HashMap<>();
        map.put("notificationId", UUID.randomUUID().toString());            
        return ResponseEntity.ok(map);
        
    }
    
    
}
