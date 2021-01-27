/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ddms.opr.v1;

import java.util.List;
import java.util.Map;
import opr.query.generic.comp.QueryParam;
import opr.query.generic.comp.QueryResultPage;

/**
 *
 * @author zlhso
 */
public interface QueryWorkflowManagementProcesses {
    
    QueryResultPage<Map<String,Object>> queryAssetMngmntProcesses(QueryParam queryParam);
    
    
    List<Map<String,Object>> fetchAssetMngmntProcessByProcessId(String procId);
}
