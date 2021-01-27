/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ddms.util;

import java.util.Map;
import java.util.TreeMap;
import org.springframework.stereotype.Component;

/**
 *
 * @author zlhso
 */

@Component
public class VariableProcessor {
 
    
    private static final String BOOLTYP="Boolean";
    private static final String STRTYP="String";
    private static final String TYP="type";
    private static final String VLU="value";    
        
    
    public void processVariables(Map<String,Object> src,Map<String,Object> variables){
        for(Map.Entry<String,Object> entry:src.entrySet()){
            String name=entry.getKey();
            Object valu=entry.getValue();
            
            if(valu instanceof String){
                processVariables(name, STRTYP, src, variables, null);
            }else if(valu instanceof Boolean){
                processVariables(name, BOOLTYP, src, variables, null);
            }
            
        }
    }
    
    public void processVariables(String name,String type,Map<String,Object> src,Map<String,Object> variables,Map<String,Object> defaultAttrs){
        Object value=src.get(name);
        if(value!=null){
            Map<String,Object> attrs=new TreeMap<>();
            attrs.put(VLU, value);
            attrs.put(TYP, type);
            variables.put(name, attrs);
        }else{
            if(defaultAttrs!=null){
                variables.put(name, defaultAttrs);
            }
        }
    }
}
