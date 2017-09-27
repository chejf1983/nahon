package com.nahon.sobs.LamApp;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.logging.Level;

import lam.devcontrol.ifs.ISPDevConfig;
import lam.faultcenter.SYSLOG;
import nahon.dev.data.EquipmentInfo;
import nahon.dev.data.SConnectInfo;
import spdev.dev.data.SpectralPar;
import spdev.dev.data.WaveCaculatePar;
import sps.system.SPDevSystem;

public class DevConfigActivity extends AppCompatActivity {

    // <editor-fold defaultstate="collapsed" desc="初始化界面">
    private TextView eia_devname, eia_buildserial, eia_hwversion, eia_sfversion, eia_builddate;
    private TextView sp_waverange, sp_timerange, sp_nodenum;
    private TextView net_ip, net_port, net_addr;
    private EditText C0, C1, C2, C3;
    private Button button_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_config);
        this.initView();
    }

    //初始化界面
    private void initView() {
        LinearLayout list_view = (LinearLayout) this.findViewById(R.id.config_list);
        ConfigViewHelp viewHelp = new ConfigViewHelp(this.getBaseContext());

        viewHelp.addTitle(list_view, R.mipmap.ic_launcher, "设备信息");
        eia_devname = viewHelp.addTextColumn(list_view, "设备名称：", "", true);
        eia_hwversion = viewHelp.addTextColumn(list_view, "硬件版本：", "", true);
        eia_sfversion = viewHelp.addTextColumn(list_view, "软件版本：", "", true);
        eia_buildserial = viewHelp.addTextColumn(list_view, "序列号：", "", true);
        eia_builddate = viewHelp.addTextColumn(list_view, "生产日期：", "", false);

        viewHelp.addTitle(list_view, R.mipmap.ic_launcher, "设备参数");
        sp_waverange = viewHelp.addTextColumn(list_view, "测量波长范围：", "", true);
        sp_timerange = viewHelp.addTextColumn(list_view, "积分时间范围：", "", true);
        sp_nodenum = viewHelp.addTextColumn(list_view, "测量点：", "", false);

        viewHelp.addTitle(list_view, R.mipmap.ic_launcher, "网络连接");
        net_ip = viewHelp.addTextColumn(list_view, "IP地址：", "", true);
        net_port = viewHelp.addTextColumn(list_view, "端口号：", "", true);
        net_addr = viewHelp.addTextColumn(list_view, "设备地址：", "", false);

        viewHelp.addTitle(list_view, R.mipmap.ic_launcher, "光学参数");
        C0 = viewHelp.addEditNumColumn(list_view, "C0：", 0, true);
        C1 = viewHelp.addEditNumColumn(list_view, "C1：", 0, true);
        C2 = viewHelp.addEditNumColumn(list_view, "C2：", 0, true);
        C3 = viewHelp.addEditNumColumn(list_view, "C3：", 0, false);

        button_save = (Button) this.findViewById(R.id.button_save);
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_save.setEnabled(false);
                MsgHandler.sendEmptyMessage(SAVECONFIG);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        this.MsgHandler.sendEmptyMessage(READCONFIG);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="消息处理接口">
    ExecutorService thread = SPDevSystem.GetInstance().systemthreadpool;

    private final int ACKMASK = 0x100;
    private final int READCONFIG = 0x01;
    private final int SAVECONFIG = 0x02;
    private final int SAVECONFIGACK = SAVECONFIG | ACKMASK;

    private Handler MsgHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case READCONFIG:
                    readConfiguration();
                    break;
                case SAVECONFIG:
                    saveConfig();
                    break;
                case SAVECONFIGACK:
                    saveConfigAck(msg.arg1);
                    break;
                default:
                    FaultToast.makeText(DevConfigActivity.this, "异常消息", Toast.LENGTH_SHORT);
            }
        }
    };

    private ISPDevConfig spdev_config;
    private WaveCaculatePar wave_par;
    private SpectralPar sp_par;

    //读取配置
    private void readConfiguration() {
//        Toast.makeText(this, "读取配置参数...", Toast.LENGTH_SHORT).show();
        spdev_config = SPDevSystem.GetInstance().spdevcontrol.GetSPDevConfig();

        EquipmentInfo eia = spdev_config.GetEquipmentInfo();
        eia_devname.setText(eia.DeviceName);
        eia_hwversion.setText(eia.Hardversion);
        eia_sfversion.setText(eia.SoftwareVersion);
        eia_buildserial.setText(eia.BuildSerialNum);
        eia_builddate.setText(eia.BuildDate);

        SConnectInfo conninfo = spdev_config.GetDevConnectInfo();
        net_ip.setText(conninfo.io.par[0]);
        net_port.setText(conninfo.io.par[1]);
        net_addr.setText(String.format("0x%x", conninfo.dstaddr));


        sp_par = spdev_config.GetSpectralPar();

        sp_waverange.setText(sp_par.minWaveLength + "-" + sp_par.maxWaveLength + "(nm)");
        sp_timerange.setText(sp_par.minIntegralTime + "-" + sp_par.maxIntegralTime + "(ms)");
        sp_nodenum.setText(String.valueOf(sp_par.nodeNumber));

        wave_par = spdev_config.GetWaveParameter();

        C0.setText(String.valueOf(wave_par.C0));
        C1.setText(String.valueOf(wave_par.C1));
        C2.setText(String.valueOf(wave_par.C2));
        C3.setText(String.valueOf(wave_par.C3));
    }

    //保存配置
    private void saveConfig() {
        thread.submit(new Runnable() {
            @Override
            public void run() {
                int result = 0;
                WaveCaculatePar par = new WaveCaculatePar();
                par.C0 = Double.valueOf(C0.getText().toString());
                par.C1 = Double.valueOf(C1.getText().toString());
                par.C2 = Double.valueOf(C2.getText().toString());
                par.C3 = Double.valueOf(C3.getText().toString());

                try {
                    spdev_config.SetWaveParameter(par);
                } catch (Exception e) {
                    SYSLOG.getLog().PrintLog(Level.SEVERE, e.toString());
                    result = 1;
                }

                Message ack = new Message();
                ack.what = SAVECONFIGACK;
                ack.arg1 = result;
                MsgHandler.sendMessage(ack);
            }
        });
    }

    private void saveConfigAck(int result) {
        button_save.setEnabled(true);
        if (result != 0) {
            FaultToast.makeText(DevConfigActivity.this, "保存配置失败", Toast.LENGTH_SHORT);
        } else {
            FaultToast.makeText(DevConfigActivity.this, "保存配置成功", Toast.LENGTH_SHORT);
            this.finish();
        }
    }
    // </editor-fold>
}
