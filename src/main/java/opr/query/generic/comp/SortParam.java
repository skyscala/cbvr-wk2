/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opr.query.generic.comp;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author zeyarh
 */

@Getter
@Setter
public class SortParam {
 
    public static final String DESC="DESC";
    public static final String ASC="ASC";
    
    private int order;
    private String key;
    private String sortType;

    
}
