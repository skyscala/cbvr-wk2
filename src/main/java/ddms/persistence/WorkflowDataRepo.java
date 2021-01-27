/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ddms.persistence;

import ddms.persistence.entity.WorkflowData;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author zlhso
 */

@Repository
interface WorkflowDataRepo extends PagingAndSortingRepository<WorkflowData, String>{

    WorkflowData findByRefId(String refId);
    
}
