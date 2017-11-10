package com.pactera.hn.rtds.scala.initialRDDOPerator

import com.pactera.hn.rtds.java.keyValue.{PacteraForRLKey, PacteraForRLValue}
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path, PathFilter}
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.StreamingContext
import org.slf4j.{Logger, LoggerFactory}

/**
  * Created by Administrator on 2017/3/7.
  */
object CreateObjectRdd extends java.io.Serializable {
  private val logger: Logger = LoggerFactory
    .getLogger(CreateObjectRdd.getClass)

  def createRdd(ssc: StreamingContext, hdfsPathPrefix: String, numRDD: Int, conf: Configuration, keepStateTimes: Int):RDD[(PacteraForRLKey, (PacteraForRLValue, Boolean, Int))]={

    var finalRDD:RDD[(PacteraForRLKey, (PacteraForRLValue, Boolean, Int))] =null
    var seqRdd: Seq[RDD[(PacteraForRLKey, (PacteraForRLValue, Boolean, Int))]] = Seq()
    val hdfs  = FileSystem.get(conf).listStatus(new Path(hdfsPathPrefix),new PathFilter {
      override def accept(path: Path): Boolean = {
        if(path.toString.contains("part"))
          true
        else
          false
      }
    })
    if(hdfs.isEmpty){
      val elem = createNoneElems()
      finalRDD= ssc.sparkContext.makeRDD(Seq((elem._1,(elem._2,false,keepStateTimes))),numRDD)
      seqRdd = seqRdd.+:(finalRDD)
    }else{
      for (index <- 0 until hdfs.length) {
        val rdd = ssc.sparkContext.objectFile[(PacteraForRLKey, (PacteraForRLValue, Boolean, Int))](hdfs(index).getPath.toString.trim,numRDD)
        seqRdd = seqRdd.+:(rdd)
      }
    }

    val  resultRdd = ssc.sparkContext.union(seqRdd)

    resultRdd

  }

  def createNoneElemRdd(ssc:StreamingContext,keepStateTimes:Int,numRDD:Int):RDD[(PacteraForRLKey, (PacteraForRLValue, Boolean, Int))]={
    val elem = createNoneElems()
    val rdd = ssc.sparkContext.makeRDD(Seq((elem._1,(elem._2,false,keepStateTimes))),numRDD)
    rdd

  }

  def createNoneElems():(PacteraForRLKey, PacteraForRLValue)={
    val key = new PacteraForRLKey("null", "null", "null", 0l, "null", "null", "null", 0l, "null")
    val detalList = new java.util.ArrayList[java.lang.Long]()
    val value = new PacteraForRLValue(0l, 0, detalList)
    (key,value)
  }

}
