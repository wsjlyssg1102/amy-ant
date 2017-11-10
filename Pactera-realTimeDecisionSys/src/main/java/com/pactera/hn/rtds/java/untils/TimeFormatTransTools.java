package com.pactera.hn.rtds.java.untils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Administrator on 2017/3/2.
 */
public class TimeFormatTransTools {

    private static SimpleDateFormat formatter;

    public static  long timeFormatTrans(String string){
        try {
            if(string.indexOf("/") > -1 && string.indexOf(" ")== -1){

                formatter = new SimpleDateFormat("yyyy/MM/dd");
            }
            else if(string.indexOf("/") > -1 && string.indexOf(" ") > -1){
                formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            }
            else if(string.indexOf("-") > -1 && string.indexOf(" ") > -1){
                formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            }
            else if(string.indexOf("-") > -1 && string.indexOf(" ") == -1){
                formatter = new SimpleDateFormat("yyyy-MM-dd");
            }
            return  formatter.parse(string).getTime()/1000;
        }catch (ParseException e){
            return  0l;
        }


    }

}
