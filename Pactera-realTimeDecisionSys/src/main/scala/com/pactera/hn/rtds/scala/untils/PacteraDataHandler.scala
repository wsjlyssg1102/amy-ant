package com.pactera.hn.rtds.scala.untils

import com.pactera.hn.rtds.java.keyValue.RedisParmKey
import org.apache.hadoop.conf.Configuration
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.dstream.InputDStream

import scala.collection.mutable.ArrayBuffer

/**
 * Created by dnion on 2017/3/2.
 */
class PacteraDataHandler(ssc: StreamingContext,
                         hadConf:Configuration,
                         numRdd:Int,
                         keepNoneStateTimes:Int,
                         flageForReadHdfs:Boolean,
                         broadcastForPreStatRdd:Broadcast[Map[String,String]])  extends java.io.Serializable  {

  def handleStreamMediaStreaming(stream:InputDStream[(String,String)],
                                 broadcastForServerType:Broadcast[RedisParmKey],
                                 redisBdIndexs :ArrayBuffer[Int],
                                 delayTime:Int,
                                 clearStateMinTTl:Int,
                                 clearStateHourTTl:Int,
                                 clearStateDayTTl:Int,
                                 schedulingInterval:Int):Unit={
    //原始数据分析
    val pactearOriStreaming = stream.map(x=>DataAnalyseHandler.streamPacteraElemHandler(x._2)).
      filter(elem =>DataAnalyseHandler.filterFuncForInvalid(elem))


   //缓存数据，每两个批次进行checkpoint操作
    pactearOriStreaming.persist(StorageLevel.MEMORY_ONLY_SER)
    pactearOriStreaming.checkpoint(Seconds(schedulingInterval *2))



    //数据预处理
    PresStatHandler.handlerPreStatData(ssc,hadConf,pactearOriStreaming,keepNoneStateTimes,
      flageForReadHdfs,broadcastForServerType ,schedulingInterval,broadcastForPreStatRdd,redisBdIndexs,
      clearStateMinTTl,clearStateHourTTl,clearStateDayTTl,numRdd)



  }

}
