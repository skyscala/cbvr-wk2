/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ddms.util;

import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author zeyarhtike
 */
public class ProcessResultFactory {
    
    public static final String STATUS="status";
    public static final String ERROR_DESC="error_description";
    public static final String RES_FAIL="FAIL";
    public static final String RES_ERROR_DESC="Error occurred while processing request.";

    private ProcessResultFactory(){}

    public static ProcessResult createSuccessResult(Object data, String message) {
        ProcessResult msg=new ProcessResult();
        msg.setStatus(ProcessResult.STATUS_SUCCESS);
        msg.setData(data);
        msg.setMessage(message);
        return msg;
    }

    public static ProcessResult createSuccessResult(Object data) {
        ProcessResult msg=new ProcessResult();
        msg.setStatus(ProcessResult.STATUS_SUCCESS);
        msg.setData(data);
        return msg;
    }

    public static ProcessResult createFailResult(String message) {
        ProcessResult msg=new ProcessResult();
        msg.setStatus(ProcessResult.STATUS_FAIL);
        msg.setMessage(message);
        return msg;
    }

    public static ProcessResult createFailResult(Object error, String message) {
        ProcessResult msg=new ProcessResult();
        msg.setStatus(ProcessResult.STATUS_FAIL);
        msg.setError(error);
        msg.setMessage(message);
        return msg;
    }
    
    public static Map<String, Object> createFailResult() {
        Map<String, Object> map = new TreeMap<>();
        map.put(STATUS, RES_FAIL);
        map.put(ERROR_DESC, RES_ERROR_DESC);
        return map;
    }
}
