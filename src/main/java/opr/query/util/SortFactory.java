/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opr.query.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import opr.query.functional.comp.SortCriteria;
import opr.query.generic.comp.SortParam;

/**
 *
 * @author zlhso
 */
public class SortFactory {

    private SortFactory(){}
    
    public static SortCriteria[] from(List<SortParam> inputs) {
        List<SortParam> sortParams = new ArrayList<>();
        sortParams.addAll(inputs);
        List<SortCriteria> list = new ArrayList<>();
        Collections.sort(sortParams, (arg0, arg1) -> Integer.valueOf(arg0.getOrder()).compareTo(arg1.getOrder()));
        SortCriteria[] array = new SortCriteria[list.size()];
        for (SortParam param : sortParams) {
            list.add(from(param));
        }
        return list.toArray(array);
    }

    public static SortCriteria from(SortParam sortParam) {
        SortCriteria sc = new SortCriteria();
        sc.setKey(sortParam.getKey());
        sc.setType(sortParam.getSortType());
        return sc;
    }
}
