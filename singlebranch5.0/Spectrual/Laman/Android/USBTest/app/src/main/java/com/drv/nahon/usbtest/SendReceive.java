package com.drv.nahon.usbtest;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.drv.nahon.usb.LibUSB;
import com.drv.nahon.usb.USBManager;

import java.util.concurrent.Executors;

public class SendReceive extends AppCompatActivity {

    //搜索按钮
    private Button button_send;
    private EditText input_text;
    private EditText output_text;
    private LibUSB usb_instance;
    byte[] senddata = new byte[]{(byte) 0x55, (byte) 0xAA, (byte) 0x7B, (byte) 0x7B,
            (byte) 0x0, (byte) 0x0, (byte) 0x30, (byte) 0x0, (byte) 0x0, (byte) 0x0,
            (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x44, (byte) 0x44,
            (byte) 0x55, (byte) 0xAA, (byte) 0x7D, (byte) 0x7D};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_receive);

        this.initView();
    }

    private void initView() {
        this.button_send = (Button) this.findViewById(R.id.data_send);
        this.input_text = (EditText) this.findViewById(R.id.input_text);
        this.output_text = (EditText) this.findViewById(R.id.display_text);

        this.usb_instance = USBManager.OpenUSBImpl(0);

        this.button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendData();
            }
        });
    }
    String retbuffer = "";
    private void SendData() {
        if (!USBManager.IsPermissioon(0)) {
            FaultToast.makeText(SendReceive.this.getBaseContext(), "no permssion!", Toast.LENGTH_SHORT);
            return;
        }
        Executors.newSingleThreadExecutor().submit(new Runnable() {
            @Override
            public void run() {
                try {
                    byte[] recdata = new byte[1000];
                    int retlen = 0;
                    FaultToast.makeText(SendReceive.this.getBaseContext(), "begain send:", Toast.LENGTH_SHORT);

                    if(-1 ==  usb_instance.USBBulkWriteDataImpl(senddata, senddata.length, 5000)){
                        FaultToast.makeText(SendReceive.this.getBaseContext(), "connection == null", Toast.LENGTH_SHORT);
                    }

                    FaultToast.makeText(SendReceive.this.getBaseContext(), "begain receive:", Toast.LENGTH_SHORT);
                    retlen = usb_instance.USBBulkReadDataImpl(recdata, recdata.length, 10000);

                    retbuffer = "";
                    for (int i = 0; i < retlen; i++) {
                        retbuffer += " 0x" + recdata[i];
                    }
                    MsgHandler.sendEmptyMessage(0);
                    FaultToast.makeText(SendReceive.this.getBaseContext(), "receive:" + retlen, Toast.LENGTH_SHORT);
                } catch (Exception ex) {
                    FaultToast.makeText(SendReceive.this.getBaseContext(), ex.getMessage(), Toast.LENGTH_SHORT);
                }
            }
        });
        //String mem = this.input_text.getText().toString();
        //String[] sbytes = mem.trim().split(" ");
        //byte[] tmp = new byte[sbytes.length];
        //for (int i = 0; i < tmp.length; i++) {
        //     String hex = sbytes[i].trim();
        //    hex = hex.substring(hex.indexOf("x") + 1);
        //    tmp[i] = (byte) Integer.parseInt(hex, 16);
        // }


    }
    private Handler MsgHandler = new Handler() {
        public void handleMessage(Message msg) {
            output_text.setText(retbuffer);
        }

    };

}
