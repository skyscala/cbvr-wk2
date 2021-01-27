/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ddms.persistence;

import ddms.persistence.entity.WorkflowEventLog;
import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author zlhso
 */
@Repository
interface WorkflowEvtLogRepo extends PagingAndSortingRepository<WorkflowEventLog, String>{

    WorkflowEventLog findByEvtId(String evtId);
    List<WorkflowEventLog> findByRefId(String refId);
}
