package com.pactera.hn.rtds.java.untils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by dnion on 201/08/29.
 */
public class ConfigHandler {
    private static final long serialVersionUID = -2283586891263163719L;
    private String configFileName;
    private Properties prop;
    private Map<String, String> parameterMap;
    public ConfigHandler(String confName) {
        this.configFileName = confName;
    }

    public void init() throws Exception {
        if (configFileName == null) {
            throw new Exception("missing config file name");
        }
        prop = new Properties();
        InputStream in = null;
        try {
            File configFile = JavaDataAnalyseHandler
                    .getAppopintFile(configFileName);
            if (configFile.exists()) {
                in = new FileInputStream(configFile);
            } else {
                in = this.getClass().getClassLoader()
                        .getResourceAsStream(configFileName);
            }
            prop.load(in);
            parameterMap = new HashMap<String, String>();
            for (Object key : prop.keySet()) {
                String newKey = String.valueOf(key);
                String value = prop.getProperty(newKey);
                if (value != null && !value.trim().equals("")) {
                    parameterMap.put(newKey, value.trim());
                }
            }
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
            }
        }
    }

    public String getParameter(String key) {
        return parameterMap.get(key);
    }

    public String getParameter(String key, String defaultValue) {
        String value = parameterMap.get(key);
        if (value == null) {
            value = defaultValue;
        }
        return value;
    }

    public int getIntParameter(String key, int defaultValue) {
        int value = defaultValue;
        String valueStr = parameterMap.get(key);
        if (valueStr != null) {
            value = Integer.valueOf(valueStr);
        }
        return value;
    }

    public long getLongParameter(String key, long defaultValue) {
        long value = defaultValue;
        String valueStr = parameterMap.get(key);
        if (valueStr != null) {
            value = Long.valueOf(valueStr);
        }
        return value;
    }

    public boolean getBooleanParameter(String key, boolean defaultValue) {
        boolean value = defaultValue;
        String valueStr = parameterMap.get(key);
        if (valueStr != null) {
            value = Boolean.valueOf(valueStr);
        }
        return value;
    }

    public double getDoubleParameter(String key, double defaultValue) {
        double value = defaultValue;
        String valueStr = parameterMap.get(key);
        if (valueStr != null) {
            value = Double.valueOf(valueStr);
        }
        return value;
    }
}
