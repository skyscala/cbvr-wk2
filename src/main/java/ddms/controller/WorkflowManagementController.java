/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ddms.controller;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import opr.query.generic.comp.QueryParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ddms.opr.v1.QueryWorkflowEventLogs;
import ddms.opr.v1.QueryWorkflows;
import ddms.opr.v1.QueryWorkflowManagementProcesses;
import ddms.opr.v1.StartWorkflowManagementProcess;
import ddms.opr.v1.CompleteWorkflowProcessingTask;

/**
 *
 * @author zlhso
 */

@RestController
@CrossOrigin
@Log4j2
@AllArgsConstructor
public class WorkflowManagementController {
    
    
    
    private final StartWorkflowManagementProcess starter;
    private final CompleteWorkflowProcessingTask taskCompletionMgr;
    private final QueryWorkflows queryAssets;
    private final QueryWorkflowEventLogs queryEvts;
    private final QueryWorkflowManagementProcesses queryProcesses;
    
    
    @PostMapping(value = "/workflows")
    public ResponseEntity<Object> queryWorkflows(@RequestBody QueryParam queryParam){
        return ResponseEntity.ok(queryAssets.queryAssets(queryParam));
    }
    
    @GetMapping(value = "/workflow-details/{refId}")
    public ResponseEntity<Object> queryWorkflowDetails(@PathVariable(value = "refId") String refId){
        return ResponseEntity.ok(queryAssets.fetchAssetDetails(refId));
    }
        
    @PostMapping(value = "/workflow/process/events")
    public ResponseEntity<Object> queryWrokflowEvts(@RequestBody QueryParam queryParam){
        return ResponseEntity.ok(queryEvts.queryAssetEvts(queryParam));
    }
    
    @GetMapping(value = "/workflow/process/event-details/{evtId}")
    public ResponseEntity<Object> fetchWorkflowEvtDetails(@PathVariable(value = "evtId") String procId){
        return ResponseEntity.ok(queryEvts.fetchAssetEvtDetails(procId));
    }
    
    @PostMapping(value = "/workflow/process/list")
    public ResponseEntity<Object> queryWorkflowProcessList(@RequestBody QueryParam queryParam){
        return ResponseEntity.ok(queryProcesses.queryAssetMngmntProcesses(queryParam));
    }
    
    @GetMapping(value = "/workflow/process-details/{procId}")
    public ResponseEntity<Object> queryWorkflowProcessDetails(@PathVariable(value = "procId") String procId){
        return ResponseEntity.ok(queryProcesses.fetchAssetMngmntProcessByProcessId(procId));
    }
    
    @PostMapping(value = "/workflow/process/start")
    public ResponseEntity<Object> startWorkflowProcess(@RequestBody StartWorkflowManagementProcess.ProcessInitCmd cmd){
     
        return ResponseEntity.ok(starter.performProcessInit(cmd));
    }
    
    @PostMapping(value = "/workflow/task/complete")
    public ResponseEntity<Object> completeWorkflowProcessTask(@RequestBody CompleteWorkflowProcessingTask.TaskUpdCmd cmd){     
        return ResponseEntity.ok(taskCompletionMgr.performTaskCompletion(cmd));
    }
    
}
