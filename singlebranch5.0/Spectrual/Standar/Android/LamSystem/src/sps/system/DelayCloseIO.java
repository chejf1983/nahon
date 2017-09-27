/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sps.system;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import lam.faultcenter.SYSLOG;
import nahon.absractio.AbstractIO;
import nahon.absractio.IOInfo;

/**
 *
 * @author jiche
 */
public class DelayCloseIO implements AbstractIO {
    private int lagtime = 500;
    private final AbstractIO io_instance;
    private int user = 0;
    private final Lock iolock = new ReentrantLock();

    public DelayCloseIO(AbstractIO io_instance) {
        this.io_instance = io_instance;
    }

    @Override
    public boolean IsClosed() {
        return this.user == 0;
    }

    @Override
    public void Open() throws Exception {
        this.iolock.lock();
        try {
            if (this.io_instance.IsClosed()) {
                this.io_instance.Open();
            }
            this.user++;
        } finally {
            this.iolock.unlock();
        }
    }

    @Override
    public void Close() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.MILLISECONDS.sleep(lagtime);
                } catch (InterruptedException ex) {
                    SYSLOG.getLog().PrintLog(Level.SEVERE, ex.getMessage());
                }

                iolock.lock();
                try {
                    user--;                    
                    if(IsClosed()){
                        io_instance.Close();
                    }
                } finally {
                    iolock.unlock();
                }
            }
        }).start();
    }

    @Override
    public void SendData(byte[] bytes) throws Exception {
        this.io_instance.SendData(bytes);
    }

    @Override
    public int ReceiveData(byte[] bytes, int timeout) throws Exception {
        return this.io_instance.ReceiveData(bytes, timeout);
    }

    @Override
    public IOInfo GetConnectInfo() {
        return this.io_instance.GetConnectInfo();
    }
}
