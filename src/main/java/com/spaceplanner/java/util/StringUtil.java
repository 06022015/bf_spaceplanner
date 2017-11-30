package com.spaceplanner.java.util;

/**
 * Created by IntelliJ IDEA.
 * User: ashifqureshi
 * Date: 06/11/15
 * Time: 12:43 AM
 * To change this template use File | Settings | File Templates.
 */
public class StringUtil {
    
    
    
    public static boolean isNotNullOrEmpty(Object text){
       return null!=text && !"".equals(text);
    }
    
    public static String trim(String text){
        return null!=text?text.trim():null;
    }
    
    public static String toString(Object text){
        return null!=text?text.toString().trim():null;
    }
}
