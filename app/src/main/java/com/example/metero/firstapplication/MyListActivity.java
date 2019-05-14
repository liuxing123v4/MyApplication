package com.example.metero.firstapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MyListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
	List<String> data = new ArrayList<String>();
	String TAG = "Mylist";
	ArrayAdapter adapter;
	//ArrayAdapter 可以直接使用remove方法
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_list);

		ListView listView = (ListView) findViewById(R.id.mylist);
		for(int i=0;i<10;i++){
			data.add("item"+i);
		}

//		String data[] = {"111","222"};//绑定数据和布局
		adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);
		listView.setAdapter(adapter);
		listView.setEmptyView(findViewById(R.id.nodata));
		listView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		//parent ==当前的listview
		Log.i(TAG, "onItemClick: position"+position);
		adapter.remove(parent.getItemAtPosition(position));
	}
}
