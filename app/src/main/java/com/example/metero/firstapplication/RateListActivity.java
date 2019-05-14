package com.example.metero.firstapplication;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RateListActivity extends ListActivity implements Runnable{
	private final String TAG = "Rate";//有了父类就不需要用布局去填充它 8
	String data[] = {"one","two","three"};
	Handler mHandler;

	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {//adapter是去联系data 和listview的关系

		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_rate_list);
		//范型：数据项是什么描述
		List<String> list1 = new ArrayList<String>();

		for(int i=1;i<100;i++){
			list1.add("item"+i);
		}

		ListAdapter adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list1);//当前对象，布局，数据
		setListAdapter(adapter);

		Thread thread = new Thread(this);
		thread.start();
		mHandler = new Handler(){
			public void handleMessage(Message msg) {
				if(msg.what==7){
					List<String> list2 = (List<String>) msg.obj;
					ListAdapter adapter = new ArrayAdapter<String>(RateListActivity.this,android.R.layout.simple_list_item_1,list2);
					setListAdapter(adapter);
				}
				super.handleMessage(msg);
			}
		};
	}

	@Override
	public void run() {
		//获取网络数据，放入到list带回到主线程中
		List<String> retList = new ArrayList<String>();
		Document doc = null;
		Bundle bundle = new Bundle();
		try{
			Thread.sleep(3000);
			doc = Jsoup.connect("http://www.boc.cn/sourcedb/whpj/").get();
			Log.i(TAG, "run: "+doc.title());
			Elements tables = doc.getElementsByTag("table");
//            int i = 1;

//            for(Element table : tables){
//                Log.i(TAG, "run: table["+i+"] = "+table);
//                i++;
//            }
			Element table = tables.get(1);
			Log.i(TAG, "run: table="+table);
			Elements tds = table.getElementsByTag("td");
			for(int i= 0; i<tds.size();i+=8){
				Element td1 = tds.get(i);
				Element td2 = tds.get(i+5);
				String str1 = td1.text();
				String str2 = td2.text();
				Log.i(TAG, "run: "+str1+"==>"+str2);
				retList.add(str1+"==>"+str2);
			}
//            for(Element td:tds){
//                Log.i(TAG, "run: td="+td);
//                Log.i(TAG, "run: text= "+td.text());
//                Log.i(TAG, "run: html="+td.html());
//            }
		}catch (IOException e){
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Message msg = mHandler.obtainMessage(7);
//        //msg.what = 5;
//        msg.obj = "Hello from run()";
		msg.obj =retList;
		mHandler.sendMessage(msg);


	}

	}


