package com.nahon.sobs.LamApp;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.nahon.io.IOManager;
import com.nahon.io.USBManager;

import java.util.logging.Level;

import nahon.comm.event.Event;
import nahon.comm.event.EventListener;
import nahon.comm.faultsystem.FaultCenter;
import sps.dev.data.SSPDevInfo;
import sps.platform.SpectralPlatService;
import sps.platform.log.SYSLOG;

public class DevListActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_list);

        //初始化系统
        this.initSystem();

        USBManager.InitUSBManager(this.getBaseContext());

        //初始化界面
        this.initView();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    //初始化系统
    private void initSystem() {
        //打印系统错误
        FaultCenter.Instance().RegisterEvent(new EventListener<Level>() {
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
            FaultCenter.Instance().SendFaultReport(Level.SEVERE, ex.toString());
        }
    }

    // <editor-fold defaultstate="collapsed" desc="初始化界面">
    private Button search_button;
    private ListView dev_name_List;
    private LayoutInflater inflater;
    private dev_nameListAdapter adapter;
    private boolean loading = false;

    private void initView() {
        this.inflater = LayoutInflater.from(this);

        //初始化设备列表
        dev_name_List = (ListView) this.findViewById(R.id.dev_list_view);
        this.adapter = new dev_nameListAdapter();
        dev_name_List.setAdapter(this.adapter);

        //list 选择响应
        dev_name_List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                selectDevice(position);
            }
        });

        //搜索按钮初始化
        this.search_button = (Button) this.findViewById(R.id.button_search);
        this.search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_button.setEnabled(false);
                handler.sendEmptyMessage(SEARCHDEV);
            }
        });
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("DevList Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    //设备列表适配器
    private class dev_nameListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return SpectralPlatService.GetInstance().GetDevManager().GetDeviceList().size();
        }

        @Override
        public Object getItem(int position) {
            return SpectralPlatService.GetInstance().GetDevManager().GetDeviceList().get(position).GetDevInfo();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView dev_name;
            if (convertView == null) {
                //初始化list 界面条
                convertView = inflater.inflate(R.layout.devnamelist_item, null);

                //list界面条元素
                dev_name = (TextView) convertView.findViewById(R.id.dev_name);
                convertView.setTag(dev_name);
            } else {
                dev_name = (TextView) convertView.getTag();
            }
            //更新名称和地址
            SSPDevInfo connInfo = (SSPDevInfo) this.getItem(position);
            dev_name.setText(connInfo.eia.DeviceName + "(" + connInfo.ioinfo.iotype + ")");
            return convertView;
        }

    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="消息处理接口">
    private final int ACKMASK = 0x100;

    private final int SEARCHDEV = 0x01;
    private final int SEARCHDEVACK = SEARCHDEV | ACKMASK;
    private final int SELECTDEV = 0x02;
    private final int FAULTPRINT = 0x03;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SEARCHDEV:
                    searchDevice();
                    break;
                case SEARCHDEVACK:
                    searchDeviceAck();
                    break;
                case SELECTDEV:
                    startActivity(new Intent(DevListActivity.this, DevControlActivity.class));
                    break;
                case FAULTPRINT:
                    FaultToast.makeText(DevListActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT);
                    break;
                default:
                    FaultToast.makeText(DevListActivity.this, "异常消息", Toast.LENGTH_SHORT);
            }
        }

        //加载本地IP
//        private void loadLocalIp() {
//            SpectralPlatService.GetInstance().GetThreadPools().submit(new Runnable() {
//                @Override
//                public void run() {
//                    WifiManager wifiManager = (WifiManager) DevSearchActivity.this.getSystemService(Context.WIFI_SERVICE);
//                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//                    etext_ipaddr.setText(Formatter.formatIpAddress(wifiInfo.getIpAddress()));
//                    etext_portnum.setText("2000");
//                }
//            });
//        }


    };

    //搜索设备
    private void searchDevice() {
        SpectralPlatService.GetInstance().GetThreadPools().submit(new Runnable() {
            @Override
            public void run() {
                //搜索光谱仪设备
                //FaultToast.makeText(DevListActivity.this, "搜索USB" + IOManager.CreateIOList().length, Toast.LENGTH_SHORT);
                SpectralPlatService.GetInstance().GetDevManager().StartSearchIO(IOManager.CreateIOList());
                //搜索到设备，刷新界面
                 handler.sendEmptyMessage(SEARCHDEVACK);
            }
        });

    }

    //搜索设备结果
    private void searchDeviceAck() {
        if (SpectralPlatService.GetInstance().GetDevManager().GetDeviceList().isEmpty()) {
            FaultCenter.Instance().SendFaultReport(Level.SEVERE, " 没有设备");
        } else {
            int foundeddev = SpectralPlatService.GetInstance().GetDevManager().GetDeviceList().size();
            FaultToast.makeText(DevListActivity.this, "找到设备: " + foundeddev, Toast.LENGTH_SHORT);
        }

        this.adapter.notifyDataSetChanged();
        search_button.setEnabled(true);
    }

    //响应选择设备
    private void selectDevice(final int index) {
        if (!loading) {
            this.loading = true;
            FaultToast.makeText(this, "加载设备...", Toast.LENGTH_SHORT);
            SpectralPlatService.GetInstance().GetThreadPools().submit(new Runnable() {
                @Override
                public void run() {
                    //打开选择的设备
                    if (SpectralPlatService.GetInstance().GetDevManager().GetDevControl().Open(
                            SpectralPlatService.GetInstance().GetDevManager().GetDeviceList().get(index))) {

                        //触发打开成功
                        handler.sendEmptyMessage(SELECTDEV);
                    } else {
                        FaultCenter.Instance().SendFaultReport(Level.SEVERE, "无法打开设备");
                    }
                    //加载结束
                    loading = false;
                }
            });
        }
    }
    // </editor-fold>


}
