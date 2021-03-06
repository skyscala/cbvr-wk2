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
public interface CompleteWorkflowProcessingTask {
    
    Map<String,Object> performTaskCompletion(TaskUpdCmd cmd);
    
    @Data
    public class TaskUpdCmd {
        
        @NotNull
        private TaskComplAction actionParams;
        
        @NotNull
        private Map<String, Object> content;

    }
    
    @Data
    public class TaskComplAction{
        
        @NotNull
        String refId;
        @NotNull
        String processId;        
        @NotNull
        String taskId;
        
        private Map<String, Object> attrs;
        
    }
}
