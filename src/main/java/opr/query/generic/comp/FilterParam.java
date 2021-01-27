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
public class FilterParam {

    private int order;
    private String key;
    private String filterType;
    private String filterExpression;
    private TextValue textValue;
    private TextArrayValue textArrayValue;
    private DateValue dateValue;
    private DateRangeValue dateRangeValue;
    private NumberValue numberValue;

    
}
