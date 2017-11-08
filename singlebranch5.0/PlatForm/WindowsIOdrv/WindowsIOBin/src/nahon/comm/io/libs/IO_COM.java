/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nahon.comm.io.libs;

//import gnu.io.CommPortIdentifier;
//import gnu.io.SerialPort;
import java.io.*;
import java.util.Enumeration;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import nahon.comm.io.libs.WindowsIOFactory.IOTYPE;

/**
 *
 * @author chejf
 */
public class IO_COM implements WindowsIO {

    private boolean isClosed = true;
    private CommPortIdentifier comportId;
    private SerialPort comserialPort;
    private OutputStream comout = null;
    private InputStream comin = null;

    private final String comName;
    private final int baundrate;

    public IO_COM(String name, int baundrate) {
        this.comName = name;
        this.baundrate = baundrate;
    }

    private static boolean IsInit = false;

    public static void InitLib() throws Exception {
        if (!IsInit) {
//            CreateDLLTempFile("win32com.dll");
//            System.load(CreateDLLTempFile("win32com.dll"));
            CreateDLLTempFile("rxtxSerial.dll");
            CreateDLLTempFile("rxtxParallel.dll");
            System.load(CreateDLLTempFile("rxtxSerial.dll"));
            System.load(CreateDLLTempFile("rxtxParallel.dll"));
            IsInit = true;
        }
    }

    private static String CreateDLLTempFile(String Filename) throws Exception {
        File tmp = new File("./jre/bin");
        if (tmp.exists()) {
            tmp = new File("./jre/bin/" + Filename);
        } else {
            tmp = new File("./" + Filename);
        }

        if (!tmp.exists()) {
            InputStream in = IO_COM.class.getResourceAsStream("/Resource/" + Filename);
            FileOutputStream out = new FileOutputStream(tmp);

            int i;
            byte[] buf = new byte[1024];
            while ((i = in.read(buf)) != -1) {
                out.write(buf, 0, i);
            }

            in.close();
            out.close();
            System.out.println("create file:" + Filename);
        }
        // return tmp.getAbsoluteFile().getAbsolutePath();
        return tmp.getCanonicalPath();
    }

    private static int sendBufferLimit = 128;

    @Override
    public void SendData(byte[] data) throws Exception {
        for (int sendIndex = 0; sendIndex < data.length;) {
            if (data.length - sendIndex > sendBufferLimit) {
                comout.write(data, sendIndex, sendBufferLimit);
                sendIndex += sendBufferLimit;
            } else {
                comout.write(data, sendIndex, data.length - sendIndex);
                sendIndex += data.length - sendIndex;
            }
        }
    }

    @Override
    public int ReceiveData(byte[] data, int timeout) throws Exception {
        int index = 0;
        while ((index++) * 10 < timeout) {
            if (comin.available() > 0) {
                return comin.read(data);
            }
            TimeUnit.MILLISECONDS.sleep(10);
        }

        return 0;
    }

    private void CloseIO() {
        if (!this.IsClosed()) {
            try {
                if (comserialPort != null) {
                    comserialPort.close();
                }
                if (comout != null) {
                    comout.close();
                }
                if (comin != null) {
                    comin.close();
                }
            } catch (Exception ex) {
            } finally {
                this.isClosed = true;
            }
        }

    }

    @Override
    public boolean IsClosed() {
        return this.isClosed;
    }

    private void OpenIO() throws Exception {
        if (this.IsClosed()) {
            InitLib();

            Enumeration portList = CommPortIdentifier.getPortIdentifiers();

            while (portList.hasMoreElements()) {
                comportId = (CommPortIdentifier) portList.nextElement();
                if ((comportId.getPortType() == CommPortIdentifier.PORT_SERIAL)
                        && (comportId.getName().equals(this.comName))) {
                    comserialPort = (SerialPort) comportId.open("SimpleWriteApp", 2000);
                    comserialPort.setSerialPortParams(this.baundrate,
                            SerialPort.DATABITS_8,
                            SerialPort.STOPBITS_1,
                            SerialPort.PARITY_NONE);
                    comserialPort.setInputBufferSize(40960);
                    comout = comserialPort.getOutputStream();
                    comin = comserialPort.getInputStream();

                    this.isClosed = false;
                    return;
                }
            }

            throw new Exception("Could not found Comport");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="physical IO control"> 
    private Lock usercoutlock = new ReentrantLock();
    private int delaycolsetime = 200;//ms
    private int user = 0;

    @Override
    public void Open() throws Exception {
        this.usercoutlock.lock();
        this.user++;
        this.usercoutlock.unlock();
        this.OpenIO();
    }

    @Override
    public void Close() {
        Executors.newSingleThreadExecutor().submit(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.MILLISECONDS.sleep(delaycolsetime);
                    usercoutlock.lock();
                    user--;
                    if (user <= 0) {
                        if (!IsClosed()) {
                            CloseIO();
                        }
                        user = 0;
                    }
                    usercoutlock.unlock();
                } catch (Exception ex) {

                }
            }
        });
    }
    // </editor-fold> 

    @Override
    public WIOInfo GetConnectInfo() {
        return new WIOInfo(IOTYPE.COM.toString(), this.comName, String.valueOf(this.baundrate));
    }

    @Override
    public int MaxBuffersize() {
        return 65535;
    }
}
