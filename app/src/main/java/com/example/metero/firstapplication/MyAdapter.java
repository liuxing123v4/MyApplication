package com.example.metero.firstapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyAdapter extends ArrayAdapter {

	public MyAdapter(Context context, int resource, ArrayList<HashMap<String,String>> list) {
		super(context, resource, list);
	}

	public View getView(int position,View convertView, ViewGroup parent) {
		View itemView = convertView;//来源于第二个参数 从父类继承
		if(itemView==null){
			itemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
//parent 属于当前列表
		}

		Map<String,String> map = (Map<String,String>) getItem(position);//获得每一个位置的对象（此处有两个对象
		TextView title = (TextView) itemView.findViewById(R.id.itemTitle);
		TextView detail = (TextView) itemView.findViewById(R.id.itemDetail);
		title.setText("Title:"+map.get("ItemTitle"));
		detail.setText("detail:"+map.get("ItemDetail"));
		return itemView;//用于填充每一行的数据

	}
}
