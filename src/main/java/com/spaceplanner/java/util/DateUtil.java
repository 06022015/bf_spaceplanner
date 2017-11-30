package com.spaceplanner.java.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 1/31/16
 * Time: 11:19 AM
 * To change this template use File | Settings | File Templates.
 */
public class DateUtil {

    private static Logger logger = LoggerFactory.getLogger(DateUtil.class);

    public static final String DATE_FORMAT = "dd/MM/yyyy";
    
    
    public static Date getDate(String date){
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
           logger.error("Unable to parse date: "+ date);
        }
        return null;
    }
    
    public static Date changeFormat(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        String sDate = sdf.format(date);
        return getDate(sDate);
    }

    public static String getCurrentDateAsString(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return  sdf.format(date);
    }

    public static Date getCurrentDate(){
        return  new Date();
    }


}
