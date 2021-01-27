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
public class QueryParam {

    
    private PaginationParam paginationParam;
    private Filter filter;
    private List<SortParam> sortingParams;
    
    
}
