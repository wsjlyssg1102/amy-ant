import com.pactera.hn.rtds.java.keyValue.FlagKey;
import com.pactera.hn.rtds.java.untils.FileOutPutOpeartor;

/**
 * Created by User on 3/11/2017.
 */
public class ModiyTest {
    public static void main(String [] ages) throws  Exception{
//        FlagKey key = new FlagKey();
//        FileOutPutOpeartor fileOutPutOpeartor = new FileOutPutOpeartor("moniter.conf","hdfs://spark130/data");
//        long time = fileOutPutOpeartor.checkUpdate();
//        key.setOldTime(time);
//        FileOutPutOpeartor fileOutPutOpeartor1 = new FileOutPutOpeartor("moniter.conf","hdfs://spark130/data");
//        long time1 = fileOutPutOpeartor1.checkUpdate();
//        if(key.getOldTime() != time1){
//            System.out.println("fuck");
//        }
//        System.out.println(time1);
//        System.out.println(time);
        String str1 = "select * from jiang ";
        System.out.println(str1.length());
        System.out.println(str1.trim().length());
    }
}
