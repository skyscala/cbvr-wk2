/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opr.query.util;

import com.querydsl.core.types.dsl.BooleanExpression;
import opr.query.functional.comp.FilterCriteria;



/**
 *
 * @author zeyarhtike
 */
public class BooleanExpressionHelper {
    
    private BooleanExpressionHelper(){}
    
    public static BooleanExpression processExpAnd(FilterCriteria[] filterArray,Object clz){
        BooleanExpression exp=null;        
        if(filterArray!=null&&filterArray.length>0){
            exp = PredicateFactory.createBooleanExpression(filterArray[0], clz);
            for(int i=1;i<filterArray.length;i++){
                BooleanExpression b = PredicateFactory.createBooleanExpression(filterArray[i], clz);
                exp = exp.and(b);
            }
            
        }        
        return exp;
    }
    
    public static BooleanExpression processExpOr(FilterCriteria[] filterArray,Object clz){
        BooleanExpression exp=null;
        
        if(filterArray!=null&&filterArray.length>0){
            exp = PredicateFactory.createBooleanExpression(filterArray[0], clz);
            for(int i=1;i<filterArray.length;i++){
                BooleanExpression b = PredicateFactory.createBooleanExpression(filterArray[i], clz);
                exp = exp.or(b);
            }
            
        }
        
        return exp;
    }
    
}
