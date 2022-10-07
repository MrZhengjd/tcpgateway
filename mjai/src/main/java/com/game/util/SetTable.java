package com.game.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author zheng
 */
public class SetTable {
    private HashMap<Integer,Boolean> m_tbl = new HashMap<>();
    public boolean check(Integer number){
        return m_tbl.containsKey(number);
    }
    public void add(int key){
        if (m_tbl.containsKey(key)){
            return;
        }
        m_tbl.put(key,true);
    }

    public void dump(String name){
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(name));
            Iterator<Map.Entry<Integer, Boolean>> iterator = m_tbl.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry<Integer, Boolean> next = iterator.next();
                bw.write(next.getKey()+"");
                bw.newLine();
            }
            bw.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void load(String path){
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(path));
            String line = "";
            while ((line = bufferedReader.readLine()) != null){
                m_tbl.put(Integer.parseInt(line),true);
            }
            bufferedReader.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
