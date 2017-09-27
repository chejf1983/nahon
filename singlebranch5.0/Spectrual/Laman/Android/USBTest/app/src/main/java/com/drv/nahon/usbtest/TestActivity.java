package com.drv.nahon.usbtest;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
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

import com.drv.nahon.usb.LibUSB;
import com.drv.nahon.usb.USBManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;

public class TestActivity extends AppCompatActivity {

    //usb设备列表
    private ListView dev_name_List;
    private LayoutInflater inflater;
    private dev_nameListAdapter adapter;

    //usb列表明恒
    private ArrayList<String> usblist = new ArrayList();

    //搜索按钮
    private Button button_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        this.initView();

    }

    private void initView() {
        this.inflater = LayoutInflater.from(this);

        //初始USB设备列表
        this.dev_name_List = (ListView) this.findViewById(R.id.usb_list_view);
        this.adapter = new dev_nameListAdapter();
        this.dev_name_List.setAdapter(this.adapter);

        //USB设备列表选择响应
        this.dev_name_List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //selectDevice(position);
            }
        });


        this.button_search = (Button)this.findViewById(R.id.usb_search);
        this.button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchUsb();
            }
        });


        //list 选择响应
        dev_name_List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                selectDevice(position);
            }
        });
    }

    //设备列表适配器
    private class dev_nameListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return usblist.size();
        }

        @Override
        public Object getItem(int position) {
            return usblist.get(position);
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
                //convertView = inflater.inflate(R.layout.devnamelist_item, null);

                //list界面条元素
                //dev_name = (TextView) convertView.findViewById(R.id.dev_name);
                //convertView.setTag(dev_name);
                dev_name = new TextView(inflater.getContext());
                dev_name.setTextSize(50);
                convertView = dev_name;
            } else {
                dev_name = (TextView) convertView.getTag();
            }
            //更新名称和地址
            //NahonDevInfo connInfo = (NahonDevInfo) this.getItem(position);
            dev_name.setText(usblist.get(position));
            return convertView;
        }

    }

    private void SearchUsb() {
        usblist.clear();
        int devnum = USBManager.USBScanDevImpl(this.getBaseContext());
        for(int i = 0; i < devnum; i++){
            UsbDevice dev = USBManager.GetDevList()[i];
            String des = dev.getDeviceName();
            for(int j = 0; j < dev.getInterfaceCount(); j++){
                des += "intef" + j + " " + dev.getInterface(j).getEndpointCount();
            }
            usblist.add(des);
        }
        adapter.notifyDataSetChanged();
    }

    //响应选择设备
    private void selectDevice(int index) {
        startActivity(new Intent(this, SendReceive.class));
    }
}
