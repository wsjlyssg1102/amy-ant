package com.pactera.hn.rtds.java.untils;





import com.pactera.hn.rtds.java.logAnalyseConstants;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dnion on 201/08/29.
 */
public class JavaDataAnalyseHandler {


    public static long getTimeStamp(String timeStr) {
        if (timeStr == null || timeStr.trim().equals(""))
            return 0l;
        SimpleDateFormat formatDate = new SimpleDateFormat(logAnalyseConstants.SPARK_DELAY_TIME_FORMAT);
        Date date = null;
        try {
            date = formatDate.parse(timeStr);
        } catch (ParseException e) {
            return 0l;
        }
        return date.getTime()/1000;
    }

    public static long getFrontTimeRangeSecs(long timestamp, int partitonTimeInterval){
        if(partitonTimeInterval== logAnalyseConstants.DATA_CONSTANTS_ONE_DAY_SECONDS){
            long newTimeStamp = 0;
            long last = (timestamp+28800) % partitonTimeInterval;
            if(last ==0){
                newTimeStamp = timestamp -logAnalyseConstants.DATA_CONSTANTS_ONE_DAY_SECONDS;
            }
            else {
                newTimeStamp = timestamp - last;
            }
            return newTimeStamp;
        }
        else {
            long last = timestamp % partitonTimeInterval;
            if (last == 0) {
                return timestamp -logAnalyseConstants.DATA_CONSTANTS_ONE_HOUR_SECONDS;
            }
            else {
                return timestamp - last;
            }
        }

    }

    /**
     * @param timestamp
     * @param partitonTimeInterval
     * @return
     */
    public static long getBehindTimeRangeSecs(long timestamp, int partitonTimeInterval){
        long last = timestamp % partitonTimeInterval;
        return partitonTimeInterval - last + timestamp;
    }

    public static File getAppopintFile(String fileName){
        File resultFile = new File(fileName);
        String userDir = null;
        if(!resultFile.exists()){
            userDir = System.getProperty("user.dir");
            resultFile = new File(userDir,fileName);
        }
        if(!resultFile.exists()){
            resultFile = new File(userDir +"/etc",fileName);
        }
        if(!resultFile.exists()){
            resultFile = new File(userDir + "/conf",fileName);
        }
        if(!resultFile.exists()){
            File parntFile = new File(userDir).getParentFile();
            resultFile = new File(parntFile,"etc/"+fileName);
        }
        if(!resultFile.exists()){
            File parntFile = new File(userDir).getParentFile();
            resultFile = new File(parntFile,"conf/"+fileName);
        }
        return  resultFile;
    }

}
