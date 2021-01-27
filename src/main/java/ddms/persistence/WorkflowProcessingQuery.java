/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ddms.persistence;

import ddms.persistence.entity.WorkflowData;
import ddms.persistence.entity.WorkflowEventLog;
import ddms.persistence.entity.WorkflowMngmntProcess;

import ddms.util.DateUtil;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import lombok.AllArgsConstructor;
import opr.query.generic.comp.QueryParam;
import opr.query.generic.comp.QueryResultPage;
import opr.query.util.QueryResultPageHelper;
import org.springframework.stereotype.Component;
import ddms.opr.v1.QueryWorkflowEventLogs;
import ddms.opr.v1.QueryWorkflows;
import ddms.opr.v1.QueryWorkflowManagementProcesses;
import ddms.persistence.entity.QWorkflowData;
import ddms.persistence.entity.QWorkflowEventLog;
import ddms.persistence.entity.QWorkflowMngmntProcess;

/**
 *
 * @author zlhso
 */
@Component
@AllArgsConstructor
class WorkflowProcessingQuery implements QueryWorkflows, QueryWorkflowEventLogs, QueryWorkflowManagementProcesses {

    private final EntityManager em;
    private final WorkflowInfoQueryHelper queryHelper;
    
    @Override
    public QueryResultPage<Map<String, Object>> queryAssets(QueryParam queryParam) {
        final SimpleDateFormat format = DateUtil.createDefaultDateFormatter();
        return QueryResultPageHelper.query(queryParam, QWorkflowData.workflowData, em, data -> {
            try {
                Field[] fields = WorkflowData.class.getDeclaredFields();
                Map<String,Class> jsonFieldNames = new HashMap<>();
                jsonFieldNames.put("dataContent",Map.class);
                return queryHelper.convert(fields, data, format, jsonFieldNames);
            } catch (Exception ex) {
                throw new UnsupportedOperationException(ex);
            }
        });
    }

    @Override
    public Map<String, Object> fetchAssetDetails(String assetid) {
        return queryHelper.fetchAssetDetails(assetid);
    }

    @Override
    public QueryResultPage<Map<String, Object>> queryAssetEvts(QueryParam queryParam) {
        final SimpleDateFormat format = DateUtil.createDefaultDateFormatter();
        return QueryResultPageHelper.query(queryParam, QWorkflowEventLog.workflowEventLog, em, data -> {
            try {
                return queryHelper.convertAssetEvtToMap((WorkflowEventLog)data, format);
            } catch (Exception ex) {
                throw new UnsupportedOperationException(ex);
            }
        });
    }

    @Override
    public Map<String, Object> fetchAssetEvtDetails(String evtId) {
        return queryHelper.fetchAssetEvtDetails(evtId);
    }
    
    

    @Override
    public QueryResultPage<Map<String, Object>> queryAssetMngmntProcesses(QueryParam queryParam) {
        
        return QueryResultPageHelper.query(queryParam, QWorkflowMngmntProcess.workflowMngmntProcess, em, data -> {
            try {                
                return queryHelper.convertAssetMngmntProcessToMap((WorkflowMngmntProcess)data);
            } catch (Exception ex) {
                throw new UnsupportedOperationException(ex);
            }
        });
    }

    @Override
    public List<Map<String, Object>> fetchAssetMngmntProcessByProcessId(String procId) {
        return queryHelper.fetchAssetMngmntProcessesByProcessId(procId);
    }

    
    
    
}
