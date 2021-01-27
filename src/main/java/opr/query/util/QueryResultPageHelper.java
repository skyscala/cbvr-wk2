/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opr.query.util;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.EntityPathBase;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import opr.query.generic.comp.Filter;
import opr.query.generic.comp.PaginationParam;
import opr.query.generic.comp.QueryParam;
import opr.query.generic.comp.QueryResultPage;
import opr.query.generic.comp.SortParam;



/**
 *
 * @author zlhso
 */
public class QueryResultPageHelper {
    


    private QueryResultPageHelper(){}

    public static <Q,I,O> QueryResultPage<O> query(QueryParam queryParam,
            EntityPathBase<Q> epb,EntityManager em,ResultItemConverter<I,O> converter) {
        
        PaginationParam paginationParam=queryParam.getPaginationParam();
        int pageNumber=paginationParam.getPageNumber();        
        int pageSize=paginationParam.getPageSize();
        
        if(pageNumber<1){
            throw new IllegalArgumentException("Invalid input. Page Number less than 1 is not allowed.");
        }
        
        if(pageSize<1){
            throw new IllegalArgumentException("Invalid input. Minimum Page Size must be 1.");
        }
        
        if(pageSize>1000){
            throw new IllegalArgumentException("Invalid input. Maximum page size allow is 1000.");
        }
        
        List<SortParam> sortParams=queryParam.getSortingParams();
        
        if(sortParams==null){
            throw new IllegalArgumentException("No sorting param found.");
        }
        
        Predicate predicate=null;        
        Filter filter=queryParam.getFilter();        
        if(filter!=null){
            if("or".equals(filter.getLogic())){
                predicate=BooleanExpressionHelper.processExpOr(FilterFactory.from(filter), epb);
            }else{
                predicate=BooleanExpressionHelper.processExpAnd(FilterFactory.from(filter), epb);
            }
        }        
        JPAQueryFactory factory = new JPAQueryFactory(em);
        
        int offset=(pageNumber-1)*pageSize;

        OrderSpecifier[] orders=OrderSpecifierFactory.
                createOrderSpecifiers(SortFactory.from(sortParams), epb);
        
        


        JPAQuery<Q> query=factory.selectFrom(epb)
                .offset(offset).limit(pageSize)
                .orderBy(orders);


        if(predicate!=null){            
            query.where(predicate);
        }


        JPAQuery<I> qb= (JPAQuery) query.fetchAll();
        long fetchCount=qb.fetchCount();
        QueryResults<I> results=qb.fetchResults();
        List<I> queryItems=results.getResults();

        List<O> items=new ArrayList<>();
        
        for(I itm:queryItems){
            items.add(converter.from(itm));
        }
        
        QueryResultPage<O> resultPage=new QueryResultPage<>();
        resultPage.setPageNumber(pageNumber);
        resultPage.setPageSize(pageSize);        
        resultPage.setItems(items);
        resultPage.setNumberOfElements(items.size());
        resultPage.setTotalElements(fetchCount);
        int pageCount=(int)(fetchCount/pageSize);
        int sub=(int)(fetchCount%pageSize);
        if(sub>0){
            pageCount++;
        }
        resultPage.setTotalPages(pageCount);
        return resultPage;
        
    }
}
