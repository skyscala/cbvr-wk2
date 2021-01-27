/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ddms.persistence.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 *
 * @author zlhso
 */

@Entity
@Table(name = "asset_mangement_process")
@Data
public class WorkflowMngmntProcess implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @EmbeddedId
    private WorkflowMngmntProcessPk processPk;
    
    @Size(max = 255)
    @Column(name = "name")
    private String processName;
    
    @Size(max = 500)
    @Column(name = "tasks")
    private String tasks;
    
    
    
}
