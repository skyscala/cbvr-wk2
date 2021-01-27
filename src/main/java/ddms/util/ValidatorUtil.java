/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ddms.util;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 *
 * @author zlhso
 * @param <T>
 */
public class ValidatorUtil {
    
    private ValidatorUtil(){}

    public static void validate(Object t){
        ValidatorFactory factory= Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Object>> violations = validator.validate(t);
        if(!violations.isEmpty()){
            throw new ConstraintViolationException(violations);
        }
    }
}
