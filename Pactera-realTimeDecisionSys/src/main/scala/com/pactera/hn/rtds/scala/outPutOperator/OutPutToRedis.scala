package com.pactera.hn.rtds.scala.outPutOperator

import java.util

import com.pactera.hn.rtds.java.keyValue.{PacteraForRLKey, PacteraForRLValue, RedisParmKey}
import com.pactera.hn.rtds.java.logAnalyseConstants
import com.pactera.hn.rtds.scala.untils.DataAnalyseHandler
import org.apache.commons.pool2.impl.GenericObjectPoolConfig
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.streaming.dstream.DStream
import redis.clients.jedis.JedisPool
import scala.collection.JavaConversions._


/**
 * Created by dnion on 2016/8/29.
 */
object OutPutToRedis extends  Serializable{


  def  outPutToRedis(stream:DStream[(PacteraForRLKey,(PacteraForRLValue,Boolean,Int))],
                     broadcastForRedisPram:Broadcast[RedisParmKey],dbIndex:Int):Unit={

    val key = broadcastForRedisPram.value
    stream.filter(_._2._2 == true).foreachRDD(rdd=>{
      rdd.foreachPartition(partitionOfRecords => {
        partitionOfRecords.foreach(pair => {

          object InternalRedisClient extends Serializable {

            @transient private var pool: JedisPool = null

            def makePool(redisHost: String, redisPort: Int, redisTimeout: Int,
                         maxTotal: Int, maxIdle: Int, minIdle: Int, testOnBorrow: Boolean,
                         testOnReturn: Boolean, maxWaitMillis: Long,redisBlockExhaust:Boolean): Unit = {
              if(pool == null) {
                val poolConfig = new GenericObjectPoolConfig()
                poolConfig.setMaxTotal(maxTotal)
                poolConfig.setMaxIdle(maxIdle)
                poolConfig.setMinIdle(minIdle)
                poolConfig.setTestOnBorrow(testOnBorrow)
                poolConfig.setTestOnReturn(testOnReturn)
                poolConfig.setMaxWaitMillis(maxWaitMillis)
                poolConfig.setBlockWhenExhausted(redisBlockExhaust)
                pool = new JedisPool(poolConfig, redisHost, redisPort, redisTimeout)

                val hook = new Thread{
                  override def run = pool.destroy()
                }
                sys.addShutdownHook(hook.run)
              }
            }

            def getPool: JedisPool = {
              assert(pool != null)
              pool
            }
          }

          // Redis configurations

          InternalRedisClient.makePool(key.getRedisHostName,
            key.getRedisHostPort,
            key.getRedisIdleConTimeOUt,
            key.getRedisMaxTotalConnects,
            key.getRedisMaxIdleConnects,
            key.getRedisMinIdleConnects,
            key.isRedisCheckConnect,
            key.isRedisCheckReturnConn,
            key.getRedisMaxWaitTime,
            key.isRedisBlockExhaust)

          val redisKey = pair._1.getModule.concat(logAnalyseConstants.DATA_FIELD_OR_SPACE).concat(pair._1.getCustNo).
            concat(logAnalyseConstants.DATA_FIELD_OR_SPACE).concat(pair._1.getConDate.toString).
            concat(logAnalyseConstants.DATA_FIELD_OR_SPACE).concat(pair._1.getChannelType)

          val map :java.util.Map[String,String]= new util.HashMap[String,String]()
          map.put(logAnalyseConstants.PACTERA_CARD_TYPE,pair._1.getCarType)
          map.put(logAnalyseConstants.PACTERA_CARD_NO,pair._1.getCardNo)
          map.put(logAnalyseConstants.PACTERA_CARD_AUM,pair._1.getCardAum.toString)
          map.put(logAnalyseConstants.PACTERA_CUST_NO,pair._1.getCustNo)
          map.put(logAnalyseConstants.PACTERA_CARD_STATUS,pair._1.getCardStatus)
          map.put(logAnalyseConstants.PACTERA_OPEN_DATE,pair._1.getOpenDate)
          map.put(logAnalyseConstants.PACTERA_CONSUM_DATE,pair._1.getConDate.toString)
          map.put(logAnalyseConstants.PACTERA_CHANNEL_TYPE,pair._1.getChannelType)
          map.put(logAnalyseConstants.PACTERA_TOTAL_CONSUM_TIME,pair._2._1.getTotalConsumTimes.toString)
          map.put(logAnalyseConstants.PACTERA_TOTAL_CONSUM_ACOUNT,pair._2._1.getTotalConsums.toString)
          map.put(logAnalyseConstants.PACTERA_CONSUM_ACOUNT_DETAIL,DataAnalyseHandler.toStrings(pair._2._1.getDetalList.toList))

          val jedis =InternalRedisClient.getPool.getResource
          jedis.select(dbIndex)
          jedis.hmset(redisKey,map)
          jedis.close()
        })
      })
    })
  }



}
