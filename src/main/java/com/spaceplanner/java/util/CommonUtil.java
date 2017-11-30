package com.spaceplanner.java.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * UserEntity: Ashif.Qureshi
 * Date: 25/8/14
 * Time: 12:13 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class CommonUtil {

    private Logger logger = LoggerFactory.getLogger(CommonUtil.class);

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private Properties properties;

    private MessageSourceAccessor messageSourceAccessor=null;

    public static Object getBean(String beanName) {
        return ApplicationContextUtil.getApplicationContext().getBean(beanName);
    }

    private MessageSourceAccessor getMessageSourceAccessor() {
        if(null==messageSourceAccessor)
            messageSourceAccessor = new MessageSourceAccessor(messageSource);
        return messageSourceAccessor;
    }

    private static Properties getProperties() {
        return (Properties) getBean("properties");
    }

    public static String getProperty(String text) {
        return getProperties().getProperty(text);
    }

    public String getText(String key){
        return getMessageSourceAccessor().getMessage(key);
    }

    public String getText(String key, String arg) {
        return getText(key, new Object[]{arg});
    }

    public String getText(String key, Object[] args) {
        return getMessageSourceAccessor().getMessage(key, args);
    }

    public String getText(String msgKey, Locale locale) {
        return getMessageSourceAccessor().getMessage(msgKey, locale);
    }

    public String getText(String msgKey, String arg, Locale locale) {
        return getText(msgKey, new Object[]{arg}, locale);
    }

    public String getText(String msgKey, Object[] args, Locale locale) {
        return messageSource.getMessage(msgKey, args, locale);
    }

}
