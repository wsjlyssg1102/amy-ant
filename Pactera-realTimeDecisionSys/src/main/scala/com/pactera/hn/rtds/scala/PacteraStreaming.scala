package com.pactera.hn.rtds.scala

import java.io.{File, FileNotFoundException}

import com.pactera.hn.rtds.java.keyValue.{FlagKey, PacteraForRLKey, PacteraForRLValue, RedisParmKey}
import com.pactera.hn.rtds.java.logAnalyseConstants
import com.pactera.hn.rtds.java.untils.{ConfigHandler, FileOutPutOpeartor}
import com.pactera.hn.rtds.scala.untils.{PacteraDataHandler, UpdateStateByketHandler}
import kafka.common.TopicAndPartition
import kafka.message.MessageAndMetadata
import kafka.serializer.StringDecoder
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.log4j.PropertyConfigurator
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkException}
import org.slf4j.{Logger, LoggerFactory}
import org.apache.spark.streaming.kafka.{HasOffsetRanges, KafkaUtils}

import scala.collection.JavaConversions._
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
  * Created by dnion on 2016/8/29.
  */
object PacteraStreaming extends Serializable {
  private val logger: Logger = LoggerFactory.getLogger(PacteraStreaming.getClass)
  private var handler: ConfigHandler = _
  private val fileName: String = logAnalyseConstants.CONFIG_FILE_NAME
  private var checkpoint: String = _
  private var sparkAppName: String = _

  private var setLogLevel: String = _
  private var numRdd: Int = _
  private var executorMemoryFraction: Double = _
  private var memoryForStorageFraction: String = _
  private var memoryForShuffleFraction: Double = _
  private var memoryLegacyMode: String = _
  private val hadoopConf = new Configuration()
  private var hdfsCoreCfgPath: String = _
  private var offsetFile: FileOutPutOpeartor = _
  private var offsetFileHdfsPath: String = _
  private var offsetFileHdfsPathCopy: String = _
  private var lastestOffset: java.util.ArrayList[String] = _
  private var offsetFileCopy: FileOutPutOpeartor = _
  private var offsetFileFlag: FileOutPutOpeartor = _
  private var receiverMaxRate: String = _

  private var schedulingInterval: Int = _
  private var shuffleType: String = _
  private var shuffleBuffSize: Int = _
  private var shuffleFetchDataTimes: Int = _
  private var shuffleFecthDataInterval: Int = _
  private var graceStop: Boolean = _
  private var serializerType: String = _

  private var realTimeKafkaTopics: String = _
  private var kafkabrokers: String = _
  private var kafkaoffset: String = _

  private var redisHostName: String = _
  private var redisHostPort: Int = _
  private var redisDbindexArr:ArrayBuffer[Int] = ArrayBuffer[Int]()
  private var redisMaxTotalConnects: Int = _
  private var redisMaxIdleConnects: Int = _
  private var redisMinIdleConnects: Int = _
  private var redisIdleConTimeOUt: Int = _
  private var redisCheckConnect: Boolean = _
  private var redisCheckReturnConn: Boolean = _
  private var redisBlockExhaust: Boolean = _
  private var redisMaxWaitTime: Long = _


  val redisParmData  = new RedisParmKey
  private var keepNoneStateTimes: Int = _

  private  var hdfsPreStatForMin:String =_
  private  var hdfsPreStatForHour:String =_
  private  var hdfsPreStatForDay:String =_
  private var hdfsPathMap: Map[String, String] = Map()

  var flagForTimer: Boolean = false
  var flagForTimerGraceStop: Boolean = false
  private  val keyForFlag :FlagKey = new FlagKey
  private  var modityTime:Long =0
  private var delayTimeForSecWork: Int = 0

  private var clearStateMinTTl: Int = _
  private var clearStateHourTTl: Int = _
  private var clearStateDayTTl: Int = _


  def initSparkConfgParam(): Unit = {
    handler = new ConfigHandler(fileName)
    handler.init()
    //spark checkpoint位置
    checkpoint = handler.getParameter(logAnalyseConstants.SPARK_CONFIG_CHECK_POINT,
      logAnalyseConstants.SPARK_CONFIG_CHECK_POINT_DEFAULT)
    if (checkpoint == null) {
      throw new Exception("未能得到checkpoint的目录！！！")
    }
    //spark app
    sparkAppName = handler.getParameter(logAnalyseConstants.SPARK_CONFIG_APP_NAME,
      logAnalyseConstants.SPARK_CONFIG_APP_NAME_RTSC)



    //spark日志级别
    setLogLevel = handler.getParameter(logAnalyseConstants.SPARK_CONFIG_SET_LOG_LEVEL,
      logAnalyseConstants.SPARK_CONFIG_SET_LOG_LEVEL_DEFAULT).toUpperCase

    //spark RDD 分区数
    numRdd = handler.getIntParameter(logAnalyseConstants.SPARK_CONFIG_NUM_RDD_PARTITIONS,
      logAnalyseConstants.SPARK_CONFIG_NUM_RDD_PARTITIONS_DEFAULT)

    //spark集群中每个executor用来存储的内存占分配的内存最低比例
    executorMemoryFraction = handler.getDoubleParameter(logAnalyseConstants.SPARK_CONFIG_STORAGE_FRACTION,
      logAnalyseConstants.SPARK_CONFIG_STORAGE_FRACTION_DEFAULT)

    //内存for存储比例
    memoryForStorageFraction = handler.getParameter(logAnalyseConstants.SPARK_CONFIG_MEMORY_STORAGE_FRACTION)
    if (memoryForStorageFraction == null) {
      throw new Exception("内存for存储的比例值没有配置！！！！")
    }
    //内存exceute的比例
    memoryForShuffleFraction = handler.getDoubleParameter(logAnalyseConstants.SPARK_CONFIG_EXECUTOR_MEMORY_FRACTION,
      logAnalyseConstants.SPARK_CONFIG_EXECUTOR_MEMORY_FRACTION_DEFAULT)
    //是否开启调整内存存储比例功能
    memoryLegacyMode = handler.getParameter(logAnalyseConstants.SPARK_CONFIG_MEMORY_USE_LEGACY_MODE,
      logAnalyseConstants.SPARK_CONFIG_MEMORY_USE_LEGACY_MODE_DEFAULT)

    //stream最大接收速率
    receiverMaxRate = handler.getParameter(logAnalyseConstants.SPARK_STREAMING_RECEIVER_MAX_RATE,
      logAnalyseConstants.SPARK_STREAMING_RECEIVER_MAX_RATE_DEFAULT)

    //stream调度间隔
    schedulingInterval = handler.getIntParameter(logAnalyseConstants.SPARK_STREAMING_SCHEDUL_INTERVAL,
      logAnalyseConstants.SPARK_STREAMING_SCHEDUL_INTERVAL_DEFAULT)

    //kafka
    realTimeKafkaTopics = handler.getParameter(logAnalyseConstants.SPARK_STREAMING_KAFKA_REALTIME_TOPICS)
    kafkabrokers = handler.getParameter(logAnalyseConstants.SPARK_STREAMING_KAFKA_BROKERS)
    if (kafkabrokers == null) {
      throw new Exception("未设置Kafka！！！")
    }
    kafkaoffset = handler.getParameter(logAnalyseConstants.SPARK_STREAMING_KAFKA_OFFSET,
      logAnalyseConstants.SPARK_STREAMING_KAFKA_OFFSET_DEFAULT)

    //shuffle类型
    shuffleType = handler.getParameter(logAnalyseConstants.SPARK_STREAMING_SHUFFLE_TYPE,
      logAnalyseConstants.SPARK_STREAMING_SHUFFLE_TYPE_DEFAULT)
    //shuffle缓存大小
    shuffleBuffSize = handler.getIntParameter(logAnalyseConstants.SPARK_STREAMING_SHUFFLE_BUFFILE_SIZE,
      logAnalyseConstants.SPARK_STREAMING_SHUFFLE_BUFFILE_SIZE_DEFAULT)
    //shuffle read 拉取数据的最大次数
    shuffleFetchDataTimes = handler.getIntParameter(logAnalyseConstants.SPARK_STREAMING_SHUFFLE_BUFFILE_MAXTRY,
      logAnalyseConstants.SPARK_STREAMING_SHUFFLE_BUFFILE_MAXTRY_DEFAULT)
    //shuffle read 拉取数据的间隔
    shuffleFecthDataInterval = handler.getIntParameter(logAnalyseConstants.SPARK_STREAMING_SHUFFLE_BUFFILE_FETCH_INTERVAL,
      logAnalyseConstants.SPARK_STREAMING_SHUFFLE_BUFFILE_FETCH_INTERVAL_DEFAULT)



    delayTimeForSecWork = handler.getIntParameter(logAnalyseConstants.SPARK_STREAMING_DELAY_TIMES_SECONDS_WORK, 180)




    //是否优雅的关闭spark程序
    graceStop = handler.getBooleanParameter(logAnalyseConstants.SPARK_CONFIG_STOP_GRACEFULLY, true)
    //spark 程序序列化方式
    serializerType = handler.getParameter(logAnalyseConstants.SPARK_CONFIG_SERIALIZER_TYPE,
      logAnalyseConstants.SPARK_CONFIG_SERIALIZER_TYPE_DEFAULT)

    //spark集群中hdfs的配置文件
    hdfsCoreCfgPath = handler.getParameter(logAnalyseConstants.SPARK_CONFIG_HDFS_CORE_CONFIG_PATH)
    if (hdfsCoreCfgPath == null) {
      throw new Exception("The hdfs core config file path must be set!!!")
    }
    offsetFileHdfsPath = handler.getParameter(logAnalyseConstants.SPARK_CONFIG_OFFSET_HDFS_SUFFIX)
    offsetFile = new FileOutPutOpeartor(logAnalyseConstants.OFFSET_FILE_NAME, offsetFileHdfsPath)

    offsetFileHdfsPathCopy = handler.getParameter(logAnalyseConstants.SPARK_CONFIG_OFFSET_HDFS_SUFFIX_COPY)
    offsetFileCopy = new FileOutPutOpeartor(logAnalyseConstants.OFFSET_FILE_NAME, offsetFileHdfsPathCopy)
    lastestOffset = offsetFile.readerOffset()

    offsetFileFlag = new FileOutPutOpeartor(logAnalyseConstants.MONITER_FLAG_FILE_NAME,
      logAnalyseConstants.MONITER_FLAG_FILE_PATH)
    modityTime = offsetFileFlag.checkUpdate()
    keyForFlag.setOldTime(modityTime)


    //超时无新数据更新状态保留的批次数目
    keepNoneStateTimes = handler.getIntParameter(logAnalyseConstants.SPARK_CONFIG_KEEP_NONE_STATE_TIMES
      , logAnalyseConstants.DATA_CONSTANTS_DEFAULT_KEEP_NONE_STATE_TIMES)

    hdfsPreStatForMin  = handler.getParameter(logAnalyseConstants.READ_DATA_FROM_HDFS_FOR_MIN_RDD_PATH)

    hdfsPreStatForHour = handler.getParameter(logAnalyseConstants.READ_DATA_FROM_HDFS_FOR_HOUR_RDD_PATH)

    hdfsPreStatForDay = handler.getParameter(logAnalyseConstants.READ_DATA_FROM_HDFS_FOR_DAY_RDD_PATH)

    if(hdfsPreStatForMin ==  null || hdfsPreStatForHour ==null || hdfsPreStatForDay ==null){
      throw new Exception("hdfs path is null for provinvce or sp or ps!!1")
    }

    hdfsPathMap += (logAnalyseConstants.RDD_FLAG_PACTERA_DAY -> hdfsPreStatForDay)
    hdfsPathMap +=(logAnalyseConstants.RDD_FLAG_PACTERA_MIN -> hdfsPreStatForMin )
    hdfsPathMap += (logAnalyseConstants.RDD_FLAG_PACTERA_HOUR -> hdfsPreStatForHour)

    //spark streaming updatestatebykey 分钟数据流 中间状态清除时间
    clearStateMinTTl = handler.getIntParameter(logAnalyseConstants.SPARK_SREAMING_KEEP_STATE_TTL_MIN,
      logAnalyseConstants.SPARK_STREAMING_KEEP_STATE_TTL_MIN_DEFAULT)

    //spark streaming updatestatebykey 小时数据流 中间状态清除时间
    clearStateHourTTl = handler.getIntParameter(logAnalyseConstants.SPARK_SREAMING_KEEP_STATE_TTL_HOUR,
      logAnalyseConstants.SPARK_STREAMING_KEEP_STATE_TTL_HOUR_DEFAULT)

    //spark streaming updatestatebykey 天数据流 中间状态清除时间
    clearStateDayTTl = handler.getIntParameter(logAnalyseConstants.SPARK_SREAMING_KEEP_STATE_TTL_DAY,
      logAnalyseConstants.SPARK_STREAMING_KEEP_STATE_TTL_DAY_DEFAULT)


    //redis数据库的ip地址
    redisHostName = handler.getParameter(logAnalyseConstants.REDIS_CONNECT_HOST_NAME,
      logAnalyseConstants.REDIS_CONNECT_HOST_NAME_DEFAULT_VALUE)

    //redis数据库的端口
    redisHostPort = handler.getIntParameter(logAnalyseConstants.REDIS_CONNECT_HOST_PORT,
      logAnalyseConstants.REDIS_CONNECT_HOST_PORT_DEFAULT_VALUE)

    //redis数据库使用的DBIndex
    val  redisDbIndexTmp  = handler.getParameter(logAnalyseConstants.REDIS_CONNECT_DB_INDEX,
      logAnalyseConstants.REDIS_CONNECT_DBINDEX_DEFAULT_VALUE).split(logAnalyseConstants.DATA_FIELD_SUB_SPACE)
    if(redisDbIndexTmp.length!= 3){
      throw new IllegalArgumentException("The drop rate threshold must be three valuse")
    }
    if(redisDbIndexTmp.length ==3){
      for(index <- redisDbIndexTmp){
        redisDbindexArr += index.toInt
      }
    }

    //允许的最大连接数
    redisMaxTotalConnects = handler.getIntParameter(logAnalyseConstants.REDIS_CONNECT_MAX_TOTAL_CONNECT,
      logAnalyseConstants.REDIS_CONNECT_MAX_TOTAL_CONNECT_DEFAULT_VALUE)

    //允许的最大空闲连接数
    redisMaxIdleConnects = handler.getIntParameter(logAnalyseConstants.REDIS_CONNECT_MAX_IDLE_CONNECT,
      logAnalyseConstants.REDIS_CONNECT_MAX_IDLE_CONNECT_DEFAULT_VALUE)

    //允许的最小空闲连接数
    redisMinIdleConnects = handler.getIntParameter(logAnalyseConstants.REDIS_CONNECT_MIN_IDLE_CONNECT,
      logAnalyseConstants.REDIS_CONNECT_MIN_IDLE_CONNECT_DEFAULT_VALUE)

    //空闲的链接多少时间关闭
    redisIdleConTimeOUt = handler.getIntParameter(logAnalyseConstants.REDIS_CONNECT_IDLE_CONNECT_TIMEOUT,
      logAnalyseConstants.REDIS_CONNECT_IDLE_TIMEOUT_DEFAULT_VALUE)

  //获取链接前检查是否检查链接有效
    redisCheckConnect = handler.getBooleanParameter(logAnalyseConstants.REDIS_CONNECT_CHECK_CONNECT_VALID,
      logAnalyseConstants.REDIS_CONNECT_CHECK_CONNECT_VALID_DEFAULT)

    //链接在释放会连接池是是否检查有效
    redisCheckReturnConn = handler.getBooleanParameter(logAnalyseConstants.REDIS_CONNECT_CHECK_RETURN_CONNECT_VALID,
      logAnalyseConstants.REDIS_CONNECT_CHECK_RETURN_CONNECT_VALID_DEFAULT)

    //连接耗尽时是否阻塞
    redisBlockExhaust = handler.getBooleanParameter(logAnalyseConstants.REDIS_CONNECT_BLOCK_EXHAUSTED,
      logAnalyseConstants.REDIS_CONNECT_REDIS_CONNECT_BLOCK_EXHAUSTED_DEFAULT)

    //在获取redis链接时的最大等待时间
    redisMaxWaitTime = handler.getLongParameter(logAnalyseConstants.REDIS_CONNECT_MAX_WAIT_TIME,
      logAnalyseConstants.REDIS_CONNECT_MAX_WAIT_TIME_DEFAULT_VALUE)


    redisParmData.setRedisHostName(redisHostName)
    redisParmData.setRedisHostPort(redisHostPort)
    redisParmData.setRedisMaxTotalConnects(redisMaxTotalConnects)
    redisParmData.setRedisMaxIdleConnects(redisMaxIdleConnects)
    redisParmData.setRedisMinIdleConnects(redisMinIdleConnects)
    redisParmData.setRedisIdleConTimeOUt(redisIdleConTimeOUt)
    redisParmData.setRedisCheckConnect(redisCheckConnect)
    redisParmData.setRedisCheckReturnConn(redisCheckReturnConn)
    redisParmData.setRedisBlockExhaust(redisBlockExhaust)
    redisParmData.setRedisMaxWaitTime(redisMaxWaitTime)

  }

  def initalSparkConf: SparkConf = {
    val sparkConf = new SparkConf().setAppName(sparkAppName)
    sparkConf.set(logAnalyseConstants.SPARK_CONFIG_MEMORY_STORAGE_FRACTION_PRAMA, memoryForStorageFraction)
    sparkConf.set(logAnalyseConstants.SPARK_CONFIG_EXECUTOR_MEMORY_FRACTION_PRAMA, memoryForShuffleFraction.toString)
    sparkConf.set(logAnalyseConstants.SPARK_CONFIG_MEMORY_USE_LEGACY_MODE_PRAMA, memoryLegacyMode)
    sparkConf.set(logAnalyseConstants.SPARK_STREAMING_RECEIVER_MAX_RATE_PRAMA, receiverMaxRate)
    sparkConf.set(logAnalyseConstants.SPARK_STREAMING_SHUFFLE_TYPE_PRAMA, shuffleType)
    sparkConf.set(logAnalyseConstants.SPARK_STREAMING_SHUFFLE_BUFFILE_SIZE_PRAMA, shuffleBuffSize.toString)
    sparkConf.set(logAnalyseConstants.SPARK_STREAMING_SHUFFLE_BUFFILE_MAXTRY_PRAMA, shuffleFetchDataTimes.toString)
    sparkConf.set(logAnalyseConstants.SPARK_STREAMING_SHUFFLE_BUFFILE_FETCH_INTERVAL_PRAMA, shuffleFecthDataInterval.toString)
    sparkConf.set(logAnalyseConstants.SPARK_CONFIG_STOP_GRACEFULLY_PRAMA, graceStop.toString)
    sparkConf.set(logAnalyseConstants.SPARK_CONFIG_SERIALIZER_TYPE_PRAMA, serializerType)

    sparkConf.registerKryoClasses(Array(classOf[PacteraForRLKey],classOf[PacteraForRLValue],classOf[RedisParmKey],
      classOf[PacteraDataHandler],classOf[UpdateStateByketHandler]))

  }

  def writeOffsetToHdfs(realTimeKafkaStream: InputDStream[(String, (String))]): Unit = {
    realTimeKafkaStream.foreachRDD(x => {
      val offsetArr = x.asInstanceOf[HasOffsetRanges].offsetRanges
      var arrStr: Array[String] = Array[String]()
      for (index <- offsetArr) {
        val elem = index.topic.concat(",").concat(index.partition.toString).concat(",").concat(index.fromOffset.toString).concat(",").concat(index.untilOffset.toString)
        arrStr = arrStr.:+(elem)
      }
      offsetFile.WrieOffsetToFile(arrStr)
      if (flagForTimer) {
        offsetFileCopy.WrieOffsetToFile(arrStr)
      }
    })
  }

  //创建sparkContext
  def createStreaming: StreamingContext = {

    //判断是否从HDFS初始化读取
    //HbaseConfiguration
    hadoopConf.addResource(new Path(hdfsCoreCfgPath))

    val checkpointExist = FileSystem.get(hadoopConf).exists(new Path(checkpoint))

    val ssc = new StreamingContext(initalSparkConf, Seconds(schedulingInterval))
    ssc.checkpoint(checkpoint)
    ssc.sparkContext.setLogLevel(setLogLevel)




    val broadcastForServerType = ssc.sparkContext.broadcast(redisParmData)
    val broadcastForPreStatRdd = ssc.sparkContext.broadcast(hdfsPathMap)


    //Kafka的参数设置
    val kafkaParams = Map[String, String](
      logAnalyseConstants.SPARK_STREAMING_KAFKA_BROKERS_PRAMA -> kafkabrokers,
      logAnalyseConstants.SPARK_STREAMING_KAFKA_OFFSET_PRAMA -> kafkaoffset
    )
    val realTimetopicsSet = realTimeKafkaTopics.split(logAnalyseConstants.DATA_FIELD_SUB_SPACE).toSet


    var offsetMap: Map[TopicAndPartition, Long] = Map[TopicAndPartition, Long]()
    if (lastestOffset.size() >= numRdd) {
      for (index <- 0 until numRdd) {
        val str = lastestOffset(index).split(",")
        offsetMap += (TopicAndPartition(str(0), str(1).trim.toInt) -> str(3).trim.toLong)
      }
    }




    //创建输入流
    val realTimeKafkaStream = if (checkpointExist || offsetMap.size != numRdd) {
      val stream = KafkaUtils.createDirectStream[String, String,
        StringDecoder, StringDecoder](ssc, kafkaParams, realTimetopicsSet)
      stream
    } else {
      val stream = KafkaUtils.createDirectStream[String, String,
        StringDecoder, StringDecoder, (String, String)](ssc, kafkaParams,
        offsetMap, (mmd: MessageAndMetadata[String, String]) => (mmd.key(), mmd.message()))
      stream
    }
    realTimeKafkaStream.foreachRDD(x => {
      flagForTimer = if (Math.abs((System.currentTimeMillis() / 1000) % 86400 - 57600) >= 0 && Math.abs((System.currentTimeMillis() / 1000) % 86400 - 57600) <= schedulingInterval) true
      else false
    })


    realTimeKafkaStream.foreachRDD(x => {
     if(Math.abs((System.currentTimeMillis()/1000 ) %300  -300) <= schedulingInterval){
       val mointerFileRead =  new FileOutPutOpeartor(logAnalyseConstants.MONITER_FLAG_FILE_NAME,logAnalyseConstants.MONITER_FLAG_FILE_PATH)
       val newTime = mointerFileRead.checkUpdate()
        if(newTime != keyForFlag.getOldTime){
           flagForTimerGraceStop = true
          keyForFlag.setOldTime(newTime)
        }
      }
    })

    val streamMediaHandler = new PacteraDataHandler(ssc, hadoopConf, numRdd,keepNoneStateTimes ,checkpointExist,broadcastForPreStatRdd)
    streamMediaHandler.handleStreamMediaStreaming(realTimeKafkaStream, broadcastForServerType, redisDbindexArr, delayTimeForSecWork,
      clearStateMinTTl, clearStateHourTTl,clearStateDayTTl,schedulingInterval)

    writeOffsetToHdfs(realTimeKafkaStream)
    ssc
  }

  def main(args: Array[String]) {

    //读取配置文件
    try {
      val log4jPath = new File(System.getProperty("user.dir"), "log4j.properties")
      PropertyConfigurator.configure(log4jPath.getAbsolutePath)
      initSparkConfgParam
    } catch {
      case ex: FileNotFoundException => logger.info("log4j.properties not found!!!")
        System.exit(1)
    }

    //程序启动
    try {
      //创建streamingContext
      val contextssc = StreamingContext.getOrCreate(checkpoint, () => {
        createStreaming
      })
      contextssc.sparkContext.setLogLevel(setLogLevel)
      contextssc.start()
      logger.info("start streaming success!")
      contextssc.awaitTermination()
    } catch {
      case ex: SparkException => {
        logger.info("catch SparkException !!")
        ex.printStackTrace()
      }
      case fx: FileNotFoundException => {
        logger.info("catch FileNotFoundException !!")
        fx.printStackTrace()
      }
    }

  }
}
