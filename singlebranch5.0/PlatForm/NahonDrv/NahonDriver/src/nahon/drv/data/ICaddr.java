/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nahon.drv.data;

/**
 *
 * @author jiche
 */
public class ICaddr {

    public ICaddr(int corenum){
        this.startaddr = new int[corenum];
        this.endaddr = new int[corenum];
        this.corenum = corenum;
    }
    
    public int corenum;
    public int startaddr[];
    public int endaddr[];
    public static final int ICaddr_Length = 8;
    public static final int MaxInt = 0xEFFFFFFF;
}
