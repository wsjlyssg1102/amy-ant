package com.pactera.hn.rtds.scala.initialRDDOPerator

import com.pactera.hn.rtds.java.keyValue.{PacteraForRLKey, PacteraForRLValue}
import com.pactera.hn.rtds.java.logAnalyseConstants
import com.pactera.hn.rtds.java.untils.{JavaDataAnalyseHandler, TimeFormatTransTools}
import com.pactera.hn.rtds.scala.untils.DataAnalyseHandler
import net.sf.json.JSONObject
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path, PathFilter}
import org.apache.spark.rdd.RDD
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.StreamingContext
import org.slf4j.{Logger, LoggerFactory}

/**
  * Created by pactera on 2016/10/17.
  */
object CreateStatDataRdd extends java.io.Serializable {
  private val logger: Logger = LoggerFactory
    .getLogger(CreateStatDataRdd.getClass)

  def PrestatRdd(ssc: StreamingContext, hdfsPathPrefix: String, numRDD: Int, conf: Configuration, keepStateTimes: Int): RDD[(PacteraForRLKey, (PacteraForRLValue, Boolean, Int))] = {

    var finalRDD: RDD[String] = null
    var seqRdd: Seq[RDD[String]] = Seq()
    val hdfs = FileSystem.get(conf).listStatus(new Path(hdfsPathPrefix), new PathFilter {
      override def accept(path: Path): Boolean = {
        if (path.toString.contains("part"))
          true
        else
          false
      }
    })
    if (hdfs.isEmpty) {
      finalRDD = ssc.sparkContext.makeRDD(Seq[String]("{\"spark\":\"test\"}"), numRDD)
      seqRdd = seqRdd.+:(finalRDD)
    }
    else {
      for (index <- 0 until hdfs.length) {
        val rdd = ssc.sparkContext.textFile(hdfs(index).getPath.toString.trim, numRDD)
        seqRdd = seqRdd.+:(rdd)
      }
    }

    val baseKeyInitalRDD = ssc.sparkContext.union(seqRdd).map(elem => {
      if (elem.isEmpty) {
        val newElem = preStatKeyValueElem()
        (newElem._1, (newElem._2, false, keepStateTimes))
      } else {
        val key = new PacteraForRLKey("null", "null", "null", 0l, "null", "null", "null", 0l, "null")
        val detalList = new java.util.ArrayList[java.lang.Long]()
        val value = new PacteraForRLValue(0l, 0, detalList)
        val data = JSONObject.fromObject(elem)

        try {
          val carType = data.getString(logAnalyseConstants.PACTERA_CARD_TYPE)
          key.setCarType(carType)
        } catch {
          case ex: Exception => {
            key.setCarType("null")
          }
        }

        try {
          val module = data.getString(logAnalyseConstants.PACTERA_MODULE)
          key.setModule(module)
        } catch {
          case ex: Exception => {
            key.setModule("test")
          }
        }

        try {
          val cardNo = data.getString(logAnalyseConstants.PACTERA_CARD_NO)
          key.setCardNo(cardNo)
        } catch {
          case ex: Exception => {
            key.setCardNo("null")
          }
        }

        try {
          val cardAum = data.getLong(logAnalyseConstants.PACTERA_CARD_AUM)
          key.setCardAum(cardAum)
        } catch {
          case ex: Exception => {
            key.setCardAum(0)
          }
        }

        try {
          val custNo = data.getString(logAnalyseConstants.PACTERA_CUST_NO)
          key.setCustNo(custNo)
        } catch {
          case ex: Exception => {
            key.setCustNo("null")
          }
        }

        try {
          val cardStatus = data.getString(logAnalyseConstants.PACTERA_CARD_STATUS)
          key.setCardStatus(cardStatus)
        } catch {
          case ex: Exception => {
            key.setCardStatus("null")
          }
        }

        try {
          val openDate = data.getString(logAnalyseConstants.PACTERA_OPEN_DATE)
          key.setOpenDate(openDate)
        } catch {
          case ex: Exception => {
            key.setOpenDate("null")

          }
        }
        try {
          val condate = data.getString(logAnalyseConstants.PACTERA_CONSUM_DATE)
          key.setConDate(JavaDataAnalyseHandler.getBehindTimeRangeSecs(TimeFormatTransTools.timeFormatTrans(condate),logAnalyseConstants.DATA_CONSTANTS_FIVE_MIN_SECONDS))
        } catch {
          case ex: Exception => {
            key.setConDate(0l)
            key.setModule("null")
          }
        }

        try {
          val channelType = data.getString(logAnalyseConstants.PACTERA_CHANNEL_TYPE)
          key.setChannelType(channelType)
        } catch {
          case ex: Exception => {
            key.setChannelType("null")
          }
        }
        try {
          val conAmt = data.getLong(logAnalyseConstants.PACTERA_EVER_CONSUM_NUM)
          value.getDetalList.add(conAmt)
          value.setTotalConsums(conAmt)
        } catch {
          case ex: Exception => {
            value.getDetalList.add(0l)
            value.setTotalConsums(0l)
          }
        }

        value.setTotalConsumTimes(1)
        var keepStateTimesFromHDfs: Int = 0
        try {
          keepStateTimesFromHDfs = data.getInt(logAnalyseConstants.STATE_DATA_KEEP_TIMES)
        }
        catch {
          case ex: Exception => {
            keepStateTimesFromHDfs = keepStateTimes
          }
        }

        (key, (value, false, keepStateTimesFromHDfs))
      }
    }).reduceByKey(DataAnalyseHandler.newMergeBoolen(_, _), numRDD)
    baseKeyInitalRDD.persist(StorageLevel.MEMORY_ONLY_SER_2)
    baseKeyInitalRDD
  }

  def preStatKeyValueElem(): (PacteraForRLKey, PacteraForRLValue) = {
    val key = new PacteraForRLKey("null", "null", "null", 0l, "null", "null", "null", 0l, "null")
    val detalList = new java.util.ArrayList[java.lang.Long]()
    val value = new PacteraForRLValue(0l, 0, detalList)
    (key, value)
  }

}
