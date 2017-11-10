package com.pactera.hn.rtds.scala.outPutOperator


import com.pactera.hn.rtds.java.keyValue.{PacteraForRLKey, PacteraForRLValue}
import com.pactera.hn.rtds.scala.PacteraStreaming
import net.sf.json.JSONObject
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.DStream

/**
  * Created by dnion on 2016/10/17.
  */
object OutputTOhdfs {

var flagForWrite :Boolean = false

  def writePacteraStream(pacteraStream:DStream[(PacteraForRLKey,(PacteraForRLValue,Boolean,Int))], hdfsFilePathPrefix:String):Unit={
    pacteraStream.foreachRDD(t=>if(flagForWrite || PacteraStreaming.flagForTimerGraceStop){
      flagForWrite = true
      t.map(x=>{
        (x._1,(x._2._1,x._2._2,x._2._3))
      }).saveAsObjectFile(hdfsFilePathPrefix)}
    else {
      print("")
    })
  }
}
