/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opr.query.util;

import ddms.util.DateUtil;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import opr.query.functional.comp.DateFilterCriteria;
import opr.query.functional.comp.DateRangeFilterCriteria;
import opr.query.functional.comp.FilterCriteria;
import opr.query.functional.comp.ListOfStringsFilterCriteria;
import opr.query.functional.comp.NumberFilterCriteria;
import opr.query.functional.comp.StringFilterCriteria;
import opr.query.generic.comp.DateRangeValue;
import opr.query.generic.comp.Filter;
import opr.query.generic.comp.FilterParam;
import opr.query.generic.comp.NumberValue;
import opr.query.generic.comp.TextArrayValue;
import opr.query.generic.comp.TextValue;


/**
 *
 * @author zlhso
 */
public class FilterFactory {

    private FilterFactory(){}

    public static FilterCriteria[] from(Filter filter){
        List<FilterParam> list=filter.getFilterParams();
        List<FilterCriteria> list2=new ArrayList<>();
        for(FilterParam param:list){
            list2.add(from(param));
        }
        FilterCriteria[] array=new FilterCriteria[list2.size()];
        return list2.toArray(array);
    }
    
    
    public static FilterCriteria from(FilterParam filterParam){
        
        String filterKey=filterParam.getKey();
        String filterType=filterParam.getFilterType();
        String expression=filterParam.getFilterExpression();
        if(filterType.equalsIgnoreCase(PredicateFactory.DATE_FILTER)){
            try{
                DateFilterCriteria dfc=new DateFilterCriteria();
                dfc.setKey(filterKey);
                dfc.setOperation(expression);                
                dfc.setDate(convertToDate(filterParam.getDateValue().getDate()));
                return dfc;
            }catch(ParseException pe){
                throw new IllegalArgumentException("Cannot translate input date. Date pattern must be "+DateUtil.dateFormatterPattern()+".");
            }
        }else if(filterType.equalsIgnoreCase(PredicateFactory.DATE_RANGE_FILTER)){
            try{
                DateRangeValue dateRange=filterParam.getDateRangeValue();
                Date start=convertToDate(dateRange.getStart());
                Date end=convertToDate(dateRange.getEnd());
                DateRangeFilterCriteria drfc=new DateRangeFilterCriteria();
                drfc.setStart(start);
                drfc.setEnd(end);
                drfc.setKey(filterKey);
                drfc.setOperation(expression);  
                return drfc;
            }catch(ParseException pe){
                throw new IllegalArgumentException("Cannot translate input date. Date pattern must be "+DateUtil.dateFormatterPattern()+".");
            }
        }else if(filterType.equalsIgnoreCase(PredicateFactory.TEXT_FILTER)){
            TextValue textValue=filterParam.getTextValue();
            StringFilterCriteria sfc=new StringFilterCriteria();
            sfc.setKey(filterKey);
            sfc.setOperation(expression);  
            sfc.setValue(textValue.getValue());
            return sfc;
        }else if(filterType.equalsIgnoreCase(PredicateFactory.TEXT_ARRAY_FILTER)){
            TextArrayValue textArrayValue=filterParam.getTextArrayValue();
            ListOfStringsFilterCriteria lstsfc=new ListOfStringsFilterCriteria();
            lstsfc.setKey(filterKey);
            lstsfc.setOperation(expression);
            lstsfc.setValues(textArrayValue.getList());
            return lstsfc;
        }else if(filterType.equalsIgnoreCase(PredicateFactory.NUMBER_FILTER)){
            NumberValue numValue=filterParam.getNumberValue();
            NumberFilterCriteria sfc=new NumberFilterCriteria();
            sfc.setKey(filterKey);
            sfc.setOperation(expression);  
            sfc.setValue(numValue.getValue());
            return sfc;
        }
        throw new IllegalArgumentException("Invalid filter type "+filterType+".");
    }
    
    private static Date convertToDate(String dateString)throws ParseException{
        SimpleDateFormat format=DateUtil.createDefaultDateFormatter();
        return format.parse(dateString);
    }
}
