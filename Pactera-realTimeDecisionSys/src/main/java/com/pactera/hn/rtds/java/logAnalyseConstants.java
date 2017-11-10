package com.pactera.hn.rtds.java;

/**
 * 常量
 */
public class logAnalyseConstants {
    /**
     * 配置文件名
     */
    public static final String CONFIG_FILE_NAME = "PacteraRTDS.conf";
    public static final String OFFSET_FILE_NAME = "offset.txt";

    public static final String  MONITER_FLAG_FILE_NAME = "moniter.conf";


    /**
     * 数据存储key value
     */

    public static final int DATA_STORE_CONSTANTS_ALL = 1111;
    public static final String DATA_STORE_CONSTANTS_STRING_ALL = "all";
    public static final int DATA_STORE_CONSTANTS_ABROAD = -567;
    public static final int DATA_STORE_CONSTANTS_CHINA = 156;

    public static final String DATA_STORE_CONSTANTS_UNKNOWN = "unknown";
    public static final int DATA_STORE_CONSTANTS_UNKNOWN_DS = 99;
    public static final String DATA_STORE_CONSTANTS_OTHER = "other";
    public static final int DATA_CONSTANTS_FIVE_MIN_SECONDS = 300;
    public static final int DATA_CONSTANTS_ONE_HOUR_SECONDS = 3600;
    public static final int DATA_CONSTANTS_ONE_DAY_SECONDS = 86400;//TIME_INTERVAL_
    public static final int DATA_CONSTANTS_DEFAULT_AREA_LENGTH = 4;
    public static final double DATA_CONSTANTS_TIMESTAMP_LENGTH = 10;
    public static final int DATA_CONSTANTS_CENTER_NODE_MARK = 1;
    public static final double DATA_CONSTANTS_EDGE_NODE_MARK = 0.0;
    public static final String DATA_CONSTANTS_DEFAULT_MAXATE = "0";
    public static final int DATA_CONSTANTS_DEFAULT_VALUE = 0;
    public static final int DATA_CONTANTS_STATE_KEEP_TIMES = -9;
    public static final String SPARK_DELAY_TIME_FORMAT = "yyyy-MM-dd hhmm";
    public static final String DATA_MEDIA_STREAM_UUID_STR = "null";
    public static final int MAX_INT_VALUE = 2147483647;

    public static final long CONSTANTS_ALL_IP = 9;
    public static final String  CONSTANTS_ALL_STREAM_NAME = "specialflag_allstream";
    public static final String CONSTANTS_ALL_SP = "999";


    /**
     * 字符串操作符
     */
    public static final String DATA_FIELD_SUB_SPACE = ",";
    public static final String DATA_FIELD_OR_SPACE = "-";
    public static final String DATA_FIELD_SPACE = "_";
    public static final String NODE_PATH_SUB_SPACE = "/";
    public static final String STREAM_ELEM_DATA_SPLIT = "\t";
    public static final String AREA_CODE_SPLIT = "=";

    /**
     * redis相关的配置参数
     */
    public static final  String  REDIS_CONNECT_HOST_NAME = "spark.streaming.redis.hosts";
    public static final  String  REDIS_CONNECT_HOST_NAME_DEFAULT_VALUE = "localhost";
    public static final  String  REDIS_CONNECT_HOST_PORT = "spark.streaming.redis.hosts.port";
    public static final  int  REDIS_CONNECT_HOST_PORT_DEFAULT_VALUE = 6379;
    public static final  String  REDIS_CONNECT_DB_INDEX = "spark.streaming.redis.dbindex";
    public static final  String  REDIS_CONNECT_DBINDEX_DEFAULT_VALUE = "1,2,3";
    public static final  String  REDIS_CONNECT_MAX_TOTAL_CONNECT = "spark.streaming.redis.maxTotal.connects";
    public static final  int  REDIS_CONNECT_MAX_TOTAL_CONNECT_DEFAULT_VALUE = 300;
    public static final  String  REDIS_CONNECT_MAX_IDLE_CONNECT = "spark.streaming.redis.maxIdle.connects";
    public static final  int  REDIS_CONNECT_MAX_IDLE_CONNECT_DEFAULT_VALUE = 100;
    public static final  String  REDIS_CONNECT_MIN_IDLE_CONNECT = "spark.streaming.redis.minIdle.connects";
    public static final  int  REDIS_CONNECT_MIN_IDLE_CONNECT_DEFAULT_VALUE = 10;
    public static final  String REDIS_CONNECT_IDLE_CONNECT_TIMEOUT = "spark.streaming.redis.idle.connect.timeout";
    public static final  int REDIS_CONNECT_IDLE_TIMEOUT_DEFAULT_VALUE  = 0;
    public static final  String  REDIS_CONNECT_CHECK_CONNECT_VALID = "spark.streaming.redis.checkconnect.isValid";
    public static final boolean REDIS_CONNECT_CHECK_CONNECT_VALID_DEFAULT = false;
    public static final  String  REDIS_CONNECT_CHECK_RETURN_CONNECT_VALID = "spark.streaming.redis.checkReturn.isValid";
    public static final boolean REDIS_CONNECT_CHECK_RETURN_CONNECT_VALID_DEFAULT = false;
    public static final  String  REDIS_CONNECT_BLOCK_EXHAUSTED = "spark.streaming.redis.blockWhenExhaustest";
    public static final boolean REDIS_CONNECT_REDIS_CONNECT_BLOCK_EXHAUSTED_DEFAULT = true;
    public static final String REDIS_CONNECT_MAX_WAIT_TIME = "spark.streaming.redis.setMaxWaitTime";
    public static final  long REDIS_CONNECT_MAX_WAIT_TIME_DEFAULT_VALUE = -1l;


    /**
     * redis 相关配置项的key
     */
    public static final String  PACTERA_CARD_TYPE = "carType";
    public static final String PACTERA_MODULE = "module";
    public static final String PACTERA_CARD_NO = "cardNo";
    public static final String PACTERA_CARD_AUM = "cardAum";
    public static final String PACTERA_CUST_NO = "custNo";
    public static final String PACTERA_CARD_STATUS = "cardStatus";
    public static final String PACTERA_OPEN_DATE = "openDate";
    public static final String PACTERA_CONSUM_DATE = "conDate";
    public static final String PACTERA_CHANNEL_TYPE = "channelType";
    public static final String PACTERA_TOTAL_CONSUM_TIME = "totalConsumTimes";
    public static final String PACTERA_EVER_CONSUM_NUM = "conAmt";
    public static final String PACTERA_TOTAL_CONSUM_ACOUNT = "totalConsums";
    public static final String PACTERA_CONSUM_ACOUNT_DETAIL = "detailConsum";
    public static final String STATE_DATA_KEEP_TIMES  = "keepStateTimes";


    /**
     * 可靠性的相关配置参数
     */
    public static final  String READ_DATA_FROM_HDFS_FOR_MIN_RDD_PATH = "sparkConfig.readHdfsData.min.RDD.Path";
    public static final  String READ_DATA_FROM_HDFS_FOR_HOUR_RDD_PATH = "sparkConfig.readHdfsData.hour.RDD.Path";
    public static final  String READ_DATA_FROM_HDFS_FOR_DAY_RDD_PATH ="sparkConfig.readHdfsData.day.RDD.Path";

    public static final  String MONITER_FLAG_FILE_PATH = "sparkConfig.gracefully.stop.path";

    public static final String RDD_FLAG_PACTERA_DAY = "daySpRDD";
    public static final String RDD_FLAG_PACTERA_MIN = "minPSRDD";
    public static final String RDD_FLAG_PACTERA_HOUR = "hourPSRDD";



    /*
    * spark公共配置
    * */
    public static final String SPARK_CONFIG_SET_LOG_LEVEL = "spark.commons.logLevel";
    public static final String SPARK_CONFIG_SET_LOG_LEVEL_DEFAULT = "DEBUG";
    public static final String SPARK_CONFIG_APP_NAME = "spark.commons.appName";
    public static final String SPARK_CONFIG_APP_NAME_RTSC = "RTSC";
    public static final String SPARK_CONFIG_CHECK_POINT = "spark.commons.checkpoint";
    public static final String SPARK_CONFIG_CHECK_POINT_DEFAULT = "/rtsc/normal/checkpoint";
    public static final String SPARK_CONFIG_CLEARN_TTL = "spark.commons.clearnTTL";
    public static final String SPARK_CONFIG_CLEARN_TTL_DEALUT = "500";
    public static final String SPARK_CONFIG_CLEARN_TTL_PRAMA = "spark.cleaner.ttl";
    public static final String SPARK_CONFIG_NUM_RDD_PARTITIONS = "spark.commons.RDD.num";
    public static final int SPARK_CONFIG_NUM_RDD_PARTITIONS_DEFAULT = 4;
    public static final String SPARK_CONFIG_MASTER = "spark.commons.master";
    public static final String SPARK_CONFIG_MASTER_LOCAL = "local[*]";
    public static final String SPARK_CONFIG_STOP_GRACEFULLY = "spark.commons.gracefully.stop";
    public static final String SPARK_CONFIG_STOP_GRACEFULLY_PRAMA = "spark.streaming.stopGracefullyOnShutdown";
    public static final String SPARK_CONFIG_KEEP_NONE_STATE_TIMES = "sparkConfig.keepState.times";
    public static final int DATA_CONSTANTS_DEFAULT_KEEP_NONE_STATE_TIMES = 5;
    public static final String SPARK_CONFIG_SERIALIZER_TYPE = "spark.commons.serializer";
    public static final String SPARK_CONFIG_SERIALIZER_TYPE_DEFAULT = "org.apache.spark.serializer.JavaSerializer";
    public static final String SPARK_CONFIG_SERIALIZER_TYPE_PRAMA = "spark.serializer";

    public static final String SPARK_CONFIG_STORAGE_FRACTION = "spark.commons.storageFraction";
    public static final double SPARK_CONFIG_STORAGE_FRACTION_DEFAULT = 0.5;
    public static final String SPARK_CONFIG_STORAGE_FRACTION_PARMA = "spark.memory.storageFraction";
    public static final String SPARK_CONFIG_EXECUTOR_MEMORY_FRACTION = "spark.commons.shuffle.storageFraction";
    public static final double SPARK_CONFIG_EXECUTOR_MEMORY_FRACTION_DEFAULT = 0.4;
    public static final String SPARK_CONFIG_EXECUTOR_MEMORY_FRACTION_PRAMA = "spark.shuffle.memoryFraction";


    public static final String SPARK_CONFIG_MEMORY_STORAGE_FRACTION = "spark.commons.memory.storage.fraction";
    public static final String SPARK_CONFIG_MEMORY_STORAGE_FRACTION_PRAMA = "spark.storage.memoryFraction";
    public static final String SPARK_CONFIG_MEMORY_USE_LEGACY_MODE = "spark.commons.memory.legacy.mode";
    public static final String SPARK_CONFIG_MEMORY_USE_LEGACY_MODE_DEFAULT = "false";
    public static final String SPARK_CONFIG_MEMORY_USE_LEGACY_MODE_PRAMA = "spark.memory.useLegacyMode";
    public static final String SPARK_CONFIG_OFFSET_HDFS_SUFFIX = "sparkConfig.offset.hdfs.path.suffix";
    public static final String SPARK_CONFIG_OFFSET_HDFS_SUFFIX_COPY = "sparkConfig.offset.hdfs.path.suffix.copy";
    public static final String SPARK_CONFIG_AKKA_FRAME_SIZE = "spark.commons.akka.frame.size";
    public static final int SPARK_CONFIG_AKKA_FRAME_SIZE_DEFAULT = 150;
    public static final String SPARK_CONFIG_AKKA_FRAME_SIZE_PRAMA = "spark.akka.frameSize";
    public static final String SPARK_CONFIG_AKKA_NUM_THREADS = "spark.commons.akka.thread.num";
    public static final int SPARK_CONFIG_AKKA_NUM_THREADS_DEFAULT = 4;
    public static final String SPARK_CONFIG_AKKA_NUM_THREADS_PRAMA = "spark.akka.threads";
    public static final String SPARK_CONFIG_NETWORK_TIMEOUT = "spark.commons.network.timeOut";
    public static final int SPARK_CONFIG_NETWORK_TIMEOUT_DEFAULT = 150;
    public static final String SPARK_CONFIG_NETWORK_TIMEOUT_PRAMA = "spark.network.timeout";
    public static final String SPARK_CONFIG_AKKA_TIMEOUT = "spark.akka.timeout";
    public static final int SPARK_CONFIG_AKKA_TIMEOUT_DEFAULT = 150;
    public static final String SPARK_CONFIG_AKKA_TIMEOUT_PRAMA = "spark.akka.timeout";




    public static final String SPARK_CONFIG_ZOOKEEPER_HOST_PRAMA = "hbase.zookeeper.quorum";
    public static final String SPARK_CONFIG_ZOOKEEPER_PORT_PRAMA = "hbase.zookeeper.property.clientPort";
    /*
    * stream配置
    * */
    public static final String SPARK_STREAMING_SCHEDUL_INTERVAL = "saprk.streaming.schedulingInterval";
    public static final int SPARK_STREAMING_SCHEDUL_INTERVAL_DEFAULT = 5;
    public static final String SPARK_STREAMING_RECEIVER_MAX_RATE = "spark.streaming.receiverMaxRate";
    public static final String SPARK_STREAMING_RECEIVER_MAX_RATE_PRAMA = "spark.streaming.kafka.maxRatePerPartition";
    public static final String SPARK_STREAMING_RECEIVER_MAX_RATE_DEFAULT = "0";
    public static final String SPARK_SREAMING_KEEP_STATE_TTL_MIN = "spark.streaming.keep.state.min.times";
    public static final int SPARK_STREAMING_KEEP_STATE_TTL_MIN_DEFAULT = 15;
    public static final String SPARK_SREAMING_KEEP_STATE_TTL_HOUR = "spark.streaming.keep.state.hour.times";
    public static final int SPARK_STREAMING_KEEP_STATE_TTL_HOUR_DEFAULT = 15;
    public static final String SPARK_SREAMING_KEEP_STATE_TTL_DAY = "spark.streaming.keep.state.day.times";
    public static final int SPARK_STREAMING_KEEP_STATE_TTL_DAY_DEFAULT = 15;


    public static final String SPARK_STREAMING_SHUFFLE_TYPE = "spark.streaming.shuffle.type";
    public static final String SPARK_STREAMING_SHUFFLE_TYPE_PRAMA = "spark.shuffle.manager";
    public static final String SPARK_STREAMING_SHUFFLE_TYPE_DEFAULT = "sort";
    public static final String SPARK_STREAMING_SHUFFLE_BUFFILE_SIZE = "spark.streaming.shuffle.file.buffer.size";
    public static final int SPARK_STREAMING_SHUFFLE_BUFFILE_SIZE_DEFAULT = 32;
    public static final String SPARK_STREAMING_SHUFFLE_BUFFILE_SIZE_PRAMA = "spark.shuffle.file.buffer";
    public static final String SPARK_STREAMING_SHUFFLE_BUFFILE_MAXTRY = "spark.streaming.shuffle.fetchData.times";
    public static final int SPARK_STREAMING_SHUFFLE_BUFFILE_MAXTRY_DEFAULT = 3;
    public static final String SPARK_STREAMING_SHUFFLE_BUFFILE_MAXTRY_PRAMA = "spark.shuffle.io.maxRetries";
    public static final String SPARK_STREAMING_SHUFFLE_BUFFILE_FETCH_INTERVAL = "spark.streaming.shuffle.fetchData.interval";
    public static final int SPARK_STREAMING_SHUFFLE_BUFFILE_FETCH_INTERVAL_DEFAULT = 5;
    public static final String SPARK_STREAMING_SHUFFLE_BUFFILE_FETCH_INTERVAL_PRAMA = "spark.shuffle.io.retryWait";
    public static final String SPARK_STREAMING_EXXECUTOR_HEARTTIME = "spark.streming.executor.heartTime.driver";
    public static final int SPARK_STREAMING_EXXECUTOR_HEARTTIME_DEFAULT = 10;
    public static final String SPARK_STREAMING_EXXECUTOR_HEARTTIME_PRAMA = "spark.executor.heartbeatInterval";
    public static final String SPARK_CONFIG_HDFS_CORE_CONFIG_PATH = "sparkConfig.hadoop.hdfs.core-site.xml.path";

    public static final String SPARK_STREAMING_CONNECT_HBASE = "spark.streaming.hbase.hosts";
    public static final String SPARK_STREAMING_CONNECT_HBASE_PRAMA = "spark.hbase.host";
    public static final String SPARK_STREAMING_CONNECT_HBASE_PORT = "spark.streaming.hbase.hosts.port";
    public static final int SPARK_STREAMING_CONNECT_HBASE_PORT_DEFAULT = 10060;
    public static final String SPARK_STREAMING_CONNECT_HBASE_PORT_PRAMA = "spark.hbase.port";


    public static final String SPARK_STREAMING_HBASE_CORE_FILE_NAME = "spark.streaming.hbase.core.file.path";

    public static final String SPARK_STREAMING_HBASE_BATCH_SIZE = "spark.streaming.hbase.batch.size";

    public static final String SPARK_STREAMING_DELAY_TIMES_SECONDS_WORK = "spark.streaming.delay.times.seconds.work";
    public static final String SPARK_STREAMING_DELAY_TIMES_THIRD_WORK ="spark.streaming.delay.times.third.work";


    /**
     * kafka的相关配置参数
     */
    public static final String SPARK_STREAMING_KAFKA_REALTIME_TOPICS = "spark.streaming.realTime.kafkaTopics";
    public static final String SPARK_STREAMING_KAFKA_DELAY_TOPICS = "spark.streaming.delay.kafkaTopics";
    public static final String SPARK_STREAMING_KAFKA_BROKERS = "spark.streaming.kafkaBrokers";
    public static final String SPARK_STREAMING_KAFKA_BROKERS_PRAMA = "metadata.broker.list";
    public static final String SPARK_STREAMING_KAFKA_OFFSET = "spark.streaming.kafka.offsets";
    public static final String SPARK_STREAMING_KAFKA_OFFSET_PRAMA = "auto.offset.reset";
    public static final String SPARK_STREAMING_KAFKA_OFFSET_DEFAULT = "largest";


}