package com.boot.utils;


import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取批量异常信息
 * Created by sunshine on 2018/9/26.
 */
public class ConstraintViolationExceptionHandler {

    public static String getMessage(ConstraintViolationException e){
        List<String> msList=new ArrayList<>();
        for(ConstraintViolation<?> constraintViolation:e.getConstraintViolations()){
            msList.add(constraintViolation.getMessage());
        }
        String messages= StringUtils.join(msList.toArray(),";");
        return messages;
    }




}
