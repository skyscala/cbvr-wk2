/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ddms.persistence;


import ddms.opr.v1.CompleteWorkflowProcessingTask.TaskUpdCmd;
import ddms.opr.v1.StartWorkflowManagementProcess.ProcessInitCmd;
import ddms.persistence.entity.WorkflowData;
import ddms.persistence.entity.WorkflowEventLog;
import ddms.persistence.entity.WorkflowMngmntProcess;
import ddms.persistence.entity.WorkflowMngmntProcessPk;
import ddms.util.DateUtil;
import ddms.util.HttpGetUtil;
import ddms.util.HttpPostUtil;
import ddms.util.JsonUtil;
import ddms.util.ValidatorUtil;
import ddms.util.VariableProcessor;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ddms.opr.v1.StartWorkflowManagementProcess;
import ddms.opr.v1.CompleteWorkflowProcessingTask;

/**
 *
 * @author zlhso
 */
@Component
@Log4j2
class WorkflowProcessingAdapter implements CompleteWorkflowProcessingTask,StartWorkflowManagementProcess{

    private final String requestRoute = "http://localhost:8080/engine-rest/process-definition/key/%s/start";
    private final String requestProcessData = "http://localhost:8080/engine-rest/task/";
    private final String requestCompleteTask = "http://localhost:8080/engine-rest/task/%s/complete";

    
    private final String WORKFLOWID="workflowId";
    private final String PROCNAME="processName";
    private final String PROCID="processId";
    private final String TID="taskId";
    
    @Autowired
    private WorkflowDataRepo assetRepo;
    @Autowired
    private WorkflowEvtLogRepo assetEvtRepo;
    @Autowired
    private WorkflowMngmntProcessRepo processRepo;
    @Autowired
    private VariableProcessor varProcr;
    @Autowired
    private WorkflowInfoQueryHelper queryHelper;

    
    
    
    @Transactional
    @Override
    public Map<String,Object> performProcessInit(ProcessInitCmd cmd) {

        ValidatorUtil.validate(cmd);
        
        Map<String, Object> metaData = cmd.getActionParams();
        
        String assetId=metaData.get(WORKFLOWID).toString();
        String procName = metaData.get(PROCNAME).toString();
        
        Map<String, Object> content = cmd.getContent();
        Map<String, Object> variables = new HashMap<>();
        
        varProcr.processVariables(metaData, variables);
        
        
        
        String path = String.format(requestRoute, procName);

        log.info("requestRoute:{}", path);
        Map<String, Object> processPayload = new HashMap<>();
        processPayload.put("variables", variables);
        log.info("processPayload:{}", processPayload);

        HttpPostUtil.PostResponse response = HttpPostUtil.postJson(path, processPayload);
        log.info("Process initiation response code: {}", response.getStatusCode());

        if (response.getStatusCode() < 300) {
            Map<String,Object> data=JsonUtil.fromJsonString(response.getResponseBody(),Map.class);
            log.info("response process started:{}",data);
            String processId=data.get("id").toString();
            Date now = DateUtil.now();
            WorkflowEventLog assetEvt = saveAssetEvt(assetId, processId, null, metaData, content, now);
            saveAsset(assetEvt);  
            saveProcess(procName,assetEvt);
            return fetchEvt(assetEvt);
            
        }
        
        throw new RuntimeException(response.getResponseBody());

    }
    
    @Transactional
    @Override
    public Map<String,Object> performTaskCompletion(TaskUpdCmd cmd) {
        
        ValidatorUtil.validate(cmd);
        
        Map<String, Object> metaData = cmd.getActionParams();
        
        String assetId = metaData.get(WORKFLOWID).toString();
        String processId = metaData.get(PROCID).toString();        
        String taskId=metaData.get(TID).toString();
        
        Map<String, Object> content = cmd.getContent();
        Map<String, Object> variables = new HashMap<>();
        varProcr.processVariables(metaData, variables);
    
        String path=String.format(requestCompleteTask, taskId);
        log.info("requestRoute:{}",path);
        Map<String,Object> processPayload=new HashMap<>();
        processPayload.put("variables", variables);
        log.info("taskPayload:{}",processPayload);
        
        HttpPostUtil.PostResponse response=HttpPostUtil.postJson(path, processPayload);
        
        log.info("Task completion response code: {}",response.getStatusCode());
        if(response.getStatusCode()<300){  
            Map<String,Object> data=JsonUtil.fromJsonString(response.getResponseBody(),Map.class);
            log.info("response process started:{}",data);            
            Date now = DateUtil.now();
            WorkflowEventLog assetEvt = saveAssetEvt(assetId, processId, taskId, metaData, content, now);
            saveAsset(assetEvt);  
            saveProcess(null,assetEvt);
            return fetchEvt(assetEvt);
        }
        throw new RuntimeException(response.getResponseBody()); 
    }
    
    
    
    
    
    private Map<String,Object> fetchEvt(WorkflowEventLog evt){
        return queryHelper.fetchAssetEvtDetails(evt.getEvtId());
    }
    
    private WorkflowEventLog saveAssetEvt(String assetId,
            String processId,
            String complTaskId,            
            Map<String,Object> metaData,
            Map<String,Object> dataContent,Date now) {
        
        WorkflowEventLog assetEvt = new WorkflowEventLog();  
        assetEvt.setRefId(assetId);
        assetEvt.setEvtId(UUID.randomUUID().toString());
        assetEvt.setProcessId(processId);
        assetEvt.setCompletedTaskId(complTaskId);
        assetEvt.setActionParams(JsonUtil.toJsonString(metaData));
        List<Map<String,Object>> processDataList=fetchProcessData(processId, 3);
        
        /*
        List<String> nextTaskIds=processDataList.stream()
                .filter(e -> e.containsKey("taskId"))
                .map(m->m.get("taskId").toString())
                .collect(Collectors.toList());
        */
        if(processDataList!=null&&!processDataList.isEmpty()){
            assetEvt.setNextTasks(JsonUtil.toJsonString(processDataList));
        }
        
        assetEvt.setDataContent(JsonUtil.toJsonString(dataContent));
        assetEvt.setRecordedDate(now);
        assetEvtRepo.save(assetEvt);
        
        return assetEvt;
    }
    
    private WorkflowMngmntProcess saveProcess(String processName,WorkflowEventLog assetEvt) {
        
        WorkflowMngmntProcessPk pk=new WorkflowMngmntProcessPk();
        pk.setRefId(assetEvt.getRefId());
        pk.setProcessId(assetEvt.getProcessId());
        WorkflowMngmntProcess process = processRepo.findByProcessPk(pk);
        String remainingTasks=assetEvt.getNextTasks();
        if(remainingTasks==null||remainingTasks.trim().isEmpty()){
            if(process!=null){
                processRepo.delete(process);
                return null;
            }
        }else{
            if (process == null) {
                process = new WorkflowMngmntProcess();
                process.setProcessPk(pk);
                process.setProcessName(processName);
            }
            process.setTasks(assetEvt.getNextTasks());        
            processRepo.save(process);
        }
        return process;
        
    }

    private WorkflowData saveAsset(WorkflowEventLog assetEvt) {
        WorkflowData asset = assetRepo.findByRefId(assetEvt.getRefId());
        Date now=assetEvt.getRecordedDate();
        if (asset == null) {
            asset = new WorkflowData();
            asset.setCreatedDate(now);
            asset.setRefId(assetEvt.getRefId());
        }        
        asset.setDataContent(assetEvt.getDataContent());
        asset.setUpdatedDate(now);
        asset.setLastActionParams(assetEvt.getActionParams());
        assetRepo.save(asset);
        return asset;
    }
    
    
    
    private List<Map<String,Object>> fetchProcessData(String processId,int count){        
        if(count>3){
            throw new RuntimeException("Fail to fetch process data!");
        }        
        Map<String,Object> params=new HashMap<>();
        params.put("processInstanceId",processId);
        HttpGetUtil.GetResponse response=HttpGetUtil.getJson(requestProcessData, params);
        
        if(response.getStatusCode()<300){
            if(response.getResponseBody().trim().isEmpty()){
                return new ArrayList<>();
            }
            List<Map<String,Object>> data=JsonUtil.fromJsonString(response.getResponseBody(),List.class);
            log.info("response process data:{}",data);            
            return data;
        }else{
            return fetchProcessData(processId, count+1);
        }
    }

    

    

}
