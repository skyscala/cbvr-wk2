/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opr.query.util;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;

import java.util.ArrayList;
import java.util.List;
import opr.query.functional.comp.SortCriteria;

/**
 *
 * @author zlhso
 */
public class OrderSpecifierFactory {
    
    
    private OrderSpecifierFactory(){}
    
    public static OrderSpecifier[] createOrderSpecifiers( SortCriteria[] sortCriteriaList,Object clz) {
        List<OrderSpecifier<?>> orderSpecs = new ArrayList<>();
        for (SortCriteria sortCriteria : sortCriteriaList) {
            String prop = sortCriteria.getKey();
            if (prop == null) {
                continue;
            }
            String sortType = sortCriteria.getType();
            if (sortType != null && "ASC".equalsIgnoreCase(sortType)) {
                OrderSpecifier<?> spec = new OrderSpecifier<>(Order.ASC, PathFactory.retrievePath(prop, clz));
                orderSpecs.add(spec);
            } else {
                OrderSpecifier<?> spec = new OrderSpecifier<>(Order.DESC, PathFactory.retrievePath(prop, clz));
                orderSpecs.add(spec);
            }

            if (!orderSpecs.isEmpty()) {
                OrderSpecifier<?>[] orderArray = new OrderSpecifier<?>[orderSpecs.size()];
                return orderSpecs.toArray(orderArray);
                
            }
        }
        return new OrderSpecifier[]{};
    }
}
