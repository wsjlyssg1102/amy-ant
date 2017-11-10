package com.pactera.hn.rtds.scala.untils

import com.pactera.hn.rtds.java.{PreStatTimeType, logAnalyseConstants}
import com.pactera.hn.rtds.java.keyValue.{PacteraForRLKey, PacteraForRLValue, RedisParmKey}
import org.apache.hadoop.conf.Configuration
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.DStream

import scala.collection.mutable.ArrayBuffer

/**
  * Created by Administrator on 2017/3/3.
  */
object PresStatHandler {

  def handlerPreStatData(ssc: StreamingContext,
                         hadconf: Configuration,
                         originalStream: DStream[(PacteraForRLKey, PacteraForRLValue)],
                         keepNoneStateTimes: Int,
                         flageForReadHdfs: Boolean,
                         broadcastForParmKey: Broadcast[RedisParmKey],
                         schedulingInterval:Int,
                         broadcastForPreStatRdd:Broadcast[Map[String,String]],
                         dbindex:ArrayBuffer[Int],
                         clearStateTTl: Int,
                         clearStateHourTTl:Int,
                         clearStateDayTTl: Int,
                         numRdd:Int):Unit ={
    val updateHandler = new UpdateStateByketHandler(ssc,hadconf,flageForReadHdfs,
      numRdd,schedulingInterval,broadcastForParmKey)

    //demo  five min data
    updateHandler.updatePreStatData(originalStream,clearStateTTl,keepNoneStateTimes,
      broadcastForPreStatRdd.value(logAnalyseConstants.RDD_FLAG_PACTERA_MIN),PreStatTimeType.MINUTES,dbindex(0))

    //demo hour data

    updateHandler.updatePreStatData(originalStream,clearStateHourTTl,keepNoneStateTimes,
      broadcastForPreStatRdd.value(logAnalyseConstants.RDD_FLAG_PACTERA_HOUR),PreStatTimeType.HOUR,dbindex(1))

   //demo dat data
    updateHandler.updatePreStatData(originalStream,clearStateDayTTl,keepNoneStateTimes,
      broadcastForPreStatRdd.value(logAnalyseConstants.RDD_FLAG_PACTERA_DAY),PreStatTimeType.DAY,dbindex(2))
  }

}
