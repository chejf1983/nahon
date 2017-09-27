package com.nahon.sobs.LamApp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

    public void addTitle(LinearLayout list_view, int icon_id, String text){
        @SuppressLint("InflateParams")
        View v = LayoutInflater.from(this.context).inflate(R.layout.config_title, null);
        v.setBackgroundColor(Color.LTGRAY);

        TextView title = (TextView)v.findViewById(R.id.item_title_name);
        title.setText(text);

        ImageView icon = (ImageView) v.findViewById(R.id.item_title_image);
        if(icon_id != 0) {
            icon.setVisibility(View.VISIBLE);
            icon.setImageResource(icon_id);
        }else{
            icon.setVisibility(View.GONE);
        }

        list_view.addView(v, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                this.dip2px(48)));
    }

    public TextView addTextColumn(LinearLayout list_view, String column_name, String def_value, boolean divide_line){
        @SuppressLint("InflateParams")
        View v = LayoutInflater.from(this.context).inflate(R.layout.config_column_text, null);

        if(divide_line){
            LinearLayout line = (LinearLayout)v.findViewById(R.id.item_column_line);
            line.setVisibility(View.VISIBLE);
        }

        TextView item_name = (TextView)v.findViewById(R.id.item_column_title);
        item_name.setText(column_name);

        TextView item_content = (TextView)v.findViewById(R.id.item_column_content);
        item_content.setText(def_value);

        list_view.addView(v, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                this.dip2px(48)));

        return item_content;
    }

    public EditText addEditNumColumn(LinearLayout list_view, String column_name, float value, boolean divide_line){
        @SuppressLint("InflateParams")
        View v = LayoutInflater.from(this.context).inflate(R.layout.config_column_editnum, null);

        if(divide_line){
            LinearLayout line = (LinearLayout)v.findViewById(R.id.item_column_line);
            line.setVisibility(View.VISIBLE);
        }

        TextView item_name = (TextView)v.findViewById(R.id.item_column_title);
        item_name.setText(column_name);

        EditText item_content = (EditText)v.findViewById(R.id.item_column_content);
        item_content.setText(String.valueOf(value));

        list_view.addView(v, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                this.dip2px(48)));

        return item_content;
    }
}
