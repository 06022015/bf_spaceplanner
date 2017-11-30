package com.spaceplanner.java.util;

import com.spaceplanner.java.dto.DesignUploadForm;
import com.spaceplanner.java.dto.FloorUploadForm;
import com.spaceplanner.java.exception.SpacePlannerResponseStatus;
import com.spaceplanner.java.model.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ashif
 * Date: 9/19/14
 * Time: 10:12 AM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class ValidatorUtil implements Constants {

    private Logger logger = LoggerFactory.getLogger(ValidatorUtil.class);

    private final static String PATTERN_PASSWORD = "^(?=.*\\d)(?=.*[A-Za-z])[A-Za-z0-9]{6,18}$";
    private final static String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private final static int NORMAL_TEXT_FIELD_LENGTH = 60;
    public final static String PATTERN_DOUBLE = "^[1-9]([0-9]{1,6})?(\\.[0-9]{1,2})?$";


    @Autowired
    private CommonUtil commonUtil;

    public void validate(UserEntity userEntity, SpacePlannerResponseStatus status) {
        userEntity.trim();
        if (isNullOrEmpty(userEntity.getEmail()))
            status.addError(commonUtil.getText("error.email.required", status.getLocale()));
        else if (!isValidEmail(userEntity.getEmail()))
            status.addError(commonUtil.getText("error.email.invalid", status.getLocale()));
        if(null== userEntity.getId()){
            if (isNullOrEmpty(userEntity.getPassword()))
                status.addError(commonUtil.getText("error.password.required", status.getLocale()));
            else if (!userEntity.getPassword().equals(userEntity.getConfirmPassword()))
                status.addError(commonUtil.getText("error.password.and.confirm.password.mismatch", status.getLocale()));
            else if (!isValidPassword(userEntity.getPassword()))
                status.addError(commonUtil.getText("error.password.invalid", status.getLocale()));
        }
        if (isNullOrEmpty(userEntity.getFirstName()))
            status.addError(commonUtil.getText("error.first.name.required", status.getLocale()));
        else if (!isValidLength(userEntity.getFirstName(), NORMAL_TEXT_FIELD_LENGTH))
            status.addError(commonUtil.getText("error.text.length.must.be.less.than",
                    new Object[]{PARAM_USER_FIRST_NAME, NORMAL_TEXT_FIELD_LENGTH}, status.getLocale()));

        if (isNullOrEmpty(userEntity.getLastName()))
            status.addError(commonUtil.getText("error.last.name.required", status.getLocale()));
        else if (!isValidLength(userEntity.getLastName(), NORMAL_TEXT_FIELD_LENGTH))
            status.addError(commonUtil.getText("error.text.length.must.be.less.than",
                    new Object[]{PARAM_USER_LAST_NAME, NORMAL_TEXT_FIELD_LENGTH}, status.getLocale()));

    }

    public void validate(DesignUploadForm designUploadForm, SpacePlannerResponseStatus status){
        if((null==designUploadForm.getDesignDXFFile() || designUploadForm.getDesignDXFFile().getSize()<=0)
                && (null==designUploadForm.getDesignPDFFile() || designUploadForm.getDesignPDFFile().getSize()<=0)){
            status.addError(commonUtil.getText("message.please.choose.atleast.one.file", status.getLocale()));
        }else{
            if((null != designUploadForm.getDesignPDFFile()&& designUploadForm.getDesignPDFFile().getSize()>0)
                    && !designUploadForm.getDesignPDFFile().getContentType().equals(Constants.FILE_CONTENT_TYPE_PDF)){
                status.addError(commonUtil.getText("error.invalid.file.format", status.getLocale()));
            }
            if((null != designUploadForm.getDesignDXFFile() && designUploadForm.getDesignDXFFile().getSize()>0)
                    && !designUploadForm.getDesignDXFFile().getOriginalFilename().endsWith(Constants.FILE_TYPE_DXF)){
                status.addError(commonUtil.getText("error.invalid.file.format", status.getLocale()));
            }
        }

    }


    public void validate(FloorUploadForm floorUploadForm, SpacePlannerResponseStatus status) {
        if (null == floorUploadForm.getFile() || floorUploadForm.getFile().getSize() <= 0) {
            status.addError(commonUtil.getText("error.please.choose.file", status.getLocale()));
        } else {
            if (!floorUploadForm.getFile().getOriginalFilename().endsWith(Constants.FILE_TYPE_XLS)) {
                if (!floorUploadForm.getFile().getOriginalFilename().endsWith(Constants.FILE_TYPE_XLSX))
                    status.addError(commonUtil.getText("error.invalid.file.format", status.getLocale()));
            }
        }

    }


    public void validateUserUpdateAbleFiled(String name, String value, SpacePlannerResponseStatus status) {
        name = null!= name?name.trim():name;
        if(!userUpdateAbleField().contains(name)){
            status.addError(commonUtil.getText("error.unknown.field.name", status.getLocale()));
            return;
        }
        value = null != value ? value.trim() : value;
        if (name.equals(PARAM_USER_FIRST_NAME)) {
            if (isNullOrEmpty(value))
                status.addError(commonUtil.getText("error.first.name.required", status.getLocale()));
        }else if(name.equals(PARAM_USER_LAST_NAME)){
            if (isNullOrEmpty(value))
                status.addError(commonUtil.getText("error.last.name.required", status.getLocale()));
            else if (!isValidLength(value, NORMAL_TEXT_FIELD_LENGTH))
                status.addError(commonUtil.getText("error.text.length.must.be.less.than",
                    new Object[]{PARAM_USER_LAST_NAME, NORMAL_TEXT_FIELD_LENGTH}, status.getLocale()));
        }
    }

    public void validatePassword(String password, String confirmPassword, SpacePlannerResponseStatus status){
        if(isNullOrEmpty(password))
            status.addError(commonUtil.getText("error.password.required", status.getLocale()));
        else if(!password.equals(confirmPassword))
            status.addError(commonUtil.getText("error.password.and.confirm.password.mismatch"));
        else if(!isValidPassword(password))
            status.addError(commonUtil.getText("error.password.invalid"));
    }

    public static List<String> userUpdateAbleField(){
        List<String> filedNameList = new ArrayList<String>();
        filedNameList.add(PARAM_USER_FIRST_NAME);
        filedNameList.add(PARAM_USER_LAST_NAME);
        filedNameList.add(PARAM_USER_PROFILE_PICK);
        return filedNameList;
    }

    public static boolean isValidLength(String text, int length) {
        return text.length() <= length;
    }

    public static boolean isValidEmail(String email) {
        return email.matches(PATTERN_EMAIL);
    }


    public static boolean isValidPassword(String password) {
        return password.matches(PATTERN_PASSWORD);
    }


    public static boolean isNullOrEmpty(String text) {
        return null == text || "".equals(text);
    }
}
