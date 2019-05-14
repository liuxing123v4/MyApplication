package com.example.metero.firstapplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyList2Activity extends ListActivity implements Runnable, AdapterView.OnItemLongClickListener,AdapterView.OnItemClickListener {

	Handler handler;
	ListView listView;
	private List<HashMap<String, String>> listItems;
	private SimpleAdapter listItemAdapter;

	private String TAG = "MyListActivity";

	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_my_list);

//		listView = findViewById(R.id.myList);
		String data[] = {"one", "two"};

		this.setListAdapter(listItemAdapter);

		//开启子线程
		Thread thread = new Thread(this); //注意！必须加this
		thread.start(); // 调用run方法

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 7) {
					listItems = (List<HashMap<String, String>>) msg.obj;
					listItemAdapter = new SimpleAdapter(MyList2Activity.this, listItems,
							R.layout.list_item,
							new String[]{"ItemTitle", "ItemDetail"},
							new int[]{R.id.itemTitle, R.id.itemDetail}
					);
					setListAdapter(listItemAdapter);
				}
				super.handleMessage(msg);
			}
		};

		//列表绑定监听器
		getListView().setOnItemClickListener(this);
		getListView().setOnItemLongClickListener(this);
	}

	public void run() {
		//获取网络数据，放入list带回主线程
		List<HashMap<String, String>> retList = new ArrayList<HashMap<String, String>>();

		Document doc = null;
		try {
			//jsoup直接从网络地址获取
			doc = Jsoup.connect("http://www.boc.cn/sourcedb/whpj").get();//获得connect对象，get方法获得对应doc
			Log.i(TAG, "run: " + doc.title());
			Elements tables = doc.getElementsByTag("table");
			Element table = tables.get(1);
			//获取td中的数据
			Elements tds = table.getElementsByTag("td");

			//创建list存放数据
			for (int i = 0; i < tds.size(); i += 8) {
				Element td1 = tds.get(i);
				Element td2 = tds.get(i + 5);
				String str = td1.text();
				String val = td2.text();
				Log.i(TAG, "run: " + str + "==>" + val);
				HashMap<String, String> map = new HashMap<>();
				map.put("ItemTitle", str);
				map.put("ItemDetail", val);
				retList.add(map);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		Message msg = handler.obtainMessage(7);
		msg.obj = retList;
		handler.sendMessage(msg);
	}


	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
		// view是listview的item
//		有两种方式获得列表数据：
//		 1.通过getListView().getItemIdAtPosition(position)获得map-通过get方法
//        HashMap<String,String> map = (HashMap<String,String>)getListView().getItemAtPosition(position);
//        String title = map.get("ItemTitle");
//		  String detailStr = map.get("ItemDetail")
//		2.通过view.findViewById()获得控件，从而获得对应数据
//        TextView detail = view.findViewById(R.id.itemDetail);
//        detail.getText();

		//获取货币及汇率
		TextView title = view.findViewById(R.id.itemTitle);
		TextView detail = view.findViewById(R.id.itemDetail);
		String ItemTitle = title.getText().toString();
		String ItemDetail = detail.getText().toString();
		Log.i(TAG, "onItemClick: name=" + ItemTitle);
		Log.i(TAG, "onItemClick: rate=" + ItemDetail);

//		//打开新的页面，传入参数
//		Intent rateCal = new Intent(this, RateCalActivity.class);
//		rateCal.putExtra("title", ItemTitle);
//		rateCal.putExtra("rate", Float.parseFloat(ItemDetail));
//		startActivity(rateCal);
		// 创建对话框构建器

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// 获取布局
		View view2 = View.inflate(this, R.layout.activity_rate_cal, null);
		// 获取布局中的控件
		final TextView name = (TextView) view2.findViewById(R.id.cal_name);
		final EditText input = (EditText) view2.findViewById(R.id.cal_inp);
		final TextView value = (TextView) view2.findViewById(R.id.cal_value);
		// 设置参数
		builder.setTitle("实时汇率计算").setIcon(R.drawable.money)
				.setView(view2);
		// 创建对话框
		final AlertDialog alertDialog = builder.create();
		name.setText(ItemTitle);
		final float rate = Float.parseFloat(ItemDetail);


		input.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@SuppressLint("SetTextI18n")
			@Override
			public void afterTextChanged(Editable s) {
				if(s.length()>0){
					float val = Float.parseFloat(s.toString());
					value.setText(val+"===>"+(100/rate*val));
				}
				else{
					value.setText("");
				}
			}
		});
		alertDialog.show();

	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
		Log.i(TAG, "onItemLongClick: 长按列表项：pisition = " + position);
		//删除操作
//		listItems.remove(position);
//		listItemAdapter.notifyDataSetChanged();
		//删除数据 长安
		//构造对话框
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("提示").setMessage("请确认是否删除当前数据")
				.setPositiveButton("是", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Log.i(TAG, "onClick: 对话框事件处理");
						listItems.remove(position);
						listItemAdapter.notifyDataSetChanged();
					}
				})
				.setNegativeButton("否", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Log.i(TAG, "onClick: 对话框 事件处理");
					}
				});
		builder.show();


		return true;
	}
}

