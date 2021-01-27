/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ddms.persistence;

import ddms.persistence.entity.WorkflowMngmntProcess;
import ddms.persistence.entity.WorkflowMngmntProcessPk;
import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author zlhso
 */
@Repository
interface WorkflowMngmntProcessRepo extends PagingAndSortingRepository<WorkflowMngmntProcess, WorkflowMngmntProcessPk>{

    WorkflowMngmntProcess findByProcessPk(WorkflowMngmntProcessPk pk);
    
    List<WorkflowMngmntProcess> findByProcessPk_refId(String refId);
    
    List<WorkflowMngmntProcess> findByProcessPk_processId(String processId);
}
