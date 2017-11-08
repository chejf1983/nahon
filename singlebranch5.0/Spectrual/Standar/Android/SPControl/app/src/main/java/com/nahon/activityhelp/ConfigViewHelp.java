package com.nahon.activityhelp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nahon.spcontrol.R;

/**
 * Created by jiche on 2016/11/23.
 */

public class ConfigViewHelp {
    private final Context context;
    public ConfigViewHelp(Context context){
        this.context = context;
    }

    private int dip2px(float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    //添加标题
    public void addTitle(LinearLayout list_view, int icon_id, String text){
        @SuppressLint("InflateParams")
        View v = LayoutInflater.from(this.context).inflate(R.layout.config_title, null);
        //设置标题背景颜色
        v.setBackgroundColor(Color.LTGRAY);

        //标题的文字
        TextView title = (TextView)v.findViewById(R.id.item_title_name);
        title.setText(text);

        //标题的图标
        ImageView icon = (ImageView) v.findViewById(R.id.item_title_image);
        if(icon_id != 0) {
            icon.setVisibility(View.VISIBLE);
            icon.setImageResource(icon_id);
        }else{
            icon.setVisibility(View.GONE);
        }

        //添加标题栏到滚动layout中
        list_view.addView(v, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                this.dip2px(48)));
    }

    //添加纯文字列
    public TextView addTextColumn(LinearLayout list_view, String column_name, String def_value, boolean divide_line){
        @SuppressLint("InflateParams")
        View v = LayoutInflater.from(this.context).inflate(R.layout.config_column_text, null);

        //是否添加分割线
        if(divide_line){
            LinearLayout line = (LinearLayout)v.findViewById(R.id.item_column_line);
            line.setVisibility(View.VISIBLE);
        }

        //添加文字框说明
        TextView item_name = (TextView)v.findViewById(R.id.item_column_title);
        item_name.setText(column_name);

        //添加文字框内容
        TextView item_content = (TextView)v.findViewById(R.id.item_column_content);
        item_content.setText(def_value);

        list_view.addView(v, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                this.dip2px(48)));

        //返回内容view
        return item_content;
    }

    public Button addButtonText(LinearLayout list_view, String button_name, String def_value, boolean divide_lin){
        return null;
    }

    //添加可编辑文字列
    public EditText addEditNumColumn(LinearLayout list_view, String column_name, float value, boolean divide_line){
        @SuppressLint("InflateParams")
        View v = LayoutInflater.from(this.context).inflate(R.layout.config_column_editnum, null);

        //是否添加分割线，表示一段的结尾
        if(divide_line){
            LinearLayout line = (LinearLayout)v.findViewById(R.id.item_column_line);
            line.setVisibility(View.VISIBLE);
        }

        //添加文字说明框
        TextView item_name = (TextView)v.findViewById(R.id.item_column_title);
        item_name.setText(column_name);

        //添加编辑文字框
        EditText item_content = (EditText)v.findViewById(R.id.item_column_content);
        item_content.setText(String.valueOf(value));

        list_view.addView(v, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                this.dip2px(48)));

        //返回可以编辑的view
        return item_content;
    }
}
