/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forge.bill.eia.builder;

import forge.bill.eia.saver.IEiaDB;
import forge.bill.eia.saver.ExcelHelper;
import forge.bill.eia.saver.SqlHelper;
import forge.bill.platform.ForgeSystem;
import static forge.bill.eia.builder.NahonDevMap.CompanyName;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Administrator
 */
public class EIABuilder {

    //读取数据库配置
    public void InitBuilder() throws Exception {
        this.isServerEnable = !ForgeSystem.GetInstance().systemConfig.IsLocalEiaDB();

        this.serverDB = new SqlHelper();
        this.serverDB.InitConfig();

        this.fileDB = new ExcelHelper();
        this.fileDB.InitConfig();
    }

    // <editor-fold defaultstate="collapsed" desc="配置接口">    
    private SqlHelper serverDB;

    public SqlHelper GetServerDB() {
        return this.serverDB;
    }

    private ExcelHelper fileDB;

    public ExcelHelper GetFileDB() {
        return this.fileDB;
    }

    private boolean isServerEnable = true;

    //使能服务器
    public void EnableServer(boolean value) {
        if (this.isServerEnable != value) {
            ForgeSystem.GetInstance().systemConfig.SetLocalEiaDB(!value);
            this.isServerEnable = value;
        }
    }

    public boolean IsServerEnable() {
        return this.isServerEnable;
    }
    // </editor-fold>     

    // <editor-fold defaultstate="collapsed" desc="备注记录更新">
    //创建新的序列号
    public String BuildSerialID(NahonDevMap.DevType devtype) throws Exception {
        //根据设备类型，获取设备编号
        String devnum = NahonDevMap.GetDevNum(devtype);
        if (devnum == null) {
            return "";
        }

        //选择数据库
        IEiaDB db = this.fileDB;
        if (this.isServerEnable) {
            //如果有服务器，选择服务器数据库
            db = this.serverDB;
        }

        //随机标识符
        int randomid = 0;
        try {
            db.Connect();
            //获取同类型设备，最后一条序列号
            String readLastestSerialNum = db.readLastestSerialNum(devtype.toString());
            if (readLastestSerialNum != null) {
                //获取最后一次的随机码
                randomid = Integer.valueOf(readLastestSerialNum.substring(10));
                while (true) {
                    //随机码+1
                    randomid++;

                    //超过6位，归零
                    if (randomid > 999999) {
                        randomid = 0;
                    }

                    //创建序列号 公司编号（3）设备编号（5）年份（2）随机码（6）
                    String tmp = CompanyName + devnum + new SimpleDateFormat("yy").format(new Date()) + String.format("%06d", randomid);
                    //查找新的序列号，是否有重复，如果有，继续增加随机码，如果没有，则返回。
                    if (db.readRecord(devtype.toString(), tmp) == null) {
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            //随机码获取失败，自动从0开始            
        } finally {
            db.DisConnectDB();
        }

        //创建序列号 公司编号（3）设备编号（5）年份（2）随机码（6）
        String sid = CompanyName + devnum + new SimpleDateFormat("yy").format(new Date()) + String.format("%06d", randomid);
        return sid;
    }

    //更新记录
    public void UpdateRecord(String oldserial, SEiaRecord newrecord) throws Exception {
        //如果新记录是空，报异常
        if (newrecord == null) {
            throw new Exception("没有记录！");
        }

        //选择数据库
        IEiaDB db = this.fileDB;
        if (this.isServerEnable) {
            //如果有服务器，选择服务器数据库
            db = this.serverDB;
        }

        //尝试连接服务器
        try {
            db.Connect();
        } catch (Exception ex) {
            throw new Exception("无法正常连接数据库服务器，本条记录无法保存!");
        }
        try {
            //检查序列号是否合法
            if (NahonDevMap.GetDevTypeBySerialNum(newrecord.devserialID) == null) {
                throw new Exception("不合法的序列号，请更换" + newrecord.devserialID);
            }

            //如果新旧序列号相同，更新原有内容
            if (oldserial.contentEquals(newrecord.devserialID)) {
                //如果新旧序列号相同，其它内容不同，则更新记录
                db.updateRecord(NahonDevMap.GetDevTypeBySerialNum(oldserial).toString(),
                        oldserial, newrecord);
            } else {
                //如果新旧序列号不同，删除旧记录，添加新记录

                //检查新的序列号，是否已经被使用过了
                if (this.ReadRecord(newrecord.devserialID) != null) {
                    throw new Exception("序列号已经使用过了，请更换序列号，或者自动生成序列号！");
                }

                //先删除旧记录，再增加新记录
                NahonDevMap.DevType devtype = NahonDevMap.GetDevTypeBySerialNum(oldserial);
                if (devtype != null) {
                    //删除记录
                    db.deleteRecord(devtype.toString(), oldserial);
                }

                //新增记录
                devtype = NahonDevMap.GetDevTypeBySerialNum(newrecord.devserialID);
                //创建数据表，如果表格不存在的话
                db.createTable(devtype.toString());
                db.addnewRecord(devtype.toString(), newrecord);
            }
        } finally {
            db.DisConnectDB();
        }
    }

    //新增记录
    public void AddNewRecord(SEiaRecord newrecord) throws Exception {
        //如果新记录是空，报异常
        if (newrecord == null) {
            throw new Exception("没有记录！");
        }

        //选择数据库
        IEiaDB db = this.fileDB;
        if (this.isServerEnable) {
            //如果有服务器，选择服务器数据库
            db = this.serverDB;
        }

        //尝试连接服务器
        try {
            db.Connect();
        } catch (Exception ex) {
            throw new Exception("无法正常连接数据库服务器，本条记录无法保存!");
        }

        try {
            //检查新的序列号，是否已经被使用过了
            if (this.ReadRecord(newrecord.devserialID) != null) {
                throw new Exception("序列号已经使用过了，请更换序列号，或者自动生成序列号！");
            }

            //检查序列号是否合法
            if (NahonDevMap.GetDevTypeBySerialNum(newrecord.devserialID) == null) {
                throw new Exception("不合法的序列号，请更换" + newrecord.devserialID);
            }

            //新增记录
            NahonDevMap.DevType devtype = NahonDevMap.GetDevTypeBySerialNum(newrecord.devserialID);
            //创建数据表，如果表格不存在的话
            db.createTable(devtype.toString());
            db.addnewRecord(devtype.toString(), newrecord);
        } finally {
            db.DisConnectDB();
        }
    }

    //读取记录
    public SEiaRecord ReadRecord(String buildserial) {
        //选择数据库
        IEiaDB db = this.fileDB;
        if (this.isServerEnable) {
            //如果有服务器，选择服务器数据库
            db = this.serverDB;
        }

        //找到对应的设备类型
        NahonDevMap.DevType devtype = NahonDevMap.GetDevTypeBySerialNum(buildserial);
        if (devtype == null) {
            return null;
        }

        try {
            db.Connect();
            //读取记录
            SEiaRecord readRecord = db.readRecord(devtype.toString(), buildserial);
            return readRecord;
        } catch (Exception ex) {
        } finally {
            try {
                db.DisConnectDB();
            } catch (Exception ex) {
            }
        }

        return null;
    }
    // </editor-fold> 
}
