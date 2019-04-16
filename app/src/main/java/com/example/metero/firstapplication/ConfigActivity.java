package com.example.metero.firstapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class ConfigActivity extends AppCompatActivity {
    public final String TAG ="ConfigActivity";

    EditText dollarText;
    EditText euroText;
    EditText wonText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        //获取之前携带的数据
        Intent intent = getIntent();
        //是什么类型就用什么类型去取
        float dollar2 = intent.getFloatExtra("dollar_rate_key",0.0f);//变量必须一样，后面是默认值
        float euro2 = intent.getFloatExtra("euro_rate_key",0.0f);
        float won2 = intent.getFloatExtra("won_rate_key",0.0f);

        dollarText = (EditText) findViewById(R.id.dollar_rate);
        euroText =(EditText) findViewById(R.id.euro_rate);
        wonText = (EditText)findViewById(R.id.won_rate);
        //显示数据到控件
        dollarText.setText(String.valueOf(dollar2));
        euroText.setText(String.valueOf(euro2));
        wonText.setText(String.valueOf(won2));

        Log.i(TAG, "onCreate: dollar2 = "+dollar2);
        Log.i(TAG, "onCreate: euro2 = "+euro2);
        Log.i(TAG, "onCreate: won2 = "+won2);
    }
    public void save(View v){

        //获取新的值
        float newDollar = Float.parseFloat(dollarText.getText().toString());
        float newEuro = Float.parseFloat(euroText.getText().toString());
        float newWon = Float.parseFloat(wonText.getText().toString());

        Log.i(TAG, "save: 获取到新值");
        Log.i(TAG, "save: newDollar"+newDollar);
        Log.i(TAG, "save: newEuro"+newEuro);
        Log.i(TAG, "save: newWon"+newWon);
        //保存到Bundle或者Extra中
        Intent intent = getIntent();
        Bundle bdl = new Bundle();
        bdl.putFloat("key_dollar",newDollar);
        bdl.putFloat("key_euro",newEuro);
        bdl.putFloat("key_won",newWon);
        intent.putExtras(bdl);
        setResult(2,intent);

        Log.i("cfg", "save: ");
        //返回到调用页面
        finish();
    }


}
