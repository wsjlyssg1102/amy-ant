package com.pactera.hn.rtds.java.keyValue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/19.
 */
public class PacteraForRLValue implements Serializable{
    private  long totalConsums;
    private  int totalConsumTimes;
    private ArrayList<Long>  detalList ;



    public PacteraForRLValue(){}

    public PacteraForRLValue(long totalConsums,int totalConsumTimes, ArrayList<Long> list){
        this.totalConsums = totalConsums;
        this.totalConsumTimes = totalConsumTimes;
        this.detalList = list;
    }

    public long getTotalConsums() {
        return totalConsums;
    }

    public void setTotalConsums(long totalConsums) {
        this.totalConsums = totalConsums;
    }

    public int getTotalConsumTimes() {
        return totalConsumTimes;
    }

    public void setTotalConsumTimes(int totalConsumTimes) {
        this.totalConsumTimes = totalConsumTimes;
    }

    public ArrayList<Long> getDetalList() {
        return detalList;
    }

    public void setDetalList(ArrayList<Long> detalList) {
        this.detalList = detalList;
    }



    public PacteraForRLValue add(PacteraForRLValue value){

        this.totalConsums +=  value.getTotalConsums();
        this.totalConsumTimes += value.getTotalConsumTimes();

        this.detalList.addAll(value.getDetalList());
        return  this;
    }
}
