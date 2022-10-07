package com.game.mj.util;




import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/***
 * PropertiesUtil
 */
public class PropertiesUtil {
    private String properiesName;
    public PropertiesUtil(String properiesName){
        this.properiesName = properiesName;
    }

    public String getPropery(String key){
        Properties prop = new Properties();
        InputStream is = null;
        try{
            is = Thread.currentThread().getClass().getClassLoader().getResourceAsStream(this.properiesName);
            prop.load(is);
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            try{
                is.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        return prop.getProperty(key);
    }

//    public static void main(String[] args){
//        PropertiesUtil propertiesUtil = new PropertiesUtil(PropertiesConfig.icuProperties);
//        String demo = propertiesUtil.getPropery("timer.hashed.wheel.producer.task");
//        System.out.println(demo);
//    }
}
