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
public interface QueryWorkflows {
    
    QueryResultPage<Map<String,Object>> queryAssets(QueryParam queryParam);
    
    Map<String,Object> fetchAssetDetails(String assetid);
}
