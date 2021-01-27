/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ddms.opr.v1;

import java.util.Map;
import opr.query.generic.comp.QueryParam;
import opr.query.generic.comp.QueryResultPage;

/**
 *
 * @author zlhso
 */
public interface QueryWorkflowEventLogs {
    QueryResultPage<Map<String,Object>> queryAssetEvts(QueryParam queryParam);
    
    Map<String,Object> fetchAssetEvtDetails(String evtId);
}
