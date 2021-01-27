/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ddms.util;

import lombok.Data;

/**
 *
 * @author zeyarhtike
 */
@Data
public class ProcessResult {
 
    public static final String STATUS_SUCCESS="SUCCESS";
    public static final String STATUS_FAIL="FAIL";
    
    private String status;
    private Object data;
    private Object error;
    private String message;
}
