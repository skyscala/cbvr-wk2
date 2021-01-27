/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opr.query.util;

/**
 *
 * @author zlhso
 */
public interface ResultItemConverter<I,O> {
    
    public O from(I i);
}
