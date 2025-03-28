package com.alianza.test.cm.service.utils;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.util.Date;

public class ProcessorUtil {

    // Format Dates
    public static final String[] DATE_FORMATS = {
            "dd-MM-yyyy","dd-MM-yyyy HH:mm:ss","hh:mm:ss","yyyy-MM-dd","yyyy-MM-dd HH:mm:ss","yyyy-MM-dd'T'HH:mm:ss.SSS'Z'","yyyy-MM-dd'T'HH:mm:ss.SSS",
            "dd/MM/yyyy","dd/MM/yyyy HH:mm:ss","hh:mm:ss","yyyy/MM/dd","yyyy/MM/dd HH:mm:ss","yyyy/MM/dd'T'HH:mm:ss.SSS'Z'","yyyy/MM/dd'T'HH:mm:ss.SSS"
    };

    public static final String  DATE_FORMAT_DDMMYYYY = "dd/MM/yyyy";


    public static String convertDate(String stringDate, String format){
        Date date;
        try {
            date = DateUtils.parseDateStrictly(stringDate, DATE_FORMATS);
            return  DateFormatUtils.format(date, format);

        } catch (ParseException e) {
           return  stringDate;
        }
    }

}
