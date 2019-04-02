package com.example.metero.firstapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;

public class HelloActivity extends AppCompatActivity {

    TextView out;
    EditText inp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //方法执行都写在里面 其它的类的命名写在外面
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);

        out = (TextView) findViewById(R.id.input_text);

        inp = findViewById(R.id.input_hint);
        //String str = inp.getText().toString();

        Button btn = findViewById(R.id.button1);
        //btn.setOnClickListener(this); //需要在类的开始写onclick的接口 按钮按下来的时候传给当前对象 当前对象调用Onclick的方法
    }


    public void output(View v) {
        Log.i("main","onClick msg");
        //如何获取摄氏度
        String str = inp.getText().toString();
        double value = Double.valueOf(str.toString());
        //转化为华氏度
        value = value*9/5+32;
        DecimalFormat df = new DecimalFormat("#.00");
        out.setText("结果为:"+df.format(value)+"F");
    }
}



