package com.pactera.hn.rtds.java.keyValue;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/19.
 */
public class PacteraForRLKey implements Serializable {
    private String module;
    private String carType ;
    private String cardNo ;
    private long cardAum ;
    private  String custNo ;
    private String cardStatus ;
    private String openDate ;
    private long conDate  ;

    private String channelType ;

    public PacteraForRLKey(String module,
                           String carType,
                           String  cardNo,
                           long cardAum,
                           String custNo,
                           String cardStatus,
                           String openDate,
                           long conDate,
                           String channelType){
        this.module = module;
        this.carType = carType;
        this.cardNo = cardNo;
        this.cardAum = cardAum;
        this.custNo = custNo;
        this.cardStatus = cardStatus;
        this.openDate = openDate;
        this.conDate = conDate;
        this.channelType = channelType;

    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public void setCardAum(long cardAum) {
        this.cardAum = cardAum;
    }

    public void setCustNo(String custNo) {
        this.custNo = custNo;
    }

    public void setCardStatus(String cardStatus) {
        this.cardStatus = cardStatus;
    }

    public void setOpenDate(String openDate) {
        this.openDate = openDate;
    }



    public void setConDate(long conDate) {
        this.conDate = conDate;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }


    public String getCarType() {
        return carType;
    }

    public String getCardNo() {
        return cardNo;
    }

    public long getCardAum() {
        return cardAum;
    }

    public String getCustNo() {
        return custNo;
    }

    public String getCardStatus() {
        return cardStatus;
    }

    public String getOpenDate() {
        return openDate;
    }



    public long getConDate() {
        return conDate;
    }

    public String getChannelType() {
        return channelType;
    }

    @Override
    public int hashCode(){
        int res = module.hashCode()^  carType.hashCode() ^ cardNo.hashCode()^ new Long(cardAum).hashCode() ^ custNo.hashCode() ^ cardStatus.hashCode()^ openDate.hashCode() ^ new Long(conDate).hashCode() ^ channelType.hashCode();
        return  res;
    }

    @Override
    public boolean equals(Object object){

        if(object == null){
            return  false;
        }
        if(!(object instanceof PacteraForRLKey)){
            return  false;
        }
        PacteraForRLKey tempObj = (PacteraForRLKey) object;
        return  module.equals(tempObj.getModule()) && carType.equals(tempObj.getCarType()) && (cardAum==tempObj.getCardAum()) &&
                cardNo.equals(tempObj.getCardNo()) && custNo.equals(tempObj.getCustNo()) &&
                cardStatus.equals(tempObj.getCardStatus()) && openDate.equals(tempObj.getOpenDate())  &&
                channelType.equals(tempObj.getChannelType()) && (conDate == tempObj.getConDate()) ;

    }
}
