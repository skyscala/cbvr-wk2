/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ddms.persistence.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 *
 * @author zlhso
 */

@Entity
@Table(name = "asset_event")
@Data
public class WorkflowEventLog implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 225)
    @Column(name = "evt_id")
    private String evtId;
   
    @Size(max = 255)
    @Column(name = "process_id")
    private String processId;
    
    @NotNull
    @Size(max = 225)
    @Column(name = "ref_id")
    private String refId;
    
    @Size(max = 255)
    @Column(name = "completed_task_id")
    private String completedTaskId;
    
    @Size(max = 500000)
    @Column(name = "actionParams")
    private String actionParams;
    
    @Size(max = 500)
    @Column(name = "next_tasks")
    private String nextTasks;
    
    @Size(max = 500000)
    @Column(name = "data_content")
    private String dataContent;  
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "recorded_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date recordedDate;
    
    
}
