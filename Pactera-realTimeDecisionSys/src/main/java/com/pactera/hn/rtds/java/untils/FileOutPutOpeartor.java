package com.pactera.hn.rtds.java.untils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;

/**
 * Created by dnion on 2016/5/13.
 */
public class FileOutPutOpeartor implements Serializable {

    private String fileName;
    private String hdfsPathSuffix;

    public FileOutPutOpeartor(String fileName, String hdfsPath) {
        this.fileName = fileName;
        this.hdfsPathSuffix = hdfsPath;
    }

    public void WrieOffsetToFile(String[] elem) throws Exception {
        Configuration conf = new Configuration();
        FileSystem newHdfs = null;
        try {
            newHdfs = FileSystem.get(URI.create(hdfsPathSuffix), conf);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        String hdfsPathStr = new StringBuffer(hdfsPathSuffix).append("/").append(fileName).toString();
        Path hdfsPath= new Path(hdfsPathStr);
        if(!newHdfs.exists(hdfsPath)){
            newHdfs.createNewFile(hdfsPath);
        }

        try {
            FSDataOutputStream hdfsout = newHdfs.create(hdfsPath, true);
            for (int index = 0; index < elem.length; index++) {
                byte[] context = elem[index].trim().concat("\n").getBytes();
                hdfsout.write(context);
                hdfsout.flush();
            }
           hdfsout.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> readerOffset() throws Exception {
        Configuration conf = new Configuration();
        FileSystem newHdfs = null;
        try {
            newHdfs = FileSystem.get(URI.create(hdfsPathSuffix), conf);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String hdfsPathStr = new StringBuffer(hdfsPathSuffix).append("/").append(fileName).toString();
        Path hdfsPath= new Path(hdfsPathStr);
        if(!newHdfs.exists(hdfsPath)){
            newHdfs.createNewFile(hdfsPath);
        }
        FSDataInputStream fsDataInputStream = new FSDataInputStream(newHdfs.open(hdfsPath));
        BufferedReader fileReader = new BufferedReader(new InputStreamReader(fsDataInputStream));
        try {
            String str = fileReader.readLine();
            ArrayList<String> result = new ArrayList<String>();
            while (str != null) {
                result.add(str.trim());
                str = fileReader.readLine();
            }
            fileReader.close();
            return result;
        } catch (IOException e) {
            throw e;
        }
    }
    public synchronized  long checkUpdate() throws  Exception{
        Configuration conf = new Configuration();
        FileSystem newHdfs = null;
        try {
            newHdfs = FileSystem.get(URI.create(hdfsPathSuffix), conf);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String hdfsPathStr = new StringBuffer(hdfsPathSuffix).append("/").append(fileName).toString();
        Path hdfsPath= new Path(hdfsPathStr);
        if(!newHdfs.exists(hdfsPath)){
            newHdfs.createNewFile(hdfsPath);
        }
        FileStatus fileStatus = newHdfs.getFileStatus(hdfsPath);
        return  fileStatus.getModificationTime();
    }
}
