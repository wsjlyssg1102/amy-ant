package com.pactera.hn.rtds.scala.untils


import com.pactera.hn.rtds.java.keyValue.{PacteraForRLKey, PacteraForRLValue}
import com.pactera.hn.rtds.java.logAnalyseConstants
import com.pactera.hn.rtds.java.untils.{JavaDataAnalyseHandler, TimeFormatTransTools}
import net.sf.json.JSONObject


/**
  * Created by dnion on 2016/8/29.
  */
object DataAnalyseHandler extends java.io.Serializable {


  def streamPacteraElemHandler(strElem :String):(PacteraForRLKey,PacteraForRLValue)={
    val key = new PacteraForRLKey("null", "null", "null", 0l, "null", "null", "null", 0l, "null")
    val   detalList = new java.util.ArrayList[java.lang.Long]()
    val value = new PacteraForRLValue(0l,0,detalList)
    try{
      val data = JSONObject.fromObject(strElem)

      try {
        val carType = data.getString(logAnalyseConstants.PACTERA_CARD_TYPE)
        key.setCarType(carType)
      }catch {
        case ex:Exception =>{
          key.setCarType("null")
        }
      }

      try {
        val module = data.getString(logAnalyseConstants.PACTERA_MODULE)
        key.setModule(module)
      }catch {
        case ex:Exception =>{
          key.setModule("test")
        }
      }

      try{
        val cardNo = data.getString(logAnalyseConstants.PACTERA_CARD_NO)
        key.setCardNo(cardNo)
      }catch {
        case ex:Exception =>{
          key.setCardNo("null")
        }
      }

      try {
        val cardAum = data.getLong(logAnalyseConstants.PACTERA_CARD_AUM)
        key.setCardAum(cardAum)
      }catch {
        case  ex:Exception =>{
          key.setCardAum(0)
        }
      }

      try{
        val custNo = data.getString(logAnalyseConstants.PACTERA_CUST_NO)
        key.setCustNo(custNo)
      }catch {
        case ex:Exception =>{
          key.setCustNo("null")
        }
      }

      try{
        val cardStatus = data.getString(logAnalyseConstants.PACTERA_CARD_STATUS)
        key.setCardStatus(cardStatus)
      }catch {
        case ex:Exception =>{
          key.setCardStatus("null")
        }
      }

      try{
        val openDate = data.getString(logAnalyseConstants.PACTERA_OPEN_DATE)
        key.setOpenDate(openDate)

      }catch {
        case ex:Exception =>{
          key.setOpenDate("null")

        }
      }
      try{
        val condate = data.getString(logAnalyseConstants.PACTERA_CONSUM_DATE)
        key.setConDate(JavaDataAnalyseHandler.getBehindTimeRangeSecs(TimeFormatTransTools.timeFormatTrans(condate),logAnalyseConstants.DATA_CONSTANTS_FIVE_MIN_SECONDS))
      }catch {
        case ex:Exception =>{
          key.setConDate(0l)
          key.setModule("null")
        }
      }

      try{
        val channelType = data.getString(logAnalyseConstants.PACTERA_CHANNEL_TYPE)
        key.setChannelType(channelType)
      }catch {
        case ex :Exception =>{
          key.setChannelType("null")
        }
      }
      try{
        val conAmt = data.getLong(logAnalyseConstants.PACTERA_EVER_CONSUM_NUM)
        value.getDetalList.add(conAmt)
        value.setTotalConsums(conAmt)
      }catch {
        case ex :Exception =>{
          value.getDetalList.add(0l)
          value.setTotalConsums(0l)
        }
      }
      value.setTotalConsumTimes(1)

    }catch {
      case ex:Exception =>{
        key.setModule("null")
      }
    }

    (key,value)

  }



  def filterFuncForInvalid(elem:(PacteraForRLKey,PacteraForRLValue)):Boolean ={
    if(elem._1.getModule.equals("null")) false
    else true
  }


  def newMergeBoolen(valueOne:(PacteraForRLValue,Boolean,Int),valueTwo:(PacteraForRLValue,Boolean,Int)):(PacteraForRLValue,Boolean,Int)={
    val   detalList = new java.util.ArrayList[java.lang.Long]()
    var totalValue = new PacteraForRLValue(0l,0,detalList)
    totalValue = valueOne._1.add(valueTwo._1)
    (totalValue,valueOne._2,valueOne._3)
  }

  def getMinTime(time: Long, min: Int): Long = {
    var result: Long = 0l
    if (time % min == 0) {
      result = time
    } else {
      val tmp = time % min
      result = time - tmp
    }
    result
  }

  def toStrings(str:List[java.lang.Long]): String ={
    var res :String=""
    for(index <- 0 until(str.size -1)){
      res = res.concat(str(index).toString).concat(",")
    }
    res = res.concat(str(str.size-1).toString)
    res
  }



  //累加
  def mergeValue(values: Seq[PacteraForRLValue], clearStateTTl: Int, state: Option[(PacteraForRLValue, Boolean, Int)], keepNoneStateTimes: Int) = {
    var result: Option[(PacteraForRLValue, Boolean, Int)] = null
    val   detalList = new java.util.ArrayList[java.lang.Long]()

    var keepStateTimes = state.getOrElse((new PacteraForRLValue(0,0,detalList),false,clearStateTTl))._3
    val currentValue = new PacteraForRLValue(0l,0,detalList)
    values.foreach(x=>currentValue.add(x))
    if(keepStateTimes >0){
      val   detalListState = new java.util.ArrayList[java.lang.Long]()
      keepStateTimes -=1
      val previousValue = state.getOrElse((new PacteraForRLValue(0,0,detalListState), false,keepStateTimes))
      val stateChange = {
        if (currentValue.getTotalConsums != 0 || currentValue.getTotalConsumTimes != 0 ) true
        else false
      }
      previousValue._1.add(currentValue)
      result = Some((previousValue._1, stateChange,keepStateTimes))

    }
    else {
      if(values.isEmpty){
        var counter = state.get._3
        if (counter == logAnalyseConstants.DATA_CONTANTS_STATE_KEEP_TIMES) {
          result = None
        } else {
          counter -= 1
          result = Some(state.get._1, false,counter)
        }
      }
      else{
        val   detalListState = new java.util.ArrayList[java.lang.Long]()
        val previousValue = state.getOrElse((new PacteraForRLValue(0,0,detalListState), false, keepNoneStateTimes))
        val stateChange = {
          if (currentValue.getTotalConsums != 0 || currentValue.getTotalConsumTimes != 0 ) true
          else false
        }
        previousValue._1.add(currentValue)
        result = Some((previousValue._1, stateChange,keepNoneStateTimes))
      }
    }
    result
  }


}