package com.nahon.lam.io;

/**
 * Created by Administrator on 2017/3/24.
 */


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import lam.faultcenter.FaultCenter;
import lam.faultcenter.SYSLOG;
import nahon.absractio.AbstractIO;
import nahon.absractio.IOInfo;

/**
 * @author jiche
 */
public class IO_TCP implements AbstractIO {

    private Socket mysocket;
    private OutputStream tcpout = null;
    private InputStream tcpin = null;

    private final String serverIp;
    private final int portNum;
    private boolean isClosed = true;

    public IO_TCP(String serverIp, int portnum) {
        this.serverIp = serverIp;
        this.portNum = portnum;
    }

    public IO_TCP(Socket socket) throws Exception {
        this.mysocket = socket;
        this.mysocket.setKeepAlive(true);
        this.tcpout = this.mysocket.getOutputStream();
        this.tcpin = this.mysocket.getInputStream();

//        this.serverIp = ((InetSocketAddress) this.mysocket.getRemoteSocketAddress()).getHostString();
        this.serverIp = ((InetSocketAddress) this.mysocket.getRemoteSocketAddress()).getHostName();
        this.portNum = ((InetSocketAddress) this.mysocket.getRemoteSocketAddress()).getPort();
        this.isClosed = false;
    }

    @Override
    public boolean IsClosed() {
        return this.isClosed;
    }

    @Override
    public void SendData(byte[] data) throws Exception {
        tcpout.write(data);
    }

    @Override
    public int ReceiveData(byte[] data, int timeout) throws Exception {
        this.mysocket.setSoTimeout(timeout);
        try {
            return tcpin.read(data);
        } catch (SocketTimeoutException ex) {
            return 0;
        }
    }

    @Override
    public IOInfo GetConnectInfo() {
        return new IOInfo("TCP", this.serverIp, String.valueOf(this.portNum));
    }

    @Override
    public void Open() throws Exception {
        if (this.IsClosed()) {
            this.mysocket = new Socket();
            this.mysocket.setKeepAlive(true);
            this.mysocket.connect(new InetSocketAddress(InetAddress.getByName(this.serverIp),
                    this.portNum), 1000);
            this.tcpout = this.mysocket.getOutputStream();
            this.tcpin = this.mysocket.getInputStream();
            isClosed = false;
        }
    }

    @Override
    public void Close() {
        if (!this.IsClosed()) {
            try {
                mysocket.close();
                tcpout.close();
                tcpin.close();
            } catch (IOException ex) {
                SYSLOG.getLog().PrintLog(Level.SEVERE, ex.toString());
            } finally {
                isClosed = true;
            }
        }
    }

}
