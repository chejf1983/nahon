package com.nahon.sobs.LamApp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.concurrent.ExecutorService;
import java.util.logging.Level;

import lam.devcontrol.ifs.ISPDevControl;
import lam.devcontrol.inst.SPDevControl;
import lam.faultcenter.FaultCenter;
import lam.faultcenter.SYSLOG;
import nahon.comm.event.Event;
import nahon.comm.event.EventListener;
import spdev.alg.spdatacal.DarkDataModel;
import spdev.dev.data.DataCollectPar;
import spdev.dev.data.SPData;
import spdev.dev.data.SpectralDataPacket;
import sps.system.SPDevSystem;

public class DevControlActivity extends AppCompatActivity {
    private Button button_test, button_config, button_sartmonitor, button_stopcollect, button_zoomreset, button_zoomin, button_zoomout;
    private ToggleButton tbutton_dark_fliter;
    private EditText et_integerTime, et_average;

    // <editor-fold defaultstate="collapsed" desc="界面初始化">
    //创建 Activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dev_control);

        //初始化设备控制按钮
        this.init_dev_control();

        //初始化图表
        this.initChartView();
    }

    //初始化界面按钮
    private void init_dev_control() {
        this.button_test = (Button) this.findViewById(R.id.button_start);
        this.button_config = (Button) this.findViewById(R.id.button_config);
        this.button_sartmonitor = (Button) this.findViewById(R.id.button_monitor);
        this.button_stopcollect = (Button) this.findViewById(R.id.button_stop);
        this.button_zoomreset = (Button) this.findViewById(R.id.button_chart_reset);
        this.button_zoomin = (Button) this.findViewById(R.id.button_chart_zoomin);
        this.button_zoomout = (Button) this.findViewById(R.id.button_chart_zoomout);
        this.tbutton_dark_fliter = (ToggleButton) this.findViewById(R.id.button_noise_filter);

        this.et_integerTime = (EditText) this.findViewById(R.id.content_integer_time);
        this.et_average = (EditText) this.findViewById(R.id.content_average);

        this.button_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAllButtonEnable(false);
                msgHandler.sendEmptyMessage(STARTTEST);
            }
        });

        this.button_config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAllButtonEnable(false);
                startConfig();
                //msgHandler.sendEmptyMessage(UPDATESTATE);
            }
        });

        this.button_sartmonitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAllButtonEnable(false);
                msgHandler.sendEmptyMessage(STARTMONITOR);
            }
        });

        this.button_stopcollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAllButtonEnable(false);
                msgHandler.sendEmptyMessage(STOPCOLLECT);
            }
        });

        this.button_zoomreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chart_view != null) {
                    chart_view.zoomReset();
                }
            }
        });
        this.button_zoomin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chart_view != null) {
                    chart_view.zoomIn();
                }
            }
        });
        this.button_zoomout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chart_view != null) {
                    chart_view.zoomOut();
                }
            }
        });

        this.tbutton_dark_fliter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAllButtonEnable(false);
                DarkDataModel dkmode = spdevControl.GetDataCollecor().GetSpBuildModel().GetDarkDataModel();

                if (tbutton_dark_fliter.isChecked()) {
                    dkmode.SetDarkData(spdevControl.GetDataCollecor().GetLastOriginalData());
                }

                dkmode.EnableDarkData(tbutton_dark_fliter.isChecked());
                tbutton_dark_fliter.setChecked(dkmode.IsDarkDataEnable());
                msgHandler.sendEmptyMessage(UPDATESTATE);
            }
        });

        this.spdevControl.RegisterStateChange(new EventListener<ISPDevControl.CSTATE>() {
            @Override
            public void recevieEvent(Event<ISPDevControl.CSTATE> event) {
                msgHandler.sendEmptyMessage(UPDATESTATE);
            }
        });
    }

    //返回按钮响应
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (this.isAllenable) {
                setAllButtonEnable(false);
                this.msgHandler.sendEmptyMessage(CLOSE);
            } else {
                FaultToast.makeText(this, "请先关闭其他操作", Toast.LENGTH_SHORT);
            }
        }
        return true;
    }

    //Activity 激活
    @Override
    public void onStart() {
        //注册数据刷新事件
        spdevControl.GetDataCollecor().RegisterDataCollectListener(DataUpdateEvent);

        //初始化采集参数
        DataCollectPar collectpar = spdevControl.GetDataCollecor().GetDataCollectPar();
        this.et_integerTime.setText(String.valueOf(collectpar.integralTime));
        this.et_average.setText(String.valueOf(collectpar.averageTime));

        //初始化暗电流使能状态
        this.tbutton_dark_fliter.setChecked(spdevControl.GetDataCollecor().GetSpBuildModel().GetDarkDataModel().IsDarkDataEnable());

        super.onStart();
    }

    //Activity 睡眠
    @Override
    public void onStop() {
        if (spdevControl.GetDataCollecor() != null) {
            //断开数据刷新事件
            spdevControl.GetDataCollecor().UnRegisterDataCollectListener(DataUpdateEvent);
        }
        super.onStop();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="图表初始化">
    private LinearLayout chart_layout;
    private GraphicalView chart_view;
    private XYMultipleSeriesDataset dataset;
    private XYSeries series;

    //初始化图标界面
    private void initChartView() {
        XYMultipleSeriesRenderer renderer = this.createXYMSRenderer();
        dataset = new XYMultipleSeriesDataset();
        series = new XYSeries("光谱");  //曲线名称
        dataset.addSeries(series);

        renderer.addSeriesRenderer(this.createLineRender());
        chart_view = ColorLineChart.getLineChartView(this, dataset, renderer);
        this.chart_layout = (LinearLayout) this.findViewById(R.id.chart_layout);
        chart_layout.addView(chart_view);

        chart_view.invalidate();
    }

    private XYMultipleSeriesRenderer createXYMSRenderer() {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();

        renderer.setLabelsTextSize(25);
        renderer.setMargins(new int[]{70, 50, 50, 10});//设置上、左、下、右留白

        renderer.setYLabels(10);        //y轴标签大小
        renderer.setYLabelsPadding(25); //y轴标签离坐标轴空隙
        renderer.setYLabelsColor(0, Color.BLUE);

        renderer.setXLabels(8);         //x轴标签大小
        renderer.setXLabelsPadding(20); //x轴标签离坐标轴空隙
        renderer.setXLabelsAngle(40);   //x轴坐标标签角度
        renderer.setXLabelsColor(Color.BLUE);

        renderer.setShowLegend(false);
        renderer.setExternalZoomEnabled(true); //使能zoom-in -out -reset 功能

        renderer.setShowGrid(true);    //显示经纬线
//        renderer.setShowGridY(true);    //显示纬线
        renderer.setGridColor(Color.BLUE);//经纬线颜色

        renderer.setMarginsColor(Color.WHITE);     // 外部颜色
        renderer.setBackgroundColor(Color.WHITE); //全局背景颜色
        renderer.setApplyBackgroundColor(false);   //内部底色

        renderer.setPanEnabled(true);   //是否可以拖动

        return renderer;
    }

    private XYSeriesRenderer createLineRender() {
        XYSeriesRenderer xyRenderer = new XYSeriesRenderer();
        xyRenderer.setColor(Color.BLUE);  //线颜色
        xyRenderer.setPointStyle(PointStyle.POINT); //点样式
        xyRenderer.setLineWidth(5); //线宽

        //填充打开
        xyRenderer.setGradientEnabled(true);
        return xyRenderer;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="消息处理接口">
    private SPDevControl spdevControl = SPDevSystem.GetInstance().spdevcontrol;
    private ExecutorService thread = SPDevSystem.GetInstance().systemthreadpool;

    private final int ACKMASK = 0x100;

    private final int SHOWPACKET = 0x01;
    private final int STARTMONITOR = 0x02;
    private final int STARTMONITORACK = STARTMONITOR | ACKMASK;
    private final int STOPCOLLECT = 0x03;
    private final int UPDATESTATE = 0x04;
    private final int STARTTEST = 0x05;
    private final int STARTTESTACK = STARTTEST | ACKMASK;
    private final int CLOSE = 0x06;


    private Handler msgHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATESTATE:
                    setAllButtonEnable(true);
//                    UpdateButtonState(spdevControl.GetControlState());
                    break;
                case SHOWPACKET:
                    drawSpDataPacket((SpectralDataPacket) msg.obj);
                    break;
                case STARTMONITOR:
                    startMonitor();
                    break;
                case STARTMONITORACK:
                    startMonitorAck(msg.arg1);
                    break;
                case STOPCOLLECT:
                    stopCollect();
                    break;
                case STARTTEST:
                    startTest();
                    break;
                case STARTTESTACK:
                    startTestAck(msg.arg1);
                    break;
                case CLOSE:
                    closeDevice();
                    break;
                default:
                    FaultToast.makeText(DevControlActivity.this, "异常消息", Toast.LENGTH_SHORT);
            }
        }
    };

    //绘制曲线
    private void drawSpDataPacket(SpectralDataPacket data) {
        //清除当前数据
        this.series.clear();

        //添加波长数据
        SPData spdata = data.ADValue;
        for (int i = 0; i < spdata.datavalue.length; i++) {
            series.add(spdata.waveIndex[i], spdata.datavalue[i]);
        }
        //更新曲线
        chart_view.invalidate();
    }

    //开始监听设备
    private void startMonitor() {
        FaultToast.makeText(this, "开始监听设备", Toast.LENGTH_SHORT);
        thread.submit(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = STARTMONITORACK;
                msg.arg1 = 0;
                try {
                    //开始硬件模式，连续采集
                    spdevControl.GetDataCollecor().StartSustainCollect(
                            new DataCollectPar(readIntegerTime(), readAverage(), DataCollectPar.LowVolt),
                            1);
                } catch (Exception ex) {
                    msg.arg1 = 1;
                    //这里只需要记录原因，错误打印会打印出来
                    SYSLOG.getLog().PrintLog(Level.SEVERE, ex.toString());
//                    FaultCenter.getReporter().SendFaultReport(Level.SEVERE, "无法监听设备！\r\n" + ex.toString());
                }
                msgHandler.sendMessage(msg);
            }
        });
    }

    //开始监听结果
    private void startMonitorAck(int result) {
        if (result != 0) {
            FaultToast.makeText(this, "无法监听设备！", Toast.LENGTH_SHORT);
        }
        setAllButtonEnable(true);
    }

    private void stopCollect() {
        FaultToast.makeText(this, "停止监听设备", Toast.LENGTH_SHORT);
        thread.submit(new Runnable() {
            @Override
            public void run() {
                spdevControl.GetDataCollecor().StopCollectData();
                msgHandler.sendEmptyMessage(UPDATESTATE);
            }
        });
    }

    private void startTest() {
        thread.submit(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = STARTTESTACK;
                msg.arg1 = 0;

                try {
                    spdevControl.GetDataCollecor().StartSingleTest(
                            new DataCollectPar(readIntegerTime(), readAverage(), DataCollectPar.SoftMode));
                } catch (Exception ex) {
                    msg.arg1 = 1;
                    SYSLOG.getLog().PrintLog(Level.SEVERE, ex.toString());
                }
                msgHandler.sendMessage(msg);
            }
        });
    }

    private void startTestAck(int ret) {
        if (ret != 0) {
            FaultToast.makeText(this, "获取数据失败", Toast.LENGTH_SHORT);
//            UpdateButtonState(this.spdevControl.GetControlState());
        }
        this.setAllButtonEnable(true);
    }

    private void startConfig() {
        if (this.spdevControl.GetControlState() != ISPDevControl.CSTATE.CONNECT) {
            FaultToast.makeText(this, "无法设置设备，当前设备状态：" + this.spdevControl.GetControlState(), Toast.LENGTH_SHORT);
        } else {
            this.setAllButtonEnable(true);
            startActivity(new Intent(DevControlActivity.this, DevConfigActivity.class));
        }
    }

    private void closeDevice() {
        this.setAllButtonEnable(true);
        FaultToast.makeText(this, "关闭设备...", Toast.LENGTH_SHORT);
        thread.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    spdevControl.Delete();
                } catch (Exception e) {
                    FaultCenter.getReporter().SendFaultReport(Level.SEVERE, e.toString());
                } finally {
                    DevControlActivity.this.finish();
                }
            }
        });
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="其它接口">
//    private void UpdateButtonState(ISPDevControl.CSTATE state) {
//        this.isAllenable = true;
//        this.button_config.setEnabled(state == ISPDevControl.CSTATE.CONNECT);
//        this.button_test.setEnabled(state == ISPDevControl.CSTATE.CONNECT);
//        this.tbutton_dark_fliter.setEnabled(state == ISPDevControl.CSTATE.CONNECT);
//        this.button_sartmonitor.setEnabled(state == ISPDevControl.CSTATE.CONNECT);
//        this.button_stopcollect.setEnabled(state == ISPDevControl.CSTATE.COLLECT);
//    }

    private boolean isAllenable = true;

    private void setAllButtonEnable(boolean value) {

        this.isAllenable = value;
        if (!value) {
            this.button_test.setEnabled(value);
            this.button_sartmonitor.setEnabled(value);
            this.button_stopcollect.setEnabled(value);
            this.tbutton_dark_fliter.setEnabled(value);
            this.button_config.setEnabled(value);
        } else {
            ISPDevControl.CSTATE state = SPDevSystem.GetInstance().spdevcontrol.GetControlState();
            this.button_config.setEnabled(state == ISPDevControl.CSTATE.CONNECT);
            this.button_test.setEnabled(state == ISPDevControl.CSTATE.CONNECT);
            this.tbutton_dark_fliter.setEnabled(state == ISPDevControl.CSTATE.CONNECT);
            this.button_sartmonitor.setEnabled(state == ISPDevControl.CSTATE.CONNECT);
            this.button_stopcollect.setEnabled(state == ISPDevControl.CSTATE.COLLECT);
        }
    }

    private class DataUpdateListener extends EventListener<SpectralDataPacket> {
        @Override
        public void recevieEvent(Event<SpectralDataPacket> event) {
            Message msg = new Message();
            msg.what = SHOWPACKET;
            msg.obj = event.GetEvent();
            msgHandler.sendMessage(msg);
        }
    }

    private DataUpdateListener DataUpdateEvent = new DataUpdateListener();

    //读取积分时间
    private float readIntegerTime() {
        try {
            return Float.valueOf(et_integerTime.getText().toString());
        } catch (Exception ex) {
            FaultToast.makeText(this, "无效的积分时间输入", Toast.LENGTH_SHORT);
            this.et_integerTime.setText("1000");
            return 1000f;
        }
    }

    //读取平均值
    private int readAverage() {
        try {
            return Integer.valueOf(et_average.getText().toString());
        } catch (Exception ex) {
            FaultToast.makeText(this, "无效的平均次数输入", Toast.LENGTH_SHORT);
            this.et_average.setText("1");
            return 1;
        }
    }
    // </editor-fold>
}
