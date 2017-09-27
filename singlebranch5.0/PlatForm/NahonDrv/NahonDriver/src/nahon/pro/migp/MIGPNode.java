/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nahon.pro.migp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import nahon.comm.tool.convert.NahonConvert;
import nahon.drv.absractio.AbstractIO;
import nahon.drv.data.CancelException;

/**
 * MIGP IO one migp io bind one real io one real io can be bind to serveral migp
 * io
 *
 * @author jiche
 */
public class MIGPNode {

    /**
     * Physical Interface which already Opened. That can be used to send and
     * receive data directly.
     */
    // <editor-fold defaultstate="collapsed" desc="IO Control"> 
    protected final AbstractIO physicalInterface;
    protected final byte localAddr;
    protected final ReentrantLock nodelocker = new ReentrantLock();

    /**
     *
     * @param physicalInterface
     */
    public MIGPNode(AbstractIO physicalInterface, byte localAddr) {
        this.physicalInterface = physicalInterface;
        this.localAddr = localAddr;
    }

    public byte GetLocalAddr() {
        return this.localAddr;
    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="migp packet process"> 
    private final Queue<Byte> rcbuffer = new LinkedList<>();
    private static final int TMP_BUFFER_SIZE = 20480;
    private static final int MAX_RCBUFFER_SIZE = 102400;
    private static final int MAXTIMEOUT = 100;
    private boolean iscanceled = false;

    /**
     *
     * @param dstAddress
     * @param cmd
     * @param data
     * @throws java.lang.Exception
     */
    public synchronized void SendMIGPPacket(byte dstAddress, byte cmd, byte[] data) throws Exception {
        this.nodelocker.lock();
        try {
            this.physicalInterface.SendData(
                    MIGPCodec.EncodeBuffer(
                            new MIGPPacket(dstAddress, this.GetLocalAddr(), cmd, data)));
        } finally {
            this.nodelocker.unlock();
        }
    }

    /**
     * receive a migp packet from real io.
     *
     * @param timeout
     * @return
     * @throws Exception
     */
    public synchronized MIGPPacket ReceiveMIGPPacket(int timeout) throws Exception {
        this.nodelocker.lock();
        MIGPPacket packet = null;

        try {
            int timeindex = 0; //时间计数器
            this.iscanceled = false; //是否取消
            int lag = 0;
            int len = 0;
            byte[] tmpdata = new byte[TMP_BUFFER_SIZE];

            while (!iscanceled) {
                //receive data
                len = physicalInterface.ReceiveData(tmpdata, MAXTIMEOUT);

                if (len > 0) {
                    //move tmp data to recevei buffer pool
                    for (int i = 0; i < len; i++) {
                        if (rcbuffer.size() + len >= MAX_RCBUFFER_SIZE) {
                            rcbuffer.poll();
                        }
                        rcbuffer.offer(tmpdata[i]);
                    }

                    //寻找有效包
                    packet = MIGPCodec.DecodeBuffer(rcbuffer);
                    //找到包，跳出循环，返回有效包
                    if (packet != null) {
                        break;
                    }
                } else {
                    //计时器增加
                    timeindex += MAXTIMEOUT;
                    if (timeindex >= timeout) {
                    //超时，返回空包
                    //System.out.println("超时" + timeout);
                    return null;
                } 
                }
            }

            if (this.iscanceled) {
                // this.nodelocker.unlock();
                throw new CancelException("Canceled");
            }

            //no packet found, means timeout
            return packet;
        } finally {
            this.nodelocker.unlock();
        }
    }

    public boolean IsCanceled() {
        return this.iscanceled;
    }

    public void Cancel() {
        this.iscanceled = true;

        while (this.nodelocker.isLocked()) {
            //只要nodelock还锁住，表示node还在接数据，将canceled设置成true;
            this.iscanceled = true;
            try {
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException ex) {

            }
        }
    }

    private boolean checkAddr(MIGPPacket pkt) {
        if (pkt == null) {
            return false;
        }

        if (pkt.dstAddress == 0 || pkt.dstAddress == this.localAddr) {
            return true;
        } else {
            return false;
        }
    }
    // </editor-fold> 
}

class MIGPCodec {

    /**
     * Message Packet Form:
     * |Head(4)|DstDev(1)|LocalDev(1)|CMD(1)|data(x)|CRC(1)|Tail(4)|
     *
     * @author jiche
     */
    public static final byte[] head = {(byte) 0x55, (byte) 0xAA, (byte) 0x7b, (byte) 0x7b};
    public static final byte[] tail = {(byte) 0x55, (byte) 0xAA, (byte) 0x7d, (byte) 0x7d};
    public static final int HeadLength = head.length;
    public static final int DstDevLength = 1;
    public static final int SrcDevLength = 1;
    public static final int CMDLength = 1;
    public static final int LRCLength = 1;
    public static final int TailLength = tail.length;

    /**
     * 打包数据,将数据打包成MIGP格式
     *
     * @param packet
     * @return
     */
    public static byte[] EncodeBuffer(MIGPPacket packet) {
        int offset = 0;
        byte[] buffer = new byte[HeadLength + DstDevLength
                + SrcDevLength + CMDLength + packet.data.length
                + LRCLength + TailLength];

        //添加包头Head(4)：55 AA 7B 7B
        System.arraycopy(head, 0, buffer, offset, HeadLength);
        offset += HeadLength;

        //添加目的地址DstDev(1)
        buffer[offset] = packet.dstAddress;
        offset += DstDevLength;

        //添加源地址LocalDev(1)
        buffer[offset] = packet.srcAddress;
        offset += SrcDevLength;

        //添加命令字CMD(1)
        buffer[offset] = packet.CMD;
        offset += CMDLength;

        //拷贝内容
        System.arraycopy(packet.data, 0, buffer, offset, packet.data.length);
        offset += packet.data.length;

        //LRC效验，累加所有字节
        for (int i = 0; i < packet.data.length; i++) {
            buffer[offset] += packet.data[i];
        }
        offset += LRCLength;

        //添加包尾Tail(4):55 AA 7D 7D
        System.arraycopy(tail, 0, buffer, offset, tail.length);
        offset += TailLength;

        return buffer;
    }

    /**
     * 解MIGP包数据
     *
     * @param packet
     * @return
     */
    public static MIGPPacket DecodeBuffer(Queue<Byte> rcbuffer) {
        /* Get a complete data from rcbuffer*/
        byte[] migpFrame = FindeMigpFrameBuffer(rcbuffer);

        /* Check DevNum and LRC */
        if (CheckFrameLength(migpFrame) && CheckLRC(migpFrame)) {
            byte[] tmp = new byte[migpFrame.length - (HeadLength + DstDevLength
                    + SrcDevLength + CMDLength + LRCLength + TailLength)];
            /* Get MIGP packet data flow */
            System.arraycopy(migpFrame,
                    (HeadLength + DstDevLength + SrcDevLength + CMDLength), tmp, 0, tmp.length);

            return new MIGPPacket(migpFrame[4], migpFrame[5], migpFrame[6], tmp);
        } else {
            return null;
        }
    }

    /**
     * check migp packet frame length
     *
     * @param packet
     * @return
     */
    private static boolean CheckFrameLength(byte[] tmpbuffer) {
        if (tmpbuffer == null) {
            return false;
        }

        // a complete migp packet must contain head, dstdev, srcdev, cmd, lrc, tail
        if (tmpbuffer.length < ((HeadLength + DstDevLength
                + SrcDevLength + CMDLength + LRCLength + TailLength))) {
            Logger.getGlobal().log(Level.SEVERE, "Packet length is less than minpacket length.");
            return false;
        }

        return true;
    }

    /**
     * check migp packet LRC
     *
     * @param packet
     * @return
     */
    private static boolean CheckLRC(byte[] tmpbuffer) {
        int lrc = 0x00;

        /* check the lrc */
        for (int i = (HeadLength + DstDevLength + SrcDevLength + CMDLength);
                i < tmpbuffer.length - (LRCLength + TailLength); i++) {
            lrc += 0xFF & tmpbuffer[i];
        }

        /* if crc is not correct, the tmpbuffer will be dropped */
        if ((byte) lrc != tmpbuffer[(tmpbuffer.length - (LRCLength + TailLength))]) {
            Logger.getGlobal().log(Level.SEVERE, "Packet LRC is wrong!");
            return false;
        }
        return true;
    }

    /**
     * Find out a complete MIGP packet from data buffer
     *
     * @param packet
     * @return a complete migp packet
     */
    private static byte[] FindeMigpFrameBuffer(Queue<Byte> rcbuffer) {
        byte[] tmp = NahonConvert.ByteListtobyteArray(rcbuffer);

        int startpoint = 0;
        boolean startfounded = false;
        boolean endfounded = false;
        int endpoint = 0;

        for (int i = 0; i < rcbuffer.size(); i++) {
            /* must bigger than a headlength */
            if (i + head.length > tmp.length) {
                break;
            }

            /* find migp packet head position */
            if (!startfounded) {
                if ((tmp[i] == head[0])
                        && (tmp[i + 1] == head[1])
                        && (tmp[i + 2] == head[2])
                        && (tmp[i + 3] == head[3])) {
                    startpoint = i;
                    startfounded = true;
                }
            }

            /* found migp packet end position */
            if (!endfounded) {
                if ((tmp[i] == tail[0])
                        && (tmp[i + 1] == tail[1])
                        && (tmp[i + 2] == tail[2])
                        && (tmp[i + 3] == tail[3])) {
                    endpoint = i + 4;
                    endfounded = true;
                    if (startfounded) {
                        break;
                    }
                }
            }
        }

        if (endpoint > startpoint) {
            /* remove founded migp packet and data before it form rcbuffer pool */
            for (int i = 0; i < endpoint; i++) {
                rcbuffer.poll();
            }

            /* copy the data buffer */
            byte[] rcpacket = new byte[endpoint - startpoint];
            System.arraycopy(tmp, startpoint, rcpacket, 0, endpoint - startpoint);
            return rcpacket;
        } else {
            return null;
        }
    }
}
