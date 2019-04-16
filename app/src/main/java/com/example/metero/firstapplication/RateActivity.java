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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RateActivity extends AppCompatActivity implements Runnable{
    private final String TAG = "Rate";
    EditText rmb;
    TextView show;
    private float dollarRate = 0.1f;
    private float euroRate = 0.2f;
    private float wonRate = 0.3f;
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

        //开启线程
        Thread t = new Thread( this);
        t.start();

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==5){
                    String str =(String)msg.obj;
                    Log.i(TAG, "handleMessage: msg="+str);
                    show.setText(str);
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
        //获取Msg对象 用于返回主线程
        Message msg = handler.obtainMessage(5);
        //msg.what = 5;
        msg.obj = "Hello from run()";
        handler.sendMessage(msg);

        //获取网络数据
        URL url = null;
        try {
            url = new URL("http://www.usd-cny.com/icbc.htm");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            InputStream in = http.getInputStream();
            String html = inputStream2String(in);
            Log.i(TAG, "run: =" + html);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


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
