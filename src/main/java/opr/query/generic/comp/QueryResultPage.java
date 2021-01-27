/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opr.query.generic.comp;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author zeyarh
 */

@Getter
@Setter
public class QueryResultPage<E> {
    
    private int pageNumber;    
    private int pageSize;
    private int totalPages;        
    private long numberOfElements;
    private long totalElements;
    private List<E> items;
    

    
    
    
    
}
