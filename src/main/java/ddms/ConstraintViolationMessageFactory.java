/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ddms;

import java.util.Map;
import java.util.TreeMap;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 *
 * @author zeyarhtike
 */
public class ConstraintViolationMessageFactory {

    private ConstraintViolationMessageFactory(){}

    public static Map<String,String> create(ConstraintViolationException ce) {

        Map<String,String> constraints = new TreeMap<>();
        for (ConstraintViolation<?> c : ce.getConstraintViolations()) {
            constraints.put(c.getPropertyPath().toString(), c.getMessage());            
        }
        return constraints;
    }
}
