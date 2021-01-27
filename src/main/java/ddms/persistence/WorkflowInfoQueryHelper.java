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
import ddms.util.JsonUtil;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

/**
 *
 * @author zlhso
 */
@Component
@AllArgsConstructor
class WorkflowInfoQueryHelper {

    private final WorkflowDataRepo assetRepo;
    private final WorkflowEvtLogRepo assetEvtRepo;
    private final WorkflowMngmntProcessRepo procRepo;

    private static final String EVTS = "events";
    //private static final String EVTID = "evtId";
    private static final String REFID = "refId";
    private static final String PROCESSID = "processId";
    private static final String PENDINGPROCESSES = "pendingProcesses";
    private static final String PROCESSNAME = "processName";
    //private static final String RECDATE = "recordedDate";
    private static final String ACIONPARAMS = "actionParams";
    private static final String LSTACIONPARAMS = "lastActionParams";
    private static final String TASKS = "tasks";
    private static final String NEXTTASKS = "nextTasks";
    
    private static final String DATACONTENT = "dataContent";

    public Map<String, Object> fetchAssetEvtDetails(String evtId) {
        WorkflowEventLog e = assetEvtRepo.findByEvtId(evtId);
        final SimpleDateFormat format = DateUtil.createDefaultDateFormatter();
        if (e != null) {
            return convertAssetEvtToMap(e, format);
        }
        return new HashMap<>();
    }

    public Map<String, Object> fetchAssetDetails(String assetid) {
        WorkflowData asset = assetRepo.findByRefId(assetid);
        if (asset != null) {
            try {
                final SimpleDateFormat format = DateUtil.createDefaultDateFormatter();
               
                Map<String, Object> assetMap = convertAssetToMap(asset, format);

                List<WorkflowMngmntProcess> tempProcList = procRepo.findByProcessPk_refId(assetid);
                if (tempProcList != null && !tempProcList.isEmpty()) {
                    assetMap.put(PENDINGPROCESSES, tempProcList.stream().map(e -> {                        
                        return convertAssetMngmntProcessToMap(e);
                    }).collect(Collectors.toList()));
                }

                List<WorkflowEventLog> tempEvtList = assetEvtRepo.findByRefId(assetid);
                if (tempEvtList != null && !tempEvtList.isEmpty()) {
                    List<WorkflowEventLog> evts = new ArrayList<>();
                    evts.addAll(tempEvtList);
                    Collections.sort(evts, new Comparator<WorkflowEventLog>() {
                        @Override
                        public int compare(WorkflowEventLog arg0, WorkflowEventLog arg1) {
                            if (arg0 != null && arg1 != null
                                    && arg0.getRecordedDate() != null && arg1.getRecordedDate() != null) {
                                return arg1.getRecordedDate().compareTo(arg0.getRecordedDate());
                            }
                            return 0;
                        }
                    });
                    assetMap.put(EVTS, evts.stream().map(e -> {                        
                        return convertAssetEvtToMap(e, format);
                    }).collect(Collectors.toList()));
                } else {
                    assetMap.put(EVTS, new ArrayList<>());
                }
                return assetMap;
            } catch (Exception ex) {
                throw new UnsupportedOperationException(ex);
            }
            
        }
        return new HashMap<>();
    }
    
    public List<Map<String,Object>> fetchAssetMngmntProcessesByProcessId(String procId){
        List<WorkflowMngmntProcess> procList=procRepo.findByProcessPk_processId(procId);
        return procList.stream().map(e -> convertAssetMngmntProcessToMap(e)).collect(Collectors.toList());
    }

    public Map<String, Object> convertAssetMngmntProcessToMap(WorkflowMngmntProcess e) {
        String name = e.getProcessName();
        String processId = e.getProcessPk().getProcessId();
        String assetId = e.getProcessPk().getRefId();
        Map<String, Object> m = new TreeMap<>();
        m.put(REFID, assetId);
        m.put(PROCESSNAME, name);
        m.put(PROCESSID, processId);
        String tasksString=e.getTasks();
        if(tasksString!=null){
            m.put(TASKS, JsonUtil.fromJsonString(tasksString, List.class));
        }
        return m;
    }
    
    public Map<String, Object> convertAssetEvtToMap(WorkflowEventLog e,SimpleDateFormat format) {
        try{
            Map<String,Class> jsonFieldNames=new HashMap<>();
            jsonFieldNames.put(DATACONTENT,Map.class);
            jsonFieldNames.put(ACIONPARAMS,Map.class);
            jsonFieldNames.put(NEXTTASKS,List.class);
            return convert(WorkflowEventLog.class.getDeclaredFields(), e, format, jsonFieldNames);
        }catch(Exception ex){
            throw new RuntimeException(ex);
        }
        
    }
    
    public Map<String, Object> convertAssetToMap(WorkflowData e,SimpleDateFormat format) {
        try{
            Map<String,Class> jsonFieldNames=new HashMap<>();
            jsonFieldNames.put(DATACONTENT,Map.class);
            jsonFieldNames.put(LSTACIONPARAMS,Map.class);
            return convert(WorkflowData.class.getDeclaredFields(), e, format, jsonFieldNames);
        }catch(Exception ex){
            throw new RuntimeException(ex);
        }
        
    }

    public Map<String, Object> convert(Field[] fields, Object data, SimpleDateFormat format, 
            Map<String,Class> jsonFieldNames) throws IllegalAccessException {
        Map<String, Object> map = new TreeMap<>();
        for (Field f : fields) {

            String fName = f.getName();
            map.put(fName, "");
            ReflectionUtils.makeAccessible(f);
            Object val = f.get(data);

            if (val instanceof Date) {
                map.put(fName, format.format((Date) val));
            } else if (val instanceof String) {
                map.put(fName, convertStringOrTyp(fName, jsonFieldNames, val));
            } else {
                map.put(fName, val);
            }
        }

        return map;
    }

    
    
    public Object convertStringOrTyp(String fieldName,
            Map<String,Class> jsonFieldNames,
            Object val) {
        if (val != null
                && jsonFieldNames != null
                && jsonFieldNames.keySet().contains(fieldName)) {
            return JsonUtil.fromJsonString(String.valueOf(val), jsonFieldNames.get(fieldName));
        }
        return val != null ? val : "";
    }
}
