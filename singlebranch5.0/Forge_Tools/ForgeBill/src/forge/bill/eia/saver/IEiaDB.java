/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forge.bill.eia.saver;

import forge.bill.eia.builder.SEiaRecord;

/**
 *
 * @author Administrator
 */
public interface IEiaDB {

    public void Connect() throws Exception ;

    public void DisConnectDB() throws Exception ;

    //读取最后一个序列号
    public String readLastestSerialNum(String devType) throws Exception ;
    
    //创建表格
    public void createTable(String TableName) throws Exception ;
    
    //读取记录
    public SEiaRecord readRecord(String devType, String serial);
   
    //更新记录
    public void updateRecord(String devType, String oldserial, SEiaRecord newrecord) throws Exception ;

    //删除记录
    public void deleteRecord(String devType, String oldserial) throws Exception ;

    //添加新记录
    public void addnewRecord(String devType, SEiaRecord newrecord) throws Exception ;
    
}
