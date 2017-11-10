import com.pactera.hn.rtds.java.keyValue.{PacteraForRLKey, PacteraForRLValue}
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path, PathFilter}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by User on 3/11/2017.
  */
object RddReaderTest {

  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setAppName("ame").setMaster("local[*]")

    val sc = new SparkContext(sparkConf)

    sc.setLogLevel("OFF")
    val rdd = sc.objectFile[(PacteraForRLKey, (PacteraForRLValue, Boolean, Int))]("hdfs://spark130/data/checkpointTest/PacteraDay/part-00000")
     rdd.foreach(x=>println(x._1.getCardAum+"-->"+x._2._3))
  }

  def createRdd(ssc: SparkContext, hdfsPathPrefix: String, numRDD: Int, conf: Configuration, keepStateTimes: Int):RDD[(PacteraForRLKey, (PacteraForRLValue, Boolean, Int))]={

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
      finalRDD= ssc.makeRDD(Seq((elem._1,(elem._2,false,keepStateTimes))),numRDD)
      seqRdd = seqRdd.+:(finalRDD)
    }else{
      for (index <- 0 until hdfs.length) {
        val rdd = ssc.objectFile[(PacteraForRLKey, (PacteraForRLValue, Boolean, Int))](hdfs(index).getPath.toString.trim,numRDD)
        seqRdd = seqRdd.+:(rdd)
      }
    }

    val  resultRdd = ssc.union(seqRdd)

    resultRdd

  }

  def createNoneElemRdd(ssc:SparkContext,keepStateTimes:Int,numRDD:Int):RDD[(PacteraForRLKey, (PacteraForRLValue, Boolean, Int))]={
    val elem = createNoneElems()
    val rdd = ssc.makeRDD(Seq((elem._1,(elem._2,false,keepStateTimes))),numRDD)
    rdd

  }

  def createNoneElems():(PacteraForRLKey, PacteraForRLValue)={
    val key = new PacteraForRLKey("null", "null", "null", 0l, "null", "null", "null", 0l, "null")
    val detalList = new java.util.ArrayList[java.lang.Long]()
    val value = new PacteraForRLValue(0l, 0, detalList)
    (key,value)
  }

}
