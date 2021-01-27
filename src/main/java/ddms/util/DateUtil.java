/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ddms.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author zeyarlinhtike
 */
public class DateUtil {

    private static final String FORMAT_PATTERN="yyyy-MM-dd HH:mm:ss";
    private DateUtil(){}
 
    public static final SimpleDateFormat createDefaultDateFormatter(){
        String datePattern = dateFormatterPattern();
        return new SimpleDateFormat(datePattern);

    }
    
    public static final String dateFormatterPattern(){
        return FORMAT_PATTERN;
        
    }
    
    
    public static final Date now(){
        return Calendar.getInstance().getTime();
    }
    
    
}
