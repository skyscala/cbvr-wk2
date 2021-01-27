/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opr.query.util;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QSort;
import opr.query.functional.comp.SortCriteria;


/**
 *
 * @author zeyarh
 */
public class PageableFactory {


    private PageableFactory(){}

    public static <E> QSort createPageRequest(Class<E> clz, SortCriteria[] sortCriteriaList) {
        

        PathBuilder<E> entityPath = new PathBuilder<>(clz, clz.getSimpleName());

        List<OrderSpecifier<?>> orderSpecs = new ArrayList<>();
        for (SortCriteria sortCriteria : sortCriteriaList) {
            String prop = sortCriteria.getKey();
            if (prop == null) {
                continue;
            }
            String sortType = sortCriteria.getType();
            if (sortType != null && "ASC".equalsIgnoreCase(sortType)) {
                OrderSpecifier<?> spec = new OrderSpecifier<>(Order.ASC, entityPath.getString(prop));
                orderSpecs.add(spec);
            } else {
                OrderSpecifier<?> spec = new OrderSpecifier<>(Order.DESC, entityPath.getString(prop));
                orderSpecs.add(spec);
            }

            if (!orderSpecs.isEmpty()) {
                OrderSpecifier<?>[] orderArray = new OrderSpecifier<?>[orderSpecs.size()];
                orderSpecs.toArray(orderArray);
                return new QSort(orderArray);

            }
        }
        return null;
    }
    
    
    public static <E> Pageable createPageRequest(int page, int size, Class<E> clz, SortCriteria[] sortCriteriaList) {
        Pageable pageable = null;

        PathBuilder<E> entityPath = new PathBuilder<>(clz, clz.getSimpleName());

        List<OrderSpecifier<?>> orderSpecs = new ArrayList<>();
        for (SortCriteria sortCriteria : sortCriteriaList) {
            String prop = sortCriteria.getKey();
            if (prop == null) {
                continue;
            }
            String sortType = sortCriteria.getType();
            if (sortType != null && "ASC".equalsIgnoreCase(sortType)) {
                OrderSpecifier<?> spec = new OrderSpecifier<>(Order.ASC, entityPath.getString(prop));
                orderSpecs.add(spec);
            } else {
                OrderSpecifier<?> spec = new OrderSpecifier<>(Order.DESC, entityPath.getString(prop));
                orderSpecs.add(spec);
            }

            if (!orderSpecs.isEmpty()) {
                OrderSpecifier<?>[] orderArray = new OrderSpecifier<?>[orderSpecs.size()];
                orderSpecs.toArray(orderArray);
                QSort qSort = new QSort(orderArray);
                pageable = PageRequest.of(page, size, qSort);
            }
        }
        return pageable;
    }

    public static <E> Pageable createPageRequest(int page, int size,Class<E> clz, String sortKey, String sortType) {
        
        PathBuilder<E> entityPath = new PathBuilder<>(clz, clz.getSimpleName());

        if(sortType!=null && "ASC".equals(sortType)){
            return PageRequest.of(page, size, 
                    new QSort(new OrderSpecifier<>(Order.ASC, entityPath.getString(sortKey))));
        }else{
            return PageRequest.of(page, size, 
                    new QSort(new OrderSpecifier<>(Order.DESC, entityPath.getString(sortKey))));
        }


    }
}
