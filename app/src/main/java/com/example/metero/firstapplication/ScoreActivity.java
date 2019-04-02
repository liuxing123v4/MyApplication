package com.example.metero.firstapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity {

    TextView scorea;
    TextView scoreb;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        scorea = (TextView) findViewById(R.id.scorea);
        scoreb = (TextView) findViewById(R.id.scoreb);
    }

    public void add_bnt3(View bnt){
        if(bnt.getId()==R.id.bnt_threea ){
            showScorea(3);
        }else{
            showScoreb(3);
        }
        ;
    }
    public void add_bnt2(View bnt){
        if(bnt.getId()==R.id.bnt_twoa ){
            showScorea(2);
        }else{
            showScoreb(2);
        }
    }
    public void add_bnt1(View bnt){
        if(bnt.getId()==R.id.bnt_onea){
            showScorea(1);
        }else{
            showScoreb(1);
        }
    }

    public void showScorea(int inc){
        Log.i("show","incs"+inc);
        String oldScore = (String)scorea.getText();
        int newScore = Integer.parseInt(oldScore)+inc;
        scorea.setText(""+newScore);
    }

    public void showScoreb(int inc){
        Log.i("show","incs"+inc);
        String oldScore = (String)scoreb.getText();
        int newScore = Integer.parseInt(oldScore)+inc;
        scoreb.setText(""+newScore);
    }


    public void reset(View v){
        scorea.setText("0");
        scoreb.setText("0");
    }
}
