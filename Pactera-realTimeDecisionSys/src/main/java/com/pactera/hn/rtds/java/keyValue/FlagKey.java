package com.pactera.hn.rtds.java.keyValue;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/7.
 */
public class FlagKey  implements Serializable{
    private  long oldTime;

    public long getOldTime() {
        return oldTime;
    }

    public void setOldTime(long oldTime) {
        this.oldTime = oldTime;
    }

    @Override
    public int hashCode() {
        int ret =   new Long(oldTime).hashCode()  ;
        return ret;
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (!(obj instanceof FlagKey)) {
            return false;
        }
        FlagKey tmpObj = (FlagKey) obj;
        return  oldTime == (tmpObj.getOldTime()) ;
    }
}
