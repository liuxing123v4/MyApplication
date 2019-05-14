package com.example.metero.firstapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RateActivity extends AppCompatActivity implements Runnable{
    private final String TAG = "Rate";
    EditText rmb;
    TextView show;
    private float dollarRate = 0.1f;
    private float euroRate = 0.2f;
    private float wonRate = 0.3f;
    private String updateDate = "";
    Handler handler;
    private SharedPreferences sharedPreferences;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        rmb = (EditText)findViewById(R.id.rmb);
        show = (TextView)findViewById(R.id.showout);
        //获取sp里面的数据
        //开始打开时会去寻找配置文件
        SharedPreferences sharedPreferences = getSharedPreferences("my_rate",Activity.MODE_PRIVATE);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);//推荐把关键问题写在一个配置文件中
        dollarRate = sharedPreferences.getFloat("dollar_rate",0.0f);//任何值设置时都会有默认数据
        euroRate = sharedPreferences.getFloat("euro_rate",0.0f);
        wonRate = sharedPreferences.getFloat("won_rate",0.0f);
        updateDate = sharedPreferences.getString("updata_date","");
        
        //获取当前系统时间
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//小写mm分钟数，大写MM月份
        final String todayStr = sdf.format(today);

        Log.i(TAG, "dollarRate= "+dollarRate);
        Log.i(TAG, "euroRate= "+euroRate);
        Log.i(TAG, "wonRate= "+wonRate);
        Log.i(TAG, "onCreate: update="+updateDate);
        Log.i(TAG, "onCreate: todayStr="+todayStr);

        if(!todayStr.equals(updateDate)){
            Log.i(TAG, "onCreate: 需要更新");
            //开启子线程
            Thread t =new Thread(this);
            t.start();
        }else{
            Log.i(TAG, "onCreate: 不需要更新");
        }
        //开启子线程
        Thread t = new Thread( this);
        t.start();

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==5){
                    Bundle bdl = (Bundle)msg.obj;
                    dollarRate = bdl.getFloat("dollar_rate");
                    euroRate = bdl.getFloat("euro_rate");
                    wonRate = bdl.getFloat("won_rate");

                    //保存更新的日期
                    SharedPreferences sharedPreferences = getSharedPreferences("my_rate",Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("updata_date",todayStr);
                    editor.apply();

                    Log.i(TAG, "handleMessage: dollarRate:"+dollarRate);
                    Log.i(TAG, "handleMessage: euroRate:"+euroRate);
                    Log.i(TAG, "handleMessage: wonRate:"+wonRate);
                    Toast.makeText(RateActivity.this,"汇率已更新",Toast.LENGTH_SHORT).show();
                }
                super.handleMessage(msg);
            }
        };
    }
    public void onClick(View v){
        String str = rmb.getText().toString();
        float r = 0;
        if(str.length()>0){
            r = Float.parseFloat(str);
        }
        if(v.getId()==R.id.bnt_dollar){
            float val = r *dollarRate;
            show.setText(String.valueOf(val));
        }else if(v.getId()==R.id.bnt_euro){
            float val = r*euroRate;
            show.setText(String.valueOf(val));
        }else{
            float val = r * wonRate;
            show.setText(String.valueOf(val));
        }
    }

    public void openOne(View v){
        openConfig();
//        Intent intentPhone = new Intent(Intent.ACTION_CALL, Uri.parse("tel:87092173"));

//        Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.jd.com"));
//        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rate,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() ==R.id.menu_set){
            openConfig();
        }else if(item.getItemId()==R.id.open_list){
            Intent list = new Intent(this,MyList2Activity.class);
            startActivity(list);
        }
        return super.onOptionsItemSelected(item);
    }

    private void openConfig() {
        //打开新的页面：
        Intent config = new Intent(this,ConfigActivity.class);

        //在Intent内可以携带内容进去，常用基本类型都有
        config.putExtra("dollar_rate_key",dollarRate);
        config.putExtra("euro_rate_key",euroRate);
        config.putExtra("won_rate_key",wonRate);

        Log.i(TAG, "dollarRate= "+dollarRate);
        Log.i(TAG, "euroRate= "+euroRate);
        Log.i(TAG, "wonRate= "+wonRate);
        //打开新页面
        //startAcitivity(config)直接打开
        startActivityForResult(config,1);//这里的数字都行 打开这个窗口是为了带回数据
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode==1 && resultCode==2){
            Bundle bundle = data.getExtras();
            dollarRate = bundle.getFloat("key_dollar",0.1f);
            euroRate = bundle.getFloat("key_euro",0.1f);
            wonRate = bundle.getFloat("key_won",0.1f);
            Log.i(TAG, "onActivityResult: dollarRate="+dollarRate);
            Log.i(TAG, "onActivityResult: wonRate="+wonRate);
            Log.i(TAG, "onActivityResult: euroRate="+euroRate);
            //将新设置的汇率写在SP里
            SharedPreferences sharedPreferences = getSharedPreferences("my_rate",Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putFloat("dollar_rate",dollarRate);
            editor.putFloat("euro_rate",euroRate);
            editor.putFloat("won_rate",wonRate);
            editor.apply();
            Log.i(TAG, "onActivityResult:数据已经保存在sharePrefence中 ");
        }
        super.onActivityResult(requestCode,resultCode,data);
    }

    public void run(){
        Log.i(TAG, "run: and run ");
        for(int i=1;i<6;i++){
            Log.i(TAG, "run:"+i);
            try{
                Thread.sleep(200);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        //用于保存获取的汇率
        Bundle bundle;
        bundle = getFromUsdCny();

        //获取网络数据
//        URL url = null;
//        try {
//            url = new URL("http://www.usd-cny.com/icbc.htm");
//            HttpURLConnection http = (HttpURLConnection) url.openConnection();
//            InputStream in = http.getInputStream();
//            String html = inputStream2String(in);
//            Log.i(TAG, "run: =" + html);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        bundle = getFromBOC();



        //获取Msg对象 用于返回主线程
        Message msg = handler.obtainMessage(5);
//        //msg.what = 5;
//        msg.obj = "Hello from run()";
        msg.obj =bundle;
        handler.sendMessage(msg);

    }
//    获取工商银行数据
    private Bundle getFromBOC() {
        Document doc = null;
        Bundle bundle = new Bundle();
        try{
            doc = Jsoup.connect("http://www.usd-cny.com/icbc.htm").get();
            Log.i(TAG, "run: "+doc.title());
            Elements tables = doc.getElementsByTag("table");
//            int i = 1;

//            for(Element table : tables){
//                Log.i(TAG, "run: table["+i+"] = "+table);
//                i++;
//            }
            Element table = tables.get(0);
            Log.i(TAG, "run: table="+table);
            Elements tds = table.getElementsByTag("td");
            for(int i= 0; i<tds.size();i+=5){
                Element td1 = tds.get(i);
                Element td2 = tds.get(i+3);
                Log.i(TAG, "run: "+td1.text()+"==>"+td2.text());
                if("美元".equals(td1.text())){
                    bundle.putFloat("dollar_rate",100f/Float.parseFloat(td2.text()));
                }else if("欧元".equals(td1.text())){
                    bundle.putFloat("euro_rate",100f/Float.parseFloat(td2.text()));
                }else if("韩国元".equals(td1.text())){
                    bundle.putFloat("won_rate",100f/Float.parseFloat(td2.text()));
                }

            }
//            for(Element td:tds){
//                Log.i(TAG, "run: td="+td);
//                Log.i(TAG, "run: text= "+td.text());
//                Log.i(TAG, "run: html="+td.html());
//            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return bundle;
    }

    //    获取中国银行数据
    private Bundle getFromUsdCny() {
        Document doc = null;
        Bundle bundle = new Bundle();
        try{
            doc = Jsoup.connect("http://www.boc.cn/sourcedb/whpj/").get();
            Log.i(TAG, "run: "+doc.title());
            Elements tables = doc.getElementsByTag("table");
//            int i = 1;

//            for(Element table : tables){
//                Log.i(TAG, "run: table["+i+"] = "+table);
//                i++;
//            }
            //定位到第二个table就行
            Element table = tables.get(1);
            Elements tds = table.getElementsByTag("td");
            for(int i= 0; i<tds.size();i+=8){
                Element td1 = tds.get(i);
                Element td2 = tds.get(i+5);
                Log.i(TAG, "run: "+td1.text()+"==>"+td2.text());
                if("美元".equals(td1.text())){
                    bundle.putFloat("dollar_rate",100f/Float.parseFloat(td2.text()));
                }else if("欧元".equals(td1.text())){
                    bundle.putFloat("euro_rate",100f/Float.parseFloat(td2.text()));
                }else if("韩国元".equals(td1.text())){
                    bundle.putFloat("won_rate",100f/Float.parseFloat(td2.text()));
                }

            }
//            for(Element td:tds){
//                Log.i(TAG, "run: td="+td);
//                Log.i(TAG, "run: text= "+td.text());
//                Log.i(TAG, "run: html="+td.html());
//            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return bundle;
    }

    private String inputStream2String(InputStream inputStream) throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream,"gb2312");
        for(;;){
            int rsz = in.read(buffer,0,buffer.length);
            if(rsz<0)
                break;
            out.append(buffer,0,rsz);
        }
        return out.toString();
    }
}
