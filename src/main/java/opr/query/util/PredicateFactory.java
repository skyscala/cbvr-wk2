/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opr.query.util;


import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.StringPath;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import opr.query.functional.comp.DateFilterCriteria;
import opr.query.functional.comp.DateRangeFilterCriteria;
import opr.query.functional.comp.FilterCriteria;
import opr.query.functional.comp.ListOfStringsFilterCriteria;
import opr.query.functional.comp.NumberFilterCriteria;
import opr.query.functional.comp.StringFilterCriteria;


/**
 *
 * @author zeyarh
 */
public class PredicateFactory {
    
    public static final String TEXT_FILTER="text";    
    public static final String TEXT_ARRAY_FILTER="textArray";
    public static final String DATE_RANGE_FILTER="dateRange";
    public static final String DATE_FILTER="date";
    public static final String NUMBER_FILTER="number";
    

    private PredicateFactory(){}
    
    
    
    public static BooleanExpression createBooleanExpression(FilterCriteria criteria, Object q){
        
        if(criteria==null||criteria.getKey()==null||criteria.getOperation()==null){
            throw new IllegalArgumentException("Invalid filter object!");
        }
        
        if(criteria instanceof StringFilterCriteria){
            return createBooleanExpressionForString((StringFilterCriteria)criteria,q);
        }
        
        if(criteria instanceof ListOfStringsFilterCriteria){
            return createBooleanExpressionForListOfStrings((ListOfStringsFilterCriteria)criteria,q);
        }
        
        if(criteria instanceof DateFilterCriteria){
            return createBooleanExpressionForDate((DateFilterCriteria)criteria,q);
        }
        
        if(criteria instanceof DateRangeFilterCriteria){
            return createBooleanExpressionForDateRange((DateRangeFilterCriteria)criteria,q);
        }
        
        if(criteria instanceof NumberFilterCriteria){
            return createBooleanExpressionForNumber((NumberFilterCriteria)criteria,q);
        }
        
        return null;
        
    }
    
    public static Map<String,List<String>> expressionMap(){
        Map<String,List<String>> map = new TreeMap<>();
        String[] textFilterExpressions={
            "startsWithIgnoreCase","endsWithIgnoreCase","containsIgnoreCase",
            "equalsIgnoreCase","startsWith","endsWith","contains","equals","ne"
        };
        map.put(TEXT_FILTER, Arrays.asList(textFilterExpressions));
        
        String[] textArrayFilterExpressions = {
            "in","notIn"
        };
        
        map.put(TEXT_ARRAY_FILTER, Arrays.asList(textArrayFilterExpressions));
       
        String[] dateRangeFilterExpressions = {
            "between","notBetween"
        };
        map.put(DATE_RANGE_FILTER, Arrays.asList(dateRangeFilterExpressions));
        
        String[] dateExpressions = {
            "before","after"  
        };
        map.put(DATE_FILTER, Arrays.asList(dateExpressions));
        
        String[] numberExpressions = {
            "eq","gt","lt","goe","loe"  
        };
        map.put(DATE_FILTER, Arrays.asList(numberExpressions));
        
        return map;
    }
    
        
    public static BooleanExpression createBooleanExpressionForString(StringFilterCriteria criteria,Object q){
        String key = criteria.getKey();
        StringPath path = PathFactory.retireveStringPath(key,q);
        String operation = criteria.getOperation();        
        String value=criteria.getValue();
        return createBooleanExpressionForString(path, operation,value);
    }
    
    public static BooleanExpression createBooleanExpressionForString(StringPath path,String operation,String value){
        
        if("startsWithIgnoreCase".equalsIgnoreCase(operation)){
            return path.startsWithIgnoreCase(value);
        }else if("endsWithIgnoreCase".equalsIgnoreCase(operation)){
            return path.endsWithIgnoreCase(value);
        }else if("containsIgnoreCase".equalsIgnoreCase(operation)){
            return path.containsIgnoreCase(value);
        }else if("equalsIgnoreCase".equalsIgnoreCase(operation)){
            return path.equalsIgnoreCase(value);
        }else if("startsWith".equalsIgnoreCase(operation)){
            return path.startsWith(value);
        }else if("endsWith".equalsIgnoreCase(operation)){
            return path.endsWith(value);
        }else if("contains".equalsIgnoreCase(operation)){
            return path.contains(value);
        }else if("equals".equalsIgnoreCase(operation)){
            return path.matches(value);
        }else if("ne".equalsIgnoreCase(operation)){
            if(value==null){
                return path.isNotNull();
            }
            return path.ne(value);
        }else{
            throw new IllegalArgumentException("Invalid operator for text filter.");
        }
        
    }
    
    public static BooleanExpression createBooleanExpressionForListOfStrings(ListOfStringsFilterCriteria criteria,Object q){
        String key = criteria.getKey();
        StringPath path = PathFactory.retireveStringPath(key,q);
        String operation = criteria.getOperation();        
        List<String> value=criteria.getValues();
        return createBooleanExpressionForListOfStrings(path, operation,value);
    }
    
    public static BooleanExpression createBooleanExpressionForListOfStrings(StringPath path,String operation,List<String> values){
              
        if(!values.isEmpty()){
            if("in".equalsIgnoreCase(operation)){
                if(values.size()==1){
                    values.add("");
                }
                return path.in(values);
            }else if("notIn".equalsIgnoreCase(operation)){
                if(values.size()==1){
                    values.add("");
                }
                return path.notIn(values);
            }else{
                throw new IllegalArgumentException("Invalid operator for text array filter");
            }
        }else{
            throw new IllegalArgumentException("Empty text array is not allowed.");
        }
        
    }
    
    public static BooleanExpression createBooleanExpressionForDate(DateFilterCriteria criteria,Object q){
        String key = criteria.getKey();
        String operation = criteria.getOperation();
        DateTimePath<Date> path = PathFactory.retireveDatePath(key,q);
        Date date = criteria.getDate();
        
        return createBooleanExpressionForDate(path, operation, date);
    }
    
    public static BooleanExpression createBooleanExpressionForDate(DateTimePath<Date> path,String operation,Date date){

        if(date !=null){
            if("before".equalsIgnoreCase(operation)){
                return path.before(date);
            }else if("after".equalsIgnoreCase(operation)){
                return path.after(date);
            }else {
                throw new IllegalArgumentException("Invalid operator for date filter.");
            }
        }
        
        throw new IllegalArgumentException("Invalid date.");
    }
    
    
    public static BooleanExpression createBooleanExpressionForDateRange(DateRangeFilterCriteria criteria,Object q){
        String key = criteria.getKey();
        String operation = criteria.getOperation();
        DateTimePath<Date> path = PathFactory.retireveDatePath(key,q);
        
        Date start = criteria.getStart();
        Date end = criteria.getEnd();
        
        return createBooleanExpressionForDateRange(path, operation, start, end);
    }
    
    public static BooleanExpression createBooleanExpressionForDateRange(DateTimePath<Date> path,String operation,Date start,Date end){
       
        if(start !=null && end !=null){
            if("between".equalsIgnoreCase(operation)){
                return path.between(start, end);
            }else if("notBetween".equalsIgnoreCase(operation)){
                return path.between(start, end).not();
            }else{
                throw new IllegalArgumentException("Invalid operator for date range filter.");
            }
        }
        
        
        throw new IllegalArgumentException("Invalid start date or end date");
    }    
    
    
    public static BooleanExpression createBooleanExpressionForNumber(NumberFilterCriteria criteria,Object q){
        String key = criteria.getKey();
        String operation = criteria.getOperation();
        NumberPath<Long> path = PathFactory.retireveNumberPath(key,q);
        Long value = criteria.getValue();        
        return createBooleanExpressionForNumber(path, operation, value);
    }
    
    public static BooleanExpression createBooleanExpressionForNumber(NumberPath<Long> path,String operation,Long value){

        if(value !=null){
            if("eq".equalsIgnoreCase(operation)){
                return path.eq(value);
            }else if("gt".equalsIgnoreCase(operation)){
                return path.gt(value);
            }else if("lt".equalsIgnoreCase(operation)){
                return path.lt(value);
            }else if("goe".equalsIgnoreCase(operation)){
                return path.goe(value);
            }else if("loe".equalsIgnoreCase(operation)){
                return path.loe(value);
            }else {
                throw new IllegalArgumentException("Invalid operator for number filter.");
            }
        }
        
        throw new IllegalArgumentException("Invalid number value.");
    }
    
    protected static <E extends Number & Comparable<Number>> NumberPath<E> defineNumberPath(String key,Class<E> cls,  PathBuilder<?> entityPath){
        return entityPath.getNumber(key, cls);
    }
    
    protected static boolean isDate(Object object){
        return object instanceof Date;
    }
    
    
}
