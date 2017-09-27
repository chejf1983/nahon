/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forge.bill.binfactory;

import forge.bill.platform.ForgeSystem;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import nahon.comm.tool.convert.MyConvert;
import nahon.drv.data.UpdateFileHead;

/**
 *
 * @author jiche 升级文件格式
 *
 * UpdateFileHead
 *
 * file1-length
 *
 * file1
 *
 * file end mark
 *
 * file2 length
 *
 * file2
 *
 * file end mark
 *
 */
public class UpdateFileBuilder {
    
    // <editor-fold defaultstate="collapsed" desc="文件压缩控制接口">  
    //文件制作状态
    public enum BinMakeState {
        Process,
        Success,
        Failed,
        Cancel
    };
    
    private BinMakeState state = BinMakeState.Success;
    //总共的文件大小（以FLASH_SPAN_LENGTH为单位）
    private long totalsize = 0;
    //完成大小
    private long writensize = 0;
    //制作进程
    private Future<?> ret = null;
    //最后一次异常
    private Exception lastexception;
    
    /**
     * 压缩文件
     *
     * @param input
     * @param output
     * @param fileHead
     * @return
     */
    
    public void CompressBinFiles(final File[] input, final File output, final UpdateFileHead fileHead) throws Exception {
        //如果正在压缩升级包，抛出异常
        if (this.GetState() == BinMakeState.Process) {
            throw new Exception("Files is under compressing, please wait...");
        }
        //设置开始压缩状态
        this.setState(BinMakeState.Process);

        //入参检查
        if (!this.checkInputPar(input, output, fileHead)) {
            this.setState(BinMakeState.Failed);
            return;
        }

        //统计总共文件大小(以一次缓存大小为单位)
        for (File f : input) {
            this.totalsize += (f.length() - 1 + bufferlen) / bufferlen;
        }

        //触发压缩进程
        ret = ForgeSystem.GetInstance().systemthreadpool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    //创建压缩文件
                    startcompressfiles(input, output, fileHead);
                    setState(BinMakeState.Success);
                } catch (Exception ex) {
                    lastexception = ex;
                    setState(BinMakeState.Failed);
                }
            }
        });
    }

    //设置当前工作状态
    private void setState(BinMakeState state) {
        this.state = state;
    }

    //获取当前工作状态
    public BinMakeState GetState() {
        BinMakeState tstate = this.state;
        return tstate;
    }

    //获取工作进度（0~100）
    public int GetProcessPecent() {
        //在工作状态下，计算工作百分比
        if (this.GetState() == BinMakeState.Process) {
            if (totalsize == 0) {
                return 0;
            }
            return (int) (writensize * 100 / totalsize);
        } else {
            return 0;
        }
    }

    //获取最近的一次异常
    public Exception GetLastException() {
        return this.lastexception;
    }

    //取消制作
    public void Cancel() {
        if (this.GetState() == BinMakeState.Process) {
            this.setState(BinMakeState.Cancel);
            while (!this.ret.isDone()) {
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException ex) {
                }
            }
        }
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="文件压缩接口">  
    //一次读取缓存大小
    private int bufferlen = 2048;
    
    //检查输入参数
    private boolean checkInputPar(final File[] input, final File output, final UpdateFileHead fileHead) {
        //如果原始文件为空，报错
        if (input == null || input.length == 0) {
            lastexception = new Exception("input bin failed");
            return false;
        }

        //没有输出文件，报错
        if (output == null) {
            lastexception = new Exception("output file is null");
            return false;
        }

        //输入文件不能等于输出文件
        for (File ifile : input) {
            if (ifile.getAbsolutePath().contentEquals(output.getAbsolutePath())) {
                lastexception = new Exception("can't use input bin as output binfile");
                return false;
            }
        }
        return true;
    }

    /**
     * 压缩文件
     */
    private void startcompressfiles(File[] inputBinFiles, File output, UpdateFileHead fileHead) throws Exception {
        /* Update File Header parameter */
        fileHead.BinFileNumbier = inputBinFiles.length;

        /* Clean output file */
        FileOutputStream outStream = new FileOutputStream(output);
        outStream.flush();

        /* input file head to new update file bin */
        outStream.write(fileHead.ToBytesArray());

        /* input all bin file to update file bin */
        for (int i = 0; i < inputBinFiles.length; i++) {
            compressfile(outStream, inputBinFiles[i]);
        }

        /* Close update file */
        outStream.close();
    }

    //压缩一个文件
    private void compressfile(FileOutputStream outStream, File srcfile) throws Exception {
        /* input file length at firtst */
        outStream.write(MyConvert.LongToByteArray(srcfile.length()));

        //创建读取文件流
        FileInputStream inputStream = new FileInputStream(srcfile);

        //如果输入可读，同时当前在工作状态下
        while (inputStream.available() != 0 && this.GetState() == BinMakeState.Process) {
            //创建一个空白buffer
            byte[] tmp = new byte[bufferlen];
            //读取数据，不足一个扇区，末尾添0
            int readlen = inputStream.read(tmp);

            if (readlen == bufferlen) {
                //写数据
                outStream.write(tmp);
            } else {
                outStream.write(tmp, 0, readlen);
            }
            //增加完成度
            writensize += 1;
        }

        //关闭读取文件流
        inputStream.close();

        /* stamp file endmark at last */
        outStream.write(MyConvert.IntegerToByteArray(UpdateFileHead.FileEndMark));
    }
    // </editor-fold>
}
