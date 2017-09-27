package com.nahon.sobs.LamApp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nahon.lam.io.IO_TCP;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import lam.faultcenter.FaultCenter;
import lam.faultcenter.SYSLOG;
import nahon.absractio.AbstractIO;
import nahon.comm.event.Event;
import nahon.comm.event.EventListener;
import sps.system.SPDevSystem;

public class DevSearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_search);

        //打印系统错误
        FaultCenter.getReporter().RegisterFaultEvent(new EventListener<Level>() {
            @Override
            public void recevieEvent(Event<Level> event) {
                Message msg = new Message();
                msg.what = FAULTPRINT;
                msg.obj = event.GetEvent() + event.Info().toString();
                handler.sendMessage(msg);
            }
        });

        //设置log路径
        try {
            String path = this.getExternalFilesDir(null).getAbsolutePath();
            SYSLOG.getLog().SetLogPath(path);
        } catch (Exception ex) {
            FaultCenter.getReporter().SendFaultReport(Level.SEVERE, ex.toString());
        }

        this.initView();
    }

    private Button search_button;
    private EditText etext_portnum, etext_ipaddr;


    private void initView() {
        this.etext_ipaddr = (EditText) this.findViewById(R.id.text_ipaddr);
        this.etext_portnum = (EditText) this.findViewById(R.id.text_port);
        this.search_button = (Button) this.findViewById(R.id.button_search);

        //搜索按钮初始化
        this.search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_button.setEnabled(false);
                handler.sendEmptyMessage(SEARCHDEV);
            }
        });

        //初始化默认IP
        this.handler.sendEmptyMessage(INITIP);
    }

    // <editor-fold defaultstate="collapsed" desc="消息处理接口">
    private final int ACKMASK = 0x100;

    private final int SEARCHDEV = 0x01;
    private final int SEARCHDEVACK = SEARCHDEV | ACKMASK;
    private final int INITIP = 0x02;
    private final int FAULTPRINT = 0x03;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case INITIP:
                    loadLocalIp();
                    break;
                case SEARCHDEV:
                    searchDevice();
                    break;
                case SEARCHDEVACK:
                    searchDeviceAck();
                    break;
                case FAULTPRINT:
                    printfault(msg);
                    break;
                default:
                    FaultToast.makeText(DevSearchActivity.this, "异常消息", Toast.LENGTH_SHORT);
            }
        }

        //加载本地IP
        private void loadLocalIp() {
            SPDevSystem.GetInstance().systemthreadpool.submit(new Runnable() {
                @Override
                public void run() {
                    WifiManager wifiManager = (WifiManager) DevSearchActivity.this.getSystemService(Context.WIFI_SERVICE);
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    etext_ipaddr.setText(Formatter.formatIpAddress(wifiInfo.getIpAddress()));
                    etext_portnum.setText("2000");
                }
            });
        }

        //搜索设备
        private void searchDevice() {
            if (SPDevSystem.GetInstance().dev_manager.isStart()) {
                return;
            }

            SPDevSystem.GetInstance().systemthreadpool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        String ipaddr = etext_ipaddr.getText().toString();
                        int port = Integer.valueOf(etext_portnum.getText().toString());

                        //枚举所有局域网IP
                        ipaddr = ipaddr.substring(0, ipaddr.lastIndexOf(".") + 1);
                        ArrayList<IO_TCP> iolist = new ArrayList<>();
                        for (int i = 1; i <= 255; i++) {
                            iolist.add(new IO_TCP(ipaddr + i, port));
                        }
                        SPDevSystem.GetInstance().dev_manager.StartSearchIO(iolist.toArray(new IO_TCP[0]), true);

                        handler.sendEmptyMessage(SEARCHDEVACK);
                    } catch (Exception e) {
                        FaultCenter.getReporter().SendFaultReport(Level.SEVERE, "搜索设备失败" + e.toString());
                    } finally {
                        search_button.setEnabled(true);
                    }
                }
            });

            //等待设备启动
//            while (!SPDevSystem.GetInstance().dev_manager.isStart()) {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (Exception ex) {

            }
//            }
            //显示搜索进程
            displaySearchingProcess();
        }

        //搜索设备结果
        private void searchDeviceAck() {
            int foundeddev = SPDevSystem.GetInstance().dev_manager.GetDeviceList().size();
            FaultToast.makeText(DevSearchActivity.this, "找到设备: " + foundeddev, Toast.LENGTH_SHORT);
            if (foundeddev > 0) {
                startActivity(new Intent(DevSearchActivity.this, DevListActivity.class));
            }
        }

    };

    ProgressDialog mProgressDialog;

    //搜索进度条
    private void displaySearchingProcess() {
        if (!SPDevSystem.GetInstance().dev_manager.isStart()) {
            return;
        }
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setTitle("搜索设备...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgress(0);
        mProgressDialog.setMax(SPDevSystem.GetInstance().dev_manager.GetTotalProcess());
        mProgressDialog.show();

        SPDevSystem.GetInstance().systemthreadpool.submit(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    //更新百分比
                    mProgressDialog.setProgress(SPDevSystem.GetInstance().dev_manager.Getfinishprocess());

                    //如果搜索完成，就删除搜索进程
                    if (!SPDevSystem.GetInstance().dev_manager.isStart()) {
                        mProgressDialog.dismiss();
                        break;
                    }
                    try {
                        TimeUnit.MILLISECONDS.sleep(50);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    //打印错误
    private void printfault(Message msg) {
        FaultToast.makeText(this, msg.obj.toString(), Toast.LENGTH_SHORT);
    }
    // </editor-fold>
}
