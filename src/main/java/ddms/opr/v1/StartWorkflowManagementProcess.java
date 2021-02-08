/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ddms.opr.v1;


import java.util.Map;

import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 *
 * @author zlhso
 */
public interface StartWorkflowManagementProcess {
    
    Map<String,Object> performProcessInit(ProcessInitCmd cmd);
    
    
    @Data
    public class ProcessInitCmd {

       
        @NotNull
        private ProcInitAction actionParams;
        
        @NotNull
        private Map<String, Object> content;
        

    }
    
    @Data
    public class ProcInitAction{
        
        @NotNull
        String refId;
        @NotNull
        String processName;        
        
        private Map<String, Object> attrs;
    }
}
