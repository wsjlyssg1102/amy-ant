package com.pactera.hn.rtds.scala.untils

import com.pactera.hn.rtds.java.{PreStatTimeType, logAnalyseConstants}
import com.pactera.hn.rtds.java.keyValue.{PacteraForRLKey, PacteraForRLValue, RedisParmKey}
import com.pactera.hn.rtds.java.untils.JavaDataAnalyseHandler
import com.pactera.hn.rtds.scala.initialRDDOPerator.{CreateObjectRdd, CreateStatDataRdd}
import com.pactera.hn.rtds.scala.outPutOperator.{OutPutToRedis, OutputTOhdfs}
import org.apache.hadoop.conf.Configuration
import org.apache.spark.HashPartitioner
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Seconds, StateSpec, StreamingContext}

/**
  * Created by dnion on 2016/10/17.
  */
class UpdateStateByketHandler(ssc:StreamingContext,
                              hadoopconf:Configuration,
                              flageForReadHdfs: Boolean,
                               numRdd:Int,
                              schedulingInterval:Int,
                              broadcastForRedisPram:Broadcast[RedisParmKey]) extends  java.io.Serializable{


  //预统计updateStateBykey
  def updatePreStatData(origStream: DStream[(PacteraForRLKey, PacteraForRLValue)],
                         clearStateTTl: Int, keepNoneStateTimes: Int,
                        hdfsPreStatPath:String, timeType:PreStatTimeType,dbIndex:Int): Unit = {

    val mergeKeyValue = (iterator: Iterator[(PacteraForRLKey, Seq[PacteraForRLValue], Option[(PacteraForRLValue, Boolean, Int)])]) => {
      iterator.flatMap(t => DataAnalyseHandler.mergeValue(t._2, clearStateTTl, t._3, keepNoneStateTimes).map(s => (t._1, s)))
    }

    val preStatStream = flageForReadHdfs match  {
      case true =>{
        timeType match {
          case PreStatTimeType.MINUTES =>{
            val newStream =  origStream.updateStateByKey(mergeKeyValue, new HashPartitioner(numRdd), true)
            newStream
          }
          case PreStatTimeType.HOUR =>{
            val newstream = origStream.map(x=>{
              x._1.setConDate(JavaDataAnalyseHandler.getFrontTimeRangeSecs(x._1.getConDate,logAnalyseConstants.DATA_CONSTANTS_ONE_HOUR_SECONDS))
              (x._1,x._2)
            }).updateStateByKey(mergeKeyValue,new HashPartitioner(numRdd), true)
            newstream
          }
          case PreStatTimeType.DAY =>{
            val newStream =origStream.map(x => {
                x._1.setConDate(JavaDataAnalyseHandler.getFrontTimeRangeSecs(x._1.getConDate, logAnalyseConstants.DATA_CONSTANTS_ONE_DAY_SECONDS))
                (x._1, x._2)}) updateStateByKey(mergeKeyValue, new HashPartitioner(numRdd), true)
            newStream
          }
        }
      }
      case false  =>{
        timeType match {
          case PreStatTimeType.MINUTES =>{
            val preStatRDD = CreateObjectRdd.createRdd(ssc,hdfsPreStatPath,numRdd,hadoopconf,keepNoneStateTimes)
            val newStream =  origStream.updateStateByKey(mergeKeyValue, new HashPartitioner(numRdd), true, preStatRDD)
            newStream
          }
          case PreStatTimeType.HOUR =>{
            val preStatRDD = CreateObjectRdd.createRdd(ssc,hdfsPreStatPath,numRdd,hadoopconf,keepNoneStateTimes)
            val newstream = origStream.map(x=>{
              x._1.setConDate(JavaDataAnalyseHandler.getFrontTimeRangeSecs(x._1.getConDate,logAnalyseConstants.DATA_CONSTANTS_ONE_HOUR_SECONDS))
              (x._1,x._2)
            }).updateStateByKey(mergeKeyValue,new HashPartitioner(numRdd), true,preStatRDD)
            newstream
          }
          case PreStatTimeType.DAY =>{
            val preStatRDD = CreateObjectRdd.createRdd(ssc, hdfsPreStatPath, numRdd, hadoopconf, keepNoneStateTimes)
            val newStream =origStream.map(x => {
              x._1.setConDate(JavaDataAnalyseHandler.getFrontTimeRangeSecs(x._1.getConDate, logAnalyseConstants.DATA_CONSTANTS_ONE_DAY_SECONDS))
              (x._1, x._2)}) updateStateByKey(mergeKeyValue, new HashPartitioner(numRdd), true,preStatRDD)

            newStream
          }
        }
      }
    }

    preStatStream.persist(StorageLevel.MEMORY_ONLY_SER_2)
    preStatStream.checkpoint(Seconds(schedulingInterval *2))

    OutPutToRedis.outPutToRedis(preStatStream,broadcastForRedisPram,dbIndex)
    OutputTOhdfs.writePacteraStream(preStatStream,hdfsPreStatPath)


  }

}
