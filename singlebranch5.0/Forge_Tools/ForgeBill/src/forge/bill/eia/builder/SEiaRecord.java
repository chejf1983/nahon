/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forge.bill.eia.builder;

/**
 *
 * @author jiche
 */
public class SEiaRecord {

    public static String[] names = {"设备名称", "序列号", "客户", "备注1", "备注2"};

    public String devname;
    public String devserialID;
    public String company;
    public String info2 = "";
    public String info3 = "";

    public SEiaRecord(String devname, String devserialID, String company, String info1, String info2) {
        this.devname = devname;
        this.devserialID = devserialID;
        this.company = company;
        this.info2 = info1;
        this.info3 = info2;
    }

    public boolean Equalto(SEiaRecord rec) {
        if(rec == null){
            return false;
        }
        
        return this.devname.contentEquals(rec.devname)
                && this.devserialID.contentEquals(rec.devserialID)
                && this.company.contentEquals(rec.company);
    }

    public SEiaRecord(SEiaRecord copy) {
        this.update(copy);
    }
    
    public void update(SEiaRecord copy){
        this.devname = copy.devname;
        this.devserialID = copy.devserialID;
        this.company = copy.company;    
        this.info2 = copy.info2;
        this.info3 = copy.info3;    
    }
}
