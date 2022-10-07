package com.game.index.gentable;

import com.game.index.Constants;
import com.game.util.SetTable;
import org.springframework.core.io.ClassPathResource;

/**
 * @author zheng
 */
public class TableMgr {
    private static ClassPathResource classPathResource = new ClassPathResource("data");
    public static TableMgr mgr = new TableMgr();
    //eye(将）
    public SetTable[] check_table = new SetTable[9];
    public SetTable[] check_table_eye = new SetTable[9];
    public SetTable[] feng_table_eye = new SetTable[9];
    public SetTable[] feng_table = new SetTable[9];
//    public SetTable[] hua_table = new SetTable[8];

    private TableMgr() {
        for (int i = 0;i< 9 ;i++){
            init(check_table,i);
            init(check_table_eye,i);
            init(feng_table_eye,i);
            init(feng_table,i);
        }
    }

    public static TableMgr getInstance(){
        return mgr;
    }
    private void init(SetTable[] tables,int index){
        if (index <= tables.length-1){
            tables[index] = new SetTable();
        }
    }
    private SetTable getByKey(boolean chi,boolean eye,int gui_num){
        SetTable table;
        if (chi){
            if (eye){
                table= check_table_eye[gui_num];
            }else {
                table = check_table[gui_num];
            }
        }else {
            if (eye){
                table = feng_table_eye[gui_num];
            }else {
                table = feng_table[gui_num];
            }
        }
        return table;
    }
    public void add(int key,int gui_num,boolean eye,boolean chi){
        getByKey(chi,eye,gui_num).add(key);
    }
    public boolean check(int key,int gui_num,boolean eye,boolean chi){

        return getByKey(chi,eye,gui_num).check(key);
    }
    private void loadWithName(String path,SetTable[] check_table){
        for (int i = 0;i < 9;++i){
            check_table[i].load(classPathResource.getPath()+"/"+path+i+".tbl");
        }
    }
    public boolean load(){
        loadWithName(Constants.checkTableName,check_table);
        loadWithName(Constants.checkTableEyeName,check_table_eye);
        loadWithName(Constants.fengTableName,feng_table);
        loadWithName(Constants.fengTableEyeName,feng_table_eye);
        return true;
    }
    private void dumpTableWithName(String path,SetTable[] check_table){
        for (int i = 0;i < 9;++i){
            check_table[i].dump(classPathResource.getPath()+"/"+path+i+".tbl");
        }
    }
    public boolean dump_table(){
        dumpTableWithName(Constants.checkTableName,check_table);
        dumpTableWithName(Constants.checkTableEyeName,check_table_eye);
        return true;
    }

    public boolean dump_feng_table(){
        dumpTableWithName(Constants.fengTableName,feng_table);
        dumpTableWithName(Constants.fengTableEyeName,feng_table_eye);
        return true;
    }
}
