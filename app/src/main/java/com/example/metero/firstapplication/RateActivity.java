package com.example.metero.firstapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class RateActivity extends AppCompatActivity {
    EditText rmb;
    TextView show;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        rmb = (EditText)findViewById(R.id.rmb);
        show = (TextView)findViewById(R.id.showout);

    }
    public void onClick(View v){
        String str = rmb.getText().toString();
        float r = 0;
        if(str.length()>0){
            r = Float.parseFloat(str);
        }
        if(v.getId()==R.id.bnt_dollar){
            float val = r *(1/6.7f);
            show.setText(String.valueOf(val));
        }else if(v.getId()==R.id.bnt_euro){
            float val = r*(1/11f);
            show.setText(String.valueOf(val));
        }else{
            float val = r * 500;
            show.setText(String.valueOf(val));
        }


    }
}
