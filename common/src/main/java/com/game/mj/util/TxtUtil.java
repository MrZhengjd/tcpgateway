package com.game.mj.util;


import com.alibaba.fastjson.JSONObject;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class TxtUtil {
//    public static final String configPath0 = System.getProperty("user.dir")+File.separator+"src"+File.separator+"main"+File.separator+"resources"+File.separator;
//    private static ClassPathResource classPathResource = new ClassPathResource("src");
    public static JSONObject loadTxt(String fileName){
        ClassPathResource classPathResource = new ClassPathResource("json" + File.separator + fileName + ".txt");
//        String path = classPathResource.getPath() + File.separator + "json" + File.separator + fileName + ".txt";

        try {
            BufferedReader reader = new BufferedReader(new FileReader(classPathResource.getPath()));
            String json ="";
            StringBuilder sb = new StringBuilder();
            String line = reader.readLine();
            try {
                while (line != null) {
                    sb.append(line);
                    line = reader.readLine();
                }
                json = sb.toString();
            }catch (Exception e){
                try {
                    reader.close();
                }catch (Exception e1){
                    e1.printStackTrace();
                }
            } finally {
                try {
                    reader.close();
                }catch (Exception e1){
                    e1.printStackTrace();
                }
            }
            return JSONObject.parseObject(json);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("cannot read the txt");

        }

    }
}
