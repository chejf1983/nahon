/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forge.bill.eia.saver;

import forge.bill.eia.builder.SEiaRecord;
import forge.bill.platform.ForgeSystem;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author jiche
 */
public class SqlHelper implements IEiaDB {

    protected Connection conn;

    public String ServerDBDriver = "com.mysql.jdbc.Driver";
    public String ServerDBConnectURL = "jdbc:mysql://192.168.1.110/nahon";
    public String s_user = "nahon";
    public String s_password = "nahon";

    protected final Lock dbLock = new ReentrantLock();

    public void InitConfig() {
        String[] pars = ForgeSystem.GetInstance().systemConfig.GetServerDBPar();
        this.ServerDBConnectURL = pars[0];
        this.s_user = pars[1];
        this.s_password = pars[2];
    }

    //设置数据库配置
    public void SetDBConfig(String url, String user, String password) {
        ForgeSystem.GetInstance().systemConfig.SetServeDBPar(url, user, password);
        this.ServerDBConnectURL = url;
        this.s_user = user;
        this.s_password = password;
        //this.DisConnectDB();
        //this.Connect();
    }

    @Override
    public void Connect() throws Exception {
        if (conn == null) {
            /* load db driver */
            Class.forName(ServerDBDriver);

            /* connect string, database name is data.h2db  username: nahon, password: nahong */
            conn = DriverManager.getConnection(ServerDBConnectURL, s_user, s_password);
        }
    }

    @Override
    public void DisConnectDB() throws Exception {
        if (conn != null && !conn.isClosed()) {
            conn.close();
            conn = null;
        }
    }

    private static final String DEVNAME = "DevName";
    private static final String SERNUM = "SerialNum";
    private static final String INFO1 = "Info1";
    private static final String INFO2 = "Info2";
    private static final String INFO3 = "Info3";
    private static final String TIME = "TIME";

    //创建数据表
    @Override
    public void createTable(String TableName) throws Exception {
        //创建数据表
        String CREATE_TABLE_SQL = "create table if not exists " + TableName
                + "(id int auto_increment primary key not null, "
                + DEVNAME + " varchar(100),"
                + SERNUM + " varchar(100),"
                + INFO1 + " varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci,"
                + INFO2 + " varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci,"
                + INFO3 + " varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci,"
                + TIME + " datetime(2))";
        /* Creat Device Table is not exist */
        conn.createStatement().executeUpdate(CREATE_TABLE_SQL);
    }

    //添加数据
    @Override
    public void addnewRecord(String TableName, SEiaRecord record) throws Exception {
        if (record == null) {
            return;
        }

        String INSERT_TABLE_SQL = "insert into " + TableName + " values(null, ?, ?, ?, null, null, ?)";

        PreparedStatement prepareCall = conn.prepareStatement(INSERT_TABLE_SQL);
        prepareCall.setString(1, record.devname);
        prepareCall.setString(2, record.devserialID);
        prepareCall.setString(3, record.company);
        prepareCall.setTimestamp(4, new java.sql.Timestamp(new Date().getTime()));

        Exception ret = null;
        dbLock.lock();
        try {
            prepareCall.execute();
        } catch (Exception ex) {
            ret = ex;
        } finally {
            dbLock.unlock();
        }

        if (ret != null) {
            throw ret;
        }
    }

    //根据序列号，查找记录
    @Override
    public SEiaRecord readRecord(String TableName, String devserialID) {
        try {
            String sql = "select * from " + TableName + " where " + SERNUM + " = ?";
            PreparedStatement prepareCall = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            prepareCall.setString(1, devserialID);
            ResultSet ret = prepareCall.executeQuery();

            SEiaRecord record = null;
            while (ret.next()) {
                record = this.convertToRecord(ret);
            }
            ret.close();
            return record;
        } catch (Exception ex) {
            return null;
        }
    }

    //更新记录
    @Override
    public void updateRecord(String TableName, String oldserialnum, SEiaRecord record) throws Exception {
        String sql = "update " + TableName + " set "
                + DEVNAME + " = ?, "
                + INFO1 + " = ? "
                + " where " + SERNUM + " = ? ";

        PreparedStatement pcall = conn.prepareStatement(sql);
        pcall.setString(1, record.devname);
        pcall.setString(2, record.company);
        pcall.setString(3, oldserialnum);
        dbLock.lock();
        try {
            pcall.executeUpdate();
        } catch (Exception ex) {
            throw ex;
        } finally {
            dbLock.unlock();
        }
//        this.deleteRecord(devserialID);
//        this.addnewRecord(record);
    }

    //删除记录
    @Override
    public void deleteRecord(String TableName, String devserialID) throws Exception {
        String sql = "delete from " + TableName + " where " + SERNUM + " = ? ";
        PreparedStatement prepareCall = conn.prepareStatement(sql);
        prepareCall.setString(1, devserialID);

        Exception ret = null;
        dbLock.lock();
        try {
            prepareCall.executeUpdate();
        } catch (Exception ex) {
            ret = ex;
        } finally {
            dbLock.unlock();
        }

        if (ret != null) {
            throw ret;
        }
    }

    //读取当前表最后一条记录
    @Override
    public String readLastestSerialNum(String TableName) throws Exception {
        String sql = "select " + SERNUM + " from " + TableName + " order by id desc";
        PreparedStatement prepareCall = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet ret = prepareCall.executeQuery();
        String snum = null;
        if (ret.next()) {
            snum = ret.getString(SERNUM);
        }
        ret.close();
        return snum;
    }

    //根据数据库记录，转换成eia记录
    private SEiaRecord convertToRecord(ResultSet ret) {
        try {
            return new SEiaRecord(ret.getString(DEVNAME), 
                    ret.getString(SERNUM), 
                    ret.getString(INFO1), 
                    ret.getString(INFO2) ,
                    ret.getString(INFO3));
        } catch (Exception ex) {
            return null;
        }
    }
}
