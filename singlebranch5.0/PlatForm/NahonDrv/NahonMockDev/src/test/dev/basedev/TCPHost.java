/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.dev.basedev;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import nahon.comm.io.libs.IO_TCP;
import nahon.comm.tool.convert.MyConvert;
import nahon.comm.tool.convert.MyConvertException;
import nahon.pro.migp.MIGPCOMMEM;
import nahon.pro.migp.MIGPNode;
import nahon.pro.migp.MIGPPacket;

/**
 *
 * @author Administrator
 */
public class TCPHost implements Runnable {

    private ServerSocket tcpServer;
    private boolean isClosed = true;
    private ExecutorService tcpThread;
    private MockDev dev;

    /**
     * Listen IP Server
     *
     * @param serverIp
     * @param portnum
     * @throws IOException
     */
    public void Listen(String serverIp, int portnum, MockDev dev) throws IOException {
        if (this.IsClosed()) {
            /* Change state */
            this.isClosed = false;

            this.dev = dev;

            /* Start Listen Thread */
            this.tcpServer = new ServerSocket(portnum, 20, InetAddress.getByName(serverIp));
            tcpThread = Executors.newCachedThreadPool();
            tcpThread.submit(this);
        }
    }

    /**
     * CloseIO IP Server
     *
     * @throws IOException
     */
    public void Close() {
        if (!this.IsClosed()) {
            /* Set State close, and ClientMonitor Thread also closed */
            this.isClosed = true;

            try {
                /* CloseIO TCP Server */
                this.tcpServer.close();
            } catch (IOException ex) {
                Logger.getLogger(TCPHost.class.getName()).log(Level.SEVERE, null, ex);
            }

            /* Shut down Listen Thread */
            tcpThread.shutdown();

            this.dev = null;
        }
    }

    /**
     * Is ServerCloseIOd
     *
     * @return
     */
    public boolean IsClosed() {
        return this.isClosed;
    }

    @Override
    public void run() {
        while (!this.IsClosed()) {
            try {
                /* Listen new Client */
                Socket socket = tcpServer.accept();

                if (this.dev != null) {
                    ExecutrCMD(socket);
                }
            } catch (Exception ex) {
            }
        }
    }

    private void ExecutrCMD(final Socket socket) {
        System.out.println(new Date() + ":receive socket");
        tcpThread.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(new Date() + ":start socket");
                    while (true) {
                        MIGPNode migpnode = new MIGPNode(new IO_TCP(socket), dev.devAddr);

                        MIGPPacket pkt = migpnode.ReceiveMIGPPacket(3000);//wait 2 seconds
                        if (pkt != null) {
                            PrintInfo(pkt);
                            MIGPPacket[] retpkt = dev.ExecutorMIGPCMD(pkt);
                            for (MIGPPacket tpkt : retpkt) {
                                migpnode.SendMIGPPacket(tpkt.dstAddress, tpkt.CMD, tpkt.data);
                                TimeUnit.MILLISECONDS.sleep(10);
                            }
                        } else {
                            System.out.println(new Date() + ":close socket");
                            break;
                        }
                    }
                } catch (Exception ex) {
//                    Logger.getLogger(TCPHost.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        socket.close();
                    } catch (IOException ex) {
                        Logger.getLogger(TCPHost.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
    }

    private void PrintInfo(MIGPPacket pkt) {
        String cmdname = "";
        switch (pkt.CMD) {
            case MIGPCOMMEM.GETEIA:
                cmdname = "GETEIA";
                break;
            case MIGPCOMMEM.SETEIA:
                cmdname = "SETEIA";
                break;
            case MIGPCOMMEM.GETMDA:
                cmdname = "GETMDA";
                break;
            case MIGPCOMMEM.SETMDA:
                cmdname = "SETMDA";
                break;
            case MIGPCOMMEM.GETNVPA:
                cmdname = "GETNVPA";
                break;
            case MIGPCOMMEM.SETNVPA:
                cmdname = "SETNVPA";
                break;
            case MIGPCOMMEM.GETVPA:
                cmdname = "GETVPA";
                break;
            case MIGPCOMMEM.SETVPA:
                cmdname = "SETVPA";
                break;
            case MIGPCOMMEM.GETSRA:
                cmdname = "GETSRA";
                break;
            case MIGPCOMMEM.SETSRA:
                cmdname = "SETSRA";
                break;
        }

        try {
            int addr = MyConvert.ByteArrayToInteger(pkt.data, 0);
            int len = MyConvert.ByteArrayToInteger(pkt.data, 4);
            System.out.println(
                    new Date() + ":receive data from:" + String.format("0x%02X ", pkt.srcAddress)
                    + " cmd: " + cmdname + " addr: " + addr + " len: " + len);
        } catch (MyConvertException ex) {
            Logger.getLogger(TCPHost.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
