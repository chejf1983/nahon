/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nahon.drv.update;

import nahon.drv.data.UpdateFileHead;
import java.io.File;
import java.io.FileInputStream;
import java.util.concurrent.TimeUnit;
import nahon.comm.tool.convert.NahonConvert;
import nahon.drv.data.ICaddr;
import nahon.drv.entry.MIGPNahonDriver;
import static nahon.pro.migp.MIGPCOMMEM.FLASH_SPAN_LENGTH;

/**
 *
 * @author Administrator
 */
public class UpdateImple {

    private MIGPNahonDriver driver;
    private IProcess ret;
    private int writtenlen;
    private int totallen;

    public UpdateImple(MIGPNahonDriver drvinstance) {
        this.driver = drvinstance;
    }

    //升级文件
    public void UpdateFile(File updateFile, IProcess ret) throws Exception {
        if (updateFile == null) {
            throw new Exception("Invalude input parameter");
        }

        this.ret = ret;

        //获取输入文件流
        FileInputStream updatefileStream = new FileInputStream(updateFile);

        /*检查升级包的文件头*/
        UpdateFileHead fileHead = CheckFileHead(updatefileStream);

        //清零写入数据块数
        writtenlen = 0;
        //重置总共的需要的数据块
        totallen = (int) ((updateFile.length() - UpdateFileHead.BinFileHeadLength + FLASH_SPAN_LENGTH) / FLASH_SPAN_LENGTH);

        /* Halt Device 进入boot系统 */
        EnterIntoBoot();

        /* 检查IC大小 */
        ICaddr icInfo = CheckICBuffer(fileHead);

        //下发升级程序到不同的cpu ic地址
        for (int i = 0; i < fileHead.BinFileNumbier; i++) {
            LoadBinFileToDevice(icInfo.startaddr[i], icInfo.endaddr[i], updatefileStream);
        }

        //重启设备
        this.driver.StartApp(50);

    }

    //检查升级包文件头
    private UpdateFileHead CheckFileHead(FileInputStream updatefileStream) throws Exception {
        byte[] headbuffer = new byte[UpdateFileHead.BinFileHeadLength];
        updatefileStream.read(headbuffer);
        UpdateFileHead updateInfo = new UpdateFileHead(headbuffer);

        //检查魔术字
        if (updateInfo.magicnumber != UpdateFileHead.MagincNumber) {
            throw new Exception("Unknow update file!");
        }
        return updateInfo;
    }

    //进入boot系统
    private void EnterIntoBoot() throws Exception {
        //连续100此尝试进入boot系统，如果成功就跳出，否则异常
        for (int i = 0; i < 100; i++) {
            if (this.driver.HaltBoot(50)) {
                return;
            }
        }

        throw new Exception("Could not enter boot model. update failed!");
    }

    private ICaddr CheckICBuffer(UpdateFileHead fileHead) throws Exception {
        /* Get IC buffer Infomation */
        ICaddr icInfo = this.driver.GetICInfomation(1000);
        TimeUnit.MILLISECONDS.sleep(10);

        /* Check binfile number and device core number */
        if (fileHead.BinFileNumbier != icInfo.corenum) {
            throw new Exception("Update file not fit to this device. "
                    + "device has " + icInfo.corenum + "core."
                    + " update file has" + fileHead.BinFileNumbier + "bins");
        }

        int icbuffercount = 0;
        for (int i = 0; i < fileHead.BinFileNumbier; i++) {
            /* Check IC buffer size and File size */
            icbuffercount = (icInfo.endaddr[i] - icInfo.startaddr[i]) / FLASH_SPAN_LENGTH;
        }

        //ic个数暂时不检查
//        if (icbuffercount < filebuffercount) {
//            throw new Exception("Update file is too big, IC buffer could not be written");
//        }
        return icInfo;
    }

    private void LoadBinFileToDevice(int ic_start, int ic_end, FileInputStream updatefileStream) throws Exception {

        byte[] tmp;
        //读取升级包的长度，文件的头8个字节是数据长度
        tmp = new byte[8];
        updatefileStream.read(tmp);

//        long fileblocknumb = (long) ((NahonConvert.ByteArrayToLong(tmp, 0) + FLASH_SPAN_LENGTH - 1) / FLASH_SPAN_LENGTH);
//        int fileblocknumb = (int) (NahonConvert.ByteArrayToLong(tmp, 0) / (long) FLASH_SPAN_LENGTH);
        long fileleng = (long) NahonConvert.ByteArrayToLong(tmp, 0);

        //初始化块号
        tmp = new byte[FLASH_SPAN_LENGTH];
        int icstartindex = ic_start / FLASH_SPAN_LENGTH;
        int endblock = ic_end / FLASH_SPAN_LENGTH;

        //写完所有数据块
        while (fileleng > 0) {
            if (fileleng >= FLASH_SPAN_LENGTH) {
                //读取一片chip
                updatefileStream.read(tmp);
                fileleng -= FLASH_SPAN_LENGTH;
            } else {
                updatefileStream.read(tmp, 0, (int) fileleng);
                fileleng = 0;
            }

            this.driver.ClearFlash(icstartindex, 1000);
            
            //如果发送失败重发3次，否则发送失败
            for (int i = 0; i < 3; i++) {
                if (this.driver.WriteFlash(icstartindex, tmp, 1000)) {
                    icstartindex++;
                    if (this.totallen != 0) {
                        this.ret.SetPecent((float) this.writtenlen++ / this.totallen);
                    }
                    TimeUnit.MILLISECONDS.sleep(10);
                    break;
                } else if (i >= 3) {
                    throw new Exception("written ic failed");
                }
                TimeUnit.MILLISECONDS.sleep(10);
            }

            //超出IC块异常
            if (icstartindex >= endblock) {
                throw new Exception("binfile is exceed icbuffer size");
            }

            TimeUnit.MILLISECONDS.sleep(10);
        }

        //检查包的endmark
        tmp = new byte[4];
        updatefileStream.read(tmp);
        if (NahonConvert.ByteArrayToInteger(tmp, 0) != UpdateFileHead.FileEndMark) {
            throw new Exception("binfile endmark check failed");
        }
    }
        
}
