package com.nahon.sobs.LamApp;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.logging.Level;

import lam.faultcenter.FaultCenter;
import nahon.dev.search.NahonDevInfo;
import sps.system.SPDevSystem;

public class DevListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dev_list);

        this.initView();
    }

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
    }

    //响应选择设备
    private void selectDevice(final int index) {
        if (!loading) {
            this.loading = true;
            FaultToast.makeText(this, "加载设备...", Toast.LENGTH_SHORT);
            SPDevSystem.GetInstance().systemthreadpool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        //打开选择的设备
                        SPDevSystem.GetInstance().spdevcontrol.Open(
                                SPDevSystem.GetInstance().dev_manager.GetDeviceList().get(index));
                        loading = false;
                        //触发打开成功
                        handler.sendEmptyMessage(0);
                    } catch (Exception e) {
                        FaultCenter.getReporter().SendFaultReport(Level.SEVERE, "无法打开设备" + e.toString());
                    } finally {
                        //加载结束
                        loading = false;
                    }
                }
            });
        }
    }

    //消息转发中心
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            //打开成功，进入到设备控制界面
            startActivity(new Intent(DevListActivity.this, DevControlActivity.class));
        }
    };

    //设备列表适配器
    private class dev_nameListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return SPDevSystem.GetInstance().dev_manager.GetDeviceList().size();
        }

        @Override
        public Object getItem(int position) {
            return SPDevSystem.GetInstance().dev_manager.GetDeviceList().get(position);
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
            NahonDevInfo connInfo = (NahonDevInfo) this.getItem(position);
            dev_name.setText(connInfo.eia.DeviceName + "(" + connInfo.io.GetConnectInfo().par[0] + ")");
            return convertView;
        }

    }

}
