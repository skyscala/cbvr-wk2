/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opr.query.util;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import java.lang.reflect.Field;
import java.util.Date;

import lombok.extern.log4j.Log4j2;
import org.springframework.util.ReflectionUtils;


/**
 *
 * @author zlhso
 */

@Log4j2
public class PathFactory {
    
    private static final String TAG=PathFactory.class.getName();
    private static final String INVALID_VAL_TYPE_ERR="Invalid value type: ";
    private PathFactory(){}
    
    public static <E> Path<E> retrievePath(String prop,Object q){
        Field[] fields=q.getClass().getDeclaredFields();
        for(Field f:fields){
            try{

                ReflectionUtils.makeAccessible(f);
                String name=f.getName();
                if(prop.equals(name)){
                    Object obj=f.get(q);
                    if(obj instanceof Path){
                        return (Path)obj;
                    }else{
                        throw new IllegalArgumentException(INVALID_VAL_TYPE_ERR+prop);
                    }
                }
            }catch(Exception ex){
                log.debug(TAG, ex);
            }
        }
        
        
        return null;
    }
    
    public static StringPath retireveStringPath(String prop,Object q){
        
        
        Field[] fields=q.getClass().getDeclaredFields();
        for(Field f:fields){
            try{

                ReflectionUtils.makeAccessible(f);
                String name=f.getName();
                if(prop.equals(name)){
                    Object obj=f.get(q);
                    if(obj instanceof StringPath){
                        return (StringPath)obj;
                    }else{
                        throw new IllegalArgumentException(INVALID_VAL_TYPE_ERR+prop);
                    }
                }
            }catch(Exception ex){
                log.debug(TAG, ex);
            }
        }
        
        
        return null;
    }
    
    public static <E extends Date> DateTimePath<E> retireveDatePath(String prop, Object q){
        
        
        Field[] fields=q.getClass().getDeclaredFields();
        for(Field f:fields){
            try{

                ReflectionUtils.makeAccessible(f);
                String name=f.getName();
                if(prop.equals(name)){
                    Object obj=f.get(q);
                    if(obj instanceof DateTimePath){
                        return (DateTimePath)obj;
                    }else{
                        throw new IllegalArgumentException(INVALID_VAL_TYPE_ERR+prop);
                    }
                }
            }catch(Exception ex){
                log.debug(TAG, ex);
            }
        }
        
        
        return null;
    }
    
    public static <E extends Long> NumberPath<E> retireveNumberPath(String prop,Object q){
        
        
        Field[] fields=q.getClass().getDeclaredFields();
        for(Field f:fields){
            try{

                ReflectionUtils.makeAccessible(f);
                String name=f.getName();
                if(prop.equals(name)){
                    Object obj=f.get(q);
                    if(obj instanceof NumberPath){
                        return (NumberPath)obj;
                    }else{
                        throw new IllegalArgumentException(INVALID_VAL_TYPE_ERR+prop);
                    }
                }
            }catch(Exception ex){
                log.debug(TAG, ex);
            }
        }
        
        
        return null;
    }
}
