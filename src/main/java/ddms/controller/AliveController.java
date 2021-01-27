/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ddms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author zeyarhtike
 */

@RestController
@CrossOrigin()
public class AliveController {
    
    
    @GetMapping(value = "/alive")
    public ResponseEntity<Object> alive(){
        return ResponseEntity.ok(""+System.currentTimeMillis());
    }
}
